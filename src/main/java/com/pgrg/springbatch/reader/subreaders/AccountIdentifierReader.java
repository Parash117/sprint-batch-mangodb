package com.pgrg.springbatch.reader.subreaders;

import com.pgrg.springbatch.entity.AccountMaster;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AccountIdentifierReader {

    private final MongoTemplate mongoTemplate;

    public AccountIdentifierReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MongoItemReader<AccountMaster> reader(String cycleCode) {
        MongoItemReader<AccountMaster> accountMasterMongoItemReader = new MongoItemReaderBuilder<AccountMaster>()
                .collection("accountMaster")
                .name("accountIdentifierReader")
                .jsonQuery("{\"cycleCode99\": "+Long.valueOf(cycleCode)+" }")
                .template(mongoTemplate)
                .sorts(new HashMap<String, Sort.Direction>() {{
                    put("_id", Sort.Direction.DESC);
                }})
                .targetType(AccountMaster.class)
                .build();
        return accountMasterMongoItemReader;
    }

}
