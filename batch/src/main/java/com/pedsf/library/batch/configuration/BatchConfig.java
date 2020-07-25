package com.pedsf.library.batch.configuration;

import com.pedsf.library.batch.dto.global.MessageDTO;
import com.pedsf.library.batch.tasklet.DataProcessor;
import com.pedsf.library.batch.tasklet.DataReader;
import com.pedsf.library.batch.tasklet.DataWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

   @Value("${library.batch.request.borrowing.endpoint}")
   private String requestBorrowingEndPoint;
   @Value("${library.batch.request.booking.endpoint}")
   private String requestBookingEndPoint;

   @Value("${library.batch.retry.delay}")
   private Integer retryDelay;
   @Value("${library.mail.noReply}")
   private String noReplyEmail;

   @Value("${library.mail.borrowing.subject}")
   private String libraryMailBorrowingLimitSubject;
   @Value("${library.mail.borrowing.content}")
   private String libraryMailBorrowingLimitContent;

   @Value("${library.mail.booking.subject}")
   private String libraryMailBookingSubject;
   @Value("${library.mail.booking.content}")
   private String libraryMailBookingContent;

   @Value("${spring.kafka.topic}")
   private String topic;

   private final JobExecutionListener jobListener = new JobListener();

   private final JobBuilderFactory jobs;
   private final StepBuilderFactory steps;
   private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

   public BatchConfig(JobBuilderFactory jobs, StepBuilderFactory steps, KafkaTemplate<String, MessageDTO> kafkaTemplate) {
      this.jobs = jobs;
      this.steps = steps;
      this.kafkaTemplate = kafkaTemplate;
   }

   @Bean
   protected Step readDatas(){
      return steps
            .get("readDatas")
            .tasklet(new DataReader(requestBorrowingEndPoint, requestBookingEndPoint, retryDelay))
            .build();
   }

   @Bean
   protected Step processDatas() {
      return steps
            .get("processDatas")
            .tasklet(new DataProcessor(noReplyEmail,
                  libraryMailBorrowingLimitSubject, libraryMailBorrowingLimitContent,
                  libraryMailBookingSubject, libraryMailBookingContent))
            .build();
   }

   @Bean
   protected Step writeDatas() {
      return steps
            .get("writeDatas")
            .tasklet(new DataWriter(topic, kafkaTemplate))
            .build();
   }

   @Bean
   public Job job() {
      return jobs
      .get("taskletsJob")
      .start(readDatas())
      .next(processDatas())
      .next(writeDatas())
      .listener( jobListener)
      .build();
   }


   public class JobListener implements JobExecutionListener {

      @Override
      public void beforeJob(JobExecution jobExecution) {
         // Nothing to do
      }

      @Override
      public void afterJob(JobExecution jobExecution){
         if( jobExecution.getStatus() == BatchStatus.COMPLETED ){
            log.info("JOB SUCCEED");
         }
         else if(jobExecution.getStatus() == BatchStatus.FAILED){
            //job failure
            log.info("JOB FAILED");
         }
      }
   }

}
