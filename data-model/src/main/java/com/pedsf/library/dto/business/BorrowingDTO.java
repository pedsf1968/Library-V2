package com.pedsf.library.dto.business;


import com.pedsf.library.dto.global.UserDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfert Object to manage Borrowing
 *
 * id : identification of the Delay
 * user : User
 * media : Media borrowed
 * borrowingDate : borrowing date
 * returnDate : return media date
 *  extended : incremental counter that count borrowing extentions
 */
@Data
public class BorrowingDTO implements Serializable {

   protected Integer id;
   protected UserDTO user;
   protected MediaDTO media;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date borrowingDate;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date returnDate;
   private Integer extended;

   public BorrowingDTO(Integer id, @NotNull UserDTO user, @NotNull MediaDTO media, Date borrowingDate, Date returnDate) {
      this.id = id;
      this.user = user;
      this.media = media;
      this.borrowingDate = borrowingDate;
      this.returnDate = returnDate;
      this.extended = 0;
   }

   public BorrowingDTO() {
      // nothing to do
   }
}
