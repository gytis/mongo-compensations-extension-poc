package io.narayana.compensations.mongo.dummy;

import io.narayana.compensations.mongo.ConfirmationAction;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DummyConfirmationAction implements ConfirmationAction<DummyState> {

    public static int INVOCATIONS_COUNTER = 0;

    private DummyState state;

    public DummyConfirmationAction(DummyState state) {
        this.state = state;
    }

    public DummyState getState() {
        return state;
    }

    public void setState(DummyState state) {
        this.state = state;
    }

    public void confirm() {
        INVOCATIONS_COUNTER++;
        state = new DummyState("confirmed");
    }
}
