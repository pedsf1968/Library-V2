package com.pedsf.library.batch.tasklet;

import com.pedsf.library.dto.global.MessageDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class DataWriter implements Tasklet, StepExecutionListener {

   private List<MessageDTO> messageDTOS = new ArrayList<>();

   private final String topic;
   private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

   public DataWriter(String topic, KafkaTemplate<String, MessageDTO> kafkaTemplate) {
      this.topic = topic;
      this.kafkaTemplate = kafkaTemplate;
   }


   @Override
   public void beforeStep(StepExecution stepExecution) {
      ExecutionContext executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
      this.messageDTOS = (List<MessageDTO>) executionContext.get("messages");
      stepExecution.getJobExecution().getExecutionContext().remove("messages");
      log.info("Data Writer initialized.");
   }


   @Override
   public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

      for(MessageDTO messageDTO: messageDTOS) {
         log.info("Send message to Kafka : " + messageDTO);

         kafkaTemplate.send(topic, messageDTO);
      }
      return RepeatStatus.FINISHED;
   }

   @Override
   public ExitStatus afterStep(StepExecution stepExecution) {

      log.info("Data Writer ended.");
      return ExitStatus.COMPLETED;
   }
}
