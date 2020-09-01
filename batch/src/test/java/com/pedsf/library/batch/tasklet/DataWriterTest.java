package com.pedsf.library.batch.tasklet;

import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.MessageDTO;
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
import org.springframework.kafka.core.KafkaTemplate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DataWriterTest {
   private static final String TOPIC = "ENC(kcIfopiMeuGjSbBppzqMIPTxlKuzDmehEmGdmuzDark=)";

   @Mock
   private KafkaTemplate<String, MessageDTO> kafkaTemplate;

   @InjectMocks
   private DataWriter dataWriter = new DataWriter(TOPIC, kafkaTemplate);

   private static StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
   private static List<MessageDTO> messageDTOS = new ArrayList<>();


   @BeforeAll
   static void beforeAll() {
      List<BorrowingDTO> borrowingDTOS = new ArrayList<>();
      List<BookingDTO> bookingDTOS = new ArrayList<>();
      Date date = new Date();

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

      MediaDTO newMediaDTO = new MediaDTO(newBookDTO);
      UserDTO newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      borrowingDTOS.add(new BorrowingDTO(14,newUserDTO,newMediaDTO, date,date));
      bookingDTOS.add(new BookingDTO(15,newUserDTO,newMediaDTO, date, 2));
      StringBuilder sb = new StringBuilder();
      sb.append("Media return date has passed for the media : ");
      sb.append(newMediaDTO.getMediaType());
      sb.append(" \\n with the title : ");
      sb.append(newMediaDTO.getTitle());
      sb.append(" \\n identified by : ");
      sb.append(newMediaDTO.getEan());
      sb.append(" \\n that you borrow the ");
      sb.append(date);
      sb.append(" \\n thank you for reporting it as soon as possible.");

      messageDTOS.add(new MessageDTO(newUserDTO.getFirstName(),
            newUserDTO.getLastName(),
            "no-reply@lagrandelibrairie.com",
            newUserDTO.getEmail(),
            "Borrowing limit exceeded",
            sb.toString()));

      sb = new StringBuilder();
      sb.append("The media ");
      sb.append(newMediaDTO.getMediaType());
      sb.append(" is available\\n with the title : ");
      sb.append(newMediaDTO.getTitle());
      sb.append(" \\n identified by : ");
      sb.append(newMediaDTO.getEan());
      sb.append(" \\n that you booked the ");
      sb.append(date);
      sb.append(" \\n thank you for pickup it as soon as possible.");


      messageDTOS.add(new MessageDTO(newUserDTO.getFirstName(),
            newUserDTO.getLastName(),
            "no-reply@lagrandelibrairie.com",
            newUserDTO.getEmail(),
            "Media is return",
            sb.toString()));
   }

   @Test
   @Tag("beforeStep")
   @DisplayName("Verify that recovery data from context")
   void beforeStep_returnMessage_ofContext() {

      // GIVEN
      stepExecution.getJobExecution().getExecutionContext().put("messages",messageDTOS);

      // WHEN
      dataWriter.beforeStep(stepExecution);

      // THEN
      assertThat(dataWriter.getMessageDTOS()).isEqualTo(messageDTOS);
   }

   @Test
   @Tag("execute")
   @DisplayName("Verify that data are initialised")
   void execute_returnFinished_ofBorrowingAndBookingNotNull() throws Exception {

      // GIVEN
      StepContribution stepContribution = new StepContribution(stepExecution);
      ChunkContext chunkContext = new ChunkContext( new StepContext(stepExecution));

      // WHEN
      RepeatStatus repeatStatus = dataWriter.execute(stepContribution,chunkContext);

      // THEN
      assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
   }


   @Test
   @Tag("afterStep")
   @DisplayName("Verify that save data into execution context")
   void afterStep_returnNewContext_ofMessages() {

      // WHEN
      ExitStatus exitStatus = dataWriter.afterStep(stepExecution);

      // THEN
      assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
   }
}