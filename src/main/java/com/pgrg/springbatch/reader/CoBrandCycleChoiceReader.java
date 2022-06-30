package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.TransactionDetails;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CoBrandCycleChoiceReader {

    public JsonItemReader<TransactionDetails> reader() {
        return new JsonItemReaderBuilder<TransactionDetails>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(TransactionDetails.class))
                .resource(new ClassPathResource("score-transaction.json"))
                .name("ODSTransactionReader")
                .build();
    }

}
