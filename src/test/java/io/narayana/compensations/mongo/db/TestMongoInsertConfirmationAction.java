package io.narayana.compensations.mongo.db;

import io.narayana.compensations.mongo.ConfirmationAction;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TestMongoInsertConfirmationAction implements ConfirmationAction<TestMongoInsertState> {

    public static int INVOCATIONS_COUNTER = 0;

    private TestMongoInsertState state;

    public TestMongoInsertConfirmationAction(TestMongoInsertState state) {
        this.state = state;
    }

    public TestMongoInsertState getState() {
        return state;
    }

    public void setState(TestMongoInsertState state) {
        this.state = state;
    }

    public void confirm() {
        INVOCATIONS_COUNTER++;
    }

}
