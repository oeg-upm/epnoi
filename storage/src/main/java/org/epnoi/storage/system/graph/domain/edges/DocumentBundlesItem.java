package org.epnoi.storage.system.graph.domain.edges;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.RelationProperties;
import org.epnoi.model.domain.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="BUNDLES")
@Data
@EqualsAndHashCode(of={"document","item"},callSuper = false)
@ToString(of = {"id"},callSuper = true)
public class DocumentBundlesItem extends Edge {

    @GraphId
    private Long id;

    @StartNode
    private DocumentNode document;

    @EndNode
    private ItemNode item;

    private String date;

    @Override
    public void setStart(Resource start) {
        this.document = (DocumentNode) start;
    }

    @Override
    public void setEnd(Resource end) {
        this.item = (ItemNode) end;
    }

    @Override
    public Resource getStart() {
        return document;
    }

    @Override
    public Resource getEnd() {
        return item;
    }

    @Override
    public void setProperties(RelationProperties properties) {
        this.date = properties.getDate();
    }

    @Override
    public Double getWeight() {
        return 0.0;
    }
}
