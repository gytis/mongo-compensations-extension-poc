package io.narayana.compensations.mongo.utils;

import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.internal.TransactionManagerImpl;

import javax.enterprise.inject.Produces;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionManagerProducer {

    @Produces
    public TransactionManager produceTransactionManager() {
        return new TransactionManagerImpl();
    }

}
