package com.pgrg.springbatch.controller;

import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.job.CoBrandCycleChoiceJob;
import com.pgrg.springbatch.job.CoBrandCycleJob;
import com.pgrg.springbatch.repo.CutOffRepo;
import com.pgrg.springbatch.service.RawJsonFileReader;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CoBrandCycleJob coBrandCycleJob;

    @Autowired
    private CoBrandCycleChoiceJob coBrandCycleChoiceJob;

    @Autowired
    private CutOffRepo cutOffRepo;

    @GetMapping(path = "/read-json") // Start batch process path
    public ResponseEntity<?> readJsonData() throws IOException {
        RawJsonFileReader rawJsonFileReader = new RawJsonFileReader();
        rawJsonFileReader.getAccountMaster("src/main/resources/score-transaction.json");
        Date date = rawJsonFileReader.getCutOffDate("src/main/resources/cut-off-dates.json");

        return new ResponseEntity<>(rawJsonFileReader.getAccountMaster("src/main/resources/score-transaction.json"), HttpStatus.OK);
    }

    @GetMapping(path = "/startv2") // Start batch process path
    public ResponseEntity<String> startBatchv2() throws ParseException {
        List<CutOffDate> cutOffDate = cutOffRepo.findAll();
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        CutOffDate cutOffDateObject = cutOffDate.stream().filter(x-> {
            try {
                return DateUtils.isSameDay(sdf.parse(x.getProcessingDate()), new Date());
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }).findAny().orElse(null);


        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(coBrandCycleJob.jobForRawToScoreJob("aa"), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }

    @GetMapping(path = "/startv3") // Start batch process path
    public ResponseEntity<String> startBatchv3() {
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(coBrandCycleChoiceJob.jobForRawToScoreJob(), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
        return new ResponseEntity<>("Batch Process started!!", HttpStatus.OK);
    }
}
