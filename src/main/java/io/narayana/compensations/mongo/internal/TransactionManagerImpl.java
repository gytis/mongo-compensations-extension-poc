package io.narayana.compensations.mongo.internal;

import io.narayana.compensations.mongo.CompensationAction;
import io.narayana.compensations.mongo.ConfirmationAction;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.api.CompensationHandler;
import org.jboss.narayana.compensations.api.CompensationManager;
import org.jboss.narayana.compensations.api.ConfirmationHandler;
import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.narayana.compensations.impl.ParticipantManager;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import java.util.Stack;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Vetoed
public class TransactionManagerImpl implements TransactionManager {

    private static final Logger LOGGER = Logger.getLogger(TransactionManagerImpl.class);

    /**
     * TODO this is just a demonstration. Such list wouldn't work in highly concurrent environment.
     */
    private static final Stack<ParticipantManager> participantManagers = new Stack<ParticipantManager>();

    @Inject
    private CompensationManager compensationManager;

    @Inject
    private BAControler baController;

    TransactionManagerImpl() {
        // Should only be used by CDI
    }

    /**
     *
     * @throws WrongStateException if transaction already exists
     * @throws SystemException if transaction failed to start
     */
    public void begin() throws WrongStateException, SystemException {
        if (baController.isBARunning()) {
            throw new WrongStateException("Transaction is already running");
        }

        participantManagers.clear();

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
        if (!baController.isBARunning()) {
            throw new WrongStateException("Transaction is not running");
        }

        try {
            closeAllParticipants();
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
        if (!baController.isBARunning()) {
            throw new WrongStateException("Transaction is not running");
        }

        try {
            closeAllParticipants();
            baController.cancelBusinessActivity();
        } catch (final Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     *
     * @param confirmationAction
     * @param compensationAction
     * @throws WrongStateException if transaction is not running
     * @throws SystemException if enlist fails
     */
    public void register(final ConfirmationAction confirmationAction, final CompensationAction compensationAction)
            throws WrongStateException, SystemException {

        if (!baController.isBARunning()) {
            throw new WrongStateException("Transaction is not running");
        }

        try {
            enlist(confirmationAction);
            enlist(compensationAction);
        } catch (final Exception e) {
            compensationManager.setCompensateOnly();
            throw new SystemException(e.getMessage(), e);
        }
    }

    public Object getTxData() {
        return null;
    }

    private void saveState() {

    }

    private void removeState() {

    }

    private void enlist(final Object handler) throws Exception {
        final ParticipantManager participantManager;

        if (handler instanceof CompensationHandler) {
            participantManager = baController.enlist(((CompensationHandler) handler).getClass(), null, null);
        } else if (handler instanceof  ConfirmationAction) {
            participantManager = baController.enlist(null, ((ConfirmationHandler) handler).getClass(), null);
        } else {
            return;
        }

        participantManagers.push(participantManager);
    }

    private void closeAllParticipants() {
        while (!participantManagers.empty()) {
            final ParticipantManager participantManager = participantManagers.pop();
            try {
                participantManager.completed();
            } catch (final Exception e) {
                compensationManager.setCompensateOnly();
            }
        }
    }

}
