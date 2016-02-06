package org.epnoi.storage.system.graph.domain.nodes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.edges.SourceProvidesDocument;
import org.epnoi.storage.system.graph.domain.edges.SourceComposesDomain;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Source")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class SourceNode extends Node {

    @Relationship(type = "COMPOSES", direction="OUTGOING")
    private Set<SourceComposesDomain> domains =  new HashSet<>();

    @Relationship(type = "PROVIDES", direction="OUTGOING")
    private Set<SourceProvidesDocument> documents =  new HashSet<>();

    public void addDocumentProvidedBySource(SourceProvidesDocument documentProvidedBySource){
        documents.add(documentProvidedBySource);
    }

    public void addDomainComposedBySource(SourceComposesDomain domainComoposedBySource){
        domains.add(domainComoposedBySource);
    }


    @Override
    public void add(Relation relation, Relation.Type type) {
        switch(type){
            case SOURCE_COMPOSES_DOMAIN:
                domains.add((SourceComposesDomain) relation);
                break;
            case SOURCE_PROVIDES_DOCUMENT:
                documents.add((SourceProvidesDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Source Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch(type){
            case SOURCE_COMPOSES_DOMAIN:
                domains.remove((SourceComposesDomain) relation);
                break;
            case SOURCE_PROVIDES_DOCUMENT:
                documents.remove((SourceProvidesDocument) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Source Node");
        }
    }

}