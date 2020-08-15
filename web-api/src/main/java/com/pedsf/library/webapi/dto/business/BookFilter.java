package com.pedsf.library.webapi.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Data Transfert Object to manage Book
 *
 * id : identification of the Book
 * ean : EAN code
 * mediaType : type of the Media (BOOK)
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
 * isbn : ISBN number of the Book
 * author : writer of the Book
 * editor : editor of the Book
 * type : Book type
 * format : Book format
 * summary : Book summary
 */
@Data
public class BookFilter {
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

   // Book attributes
   private String isbn;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   private Integer authorId;
   private Integer editorId;
   private String type;
   private String format;
   private Integer pages;
   private String summary;
}
