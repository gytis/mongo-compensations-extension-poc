package io.narayana.compensations.mongo.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import io.narayana.compensations.mongo.MongoCollectionConfiguration;
import io.narayana.compensations.mongo.internal.MongoCollectionImpl;
import org.bson.Document;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class MongoCollectionProducer {

    private MongoClient mongoClient;

    @Produces
    @MongoCollectionConfiguration(collectionName = "", databaseName = "")
    public MongoCollection<Document> produceMongoCollection(final InjectionPoint injectionPoint,
            @New MongoCollectionImpl collection) {

        if (mongoClient == null) {
            mongoClient = new MongoClient();
        }

        final MongoCollectionConfiguration configuration =
                injectionPoint.getAnnotated().getAnnotation(MongoCollectionConfiguration.class);

        final MongoCollection<Document> delegate =
                mongoClient.getDatabase(configuration.databaseName()).getCollection(configuration.collectionName());

        collection.setDelegate(delegate);

        return collection;
    }

}
