package com.pedsf.library.web.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Filter to search Game
 *
 * editor : editor of the Game
 * type : Game type
 * format : Game format
 * pegi : PEGI notation for games
 * summary : Game summary
 * url : link to the Game trailer
 */
@Data
public class GameFilter extends CommonFilter{

   // Game information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private Integer editorId;
   private String type;
   private String format;
   private String pegi;
   private String url;
   private String summary;
}
