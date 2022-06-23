package com.pgrg.springbatch.controller;

import com.pgrg.springbatch.job.ODSTransactionJob;
import com.pgrg.springbatch.job.RawToScoreJob;
import com.pgrg.springbatch.reader.RawDataReader;
import com.pgrg.springbatch.service.RawJsonFileReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping(path = "/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;
//    @Autowired
//    private Job job;
    @Autowired
    private ODSTransactionJob odsTransactionJob;

    @Autowired
    private RawToScoreJob rawToScoreJob;

    /*@GetMapping(path = "/start") // Start batch process path
    public ResponseEntity<String> startBatch() {
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }*/

   /* @GetMapping(path = "/startv2") // Start batch process path
    public ResponseEntity<String> startBatchv2() {
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(odsTransactionJob.job(), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }
*/
    @GetMapping(path = "/read-json") // Start batch process path
    public ResponseEntity<?> readJsonData() throws IOException {
        RawJsonFileReader rawJsonFileReader = new RawJsonFileReader();
        rawJsonFileReader.getAccountMaster("src/main/resources/account-master.json");
        Date date = rawJsonFileReader.getCutOffDate("src/main/resources/cut-off-dates.json");

        return new ResponseEntity<>(rawJsonFileReader.getAccountMaster("src/main/resources/account-master.json"), HttpStatus.OK);
    }

    @GetMapping(path = "/startv2") // Start batch process path
    public ResponseEntity<String> startBatchv2() {
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(rawToScoreJob.jobForRawToScoreJob(), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }
}
