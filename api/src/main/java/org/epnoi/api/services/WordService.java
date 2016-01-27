package org.epnoi.api.services;

import org.epnoi.storage.model.Resource;
import org.epnoi.storage.model.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class WordService extends AbstractCRUDService<Word> {

    private static final Logger LOG = LoggerFactory.getLogger(WordService.class);

    public WordService() {
        super(Resource.Type.WORD);
    }


    @Override
    protected Word save(Word resource) {
        udm.saveWord(resource);
        return resource;
    }

    @Override
    protected Optional<Word> read(String uri) {
        return udm.readWord(uri);
    }

    @Override
    protected void delete(String uri) {
        udm.deleteWord(uri);
    }

    @Override
    protected void deleteAll() {
        udm.deleteWords();
    }

    @Override
    protected List<String> findAll() {
        return udm.findWords();
    }

    @Override
    protected String newUri() {
        return uriGenerator.newWord();
    }
}
