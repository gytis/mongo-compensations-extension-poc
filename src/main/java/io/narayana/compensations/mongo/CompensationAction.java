package io.narayana.compensations.mongo;

import org.jboss.narayana.compensations.api.CompensationHandler;

import java.io.Serializable;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface CompensationAction<T extends State> extends CompensationHandler, Serializable {

    T getState();

    void setState(T state);

}
