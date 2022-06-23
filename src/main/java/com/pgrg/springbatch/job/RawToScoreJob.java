package com.pgrg.springbatch.job;

import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.step.RawDataToScoreStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RawToScoreJob {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    private RawDataToScoreStep rawDataToScoreStep;
    @Autowired
    private JobCompletionListener listener;

//    @Bean
//    @Qualifier("raw-to-score-job")
    public Job jobForRawToScoreJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(rawDataToScoreStep.stepOne())
                .build();
    }

}
