package io.narayana.compensations.mongo.integration;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.mongo.MongoCollectionConfiguration;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import io.narayana.compensations.mongo.common.DeploymentHelper;
import io.narayana.compensations.mongo.db.TestMongoInsertCompensationAction;
import io.narayana.compensations.mongo.db.TestMongoInsertConfirmationAction;
import io.narayana.compensations.mongo.db.TestMongoInsertState;
import org.bson.Document;
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
    private MongoCollection<Document> collection;

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

        TestMongoInsertConfirmationAction.INVOCATIONS_COUNTER = 0;
        TestMongoInsertCompensationAction.INVOCATIONS_COUNTER = 0;
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
        final TestMongoInsertState state = new TestMongoInsertState(new Document("value", UUID.randomUUID()));
        final TestMongoInsertConfirmationAction confirmationAction = new TestMongoInsertConfirmationAction(state);
        final TestMongoInsertCompensationAction compensationAction = new TestMongoInsertCompensationAction(state, collection);

        transactionManager.begin();
        transactionManager.register(confirmationAction, compensationAction);
        collection.insertOne(state.getValue());

        Document document = collection.find(state.getValue()).first();
        Assert.assertEquals(transactionManager.getTxData(), document.get("txData"));

        if (success) {
            transactionManager.close();
        } else {
            transactionManager.cancel();
        }

        Assert.assertEquals(success ? 1 : 0, TestMongoInsertConfirmationAction.INVOCATIONS_COUNTER);
        Assert.assertEquals(success ? 0 : 1, TestMongoInsertCompensationAction.INVOCATIONS_COUNTER);

        document = collection.find(state.getValue()).first();
        if (success) {
            Assert.assertNotNull("Document should exist", document);
            Assert.assertNull("TxData should not be available", document.get("txData"));
        } else {
            Assert.assertNull("Document should not exist", document);
        }
    }

    private void cleanCollection() {
        for (final Document document : collection.find()) {
            collection.deleteOne(document);
        }
    }
}
