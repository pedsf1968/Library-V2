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

   public MediaCommon(String ean, @NotNull @NotBlank @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX) String title,
                      @NotNull Integer quantity,
                      @NotNull Integer stock) {
      this.ean = ean;
      this.title = title;
      this.quantity = quantity;
      this.stock = stock;
   }

   public MediaCommon() {
   }

   // Media information
   @Id
   @Column(name = "ean", length = Parameters.EAN_MAX)
   private String ean;

   @NotNull
   @NotBlank
   @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX)
   @Column(name = "title", length = Parameters.TITLE_MAX)
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
