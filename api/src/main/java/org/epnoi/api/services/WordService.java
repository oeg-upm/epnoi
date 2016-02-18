package org.epnoi.api.services;

import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Word;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class WordService extends AbstractCRUDService<Word> {

    public WordService() {
        super(Resource.Type.WORD);
    }
}
