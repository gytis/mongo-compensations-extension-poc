package io.narayana.compensations.mongo.db;

import io.narayana.compensations.mongo.ConfirmationAction;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class MongoInsertConfirmationAction implements ConfirmationAction<MongoInsertState> {

    public static int INVOCATIONS_COUNTER = 0;

    private MongoInsertState state;

    public MongoInsertConfirmationAction(MongoInsertState state) {
        this.state = state;
    }

    public MongoInsertState getState() {
        return state;
    }

    public void setState(MongoInsertState state) {
        this.state = state;
    }

    public void confirm() {
        INVOCATIONS_COUNTER++;
    }

}
