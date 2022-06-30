package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.entity.AccountIdentifiers;
import com.pgrg.springbatch.repo.AccountIdentifierRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("acc-id-writer")
public class AccountIdentifierWriter implements ItemWriter<AccountIdentifiers> {

    private final MongoTemplate mongoTemplate;
    private final AccountIdentifierRepo accountIdentifierRepo;
    public AccountIdentifierWriter(MongoTemplate mongoTemplate,
                                   AccountIdentifierRepo accountIdentifierRepo) {
        this.mongoTemplate = mongoTemplate;
        this.accountIdentifierRepo = accountIdentifierRepo;
    }

    @Override
    public void write(List<? extends AccountIdentifiers> items) throws Exception {
        accountIdentifierRepo.saveAll(items);
    }
}
