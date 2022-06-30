package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import com.pgrg.springbatch.repo.ODSTransactionMessageFiservRepo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("cobrand-fiserv-writer")
public class CoBrandCycleWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {
        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;
        baseRepo.bulkInsert(odsItemWriterList, ODSTransactionMessage.class);
        /*new MongoItemWriterBuilder<ODSTransactionMessageForChoice>()
                .template(mongoTemplate).collection("ods_transaction_choice")
                .build();*/
    }
}
