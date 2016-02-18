package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 02/02/16.
 */
@Data
@EqualsAndHashCode(of={"uri"}, callSuper = false)
@ToString(of={"uri"}, callSuper = true)
public abstract class Edge<S extends Node,E extends Node> extends Relation{

    @GraphId
    protected Long id;

    @Index(unique = true)
    protected String uri;

    @Property
    private Double weight;

    @StartNode
    protected S startNode;

    @EndNode
    protected E endNode;

    public void setStart(Node node){
        this.startNode = (S) node;
    }

    public S getStart(){
        return this.startNode;
    }

    public void setEnd(Node node){
        this.endNode = (E) node;
    }

    public E getEnd(){
        return this.endNode;
    }

    @Override
    public String getStartUri(){
        return startNode.getUri();
    }

    @Override
    public String getEndUri(){
        return endNode.getUri();
    }

    @Override
    public Relation.Type getType(){
        return Type.ANY;
    }

}
