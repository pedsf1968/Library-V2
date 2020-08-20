package com.pedsf.library.batch.tasklet;

import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.BorrowingDTO;
import com.pedsf.library.dto.global.MessageDTO;
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


   private final String libraryMailBorrowingLimitSubject;
   private final String libraryMailBorrowingLimitContent;
   private final String libraryMailBookingSubject;
   private final String libraryMailBookingContent;

   private final String from;
   private final List<MessageDTO> messageDTOS = new ArrayList<>();
   private List<BorrowingDTO> borrowingDTOS;
   private List<BookingDTO> bookingDTOS;


   public DataProcessor(String from, String libraryMailBorrowingLimitSubject, String libraryMailBorrowingLimitContent,
                        String libraryMailBookingSubject, String libraryMailBookingContent) {
      this.from = from;
      this.libraryMailBorrowingLimitSubject = libraryMailBorrowingLimitSubject;
      this.libraryMailBorrowingLimitContent = libraryMailBorrowingLimitContent;
      this.libraryMailBookingSubject = libraryMailBookingSubject;
      this.libraryMailBookingContent = libraryMailBookingContent;
   }


   @Override
   public void beforeStep(StepExecution stepExecution) {
      ExecutionContext executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
      this.borrowingDTOS = (List<BorrowingDTO>) executionContext.get("borrowings");
      this.bookingDTOS = (List<BookingDTO>) executionContext.get("bookings");
      stepExecution.getJobExecution().getExecutionContext().remove("borrowings");
      stepExecution.getJobExecution().getExecutionContext().remove("bookings");
      log.info("Data Processor initialized.");
   }

   @Override
   public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

      if(borrowingDTOS!=null) {
         for (BorrowingDTO b : borrowingDTOS) {
            String content = String.format(libraryMailBorrowingLimitContent,
                  b.getMedia().getMediaType(),
                  b.getMedia().getTitle(),
                  b.getMedia().getEan(),
                  b.getBorrowingDate());

            MessageDTO messageDTO = new MessageDTO(
                  b.getUser().getFirstName(),
                  b.getUser().getLastName(),
                  from,
                  b.getUser().getEmail(),
                  libraryMailBorrowingLimitSubject,
                  content);

            log.info("Borrowing : " + b);
            log.info("Message :" + messageDTO);

            messageDTOS.add(messageDTO);
         }
      }

      if(bookingDTOS!=null) {
         for (BookingDTO b : bookingDTOS) {
            String content = String.format(libraryMailBookingContent,
                  b.getMedia().getMediaType(),
                  b.getMedia().getTitle(),
                  b.getMedia().getEan(),
                  b.getBookingDate());

            MessageDTO messageDTO = new MessageDTO(
                  b.getUser().getFirstName(),
                  b.getUser().getLastName(),
                  from,
                  b.getUser().getEmail(),
                  libraryMailBookingSubject,
                  content);
            log.info("Booking : " + b);
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
