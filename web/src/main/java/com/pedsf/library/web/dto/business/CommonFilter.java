package com.pedsf.library.web.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Common attributes for media filters
 *
 * id : identification of the Media
 * ean : EAN code
 * mediaType : type of the Media
 * title : title of the Media
 * publicationDate : is the date when the Media is published
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 * stock : total of this Media owned by the library
 * remaining : remaining Media in the library to be borrowed
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 */
@Data
public class CommonFilter {
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
}
