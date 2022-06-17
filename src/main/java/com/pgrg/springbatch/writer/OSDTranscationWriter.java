package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.repo.ODSTxMsgRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OSDTranscationWriter {

    @Autowired
    private BaseRepo<ODSTransactionMessage> odsTxMsgRepo;
    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoItemWriter<ODSTransactionMessage> writer() {
        return new MongoItemWriterBuilder<ODSTransactionMessage>()
                .template(mongoTemplate).collection("ODSTransactionMessage")
                .build();
    }


}
