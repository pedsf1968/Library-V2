package com.pedsf.library.dto.business;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Data Transfert Object to manage Book
 *
 * ean : EAN code identification of the Media
 * title : title of the Media
 * quantity : number of all Media
 * stock : Media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 *
 * isbn : ISBN number of the Book
 * publicationDate : is the date when the Media is published
 * author : writer of the Book
 * editor : editor of the Book
 * type : Book type
 * format : Book format
 * pages : number of pages in the book
 * summary : Book summary
 */
@Data
public class BookDTO extends MediaCommonDTO implements Serializable {
   private static final int ISBN_MAX = 20;

   public BookDTO() {
   }

   // Book attributes
   @NotNull
   @Size(max = ISBN_MAX, message = ERROR_MESSAGE_LESS + ISBN_MAX)
   private String isbn;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   @NotNull
   private PersonDTO author;
   @NotNull
   private PersonDTO editor;
   @Size(max = TYPE_MAX, message = ERROR_MESSAGE_LESS + TYPE_MAX)
   private String type;
   @Size(max = FORMAT_MAX, message = ERROR_MESSAGE_LESS + FORMAT_MAX)
   private String format;
   private Integer pages;
   @Size(max = SUMMARY_MAX, message = ERROR_MESSAGE_LESS + SUMMARY_MAX)
   private String summary;
}
