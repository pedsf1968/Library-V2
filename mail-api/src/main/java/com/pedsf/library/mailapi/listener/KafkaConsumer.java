package com.pedsf.library.mailapi.listener;

import com.pedsf.library.mailapi.dto.MessageDTO;
import com.pedsf.library.mailapi.exception.BadRequestException;
import com.pedsf.library.mailapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class KafkaConsumer {
   private final EmailService emailService;
   private Locale locale;

   public KafkaConsumer(EmailService emailService) {
      this.emailService = emailService;
   }

   /**
    * consumeMessageDTO : get MessageDTO from Karafka and send it to mailing method
    *
    * @param messageDTO : message to be send by mail
    */
   @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "messageDTOConcurrentKafkaListenerContainerFactory")
   public void consumeMessageDTO( MessageDTO messageDTO) {

      try {
         log.info("Message : " + messageDTO);
         emailService.sendMailSynch(messageDTO, locale);
      } catch (BadRequestException | InterruptedException ex) {
         log.error(ex.getMessage());
         Thread.currentThread().interrupt();
      }
   }
}


