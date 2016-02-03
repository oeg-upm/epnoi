package org.epnoi.storage.session;

import org.neo4j.ogm.session.transaction.Transaction;

/**
 * Created by cbadenes on 03/02/16.
 */
public class UnifiedTransaction {


    private Transaction neo4jTransaction;

    public void commit(){

        if (neo4jTransaction != null) neo4jTransaction.commit();
    }

    public void setNeo4jTransaction(Transaction neo4jTransaction) {
        this.neo4jTransaction = neo4jTransaction;
    }
}
