package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.TransactionDetails;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
//@StepScope
public class CoBrandCycleReader {

//    @Value("#{jobParameters['cycleCode']}")
    private String cycleCode;

    private final MongoTemplate mongoTemplate;

    public CoBrandCycleReader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public JsonItemReader<TransactionDetails> reader(String cycleCode) {
        return new JsonItemReaderBuilder<TransactionDetails>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(TransactionDetails.class))
                .resource(new ClassPathResource("score-transaction.json"))
                .name("ODSTransactionReader")
                .build();
    }

    public MongoItemReader<TransactionDetails> reader2(String cycleCode) {
        MongoItemReader<TransactionDetails> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setTargetType(TransactionDetails.class);
        reader.setQuery("{\"cycleCode\": "+Long.valueOf(cycleCode)+" }");
        return reader;
    }

    /*@Override
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        cycleCode = jobParameters.getString("cycleCode");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }*/
}
