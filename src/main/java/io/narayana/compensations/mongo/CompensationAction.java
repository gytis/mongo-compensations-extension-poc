package io.narayana.compensations.mongo;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface CompensationAction {

    void compensate(Object state);

    Object getState();

}
