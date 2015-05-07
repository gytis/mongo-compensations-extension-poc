package io.narayana.compensations.mongo;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface ConfirmationAction {

    void confirm(Object state);

    Object getState();

}
