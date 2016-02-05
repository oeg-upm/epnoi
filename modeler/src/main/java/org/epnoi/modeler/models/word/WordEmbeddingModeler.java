package org.epnoi.modeler.models.word;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.domain.*;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.WordDistribution;
import org.epnoi.modeler.scheduler.ModelingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
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
            List<RegularResource> regularResources = helper.getUdm().find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN,domain.getUri()).stream().
                    map(uri -> helper.getUdm().read(Resource.Type.DOCUMENT).byUri(uri)).
                    filter(res -> res.isPresent()).map(res -> (Document) res.get()).
                    map(document -> helper.getRegularResourceBuilder().from(document.getUri(), document.getTitle(), document.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(document.getAuthoredBy()), document.getTokens())).
                    collect(Collectors.toList());

            if ((regularResources == null) || (regularResources.isEmpty()))
                throw new RuntimeException("No documents found in domain: " + domain.getUri());

            // Clean Similar relations
            helper.getUdm().delete(Relation.Type.WORD_PAIRS_WITH_WORD).in(Resource.Type.DOMAIN, domain.getUri());

            // Clean Embedded relations
            helper.getUdm().delete(Relation.Type.WORD_EMBEDDED_IN_DOMAIN).in(Resource.Type.DOMAIN, domain.getUri());

            // Create the analysis
            Analysis analysis = newAnalysis("Word-Embedding","W2V",Resource.Type.WORD.name());

            // Build W2V Model
            W2VModel model = helper.getWordEmbeddingBuilder().build(analysis.getUri(), regularResources);

            // Make relations
            // First Create
            // TODO use all words of the vocabulary instead of only the existing ones
            List<Word> words = helper.getUdm().find(Resource.Type.WORD).in(Resource.Type.DOMAIN,domain.getUri()).stream().map(uri -> helper.getUdm().read(Resource.Type.WORD).byUri(uri)).filter(res -> res.isPresent()).map(res -> (Word) res.get()).collect(Collectors.toList());
            //List<Word> words = model.getVocabulary().stream().map(word -> findOrCreateWord(word)).collect(Collectors.toList());
            // Then relate
            words.stream().forEach(word -> relateWord(word,model));

            // Save the analysis
            helper.getUdm().save(Resource.Type.ANALYSIS).with(analysis);

        }catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void relateWord(Word word, W2VModel model){
        // EMBEDDED relation
        float[] vector = model.getRepresentation(word.getLemma());
        helper.getUdm().attachFrom(word.getUri()).to(domain.getUri()).by(Relation.Type.WORD_EMBEDDED_IN_DOMAIN,RelationProperties.builder().description(Arrays.toString(vector)).build());

        // PAIRED relations
        List<WordDistribution> words = model.find(word.getLemma()).stream().filter(sim -> sim.getWeight() > helper.getSimilarityThreshold()).collect(Collectors.toList());
        for (WordDistribution wordDistribution : words){
            List<String> result = helper.getUdm().find(Resource.Type.WORD).by(Word.LEMMA, wordDistribution.getWord());
            if (result != null && !result.isEmpty()){
                helper.getUdm().attachFrom(word.getUri()).to(result.get(0)).by(Relation.Type.WORD_PAIRS_WITH_WORD,RelationProperties.builder().weight(wordDistribution.getWeight()).domain(domain.getUri()).build());

            }
            // TODO Create word when not exist
        }

    }


}
