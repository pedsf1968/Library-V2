package com.library.libraryapi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


/**
 * Entity to manage Borrowing
 *
 * id : identification of the Delay
 * userId : identification of the User
 * mediaId : identification of the Media borrowed
 * borrowingDate : borrowing date
 * returnDate : return media date
 *  extended : incremental counter that count borrowing extentions
 */
@Data
@Entity
@Table(name = "borrowing")
public class Borrowing implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   protected Integer id;

   @Column(name = "user_id")
   protected Integer userId;

   @Column(name = "media_id")
   protected Integer mediaId;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "borrowing_date", columnDefinition = "DATE")
   private Date borrowingDate;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "return_date")
   private Date returnDate;

   @Column(name = "extended")
   private Integer extended;
}
