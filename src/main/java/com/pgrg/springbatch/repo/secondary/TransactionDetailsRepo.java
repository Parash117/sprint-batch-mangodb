package com.pgrg.springbatch.repo.secondary;

import com.pgrg.springbatch.entity.TransactionDetails;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailsRepo extends MongoRepository<TransactionDetails, Long> {

    @Query("{'emAccountNumber' : ?0, 'cycledForFiserv': 'N' }")
    List<TransactionDetails> findTransactionByEmAccountNumber(String emAccountNumber);

    @Query("{'emAccountNumber' : ?0, 'cycledForPartner': 'N' }")
    List<TransactionDetails> findTransactionByEmAccountNumberForChoice(String emAccountNumber);

}
