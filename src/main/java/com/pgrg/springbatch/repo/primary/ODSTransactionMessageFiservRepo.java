package com.pgrg.springbatch.repo.primary;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ODSTransactionMessageFiservRepo extends MongoRepository<ODSTransactionMessage, Long> {

}