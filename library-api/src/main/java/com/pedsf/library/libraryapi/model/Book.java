package com.pedsf.library.libraryapi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
public class Book implements Serializable {
   private static final int EAN_MAX = 20;
   private static final int ISBN_MAX = 20;
   private static final int TITLE_MIN = 1;
   private static final int TITLE_MAX = 50;
   private static final int TYPE_MAX = 20;
   private static final int FORMAT_MAX = 20;
   private static final int SUMMARY_MAX = 2048;

   // Media information
   @Id
   @Column(name = "ean", length = EAN_MAX)
   private String ean;

   @NotNull
   @NotBlank
   @Size(min = TITLE_MIN, max = TITLE_MAX)
   @Column(name = "title", length = TITLE_MAX)
   private String title;

   @NotNull
   private Integer quantity;
   @NotNull
   private Integer stock;

   // weight and dimensions for transport informations
   @Column(name = "weight")
   private Integer weight;

   @Column(name = "length")
   private Integer length;

   @Column(name = "width")
   private Integer width;

   @Column(name = "height")
   private Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "return_date")
   private Date returnDate;


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