package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.RawData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@Qualifier("raw-data-processor")
public class RawDataProcessor implements ItemProcessor<RawData, ODSTransactionMessage> {

    @Override
    public ODSTransactionMessage process(RawData item) throws Exception {
        ODSTransactionMessage odsTransactionMessage = ODSTransactionMessage.builder()
                .id(item.getId())
                .totalPointsEarned(item.getBonus().stream()
                        .map(x-> x.getPointsEarned())
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .crn(item.getEmAccountNumber())
                .cycleDate(item.getTransactionDate())
                .processedDate(item.getTransactionPostedDate())
                .cycledForFiserv(item.getCycledForFiserv())
                .cycledForChoice(item.getCycledForChoice())
                .build();
        if("Y".equalsIgnoreCase(item.getCycledForChoice()) && "Y".equalsIgnoreCase(item.getCycledForFiserv())){
            odsTransactionMessage.setDestinationSystem("BOTH");
        }
        else if("Y".equalsIgnoreCase(item.getCycledForChoice())){
            odsTransactionMessage.setDestinationSystem("FISERV");
        }
        else {
            odsTransactionMessage.setDestinationSystem("CHOICE");
        }
        log.info("Transaction processed for {}", item.getId());
        return odsTransactionMessage;
    }
}
