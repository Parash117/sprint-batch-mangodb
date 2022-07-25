package com.pgrg.springbatch;

import com.mongodb.bulk.BulkWriteResult;
import com.pgrg.springbatch.config.BatchConfig;
import com.pgrg.springbatch.dao.BaseRepo;
import com.pgrg.springbatch.dao.BaseRepoImpl;
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
import com.pgrg.springbatch.service.startjob.JobService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.sql.Date;
import java.sql.Timestamp;
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
    private JobService jobServices = new JobServiceImpl();

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

    @InjectMocks
    private JobCompletionListener jobCompletionListener;

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
        cutOffDateList.get(0).getCycleCode();
        cutOffDateList.get(0).getProcessingDate();
        cutOffDateList.get(0).get_id();
        cutOffDateList.get(0).getMonth();
        cutOffDateList.get(0).getYear();
        CutOffDate cutOffDate = new CutOffDate();
        cutOffDate.set_id("");
        cutOffDate.setCycleCode("");
        cutOffDate.setProcessingDate(new Date(new java.util.Date().getTime()));
        cutOffDate.setYear("");

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
        transactionDetailsList.get(0).getBonus();
        transactionDetailsList.get(0).getCycledForFiserv();
        transactionDetailsList.get(0).getEmAccountNumber();
        transactionDetailsList.get(0).getCycledForPartner();
        transactionDetailsList.get(0).getMerchantID();
        transactionDetailsList.get(0).getTransactionAmount();
        transactionDetailsList.get(0).getTransactionDate();
        transactionDetailsList.get(0).getTransactionPostedDate();
        transactionDetailsList.get(0).get_id();


        accountMaster = AccountMaster.builder()
                .accountId("4722524033150054")
                .accountIdentifier("4722524033150054")
                .cycleCode99(14L)
                .productCode(5050L)
                .build();

        accountMaster.set_id("1011100101101010");
        accountMaster.setAccountId("4722524033150054");
        accountMaster.setAccountIdentifier("4722524033150054");
        accountMaster.setCycleCode99(14L);
        accountMaster.setProductCode(5050L);

        accountMaster.get_id();
        accountMaster.getAccountId();
        accountMaster.getAccountIdentifier();
        accountMaster.getCycleCode99();
        accountMaster.getProductCode();

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
                .audit(new Audit())
                .bonusEarn(1000L)
                .build());

        Audit audit = new Audit();
        audit.setCreatedBy("asdasd");
        audit.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
        audit.setModifiedDate(new Timestamp(new java.util.Date().getTime()));
        audit.getCreatedBy();
        audit.getCreatedDate();
        audit.getModifiedDate();
        Audit.builder().build();
        audit = new Audit(new Timestamp(new java.util.Date().getTime()), new Timestamp(new java.util.Date().getTime()), "asdasd");

        Bonus bonus = new Bonus();
        bonus.setBonusCode("asdsad");
        bonus.setBonusRate("x1");
        bonus.setChoiceCategoryCd("asdsad");
        bonus.getBonusScore();
        bonus.getBonusCode();
        bonus.getBonusRate();
        bonus.getChoiceCategoryCd();

        ODSTransactionMessage odsTransactionMessage = new ODSTransactionMessage();
        odsTransactionMessage.set_id(4545L);
        odsTransactionMessage.setAudit(audit);
        odsTransactionMessage.setBonusEarn(45454L);
        odsTransactionMessage.setCycleDate("2022/03/12");
        odsTransactionMessage.setEmAccountNumber("1212154554");
        odsTransactionMessage.setProcessedDate("2022/03/12");
        odsTransactionMessage.get_id();
        odsTransactionMessage.getAudit();
        odsTransactionMessage.getBonusEarn();
        odsTransactionMessage.getCycleDate();
        odsTransactionMessage.getFdrProductType();
        odsTransactionMessage.getEmAccountNumber();
        odsTransactionMessage.getProcessedDate();

        odsTransactionMessage.equals(odsTransactionMessage);

        ODSTransactionMessageForChoice odsTransactionMessageForChoice = new ODSTransactionMessageForChoice();
        odsTransactionMessageForChoice.set_id(12121L);
        odsTransactionMessageForChoice.setAudit(audit);
        odsTransactionMessageForChoice.setBonusEarn(2121L);
        odsTransactionMessageForChoice.setCycleDate("2020/20/20");
        odsTransactionMessageForChoice.setBonusList(bonusList);
        odsTransactionMessageForChoice.setEmAccountNumber("2121215454");
        odsTransactionMessageForChoice.setFdrProductType(121L);
        odsTransactionMessageForChoice.setProcessedDate("2020/20/20");
        odsTransactionMessageForChoice.setPartnerConfirmationNo("asdsadasd");
        odsTransactionMessageForChoice.setPartnerMerchantCategoryCode("12121");
        odsTransactionMessageForChoice.getBonusList();
        odsTransactionMessageForChoice.getCycleDate();
        odsTransactionMessageForChoice.getEmAccountNumber();
        odsTransactionMessageForChoice.getProcessedDate();
        odsTransactionMessageForChoice.get_id();
        odsTransactionMessageForChoice.getPartnerConfirmationNo();
        odsTransactionMessageForChoice.getPartnerMerchantCategoryCode();
        odsTransactionMessageForChoice.getAudit();
        odsTransactionMessageForChoice.getFdrProductType();
        odsTransactionMessageForChoice.getBonusEarn();
    }

    @Test
    public void fiserveJobTest() throws Exception {
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, Parameters, "jobFiserv");
//        jobLauncher = Mockito.mock(JobLauncher.class);
        customMessageSource.getMessage("job.start", customMessageSource.getMessage("fiserv"));
        Mockito.when(cutOffRepo.findAll()).thenReturn(cutOffDateList);

        RunIdIncrementer runIdIncrementer = Mockito.mock(RunIdIncrementer.class);
        Job job = Mockito.mock(Job.class);
        Step step = Mockito.mock(Step.class);

        Mockito.when(accountIdentifierJob.accountIdJob("14")).thenReturn(job);
        Mockito.when(accountIdentifierStep.stepForAccId("14")).thenReturn(step);
        MongoItemReader<AccountMaster> mongoItemReader = accountIdentifierReader.reader("14");
        Mockito.when(reader.reader("14")).thenReturn(new MongoItemReader<AccountMaster>());

        Mockito.when(transactionDetailsRepo.findTransactionByEmAccountNumber(accountMaster.getAccountIdentifier())).thenReturn(transactionDetailsList);
        ODSTransactionMessage odsTransactionMessage = accountIdentifierFiservProcessor.process(accountMaster);

        Mockito.when(stepExecution.getJobParameters()).thenReturn(Parameters);
        jobInstance = new JobInstance(1L, "choiceJob");
        jobExecution.setJobInstance(jobInstance);
        stepExecution = new StepExecution("choiceStepExecution", jobExecution);
        accountIdentifierFiservProcessor.beforeStep(stepExecution);

        List<ODSTransactionMessage> odsTransactionMessageList = new ArrayList<>();
        coBrandCycleWriter.write(odsTransactionMessageList);

        Mockito.when(jobLauncher.run(Mockito.any(), Mockito.any())).thenReturn(jobExecution);
        JobExecution jobExecution1 = null;
        jobExecution1 = jobServices.startJobForAccountIdentifier();
        Assertions.assertEquals(jobExecution, jobExecution1);
    }

    @Test
    @DisplayName("Choice Test")
    public void choiceJobTest() throws Exception {
        JobInstance jobInstance = Mockito.mock(JobInstance.class);
        JobExecution jobExecution = new JobExecution(jobInstance, 1L, Parameters, "jobFiserv");
        customMessageSource.getMessage("job.start", customMessageSource.getMessage("choice"));

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
        coBrandCycleChoiceWriter.write(odsTransactionMessageForChoiceList);
        jobCompletionListener.afterJob(jobExecution);

        Mockito.when(reader.reader("14")).thenReturn(new MongoItemReader<AccountMaster>());

        Mockito.when(jobLauncher.run(Mockito.any(), Mockito.any())).thenReturn(jobExecution);
        JobExecution jobExecution1 = null;
        jobExecution1 = jobServices.startJobForChoice();
        Assertions.assertEquals(jobExecution, jobExecution1);
    }


}
