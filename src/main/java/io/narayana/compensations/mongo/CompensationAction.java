package io.narayana.compensations.mongo;

import org.jboss.narayana.compensations.api.CompensationHandler;

import java.io.Serializable;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface CompensationAction extends CompensationHandler, Serializable {

    Object getState();

    void setState(Object state);

}
