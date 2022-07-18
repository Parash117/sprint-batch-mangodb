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
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

    @Mock
    private JobCompletionListener listener;

    @Mock
    public JobBuilderFactory jobBuilderFactory;

    @Mock
    private AccountIdentifierStep accountIdentifierStep;

    @InjectMocks
    private JobServiceImpl jobService;

    List<CutOffDate> cutOffDateList = new ArrayList<>();
    JobParameters Parameters = new JobParameters();

    @Before
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    private void initilizer(){
        cutOffDateList.add(CutOffDate.builder()
                .cycleCode("14")
                .processingDate(new Date(Instant.now().toEpochMilli()))
                .year("2022").build());
        cutOffDateList.add(CutOffDate.builder()
                .cycleCode("14")
                .processingDate(new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()))
                .year("2022").build());

        Parameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addLong("cutOffDate", Instant.now().toEpochMilli())
                .addString("cycleDate", new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()).toString())
                .addString("cycleCode", "14")
                .toJobParameters();

    }

    @Test
    public void fiserveJobTest() {
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, Parameters, "jobFiserv");

        Mockito.when(cutOffRepo.findAll()).thenReturn(cutOffDateList);
        /*Mockito.when(accountIdentifierJob.accountIdJob("14")).thenReturn( jobBuilderFactory.get("accountIdJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(accountIdentifierStep.stepForAccId("14"))
                .build() );*/
        jobLauncher = Mockito.mock(JobLauncher.class);
        jobService = Mockito.mock(JobServiceImpl.class);
        RunIdIncrementer runIdIncrementer = Mockito.mock(RunIdIncrementer.class);
        Job job = Mockito.mock(Job.class);
        Step step = Mockito.mock(Step.class);

        Mockito.when(accountIdentifierJob.accountIdJob("14")).thenReturn(job);
        Mockito.when(accountIdentifierStep.stepForAccId("14")).thenReturn(step);
        try {
            Mockito.when(jobLauncher.run(accountIdentifierJob.accountIdJob("14"), Parameters)).thenReturn(jobExecution);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        Mockito.when(jobService.startJobForAccountIdentifier()).thenReturn(jobExecution);
        JobExecution jobExecution1 = null;

        try {
            jobExecution1 = jobService.startJobForAccountIdentifier();
        } catch (Exception e) {
            System.out.println("error :" + e.getMessage());
        }
        Assertions.assertEquals(jobExecution, jobExecution1);
    }

}
