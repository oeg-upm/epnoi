package org.epnoi.api.services;

import org.epnoi.api.model.relations.*;
import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.Mentions;
import org.epnoi.model.domain.relations.PairsWith;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Word;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class WordService extends AbstractResourceService<Word> {

    public WordService() {
        super(Resource.Type.WORD);
    }

    // PAIRS_WITH -> Word
    public List<String> listWords(String id){
        String uri = uriGenerator.from(Resource.Type.WORD, id);
        return udm.find(Resource.Type.WORD).in(Resource.Type.WORD, uri);
    }

    public void removeWords(String id){
        String uri = uriGenerator.from(Resource.Type.WORD, id);
        udm.delete(Relation.Type.PAIRS_WITH).in(Resource.Type.WORD,uri);
    }

    public SimilarI getWords(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.WORD, startId);
        String endUri       = uriGenerator.from(Resource.Type.WORD, endId);
        Optional<SimilarI> result = udm.find(Relation.Type.PAIRS_WITH).btw(startUri, endUri).stream().map(relation -> (PairsWith) relation).map(relation -> new SimilarI(relation.getUri(), relation.getCreationTime(), relation.getWeight(), relation.getDomain())).findFirst();
        return (result.isPresent())? result.get() : null;
    }

    public void addWords(String startId, String endId, WeightDomainI rel){
        String startUri     = uriGenerator.from(Resource.Type.WORD, startId);
        String endUri       = uriGenerator.from(Resource.Type.WORD, endId);
        PairsWith  relation = Relation.newPairsWith(startUri, endUri);
        relation.setDomain(rel.getDomain());
        relation.setWeight(rel.getWeight());
        udm.save(relation);
    }

    public void removeWords(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.WORD, startId);
        String iuri = uriGenerator.from(Resource.Type.WORD, endId);
        udm.find(Relation.Type.PAIRS_WITH).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.PAIRS_WITH).byUri(relation.getUri()));
    }


    // EMBEDDED_IN -> Domain
        public List<String> listDomains(String id){
        String uri = uriGenerator.from(Resource.Type.WORD, id);
        return udm.find(Resource.Type.DOMAIN).in(Resource.Type.WORD, uri);
    }

    public void removeDomains(String id){
        String uri = uriGenerator.from(Resource.Type.WORD, id);
        udm.delete(Relation.Type.EMBEDDED_IN).in(Resource.Type.WORD,uri);
    }

    public EmbeddedI getDomains(String startId, String endId){
        String startUri     = uriGenerator.from(Resource.Type.WORD, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOMAIN, endId);
        Optional<EmbeddedI> result = udm.find(Relation.Type.EMBEDDED_IN).btw(startUri, endUri).stream().map(relation -> (EmbeddedIn) relation).map(relation -> new EmbeddedI(relation.getUri(), relation.getCreationTime(), relation.getVector())).findFirst();
        return (result.isPresent())? result.get() : null;
    }

    public void addDomains(String startId, String endId, VectorI rel){
        String startUri     = uriGenerator.from(Resource.Type.WORD, startId);
        String endUri       = uriGenerator.from(Resource.Type.DOMAIN, endId);
        EmbeddedIn  relation = Relation.newEmbeddedIn(startUri, endUri);
        relation.setVector(rel.getVector());
        udm.save(relation);
    }

    public void removeDomains(String startId, String endId){
        String duri = uriGenerator.from(Resource.Type.WORD, startId);
        String iuri = uriGenerator.from(Resource.Type.DOMAIN, endId);
        udm.find(Relation.Type.EMBEDDED_IN).btw(startId, endId).forEach(relation -> udm.delete(Relation.Type.EMBEDDED_IN).byUri(relation.getUri()));
    }
}
