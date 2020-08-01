package com.pedsf.library.libraryapi.model;

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
 * directorId : identification of the director of the Video
 * duration : duration of the Video
 * type : Video type
 * format : Video format
 * image : image format types
 * audio : audio format types
 * audience : type of spectators
 * url : URL link to teaser
 * actors : lists of the actors of the Video
 */
@Data
@Entity
@Table(name = "video")
public class Video extends MediaCommon implements Serializable {
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int AUDIO_FORMAT_MAX = 255;
   private static final int IMAGE_FORMAT_MAX = 255;
   private static final int PUBLIC_TYPE_MAX = 20;
   private static final int URL_MAX = 255; // default length
   private static final int SUMMARY_MAX = 2048;

   // Video information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;

   @NotNull
   @Column(name = "director_id")
   private Integer directorId;

   @Column(name = "duration")
   private Integer duration;

   @Column(name = "type", length = TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private VideoType type;

   @Column(name = "format", length = FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private VideoFormat format;

   @Column(name = "image", length = IMAGE_FORMAT_MAX)
   private String image;

   @Column(name = "audio", length = AUDIO_FORMAT_MAX)
   private String audio;

   @Column(name = "audience", length = PUBLIC_TYPE_MAX)
   private String audience;

   @Column(name = "url", length = URL_MAX)
   private String url;

   @Column(name = "summary", length = SUMMARY_MAX)
   private String summary;

   @OneToMany()
   @JoinTable(name="video_actors",
         joinColumns = {@JoinColumn(name="ean", referencedColumnName="ean")},
         inverseJoinColumns = {@JoinColumn(name="actor_id", referencedColumnName="id")})
   private Set<Person> actors;

}
