package com.pgrg.springbatch.job;

import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.step.CoBrandCycleChoiceStep;
import com.pgrg.springbatch.step.CoBrandCycleFiservStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
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
//    @Qualifier("raw-to-score-job")
    public Job jobForRawToScoreJob() {
        return jobBuilderFactory.get("coBrandJobForChoice")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(coBrandCycleChoiceStep.stepOneForChoice())
                .build();
    }

}
