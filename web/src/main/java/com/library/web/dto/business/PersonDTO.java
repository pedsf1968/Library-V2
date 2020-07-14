package com.library.web.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Data Transfert Object to manage Person
 *
 * id : identification of the Person
 * firstname : firsname of the Person
 * lastname : lastname of the Person
 * birthDate : birthDate of the Person
 * url : Wikipeadia URL
 */
@Data
public class PersonDTO implements Serializable {
   private static final int FIRSTNAME_MIN = 2;
   private static final int FIRSTNAME_MAX = 50;
   private static final int LASTNAME_MIN = 2;
   private static final int LASTNAME_MAX = 50;
   private static final int URL_MAX = 255; // default length

   private static final String ERROR_MESSAGE_BETWEEN = "Length should be between : ";
   private static final String ERROR_MESSAGE_LESS = "Length should less than : ";

   @NotNull
   private Integer id;

   @NotNull
   @NotBlank
   @Size(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX,
         message = ERROR_MESSAGE_BETWEEN + FIRSTNAME_MIN + " and " + FIRSTNAME_MAX)
   private String firstName;

   @NotNull
   @NotBlank
   @Size(min = LASTNAME_MIN, max = LASTNAME_MAX,
         message = ERROR_MESSAGE_BETWEEN + LASTNAME_MIN + " and " + LASTNAME_MAX)
   private String lastName;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date birthDate;
   @Size(max = URL_MAX, message = ERROR_MESSAGE_LESS + URL_MAX)
   private String url;
   @Size(max = URL_MAX, message = ERROR_MESSAGE_LESS + URL_MAX)
   private String photoUrl;
}
