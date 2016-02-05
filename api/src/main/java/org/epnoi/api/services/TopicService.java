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

    public TopicService() {
        super(Resource.Type.TOPIC);
    }
}
