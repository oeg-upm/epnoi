package org.epnoi.storage.system.document.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.model.domain.resources.Analysis;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="analyses")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AnalysisDocument extends Analysis{

    @Override
    public Resource.Type getResourceType() {return Type.ANALYSIS;}

    @Id
    private String uri;
}
