package com.pedsf.library.dto.filter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Filter to search Book
 *
 * isbn : ISBN number of the Book
 * author : writer of the Book
 * editor : editor of the Book
 * type : Book type
 * format : Book format
 * summary : Book summary
 */
@Data
public class BookFilter extends CommonFilter {

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
