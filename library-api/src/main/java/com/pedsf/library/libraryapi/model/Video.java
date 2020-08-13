package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import com.pedsf.library.dto.VideoFormat;
import com.pedsf.library.dto.VideoType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

/**
 * Entity to manage Video Media Type
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
@Entity
@Table(name = "video")
public class Video extends MediaCommon implements Serializable {

   // Video information
   @NotNull
   @Column(name = "director_id")
   private Integer directorId;

   @OneToMany()
   @JoinTable(name="video_actors",
         joinColumns = {@JoinColumn(name="ean", referencedColumnName="ean")},
         inverseJoinColumns = {@JoinColumn(name="actor_id", referencedColumnName="id")})
   private Set<Person> actors;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;

   @Column(name = "type", length = Parameters.TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private VideoType type;

   @Column(name = "format", length = Parameters.FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private VideoFormat format;

   @Column(name = "duration")
   private Integer duration;

   @Column(name = "image", length = Parameters.IMAGE_FORMAT_MAX)
   private String image;

   @Column(name = "audio", length = Parameters.AUDIO_FORMAT_MAX)
   private String audio;

   @Column(name = "audience", length = Parameters.PUBLIC_TYPE_MAX)
   private String audience;

   @Column(name = "url", length = Parameters.URL_MAX)
   private String url;

   @Column(name = "summary", length = Parameters.SUMMARY_MAX)
   private String summary;

   public Video(String ean,
                @NotNull @NotBlank @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX) String title,
                @NotNull Integer quantity,
                @NotNull Integer stock,
                @NotNull Integer directorId) {
      super(ean, title, quantity, stock);
      this.directorId = directorId;
   }

   public Video() {
   }
}
