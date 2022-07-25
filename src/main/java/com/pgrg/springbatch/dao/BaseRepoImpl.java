package com.pgrg.springbatch.dao;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
@Repository
@RequiredArgsConstructor
@Slf4j
public class BaseRepoImpl<T> implements BaseRepo<T>{
    private final MongoTemplate mongoTemplate;
    @Override
    public Integer bulkInsert(List<T> items, Class type) {
        Integer insertedCount = 0;
        try {
            BulkOperations bulkOperations = mongoTemplate
                    .bulkOps(BulkOperations.BulkMode.UNORDERED, type);
            insertedCount = bulkOperations.insert(items).execute().getInsertedCount();
        } catch (BulkOperationException bulkOperationException) {
            log.error("[{}] BulkOperationException during bulkInsert: {}", type.getSimpleName(),
                    bulkOperationException);
            List<BulkWriteError> bulkWriteErrors = bulkOperationException.getErrors();
            bulkWriteErrors.forEach(bulkWriteError -> log
                    .error("[{}] Insert Failed record : {} ; reason: {}", type.getSimpleName(),
                            items.get(bulkWriteError.getIndex()), bulkWriteError.getMessage()));
        } catch (DuplicateKeyException dke) {
            if (dke.getCause() instanceof MongoBulkWriteException) {
                MongoBulkWriteException mwe = (MongoBulkWriteException) dke.getCause();
                insertedCount = mwe.getWriteResult().getInsertedCount();
                log.info("[{}] insertedCount : {}", type.getSimpleName(), insertedCount);
                log.error("[{}] MongoBulkWriteException during bulkInsert: {}", type.getSimpleName(),
                        mwe.getMessage(), mwe);
                List<BulkWriteError> bulkWriteErrors = mwe.getWriteErrors();
                bulkWriteErrors.forEach(bulkWriteError -> log
                        .error("{} Upsert Failed for record : {} ; reason: {}", type.getSimpleName(),
                                items.get(bulkWriteError.getIndex()), bulkWriteError.getMessage()));
            } else {
                log.error("[{}] DuplicateKeyException occurred during bulkInsert : {}",
                        type.getSimpleName(), dke.getMessage(), dke);
            }
        }
        return insertedCount;
    }
//    @Override
//    public Pair<Integer, Integer> bulkUpsert(List<T> items, Class type,
//                                             Function<T, Pair<Query, Update>> function) {
//        Integer modifiedCount = 0;
//        Integer insertedCount = 0;
//        List<Pair<Query, Update>> updates = new ArrayList<>(items.size());
//        for (T t : items) {
//            updates.add(function.apply(t));
//        }
//        try {
//            BulkOperations bulkOperations = mongoTemplate
//                    .bulkOps(BulkOperations.BulkMode.UNORDERED, type);
//            BulkWriteResult bulkWriteResult = bulkOperations.upsert(updates).execute();
//            modifiedCount = bulkWriteResult.getModifiedCount();
//            insertedCount = bulkWriteResult.getUpserts().size();
//        } catch (BulkOperationException bulkOperationException) {
//            modifiedCount = bulkOperationException.getResult().getModifiedCount();
//            insertedCount = bulkOperationException.getResult().getUpserts().size();
//            log.error("[{}] BulkOperationException during bulkUpsert: {}", type.getSimpleName(),
//                    bulkOperationException);
//            List<BulkWriteError> bulkWriteErrors = bulkOperationException.getErrors();
//            bulkWriteErrors.forEach(bulkWriteError -> log
//                    .error("{} Upsert Failed record : {} ; reason: {}", type.getSimpleName(),
//                            items.get(bulkWriteError.getIndex()), bulkWriteError.getMessage()));
//        } catch (DuplicateKeyException dke) {
//            if (dke.getCause() instanceof MongoBulkWriteException) {
//                MongoBulkWriteException mwe = (MongoBulkWriteException) dke.getCause();
//                modifiedCount = mwe.getWriteResult().getModifiedCount();
//                insertedCount = mwe.getWriteResult().getUpserts().size();
//                log.info("insertedCount : {}", insertedCount);
//                log.info("modifiedCount : {}", modifiedCount);
//                log.error("[{}] MongoBulkWriteException during bulkUpsert: {}", type.getSimpleName(),
//                        mwe.getMessage(), mwe);
//                List<BulkWriteError> bulkWriteErrors = mwe.getWriteErrors();
//                bulkWriteErrors.forEach(bulkWriteError -> log
//                        .error("{} Upsert Failed for record : {} ; reason: {}", type.getSimpleName(),
//                                items.get(bulkWriteError.getIndex()), bulkWriteError.getMessage()));
//            } else {
//                log.error("[{}] DuplicateKeyException occurred during bulkUpsert : {}",
//                        type.getSimpleName(), dke.getMessage(), dke);
//            }
//        }
//        return Pair.of(insertedCount, modifiedCount);
//    }
//    @Override
//    public Integer deleteAll(Class type) {
//        Integer deletedCount = 0;
//        try {
//            deletedCount = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, type).remove(
//                    new Query()).execute().getDeletedCount();
//        } catch (BulkOperationException bulkOperationException) {
//            log.error("[{}] BulkOperationException during deleteAll: {}", type.getSimpleName(),
//                    bulkOperationException);
//            List<BulkWriteError> bulkWriteErrors = bulkOperationException.getErrors();
//            bulkWriteErrors.forEach(bulkWriteError -> log
//                    .error("{} Delete Failed record : {} ; reason: {}", type.getSimpleName(),
//                            bulkWriteError.getDetails().toJson(), bulkWriteError.getMessage()));
//        }
//        return deletedCount;
//    }
//    @Override
//    public Integer bulkUpdate(List<T> items, Class type, Function<T, Pair<Query, Update>> function) {
//        Integer modifiedCount;
//        List<Pair<Query, Update>> updates = new ArrayList<>(items.size());
//        for (T t : items) {
//            updates.add(function.apply(t));
//        }
//        try {
//            BulkOperations bulkOperations = mongoTemplate
//                    .bulkOps(BulkOperations.BulkMode.UNORDERED, type);
//            BulkWriteResult bulkWriteResult = bulkOperations.updateMulti(updates).execute();
//            modifiedCount = bulkWriteResult.getModifiedCount();
//        } catch (BulkOperationException bulkOperationException) {
//            modifiedCount = bulkOperationException.getResult().getModifiedCount();
//            log.info("modifiedCount : {}", modifiedCount);
//            log.error("[{}] BulkOperationException during update: {}", type.getSimpleName(),
//                    bulkOperationException);
//        }
//        return modifiedCount;
//    }
}
