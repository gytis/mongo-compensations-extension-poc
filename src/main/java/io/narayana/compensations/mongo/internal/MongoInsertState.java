package io.narayana.compensations.mongo.internal;

import io.narayana.compensations.mongo.State;
import org.bson.Document;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class MongoInsertState implements State {

    private final Document value;

    public MongoInsertState(final Document value) {
        this.value = value;
    }

    public Document getValue() {
        return value;
    }

}
