package io.narayana.compensations.mongo.common;

import io.narayana.compensations.mongo.CompensationAction;
import io.narayana.compensations.mongo.ConfirmationAction;
import io.narayana.compensations.mongo.MongoCollectionConfiguration;
import io.narayana.compensations.mongo.State;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import io.narayana.compensations.mongo.db.MongoInsertCompensationAction;
import io.narayana.compensations.mongo.db.MongoInsertConfirmationAction;
import io.narayana.compensations.mongo.db.MongoInsertState;
import io.narayana.compensations.mongo.dummy.DummyCompensationAction;
import io.narayana.compensations.mongo.dummy.DummyConfirmationAction;
import io.narayana.compensations.mongo.dummy.DummyState;
import io.narayana.compensations.mongo.internal.MongoCollectionImpl;
import io.narayana.compensations.mongo.internal.ParticipantImple;
import io.narayana.compensations.mongo.internal.TransactionManagerImpl;
import io.narayana.compensations.mongo.utils.BAControlerProducer;
import io.narayana.compensations.mongo.utils.MongoCollectionProducer;
import io.narayana.compensations.mongo.utils.TransactionManagerProducer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DeploymentHelper {

    public static JavaArchive getJavaArchive() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("services/javax.enterprise.inject.spi.Extension")
                .addAsManifestResource(getManifestDependenciesAsset(), "MANIFEST.MF");
    }

    public static WebArchive getWebArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("services/javax.enterprise.inject.spi.Extension")
                .addAsManifestResource(getManifestDependenciesAsset(), "MANIFEST.MF");
    }

    public static Class[] getBaseClasses() {
        return new Class[] {
                CompensationAction.class,
                ConfirmationAction.class,
                State.class,
                SystemException.class,
                TransactionManager.class,
                WrongStateException.class,
                ParticipantImple.class,
                TransactionManagerImpl.class,
                BAControlerProducer.class,
                TransactionManagerProducer.class
        };
    }

    public static Class[] getBaseClassesWithMongo() {
        final List<Class> classes = new ArrayList<Class>(Arrays.asList(getBaseClasses()));

        classes.add(MongoCollectionConfiguration.class);
        classes.add(MongoCollectionImpl.class);
        classes.add(MongoCollectionProducer.class);

        return classes.toArray(new Class[classes.size()]);
    }

    public static Class[] getDummyTestClasses() {
        return new Class[] {
                DummyCompensationAction.class,
                DummyConfirmationAction.class,
                DummyState.class
        };
    }

    public static Class[] getDbTestClasses() {
        return new Class[] {
                MongoInsertCompensationAction.class,
                MongoInsertConfirmationAction.class,
                MongoInsertState.class
        };
    }

    public static StringAsset getManifestDependenciesAsset() {
        final String manifestDependencies = "Dependencies: org.jboss.narayana.compensations, org.jboss.xts\n";

        return new StringAsset(manifestDependencies);
    }

    public static File getMongoArchive() {
        final String mongoDriverCoordinates = "org.mongodb:mongo-java-driver:"
                + System.getProperty("version.org.mongodb");

        return Maven.resolver().resolve(mongoDriverCoordinates).withoutTransitivity().asSingleFile();
    }

}
