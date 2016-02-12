package org.epnoi.storage.session;

import org.neo4j.ogm.session.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 03/02/16.
 */
public class UnifiedTransaction {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedTransaction.class);

    private Transaction neo4jTransaction;

    public void commit(){

        if (neo4jTransaction != null) {
            try{
                neo4jTransaction.commit();
            }catch (NullPointerException e){
                // NullPointerException on: java.util.concurrent.ConcurrentHashMap.putVal(ConcurrentHashMap.java:1011)
                LOG.info("Internal error synchronizing transaction. ");
            }
        }
    }

    public void setNeo4jTransaction(Transaction neo4jTransaction) {
        this.neo4jTransaction = neo4jTransaction;
    }
}
