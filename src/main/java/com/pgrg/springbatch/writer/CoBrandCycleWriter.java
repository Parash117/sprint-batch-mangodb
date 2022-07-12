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
        int size = odsItemWriterList.size();
        int offset = size/2;
        List<ODSTransactionMessage> odsTransactionMessageList = new ArrayList<>();
        /*CompletableFuture<List<ODSTransactionMessage>> futurePartition1 = splitToCategoryWise(odsItemWriterList.subList(0, offset));
        CompletableFuture<List<ODSTransactionMessage>> futurePartition2 = splitToCategoryWise(odsItemWriterList.subList(offset+1, size-1));*/
        odsItemWriterList.parallelStream().forEach( x -> {
            x.getBonusList().parallelStream().forEach( y ->
                odsTransactionMessageList.add(ODSTransactionMessage.builder()
                        .emAccountNumber(x.getEmAccountNumber())
                        .cycleDate(x.getCycleDate())
                        .partnerMerchantCategoryCode(y.getPartnerMerchantCategoryCode())
                        .bonusEarn(y.getBonusScore())
                        .processedDate(x.getProcessedDate())
                        .audit(new Audit())
                        .build())
                );
            });

        /*odsTransactionMessageList.addAll(futurePartition1.get());
        odsTransactionMessageList.addAll(futurePartition2.get());*/
        baseRepo.bulkInsert(odsTransactionMessageList, ODSTransactionMessage.class);
        /*new MongoItemWriterBuilder<ODSTransactionMessageForChoice>()
                .template(mongoTemplate).collection("ods_transaction_choice")
                .build();*/
    }

    @Async
    public CompletableFuture<List<ODSTransactionMessage>> splitToCategoryWise(List<ODSTransactionMessage> odsItemWriterList){
        List<ODSTransactionMessage> odsTransactionMessageList = new ArrayList<>();
        odsItemWriterList.parallelStream().forEach( x->{
            Map<String, Long> odsItemSumMap = x.getBonusList().stream().collect(
                    Collectors.groupingBy(y ->
                                    y.getPartnerMerchantCategoryCode(),
                            Collectors.summingLong(y->y.getBonusScore())
                    )
            );

            odsItemSumMap.entrySet().parallelStream().forEach( z -> {
                odsTransactionMessageList.add(ODSTransactionMessage.builder()
                        .emAccountNumber(x.getEmAccountNumber())
                        .cycleDate(x.getCycleDate())
                        .partnerMerchantCategoryCode(z.getKey())
                        .bonusEarn(z.getValue())
                        .processedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                        .audit(new Audit())
                        .build());
            });
        });
        return CompletableFuture.completedFuture(odsTransactionMessageList);
    }
}
