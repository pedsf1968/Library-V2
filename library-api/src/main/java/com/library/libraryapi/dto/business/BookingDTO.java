package com.library.libraryapi.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfert Object to manage Booking
 *
 * id : identification of the borrowing
 * mediaId : reference of the media
 * mediaId : EAN of the media
 * userId : identification of the User
 * bookingDate : booking date
 * pickUpDate : limit date to pickup a return book
 */
@Data
public class BookingDTO implements Serializable {
   private static final int EAN_MAX = 20;

   private static final String ERROR_MESSAGE_BETWEEN = "Length should be between : ";
   private static final String ERROR_MESSAGE_LESS = "Length should less than : ";


   private Integer id;

   @NotNull
   protected Integer mediaId;

   @NotNull
   @Size(max = EAN_MAX, message = ERROR_MESSAGE_LESS + EAN_MAX)
   protected String ean;


   @NotNull
   protected Integer userId;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date bookingDate;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date pickUpDate;
}
