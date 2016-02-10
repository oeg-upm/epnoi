package org.epnoi.knowledgebase.wordnet;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.WordnetStemmer;
import lombok.Getter;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class WordNetHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WordNetHandler.class);

    //TODO use of Spring.Conditional
    @Getter
    @Value("${epnoi.knowledgeBase.wordnet.considered}")
    Boolean enabled;

    @Value("${epnoi.knowledgeBase.wordnet.dictionaryPath}")
    String wordnetPath;

    private IRAMDictionary wordNetDictionary;
    private WordnetStemmer wordnetStemmer;
    private Map<String, Set<String>> hypernyms;


    @PostConstruct
    public void setup() throws EpnoiInitializationException {
        LOG.info("Initializing the WordNetHandler with the following parameters: " + enabled + "||" + wordnetPath);
        try {
            File folder = new File(wordnetPath);
            this.wordNetDictionary = new RAMDictionary(folder, ILoadPolicy.IMMEDIATE_LOAD);
            this.wordNetDictionary.open();
        } catch (IOException e) {
            throw new EpnoiInitializationException(
                    "The WordNetHandler was not able to open the WordNet dictionary at "
                            + wordnetPath);
        }

        this.wordnetStemmer = new WordnetStemmer(this.wordNetDictionary);
        this.hypernyms = new HashMap<String, Set<String>>();
        for (String noun : getNouns()) {
            /*
			 * Set<String> nounHypernyms = wordnetHandler
			 * .getNounFirstMeaningHypernyms(noun);
			 */
            addHypernym(noun,
                    getNounFirstMeaningHypernyms(noun));

        }
    }


    public void addHypernym(String word, Set<String> wordHypernyms) {
        if (wordHypernyms.size() > 0) {

            this.hypernyms.put(word, wordHypernyms);

        }
    }

    public Set<String> getNounFirstMeaningHypernyms(String noun) {

        // We assume that the word has been stemmed
        Set<String> nounHypernyms = new HashSet<String>();

        if (noun != null) {


            IIndexWord idxWord = this.wordNetDictionary
                    .getIndexWord(noun, POS.NOUN);
            if (idxWord != null) {
                IWordID wordID = idxWord.getWordIDs().get(0); // We obtain the first
                // meaning (the fist
                // lemma that
                // belongs to
                // the more frequent
                // synset)
                IWord word = this.wordNetDictionary.getWord(wordID);
                ISynset synset = word.getSynset();
                List<ISynsetID> hypernyms = synset
                        .getRelatedSynsets(Pointer.HYPERNYM);

                List<IWord> words;
                for (ISynsetID sid : hypernyms) {
                    words = this.wordNetDictionary.getSynset(sid).getWords();
                    // System.out.print(sid + " {");
                    for (Iterator<IWord> i = words.iterator(); i.hasNext(); ) {

                        nounHypernyms.add(i.next().getLemma());
                    }

                }
            }
        }
        return nounHypernyms;

    }


    public String stemNoun(String noun) {
        try {
            List<String> stemmerResult = this.wordnetStemmer.findStems(noun,
                    POS.NOUN);
            if (stemmerResult.size() > 0) {
                return this.wordnetStemmer.findStems(noun, POS.NOUN).get(0);
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<String> getNouns() {
        List<String> nouns = new ArrayList<String>();
        for (Iterator<IIndexWord> i = this.wordNetDictionary
                .getIndexWordIterator(POS.NOUN); i.hasNext(); ) {
            IIndexWord wid = i.next();
            nouns.add(wid.getLemma());
        }
        return nouns;
    }

}
