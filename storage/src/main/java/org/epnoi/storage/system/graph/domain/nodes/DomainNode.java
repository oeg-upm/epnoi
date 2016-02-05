package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.edges.DomainContainsDocument;
import org.epnoi.storage.system.graph.domain.edges.SimilarDomain;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Domain")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class DomainNode extends Node {

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarDomain> domains = new HashSet<>();

    @Relationship(type = "CONTAINS", direction="OUTGOING")
    private Set<DomainContainsDocument> documents = new HashSet<>();


    public void addSimilarDomain(SimilarDomain similarDomain){
        domains.add(similarDomain);
    }

    public void addContainedDocument(DomainContainsDocument containedDocument){
        documents.add(containedDocument);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch(type){
            case DOMAIN_CONTAINS_DOCUMENT:
                documents.add((DomainContainsDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Domain Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch(type){
            case DOMAIN_CONTAINS_DOCUMENT:
                documents.remove((DomainContainsDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Domain Node");
        }
    }
}
