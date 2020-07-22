package com.library.web.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Data Transfert Object to manage Game
 *
 * id : identification of the Game
 * ean : EAN code
 * mediaType : type of the Media (GAME)
 * title : title of the Media
 * publicationDate : is the date when the Media is published
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 * stock : total of this Media owned by the library
 * remaining : remaining Media in the library to be borrowed
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 *
 * editor : editor of the Game
 * type : Game type
 * format : Game format
 * pegi : PEGI notation for games
 * summary : Game summary
 * url : link to the Game trailer
 */
@Data
public class GameFilter {

   // Media information
   private String ean;
   private String title;
   private Integer quantity;
   private Integer stock;
   private Integer weight;
   private Integer length;
   private Integer width;
   private Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date returnDate;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private Integer editorId;
   private String type;
   private String format;
   private String pegi;
   private String url;
   private String summary;
}
