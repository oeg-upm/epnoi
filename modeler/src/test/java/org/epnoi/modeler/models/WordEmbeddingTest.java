package org.epnoi.modeler.models;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.modeler.Config;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.word.W2VModel;
import org.epnoi.storage.generator.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.model.domain.Domain;
import org.epnoi.model.domain.Word;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 26/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.modeler.delay = 200000"})
public class WordEmbeddingTest {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingTest.class);

    @Autowired
    ModelingHelper helper;


    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    UDM udm;

    @Autowired
    WordDocumentRepository wordDocumentRepository;

    @Test
    public void validation(){

        String dUri = "http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5";

        List<String> words = udm.findWordsByDomain(dUri);

        words.stream().map(uri -> wordDocumentRepository.findOne(uri)).forEach(wd -> System.out.println("WordDOcument: " + wd.getDomain()));

    }


    @Test
    public void pair(){

        String dUri = "http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5";
        Optional<Domain> result = udm.readDomain(dUri);

        if (!result.isPresent()){
            LOG.error("Domain not found: " + dUri);
        }

        Domain domain = result.get();

        try{
            //TODO Optimize using Spark.parallel
            List<RegularResource> regularResources = helper.getUdm().findDocumentsByDomain(domain.getUri()).stream().
                    map(uri -> helper.getUdm().readDocument(uri)).
                    filter(res -> res.isPresent()).map(res -> res.get()).
                    map(document -> helper.getRegularResourceBuilder().from(document.getUri(), document.getTitle(), document.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(document.getAuthoredBy()), document.getTokens())).
                    collect(Collectors.toList());

            if ((regularResources == null) || (regularResources.isEmpty()))
                throw new RuntimeException("No documents found in domain: " + domain.getUri());


            // Clean Similar relations
//            helper.getUdm().deleteSimilarsBetweenWordsInDomain(domain.getUri());

            // Clean Embedded relations
//            helper.getUdm().deleteEmbeddingWordsInDomain(domain.getUri());

            String analysisUri = uriGenerator.newAnalysis();


            // Build W2V Model
            W2VModel model = helper.getWordEmbeddingBuilder().build(analysisUri, regularResources);

            List<Word> words = helper.getUdm().findWordsByDomain(domain.getUri()).stream().map(uri -> helper.getUdm().readWord(uri)).filter(res -> res.isPresent()).map(res -> res.get()).collect(Collectors.toList());

            LOG.info("Number of words: " + words.size());

            // Then relate
            words.stream().forEach(word -> relateWord(word,model,dUri));

        }catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }

    }


    private void relateWord(Word word, W2VModel model, String domainUri){
        // EMBEDDED relation
        float[] vector = model.getRepresentation(word.getLemma());
        helper.getUdm().relateWordToDomain(word.getUri(),domainUri, Arrays.toString(vector));

        // PAIRED relations
        List<WordDistribution> words = model.find(word.getLemma()).stream().filter(sim -> sim.getWeight() > helper.getSimilarityThreshold()).collect(Collectors.toList());
        for (WordDistribution wordDistribution : words){
            Optional<String> result = helper.getUdm().findWordByLemma(wordDistribution.getWord());
            String wordUri;
            if (!result.isPresent()){
                // Create word
                Word wordRef = new Word();
                wordRef.setUri(uriGenerator.newWord());
                wordRef.setCreationTime(timeGenerator.getNowAsISO());
                wordRef.setContent(wordDistribution.getWord());
                wordRef.setLemma(wordDistribution.getWord());
                wordRef.setType("term");
                udm.saveWord(wordRef);

                wordUri = wordRef.getUri();

                // Embedd to Domain
                udm.relateWordToDomain(wordRef.getUri(),domainUri,Arrays.toString(model.getRepresentation(wordRef.getLemma())));

            }else{
                wordUri = result.get();
            }

            // Pair words
            helper.getUdm().relateWordToWord(word.getUri(),wordUri,wordDistribution.getWeight(),domainUri);
        }

    }

}
