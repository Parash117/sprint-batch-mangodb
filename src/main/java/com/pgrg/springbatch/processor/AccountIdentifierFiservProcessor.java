package com.pgrg.springbatch.processor;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.Audit;
import com.pgrg.springbatch.entity.ODSTransactionMessage;
import com.pgrg.springbatch.entity.TransactionDetails;
import com.pgrg.springbatch.repo.secondary.TransactionDetailsRepo;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component("acc-id-processor")
public class AccountIdentifierFiservProcessor implements ItemProcessor<AccountMaster, ODSTransactionMessage> {

    @Autowired
    private TransactionDetailsRepo transactionDetailsRepo;
    private String cycleDate;
    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        cycleDate = jobParameters.getString("cycleDate");
    }

    @Override
    public ODSTransactionMessage process(AccountMaster item) throws Exception {
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepo.findTransactionByEmAccountNumber(item.getAccountIdentifier());

        if(transactionDetailsList != null && transactionDetailsList.size()>0) {
            TransactionDetails transactionDetails = transactionDetailsList.stream().findAny().orElse(new TransactionDetails());
            BigDecimal totalScore = transactionDetailsList.parallelStream()
//                    .filter(x-> "N".equalsIgnoreCase(x.getCycledForFiserv()))
                    .flatMap(x -> {
                                x.setCycledForFiserv("Y");
                                transactionDetailsRepo.save(x);
                                return x.getBonus().stream().map(y ->
                                        y.getPointsEarned());
                            }
                    ).reduce(BigDecimal.ZERO, BigDecimal::add);

                ODSTransactionMessage odsTransactionMessage = ODSTransactionMessage.builder()
                        .crn(transactionDetails.getEmAccountNumber())
                        .cycleDate(cycleDate)
                        .totalPointsEarned(totalScore)
                        .processedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                        .audit(new Audit())
                        .build();
                return odsTransactionMessage;
        }
        return null;
    }
}
