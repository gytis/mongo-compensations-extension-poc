package io.narayana.compensations.mongo.internal.io.narayana.compensations.mongo.utils;

import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.narayana.compensations.impl.BAControllerFactory;

import javax.enterprise.inject.Produces;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class BAControlerProducer {

    @Produces
    public BAControler produceBAControler() {
        return BAControllerFactory.getInstance();
    }

}
