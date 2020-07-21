package com.library.batch.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfert Object to manage Media
 *
 * id : identification of the Media
 * ean : ean code like ISBN for BOOKS
 * mediaType : type of the Media (BOOK,MUSIC,VIDEO,GAME...)
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 * status : the actual status of the media (FREE, BORROWED, BOOKED, BLOCKED)
 *
 * title : title of the book, movie, music, song, game
 * publicationDate : is the date when the Media is published
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 * quantity : total of this Media owned by the library
 * stock : remaining Media in the library to be borrowed. It become negative if they are bookings
 */
@Data
public class MediaDTO implements Serializable {
   private static final int MEDIA_STATUS_MAX = 10;
   private static final int MEDIA_TYPE_MAX = 10;
   private static final int TITLE_MIN = 1;
   private static final int TITLE_MAX = 50;
   private static final int EAN_MAX = 20;

   private static final String ERROR_MESSAGE_BETWEEN = "Length should be between : ";
   private static final String ERROR_MESSAGE_LESS = "Length should less than : ";

   @NotNull
   protected Integer id;

   @NotNull
   @Size(max = EAN_MAX, message = ERROR_MESSAGE_LESS + EAN_MAX)
   protected String ean;

   @NotNull
   @Size(max = MEDIA_TYPE_MAX, message = ERROR_MESSAGE_LESS + MEDIA_TYPE_MAX)
   protected String mediaType;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private java.sql.Date returnDate;

   @Size(max = MEDIA_STATUS_MAX, message = ERROR_MESSAGE_LESS + MEDIA_STATUS_MAX)
   private String status;

   @NotNull
   @Size(min = TITLE_MIN, max = TITLE_MAX,
         message = ERROR_MESSAGE_BETWEEN + TITLE_MIN + " and " + TITLE_MAX  + " !")
   private String title;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;

   // weight and dimensions for transport informations
   private Integer weight;
   private Integer length;
   private Integer width;
   private Integer height;

   private Integer quantity;
   private Integer stock;
}
