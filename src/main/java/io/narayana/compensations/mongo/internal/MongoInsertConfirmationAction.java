package io.narayana.compensations.mongo.internal;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.mongo.ConfirmationAction;
import org.bson.Document;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class MongoInsertConfirmationAction implements ConfirmationAction<MongoInsertState> {

    private static final Logger LOGGER = Logger.getLogger(MongoInsertConfirmationAction.class);

    private final MongoCollection<Document> collection;

    private MongoInsertState state;

    public MongoInsertConfirmationAction(final MongoInsertState state, MongoCollection<Document> collection) {
        this.state = state;
        this.collection = collection;
    }

    public MongoInsertState getState() {
        return state;
    }

    public void setState(MongoInsertState state) {
        this.state = state;
    }

    public void confirm() {
        final Document document = collection.find(state.getValue()).first();
        if (document == null) {
            return;
        }

        LOGGER.infov("Confirming document={0}", document);

        final Document filter = new Document("_id", document.get("_id"));
        final Document query = new Document("$unset", new Document("txData", null));

        collection.updateOne(filter, query);
    }

}
