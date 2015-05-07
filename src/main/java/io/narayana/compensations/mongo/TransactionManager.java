package io.narayana.compensations.mongo;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface TransactionManager {

    void begin() throws WrongStateException, SystemException;

    void close() throws WrongStateException, SystemException;

    void cancel() throws WrongStateException, SystemException;

    void register(ConfirmationAction confirmationAction, CompensationAction compensationAction);

    Object getTxData();

}
