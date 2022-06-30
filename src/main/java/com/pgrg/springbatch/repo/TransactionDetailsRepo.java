package com.pgrg.springbatch.repo;

import com.pgrg.springbatch.entity.TransactionDetails;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepo extends MongoRepository<TransactionDetails, Long> {

    @Query("{'emAccountNumber' : ?0 }, {'limit' : 1}")
    TransactionDetails findTransactionByEmAccountNumber(String emAccountNumber);

}
