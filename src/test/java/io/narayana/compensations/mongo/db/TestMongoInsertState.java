package io.narayana.compensations.mongo.db;

import io.narayana.compensations.mongo.State;
import org.bson.Document;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TestMongoInsertState implements State {

    private final Document value;

    public TestMongoInsertState(final Document value) {
        this.value = value;
    }

    public Document getValue() {
        return value;
    }

}
