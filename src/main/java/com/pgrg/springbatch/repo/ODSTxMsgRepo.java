package com.pgrg.springbatch.repo;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ODSTxMsgRepo extends MongoRepository<ODSTransactionMessage, Long> {

}
