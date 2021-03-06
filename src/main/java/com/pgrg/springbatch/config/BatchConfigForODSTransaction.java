//package com.pgrg.springbatch.config;
//
//import com.pgrg.springbatch.entity.ODSTransactionMessage;
//import com.pgrg.springbatch.listner.JobCompletionListener;
//import com.pgrg.springbatch.processor.ODSTransactionProcessor;
//import com.pgrg.springbatch.repo.ODSTxMsgRepo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.data.MongoItemWriter;
//import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
//import org.springframework.batch.item.json.JacksonJsonObjectReader;
//import org.springframework.batch.item.json.JsonItemReader;
//import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.IdentityHashMap;
//import java.util.Map;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Slf4j
//@Configuration
//public class BatchConfigForODSTransaction {
//    @Autowired
//    public JobBuilderFactory jobBuilderFactory;
//    @Autowired
//    public StepBuilderFactory stepBuilderFactory;
//    @Autowired
//    private JobLauncher jobLauncher;
//    @Autowired
//    private JobCompletionListener jobCompletionNotificationListener;
//    @Autowired
//    private ODSTxMsgRepo odsTxMsgRepo;
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();
//    private AtomicBoolean enabled = new AtomicBoolean(true);
//    private AtomicInteger batchRunCounter = new AtomicInteger(0);
//
//    @Bean
//    @Qualifier("odsTransactionReaderBean")
//    public JsonItemReader<ODSTransactionMessage> reader() {
//        return new JsonItemReaderBuilder<ODSTransactionMessage>()
//                .jsonObjectReader(new JacksonJsonObjectReader<>(ODSTransactionMessage.class))
//                .resource(new ClassPathResource("odstransaction-message.json"))
//                .name("ODSTransactionReader")
//                .build();
//    }
//
//    @Bean
//    @Qualifier("odsTransactionReaderBean")
//    public ODSTransactionProcessor processor() {
//        return new ODSTransactionProcessor();
//    }
//
//    @Bean
//    @Qualifier("odsTransactionStepBean")
//    public Step stepOne() {
//        return stepBuilderFactory.get("stepOne")
//                .<ODSTransactionMessage, ODSTransactionMessage>chunk(20)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer(mongoTemplate))
//                .build();
//    }
//
//    @Bean
//    @Qualifier("odsTransactionJob")
//    public Job importUserJob(JobCompletionListener listener, @Qualifier("odsTransactionStepBean") Step step1) {
//        return jobBuilderFactory.get("importUserJob")
//                .incrementer(new RunIdIncrementer())
//                .listener(listener)
////                .flow(step1)
////                .end()
//                .start(step1)
//                .build();
//    }
//
//    @Bean
//    @Qualifier("odsTransactionBean")
//    public MongoItemWriter<ODSTransactionMessage> writer(MongoTemplate mongoTemplate) {
//        return new MongoItemWriterBuilder<ODSTransactionMessage>()
//                .template(mongoTemplate).collection("ODSTransactionMessage")
//                .build();
//    }
//
//
//}
