package io.narayana.compensations.mongo.internal;

import io.narayana.compensations.mongo.CompensationAction;
import io.narayana.compensations.mongo.ConfirmationAction;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.narayana.compensations.impl.BAControllerFactory;

import javax.enterprise.inject.Vetoed;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Vetoed
public class TransactionManagerImpl implements TransactionManager {

    /**
     *
     * @throws WrongStateException if transaction already exists
     * @throws SystemException if transaction failed to start
     */
    public void begin() throws WrongStateException, SystemException {
        final BAControler baController = BAControllerFactory.getInstance();

        if (baController.isBARunning()) {
            throw new WrongStateException("Transaction is already running");
        }

        try {
            baController.beginBusinessActivity();
        } catch (final Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     *
     * @throws WrongStateException if transaction is not running
     * @throws SystemException if close failed
     */
    public void close() throws WrongStateException, SystemException {
        final BAControler baController = BAControllerFactory.getInstance();

        if (!baController.isBARunning()) {
            throw new WrongStateException("Transaction is not running");
        }

        try {
            baController.closeBusinessActivity();
        } catch (final Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     *
     * @throws WrongStateException if transaction is not running
     * @throws SystemException if cancel failed
     */
    public void cancel() throws WrongStateException, SystemException {
        final BAControler baController = BAControllerFactory.getInstance();

        if (!baController.isBARunning()) {
            throw new WrongStateException("Transaction is not running");
        }

        try {
            baController.cancelBusinessActivity();
        } catch (final Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    public void register(ConfirmationAction confirmationAction, CompensationAction compensationAction) {

    }

    public Object getTxData() {
        return null;
    }

    private void saveState() {

    }

    private void removeState() {

    }

}
