package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
