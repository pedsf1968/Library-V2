package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

/**
 * Entity to manage Book Media Type
 *
 * isbn : ISBN number of the Book
 * author : writer of the Book
 * editor : editor of the Book
 * publicationDate : is the date when the Media is published
 * type : Book type
 * format : Book format
 * pages : number of pages in the book
 * summary : Book summary
 */
@Data
@Entity
@Table(name = "book")
public class Book extends MediaCommon implements Serializable {

   // Book information
   @NotNull
   @Column(name = "isbn", length = Parameters.ISBN_MAX)
   private String isbn;

   @NotNull
   @Column(name = "author_id")
   private Integer authorId;

   @NotNull
   @Column(name = "editor_id")
   private Integer editorId;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "publication_date")
   private Date publicationDate;

   @Column(name = "type", length = Parameters.TYPE_MAX)
   @Enumerated(EnumType.STRING)
   private BookType type;

   @Column(name = "format", length = Parameters.FORMAT_MAX)
   @Enumerated(EnumType.STRING)
   private BookFormat format;

   @Column(name = "pages")
   private Integer pages;

   @Column(name = "summary", length = Parameters.SUMMARY_MAX)
   private String summary;
}
