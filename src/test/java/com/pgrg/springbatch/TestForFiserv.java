package com.pgrg.springbatch;

import com.pgrg.springbatch.config.BatchConfig;
import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.entity.*;
import com.pgrg.springbatch.job.AccountIdentifierJob;
import com.pgrg.springbatch.job.CoBrandCycleChoiceJob;
import com.pgrg.springbatch.listner.JobCompletionListener;
import com.pgrg.springbatch.processor.AccountIdentifierFiservProcessor;
import com.pgrg.springbatch.processor.CoBrandCycleChoiceProcessor;
import com.pgrg.springbatch.reader.subreaders.AccountIdentifierReader;
import com.pgrg.springbatch.repo.primary.AccountMasterRepo;
import com.pgrg.springbatch.repo.primary.CutOffRepo;
import com.pgrg.springbatch.repo.primary.ODSTransactionMessageFiservRepo;
import com.pgrg.springbatch.repo.secondary.TransactionDetailsRepo;
import com.pgrg.springbatch.service.startjob.JobServiceImpl;
import com.pgrg.springbatch.step.AccountIdentifierStep;
import com.pgrg.springbatch.step.CoBrandCycleChoiceStep;
import com.pgrg.springbatch.utils.CustomMessageSource;
import com.pgrg.springbatch.writer.CoBrandCycleChoiceWriter;
import com.pgrg.springbatch.writer.CoBrandCycleWriter;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

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

    @Mock
    private CoBrandCycleChoiceJob coBrandCycleChoiceJob;

    @InjectMocks
    private CoBrandCycleChoiceStep coBrandCycleChoiceStep;

    //    @Mock
    @Autowired
    private AccountIdentifierReader accountIdentifierReader;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private StepExecution stepExecution;

    @InjectMocks
    private AccountIdentifierFiservProcessor accountIdentifierFiservProcessor;


    @InjectMocks
    private CoBrandCycleChoiceProcessor coBrandCycleChoiceProcessor;

    @Mock
    private TransactionDetailsRepo transactionDetailsRepo;

    @InjectMocks
    private JobServiceImpl jobService;

    @Mock
    private BaseRepo<ODSTransactionMessageForChoice> baseRepo2;

    @Mock
    private BaseRepo<ODSTransactionMessage> baseRepo;

    @Mock
    private StepBuilderFactory stepBuilderFactory;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private AccountIdentifierReader reader;

    @Mock
    private CoBrandCycleChoiceProcessor processor;

    @Mock
    private CoBrandCycleChoiceWriter writer;

    @Mock
    private StepBuilder stepBuilder;

    @InjectMocks
    private CoBrandCycleChoiceWriter coBrandCycleChoiceWriter;

    @InjectMocks
    private CoBrandCycleWriter coBrandCycleWriter;

    @Autowired
            private CustomMessageSource customMessageSource;

    List<CutOffDate> cutOffDateList = new ArrayList<>();
    JobParameters Parameters = new JobParameters();
    List<TransactionDetails> transactionDetailsList = new ArrayList<>();
    AccountMaster accountMaster = new AccountMaster();
    List<ODSTransactionMessageForChoice> odsTransactionMessageForChoiceList = new ArrayList<>();
    @Before
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    private void initilizer() {
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
        List<Bonus> bonusList = new ArrayList<>();
        bonusList.add(Bonus.builder()
                .bonusRate("1X")
                .partnerMerchantCategoryCodeDesc("Gas")
                .partnerMerchantCategoryCode("CH01")
                .bonusScore(1005L)
                .bonusCode("2022CH0001").build());

        bonusList.add(Bonus.builder()
                .bonusRate("1X")
                .partnerMerchantCategoryCodeDesc("Cas")
                .partnerMerchantCategoryCode("CH02")
                .bonusScore(100L)
                .bonusCode("2022CH0001").build());

        transactionDetailsList.add(TransactionDetails.builder()
                .emAccountNumber("0000000001")
                ._id("62bcae0ca0c7450f549f1f13")
                .cycledForFiserv("N")
                .cycledForPartner("N")
                .merchantID(0L)
                .transactionAmount(1000L)
                .transactionDate("Fri Jun 17 12:45:00 NPT 2022")
                .transactionPostedDate("Mon Jun 20 12:45:00 NPT 2022")
                .bonus(bonusList)
                .build());
        accountMaster = AccountMaster.builder()
                .accountId("4722524033150054")
                .accountIdentifier("4722524033150054")
                .cycleCode99(14L)
                .productCode(5050L)
                .build();
        odsTransactionMessageForChoiceList.add(ODSTransactionMessageForChoice.builder()
                .bonusList(bonusList)
                .processedDate("Mon Jun 20 12:45:00 NPT 2022")
                .cycleDate("Mon Jun 20 12:45:00 NPT 2022")
                .emAccountNumber("4722524033150054")
                .fdrProductType(1L)
                .bonusEarn(1000L)
                .build());
        odsTransactionMessageForChoiceList.add(ODSTransactionMessageForChoice.builder()
                .bonusList(bonusList)
                .processedDate("Mon Jun 20 12:45:00 NPT 2022")
                .cycleDate("Mon Jun 20 12:45:00 NPT 2022")
                .emAccountNumber("4722524033150053")
                .fdrProductType(1L)
                .bonusEarn(1000L)
                .build());
    }

    @Test
    public void fiserveJobTest() throws Exception {
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, Parameters, "jobFiserv");
        customMessageSource.getMessage("job.start",customMessageSource.getMessage("fiserv"));
        Mockito.when(cutOffRepo.findAll()).thenReturn(cutOffDateList);
        jobLauncher = Mockito.mock(JobLauncher.class);
        jobService = Mockito.mock(JobServiceImpl.class);
        RunIdIncrementer runIdIncrementer = Mockito.mock(RunIdIncrementer.class);
        Job job = Mockito.mock(Job.class);
        Step step = Mockito.mock(Step.class);

//        Mockito.when(accountIdentifierReader.reader("14")).thenReturn(new MongoItemReader<AccountMaster>());
        Mockito.when(accountIdentifierJob.accountIdJob("14")).thenReturn(job);
        Mockito.when(accountIdentifierStep.stepForAccId("14")).thenReturn(step);
        MongoItemReader<AccountMaster> mongoItemReader = accountIdentifierReader.reader("14");

        Mockito.when(transactionDetailsRepo.findTransactionByEmAccountNumber(accountMaster.getAccountIdentifier())).thenReturn(transactionDetailsList);
        ODSTransactionMessage odsTransactionMessage = accountIdentifierFiservProcessor.process(accountMaster);

        List<ODSTransactionMessage> odsTransactionMessageList = new ArrayList<>();
//        Mockito.when(baseRepo.bulkInsert(odsTransactionMessageList, ODSTransactionMessage.class)).thenReturn(10);
        coBrandCycleWriter.write(odsTransactionMessageList);

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

    @Test
    @DisplayName("Choice Test")
    public void choiceJobTest() throws Exception {
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, Parameters, "jobFiserv");
        customMessageSource.getMessage("job.start",customMessageSource.getMessage("choice"));

        /*Mockito.when(accountIdentifierJob.accountIdJob("14")).thenReturn( jobBuilderFactory.get("accountIdJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(accountIdentifierStep.stepForAccId("14"))
                .build() );*/
        jobLauncher = Mockito.mock(JobLauncher.class);
        jobService = Mockito.mock(JobServiceImpl.class);
        cutOffRepo = Mockito.mock(CutOffRepo.class);
        Mockito.when(cutOffRepo.findAll()).thenReturn(cutOffDateList);
        Mockito.when(transactionDetailsRepo.findTransactionByEmAccountNumberForChoice(accountMaster.getAccountIdentifier())).thenReturn(transactionDetailsList);

        RunIdIncrementer runIdIncrementer = Mockito.mock(RunIdIncrementer.class);
        Job job = Mockito.mock(Job.class);
        Step step = Mockito.mock(Step.class);
        Mockito.when(coBrandCycleChoiceJob.jobForRawToScoreJob("14")).thenReturn(job);

        MongoItemReader<AccountMaster> mongoItemReader = accountIdentifierReader.reader("14");

        ODSTransactionMessageForChoice odsTransactionMessage = coBrandCycleChoiceProcessor.process(accountMaster);
        Mockito.when(stepExecution.getJobParameters()).thenReturn(Parameters);
        jobInstance = new JobInstance(1L, "choiceJob");
        jobExecution.setJobInstance(jobInstance);
        stepExecution = new StepExecution("choiceStepExecution", jobExecution);
        coBrandCycleChoiceProcessor.beforeStep(stepExecution);


        List<ODSTransactionMessageForChoice> odsTransactionMessageList = new ArrayList<>();
//        Mockito.when(baseRepo2.bulkInsert(odsTransactionMessageList, ODSTransactionMessageForChoice.class)).thenReturn(10);
        coBrandCycleChoiceWriter.write(odsTransactionMessageForChoiceList);


//        accountIdentifierReader = Mockito.mock(AccountIdentifierReader.class);
//        coBrandCycleChoiceProcessor = Mockito.mock(CoBrandCycleChoiceProcessor.class);
//        coBrandCycleChoiceWriter = Mockito.mock(CoBrandCycleChoiceWriter.class);
//Mockito.when(stepBuilderFactory.get("asdasdasd")).thenReturn(new StepBuilder("asdasd"));
//        Mockito.when(stepBuilderFactory.get("asdasdasd")
//                .<AccountMaster, ODSTransactionMessageForChoice>chunk(500)
//                .reader(accountIdentifierReader.reader("14"))
//                .processor(coBrandCycleChoiceProcessor)
//                .writer(coBrandCycleChoiceWriter).build())
//
//                .thenReturn(stepBuilderFactory.get("stepOneForChoice")
//                .<AccountMaster, ODSTransactionMessageForChoice>chunk(500)
//                .reader(accountIdentifierReader.reader("14"))
//                .processor(coBrandCycleChoiceProcessor)
//                .writer(coBrandCycleChoiceWriter)
//                .build());

        Mockito.when(reader.reader("14")).thenReturn(new MongoItemReader<AccountMaster>());
//        Mockito.when(coBrandCycleChoiceStep.stepOneForChoice("14")).thenReturn(step);


        try {
            Mockito.when(jobLauncher.run(coBrandCycleChoiceJob.jobForRawToScoreJob("14"), Parameters)).thenReturn(jobExecution);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        Mockito.when(jobService.startJobForChoice()).thenReturn(jobExecution);
        JobExecution jobExecution1 = null;

        try {
            jobExecution1 = jobService.startJobForChoice();
        } catch (Exception e) {
            System.out.println("error :" + e.getMessage());
        }
        Assertions.assertEquals(jobExecution, jobExecution1);
    }


}
