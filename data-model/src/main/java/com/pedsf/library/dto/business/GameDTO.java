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

   // Game information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private PersonDTO editor;
   @Size(max = Parameters.TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.TYPE_MAX)
   private String type;
   @Size(max = Parameters.FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.FORMAT_MAX)
   private String format;
   @Size(max = Parameters.PEGI_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.PEGI_MAX)
   private String pegi;
   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   private String url;
   @Size(max = Parameters.SUMMARY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.SUMMARY_MAX)
   private String summary;

   public GameDTO(@NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean,
                  @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                  @NotNull Integer quantity,
                  @NotNull Integer stock,
                  PersonDTO editor) {
      super(ean, title, quantity, stock);
      this.editor = editor;
   }

   public GameDTO() {
      // nothing to do
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof GameDTO)) return false;
      if (!super.equals(o)) return false;
      GameDTO gameDTO = (GameDTO) o;
      return Objects.equals(getPublicationDate(), gameDTO.getPublicationDate()) &&
            getEditor().equals(gameDTO.getEditor()) &&
            Objects.equals(getType(), gameDTO.getType()) &&
            Objects.equals(getFormat(), gameDTO.getFormat()) &&
            Objects.equals(getPegi(), gameDTO.getPegi()) &&
            Objects.equals(getUrl(), gameDTO.getUrl()) &&
            Objects.equals(getSummary(), gameDTO.getSummary());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), getPublicationDate(), getEditor(), getType(), getFormat(), getPegi(), getUrl(), getSummary());
   }
}
