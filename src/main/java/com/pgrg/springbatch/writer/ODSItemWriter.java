package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class ODSItemWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {
        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;
        List<ODSTransactionMessage> odsItemWriterList2 = new ArrayList<>();
        /*List<String> crnList = odsItemWriterList.stream().distinct().map(x ->
                x.getCrn()).collect(Collectors.toList());
        List<ODSTransactionMessage> odsTransactionMessages = new ArrayList<>();
        crnList.stream().forEach(x -> {
            List<ODSTransactionMessage> z = odsItemWriterList.stream().filter(y ->
                    x.equalsIgnoreCase(y.getCrn())).collect(Collectors.toList());
            AtomicReference<Double> sum = new AtomicReference<>(new Double(0D));
            z.stream().forEach(a -> {
                sum.set(sum.get() + a.getTotalPointsEarned());
            });
            odsTransactionMessages.add(ODSTransactionMessage.builder()
                    .processedDate(z.get(0).getProcessedDate())
                    .destinationSystem(z.get(0).getDestinationSystem())
                    .totalPointsEarned(sum.get())
                    .build());
        });*/
        /*odsItemWriterList.stream().collect(Collectors.groupingBy(x->
                x.getCrn(), Collectors.summarizingDouble(x->
                x.getTotalPointsEarned()))).forEach((x,y)->
                        System.out.println(x+"  "+y)
                );*/

        baseRepo.bulkInsert(odsItemWriterList, ODSTransactionMessage.class);
    }
}
