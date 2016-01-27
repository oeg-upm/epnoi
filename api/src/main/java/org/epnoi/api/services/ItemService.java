package org.epnoi.api.services;

import org.epnoi.storage.model.Item;
import org.epnoi.storage.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class ItemService extends AbstractCRUDService<Item> {

    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);

    public ItemService() {
        super(Resource.Type.ITEM);
    }


    @Override
    public Item create(Item resource) throws Exception {
        throw new RuntimeException("Method not handled by Web Service");
    }

    @Override
    protected Item save(Item resource) {
        return null;
    }

    @Override
    protected Optional<Item> read(String uri) {
        return udm.readItem(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteItem(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteItems();
    }

    @Override
    protected List<String> findAll() {
        return udm.findItems();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newItem();
    }
}
