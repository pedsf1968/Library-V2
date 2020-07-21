package com.library.web.dto.business;

import com.library.web.dto.MediaType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * Data Transfert Object to manage Video
 *
 * ean : EAN code
 * mediaType : type of the Media (VIDEO)
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
 * director : director of the Video
 * duration : duration of the Video
 * type : Video type
 * format : Video format
 * url : link to Video trailer
 * actors : lists of the actors of the Video
 */
@Data
public class VideoFilter {

   // Media attributes
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
