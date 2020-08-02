package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Data Transfert Object to manage Video
 *
 * directorId : identification of the director of the Video
 * actors : lists of the actors of the Video
 * publicationDate : is the date when the Media is published
 * type : Video type
 * format : Video format
 * duration : duration of the Video
 * image : image format types
 * audio : audio format types
 * audience : type of spectators
 * url : URL link to teaser
 */
@Data
public class VideoDTO extends MediaCommonDTO implements Serializable {

   public VideoDTO(@NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean,
                   @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                   @NotNull Integer quantity,
                   @NotNull Integer stock,
                   @NotNull PersonDTO director) {
      super(ean, title, quantity, stock);
      this.director = director;
   }

   public VideoDTO() {
   }

   // Video information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   @NotNull
   private PersonDTO director;
   private Integer duration;
   @Size(max = Parameters.TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.TYPE_MAX)
   private String type;
   @Size(max = Parameters.FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.FORMAT_MAX)
   private String format;
   @Size(max = Parameters.IMAGE_FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.IMAGE_FORMAT_MAX)
   private String image;
   @Size(max = Parameters.AUDIO_FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.AUDIO_FORMAT_MAX)
   private String audio;
   @Size(max = Parameters.PUBLIC_TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.PUBLIC_TYPE_MAX)
   private String audience;
   @Size(max = Parameters.SUMMARY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.SUMMARY_MAX)
   private String summary;
   @Size(max = Parameters.URL_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.URL_MAX)
   private String url;
   private List<PersonDTO> actors;
}
