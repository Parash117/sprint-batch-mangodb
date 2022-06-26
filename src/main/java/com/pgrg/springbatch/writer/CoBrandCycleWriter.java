package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
@Qualifier("cobrand-fiserv-writer")
public class CoBrandCycleWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

//    @Autowired
//    private BaseRepo<ODSTransactionMessageForChoice> baseRepo2;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {
        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;
        odsItemWriterList = odsItemWriterList.stream().filter(x-> x.getCrn()!=null).collect(Collectors.toList());
        Map<String, BigDecimal> odsItemSumMap = odsItemWriterList.stream().collect(
                Collectors.groupingBy(
                        ODSTransactionMessage::getCrn,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                ODSTransactionMessage::getTotalPointsEarned,
                                BigDecimal::add
                        )
                )
        );
        List<ODSTransactionMessage> odsTransactionMessageList = new ArrayList<>();
        List<ODSTransactionMessage> odsItemWriterListForFiserv = new ArrayList<>();
//        List<ODSTransactionMessageForChoice> odsItemWriterListForChoice = new ArrayList<>();

        for (var entrySet : odsItemSumMap.entrySet()) {
            odsTransactionMessageList.add(odsItemWriterList
                    .stream()
                    .filter(x ->
                            x.getCrn().equalsIgnoreCase(entrySet.getKey())
                    )
                    .map(x -> ODSTransactionMessage.builder()
                            .crn(x.getCrn())
                            .processedDate(x.getProcessedDate())
                            .cycleDate(x.getCycleDate())
                            .destinationSystem(x.getDestinationSystem())
                            .cycledForChoice(x.getCycledForChoice())
                            .cycledForFiserv(x.getCycledForFiserv())
                            .totalPointsEarned(entrySet.getValue())
                            .build())
                    .findAny().get()
            );
        }

        odsItemWriterListForFiserv = odsTransactionMessageList.stream().filter(x ->
                "FISERV".equalsIgnoreCase(x.getDestinationSystem()) || "BOTH".equalsIgnoreCase(x.getDestinationSystem()))
                .collect(Collectors.toList());

//        odsItemWriterListForChoice = odsTransactionMessageList.stream().filter(x ->
//                "CHOICE".equalsIgnoreCase(x.getDestinationSystem()) || "BOTH".equalsIgnoreCase(x.getDestinationSystem()))
//                .map(x -> ODSTransactionMessageForChoice.builder()
//                        .crn(x.getCrn())
//                        .bonusCode(x.getBonusCode())
//                        .destinationSystem(x.getDestinationSystem())
//                        .cycleDate(x.getCycleDate())
//                        .processedDate(x.getProcessedDate())
//                        .totalPointsEarned(x.getTotalPointsEarned())
//                        .build())
//                .collect(Collectors.toList());

//        baseRepo2.bulkInsert(odsItemWriterListForChoice, ODSTransactionMessageForChoice.class);
        baseRepo.bulkInsert(odsItemWriterListForFiserv, ODSTransactionMessage.class);
    }
}
