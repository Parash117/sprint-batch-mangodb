package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope("step")

public class CoBrandCycleReader {

    @Value("#{jobParameters['cycleCode  ']}")
    private Long cycleCode;

    private final MongoTemplate mongoTemplate;

    public CoBrandCycleReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public JsonItemReader<CoBrandAccountMaster> reader() {
        return new JsonItemReaderBuilder<CoBrandAccountMaster>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoBrandAccountMaster.class))
                .resource(new ClassPathResource("score-transaction.json"))
                .name("ODSTransactionReader")
                .build();
    }

    public MongoItemReader<CoBrandAccountMaster> reader2() {
        MongoItemReader<CoBrandAccountMaster> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setTargetType(CoBrandAccountMaster.class);
        reader.setQuery("{}");
        return reader;
    }

}
