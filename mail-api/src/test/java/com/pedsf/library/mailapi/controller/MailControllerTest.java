package com.pedsf.library.mailapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.global.MessageDTO;
import com.pedsf.library.exception.BadRequestException;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.mailapi.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = {MailController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)

class MailControllerTest {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private EmailService emailService;
   private MessageDTO newMessageDTO;
   private static ObjectMapper mapper = new ObjectMapper();

   @BeforeEach
   void beforeEach() {

      newMessageDTO = new MessageDTO("Martin",
            "DUPONT",
            "no-reply@lagrandelibrairie.com",
            "martin.dupont@free.fr",
            "Message subject",
            "Message content");

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @Test
   @Tag("sendMail")
   @DisplayName("Verify that we send Message by mail")
   void sendMail_sendMail_ofMessageDTO() throws Exception {

      // GIVEN
      ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
      ArgumentCaptor<Locale> localeCaptor = ArgumentCaptor.forClass(Locale.class);

      String json = mapper.writeValueAsString(newMessageDTO);
      doNothing().when(emailService).sendMail(any(MessageDTO.class), any(Locale.class));

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.post("/mails")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // THEN
      verify(emailService).sendMailAsynch(messageDTOCaptor.capture(),localeCaptor.capture());
      MessageDTO found = messageDTOCaptor.getAllValues().get(0);
      assertThat(found).isEqualTo(newMessageDTO);
   }

   @Test
   @Tag("sendMail")
   @DisplayName("Verify that we get BadRequest if there are mail exception")
   void sendMail_returnBadRequest_ofSendMailException() throws Exception {

      // GIVEN
      String json = mapper.writeValueAsString(newMessageDTO);
      Mockito.doThrow(BadRequestException.class).when(emailService).sendMailAsynch(any(MessageDTO.class),any(Locale.class));

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.post("/mails")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
   }

}