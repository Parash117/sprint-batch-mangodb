package com.pgrg.springbatch.service.startjob;

import com.pgrg.springbatch.entity.AccountMaster;
import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.entity.TransactionDetails;
import com.pgrg.springbatch.job.AccountIdentifierJob;
import com.pgrg.springbatch.job.CoBrandCycleChoiceJob;
import com.pgrg.springbatch.repo.primary.AccountMasterRepo;
import com.pgrg.springbatch.repo.primary.CutOffRepo;
import com.pgrg.springbatch.repo.secondary.TransactionDetailsRepo;
import com.pgrg.springbatch.utils.CustomMessageSource;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CoBrandCycleChoiceJob coBrandCycleChoiceJob;

    @Autowired
    private CutOffRepo cutOffRepo;

    @Autowired
    private AccountMasterRepo accountMasterRepo;

    @Autowired
    private AccountIdentifierJob accountIdentifierJob;

    @Autowired
    private TransactionDetailsRepo transactionDetailsRepo;

    @Autowired
    private CustomMessageSource messageSource;

    @Override
    public void startJobForChoice() throws ParseException {
        List<CutOffDate> cutOffDate = cutOffRepo.findAll();
        Date yesterdayDate = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());
        CutOffDate cutOffDateObject = cutOffDate.stream().filter(x-> {
                return DateUtils.isSameDay((x.getProcessingDate()), yesterdayDate);
        }).findAny().orElseThrow(()-> new RuntimeException( messageSource.getMessage("not.found", messageSource.getMessage("cycledate"))));

        Long dateInLong = cutOffDateObject.getProcessingDate().getTime(); //sdf.parse(cutOffDateObject.getProcessingDate()).getTime();
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addLong("cutOffDate", dateInLong)
                .addString("cycleDate", cutOffDateObject.getProcessingDate().toString())
                .addString("cycleCode", cutOffDateObject.getCycleCode())
                .toJobParameters();
        try {
            jobLauncher.run(coBrandCycleChoiceJob.jobForRawToScoreJob(cutOffDateObject.getCycleCode()), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void startJobForAccountIdentifier() throws ParseException {
        List<CutOffDate> cutOffDate = cutOffRepo.findAll();
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        CutOffDate cutOffDateObject = cutOffDate.stream().filter(x-> {
                return DateUtils.isSameDay((x.getProcessingDate()), new Date());
        }).findAny().orElseThrow(()-> new RuntimeException( messageSource.getMessage("not.found", messageSource.getMessage("cycledate"))));
        if(cutOffDateObject == null ){
            throw new RuntimeException("No cycle Date For today");
        }
        Long dateInLong = cutOffDateObject.getProcessingDate().getTime();
        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addLong("cutOffDate", dateInLong)
                .addString("cycleDate", cutOffDateObject.getProcessingDate().toString())
                .addString("cycleCode", cutOffDateObject.getCycleCode())
                .toJobParameters();
        List<AccountMaster> accountMaster = accountMasterRepo.findByCycleCode99(Long.valueOf(cutOffDateObject.getCycleCode()));
        List<TransactionDetails> transactionDetails = transactionDetailsRepo.findAll();
        try {
            jobLauncher.run(accountIdentifierJob.accountIdJob(cutOffDateObject.getCycleCode()), Parameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
    }
}
