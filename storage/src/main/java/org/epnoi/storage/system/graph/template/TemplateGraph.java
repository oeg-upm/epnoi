package org.epnoi.storage.system.graph.template;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.domain.relations.EmbeddedIn;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.utils.ResourceUtils;
import org.epnoi.storage.system.graph.GraphIdFactory;
import org.epnoi.storage.system.graph.repository.edges.UnifiedEdgeGraphRepositoryFactory;
import org.neo4j.ogm.model.QueryStatistics;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by cbadenes on 28/02/16.
 */
public abstract class TemplateGraph<T extends Relation> {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateGraph.class);

    private final Relation.Type type;

    @Autowired
    TemplateExecutor executor;

    @Autowired
    GraphIdFactory graphIdFactory;

    @Autowired
    UnifiedEdgeGraphRepositoryFactory unifiedEdgeGraphRepositoryFactory;


    public TemplateGraph(Relation.Type type){
        this.type = type;
    }


    public Relation.Type accept() {
        return type;
    }

    protected abstract String simplePath();

    protected abstract String byDomainPath();

    protected abstract TemplateParameters paramsFrom(Relation relation);

    public List<T> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Optional<Result> result = executor.query("match "+ simplePath()+" return r,s,e", params);

        if (!result.isPresent()) return Collections.EMPTY_LIST;

        List<T> res = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(result.get().queryResults().iterator(), Spliterator.ORDERED), false).
                map(x -> unifiedEdgeGraphRepositoryFactory.mappingOf(type).cast(x.get("r"))).
                map(edge -> (T) ResourceUtils.map(edge, Relation.classOf(type))).
                collect(Collectors.toList());
        return res;
    }

    public List<T> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Optional<Result> result = executor.query("match "+byDomainPath()+" return r,s,e", params);

        if (!result.isPresent()) return Collections.EMPTY_LIST;

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(result.get().queryResults().iterator(), Spliterator.ORDERED), false).
                map(x -> unifiedEdgeGraphRepositoryFactory.mappingOf(type).cast(x.get("r"))).
                map(edge -> (T) ResourceUtils.map(edge,Relation.classOf(type))).
                collect(Collectors.toList());
    }

    public void deleteIn(Resource.Type type, String uri){

        Map<String,Object> params = new HashMap<>();
        params.put("0",uri);

        String query;
        switch (type){
            case DOMAIN:
                query = "match "+byDomainPath()+" delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }

    public List<String> findAll() {
        String relationLabel    = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type.key());
        Optional<Result> result = executor.query("match (s)-[r:"+relationLabel+"]->(e) return r.uri", new HashMap<>());

        if (!result.isPresent()) return Collections.EMPTY_LIST;

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(result.get().queryResults().iterator(), Spliterator.ORDERED), false).
                map(x -> (String) x.get("r.uri")).
                collect(Collectors.toList());
    }

    public void save(Relation relation) {
        String relationLabel    = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type.key());
        String startNodeLabel   = StringUtils.capitalize(relation.getStartType().key());
        String endNodeLabel     = StringUtils.capitalize(relation.getEndType().key());

        TemplateParameters parameters = paramsFrom(relation);
        LOG.trace("Trying to create "+type.name()+" relation");
        try{
            String extraParams = Strings.isNullOrEmpty(parameters.toExpression())? "": ","+parameters.toExpression();
            QueryStatistics result = executor.execute("MATCH (a:"+startNodeLabel+"),(b:+"+endNodeLabel+") WHERE a.uri = {0} AND b.uri = {1} CREATE (a)-[r:"+relationLabel+" { uri : {2}, creationTime : {3}, weight : {4}, "+extraParams+" } ]->(b) RETURN r", parameters.getParams());
            LOG.debug("created "+type.name()+" relation: -> " +result.getRelationshipsCreated());
        }catch (Exception e){
            LOG.error("Error on "+type.name(),e);
        }
    }

}
