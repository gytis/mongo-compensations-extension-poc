package io.narayana.compensations.mongo;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class SystemException extends Exception {

    public SystemException() {
        super();
    }

    public SystemException(final String message) {
        super(message);
    }

    public SystemException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
