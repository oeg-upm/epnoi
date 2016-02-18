package org.epnoi.learner.terms;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import org.apache.commons.lang.StringUtils;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;

import java.util.*;

public class TermCandidateBuilder {
    private final String symbolPatter = "[^\\w\\s]";
    private final LearningHelper helper;

    private Document document;

    static final Comparator<Annotation> ANNOTATION_ORDER = new Comparator<Annotation>() {
        public int compare(Annotation annotation1, Annotation annotation2) {
            return annotation1.getStartNode().getOffset()
                    .compareTo(annotation2.getStartNode().getOffset());
        }
    };

    // ------------------------------------------------------------------------------------------------------------

    public TermCandidateBuilder(LearningHelper helper, Document document) {
        this.helper = helper;
        this.document = document;
    }

    // ------------------------------------------------------------------------------------------------------------

    public Term buildTermCandidate(Annotation annotation) {

        Long startOffset = annotation.getStartNode().getOffset();
        Long endOffset = annotation.getEndNode().getOffset();

        Term termCandidate = _buildTermCandidateFromRange(startOffset, endOffset);

        return termCandidate;
    }

    // ------------------------------------------------------------------------------------------------------------

    public Term buildTermCandidate(OffsetRangeSelector range) {


        Long startOffset = range.getStart();
        Long endOffset = range.getEnd();


        Term termCandidate = _buildTermCandidateFromRange(startOffset, endOffset);

        return termCandidate;
    }

    private Term _buildTermCandidateFromRange(Long startOffset, Long endOffset) {

        Term termCandidate = new Term();

        AnnotationSet annotations = this.document.getAnnotations();

        ArrayList<String> words = new ArrayList<String>();

        List<Annotation> tokenAnnotations = new ArrayList<Annotation>();
        for (Annotation tokenAnnotation : annotations.get(NLPAnnotationsConstants.TOKEN, startOffset, endOffset)) {
            tokenAnnotations.add(tokenAnnotation);
        }

        Collections.sort(tokenAnnotations, ANNOTATION_ORDER);

        for (Annotation tokenAnnotation : tokenAnnotations) {
            if (!isNoise(tokenAnnotation)) {
                words.add(((String) tokenAnnotation.getFeatures().get(NLPAnnotationsConstants.TOKEN_STRING))
                        .toLowerCase());
            }
        }

        termCandidate.setContent(StringUtils.join(words," "));
        termCandidate.setLength(words.size());

        termCandidate.setContent(StringUtils.join(words," "));
        return termCandidate;
    }

    private boolean isNoise(Annotation annotation) {
        String surfaceForm = (String) annotation.getFeatures().get(NLPAnnotationsConstants.TOKEN_STRING);
        return surfaceForm.matches(this.symbolPatter);
    }

    public Term generateSubTermCandidate(String[] words) {
        Term termCandidate = new Term();
        termCandidate.setUri(helper.getUriGenerator().newFor(Resource.Type.TERM));
        termCandidate.setCreationTime(helper.getTimeGenerator().asISO());
        termCandidate.setLength(words.length);
        termCandidate.setContent(StringUtils.join(words," "));
        return termCandidate;
    }

    // ------------------------------------------------------------------------------------------------------------

    public List<Term> splitTermCandidate(Term termCandidate) {
        List<Term> termCandidates = new ArrayList<Term>();

        String[] words = termCandidate.getContent().split(" ");
        List<String[]> listSubtermsWords = _generateSubtermsWords(words);
        for (String[] subtermWords : listSubtermsWords) {
            termCandidates.add(generateSubTermCandidate(subtermWords));
        }

        return termCandidates;
    }

    // ------------------------------------------------------------------------------------------------------------

    private List<String[]> _generateSubtermsWords(String[] words) {
        ArrayList<String[]> subtermWords = new ArrayList<String[]>();
        int length = words.length;

        for (int i = 0; i < length; i++) {
            for (int j = i; j < length; j++) {

                String[] aux = Arrays.copyOfRange(words, i, j + 1);
                if (aux.length < length) {
                    subtermWords.add(aux);
                }
            }

        }

        return subtermWords;
    }
}
