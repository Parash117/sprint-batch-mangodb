package com.pgrg.springbatch.service.startjob;

import org.springframework.batch.core.JobExecution;

import java.text.ParseException;

public interface JobService {

    void startJobForChoice() throws ParseException;

    JobExecution startJobForAccountIdentifier() throws ParseException;
}
