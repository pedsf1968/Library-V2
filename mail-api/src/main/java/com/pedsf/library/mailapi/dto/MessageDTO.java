package com.pedsf.library.mailapi.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class MessageDTO implements Serializable {
   static final int FIRSTNAME_MIN = 2;
   static final int FIRSTNAME_MAX = 50;
   static final int LASTNAME_MIN = 2;
   static final int LASTNAME_MAX = 50;
   static final int EMAIL_MIN = 4;
   static final int EMAIL_MAX = 255;
   static final String EMAIL_ERROR_MESSAGE = "Not a valid email address !";
   static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
   static final String ERROR_MESSAGE = "Length should be between : ";


   @NotNull
   @Size(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX, message = ERROR_MESSAGE + FIRSTNAME_MIN + " AND " + FIRSTNAME_MAX + " !")
   private String firstName;

   @NotNull
   @Size(min = LASTNAME_MIN, max = LASTNAME_MAX, message = ERROR_MESSAGE + LASTNAME_MIN + " AND " + LASTNAME_MAX + " !")
   private String lastName;

   @NotNull
   @Size(min = EMAIL_MIN, max = EMAIL_MAX, message = ERROR_MESSAGE + EMAIL_MIN + " AND " + EMAIL_MAX + " !")
   @Pattern(regexp = EMAIL_REGEXP, message = EMAIL_ERROR_MESSAGE)
   private String from;
   @NotNull
   @Size(min = EMAIL_MIN, max = EMAIL_MAX, message = ERROR_MESSAGE + EMAIL_MIN + " AND " + EMAIL_MAX + " !")
   @Pattern(regexp = EMAIL_REGEXP, message = EMAIL_ERROR_MESSAGE)
   private String to;
   @NotNull
   private String subject;
   @NotNull
   private String content;

   public MessageDTO() {
   }

   @JsonCreator
   public MessageDTO(
         @JsonProperty("firstName") String firstName,
         @JsonProperty("lastName") String lastName,
         @JsonProperty("from") String from,
         @JsonProperty("to") String to,
         @JsonProperty("subject") String subject,
         @JsonProperty("content") String content) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.from = from;
      this.to = to;
      this.subject = subject;
      this.content = content;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getFrom() {
      return from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public String getTo() {
      return to;
   }

   public void setTo(String to) {
      this.to = to;
   }

   public String getSubject() {
      return subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }


}
