package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
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
@StepScope
public class CoBrandCycleReader {

    @Value(value = "#{jobParameter['cycleCode']}")
    private String cutOffDate;


    private final MongoTemplate mongoTemplate;

    public CoBrandCycleReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public JsonItemReader<CoBrandAccountMaster> reader(String param1) {
        return new JsonItemReaderBuilder<CoBrandAccountMaster>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoBrandAccountMaster.class))
                .resource(new ClassPathResource("score-transaction.json"))
                .name("ODSTransactionReader")
                .build();
    }

    public MongoItemReader<CoBrandAccountMaster> reader2(String cycleCode) {
        MongoItemReader<CoBrandAccountMaster> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setTargetType(CoBrandAccountMaster.class);
        reader.setQuery("{\"cycleCode\": "+cycleCode+" }");
        return reader;
    }

}
