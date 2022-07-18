package com.pgrg.springbatch.service.startjob;

import org.springframework.batch.core.JobExecution;

import java.text.ParseException;

public interface JobService {

    JobExecution startJobForChoice() ;

    JobExecution startJobForAccountIdentifier();
}
