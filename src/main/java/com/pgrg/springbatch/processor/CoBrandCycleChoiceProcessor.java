package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.ODSTransactionMessageForChoice;
import com.pgrg.springbatch.entity.TransactionDetails;
import com.pgrg.springbatch.repo.secondary.TransactionDetailsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@Qualifier("coBrand-cycle-processor")
public class CoBrandCycleChoiceProcessor implements ItemProcessor<AccountMaster, ODSTransactionMessageForChoice> {

    @Autowired
    private TransactionDetailsRepo transactionDetailsRepo;
    private String cycleDate;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        cycleDate = jobParameters.getString("cycleDate");
    }


    @Override
    public ODSTransactionMessageForChoice process(AccountMaster item) throws Exception {
        TransactionDetails transactionDetails = transactionDetailsRepo.findTransactionByEmAccountNumber(item.getAccountIdentifier());
        if(transactionDetails != null && "N".equalsIgnoreCase(transactionDetails.getCycledForChoice())) {
            ODSTransactionMessageForChoice odsTransactionMessage = ODSTransactionMessageForChoice.builder()
                    .crn(transactionDetails.getEmAccountNumber())
                    .cycleDate(cycleDate)
                    .totalPointsEarned(transactionDetails.getBonus().stream()
                            .map(x-> x.getPointsEarned())
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                    .processedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                    .build();
            return odsTransactionMessage;
        }
        return null;
    }
}
