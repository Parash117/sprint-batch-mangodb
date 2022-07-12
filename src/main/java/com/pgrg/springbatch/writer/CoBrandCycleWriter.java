package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("cobrand-fiserv-writer")
public class CoBrandCycleWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {

        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;

        List<ODSTransactionMessage> odsTransactionMessageList = new ArrayList<>();

        odsTransactionMessageList = odsItemWriterList.parallelStream().map( x->{
            x.getBonusList().
        });

        baseRepo.bulkInsert(odsItemWriterList, ODSTransactionMessage.class);
        /*new MongoItemWriterBuilder<ODSTransactionMessageForChoice>()
                .template(mongoTemplate).collection("ods_transaction_choice")
                .build();*/
    }
}
