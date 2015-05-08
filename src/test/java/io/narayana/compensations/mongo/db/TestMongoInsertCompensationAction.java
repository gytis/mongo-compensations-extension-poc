package io.narayana.compensations.mongo.db;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.mongo.CompensationAction;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TestMongoInsertCompensationAction implements CompensationAction<TestMongoInsertState> {

    public static int INVOCATIONS_COUNTER = 0;

    private final MongoCollection collection;

    private TestMongoInsertState state;

    public TestMongoInsertCompensationAction(TestMongoInsertState state, MongoCollection collection) {
        this.state = state;

        // TODO normally collection should be injected
        this.collection = collection;
    }

    public TestMongoInsertState getState() {
        return state;
    }

    public void setState(TestMongoInsertState state) {
        this.state = state;
    }

    public void compensate() {
        INVOCATIONS_COUNTER++;

        collection.deleteOne(state.getValue());
    }

}
