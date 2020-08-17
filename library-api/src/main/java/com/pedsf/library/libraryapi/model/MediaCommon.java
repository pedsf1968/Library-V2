package com.pedsf.library.libraryapi.model;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

/**
 * Entity to manage common part of all medias
 *
 * ean : EAN code identification of the media
 * title : title of the media
 * quantity : number of all media
 * stock : media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the media
 * length : length of the media
 * width : width of the media
 * height : height of the media
 * returnDate : the next return date
 */
@Data
@MappedSuperclass
public class MediaCommon implements Serializable {

   // Media information
   @Id
   @Column(name = "ean", length = Parameters.EAN_MAX)
   protected String ean;

   @NotNull
   @NotBlank
   @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX)
   @Column(name = "title", length = Parameters.TITLE_MAX)
   protected String title;

   @NotNull
   protected Integer quantity;
   @NotNull
   protected Integer stock;

   // weight and dimensions for transport informations
   @Column(name = "weight")
   protected Integer weight;

   @Column(name = "length")
   protected Integer length;

   @Column(name = "width")
   protected Integer width;

   @Column(name = "height")
   protected Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @Column(name = "return_date")
   protected Date returnDate;

   MediaCommon(String ean,
               @NotNull @NotBlank @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX) String title,
               @NotNull Integer quantity,
               @NotNull Integer stock) {
      this.ean = ean;
      this.title = title;
      this.quantity = quantity;
      this.stock = stock;
   }

   MediaCommon() {
      // nothing to do
   }

}
