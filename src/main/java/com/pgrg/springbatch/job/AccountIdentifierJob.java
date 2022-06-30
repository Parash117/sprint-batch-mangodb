package com.pgrg.springbatch.job;

import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.step.AccountIdentifierStep;
import com.pgrg.springbatch.step.CoBrandCycleChoiceStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountIdentifierJob {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    private AccountIdentifierStep accountIdentifierStep;
    @Autowired
    private JobCompletionListener listener;

    //    @Bean
//    @Qualifier("raw-to-score-job")
    public Job accountIdJob(String cycleCode) {
        return jobBuilderFactory.get("accountIdJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(accountIdentifierStep.stepForAccId(cycleCode))
                .build();
    }
}
