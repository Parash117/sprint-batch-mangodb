package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.*;
import com.pgrg.springbatch.repo.secondary.TransactionDetailsRepo;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("acc-id-processor")
public class AccountIdentifierFiservProcessor implements ItemProcessor<AccountMaster, ODSTransactionMessage> {

    @Autowired
    private TransactionDetailsRepo transactionDetailsRepo;
    private String cycleDate;
    private String jobName;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        cycleDate = jobParameters.getString("cycleDate");
        jobName = stepExecution.getJobExecution().getJobInstance().getJobName();

    }

    @Override
    public ODSTransactionMessage process(AccountMaster item) throws Exception {
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepo.findTransactionByEmAccountNumber(item.getAccountIdentifier());

        if (transactionDetailsList != null && transactionDetailsList.size() > 0) {
            TransactionDetails transactionDetails = transactionDetailsList.stream().findAny().orElse(new TransactionDetails());
            Long totalScore = transactionDetailsList.parallelStream()
                    .flatMap(x ->
                            x.getBonus().stream().map(y ->
                                    y.getBonusScore())
                    ).mapToLong(x -> x.longValue()).sum();

            ODSTransactionMessage odsTransactionMessage = ODSTransactionMessage.builder()
                    .emAccountNumber(transactionDetails.getEmAccountNumber())
                    .cycleDate(cycleDate)
                    .bonusEarn(totalScore)
                    .audit(new Audit(jobName))
                    .processedDate(null)
                    .build();
            return odsTransactionMessage;
        }
        return null;
    }
}