package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import com.pedsf.library.dto.MusicFormat;
import com.pedsf.library.dto.MusicType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Entity to manage Music Media Type
 *
 * authorId : identification of the author of the Music
 * composerId : identification of the composer of the Music
 * interpreterId : identification of the interpreter of the Music
 * publicationDate : is the date when the Media is published
 * type : Music type
 * format : Music format
 * duration : duration of the Music
 * url : URL link to teaser
 */
@Data
@Entity
@Table(name = "music")
public class Music extends MediaCommon implements Serializable {

   public Music(String ean,
                @NotNull @NotBlank @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX) String title,
                @NotNull Integer quantity,
                @NotNull Integer stock,
                Integer authorId,
                Integer composerId,
                @NotNull Integer interpreterId) {
      super(ean, title, quantity, stock);
      this.authorId = authorId;
      this.composerId = composerId;
      this.interpreterId = interpreterId;
   }

   public Music() {
   }

   // Music information
   @Column(name = "author_id")
   private Integer authorId;

   @Column(name = "composer_id")
   private Integer composerId;

   @NotNull
   @Column(name = "interpreter_id")
   private Integer interpreterId;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;


   @Column(name = "type", length = Parameters.TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private MusicType type;

   @Column(name = "format", length = Parameters.FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private MusicFormat format;

   @Column(name = "duration")
   private Integer duration;

   @Column(name = "url", length = Parameters.URL_MAX)
   private String url;
}
