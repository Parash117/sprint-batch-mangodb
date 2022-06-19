package com.pgrg.springbatch.processor;


import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.ODSTransactionRaw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ODSTransactionProcessor implements ItemProcessor<ODSTransactionRaw, ODSTransactionMessage> {

    @Override
    public ODSTransactionMessage process(final ODSTransactionRaw ods) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ODSTransactionMessage odsTransactionMessage = ODSTransactionMessage.builder()
                .crn(ods.getCrn())
                .bonusCode(ods.getBonusCode())
                .cycleDate(dateFormat.format(new Date()))
                .totalPointsEarned(ods.getPointsEarned())
                .processedDate(ods.getTransactionPostedDate())
                .build();
        if("Y".equalsIgnoreCase(ods.getCycledForChoice()) && "Y".equalsIgnoreCase(ods.getCycledForFiserv())){
            odsTransactionMessage.setDestinationSystem("BOTH");
        }
        else if("Y".equalsIgnoreCase(ods.getCycledForChoice())){
            odsTransactionMessage.setDestinationSystem("FISERV");
        }
        else {
            odsTransactionMessage.setDestinationSystem("CHOICE");
        }
        log.info("Transaction processed for {}", ods.getId());
        return odsTransactionMessage;
    }
}
