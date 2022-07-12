package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.Audit;
import com.pgrg.springbatch.entity.Bonus;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Qualifier("cobrand-fiserv-writer")
public class CoBrandCycleWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {
        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;
        baseRepo.bulkInsert(odsItemWriterList, ODSTransactionMessage.class);
    }
}
