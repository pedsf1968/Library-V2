package com.pedsf.library.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class BatchConfigTest {

   @Value("${library.mail.noReply}")
   private String noReplyEmail;

   @Value("${library.mail.borrowing.subject}")
   private String libraryMailBorrowingLimitSubject;
   @Value("${library.mail.borrowing.content}")
   private String libraryMailBorrowingLimitContent;
   @Value("${library.mail.booking.subject}")
   private String libraryMailBookingSubject;
   @Value("${library.mail.booking.content}")
   private String libraryMailBookingContent;

   @BeforeEach
   void beforeEach() {
   }

   @Disabled
   @Test
   void init() {
      assertThat(noReplyEmail).isEqualTo("no-reply@lagrandelibrairie.com");
      assertThat(libraryMailBorrowingLimitSubject).isEqualTo("Borrowing limit exceeded");
      assertThat(libraryMailBorrowingLimitContent).isEqualTo("Media return date has passed for the media : %s \\n with the title : %s \\n identified by : %s \\n that you borrow the %s \\n thank you for reporting it as soon as possible.");
      assertThat(libraryMailBookingSubject).isEqualTo("Media is return");
      assertThat(libraryMailBookingContent).isEqualTo("The media %s is available\\n with the title : %s \\n identified by : %s \\n that you booked the %s \\n thank you for pickup it as soon as possible.");
   }

}