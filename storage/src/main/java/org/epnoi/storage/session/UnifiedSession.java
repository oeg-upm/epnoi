package org.epnoi.storage.session;

import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 03/02/16.
 */
@Component
public class UnifiedSession {

    @Value("${epnoi.neo4j.contactpoints}")
    String neo4jHost;

    @Value("${epnoi.neo4j.port}")
    String neo4jPort;

    @Autowired
    SessionFactory neo4jSessionFactory;

    Session neo4jSession;

    @PostConstruct
    public void setup(){
        this.neo4jSession = (Neo4jSession) neo4jSessionFactory.openSession("http://"+neo4jHost+":"+neo4jPort);
    }

    public void clean(){
        this.neo4jSession.clear();
    }


    public UnifiedTransaction beginTransaction(){
        UnifiedTransaction transaction = new UnifiedTransaction();
        //TODO bug on neo4j library:
        transaction.setNeo4jTransaction(this.neo4jSession.beginTransaction());
        //TODO Add Cassandra Transaction
        //TODO Add Elasticsearch Transaction
        return transaction;
    }

}
