package io.narayana.compensations.mongo.utils;

import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.internal.TransactionManagerImpl;
import org.jboss.logging.Logger;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionManagerProducer {

    private static final Logger LOGGER = Logger.getLogger(TransactionManagerProducer.class);

    @Produces
    public TransactionManager produceTransactionManager(@New TransactionManagerImpl transactionManager) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Producing TransactionManager instance");
        }

        return transactionManager;
    }

}
