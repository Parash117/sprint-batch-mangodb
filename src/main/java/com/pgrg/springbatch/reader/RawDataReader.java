package com.pgrg.springbatch.reader;

import com.pgrg.springbatch.entity.ODSTransactionRaw;
import com.pgrg.springbatch.entity.RawData;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class RawDataReader {

    public JsonItemReader<RawData> reader() {
        return new JsonItemReaderBuilder<RawData>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(RawData.class))
                .resource(new ClassPathResource("account-master.json"))
                .name("ODSTransactionReader")
                .build();
    }

}
