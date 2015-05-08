package io.narayana.compensations.mongo.internal;

import com.arjuna.ats.arjuna.state.InputObjectState;
import com.arjuna.ats.arjuna.state.OutputObjectState;
import com.arjuna.mw.wscf.model.sagas.participants.Participant;
import io.narayana.compensations.mongo.CompensationAction;
import io.narayana.compensations.mongo.ConfirmationAction;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class ParticipantImple implements Participant {

    private final static Logger LOGGER = Logger.getLogger(ParticipantImple.class);

    private final String participantId;

    private final ConfirmationAction confirmationAction;

    private final CompensationAction compensationAction;

    public ParticipantImple(final String participantId, final ConfirmationAction confirmationAction,
            final CompensationAction compensationAction) {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.tracef("Creating new participant: participantId={0}, confirmationAction={1}, compensationAction={2}",
                    participantId, confirmationAction, compensationAction);
        }

        this.participantId = participantId;
        this.confirmationAction = confirmationAction;
        this.compensationAction = compensationAction;
    }

    public void close() {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.tracef("Closing participant: participantId={0}, confirmationAction={1}, compensationAction={2}",
                    participantId, confirmationAction, compensationAction);
        }

        if (confirmationAction != null) {
            confirmationAction.confirm();
        }
    }

    public void cancel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.tracef("Canceling participant: participantId={0}, confirmationAction={1}, compensationAction={2}",
                    participantId, confirmationAction, compensationAction);
        }
    }

    public void compensate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.tracef("Compensating participant: participantId={0}, confirmationAction={1}, compensationAction={2}",
                    participantId, confirmationAction, compensationAction);
        }

        if (compensationAction != null) {
            compensationAction.compensate();
        }
    }

    public void forget() {

    }

    public String id() {
        return participantId;
    }

    public boolean save_state(OutputObjectState os) {
        return true;
    }

    public boolean restore_state(InputObjectState os) {
        return true;
    }
}
