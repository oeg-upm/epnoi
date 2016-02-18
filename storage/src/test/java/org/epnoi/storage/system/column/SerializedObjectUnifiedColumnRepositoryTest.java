package org.epnoi.storage.system.column;

import org.epnoi.model.domain.LinkableElement;
import org.epnoi.storage.system.column.domain.SerializedObjectColumn;
import org.epnoi.storage.system.column.repository.BaseColumnRepository;
import org.epnoi.storage.system.column.repository.SerializedObjectColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SerializedObjectUnifiedColumnRepositoryTest extends BaseColumnRepositoryTest<SerializedObjectColumn> {

    @Autowired
    SerializedObjectColumnRepository repository;

    @Override
    public BaseColumnRepository<SerializedObjectColumn> getRepository() {
        return repository;
    }

    @Override
    public SerializedObjectColumn getEntity() {

        LinkableElement sample = new LinkableElement();
        sample.setUri("sample");
        sample.setCreationTime("2016");



        SerializedObjectColumn column = new SerializedObjectColumn();
        column.setUri("serializations/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setInstance(sample);
        return column;
    }

}
