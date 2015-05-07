package io.narayana.compensations.mongo;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class WrongStateException extends Exception {

    public WrongStateException() {
        super();
    }

    public WrongStateException(final String message) {
        super(message);
    }

    public WrongStateException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
