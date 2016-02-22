package org.epnoi.api.services;

import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Topic;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class TopicService extends AbstractResourceService<Topic> {

    public TopicService() {
        super(Resource.Type.TOPIC);
    }
}
