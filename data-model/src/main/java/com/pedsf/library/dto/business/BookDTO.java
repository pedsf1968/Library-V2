package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Data Transfert Object to manage Book
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

   // Book attributes
   @NotNull
   @Size(max = Parameters.ISBN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.ISBN_MAX)
   private String isbn;
   @NotNull
   private PersonDTO author;
   @NotNull
   private PersonDTO editor;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   @Size(max = Parameters.TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.TYPE_MAX)
   private String type;
   @Size(max = Parameters.FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.FORMAT_MAX)
   private String format;
   private Integer pages;
   @Size(max = Parameters.SUMMARY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.SUMMARY_MAX)
   private String summary;
}
