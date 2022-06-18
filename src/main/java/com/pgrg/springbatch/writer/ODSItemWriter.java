package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ODSItemWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {
        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;
        baseRepo.bulkInsert(odsItemWriterList, ODSTransactionMessage.class);
    }
}
