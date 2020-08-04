package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import com.pedsf.library.model.MediaType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfert Object to manage Media
 *
 * id : identification of the Media
 * ean : ean code like ISBN for BOOKS
 * mediaType : type of the Media (BOOK,MUSIC,VIDEO,GAME...)
 * returnDate : the date of the next expected return (null if all Media are available in stock)
 * status : the actual status of the media (FREE, BORROWED, BOOKED, BLOCKED)
 *
 * title : title of the book, movie, music, song, game
 * publicationDate : is the date when the Media is published
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 * quantity : total of this Media owned by the library
 * stock : remaining Media in the library to be borrowed. It become negative if they are bookings
 */
@Data
public class MediaDTO extends MediaCommonDTO implements Serializable {
   public MediaDTO(@NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean,
                   @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                   @NotNull Integer quantity,
                   @NotNull Integer stock,
                   @NotNull Integer id,
                   @NotNull @Size(max = Parameters.MEDIA_TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_TYPE_MAX) String mediaType,
                   @Size(max = Parameters.MEDIA_STATUS_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_STATUS_MAX) String status) {
      super(ean, title, quantity, stock);
      this.id = id;
      this.mediaType = mediaType;
      this.status = status;
   }

   public MediaDTO() {
   }

   @NotNull
   protected Integer id;

   @NotNull
   @Column(name = "ean", length = Parameters.EAN_MAX)
   private String ean;

   @NotNull
   @Size(max = Parameters.MEDIA_TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_TYPE_MAX)
   protected String mediaType;

   @Size(max = Parameters.MEDIA_STATUS_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_STATUS_MAX)
   private String status;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;

   public void initialise( BookDTO bookDTO) {
      this.ean = bookDTO.getEan();
      this.mediaType = MediaType.BOOK.name();
      this.returnDate = bookDTO.getReturnDate();
      this.title = bookDTO.getTitle();
      this.publicationDate = bookDTO.getPublicationDate();
      this.weight = bookDTO.getWeight();
      this.length = bookDTO.getLength();
      this.width = bookDTO.getWidth();
      this.height = bookDTO.getHeight();
      this.quantity = bookDTO.getQuantity();
      this.stock = bookDTO.getStock();
   }

   public void initialise( GameDTO gameDTO) {
      this.ean = gameDTO.getEan();
      this.mediaType = MediaType.GAME.name();
      this.returnDate = gameDTO.getReturnDate();
      this.title = gameDTO.getTitle();
      this.publicationDate = gameDTO.getPublicationDate();
      this.weight = gameDTO.getWeight();
      this.length = gameDTO.getLength();
      this.width = gameDTO.getWidth();
      this.height = gameDTO.getHeight();
      this.quantity = gameDTO.getQuantity();
      this.stock = gameDTO.getStock();
   }

   public void initialise( MusicDTO musicDTO) {
      this.ean = musicDTO.getEan();
      this.mediaType = MediaType.MUSIC.name();
      this.returnDate = musicDTO.getReturnDate();
      this.title = musicDTO.getTitle();
      this.publicationDate = musicDTO.getPublicationDate();
      this.weight = musicDTO.getWeight();
      this.length = musicDTO.getLength();
      this.width = musicDTO.getWidth();
      this.height = musicDTO.getHeight();
      this.quantity = musicDTO.getQuantity();
      this.stock = musicDTO.getStock();
   }

   public void initialise( VideoDTO videoDTO) {
      this.ean = videoDTO.getEan();
      this.mediaType = MediaType.VIDEO.name();
      this.returnDate = videoDTO.getReturnDate();
      this.title = videoDTO.getTitle();
      this.publicationDate = videoDTO.getPublicationDate();
      this.weight = videoDTO.getWeight();
      this.length = videoDTO.getLength();
      this.width = videoDTO.getWidth();
      this.height = videoDTO.getHeight();
      this.quantity = videoDTO.getQuantity();
      this.stock = videoDTO.getStock();
   }

}
