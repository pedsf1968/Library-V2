package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.model.Book;
import com.pedsf.library.libraryapi.model.BookFormat;
import com.pedsf.library.libraryapi.model.BookType;
import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.service.BookService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class BookServiceTestIT {
   private static final String BOOK_EAN_TEST = "978-2253002864";
   private static final String BOOK_TITLE_TEST = "Le Horla";

   private static BookService bookService;
   private static PersonService personService;
   private static Book newBook;
   private static BookDTO newBookDTO;
   private static List<BookDTO> allBookDTOS;


   @BeforeAll
   static void beforeAll(@Autowired BookRepository bookRepository,
                         @Autowired PersonRepository personRepository) {
      personService = new PersonService(personRepository);
      bookService = new BookService(bookRepository, personService);
   }

   @BeforeEach
   void beforeEach() {
      newBook = new Book("954-8789797","The green tomato",1,1,"9548789797",2,16);
      newBook.setPages(125);
      newBook.setFormat(BookFormat.COMICS);
      newBook.setType(BookType.HUMOR);
      newBook.setHeight(11);
      newBook.setLength(11);
      newBook.setWidth(11);
      newBook.setWeight(220);
      newBook.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      newBookDTO = new BookDTO("954-8789797","The green tomato",1,1,"9548789797",personService.findById(2),personService.findById(16));
      newBookDTO.setPages(125);
      newBookDTO.setFormat("COMICS");
      newBookDTO.setType("HUMOR");
      newBookDTO.setHeight(11);
      newBookDTO.setLength(11);
      newBookDTO.setWidth(11);
      newBookDTO.setWeight(220);
      newBookDTO.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
              "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
              "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
              " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
              "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
              "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
              " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
              " comes from a line in section 1.10.32.");

      allBookDTOS = bookService.findAll();
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Book exist")
   void existsById_returnTrue_OfAnExistingBookId() {
      for(BookDTO bookDTO : allBookDTOS) {
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
   void findById_returnBook_ofExistingBookId() {
      BookDTO found;

      for(BookDTO bookDTO : allBookDTOS) {
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
      assertThat(allBookDTOS.size()).isEqualTo(7);

      // add one book to increase the list
      newBookDTO = bookService.save(newBookDTO);
      List<BookDTO> bookDTOS = bookService.findAll();
      assertThat(bookDTOS.size()).isEqualTo(8);
      assertThat(bookDTOS.contains(newBookDTO)).isTrue();

      bookService.deleteById(newBookDTO.getEan());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Books that can be booked")
   void findAllAllowed_returnBookableBooks_ofAllBooks() {
      newBook.setStock(-2);
      BookDTO dto = bookService.entityToDTO(newBook);
      dto = bookService.save(dto);
      List<BookDTO> alloweds = bookService.findAllAllowed();

      assertThat(alloweds.contains(dto)).isFalse();

      for(BookDTO bookDTO: allBookDTOS) {
         if (alloweds.contains(bookDTO)) {
            // allowed
            assertThat(bookDTO.getStock()).isGreaterThan(-bookDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(bookDTO.getStock()).isLessThanOrEqualTo(-bookDTO.getQuantity()*2);
         }
      }

      newBook.setStock(1);
      bookService.deleteById(dto.getEan());
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Book by his title and author")
   void findAllFiltered_returnOnlyOneBook_ofExistingFirstTitleAndAuthor() {
      List<BookDTO> found;

      for(BookDTO b:allBookDTOS) {
         BookDTO filter = new BookDTO();
         filter.setTitle(b.getTitle());
         filter.setAuthor(b.getAuthor());

         found = bookService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(b);
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Book by Author")
   void getFirstId_returnFirstId_ofFilteredBookByAuthor() {
      BookDTO filter = new BookDTO();
      filter.setAuthor(personService.findById(1));

      String ean = bookService.getFirstId(filter);

      assertThat(ean).isEqualTo("978-2253004226");
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
   @DisplayName("Verify that we can update a Book")
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
   void deleteById_returnExceptionWhenGetBookById_ofDeletedUserById() {
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
      Book entity;

      for (BookDTO dto: allBookDTOS) {
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
      List<Book> books = new ArrayList<>();
      BookDTO dto;

      for (BookDTO b: allBookDTOS) {
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
      bookDTO.setTitle(BOOK_TITLE_TEST);
      bookDTO = bookService.save(bookDTO);
      titles = bookService.findAllTitles();
      assertThat(titles.size()).isEqualTo(8);
      assertThat(titles.contains(BOOK_TITLE_TEST)).isTrue();

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