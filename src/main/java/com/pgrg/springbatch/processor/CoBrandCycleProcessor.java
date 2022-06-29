package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.CoBrandAccountMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@Qualifier("raw-data-processor")
public class CoBrandCycleProcessor implements ItemProcessor<CoBrandAccountMaster, ODSTransactionMessage> {

//    @StepScope
//    @Value("#{jobParameters['cutoffDate']}")
    private Long cutoffDate;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        cutoffDate = jobParameters.getLong("cutoffDate");
    }

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
        else {
            return new ODSTransactionMessage();
        }
        log.info("Transaction processed for {}", item.getId());
        return odsTransactionMessage;
    }
}
