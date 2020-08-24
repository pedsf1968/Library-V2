package com.pedsf.library.batch.configuration;

import com.pedsf.library.batch.tasklet.DataProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

   @Test
   void init() {
      assertThat(noReplyEmail).isEqualTo("no-reply@lagrandelibrairie.com");
      assertThat(libraryMailBorrowingLimitSubject).isEqualTo("Borrowing limit exceeded");
      assertThat(libraryMailBorrowingLimitContent).isEqualTo("Media return date has passed for the media : %s \\n with the title : %s \\n identified by : %s \\n that you borrow the %s \\n thank you for reporting it as soon as possible.");
      assertThat(libraryMailBookingSubject).isEqualTo("Media is return");
      assertThat(libraryMailBookingContent).isEqualTo("The media %s is available\\n with the title : %s \\n identified by : %s \\n that you booked the %s \\n thank you for pickup it as soon as possible.");
   }

}