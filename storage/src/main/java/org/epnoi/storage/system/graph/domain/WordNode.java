package org.epnoi.storage.system.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.Relation;
import org.epnoi.storage.system.graph.domain.relationships.DomainInWord;
import org.epnoi.storage.system.graph.domain.relationships.PairedWord;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Word")
@Data
@EqualsAndHashCode(of = "uri",callSuper = true)
@ToString(callSuper = true)
public class WordNode extends Node {

    private String content;

    @Relationship(type = "PAIRS_WITH", direction="UNDIRECTED")
    private Set<PairedWord> words = new HashSet<>();

    @Relationship(type = "EMBEDDED_IN", direction="OUTGOING")
    private Set<DomainInWord> domains = new HashSet<>();

    public void addPairedWord(PairedWord pairedWord){
        words.add(pairedWord);
    }

    public void addDomainInWord(DomainInWord domainInWord){
        domains.add(domainInWord);
    }

    @Override
    public void add(Relation relation, Relation.Type type) {
        switch(type){
            case WORD_EMBEDDED_IN_DOMAIN:
                domains.add((DomainInWord) relation);
                break;
            case WORD_PAIRS_WITH_WORD:
                words.add((PairedWord) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Word Node");
        }
    }

    @Override
    public void remove(Relation relation, Relation.Type type) {
        switch(type){
            case WORD_EMBEDDED_IN_DOMAIN:
                domains.remove((DomainInWord) relation);
                break;
            case WORD_PAIRS_WITH_WORD:
                words.remove((PairedWord) relation);
                break;
            default: throw new RuntimeException( "Relation " + type + " not handled from Word Node");
        }
    }
}
