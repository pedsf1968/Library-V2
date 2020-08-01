package com.pedsf.library.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Data Transfert Object to manage Media
 *
 * ean : EAN code of the media
 * title : title of the book, movie, music, song, game
 * quantity : total of this Media owned by the library
 * stock : remaining Media in the library to be borrowed. It become negative if they are bookings
 *
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 */
@Data
public class MediaCommonDTO implements Serializable {
   protected static final int TITLE_MIN = 1;
   protected static final int TITLE_MAX = 50;
   protected static final int EAN_MAX = 20;
   protected static final int TYPE_MAX = 20;
   protected static final int FORMAT_MAX = 20;
   protected static final int SUMMARY_MAX = 2048;
   protected static final int URL_MAX = 255; // default length
   protected static final String ERROR_MESSAGE_BETWEEN = "Length should be between : ";
   protected static final String ERROR_MESSAGE_LESS = "Length should less than : ";

   public MediaCommonDTO() {
   }

   // Media information
   @NotNull
   @Size(max = EAN_MAX, message = ERROR_MESSAGE_LESS + EAN_MAX)
   protected String ean;

   @NotNull
   @Size(min = TITLE_MIN, max = TITLE_MAX,
         message = ERROR_MESSAGE_BETWEEN + TITLE_MIN + " and " + TITLE_MAX  + " !")
   protected String title;

   @NotNull
   protected Integer quantity;
   @NotNull
   protected Integer stock;

   protected Integer weight;
   protected Integer length;
   protected Integer width;
   protected Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   protected Date returnDate;

}
