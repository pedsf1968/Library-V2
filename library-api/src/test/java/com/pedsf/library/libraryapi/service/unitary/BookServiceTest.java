package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.model.Book;
import com.pedsf.library.libraryapi.model.BookFormat;
import com.pedsf.library.libraryapi.model.BookType;
import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.service.BookService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BookServiceTest {
   /*
   private static final String BOOK_EAN_TEST = "978-2253002864";
   private static final String BOOK_TITLE_TEST = "Le Horla";


   @Mock
   private PersonService personService;
   @Mock
   private BookRepository bookRepository;

   @InjectMocks
   private BookService bookService = new BookService(bookRepository,personService);


   List<PersonDTO> allPersons = new ArrayList<>();
   List<Book> allBooks = new ArrayList<>();

   private Book newBook;
   private BookDTO newBookDTO;
   private List<BookDTO> allBookDTOS = bookService.findAll();

   @BeforeAll
   static void beforeAll() {

   }


   @BeforeEach
   void beforeEach() {
      List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6,7,8,9,10,11,12,13,14,15,16,17);
      allPersons.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02"),null,null));
      allPersons.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12"),null,null));
      allPersons.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26"),null,null));
      allPersons.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14"),null,null));
      allPersons.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02"),null,null));
      allPersons.add( new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17"),null,null));
      allPersons.add( new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10"),null,null));
      allPersons.add( new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26"),null,null));
      allPersons.add( new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08"),null,null));
      allPersons.add( new PersonDTO(11,"LGF","Librairie Générale Française",null,null,null));
      allPersons.add( new PersonDTO(12,"Gallimard","Gallimard",null,"http://www.gallimard.fr",null));
      allPersons.add( new PersonDTO(13,"Larousse","Larousse",null,"https://www.larousse.fr/",null));
      allPersons.add( new PersonDTO(14,"Blackpink","Blackpink",Date.valueOf("2016-06-01"),null,null));
      allPersons.add( new PersonDTO(15,"BigBang","BigBang",Date.valueOf("2006-08-19"),null,null));
      allPersons.add( new PersonDTO(16,"EA","Electronic Arts",Date.valueOf("1982-05-28"),null,null));
      allPersons.add( new PersonDTO(17,"Microsoft","Microsoft",null,null,null));

      when(personService.findAll()).thenReturn(allPersons);
      when(personService.count()).thenReturn(17);

      allBooks.add( new Book("978-2253004226","9782253004226","Germinal",1,2,2));
      allBooks.add( new Book("978-2253002864","9782253002864","Au bonheur des dames",1,2,0));
      allBooks.add( new Book("978-2253003656","9782253003656","Nana",1,2,1));
      allBooks.add( new Book("978-2253010692","9782253010692","L'éducation sentimentale",2,2,2));
      allBooks.add( new Book("978-2070413119","9782070413119","Madame Bovary",2,3,2));
      allBooks.add( new Book("978-2253096337","9782253096337","Les Misérables (Tome 1)",3,3,3));
      allBooks.add( new Book("978-2253096344","9782253096344","Les Misérables (Tome 2)",3,3,2));

      when(bookRepository.findAll()).thenReturn(allBooks);

      newBook = new Book();
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
      newBook.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      newBookDTO = bookService.entityToDTO(newBook);
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
      assertThat(bookDTOS.contains(newBookDTO)).isTrue();

      bookService.deleteById(newBookDTO.getEan());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Books that can be booked")
   void findAllAllowed_returnBookableBooks_ofAllBooks() {
      newBook.setStock(-2);
      BookDTO newBookDTO = bookService.entityToDTO(newBook);
      newBookDTO = bookService.save(newBookDTO);
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
      BookDTO newBookDTO = bookService.entityToDTO(newBook);
      List<String> titles = bookService.findAllTitles();
      assertThat(titles.size()).isEqualTo(7);

      // add an other book
      newBookDTO.setTitle(BOOK_TITLE_TEST);
      newBookDTO = bookService.save(newBookDTO);
      titles = bookService.findAllTitles();
      assertThat(titles.size()).isEqualTo(8);
      assertThat(titles.contains(BOOK_TITLE_TEST)).isTrue();

      bookService.deleteById(newBookDTO.getEan());
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


    */
}