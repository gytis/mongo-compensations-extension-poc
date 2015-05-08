package io.narayana.compensations.mongo.dummy;

import io.narayana.compensations.mongo.State;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DummyState implements State {

    private final String value;

    public DummyState(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
