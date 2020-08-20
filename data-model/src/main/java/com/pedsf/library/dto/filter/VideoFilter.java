package com.pedsf.library.dto.filter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Filter to search Video
 *
 * director : director of the Video
 * duration : duration of the Video
 * type : Video type
 * format : Video format
 * url : link to Video trailer
 * actors : lists of the actors of the Video
 */
@Data
public class VideoFilter extends CommonFilter {

   // Video attributes
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private Integer directorId;
   private Integer duration;
   private String type;
   private String format;
   private String image;
   private String audio;
   private String audience;
   private String summary;
   private String url;
   private Integer actorId;
}
