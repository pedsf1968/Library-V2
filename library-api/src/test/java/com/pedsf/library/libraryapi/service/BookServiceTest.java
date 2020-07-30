package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookServiceTest {
   private static final String BOOK_EAN_TEST = "978-2253002864";

   @TestConfiguration
   static class bookServiceTestConfiguration {
      @Autowired
      private BookRepository bookRepository;
      @Autowired
      private PersonRepository personRepository;


      @Bean
      public BookService bookService() {
         PersonService personService = new PersonService(personRepository);

         return new BookService(bookRepository,personService);
      }
   }

   @Autowired
   private BookService bookService;

   @Test
   @DisplayName("Verify that return TRUE if the Book exist")
   void existsById_returnTrue_OfAnExistingBookId() {
      List<BookDTO> bookDTOS = bookService.findAll();

      for(BookDTO bookDTO : bookDTOS) {
         String ean = bookDTO.getEan();
         assertThat(bookService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Book doesn't exist")
   void existsById_returnFalse_OfAnInexistingBookId() {
      assertThat(bookService.existsById("5lkjh5")).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Book by is ID")
   void findById_returnUser_ofExistingBookId() {
      List<BookDTO> bookDTOS = bookService.findAll();
      BookDTO found;

      for(BookDTO bookDTO : bookDTOS) {
         String ean = bookDTO.getEan();
         found = bookService.findById(ean);

         assertThat(found).isEqualTo(bookDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Book with wrong ID")
   void findById_returnException_ofInexistingBookId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         BookDTO found = bookService.findById("klgqsdf");
      });
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Users")
   void findAll_returnAllBooks() {
      List<BookDTO> bookDTOS = bookService.findAll();
      assertThat(bookDTOS.size()).isEqualTo(7);
   }

   @Test
   void findAllAllowed() {
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Book by his title and author")
   void findAllFiltered_returnOnlyOneBook_ofExistingFirstTitleAndAuthor() {
      List<BookDTO> bookDTOS = bookService.findAll();
      List<BookDTO> found;
      for(BookDTO b:bookDTOS) {
         BookDTO filter = new BookDTO();
         filter.setTitle(b.getTitle());
         filter.setAuthor(b.getAuthor());

         found = bookService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(b);
      }
   }

   @Test
   void getFirstId() {
   }

   @Test
   @DisplayName("Verify that we can create a new Book")
   void save_returnCreatedBook_ofNewBook() {
      BookDTO bookDTO = bookService.findById(BOOK_EAN_TEST);
      String newEan = "newEAN";
      String newTitle = "NewTitle";

      bookDTO.setEan(newEan);
      bookDTO.setTitle(newTitle);
      bookDTO.setReturnDate(null);
      bookDTO.setPublicationDate(null);
      bookDTO = bookService.save(bookDTO);
      String ean = bookDTO.getEan();

      assertThat(bookService.existsById(ean)).isTrue();
      bookService.deleteById(ean);
   }

   @Test
   void update() {
   }

   @Test
   void deleteById() {
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Books")
   void count_returnTheNumberOfBooks() {
      assertThat(bookService.count()).isEqualTo(7);
   }

   @Test
   void entityToDTO() {
   }

   @Test
   void dtoToEntity() {
   }

   @Test
   @Tag("findAllAuthors")
   @DisplayName("Verify that we get all Books authors")
   void findAllAuthors() {
      List<PersonDTO> personDTOS = bookService.findAllAuthors();
      assertThat(personDTOS.size()).isEqualTo(3);
   }

   @Test
   @Tag("findAllEditors")
   @DisplayName("Verify that we get all Books editors")
   void findAllEditors() {
      List<PersonDTO> personDTOS = bookService.findAllEditors();
      assertThat(personDTOS.size()).isEqualTo(3);
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Books titles")
   void findAllTitles() {
      List<String> titles = bookService.findAllTitles();
      assertThat(titles.size()).isEqualTo(7);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Book by his EAN number")
   void increaseStock_returnBookWithIncrementedStock_ofOneBook() {
      BookDTO bookDTO = bookService.findById(BOOK_EAN_TEST);
      Integer oldStock = bookDTO.getStock();
      bookService.increaseStock(BOOK_EAN_TEST);
      bookDTO = bookService.findById(BOOK_EAN_TEST);
      assertThat(bookDTO.getStock()).isEqualTo(oldStock+1);
      bookDTO.setStock(oldStock);
      bookService.update(bookDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Book by his EAN number")
   void decreaseStock_returnBookWithDecrementedStock_ofOneBook() {
      BookDTO bookDTO = bookService.findById(BOOK_EAN_TEST);
      Integer oldStock = bookDTO.getStock();
      bookService.decreaseStock(BOOK_EAN_TEST);
      bookDTO = bookService.findById(BOOK_EAN_TEST);
      assertThat(bookDTO.getStock()).isEqualTo(oldStock-1);
      bookDTO.setStock(oldStock);
      bookService.update(bookDTO);
   }
}