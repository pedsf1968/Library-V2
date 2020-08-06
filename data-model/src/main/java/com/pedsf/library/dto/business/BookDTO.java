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
 * Data Transfert Object to manage Book
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
public class BookDTO extends MediaCommonDTO implements Serializable {

   public BookDTO(@NotNull @Size(max = Parameters.EAN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.EAN_MAX) String ean,
                  @NotNull @Size(min = Parameters.TITLE_MIN, max = Parameters.TITLE_MAX, message = Parameters.ERROR_FORMAT_BETWEEN + Parameters.TITLE_MIN + " and " + Parameters.TITLE_MAX + " !") String title,
                  @NotNull Integer quantity,
                  @NotNull Integer stock,
                  @NotNull @Size(max = Parameters.ISBN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.ISBN_MAX) String isbn,
                  @NotNull PersonDTO author,
                  @NotNull PersonDTO editor) {
      super(ean, title, quantity, stock);
      this.isbn = isbn;
      this.author = author;
      this.editor = editor;
   }

   public BookDTO() {
   }

   // Book attributes
   @NotNull
   @Size(max = Parameters.ISBN_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.ISBN_MAX)
   private String isbn;
   @NotNull
   private PersonDTO author;
   @NotNull
   private PersonDTO editor;
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private Date publicationDate;
   @Size(max = Parameters.TYPE_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.TYPE_MAX)
   private String type;
   @Size(max = Parameters.FORMAT_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.FORMAT_MAX)
   private String format;
   private Integer pages;
   @Size(max = Parameters.SUMMARY_MAX, message = Parameters.ERROR_FORMAT_LESS + Parameters.SUMMARY_MAX)
   private String summary;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof BookDTO)) return false;
      if (!super.equals(o)) return false;
      BookDTO bookDTO = (BookDTO) o;
      return getIsbn().equals(bookDTO.getIsbn()) &&
              getAuthor().equals(bookDTO.getAuthor()) &&
              getEditor().equals(bookDTO.getEditor()) &&
              getType().equals(bookDTO.getType()) &&
              getFormat().equals(bookDTO.getFormat());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), getIsbn(), getAuthor(), getEditor(), getPublicationDate(), getType(), getFormat(), getPages(), getSummary());
   }
}
