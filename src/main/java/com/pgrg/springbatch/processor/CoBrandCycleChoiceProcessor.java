package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.Audit;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.service.RawJsonFileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Component
@Qualifier("coBrand-cycle-processor")
public class CoBrandCycleChoiceProcessor implements ItemProcessor<CoBrandAccountMaster, ODSTransactionMessage> {

//    @Value("#{jobParameters['cutoffDate']}")
//    private Long cutoffDate;

    @Override
    public ODSTransactionMessage process(CoBrandAccountMaster item) throws Exception {
        ODSTransactionMessage odsTransactionMessage1 = new ODSTransactionMessage();
        ODSTransactionMessage odsTransactionMessage = ODSTransactionMessage.builder()
                .id(item.getId())
                .totalPointsEarned(item.getBonus().stream()
                        .map(x-> x.getPointsEarned())
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .crn(item.getEmAccountNumber())
//                .cycleDate(new Date(cutoffDate).toString())
                .processedDate(item.getTransactionPostedDate())
                .cycledForFiserv(item.getCycledForFiserv())
                .cycledForChoice(item.getCycledForChoice())
                .audit(Audit.builder().createdDate(new Timestamp(new Date().getTime())).modifiedDate(new Timestamp(new Date().getTime())).build())
                .build();
        if("N".equalsIgnoreCase(item.getCycledForChoice()) && "N".equalsIgnoreCase(item.getCycledForFiserv())){
            odsTransactionMessage.setDestinationSystem("BOTH");
        }
        else if("N".equalsIgnoreCase(item.getCycledForChoice())){
            odsTransactionMessage.setDestinationSystem("CHOICE");
        }
        else {
            return null;
        }
        log.info("Transaction processed for {}", item.getId());
        return odsTransactionMessage;
    }
}
