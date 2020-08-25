package com.pedsf.library.batch.tasklet;

import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBatchTest
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DataReaderTest {
   private static final String BORROWING_ENDPOINT = "http://localhost:7000/borrowings/delayed/";
   private static final String BOOKING_ENDPOINT = "http://localhost:2200/library-api/bookings/pickup/";
   private static final String DATE_FORMAT = "ddMMyyyy";
   private static final int RETRY_DELAY = 1;

   @Mock
   private RestTemplate restTemplate;

   @InjectMocks
   private DataReader dataReader = new DataReader(
         BORROWING_ENDPOINT,
         BOOKING_ENDPOINT,
         RETRY_DELAY);
   private static StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
   private static List<BorrowingDTO> borrowingDTOS = new ArrayList<>();
   private static List<BookingDTO> bookingDTOS = new ArrayList<>();
   private static Date date = new Date();
   private static UserDTO newUserDTO;
   private static MediaDTO newMediaDTO;

   @BeforeAll
   static void beforeAll() {
      BookDTO newBookDTO = new BookDTO("954-8789797","The green tomato",1,1,"9548789797",
            new PersonDTO(3, "Victor", "HUGO", java.sql.Date.valueOf("1802-02-26")),
            new PersonDTO(10, "LGF", "Librairie Générale Française", null));
      newBookDTO.setPages(125);
      newBookDTO.setFormat("COMICS");
      newBookDTO.setType("HUMOR");
      newBookDTO.setHeight(11);
      newBookDTO.setLength(11);
      newBookDTO.setWidth(11);
      newBookDTO.setWeight(220);
      newBookDTO.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      newMediaDTO = new MediaDTO(newBookDTO);
      newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      borrowingDTOS.add(new BorrowingDTO(14,newUserDTO,newMediaDTO, date,date));
      bookingDTOS.add(new BookingDTO(15,newUserDTO,newMediaDTO, date, 2));
   }

   @Test
   @Tag("beforeStep")
   @DisplayName("Verify that recovery data from httprequest")
   void beforeStep_returnBorrowingAndBooking_ofHttpRequest() {
      SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
      String date = format.format(new Date());

      // GIVEN
      ResponseEntity<List<BorrowingDTO>> responseEntityBorrowing = ResponseEntity.ok(borrowingDTOS);
      when( restTemplate.exchange(
                  BORROWING_ENDPOINT + date,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<BorrowingDTO>>() { }))
            .thenReturn(responseEntityBorrowing);

      ResponseEntity<List<BookingDTO>> responseEntityBooking = ResponseEntity.ok(bookingDTOS);
      when( restTemplate.exchange(
                  BOOKING_ENDPOINT,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<BookingDTO>>() { }))
            .thenReturn(responseEntityBooking);

      // WHEN
      dataReader.beforeStep(stepExecution);

      // THEN
      assertThat(dataReader.getBookingDTOS()).isEqualTo(bookingDTOS);
      assertThat(dataReader.getBorrowingDTOS()).isEqualTo(borrowingDTOS);
   }

   @Test
   @Tag("execute")
   @DisplayName("Verify that data are initialised")
   void execute_returnFinished_ofBorrowingAndBookingNotNull() throws Exception {

      // GIVEN
      dataReader.setBorrowingDTOS(borrowingDTOS);
      dataReader.setBookingDTOS(bookingDTOS);
      StepContribution stepContribution = new StepContribution(stepExecution);
      ChunkContext chunkContext = new ChunkContext( new StepContext(stepExecution));

      // WHEN
      RepeatStatus repeatStatus = dataReader.execute(stepContribution,chunkContext);

      // THEN
      assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
   }

   @Test
   @Tag("execute")
   @DisplayName("Verify that restart previous step if datas are null")
   void execute_restartPreviousStep_ofBorrowingAndBookingNull() throws Exception {

      // GIVEN
      StepContribution stepContribution = new StepContribution(stepExecution);
      ChunkContext chunkContext = new ChunkContext( new StepContext(stepExecution));

      // WHEN
      Assertions.assertThrows(Exception.class, () -> dataReader.execute(stepContribution,chunkContext));
   }

   @Test
   @Tag("afterStep")
   @DisplayName("Verify that save data into execution context")
   void afterStep_returnNewContext_ofMessages() {
      // GIVEN
      dataReader.setBorrowingDTOS(borrowingDTOS);
      dataReader.setBookingDTOS(bookingDTOS);

      // WHEN
      ExitStatus exitStatus = dataReader.afterStep(stepExecution);

      // THEN
      assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
      assertThat(stepExecution.getJobExecution().getExecutionContext().containsKey("borrowings")).isTrue();
      assertThat(stepExecution.getJobExecution().getExecutionContext().get("borrowings")).isEqualTo(borrowingDTOS);
      assertThat(stepExecution.getJobExecution().getExecutionContext().containsKey("bookings")).isTrue();
      assertThat(stepExecution.getJobExecution().getExecutionContext().get("bookings")).isEqualTo(bookingDTOS);

   }
}