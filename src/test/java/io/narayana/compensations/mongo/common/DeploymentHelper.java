package io.narayana.compensations.mongo.common;

import io.narayana.compensations.mongo.CompensationAction;
import io.narayana.compensations.mongo.ConfirmationAction;
import io.narayana.compensations.mongo.State;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import io.narayana.compensations.mongo.dummy.DummyCompensationAction;
import io.narayana.compensations.mongo.dummy.DummyConfirmationAction;
import io.narayana.compensations.mongo.dummy.DummyState;
import io.narayana.compensations.mongo.internal.ParticipantImple;
import io.narayana.compensations.mongo.internal.TransactionManagerImpl;
import io.narayana.compensations.mongo.utils.BAControlerProducer;
import io.narayana.compensations.mongo.utils.TransactionManagerProducer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

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

    public static Class[] getDummyTestClasses() {
        return new Class[] {
                DummyCompensationAction.class,
                DummyConfirmationAction.class,
                DummyState.class
        };
    }

    public static StringAsset getManifestDependenciesAsset() {
        final String manifestDependencies = "Dependencies: org.jboss.narayana.compensations, org.jboss.xts\n";

        return new StringAsset(manifestDependencies);
    }

}
