package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

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

   @NotNull
   private Integer id;

   @NotNull
   @NotBlank
   @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX,
         message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " and " + Parameters.FIRSTNAME_MAX)
   private String firstName;

   @NotNull
   @NotBlank
   @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX,
         message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " and " + Parameters.LASTNAME_MAX)
   private String lastName;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date birthDate;
   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   private String url;
   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   private String photoUrl;


   public PersonDTO(@NotNull Integer id, @NotNull @NotBlank @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX,
           message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " and " + Parameters.FIRSTNAME_MAX) String firstName, @NotNull @NotBlank @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX,
           message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " and " + Parameters.LASTNAME_MAX) String lastName, Date birthDate) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.birthDate = birthDate;
   }

   public PersonDTO() {
      // nothing to do
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof PersonDTO)) return false;
      PersonDTO personDTO = (PersonDTO) o;
      return getId().equals(personDTO.getId()) &&
            getFirstName().equals(personDTO.getFirstName()) &&
            getLastName().equals(personDTO.getLastName()) &&
          //  Objects.equals(getBirthDate(),personDTO.getBirthDate()) &&
            Objects.equals(getUrl(), personDTO.getUrl()) &&
            Objects.equals(getPhotoUrl(), personDTO.getPhotoUrl());
   }

   @Override
   public int hashCode() {
      return Objects.hash(getId(), getFirstName(), getLastName(), getBirthDate());
   }
}
