package com.library.batch.tasklet;

import com.library.batch.dto.business.BorrowingDTO;
import com.library.batch.dto.global.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataProcessor implements Tasklet, StepExecutionListener {


   private final String libraryMailLimitSubject;
   private final String libraryMailLimitContent;

   private final String from;
   private List<BorrowingDTO> borrowingDTOS;
   private List<MessageDTO> messageDTOS = new ArrayList<>();

   public DataProcessor(String from, String libraryMailLimitSubject, String libraryMailLimitContent) {
      this.from = from;
      this.libraryMailLimitSubject = libraryMailLimitSubject;
      this.libraryMailLimitContent = libraryMailLimitContent;
   }


   @Override
   public void beforeStep(StepExecution stepExecution) {
      ExecutionContext executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
      this.borrowingDTOS = (List<BorrowingDTO>) executionContext.get("borrowings");
      stepExecution.getJobExecution().getExecutionContext().remove("borrowings");
      log.info("Data Processor initialized.");
   }

   @Override
   public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

      if(borrowingDTOS!=null) {
         for (BorrowingDTO b : borrowingDTOS) {
            MessageDTO messageDTO = new MessageDTO();

            messageDTO.setFrom(from);
            messageDTO.setTo(b.getUser().getEmail());
            messageDTO.setFirstName(b.getUser().getFirstName());
            messageDTO.setLastName(b.getUser().getLastName());
            messageDTO.setSubject(libraryMailLimitSubject);


            String content = String.format(libraryMailLimitContent,
                  b.getMedia().getMediaType(),
                  b.getMedia().getTitle(),
                  b.getMedia().getEan(),
                  b.getBorrowingDate());

            messageDTO.setContent(content);
            log.info("Borrowing : " + b);
            log.info("Message :" + messageDTO);

            messageDTOS.add(messageDTO);
         }
      }

      return RepeatStatus.FINISHED;
   }

   @Override
   public ExitStatus afterStep(StepExecution stepExecution) {
      stepExecution.getJobExecution().getExecutionContext().put("messages", this.messageDTOS);
      log.info("Data Processor ended.");
      return ExitStatus.COMPLETED;
   }

}
