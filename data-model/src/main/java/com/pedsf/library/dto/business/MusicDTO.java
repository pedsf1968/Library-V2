package com.pedsf.library.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Data Transfert Object to manage Music
 *
 * ean : EAN code identification of the Media
 * title : title of the Media
 * quantity : number of all Media
 * stock : Media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 * returnDate : the next return date
 *
 * publicationDate : is the date when the Media is published
 * authorId : identification of the author of the Music
 * composerId : identification of the composer of the Music
 * interpreterId : identification of the interpreter of the Music
 * duration : duration of the Music
 * type : Music type
 * format : Music format
 * url : URL link to teaser
 */
@Data
public class MusicDTO extends CommonDTO implements Serializable {
   private static final int PEGI_MAX = 4;

   // Music information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private PersonDTO author;
   private PersonDTO composer;
   private PersonDTO interpreter;
   private Integer duration;
   @Size(max = TYPE_MAX, message = ERROR_MESSAGE_LESS + TYPE_MAX)
   private String type;
   @Size(max = FORMAT_MAX, message = ERROR_MESSAGE_LESS + FORMAT_MAX)
   private String format;
   @Size(max = URL_MAX, message = ERROR_MESSAGE_LESS + URL_MAX)
   private String url;
}
