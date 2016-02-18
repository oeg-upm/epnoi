package org.epnoi.storage;

import lombok.Getter;
import org.epnoi.model.modules.EventBus;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.session.UnifiedSession;
import org.epnoi.storage.system.column.repository.UnifiedColumnRepository;
import org.epnoi.storage.system.document.repository.UnifiedDocumentRepository;
import org.epnoi.storage.system.graph.repository.edges.UnifiedEdgeGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 04/02/16.
 */
@Component
public class Helper {

    @Autowired @Getter
    UnifiedColumnRepository unifiedColumnRepository;

    @Autowired @Getter
    UnifiedDocumentRepository unifiedDocumentRepository;

    @Autowired @Getter
    UnifiedNodeGraphRepository unifiedNodeGraphRepository;

    @Autowired @Getter
    UnifiedEdgeGraphRepository unifiedEdgeGraphRepository;

    @Autowired @Getter
    UnifiedSession session;

    @Autowired @Getter
    EventBus eventBus;

    @Autowired @Getter
    URIGenerator uriGenerator;

}
