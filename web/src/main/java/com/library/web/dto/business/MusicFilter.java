package com.library.web.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * Data Transfert Object to manage Music
 *
 * id : identification of the Music
 * ean : EAN code
 * mediaType : type of the Media (MUSIC)
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
 * author : identification of the author of the Music
 * composer : identification of the composer of the Music
 * interpreter : identification of the interpreter of the Music
 * duration : duration of the Music
 * type : Music type
 * format : Music format
 * url : link to the video clip
 */
@Data
public class MusicFilter {

   // Media attributes
   private String ean;
   private String title;
   @NotNull
   private Integer quantity;
   @NotNull
   private Integer stock;

   // weight and dimensions for transport informations
   private Integer weight;
   private Integer length;
   private Integer width;
   private Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date returnDate;

   // Music information
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private Integer authorId;
   private Integer composerId;
   private Integer interpreterId;
   private Integer duration;
   private String type;
   private String format;
   private String url;
}
