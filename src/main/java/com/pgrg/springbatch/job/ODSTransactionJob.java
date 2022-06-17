package com.pgrg.springbatch.job;

import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.step.ODSTransactionStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ODSTransactionJob {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    private ODSTransactionStep odsTransactionStep;
    @Autowired
    private JobCompletionListener listener;

    public Job job() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(odsTransactionStep.stepOne())
                .build();
    }

}
