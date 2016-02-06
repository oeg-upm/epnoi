package org.epnoi.api.services;

import org.epnoi.model.domain.Item;
import org.epnoi.model.domain.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class ItemService extends AbstractCRUDService<Item> {

    public ItemService() {
        super(Resource.Type.ITEM);
    }

}