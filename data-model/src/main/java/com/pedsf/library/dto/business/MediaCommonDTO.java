package com.pedsf.library.dto.business;

import com.pedsf.library.Parameters;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Data Transfert Object to manage Media
 *
 * ean : EAN code identification of the Media
 * title : title of the Media
 * quantity : number of all Media
 * stock : Media in stock it decrease until (-1)*quantity*2 (counter of booking as well)
 * weight : weight of the Media
 * length : length of the Media
 * width : width of the Media
 * height : height of the Media
 */
@Data
public class MediaCommonDTO implements Serializable {

   // Media information
   @NotNull
   @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX)
   protected String ean;

   @NotNull
   @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX,
         message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX  + " !")
   protected String title;

   @NotNull
   protected Integer quantity;
   @NotNull
   protected Integer stock;

   protected Integer weight;
   protected Integer length;
   protected Integer width;
   protected Integer height;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   protected Date returnDate;

   public MediaCommonDTO(@NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean,
                         @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                         @NotNull Integer quantity,
                         @NotNull Integer stock) {
      this.ean = ean;
      this.title = title;
      this.quantity = quantity;
      this.stock = stock;
   }

   public MediaCommonDTO() {
      // nothing to do
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof MediaCommonDTO)) return false;
      MediaCommonDTO that = (MediaCommonDTO) o;
      return getEan().equals(that.getEan()) &&
              getTitle().equals(that.getTitle()) &&
              getQuantity().equals(that.getQuantity()) &&
              getStock().equals(that.getStock()) &&
              Objects.equals(getWeight(), that.getWeight()) &&
              Objects.equals(getLength(), that.getLength()) &&
              Objects.equals(getWidth(), that.getWidth()) &&
              Objects.equals(getHeight(), that.getHeight()) &&
              Objects.equals(getReturnDate(), that.getReturnDate());
   }

   @Override
   public int hashCode() {
      return Objects.hash(getEan(), getTitle(), getQuantity(), getStock(), getWeight(), getLength(), getWidth(), getHeight(), getReturnDate());
   }
}
