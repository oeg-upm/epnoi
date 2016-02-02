package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.DomainInWord;
import org.epnoi.storage.graph.domain.relationships.PairedWord;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Word")
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = true)
@ToString(of={"uri"}, callSuper = true)
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
}
