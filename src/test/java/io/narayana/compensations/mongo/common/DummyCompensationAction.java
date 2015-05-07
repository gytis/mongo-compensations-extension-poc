package io.narayana.compensations.mongo.common;

import io.narayana.compensations.mongo.CompensationAction;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DummyCompensationAction implements CompensationAction {

    public static int INVOCATIONS_COUNTER = 0;

    private Object state;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public void compensate() {
        INVOCATIONS_COUNTER++;
    }
}
