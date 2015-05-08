package io.narayana.compensations.mongo.internal;

import com.arjuna.mw.wscf.model.sagas.api.CoordinatorManager;
import com.arjuna.mw.wscf.model.sagas.participants.Participant;
import com.arjuna.mw.wscf11.model.sagas.CoordinatorManagerFactory;
import io.narayana.compensations.mongo.CompensationAction;
import io.narayana.compensations.mongo.ConfirmationAction;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.api.CompensationManager;
import org.jboss.narayana.compensations.impl.BAControler;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import java.util.Stack;
import java.util.UUID;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Vetoed
public class TransactionManagerImpl implements TransactionManager {

    private static final Logger LOGGER = Logger.getLogger(TransactionManagerImpl.class);

    /**
     * TODO such list would not work in concurent environment
     */
    private static final Stack<Participant> PARTICIPANTS = new Stack<Participant>();

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

        PARTICIPANTS.clear();

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
            enlist(confirmationAction, compensationAction);
        } catch (final Exception e) {
            compensationManager.setCompensateOnly();
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * TODO what should be returned? As participant number will increase during the transaction, returning them would
     * be inconsistent. For now, will return current transaction.
     *
     *
     * @throws SystemException if current transaction cannot be returned
     * @return Current transaction
     */
    public Object getTxData() throws SystemException {
        try {
            return baController.getCurrentTransaction();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    private void saveState() {

    }

    private void removeState() {

    }

    private void enlist(final ConfirmationAction confirmationAction, final CompensationAction compensationAction)
            throws SystemException {

        final String participantId = String.valueOf(UUID.randomUUID());
        final Participant participant = new ParticipantImple(participantId, confirmationAction, compensationAction);

        try {
            CoordinatorManagerFactory.coordinatorManager().enlistParticipant(participant);
        } catch (final Exception e) {
            throw new SystemException(e.getMessage(), e);
        }

        PARTICIPANTS.push(participant);
    }

    private void closeAllParticipants() throws SystemException {
        final CoordinatorManager coordinatorManager;

        try {
            coordinatorManager = CoordinatorManagerFactory.coordinatorManager();
        } catch (final Exception e) {
            compensationManager.setCompensateOnly();
            throw new SystemException(e.getMessage(), e);
        }

        while (!PARTICIPANTS.empty()) {
            final Participant participant = PARTICIPANTS.pop();

            try {
                coordinatorManager.participantCompleted(participant.id());
            } catch (final Exception e) {
                compensationManager.setCompensateOnly();
            }
        }
    }

}
