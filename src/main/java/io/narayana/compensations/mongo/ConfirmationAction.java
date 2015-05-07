package io.narayana.compensations.mongo;

import org.jboss.narayana.compensations.api.ConfirmationHandler;

import java.io.Serializable;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface ConfirmationAction extends ConfirmationHandler, Serializable {

    Object getState();

    void setState(Object state);

}
