package com.library.web.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Data Transfert Object to manage Game
 *
 * ean : EAN code identification of the Book
 * title : title of the Media
 * quantity : number of all Media
 * stock : Media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 *
 * publicationDate : is the date when the Media is published
 * type : Game type
 * format : Game format
 * pegi : PEGI notation
 * url : URL link to teaser
 * summary : Game summary
 */
@Data
public class GameDTO implements Serializable {
   private static final int TITLE_MIN = 1;
   private static final int TITLE_MAX = 50;
   private static final int EAN_MAX = 20;
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int PEGI_MAX = 4;
   private static final int URL_MAX = 255; // default length
   private static final int SUMMARY_MAX = 2048;

   private static final String ERROR_MESSAGE_BETWEEN = "Length should be between : ";
   private static final String ERROR_MESSAGE_LESS = "Length should less than : ";

   // Media information
   @NotNull
   @Size(max = EAN_MAX, message = ERROR_MESSAGE_LESS + EAN_MAX)
   private String ean;

   @NotNull
   @Size(min = TITLE_MIN, max = TITLE_MAX,
         message = ERROR_MESSAGE_BETWEEN + TITLE_MIN + " and " + TITLE_MAX  + " !")
   private String title;

   @NotNull
   private Integer quantity;
   @NotNull
   private Integer stock;

   private Integer weight;
   private Integer length;
   private Integer width;
   private Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date returnDate;

   // Game information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private PersonDTO editor;
   @Size(max = TYPE_MAX, message = ERROR_MESSAGE_LESS + TYPE_MAX)
   private String type;
   @Size(max = FORMAT_MAX, message = ERROR_MESSAGE_LESS + FORMAT_MAX)
   private String format;
   @Size(max = PEGI_MAX, message = ERROR_MESSAGE_LESS + PEGI_MAX)
   private String pegi;
   @Size(max = URL_MAX, message = ERROR_MESSAGE_LESS + URL_MAX)
   private String url;
   @Size(max = SUMMARY_MAX, message = ERROR_MESSAGE_LESS + SUMMARY_MAX)
   private String summary;
}
