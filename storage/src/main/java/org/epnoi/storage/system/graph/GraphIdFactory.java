package org.epnoi.storage.system.graph;

import org.elasticsearch.common.netty.util.internal.ConcurrentHashMap;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by cbadenes on 04/03/16.
 */
@Component
public class GraphIdFactory {

    private static final Logger LOG = LoggerFactory.getLogger(GraphIdFactory.class);

    ConcurrentHashMap<Long,String> idUris;
    ConcurrentHashMap<String,Long> uriIds;

    AtomicLong counter;

    @Autowired
    Session session;

    @PostConstruct
    public void setup(){
        counter = new AtomicLong(0);
        idUris = new ConcurrentHashMap<>();
        uriIds = new ConcurrentHashMap<>();
    }

    public Long from(String uri){
        Long id = uriIds.get(uri);
        if (id == null){
            id = counter.getAndIncrement();
            LOG.info("Uri: " + uri + " associated to Id: " + id);
        }
        uriIds.put(uri,id);
        return id;
    }

    public String from(Long id){
        String uri = idUris.get(id);
        if (uri == null){
            LOG.warn("No uri associated to id: " + id);
        }
        return uri;
    }

    public void delete(String uri){
        idUris.remove(uriIds.get(uri));
        uriIds.remove(uri);
    }

    public void delete(Long id){
        uriIds.remove(idUris.get(id));
        idUris.remove(id);
    }

    public Long get(){
        long count = counter.getAndIncrement();
        LOG.info("Counter: " + count);
        return count;
    }
}
