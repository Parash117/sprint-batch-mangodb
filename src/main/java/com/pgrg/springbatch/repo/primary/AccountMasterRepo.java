package com.pgrg.springbatch.repo.primary;

import com.pgrg.springbatch.entity.AccountMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMasterRepo extends MongoRepository<AccountMaster, String> {

    @Query("{'cycleCode99' : ?0 }")
    List<AccountMaster> findByCycleCode99(Long cycleCode);
}
