package org.epnoi.learner.relations.extractor;

import gate.*;
import gate.Document;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.learner.terms.TermCandidateBuilder;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.domain.relations.RelationProperties;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.relations.HypernymOf;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class RelationsExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(RelationsExtractor.class);

    private static final long MAX_DISTANCE = 20;

    private DomainsTable domainsTable;
    private TermsTable termsTable;
    private LexicalRelationalPatternGenerator patternsGenerator;
    private RelationsTable relationsTable;
    private double hypernymExtractionThreshold;

    private Domain targetDomain;

    private boolean considerKnowledgeBase = false;

    private LearningHelper helper;


    public void init(DomainsTable domainsTable, LearningHelper helper) {
        LOG.info("Initializing the Relations Extractor with the following parameters: " + helper);

        this.helper = helper;
        this.hypernymExtractionThreshold = helper.getHypernymsThresholdExtraction();
        this.targetDomain = domainsTable.getTargetDomain();

        this.considerKnowledgeBase = helper.isRelationsKnowledgeBase();

        this.patternsGenerator = new LexicalRelationalPatternGenerator();
        this.domainsTable = domainsTable;
        this.relationsTable = new RelationsTable();
    }

    public RelationsTable extract(TermsTable termsTable) {
        LOG.info("Extracting the Relations Table");

        String relationsTableUri= this.domainsTable.getTargetDomain().getUri()+"/relations";

        this.termsTable = termsTable;
        this.relationsTable = new RelationsTable();
        this.relationsTable.setUri(relationsTableUri);
        // The relations finding task is only performed in the target domain,
        // these are the resources that we should consider

        for (String documentUri : domainsTable.getDomainResources().get(domainsTable.getTargetDomain().getUri())) {
            LOG.info("Indexing the resource " + documentUri);
            _findRelationsInResource(documentUri);
        }
        return relationsTable;
    }

    private void _findRelationsInResource(String documentUri) {
        Document annotatedResourceDocument = retrieveAnnotatedDocument(documentUri);

        AnnotationSet sentenceAnnotations = annotatedResourceDocument.getAnnotations().get(NLPAnnotationsConstants.SENTENCE);

        LOG.info("There are " + sentenceAnnotations.size());

        DocumentContent sentenceContent = null;
        AnnotationSet resourceAnnotations = annotatedResourceDocument.getAnnotations();

        Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
        while (sentencesIt.hasNext()) {
            Annotation sentenceAnnotation = sentencesIt.next();

            Long sentenceStartOffset = sentenceAnnotation.getStartNode()
                    .getOffset();
            Long sentenceEndOffset = sentenceAnnotation.getEndNode()
                    .getOffset();
            TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(helper,annotatedResourceDocument);
            _testSentence(sentenceStartOffset, sentenceEndOffset,
                    annotatedResourceDocument, termCandidateBuilder);
            /*
			 * _testSentence(sentenceStartOffset, sentenceContent,
			 * annotatedResourceAnnotations.getContained( sentenceStartOffset,
			 * sentenceEndOffset));
			 */

        }

    }


    private void _testSentence(Long sentenceStartOffset,
                               Long sentenceEndOffset, Document annotatedResource,
                               TermCandidateBuilder termCandidateBuilder) {

        AnnotationSet senteceAnnotationSet = annotatedResource.getAnnotations()
                .get(sentenceStartOffset, sentenceEndOffset);
        List<Annotation> termAnnotations = new ArrayList<Annotation>();
        for (Annotation termAnnotation : senteceAnnotationSet.get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
            termAnnotations.add(termAnnotation);
        }

        String sentenceContent = null;
        try {
            sentenceContent = annotatedResource.getContent()
                    .getContent(sentenceStartOffset, sentenceEndOffset)
                    .toString();
        } catch (InvalidOffsetException e) {

            e.printStackTrace();
        }
        int combinations = 0;
        long time = System.currentTimeMillis();
        for (int i = 0; i < termAnnotations.size(); i++)
            for (int j = i + 1; j < termAnnotations.size(); j++) {
                Annotation source = termAnnotations.get(i);
                Annotation target = termAnnotations.get(j);
                if (!_areFar(source, target)) {
                    // For each pair of terms we check both as target and as
                    // source

                    _extractProbableRelationsFromSentence(source, target,
                            annotatedResource, sentenceContent,
                            termCandidateBuilder);

                    _extractProbableRelationsFromSentence(target, source,
                            annotatedResource, sentenceContent,
                            termCandidateBuilder);
                    combinations++;

                } else {
                    // System.out.println("Are far:"+source+" > "+target);
                }
            }
        // System.out.println("Sentence took "+ Math.abs(time -
        // System.currentTimeMillis())+ " consisting of "+combinations);

    }

    private boolean _areFar(Annotation source, Annotation target) {
        return (Math.abs(target.getEndNode().getOffset()
                - source.getEndNode().getOffset()) > MAX_DISTANCE);
    }

    private void _extractProbableRelationsFromSentence(Annotation source,
                                                       Annotation target, Document annotatedResource,
                                                       String sentenceContent, TermCandidateBuilder termCandidateBuilder) {
        String sourceTermWord = termCandidateBuilder.buildTermCandidate(source).getContent();
        String targetTermWord = termCandidateBuilder.buildTermCandidate(target).getContent();


        if (this.considerKnowledgeBase && helper.getKnowledgeBase().areRelated(sourceTermWord, targetTermWord, RelationHelper.HYPERNYMY)) {
            _createRelation(sentenceContent, termCandidateBuilder.buildTermCandidate(source), termCandidateBuilder.buildTermCandidate(target), 1.0);
        } else {

            List<RelationalPattern> generatedPatterns = this.patternsGenerator.generate(source, target, annotatedResource);
            for (RelationalPattern pattern : generatedPatterns) {
                double relationProbability = this.helper.getRelationalPatternsModel().calculatePatternProbability(pattern);

                if (relationProbability > this.hypernymExtractionThreshold) {

                    _createRelation(sentenceContent, sourceTermWord, targetTermWord, relationProbability);

                }
            }
        }
    }

    private void _createRelation(String sentenceContent, Term sourceTerm, Term targetTerm, double relationProbability) {

        if (sourceTerm == null || targetTerm == null){
            LOG.warn("Term is null: " + sourceTerm + " / " + targetTerm);
            return;
        }

        HypernymOf hypernymOf = new HypernymOf();
        hypernymOf.setUri(helper.getUriGenerator().newFor());
        hypernymOf.setStart(sourceTerm);
        hypernymOf.setEnd(targetTerm);
        hypernymOf.setProperties(RelationProperties.builder().domain(targetDomain.getUri()).build());
        hypernymOf.add(sentenceContent,relationProbability);
        this.relationsTable.introduceRelation(hypernymOf);


    }

    private Document retrieveAnnotatedDocument(String documentUri) {


        Optional<Resource> res = helper.getUdm().read(Resource.Type.DOCUMENT).byUri(documentUri);

        if (!res.isPresent()){
            throw new RuntimeException("No document found in DDBB by uri: " + documentUri);
        }

        // TODO Store annotated document previously in DDBB
        Document document = null;
        try {
            document = (Document) Factory.createResource( "gate.corpora.DocumentImpl",
                    Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, ((org.epnoi.model.domain.resources.Document) res.get()).getContent(), gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/plain")); //text/xml

        } catch (ResourceInstantiationException e) {
            LOG.error("Couldn't retrieve the GATE document that represents the annotated content of " + documentUri,e);
        }

        return document;
    }

}
