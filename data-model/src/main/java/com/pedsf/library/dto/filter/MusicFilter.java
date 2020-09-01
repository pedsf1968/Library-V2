package com.pedsf.library.dto.filter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Filter to search Music
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
public class MusicFilter extends CommonFilter {

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
