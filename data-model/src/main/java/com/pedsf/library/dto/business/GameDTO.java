package com.pedsf.library.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
public class GameDTO extends MediaCommonDTO implements Serializable {
   private static final int PEGI_MAX = 4;

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
