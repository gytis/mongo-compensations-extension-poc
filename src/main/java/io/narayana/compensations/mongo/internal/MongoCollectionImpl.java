package io.narayana.compensations.mongo.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.narayana.compensations.mongo.SystemException;
import io.narayana.compensations.mongo.TransactionManager;
import io.narayana.compensations.mongo.WrongStateException;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class MongoCollectionImpl implements MongoCollection<Document> {

    private static final Logger LOGGER = Logger.getLogger(MongoCollectionImpl.class);

    @Inject
    private TransactionManager transactionManager;

    private MongoCollection<Document> delegate;

    public void setDelegate(final MongoCollection<Document> delegate) {
        this.delegate = delegate;
    }

    public MongoNamespace getNamespace() {
        return delegate.getNamespace();
    }

    public Class<Document> getDocumentClass() {
        return delegate.getDocumentClass();
    }

    public CodecRegistry getCodecRegistry() {
        return delegate.getCodecRegistry();
    }

    public ReadPreference getReadPreference() {
        return delegate.getReadPreference();
    }

    public WriteConcern getWriteConcern() {
        return delegate.getWriteConcern();
    }

    public MongoCollection<Document> withDocumentClass(Class aClass) {
        return null;
    }

    public MongoCollection withCodecRegistry(CodecRegistry codecRegistry) {
        return delegate.withCodecRegistry(codecRegistry);
    }

    public MongoCollection withReadPreference(ReadPreference readPreference) {
        return delegate.withReadPreference(readPreference);
    }

    public MongoCollection withWriteConcern(WriteConcern writeConcern) {
        return delegate.withWriteConcern(writeConcern);
    }

    public long count() {
        return delegate.count();
    }

    public long count(Bson bson) {
        return delegate.count(bson);
    }

    public long count(Bson bson, CountOptions countOptions) {
        return delegate.count(bson, countOptions);
    }

    public DistinctIterable<Document> distinct(String s, Class aClass) {
        return null;
    }

    public FindIterable find() {
        return delegate.find();
    }

    public FindIterable<Document> find(Class aClass) {
        return null;
    }

    public FindIterable find(Bson bson) {
        return delegate.find(bson);
    }

    public AggregateIterable<Document> aggregate(List list, Class aClass) {
        return null;
    }

    public FindIterable<Document> find(Bson bson, Class aClass) {
        return null;
    }

    public AggregateIterable aggregate(List list) {
        return delegate.aggregate(list);
    }

    public MapReduceIterable mapReduce(String s, String s1) {
        return delegate.mapReduce(s, s1);
    }

    public MapReduceIterable<Document> mapReduce(String s, String s1, Class aClass) {
        return null;
    }

    public BulkWriteResult bulkWrite(List list) {
        return delegate.bulkWrite(list);
    }

    public BulkWriteResult bulkWrite(List list, BulkWriteOptions bulkWriteOptions) {
        return delegate.bulkWrite(list, bulkWriteOptions);
    }

    public void insertOne(Document document) {
        document = appendInsertTxData(document);

        delegate.insertOne(document);
    }

    public void insertMany(List list) {
        delegate.insertMany(list);
    }

    public void insertMany(List list, InsertManyOptions insertManyOptions) {
        delegate.insertMany(list, insertManyOptions);
    }

    public DeleteResult deleteOne(Bson bson) {
        return delegate.deleteOne(bson);
    }

    public DeleteResult deleteMany(Bson bson) {
        return delegate.deleteMany(bson);
    }

    public UpdateResult replaceOne(Bson bson, Document o) {
        return delegate.replaceOne(bson, o);
    }

    public UpdateResult replaceOne(Bson bson, Document o, UpdateOptions updateOptions) {
        return delegate.replaceOne(bson, o, updateOptions);
    }

    public UpdateResult updateOne(Bson bson, Bson bson1) {
        return delegate.updateOne(bson, bson1);
    }

    public UpdateResult updateOne(Bson bson, Bson bson1, UpdateOptions updateOptions) {
        return delegate.updateOne(bson, bson1, updateOptions);
    }

    public UpdateResult updateMany(Bson bson, Bson bson1) {
        return delegate.updateMany(bson, bson1);
    }

    public UpdateResult updateMany(Bson bson, Bson bson1, UpdateOptions updateOptions) {
        return delegate.updateMany(bson, bson1, updateOptions);
    }

    public Document findOneAndDelete(Bson bson) {
        return delegate.findOneAndDelete(bson);
    }

    public Document findOneAndDelete(Bson bson, FindOneAndDeleteOptions findOneAndDeleteOptions) {
        return delegate.findOneAndDelete(bson, findOneAndDeleteOptions);
    }

    public Document findOneAndReplace(Bson bson, Document o) {
        return delegate.findOneAndReplace(bson, o);
    }

    public Document findOneAndReplace(Bson bson, Document o, FindOneAndReplaceOptions findOneAndReplaceOptions) {
        return delegate.findOneAndReplace(bson, o, findOneAndReplaceOptions);
    }

    public Document findOneAndUpdate(Bson bson, Bson bson1) {
        return delegate.findOneAndUpdate(bson, bson1);
    }

    public Document findOneAndUpdate(Bson bson, Bson bson1, FindOneAndUpdateOptions findOneAndUpdateOptions) {
        return delegate.findOneAndUpdate(bson, bson1, findOneAndUpdateOptions);
    }

    public void drop() {
        delegate.drop();
    }

    public String createIndex(Bson bson) {
        return delegate.createIndex(bson);
    }

    public String createIndex(Bson bson, IndexOptions indexOptions) {
        return delegate.createIndex(bson, indexOptions);
    }

    public List<String> createIndexes(List list) {
        return delegate.createIndexes(list);
    }

    public ListIndexesIterable<Document> listIndexes() {
        return delegate.listIndexes();
    }

    public ListIndexesIterable<Document> listIndexes(Class aClass) {
        return null;
    }

    public void dropIndex(String s) {
        delegate.dropIndex(s);
    }

    public void dropIndex(Bson bson) {
        delegate.dropIndex(bson);
    }

    public void dropIndexes() {
        delegate.dropIndexes();
    }

    public void renameCollection(MongoNamespace mongoNamespace) {
        delegate.renameCollection(mongoNamespace);
    }

    public void renameCollection(MongoNamespace mongoNamespace, RenameCollectionOptions renameCollectionOptions) {
        delegate.renameCollection(mongoNamespace, renameCollectionOptions);
    }

    private Document appendInsertTxData(final Document document) {
        final Document copy = new Document(document);

        try {
            final Object txData = transactionManager.getTxData();
            if (txData != null) {
                copy.put("txData", txData.toString());
                final MongoInsertState state = new MongoInsertState(copy);
                final MongoInsertConfirmationAction confirmationAction = new MongoInsertConfirmationAction(state, delegate);
                transactionManager.register(confirmationAction, null);
            }
        } catch (final SystemException e) {
            LOGGER.warn(e.getMessage());
        } catch (final WrongStateException e) {
            LOGGER.warn(e.getMessage());
        }

        return copy;
    }
}
