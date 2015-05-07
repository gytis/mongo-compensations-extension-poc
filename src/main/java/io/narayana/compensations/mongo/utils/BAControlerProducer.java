package io.narayana.compensations.mongo.utils;

import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.narayana.compensations.impl.BAControllerFactory;

import javax.enterprise.inject.Produces;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class BAControlerProducer {

    private static final Logger LOGGER = Logger.getLogger(BAControlerProducer.class);

    @Produces
    public BAControler produceBAControler() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Producing BAControler instance");
        }

        return BAControllerFactory.getInstance();
    }

}
