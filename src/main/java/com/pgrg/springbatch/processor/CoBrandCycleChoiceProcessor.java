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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            Map<String, Long> odsItemSumMap = transactionDetailsList.stream()
                    .flatMap(x -> x.getBonus().stream()).collect(
                    Collectors.groupingBy(y ->
                                    y.getPartnerMerchantCategoryCode(),
                            Collectors.summingLong(y -> y.getBonusScore())
                    )
            );

            List<Bonus> bonusList = new ArrayList<>();
            odsItemSumMap.entrySet().parallelStream().forEach(z -> {
                bonusList.add(Bonus.builder()
                        .partnerMerchantCategoryCode(z.getKey())
                        .bonusScore(z.getValue())
                        .build());
            });

            ODSTransactionMessageForChoice odsTransactionMessage = ODSTransactionMessageForChoice.builder()
                    .emAccountNumber(transactionDetails.getEmAccountNumber())
                    .cycleDate(cycleDate)
                    .bonusList(bonusList)
                    .processedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                    .audit(new Audit())
                    .build();
            return odsTransactionMessage;
        }
        return null;
    }
}
