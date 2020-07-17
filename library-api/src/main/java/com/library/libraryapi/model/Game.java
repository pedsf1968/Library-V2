package com.library.libraryapi.model;

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
public class Game implements Serializable {
   private static final int EAN_MAX = 20;
   private static final int TITLE_MIN = 1;
   private static final int TITLE_MAX = 50;
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int PEGI_MAX = 4;
   private static final int URL_MAX = 255; // default length
   private static final int SUMMARY_MAX = 2048;

   // Media information
   @Id
   @Column(name = "ean", length = EAN_MAX)
   private String ean;

   @NotNull
   @NotBlank
   @Size(min = TITLE_MIN, max = TITLE_MAX)
   @Column(name = "title", length = TITLE_MAX)
   private String title;

   @NotNull
   private Integer quantity;
   @NotNull
   private Integer stock;

   // weight and dimensions for transport informations
   @Column(name = "weight")
   private Integer weight;

   @Column(name = "length")
   private Integer length;

   @Column(name = "width")
   private Integer width;

   @Column(name = "height")
   private Integer height;

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
