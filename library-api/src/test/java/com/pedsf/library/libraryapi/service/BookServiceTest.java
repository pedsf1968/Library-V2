package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.model.Book;
import com.pedsf.library.libraryapi.model.BookFormat;
import com.pedsf.library.libraryapi.model.BookType;
import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookServiceTest {
   private static final String BOOK_EAN_TEST = "978-2253002864";
   private static final String BOOK_TITLE_TEST = "Le Horla";

   private static Book newBook = new Book();

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

   @BeforeAll
   static void beforeAll() {
      newBook.setTitle("The green tomato");
      newBook.setAuthorId(1);
      newBook.setEditorId(1);
      newBook.setEan("954-8789797");
      newBook.setIsbn("9548789797");
      newBook.setPages(125);
      newBook.setFormat(BookFormat.COMICS);
      newBook.setType(BookType.HUMOR);
      newBook.setHeight(11);
      newBook.setLength(11);
      newBook.setWidth(11);
      newBook.setWeight(220);
      newBook.setStock(1);
      newBook.setQuantity(1);
   }

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
   @DisplayName("Verify that we have the list of all Books")
   void findAll_returnAllBooks() {
      BookDTO newBookDTO = bookService.entityToDTO(newBook);
      List<BookDTO> bookDTOS = bookService.findAll();
      assertThat(bookDTOS.size()).isEqualTo(7);

      // add one book to increasee the list
      newBookDTO = bookService.save(newBookDTO);
      bookDTOS = bookService.findAll();
      assertThat(bookDTOS.size()).isEqualTo(8);

      bookService.deleteById(newBookDTO.getEan());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Books that can be booked")
   void findAllAllowed_returnBookableBooks_ofAllBooks() {
      newBook.setStock(-2);
      BookDTO newBookDTO = bookService.save(bookService.entityToDTO(newBook));
      List<BookDTO> bookDTOS = bookService.findAll();
      List<BookDTO> alloweds = bookService.findAllAllowed();

      for(BookDTO bookDTO: bookDTOS) {
         if (alloweds.contains(bookDTO)) {
            // allowed
            assertThat(bookDTO.getStock()).isGreaterThan(-bookDTO.getQuantity()*2);
         } else {
            // not allowed

            assertThat(bookDTO.getStock()).isLessThanOrEqualTo(-bookDTO.getQuantity()*2);
         }
      }

      newBook.setStock(1);
      bookService.deleteById(newBookDTO.getEan());
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
   @Tag("save")
   @DisplayName("Verify that we can create a new Book")
   void save_returnCreatedBook_ofNewBook() {
      BookDTO bookDTO = bookService.entityToDTO(newBook);

      bookDTO = bookService.save(bookDTO);
      String ean = bookDTO.getEan();

      assertThat(bookService.existsById(ean)).isTrue();
      bookService.deleteById(ean);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Book")
   void update_returnUpdatedBook_ofBookAndNewTitle() {
      BookDTO bookDTO = bookService.findById(BOOK_EAN_TEST);
      String oldTitle = bookDTO.getTitle();
      bookDTO.setTitle(BOOK_TITLE_TEST);

      BookDTO bookSaved = bookService.update(bookDTO);
      assertThat(bookSaved).isEqualTo(bookDTO);
      BookDTO bookFound = bookService.findById(bookDTO.getEan());
      assertThat(bookFound).isEqualTo(bookDTO);

      bookDTO.setTitle(oldTitle);
      bookService.update(bookDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Book by his EAN")
   void deleteById_returnExceptionWhenGetUserById_ofDeletedUserById() {
      BookDTO bookDTO = bookService.entityToDTO(newBook);

      bookDTO = bookService.save(bookDTO);
      String ean = bookDTO.getEan();

      assertThat(bookService.existsById(ean)).isTrue();
      bookService.deleteById(ean);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         bookService.findById(ean);
      });
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Books")
   void count_returnTheNumberOfBooks() {
      BookDTO bookDTO = bookService.entityToDTO(newBook);
      assertThat(bookService.count()).isEqualTo(7);

      // add an other book
      bookDTO = bookService.save(bookDTO);
      assertThat(bookService.count()).isEqualTo(8);
      bookService.deleteById(bookDTO.getEan());
   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Book DTO is converted in right Book Entity")
   void dtoToEntity_returnRightBookEntity_ofBookDTO() {
      List<BookDTO> bookDTOS = bookService.findAll();
      Book entity;

      for (BookDTO dto: bookDTOS) {
         entity = bookService.dtoToEntity(dto);
         assertThat(entity.getEan()).isEqualTo(dto.getEan());
         assertThat(entity.getTitle()).isEqualTo(dto.getTitle());
         assertThat(entity.getQuantity()).isEqualTo(dto.getQuantity());
         assertThat(entity.getStock()).isEqualTo(dto.getStock());
         assertThat(entity.getHeight()).isEqualTo(dto.getHeight());
         assertThat(entity.getLength()).isEqualTo(dto.getLength());
         assertThat(entity.getWeight()).isEqualTo(dto.getWeight());
         assertThat(entity.getWidth()).isEqualTo(dto.getWidth());
         assertThat(entity.getReturnDate()).isEqualTo(dto.getReturnDate());

         assertThat(entity.getIsbn()).isEqualTo(dto.getIsbn());
         assertThat(entity.getAuthorId()).isEqualTo(dto.getAuthor().getId());
         assertThat(entity.getEditorId()).isEqualTo(dto.getEditor().getId());
         assertThat(entity.getPages()).isEqualTo(dto.getPages());
         assertThat(entity.getPublicationDate()).isEqualTo(dto.getPublicationDate());
         assertThat(entity.getFormat().name()).isEqualTo(dto.getFormat());
         assertThat(entity.getType().name()).isEqualTo(dto.getType());
         assertThat(entity.getSummary()).isEqualTo(dto.getSummary());
      }
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Book Entity is converted in right Book DTO")
   void dtoToEntity_returnRightBookDTO_ofBookEntity() {
      List<BookDTO> bookDTOS = bookService.findAll();
      List<Book> books = new ArrayList<>();
      BookDTO dto;

      for (BookDTO b: bookDTOS) {
         books.add(bookService.dtoToEntity(b));
      }

      for (Book entity: books) {
         dto = bookService.entityToDTO(entity);
         assertThat(dto.getEan()).isEqualTo(entity.getEan());
         assertThat(dto.getTitle()).isEqualTo(entity.getTitle());
         assertThat(dto.getQuantity()).isEqualTo(entity.getQuantity());
         assertThat(dto.getStock()).isEqualTo(entity.getStock());
         assertThat(dto.getHeight()).isEqualTo(entity.getHeight());
         assertThat(dto.getLength()).isEqualTo(entity.getLength());
         assertThat(dto.getWeight()).isEqualTo(entity.getWeight());
         assertThat(dto.getWidth()).isEqualTo(entity.getWidth());
         assertThat(dto.getReturnDate()).isEqualTo(entity.getReturnDate());

         assertThat(dto.getIsbn()).isEqualTo(entity.getIsbn());
         assertThat(dto.getAuthor().getId()).isEqualTo(entity.getAuthorId());
         assertThat(dto.getEditor().getId()).isEqualTo(entity.getEditorId());
         assertThat(dto.getPages()).isEqualTo(entity.getPages());
         assertThat(dto.getPublicationDate()).isEqualTo(entity.getPublicationDate());
         assertThat(dto.getFormat()).isEqualTo(entity.getFormat().name());
         assertThat(dto.getType()).isEqualTo(entity.getType().name());
         assertThat(dto.getSummary()).isEqualTo(entity.getSummary());
      }
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
      BookDTO bookDTO = bookService.entityToDTO(newBook);
      List<String> titles = bookService.findAllTitles();
      assertThat(titles.size()).isEqualTo(7);

      // add an other book
      bookDTO = bookService.save(bookDTO);
      titles = bookService.findAllTitles();
      assertThat(titles.size()).isEqualTo(8);

      bookService.deleteById(bookDTO.getEan());
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