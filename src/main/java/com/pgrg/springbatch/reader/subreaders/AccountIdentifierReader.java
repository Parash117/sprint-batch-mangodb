package com.pgrg.springbatch.reader.subreaders;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import org.springframework.batch.item.data.MongoItemReader;
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
        MongoItemReader<AccountMaster> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setTargetType(AccountMaster.class);
        reader.setQuery("{\"cycleCode\": "+cycleCode+" }");
        return reader;
    }

}
