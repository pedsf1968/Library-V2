package com.pedsf.library.dto.business;

import com.pedsf.library.dto.global.UserDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
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
 * mediaId : ID of a media if booked and quantity available
 */
@Data
public class BookingDTO implements Serializable {
   private Integer id;

   @NotNull
   protected UserDTO user;

   @NotNull
   protected MediaDTO media;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date bookingDate;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date pickUpDate;

   private Integer mediaId;

   @NotNull
   private Integer rank;
}
