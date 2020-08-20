package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import com.pedsf.library.dto.MediaStatus;
import com.pedsf.library.model.MediaType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

   @NotNull
   protected Integer id;

   @NotNull
   @Size(max = Parameters.MEDIA_TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_TYPE_MAX)
   protected String mediaType;

   @Size(max = Parameters.MEDIA_STATUS_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_STATUS_MAX)
   private String status;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;

   public MediaDTO(@NotNull Integer id,
                   @NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean,
                   @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                   @NotNull @Size(max = Parameters.MEDIA_TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_TYPE_MAX) String mediaType,
                   @Size(max = Parameters.MEDIA_STATUS_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.MEDIA_STATUS_MAX) String status,
                   @NotNull Integer quantity,
                   @NotNull Integer stock) {
      super(ean, title, quantity, stock);
      this.id = id;
      this.mediaType = mediaType;
      this.status = status;
   }

   public MediaDTO( BookDTO dto) {
      initialise(dto);
   }

   public MediaDTO( GameDTO dto) {
      initialise(dto);
   }

   public MediaDTO( MusicDTO dto) {
      initialise(dto);
   }

   public MediaDTO( VideoDTO dto) {
      initialise(dto);
   }

   public MediaDTO() {
      this.status = MediaStatus.FREE.name();
   }

   public void initialise( BookDTO dto) {
      this.ean = dto.getEan();
      this.mediaType = MediaType.BOOK.name();
      this.title = dto.getTitle();
      this.publicationDate = dto.getPublicationDate();
      this.weight = dto.getWeight();
      this.length = dto.getLength();
      this.width = dto.getWidth();
      this.height = dto.getHeight();
      this.quantity = dto.getQuantity();
      this.stock = dto.getStock();
   }

   public void initialise( GameDTO dto) {
      this.ean = dto.getEan();
      this.mediaType = MediaType.GAME.name();
      this.title = dto.getTitle();
      this.publicationDate = dto.getPublicationDate();
      this.weight = dto.getWeight();
      this.length = dto.getLength();
      this.width = dto.getWidth();
      this.height = dto.getHeight();
      this.quantity = dto.getQuantity();
      this.stock = dto.getStock();
   }

   public void initialise( MusicDTO dto) {
      this.ean = dto.getEan();
      this.mediaType = MediaType.MUSIC.name();
      this.title = dto.getTitle();
      this.publicationDate = dto.getPublicationDate();
      this.weight = dto.getWeight();
      this.length = dto.getLength();
      this.width = dto.getWidth();
      this.height = dto.getHeight();
      this.quantity = dto.getQuantity();
      this.stock = dto.getStock();
   }

   public void initialise( VideoDTO dto) {
      this.ean = dto.getEan();
      this.mediaType = MediaType.VIDEO.name();
      this.title = dto.getTitle();
      this.publicationDate = dto.getPublicationDate();
      this.weight = dto.getWeight();
      this.length = dto.getLength();
      this.width = dto.getWidth();
      this.height = dto.getHeight();
      this.quantity = dto.getQuantity();
      this.stock = dto.getStock();
   }

}
