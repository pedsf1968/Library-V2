package com.pedsf.library.dto.global;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pedsf.library.Parameters;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class MessageDTO implements Serializable {

   @NotNull
   @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " AND " + Parameters.FIRSTNAME_MAX + " !")
   private String firstName;

   @NotNull
   @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " AND " + Parameters.LASTNAME_MAX + " !")
   private String lastName;

   @NotNull
   @Size(min = Parameters.EMAIL_MIN, max = Parameters.EMAIL_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.EMAIL_MIN + " AND " + Parameters.EMAIL_MAX + " !")
   @Pattern(regexp = Parameters.EMAIL_REGEXP, message = Parameters.ERROR_EMAIL_FORMAT)
   private String from;
   @NotNull
   @Size(min = Parameters.EMAIL_MIN, max = Parameters.EMAIL_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.EMAIL_MIN + " AND " + Parameters.EMAIL_MAX + " !")
   @Pattern(regexp = Parameters.EMAIL_REGEXP, message = Parameters.ERROR_EMAIL_FORMAT)
   private String to;
   @NotNull
   private String subject;
   @NotNull
   private String content;

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

}
