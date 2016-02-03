package org.epnoi.api.services;

import org.epnoi.model.domain.Resource;
import org.epnoi.model.domain.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class TopicService extends AbstractCRUDService<Topic> {

    private static final Logger LOG = LoggerFactory.getLogger(TopicService.class);

    public TopicService() {
        super(Resource.Type.TOPIC);
    }


    @Override
    public Topic create(Topic resource) throws Exception {
        throw new RuntimeException("Method not handled by Web Service");
    }

    @Override
    protected Topic save(Topic resource) {
        return null;
    }

    @Override
    protected Optional<Topic> read(String uri) {
        return udm.readTopic(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteTopic(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteTopics();
    }

    @Override
    protected List<String> findAll() {
        return udm.findTopics();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newTopic();
    }
}
