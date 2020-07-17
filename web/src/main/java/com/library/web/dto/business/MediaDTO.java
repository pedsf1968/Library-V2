package com.library.web.dto.business;

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
 * ean : EAN code
 * mediaType : type of the Media (BOOK,MUSIC,VIDEO,GAME...)
 * title : title of the Media
 * publicationDate : is the date when the Media is published
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 * stock : total of this Media owned by the library
 * remaining : remaining Media in the library to be borrowed
 */
@Data
public class MediaDTO implements Serializable {
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

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date returnDate;
   private Integer stock;
   private Integer remaining;
}
