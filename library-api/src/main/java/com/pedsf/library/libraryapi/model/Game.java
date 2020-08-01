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
 * Entity to manage Game Media Type
 *
 * ean : EAN code identification of the Book
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
 * type : Game type
 * format : Game format
 * pegi : PEGI notation
 * url : URL link to teaser
 * summary : Game summary
 */
@Data
@Entity
@Table(name = "game")
public class Game extends MediaCommon implements Serializable {
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int PEGI_MAX = 4;
   private static final int URL_MAX = 255; // default length
   private static final int SUMMARY_MAX = 2048;

   // Game information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;

   @NotNull
   @Column(name = "editor_id")
   private Integer editorId;

   @Column(name = "type", length = TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private GameType type;

   @Column(name = "format", length = FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private GameFormat format;

   @Column(name = "pegi", length = PEGI_MAX)
   private String pegi;

   @Column(name = "url", length = URL_MAX)
   private String url;

   @Column(name = "summary", length = SUMMARY_MAX)
   private String summary;
}
