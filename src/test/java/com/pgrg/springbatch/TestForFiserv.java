package com.pgrg.springbatch;

import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.job.AccountIdentifierJob;
import com.pgrg.springbatch.job.CoBrandCycleChoiceJob;
import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.repo.primary.AccountMasterRepo;
import com.pgrg.springbatch.repo.primary.CutOffRepo;
import com.pgrg.springbatch.repo.primary.ODSTransactionMessageFiservRepo;
import com.pgrg.springbatch.service.startjob.JobServiceImpl;
import com.pgrg.springbatch.step.AccountIdentifierStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestForFiserv {


    @Mock
    private CutOffRepo cutOffRepo;

    @Mock
    private AccountIdentifierJob accountIdentifierJob;

    @Mock
    private JobLauncher jobLauncher;


    @InjectMocks
    private JobServiceImpl jobService;

    @Test
    public void fiserveJobTest(){
        List<CutOffDate> cutOffDateList = new ArrayList<>();
        cutOffDateList.add(CutOffDate.builder()
                .cycleCode("14")
                .processingDate(new Date(Instant.now().toEpochMilli()))
                .year("2022").build());
        cutOffDateList.add(CutOffDate.builder()
                .cycleCode("14")
                .processingDate(new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()))
                .year("2022").build());

//        AccountIdentifierJob accountIdentifierJob = Mockito.mock(AccountIdentifierJob.class);
        JobInstance jobInstance = Mockito.mock(JobInstance.class);

        JobParameters Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addLong("cutOffDate", Instant.now().toEpochMilli())
                .addString("cycleDate", new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()).toString())
                .addString("cycleCode", "14")
                .toJobParameters();

        JobExecution jobExecution = new JobExecution(jobInstance, 1L, Parameters, "jobFiserv");

        Mockito.when(cutOffRepo.findAll()).thenReturn(cutOffDateList);
        /*Mockito.when(accountIdentifierJob.accountIdJob("14")).thenReturn( jobBuilderFactory.get("accountIdJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(accountIdentifierStep.stepForAccId("14"))
                .build() );*/
        jobLauncher = Mockito.mock(JobLauncher.class);
        try {
        Mockito.when(jobLauncher.run(Mockito.any(), Mockito.any())).thenReturn(jobExecution);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {

            e.printStackTrace();
        }
        JobExecution jobExecution1 = null;

        try {
            jobExecution1 = jobService.startJobForAccountIdentifier();
        }
        catch (Exception e){
            System.out.println("error :"+e.getMessage());
        }

        System.out.println("");
    }

}
