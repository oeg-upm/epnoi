package org.epnoi.storage.system.graph.edge;

import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Term;
import org.epnoi.storage.system.graph.domain.edges.AppearedInEdge;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.repository.edges.AppearedInEdgeRepository;
import org.epnoi.storage.system.graph.repository.edges.RelationGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.TermGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.UnifiedNodeGraphRepository;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class AppearedInGraphRepositoryTest extends BaseGraphRepositoryTest<AppearedInEdge> {

    private static final Logger LOG = LoggerFactory.getLogger(AppearedInGraphRepositoryTest.class);

    @Autowired
    AppearedInEdgeRepository repository;

    @Autowired
    UnifiedNodeGraphRepository nodeRepository;

    Domain domain;

    Term term;

    public void setup(){
        this.domain = Resource.newDomain();
        this.term  = Resource.newTerm();

        nodeRepository.save(domain);
        nodeRepository.save(term);
    }

    public void shutdown(){
        nodeRepository.delete(domain.getResourceType(),domain.getUri());
        nodeRepository.delete(term.getResourceType(),term.getUri());
    }

    @Override
    public RelationGraphRepository<AppearedInEdge> getRepository() {
        return repository;
    }

    @Override
    public AppearedInEdge getEntity() {
        AppearedInEdge edge = new AppearedInEdge();
        edge.setStartUri(term.getUri());
        edge.setEndUri(domain.getUri());
        return edge;
    }

}
