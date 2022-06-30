package com.pgrg.springbatch.listner;

import com.pgrg.springbatch.repo.ODSTransactionMessageFiservRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

    private final ODSTransactionMessageFiservRepo odsTransactionMessageFiservRepo;

    public JobCompletionListener(ODSTransactionMessageFiservRepo odsTransactionMessageFiservRepo) {
        this.odsTransactionMessageFiservRepo = odsTransactionMessageFiservRepo;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("One job finished.");
        }
    }
}
