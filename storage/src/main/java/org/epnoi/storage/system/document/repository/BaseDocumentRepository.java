package org.epnoi.storage.system.document.repository;

import org.epnoi.model.domain.resources.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface BaseDocumentRepository<T extends Resource> extends ElasticsearchRepository<T, String> {
}
