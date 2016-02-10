package org.epnoi.knowledgebase;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.model.RelationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KnowledgeBase {

    private static final Logger LOG = LoggerFactory.getLogger(KnowledgeBase.class);

    private static Set<String> ambiguousTerms = new HashSet<String>(Arrays.asList("film", "taxon", "band", "song", "album","episode","magazine","human"));
    
    WordNetHandler wordNetHandler;
    WikidataHandler wikidataHandler;

    boolean considerWordNet;
    boolean considerWikidata;



    public KnowledgeBase(WordNetHandler wordNetHandler, WikidataHandler wikidataHandler) {
        this.wordNetHandler = wordNetHandler;
        this.wikidataHandler = wikidataHandler;
        this.considerWordNet = true;
        this.considerWikidata = true;
    }


    public void init(boolean wikidataEnabled, boolean wordNetEnabled) {
        this.considerWikidata = wikidataEnabled;
        this.considerWordNet = wordNetEnabled;
    }

    public boolean areRelated(String source, String target, String type) {
        LOG.info("Wordnet " + this.considerWordNet + " Wikidata " + this.considerWikidata);
        if (RelationHelper.HYPERNYMY.equals(type) && (source.length() > 0) && (target.length() > 0)) {
            boolean areRelatedInWikidata = false;
            if (this.considerWikidata) {
                areRelatedInWikidata = (areRelatedInWikidata(source, target));
            }
            boolean areRelatedInWordNet = false;
            if (this.considerWordNet) {
                areRelatedInWordNet = (areRelatedInWordNet(source, target));
            }
            return (areRelatedInWikidata || areRelatedInWordNet);
        }
        return false;
    }

    public boolean areRelatedInWordNet(String source, String target) {

        String stemmedSource = this.wordNetHandler.stemNoun(source);
        stemmedSource = (stemmedSource == null) ? stemmedSource = source : stemmedSource;
        String stemmedTarget = this.wordNetHandler.stemNoun(target);
        stemmedTarget = (stemmedTarget == null) ? stemmedTarget = source : stemmedTarget;
        LOG.info(">> stemmedSource " + stemmedSource);
        Set<String> sourceHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(stemmedSource);
        LOG.info(">> stemmedSourceHypernyms " + sourceHypernyms);
        return (sourceHypernyms != null && sourceHypernyms.contains(stemmedTarget));

    }

    public boolean areRelatedInWikidata(String source, String target) {
        LOG.info("> " + source + " " + target);
        source = source.toLowerCase();
        target = target.toLowerCase();

        String stemmedSource = this.wikidataHandler.stem(source);
        LOG.info(">> stemmedSource " + stemmedSource);
        String stemmedTarget = this.wikidataHandler.stem(target);
        LOG.info(">> stemmedTarget " + stemmedTarget);
        Set<String> stemmedSourceHypernyms = this.wikidataHandler.getRelated(stemmedSource, RelationHelper.HYPERNYMY);
        Set<String> sourceHypernyms = this.wikidataHandler.getRelated(source, RelationHelper.HYPERNYMY);
        LOG.info(">> stemmedSourceHypernyms " + stemmedSourceHypernyms);

        LOG.info(">> sourceHypernyms " + sourceHypernyms);

        sourceHypernyms.addAll(stemmedSourceHypernyms);

        return (sourceHypernyms != null
                && (sourceHypernyms.contains(stemmedTarget) || sourceHypernyms.contains(target)));

    }


    public Set<String> getHypernyms(String source) {

        Set<String> hypernyms = new HashSet<String>();
        if (this.considerWikidata) {

            Set<String> wikidataHypernyms = this.wikidataHandler.getRelated(source, RelationHelper.HYPERNYMY);
            hypernyms.addAll(wikidataHypernyms);

            String stemmedSource = this.wikidataHandler.stem(source);
            wikidataHypernyms = this.wikidataHandler.getRelated(stemmedSource, RelationHelper.HYPERNYMY);

            hypernyms.addAll(wikidataHypernyms);

        }
        if (this.considerWordNet) {
            String stemmedSource = this.wordNetHandler.stemNoun(source);

            Set<String> wordNetHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(stemmedSource);
            hypernyms.addAll(wordNetHypernyms);
        }
        hypernyms.remove(source);
        hypernyms.removeAll(ambiguousTerms);
        return hypernyms;
    }


    public Set<String> stem(String term) {


        Set<String> stemmedTerm = new HashSet<String>();
        if (this.considerWordNet) {
            String wordNetStemmedTerm = this.wordNetHandler.stemNoun(term);
            if (_test(wordNetStemmedTerm)) {
                stemmedTerm.add(wordNetStemmedTerm);
            }
        }
        if (this.considerWikidata) {
            String wikidataStemmedTerm = this.wikidataHandler.stem(term);
            if (_test(wikidataStemmedTerm)) {
                stemmedTerm.add(wikidataStemmedTerm);
            }
        }
        return stemmedTerm;
    }

    private boolean _test(String term) {
        if (term != null && term.length() > 1) {
            String result = term.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "");
            return result.length() > 2;
        }
        return false;
    }

}
