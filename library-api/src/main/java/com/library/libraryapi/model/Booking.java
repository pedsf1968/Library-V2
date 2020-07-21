package com.library.libraryapi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;


/**
 * Entity to manage Booking
 *
 * id : identification of the borrowing
 * mediaId : reference of the media
 * userId : identification of the User
 * bookingDate : booking date
 * pickUpDate : limit date to pickup a return book
 * mediaId : ID of a media if booked and quantity available
 */
@Data
@Entity
@Table(name = "booking")
public class Booking {
   private static final int EAN_MAX = 20;

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @Column(name = "ean", length = EAN_MAX)
   private String ean;

   @NotNull
   @Column(name = "user_id")
   protected Integer userId;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "booking_date", columnDefinition = "DATE")
   private Date bookingDate;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "pickup_date", columnDefinition = "DATE")
   private Date pickUpDate;

   @Column(name = "media_id")
   private Integer mediaId;

   @NotNull
   @Column(name = "rank")
   private Integer rank;
}
