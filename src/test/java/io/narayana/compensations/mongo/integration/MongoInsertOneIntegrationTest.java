package io.narayana.compensations.mongo.integration;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.mongo.MongoCollectionConfiguration;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import io.narayana.compensations.mongo.common.DeploymentHelper;
import io.narayana.compensations.mongo.db.MongoInsertCompensationAction;
import io.narayana.compensations.mongo.db.MongoInsertConfirmationAction;
import io.narayana.compensations.mongo.db.MongoInsertState;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.UUID;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class MongoInsertOneIntegrationTest {

    private static final Logger LOGGER = Logger.getLogger(MongoInsertOneIntegrationTest.class);

    @Inject
    @MongoCollectionConfiguration(databaseName = "test-database", collectionName = "test-collection")
    private MongoCollection collection;

    @Inject
    private TransactionManager transactionManager;

    @Inject
    private BAControler baController;

    @Deployment
    public static Archive<?> getDeployment() {
        final WebArchive archive = DeploymentHelper.getWebArchive()
                .addAsLibraries(DeploymentHelper.getMongoArchive())
                .addClasses(DeploymentHelper.getBaseClassesWithMongo())
                .addClasses(DeploymentHelper.getDbTestClasses())
                .addClass(MongoInsertOneIntegrationTest.class);

        System.out.println("Deploying test archive: " + archive.toString(true));

        return archive;
    }

    @Before
    public void before() {
        Assert.assertNotNull("Mongo collection was not injected", collection);
        Assert.assertNotNull("Transaction manager was not injected", transactionManager);
        Assert.assertNotNull("BA controller was not injected", baController);
        Assert.assertFalse("Transaction should not be running before the test", baController.isBARunning());

        MongoInsertConfirmationAction.INVOCATIONS_COUNTER = 0;
        MongoInsertCompensationAction.INVOCATIONS_COUNTER = 0;
    }

    @After
    public void after() {
        cleanCollection();

        if (baController.isBARunning()) {
            try {
                baController.cancelBusinessActivity();
            } catch (Exception e) {
                LOGGER.warn(e);
            }
            Assert.fail("Transaction should not be running after the test");
        }
    }

    @Test
    public void shouldInsertDocumentAndConfirm() throws WrongStateException, SystemException {
        LOGGER.info(getClass().getSimpleName() + "shouldInsertDocumentAndConfirm starting");

        executeTest(true);
    }

    @Test
    public void shouldInsertDocumentAndCancel() throws WrongStateException, SystemException {
        LOGGER.info(getClass().getSimpleName() + "shouldInsertDocumentAndCancel starting");

        executeTest(false);
    }

    private void executeTest(final boolean success) throws WrongStateException, SystemException {
        final MongoInsertState state = new MongoInsertState(new Document("value", UUID.randomUUID()));
        final MongoInsertConfirmationAction confirmationAction = new MongoInsertConfirmationAction(state);
        final MongoInsertCompensationAction compensationAction = new MongoInsertCompensationAction(state, collection);

        transactionManager.begin();
        transactionManager.register(confirmationAction, compensationAction);
        collection.insertOne(state.getValue());

        if (success) {
            transactionManager.close();
        } else {
            transactionManager.cancel();
        }

        Assert.assertEquals(success ? 1 : 0, MongoInsertConfirmationAction.INVOCATIONS_COUNTER);
        Assert.assertEquals(success ? 0 : 1, MongoInsertCompensationAction.INVOCATIONS_COUNTER);
        Assert.assertEquals(success, collection.find(state.getValue()).iterator().hasNext());
    }

    private void cleanCollection() {
        for (final Object document : collection.find()) {
            if (document instanceof Bson) {
                collection.deleteOne((Bson) document);
            }
        }
    }
}
