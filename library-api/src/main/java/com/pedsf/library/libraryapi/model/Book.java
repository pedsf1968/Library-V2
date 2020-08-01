package com.pedsf.library.libraryapi.model;

import com.pedsf.library.dto.business.PersonDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Entity to manage Book Media Type
 *
 * ean : EAN code identification of the Book
 * title : title of the Media
 * quantity : number of all Media
 * stock : Media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 * returnDate : the next return date
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
@Entity
@Table(name = "book")
public class Book extends MediaCommon implements Serializable {
   private static final int ISBN_MAX = 20;
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int SUMMARY_MAX = 2048;

   // Book information
   @NotNull
   @Column(name = "isbn", length = ISBN_MAX)
   private String isbn;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;


   @NotNull
   @Column(name = "author_id")
   private Integer authorId;

   @NotNull
   @Column(name = "editor_id")
   private Integer editorId;

   @Column(name = "type", length = TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private BookType type;

   @Column(name = "format", length = FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private BookFormat format;

   @Column(name = "pages")
   private Integer pages;

   @Column(name = "summary", length = SUMMARY_MAX)
   private String summary;
}
