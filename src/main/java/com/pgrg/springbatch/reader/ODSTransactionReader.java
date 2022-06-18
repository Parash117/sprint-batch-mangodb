package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionRaw;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

public class ODSTransactionReader  {

    public JsonItemReader<ODSTransactionRaw> reader() {
        return new JsonItemReaderBuilder<ODSTransactionRaw>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(ODSTransactionRaw.class))
                .resource(new ClassPathResource("odsraw-data.json"))
                .name("ODSTransactionReader")
                .build();
    }

}
