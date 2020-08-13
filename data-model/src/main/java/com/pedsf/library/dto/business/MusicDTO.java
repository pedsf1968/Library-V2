package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

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
public class MusicDTO extends MediaCommonDTO implements Serializable {

   // Music information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private PersonDTO author;
   private PersonDTO composer;
   private PersonDTO interpreter;
   private Integer duration;
   @Size(max = Parameters.TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.TYPE_MAX)
   private String type;
   @Size(max = Parameters.FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.FORMAT_MAX)
   private String format;
   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   private String url;

   public MusicDTO(@NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean, @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                   @NotNull Integer quantity,
                   @NotNull Integer stock,
                   PersonDTO author,
                   PersonDTO composer,
                   PersonDTO interpreter) {
      super(ean, title, quantity, stock);
      this.author = author;
      this.composer = composer;
      this.interpreter = interpreter;
   }

   public MusicDTO() {
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof MusicDTO)) return false;
      if (!super.equals(o)) return false;
      MusicDTO musicDTO = (MusicDTO) o;
      return Objects.equals(getPublicationDate(), musicDTO.getPublicationDate()) &&
            getAuthor().equals(musicDTO.getAuthor()) &&
            getComposer().equals(musicDTO.getComposer()) &&
            getInterpreter().equals(musicDTO.getInterpreter()) &&
            Objects.equals(getDuration(), musicDTO.getDuration()) &&
            Objects.equals(getType(), musicDTO.getType()) &&
            Objects.equals(getFormat(), musicDTO.getFormat()) &&
            Objects.equals(getUrl(), musicDTO.getUrl());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), getPublicationDate(), getAuthor(), getComposer(), getInterpreter(), getDuration(), getType(), getFormat(), getUrl());
   }
}
