package org.epnoi.storage.document.repository;

import org.epnoi.model.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface BaseDocumentRepository<T extends Resource> extends ElasticsearchRepository<T, String> {
}
