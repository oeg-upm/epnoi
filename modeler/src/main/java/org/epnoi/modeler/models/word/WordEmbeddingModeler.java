package org.epnoi.modeler.models.word;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Domain;
import org.epnoi.model.domain.Word;
import org.epnoi.modeler.scheduler.ModelingTask;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.WordDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 11/01/16.
 */
public class WordEmbeddingModeler extends ModelingTask {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingModeler.class);

    public WordEmbeddingModeler(Domain domain, ModelingHelper modelingHelper) {
        super(domain, modelingHelper);
    }


    @Override
    public void run() {

        // TODO use a factory to avoid this explicit flow

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
            helper.getUdm().deleteSimilarsBetweenWordsInDomain(domain.getUri());

            // Clean Embedded relations
            helper.getUdm().deleteEmbeddingWordsInDomain(domain.getUri());

            // Create the analysis
            Analysis analysis = newAnalysis("Word-Embedding","W2V",Resource.Type.WORD.name());

            // Build W2V Model
            W2VModel model = helper.getWordEmbeddingBuilder().build(analysis.getUri(), regularResources);

            // Make relations
            //TODO Improve using Spark.parallel
            // First Create
            // TODO use all words of the vocabulary instead of only the existing ones
            List<Word> words = helper.getUdm().findWordsByDomain(domain.getUri()).stream().map(uri -> helper.getUdm().readWord(uri)).filter(res -> res.isPresent()).map(res -> res.get()).collect(Collectors.toList());
            //List<Word> words = model.getVocabulary().stream().map(word -> findOrCreateWord(word)).collect(Collectors.toList());
            // Then relate
            words.stream().forEach(word -> relateWord(word,model));

            // Save the analysis
            helper.getUdm().saveAnalysis(analysis);

        }catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void relateWord(Word word, W2VModel model){
        // EMBEDDED relation
        float[] vector = model.getRepresentation(word.getLemma());
        helper.getUdm().relateWordToDomain(word.getUri(),domain.getUri(),Arrays.toString(vector));

        // PAIRED relations
        List<WordDistribution> words = model.find(word.getLemma()).stream().filter(sim -> sim.getWeight() > helper.getSimilarityThreshold()).collect(Collectors.toList());
        for (WordDistribution wordDistribution : words){
            Optional<String> wordUri = helper.getUdm().findWordByLemma(wordDistribution.getWord());
            if (wordUri.isPresent()){
                helper.getUdm().relateWordToWord(word.getUri(),wordUri.get(),wordDistribution.getWeight(),domain.getUri());
            }
            // TODO Create word when not exist
        }

    }


    private Word findOrCreateWord(String word){
        Word wordData = new Word();
        wordData.setContent(word);
        wordData.setLemma(word);
        wordData.setType("term");

        Optional<String> result = helper.getUdm().findWordByLemma(word);
        if (!result.isPresent()){
            wordData.setUri(helper.getUriGenerator().newWord());
            wordData.setCreationTime(helper.getTimeGenerator().getNowAsISO());
            helper.getUdm().saveWord(wordData);
            LOG.info("Word created from modeler: " + word);
        }else{
            wordData.setUri(result.get());
        }
        return wordData;
    }



}
