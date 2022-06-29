package com.pgrg.springbatch.service.startjob;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.job.CoBrandCycleChoiceJob;
import com.pgrg.springbatch.job.CoBrandCycleJob;
import com.pgrg.springbatch.repo.AccountMasterRepo;
import com.pgrg.springbatch.repo.CutOffRepo;
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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CoBrandCycleJob coBrandCycleJob;

    @Autowired
    private CoBrandCycleChoiceJob coBrandCycleChoiceJob;

    @Autowired
    private CutOffRepo cutOffRepo;

    @Autowired
    private AccountMasterRepo accountMasterRepo;

    @Override
    public void startJobForFiserv() throws ParseException {
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

        Long dateInLong = sdf.parse(cutOffDateObject.getProcessingDate()).getTime();
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addLong("cutOffDate", dateInLong)
                .addString("cycleCode", cutOffDateObject.getCycleCode())
                .toJobParameters();
        List<AccountMaster> accountMaster = accountMasterRepo.findByCycleCode99(Long.valueOf(cutOffDateObject.getCycleCode()));
        try {
            jobLauncher.run(coBrandCycleJob.jobForRawToScoreJob(), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void startJobForChoice() throws ParseException {
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

        Long dateInLong = sdf.parse(cutOffDateObject.getProcessingDate()).getTime();

        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addLong("cutOffDate", dateInLong)
                .toJobParameters();
        try {
            jobLauncher.run(coBrandCycleChoiceJob.jobForRawToScoreJob(), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
    }
}
