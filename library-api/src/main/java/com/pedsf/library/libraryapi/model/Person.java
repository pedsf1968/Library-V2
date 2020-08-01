package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;


/**
 * Entity to manage Person (writers, actors, directors...)
 *
 * id : identification of the Person
 * firstname : firsname of the Person
 * lastname : lastname of the Person
 * birthDate : birthDate of the Person
 * url : Wikipeadia URL
 */
@Data
@Entity
@Table(name = "person")
public class Person implements Serializable {

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @Size(min = Parameters.FIRSTNAME_MIN, max = Parameters.FIRSTNAME_MAX,
         message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.FIRSTNAME_MIN + " and " + Parameters.FIRSTNAME_MAX)
   @Column(name = "firstname", length = Parameters.FIRSTNAME_MAX)
   private String firstName;

   @NotNull
   @Size(min = Parameters.LASTNAME_MIN, max = Parameters.LASTNAME_MAX,
         message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.LASTNAME_MIN + " and " + Parameters.LASTNAME_MAX)
   @Column(name = "lastname", length = Parameters.LASTNAME_MAX)
   private String lastName;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "birth_date")
   private Date birthDate;

   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   @Column(name = "url", length = Parameters.URL_MAX)
   private String url;

   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   @Column(name = "photo_url", length = Parameters.URL_MAX)
   private String photoUrl;
}
