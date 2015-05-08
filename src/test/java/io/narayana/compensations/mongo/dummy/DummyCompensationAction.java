package io.narayana.compensations.mongo.dummy;

import io.narayana.compensations.mongo.CompensationAction;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DummyCompensationAction implements CompensationAction<DummyState> {

    public static int INVOCATIONS_COUNTER = 0;

    private DummyState state;

    public DummyCompensationAction(DummyState state) {
        this.state = state;
    }

    public DummyState getState() {
        return state;
    }

    public void setState(DummyState state) {
        this.state = state;
    }

    public void compensate() {
        INVOCATIONS_COUNTER++;
        state = new DummyState("compensated");
    }
}
