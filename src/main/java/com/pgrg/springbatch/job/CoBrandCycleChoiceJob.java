package com.pgrg.springbatch.job;

import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.step.CoBrandCycleChoiceStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CoBrandCycleChoiceJob {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    private CoBrandCycleChoiceStep coBrandCycleChoiceStep;
    @Autowired
    private JobCompletionListener listener;

//    @Bean
//    @Qualifier("coBrand-job")
    public Job jobForRawToScoreJob(String cycleCode) {
        return jobBuilderFactory.get("coBrandJobForChoice")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(coBrandCycleChoiceStep.stepOneForChoice(cycleCode))
                .build();
    }

}
