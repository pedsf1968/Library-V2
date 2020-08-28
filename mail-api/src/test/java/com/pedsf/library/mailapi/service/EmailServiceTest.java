package com.pedsf.library.mailapi.service;

import com.pedsf.library.dto.global.MessageDTO;
import com.pedsf.library.mailapi.configuration.SpringMailConfig;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBootConfiguration
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@Import( SpringMailConfig.class)
class EmailServiceTest {
   private static final String EMAIL_TEMPLATE = "email.html";
   private static final String SMTP_HOST = "smtp.example.com";
   private static final String SMTP_PORT = "25";
   private static final String PNG_MIME = "image/png";

   @Value("${mail-api.mail.background}")
   private String mailBackground;
   @Value("${mail-api.mail.banner}")
   private String mailBanner;
   @Value("${mail-api.mail.logo}")
   private String mailLogo;
   @Value("${mail-api.mail.logo.background}")
   private String mailLogoBackground;
   @Value("${mail-api.mail.count.down}")
   private Integer mailCountDown;

   @Mock
   private JavaMailSender mailSender;

   @Mock
   private ITemplateEngine templateEngine;

   @InjectMocks
   private EmailService emailService;
   private MessageDTO newMessageDTO;
   private static MimeMessage mimeMessage;

   @BeforeAll
   static void beforeAll() {
      Properties properties = new Properties();
      properties.put("mail.smtp.host", SMTP_HOST);
      properties.put("mail.smtp.port", SMTP_PORT);
      Session session = Session.getDefaultInstance(properties, null);
      mimeMessage= new MimeMessage(session);

   }

   @BeforeEach
   void beforeEach() {
      ReflectionTestUtils.setField(emailService,"mailBackground",this.mailBackground);
      ReflectionTestUtils.setField(emailService,"mailBanner",this.mailBanner);
      ReflectionTestUtils.setField(emailService,"mailLogo",this.mailLogo);
      ReflectionTestUtils.setField(emailService,"mailLogoBackground",this.mailLogoBackground);
      ReflectionTestUtils.setField(emailService,"mailCountDown",this.mailCountDown);

      newMessageDTO = new MessageDTO("Martin",
            "DUPONT",
            "no-reply@lagrandelibrairie.com",
            "martin.dupont@free.fr",
            "Message subject",
            "Message content");
   }

   @Test
   @Tag("sendMailSynch")
   @DisplayName("Verify that is waiting before sending Mail")
   void sendMailSynch() throws InterruptedException {
      // GIVEN
      String output = "OUTPUT";
      when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
      when(templateEngine.process(anyString(),any(Context.class))).thenReturn(output);
      long startTime = new Date().getTime();
      emailService.sendMail(newMessageDTO, new Locale.Builder().build());
      long referenceTime = startTime - new Date().getTime();

      // WHEN
      long endTime = new Date().getTime();
      emailService.sendMailSynch(newMessageDTO, new Locale.Builder().build());

      // THEN
      long endDate = new Date().getTime();
      assertThat(endTime-startTime).isGreaterThan(referenceTime);
   }


   @Test
   @Tag("sendMail")
   @DisplayName("Verify that all datas in mail")
   void sendMail() {
      // GIVEN
      ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
      ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);

      String output = "OUTPUT";
      when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
      when(templateEngine.process(anyString(),any(Context.class))).thenReturn(output);

      // WHEN
      emailService.sendMail(newMessageDTO, new Locale.Builder().build());
      verify(templateEngine).process(stringCaptor.capture(),contextCaptor.capture());
      verify(mailSender).send(mimeMessageCaptor.capture());

      // THEN
      List<String> capturedString = stringCaptor.getAllValues();
      List<Context> capturedContext = contextCaptor.getAllValues();
      List<MimeMessage> capturedMimeMessage = mimeMessageCaptor.getAllValues();
      assertThat(capturedString.get(0)).isEqualTo(EMAIL_TEMPLATE);
      assertThat(capturedContext.get(0).getVariable("toFirstName")).isEqualTo(newMessageDTO.getFirstName());
      assertThat(capturedContext.get(0).getVariable("toLastName")).isEqualTo(newMessageDTO.getLastName());
      assertThat(capturedContext.get(0).getVariable("mailSubject")).isEqualTo(newMessageDTO.getSubject());
      assertThat(capturedContext.get(0).getVariable("mailContent")).isEqualTo(newMessageDTO.getContent());
      Date found = (Date) capturedContext.get(0).getVariable("mailDate");
      assertThat(found).isEqualToIgnoringHours(new Date());
      assertThat(capturedMimeMessage.get(0).getSession().getProperties().get("mail.smtp.host")).isEqualTo(SMTP_HOST);
      assertThat(capturedMimeMessage.get(0).getSession().getProperties().get("mail.smtp.port")).isEqualTo(SMTP_PORT);
   }
}