package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CoBrandCycleChoiceReader {

    public JsonItemReader<CoBrandAccountMaster> reader() {
        return new JsonItemReaderBuilder<CoBrandAccountMaster>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoBrandAccountMaster.class))
                .resource(new ClassPathResource("account-master.json"))
                .name("ODSTransactionReader")
                .build();
    }

}
