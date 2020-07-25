package com.pedsf.library.mailapi.service;

import com.pedsf.library.mailapi.dto.MessageDTO;
import com.pedsf.library.mailapi.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;

@Slf4j
@Service
public class EmailService {
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

   private static final String EMAIL_TEMPLATE = "email.html";
   private static final String PNG_MIME = "image/png";

   @Autowired
   private JavaMailSender mailSender;

   @Autowired
   private TemplateEngine templateEngine;

   /**
    * sendMailAsynch : for the controller MailController
    *                  send mail asynchronously
    *
    * @param messageDTO : message to be send by mail
    * @param locale : not used now
    */
   @Async
   public void sendMailAsynch(MessageDTO messageDTO, Locale locale){
      sendMail(messageDTO, locale);
   }

   /**
    * sendMailSynch : for the listener KafkaConsumer
    *                 use delay to wait before sending a mail due to MailTrap limitation
    *
    * @param messageDTO : message to be send by mail
    * @param locale : not used now
    * @throws InterruptedException
    */
   public void sendMailSynch(MessageDTO messageDTO, Locale locale) throws InterruptedException {
      Thread.sleep(mailCountDown*1000L);
      sendMail(messageDTO, locale);
   }

   /**
    * sendMail : build mail according to a Thymeleaf template and a MessageDTO object
    *
    * @param messageDTO : message to be send by mail
    * @param locale : not used now
    */
   public void sendMail(MessageDTO messageDTO, Locale locale) {
      final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
      final MimeMessageHelper message;

      // Prepare the evaluation context
      final Context ctx = new Context(locale);
      ctx.setVariable("toFirstName", messageDTO.getFirstName());
      ctx.setVariable("toLastName", messageDTO.getLastName());
      ctx.setVariable("mailSubject", messageDTO.getSubject());
      ctx.setVariable("mailContent", messageDTO.getContent());
      ctx.setVariable("mailDate", new Date());

      // Create the HTML body using Thymeleaf
      final String output = templateEngine.process(EMAIL_TEMPLATE, ctx);

      try {
         message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");

         message.setFrom(messageDTO.getFrom());
         message.setTo(messageDTO.getTo());
         message.setSubject(messageDTO.getSubject());
         message.setText(output, true /* isHtml */);

         // Add the inline images, referenced from the HTML code as "cid:image-name"
         message.addInline("background", new ClassPathResource(mailBackground), PNG_MIME);
         message.addInline("library-banner", new ClassPathResource(mailBanner), PNG_MIME);
         message.addInline("library-logo", new ClassPathResource(mailLogo), PNG_MIME);
         message.addInline("logo-background", new ClassPathResource(mailLogoBackground), PNG_MIME);
      } catch (MessagingException ex) {
         throw new BadRequestException(ex.getMessage());
      }

    // Send mail
    this.mailSender.send(mimeMessage);
   }
}
