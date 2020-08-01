package com.pedsf.library.libraryapi.model;

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
 * ean : EAN code identification of the Media
 * title : title of the Media
 * quantity : number of all Media
 * stock : Media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
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
@Entity
@Table(name = "music")
public class Music extends MediaCommon implements Serializable {
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int URL_MAX = 255; // default length

   // Music information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;

   @Column(name = "author_id")
   private Integer authorId;

   @Column(name = "composer_id")
   private Integer composerId;

   @NotNull
   @Column(name = "interpreter_id")
   private Integer interpreterId;

   @Column(name = "duration")
   private Integer duration;

   @Column(name = "type", length = TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private MusicType type;

   @Column(name = "format", length = FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private MusicFormat format;

   @Column(name = "url", length = URL_MAX)
   private String url;
}
