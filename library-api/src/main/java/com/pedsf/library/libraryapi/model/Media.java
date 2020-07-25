package com.pedsf.library.libraryapi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Entity
@Table(name = "media")
public class Media implements Serializable {
   private static final int EAN_MAX = 20;
   private static final int MEDIA_TYPE_MAX = 10;
   private static final int STATUS_MAX = 10;

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @Column(name = "ean", length = EAN_MAX)
   private String ean;

   @NotNull
   @Column(name = "media_type", length = MEDIA_TYPE_MAX)
   @Enumerated(EnumType.STRING)
   protected MediaType mediaType;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "return_date")
   private Date returnDate;

   @Column(name = "status", length = STATUS_MAX)
   @Enumerated(EnumType.STRING)
   private MediaStatus status;
}
