package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@Qualifier("raw-data-processor")
public class CoBrandCycleProcessor implements ItemProcessor<CoBrandAccountMaster, ODSTransactionMessage> {

    @Override
    public ODSTransactionMessage process(CoBrandAccountMaster item) throws Exception {
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
        if("N".equalsIgnoreCase(item.getCycledForChoice()) && "N".equalsIgnoreCase(item.getCycledForFiserv())){
            odsTransactionMessage.setDestinationSystem("BOTH");
        }
        else if("N".equalsIgnoreCase(item.getCycledForFiserv())){
            odsTransactionMessage.setDestinationSystem("FISERV");
        }
        else if("N".equalsIgnoreCase(item.getCycledForChoice())){
            odsTransactionMessage.setDestinationSystem("CHOICE");
        }
        else {
            return new ODSTransactionMessage();
        }
        log.info("Transaction processed for {}", item.getId());
        return odsTransactionMessage;
    }
}
