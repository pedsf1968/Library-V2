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
 * Entity to manage common part of all Media
 *
 * id : identification of the Media
 * ean : ean code like ISBN for BOOKS
 * mediaType : type of the Media (BOOK,MUSIC,VIDEO,GAME...)
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 * status : the actual status of the media (FREE, BORROWED, BOOKED, BLOCKED)
 *
 * title : title of the book, movie, music, song, game
 * publicationDate : is the date when the Media is published
 */
@Data
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class MediaCommon implements Serializable {
   private static final int EAN_MAX = 20;
   private static final int ISBN_MAX = 20;
   private static final int TITLE_MIN = 1;
   private static final int TITLE_MAX = 50;

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

}
