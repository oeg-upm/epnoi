package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.DealsWithFromPart;
import org.epnoi.model.domain.relations.Relation;
import org.neo4j.ogm.session.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsPartGraphQuery implements GraphQuery<DealsWithFromPart> {

    private static final Logger LOG = LoggerFactory.getLogger(DealsPartGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.DEALS_WITH_FROM_PART;
    }

    @Override
    public List<DealsWithFromPart> execute(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.execute("match (node1:Part{uri:{0}})-[r:DEALS_WITH]->(node2:Topic{uri:{1}}) return r", params);

        List<DealsWithFromPart> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                DealsWithFromPart similarTo = new DealsWithFromPart();
                BeanUtils.populate(similarTo,values);
                similarTo.setStartUri(startUri);
                similarTo.setEndUri(endUri);
                similars.add(similarTo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting deals_with relation between: " + startUri +" and " + endUri,e);
            }
        }
        return similars;
    }
}
