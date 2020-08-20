package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import com.pedsf.library.dto.GameFormat;
import com.pedsf.library.dto.GameType;
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
 * editorId : identification of the editor
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

   // Game information
   @NotNull
   @Column(name = "editor_id")
   private Integer editorId;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;

   @Column(name = "type", length = Parameters.TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private GameType type;

   @Column(name = "format", length = Parameters.FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private GameFormat format;

   @Column(name = "pegi", length = Parameters.PEGI_MAX)
   private String pegi;

   @Column(name = "url", length = Parameters.URL_MAX)
   private String url;

   @Column(name = "summary", length = Parameters.SUMMARY_MAX)
   private String summary;

   public Game(String ean, @NotNull @NotBlank @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX) String title,
               @NotNull Integer quantity,
               @NotNull Integer stock,
               @NotNull Integer editorId) {
      super(ean, title, quantity, stock);
      this.editorId = editorId;
   }

   public Game() {
      // nothing to do
   }
}
