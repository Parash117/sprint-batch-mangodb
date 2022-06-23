package com.pgrg.springbatch.writer;

import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("raw-to-score-writer")
public class RawToScoreWriter implements ItemWriter<ODSTransactionMessage> {

    @Autowired
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Autowired
    private BaseRepo<ODSTransactionMessageForChoice> baseRepo2;

    @Override
    public void write(List<? extends ODSTransactionMessage> items) throws Exception {

        List<ODSTransactionMessage> odsItemWriterList = (List<ODSTransactionMessage>) items;
        List<ODSTransactionMessage> odsItemWriterListForFiserv = new ArrayList<>();
        List<ODSTransactionMessageForChoice> odsItemWriterListForChoice = new ArrayList<>();

        odsItemWriterListForFiserv = odsItemWriterList.stream().filter(x ->
                "FISERV".equalsIgnoreCase(x.getDestinationSystem()) || "BOTH".equalsIgnoreCase(x.getDestinationSystem()) )
                .collect(Collectors.toList());

        odsItemWriterListForChoice = odsItemWriterList.stream().filter(x ->
                "CHOICE".equalsIgnoreCase(x.getDestinationSystem()) || "BOTH".equalsIgnoreCase(x.getDestinationSystem()) )
                .map(x-> ODSTransactionMessageForChoice.builder()
                        .crn(x.getCrn())
                        .bonusCode(x.getBonusCode())
                        .destinationSystem(x.getDestinationSystem())
                        .cycleDate(x.getCycleDate())
                        .processedDate(x.getProcessedDate())
                        .totalPointsEarned(x.getTotalPointsEarned())
                        .build())
                .collect(Collectors.toList());

        baseRepo2.bulkInsert(odsItemWriterListForChoice, ODSTransactionMessageForChoice.class);
        baseRepo.bulkInsert(odsItemWriterListForFiserv, ODSTransactionMessage.class);
    }
}
