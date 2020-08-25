package com.pedsf.library.batch.tasklet;

import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.BorrowingDTO;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
public class DataReader implements Tasklet, StepExecutionListener {
   private static final String DATE_FORMAT = "ddMMyyyy";
   private final String requestBookingEndPoint;
   private final String requestBorrowingEndPoint;
   private final Integer retryDelay;
   private List<BorrowingDTO> borrowingDTOS;
   private List<BookingDTO> bookingDTOS;
   @Autowired
   private RestTemplate restTemplate;


   public DataReader(String requestBorrowingEndPoint, String requestBookingEndPoint, Integer retryDelay) {
      this.requestBookingEndPoint = requestBookingEndPoint;
      this.requestBorrowingEndPoint = requestBorrowingEndPoint;
      this.retryDelay = retryDelay;
   }

   @SneakyThrows
   @Override
   public void beforeStep(StepExecution stepExecution) {
      ResponseEntity<List<BorrowingDTO>> responseEntityBorrowing = null;
      ResponseEntity<List<BookingDTO>> responseEntityBooking = null;

      // calculate the date daysOfDelay before now
      SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
      String url = requestBorrowingEndPoint + format.format(new Date());
      log.info("GET Request : " + url);

      try {
         responseEntityBorrowing = restTemplate.exchange(
               url,
               HttpMethod.GET,
               null,
               new ParameterizedTypeReference<List<BorrowingDTO>>() {
               });
         this.borrowingDTOS = responseEntityBorrowing.getBody();
      } catch (RestClientException ex) {
         log.error( ex.getMessage());
         log.info("Retry delay : " + retryDelay + " seconds" );
         if(retryDelay>0) {
            Thread.sleep(retryDelay * 1000L);
            beforeStep(stepExecution);
         }
      }

      try {
         responseEntityBooking = restTemplate.exchange(
               requestBookingEndPoint,
               HttpMethod.GET,
               null,
               new ParameterizedTypeReference<List<BookingDTO>>() {
               });
         this.bookingDTOS = responseEntityBooking.getBody();
      } catch (RestClientException ex) {
         log.error( ex.getMessage());
         log.info("Retry delay : " + retryDelay + " seconds" );
         if(retryDelay>0) {
            Thread.sleep(retryDelay * 1000L);
            beforeStep(stepExecution);
         }
      }

      log.info("Data Reader initialized.");
   }

   @Override
   public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

         if(borrowingDTOS==null && bookingDTOS==null) {
            this.beforeStep(stepContribution.getStepExecution());
         }

         return RepeatStatus.FINISHED;
   }

   @Override
   public ExitStatus afterStep(StepExecution stepExecution) {
      stepExecution.getJobExecution().getExecutionContext().put("borrowings", this.borrowingDTOS);
      stepExecution.getJobExecution().getExecutionContext().put("bookings", this.bookingDTOS);
      if(borrowingDTOS!=null) {
         for (BorrowingDTO b : borrowingDTOS) {
            log.info(b.toString());
         }
      }
      if(bookingDTOS!=null) {
         for (BookingDTO b : bookingDTOS) {
            log.info(b.toString());
         }
      }
      log.info("Datas Reader ended.");
      return ExitStatus.COMPLETED;
   }

}
