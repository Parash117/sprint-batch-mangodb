package com.pgrg.springbatch;


import com.pgrg.springbatch.config.BatchConfig;
import com.pgrg.springbatch.entity.Bonus;
import com.pgrg.springbatch.entity.CutOffDate;
import com.pgrg.springbatch.entity.TransactionDetails;
import com.pgrg.springbatch.job.CoBrandCycleChoiceJob;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { BatchConfig.class, CoBrandCycleChoiceJob.class })
public class Test1 {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    List<CutOffDate> cutOffDateList = new ArrayList<>();
    JobParameters Parameters = new JobParameters();
    List<TransactionDetails> transactionDetailsList = new ArrayList<>();

    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("file.input", "TEST_INPUT");
        paramsBuilder.addString("file.output", "TEST_OUTPUT");
        return paramsBuilder.toJobParameters();
    }
    @Test
    public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
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
        transactionDetailsList.add(TransactionDetails.builder()
                .emAccountNumber("0000000001")
                .cycledForFiserv("N")
                .cycledForPartner("N")
                .merchantID(0L)
                .bonus(new ArrayList<Bonus>(){{
                    Bonus.builder().partnerMerchantCategoryCodeDesc("CHI@").bonusScore(1005L).bonusCode("CH1").build();
                }})
                .build());
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
//        assertThat(actualJobInstance.getJobName(), is("transformBooksRecords"));
//        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
//        AssertFile.assertFileEquals(expectedResult, actualResult);
    }

}
