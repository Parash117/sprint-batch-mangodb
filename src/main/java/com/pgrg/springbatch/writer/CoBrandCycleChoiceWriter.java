package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import com.pgrg.springbatch.repo.AccountIdentifierRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Qualifier("cobrand-choice-writer")
public class CoBrandCycleChoiceWriter implements ItemWriter<ODSTransactionMessageForChoice> {

    private final MongoTemplate mongoTemplate;
    private final AccountIdentifierRepo accountIdentifierRepo;

    @Autowired
    private BaseRepo<ODSTransactionMessageForChoice> baseRepo2;

    public CoBrandCycleChoiceWriter(MongoTemplate mongoTemplate, AccountIdentifierRepo accountIdentifierRepo) {
        this.mongoTemplate = mongoTemplate;
        this.accountIdentifierRepo = accountIdentifierRepo;
    }

    @Override
    public void write(List<? extends ODSTransactionMessageForChoice> items) throws Exception {
        new MongoItemWriterBuilder<ODSTransactionMessageForChoice>()
                .template(mongoTemplate).collection("ods_transaction_choice")
                .build();
    }
}
