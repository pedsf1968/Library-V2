package com.pedsf.library.libraryapi.model;

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
   static final int FIRSTNAME_MIN = 2;
   static final int FIRSTNAME_MAX = 50;
   static final int LASTNAME_MIN = 2;
   static final int LASTNAME_MAX = 50;
   static final int URL_MAX = 255;
   static final String ERROR_FORMAT_MESSAGE_BETWEEN = "Length should be between : ";
   static final String ERROR_FORMAT_MESSAGE_MAX = "Length should less than : ";

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @Size(min = FIRSTNAME_MIN, max = FIRSTNAME_MAX,
         message = ERROR_FORMAT_MESSAGE_BETWEEN + FIRSTNAME_MIN + " and " + FIRSTNAME_MAX)
   @Column(name = "firstname", length = FIRSTNAME_MAX)
   private String firstName;

   @NotNull
   @Size(min = LASTNAME_MIN, max = LASTNAME_MAX,
         message = ERROR_FORMAT_MESSAGE_BETWEEN + LASTNAME_MIN + " and " + LASTNAME_MAX)
   @Column(name = "lastname", length = LASTNAME_MAX)
   private String lastName;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "birth_date")
   private Date birthDate;

   @Size(max = URL_MAX, message = ERROR_FORMAT_MESSAGE_MAX + URL_MAX)
   @Column(name = "url", length = URL_MAX)
   private String url;

   @Size(max = URL_MAX, message = ERROR_FORMAT_MESSAGE_MAX + URL_MAX)
   @Column(name = "photo_url", length = URL_MAX)
   private String photoUrl;
}
