package com.pgrg.springbatch.reader.subreaders;

import com.pgrg.springbatch.entity.AccountMaster;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AccountIdentifierReader{

    private final MongoTemplate mongoTemplate;

    public AccountIdentifierReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * In the jsonQuery Below 604, 605, 606, 607 are the associated product code that identifies the account
     * as coBrand account.
     *
     *  @param cycleCode
     * @return
     */
    public MongoItemReader<AccountMaster> reader(String cycleCode) {
        MongoItemReader<AccountMaster> accountMasterMongoItemReader = new MongoItemReaderBuilder<AccountMaster>()
                .collection("accountMaster")
                .name("accountIdentifierReader")
//                .jsonQuery("{\"cycleCode99\": "+Long.valueOf(cycleCode)+" }")
                .jsonQuery("{ $and: " +
                        "[ " +
                        "{ $or: [ " +
                        "{'productCode' : 604}, " +
                        "{'productCode' : 605}, " +
                        "{'productCode' : 606}, " +
                        "{'productCode' : 607} ] " +
                        "}, " +
                        "{'cycleCode99' : "+Long.valueOf(cycleCode)+"} ] }")
                .template(mongoTemplate)
                .sorts(new HashMap<String, Sort.Direction>() {{
                    put("_id", Sort.Direction.DESC);
                }})
                .targetType(AccountMaster.class)
                .build();
        return accountMasterMongoItemReader;
    }
}
