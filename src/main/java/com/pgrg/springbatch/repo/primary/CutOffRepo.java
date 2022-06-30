package com.pgrg.springbatch.repo.primary;

import com.pgrg.springbatch.entity.CutOffDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CutOffRepo extends MongoRepository<CutOffDate, String> {


}
