package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
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

@Component
public class ODSTransactionReader  {

    public JsonItemReader<ODSTransactionMessage> reader() {
        return new JsonItemReaderBuilder<ODSTransactionMessage>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(ODSTransactionMessage.class))
                .resource(new ClassPathResource("odstransaction-message.json"))
                .name("ODSTransactionReader")
                .build();
    }

}
