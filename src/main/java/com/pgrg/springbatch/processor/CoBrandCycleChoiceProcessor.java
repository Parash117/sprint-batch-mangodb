package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.*;
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
import java.util.List;

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
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepo.findTransactionByEmAccountNumberForChoice(item.getAccountIdentifier());

        if(transactionDetailsList != null && transactionDetailsList.size()>0) {
            TransactionDetails transactionDetails = transactionDetailsList.stream().findAny().orElse(new TransactionDetails());
            Long totalScore = transactionDetailsList.parallelStream()
//                    .filter(x-> "N".equalsIgnoreCase(x.getCycledForFiserv()))
                    .flatMap(x ->
                            x.getBonus().stream().map(y ->
                                    y.getPointsEarned())
                    ).mapToLong(x-> x.longValue()).sum();

            ODSTransactionMessageForChoice odsTransactionMessage = ODSTransactionMessageForChoice.builder()
                    .crn(transactionDetails.getEmAccountNumber())
                    .cycleDate(cycleDate)
                    .totalPointsEarned(totalScore)
                    .audit(new Audit())
                    .processedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                    .build();
            return odsTransactionMessage;
        }
        return null;
    }
}
