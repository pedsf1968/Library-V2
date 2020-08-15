package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.*;
import com.pedsf.library.libraryapi.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BookServiceTest {
   private static final String BOOK_TITLE_TEST = "Le Horla";
   private static final List<PersonDTO> allPersons = new ArrayList<>();
   private static final List<Book> allBooks = new ArrayList<>();
   private static final List<Book> allAllowedBooks = new ArrayList<>();

   @Mock
   private PersonService personService;
   @Mock
   private BookRepository bookRepository;
   private BookService bookService;
   private Book newBook;
   private BookDTO newBookDTO;

   @BeforeAll
   static void beforeAll() {
      allPersons.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersons.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersons.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersons.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersons.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersons.add( new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersons.add( new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersons.add( new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersons.add( new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersons.add( new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersons.add( new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersons.add( new PersonDTO(12,"Larousse","Larousse",null));
      allPersons.add( new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersons.add( new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersons.add( new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersons.add( new PersonDTO(16,"Microsoft","Microsoft",null));

      allBooks.add( new Book("978-2253004226","Germinal",3,2,"9782253004226",1,11));
      allBooks.add( new Book("978-2253002864","Au bonheur des dames",1,0,"9782253002864",1,11));
      allBooks.add( new Book("978-2253003656","Nana",2,1,"9782253003656",1,11));
      allBooks.add( new Book("978-2253010692","L'éducation sentimentale",2,-4,"9782253010692",2,11));
      allBooks.add( new Book("978-2070413119","Madame Bovary",2,-1,"9782070413119",2,11));
      allBooks.add( new Book("978-2253096337","Les Misérables (Tome 1)",3,-6,"9782253096337",3,11));
      allBooks.add( new Book("978-2253096344","Les Misérables (Tome 2)",3,1,"9782253096344",3,11));

      allAllowedBooks.add( new Book("978-2253004226","Germinal",3,2,"9782253004226",1,11));
      allAllowedBooks.add( new Book("978-2253002864","Au bonheur des dames",1,0,"9782253002864",1,11));
      allAllowedBooks.add( new Book("978-2253003656","Nana",2,1,"9782253003656",1,11));
      allAllowedBooks.add( new Book("978-2070413119","Madame Bovary",2,-1,"9782070413119",2,11));
      allAllowedBooks.add( new Book("978-2253096344","Les Misérables (Tome 2)",3,1,"9782253096344",3,11));

      for(Book book:allBooks) {
         book.setFormat(BookFormat.POCKET);
         book.setType(BookType.NOVEL);
      }

      for(Book book:allAllowedBooks) {
         book.setFormat(BookFormat.POCKET);
         book.setType(BookType.NOVEL);
      }

   }

   @BeforeEach
   void beforeEach() {
      bookService = new BookService(bookRepository,personService);

      newBook = new Book("954-8789797","The green tomato",1,1,"9548789797",2,10);
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

      newBookDTO = new BookDTO("954-8789797","The green tomato",1,1,"9548789797",allPersons.get(1),allPersons.get(9));
      newBookDTO.setPages(125);
      newBookDTO.setFormat(BookFormat.COMICS.name());
      newBookDTO.setType(BookType.HUMOR.name());
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

      Mockito.lenient().when(bookRepository.findAll()).thenReturn(allBooks);
      Mockito.lenient().when(bookRepository.findAllAllowed()).thenReturn(allAllowedBooks);
      Mockito.lenient().when(bookRepository.findByEan("978-2253004226")).thenReturn(java.util.Optional.ofNullable(allBooks.get(0)));
      Mockito.lenient().when(bookRepository.findByEan("978-2253002864")).thenReturn(java.util.Optional.ofNullable(allBooks.get(1)));
      Mockito.lenient().when(bookRepository.findByEan("978-2253003656")).thenReturn(java.util.Optional.ofNullable(allBooks.get(2)));
      Mockito.lenient().when(bookRepository.findByEan("978-2253010692")).thenReturn(java.util.Optional.ofNullable(allBooks.get(3)));
      Mockito.lenient().when(bookRepository.findByEan("978-2070413119")).thenReturn(java.util.Optional.ofNullable(allBooks.get(4)));
      Mockito.lenient().when(bookRepository.findByEan("978-2253096337")).thenReturn(java.util.Optional.ofNullable(allBooks.get(5)));
      Mockito.lenient().when(bookRepository.findByEan("978-2253096344")).thenReturn(java.util.Optional.ofNullable(allBooks.get(6)));
      Mockito.lenient().when(bookRepository.findByEan("954-8789797")).thenReturn(java.util.Optional.ofNullable(newBook));

      Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]-1));
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Book exist")
   void existsById_returnTrue_OfAnExistingBookId() {
      for(Book book : allBooks) {
         String ean = book.getEan();
         assertThat(bookService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Book doesn't exist")
   void existsById_returnFalse_OfAnInexistingBookId() {
      assertThat(bookService.existsById("78-22530042265lkjh5")).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Book by is ID")
   void findById_returnBook_ofExistingBookId() {
      BookDTO found;

      for(Book book : allBooks) {
         String ean = book.getEan();
         found = bookService.findById(ean);

         assertThat(found).isEqualTo(bookService.entityToDTO(book));
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Book with wrong ID")
   void findById_returnException_ofInexistingBookId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> bookService.findById("klgqsdf"));
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Books")
   void findAll_returnAllBooks() {
      List<BookDTO> bookDTOS = bookService.findAll();

      assertThat(bookDTOS.size()).isEqualTo(7);

      for(Book book: allBooks) {
         BookDTO bookDTO = bookService.entityToDTO(book);
         assertThat(bookDTOS.contains(bookDTO)).isTrue();
      }
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no Book")
   void findAll_throwResourceNotFoundException_ofEmptyList() {
      List<Book> emptyList = new ArrayList<>();
      Mockito.lenient().when(bookRepository.findAll()).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> bookService.findAll());
   }


   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Books that can be booked")
   void findAllAllowed_returnBookableBooks_ofAllBooks() {
      List<BookDTO> alloweds = bookService.findAllAllowed();
      assertThat(alloweds.size()).isEqualTo(5);

      for(Book book: allBooks) {
         BookDTO bookDTO = bookService.entityToDTO(book);

         if (alloweds.contains(bookDTO)) {
            // allowed
            assertThat(bookDTO.getQuantity()*2).isGreaterThan(-bookDTO.getStock());
         } else {
            // not allowed
            assertThat(bookDTO.getQuantity()*2).isEqualTo(-bookDTO.getStock());
         }
      }
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Book by his title and author")
   void findAllFiltered_returnOnlyOneBook_ofExistingFirstTitleAndAuthor() {
      List<BookDTO> found;

      for(Book book:allBooks) {
         Book filter = new Book();
         filter.setTitle(book.getTitle());
         filter.setAuthorId(book.getAuthorId());

         Mockito.lenient().when(bookRepository.findAll(any(BookSpecification.class))).thenReturn(Collections.singletonList(book));

         BookDTO filterDTO = bookService.entityToDTO(filter);
         found = bookService.findAllFiltered(filterDTO);

         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(bookService.entityToDTO(book));
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Book by Author")
   void getFirstId_returnFirstId_ofFilteredBookByAuthor() {
      BookDTO filter = new BookDTO();
      filter.setAuthor(personService.findById(1));

      Mockito.lenient().when(bookRepository.findAll(any(BookSpecification.class))).thenReturn(Arrays.asList(allBooks.get(0),allBooks.get(1),allBooks.get(2)));

      String ean = bookService.getFirstId(filter);

      assertThat(ean).isEqualTo("978-2253004226");
   }


   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Book")
   void save_returnCreatedBook_ofNewBook() {
      Mockito.lenient().when(bookRepository.save(any(Book.class))).thenReturn(newBook);

      BookDTO found = bookService.save(newBookDTO);
      String ean = found.getEan();

      assertThat(bookService.existsById(ean)).isTrue();
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Book with has no title")
   void save_throwBadRequestException_ofNewBookWithNoTitle() {
      newBookDTO.setTitle("");
      Assertions.assertThrows(BadRequestException.class, ()-> bookService.save(newBookDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Book with has no Author")
   void save_throwBadRequestException_ofNewBookWithNoAuthor() {
      newBookDTO.setAuthor(null);
      Assertions.assertThrows(BadRequestException.class, ()-> bookService.save(newBookDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Book with has no Editor")
   void save_throwBadRequestException_ofNewBookWithNoEditor() {
      newBookDTO.setEditor(null);
      Assertions.assertThrows(BadRequestException.class, ()-> bookService.save(newBookDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Book with has no Format")
   void save_throwBadRequestException_ofNewBookWithNoFormat() {
      newBookDTO.setFormat(null);
      Assertions.assertThrows(BadRequestException.class, ()-> bookService.save(newBookDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Book with has no Type")
   void save_throwBadRequestException_ofNewBookWithNoType() {
      newBookDTO.setType(null);
      Assertions.assertThrows(BadRequestException.class, ()-> bookService.save(newBookDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Book")
   void update_returnUpdatedBook_ofBookAndNewTitle() {
      String oldTitle = newBook.getTitle();
      newBook.setTitle(BOOK_TITLE_TEST);
      newBookDTO.setTitle(BOOK_TITLE_TEST);
      Mockito.lenient().when(bookRepository.save(any(Book.class))).thenReturn(newBook);

      BookDTO bookSaved = bookService.update(newBookDTO);
      assertThat(bookSaved).isEqualTo(newBookDTO);

      newBook.setTitle(oldTitle);
      newBookDTO.setTitle(oldTitle);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Book with has no title")
   void update_throwConflictException_ofNewBookWithNoTitle() {
      newBookDTO.setTitle("");
      Assertions.assertThrows(ConflictException.class, ()-> bookService.update(newBookDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Book with has no Author")
   void update_throwConflictException_ofNewBookWithNoAuthor() {
      newBookDTO.setAuthor(null);
      Assertions.assertThrows(ConflictException.class, ()-> bookService.update(newBookDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Book with has no Editor")
   void update_throwConflictException_ofNewBookWithNoEditor() {
      newBookDTO.setEditor(null);
      Assertions.assertThrows(ConflictException.class, ()-> bookService.update(newBookDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Book with has no Format")
   void update_throwConflictException_ofNewBookWithNoFormat() {
      newBookDTO.setFormat(null);
      Assertions.assertThrows(ConflictException.class, ()-> bookService.update(newBookDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Book with has no Type")
   void update_throwConflictException_ofNewBookWithNoType() {
      newBookDTO.setType(null);
      Assertions.assertThrows(ConflictException.class, ()-> bookService.update(newBookDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ResourceNotFoundException when update a Book with bad ID")
   void update_throwResourceNotFoundException_ofNewBookWithWrongId() {
      newBookDTO.setEan("mlkhmlkjmlk");
      Assertions.assertThrows(ResourceNotFoundException.class, ()-> bookService.update(newBookDTO));
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Book by his EAN")
   void deleteById_returnExceptionWhenGetUserById_ofDeletedUserById() {
      String ean = newBookDTO.getEan();

      assertThat(bookService.existsById(ean)).isTrue();
      bookService.deleteById(ean);
      Mockito.lenient().when(bookRepository.findByEan(ean)).thenThrow(ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> bookService.findById(ean));
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Books")
   void count_returnTheNumberOfBooks() {
      Mockito.lenient().when(bookRepository.count()).thenReturn(7L);
      assertThat(bookService.count()).isEqualTo(7);
   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Book DTO is converted in right Book Entity")
   void dtoToEntity_returnRightBookEntity_ofBookDTO() {
      Book entity = bookService.dtoToEntity(newBookDTO);

      assertThat(entity.getEan()).isEqualTo(newBookDTO.getEan());
      assertThat(entity.getTitle()).isEqualTo(newBookDTO.getTitle());
      assertThat(entity.getQuantity()).isEqualTo(newBookDTO.getQuantity());
      assertThat(entity.getStock()).isEqualTo(newBookDTO.getStock());
      assertThat(entity.getHeight()).isEqualTo(newBookDTO.getHeight());
      assertThat(entity.getLength()).isEqualTo(newBookDTO.getLength());
      assertThat(entity.getWeight()).isEqualTo(newBookDTO.getWeight());
      assertThat(entity.getWidth()).isEqualTo(newBookDTO.getWidth());
      assertThat(entity.getReturnDate()).isEqualTo(newBookDTO.getReturnDate());

      assertThat(entity.getIsbn()).isEqualTo(newBookDTO.getIsbn());
      assertThat(entity.getAuthorId()).isEqualTo(newBookDTO.getAuthor().getId());
      assertThat(entity.getEditorId()).isEqualTo(newBookDTO.getEditor().getId());
      assertThat(entity.getPages()).isEqualTo(newBookDTO.getPages());
      assertThat(entity.getPublicationDate()).isEqualTo(newBookDTO.getPublicationDate());
      assertThat(entity.getFormat().name()).isEqualTo(newBookDTO.getFormat());
      assertThat(entity.getType().name()).isEqualTo(newBookDTO.getType());
      assertThat(entity.getSummary()).isEqualTo(newBookDTO.getSummary());
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Book Entity is converted in right Book DTO")
   void dtoToEntity_returnRightBookDTO_ofBookEntity() {

      BookDTO dto = bookService.entityToDTO(newBook);

      assertThat(dto.getEan()).isEqualTo(newBook.getEan());
      assertThat(dto.getTitle()).isEqualTo(newBook.getTitle());
      assertThat(dto.getQuantity()).isEqualTo(newBook.getQuantity());
      assertThat(dto.getStock()).isEqualTo(newBook.getStock());
      assertThat(dto.getHeight()).isEqualTo(newBook.getHeight());
      assertThat(dto.getLength()).isEqualTo(newBook.getLength());
      assertThat(dto.getWeight()).isEqualTo(newBook.getWeight());
      assertThat(dto.getWidth()).isEqualTo(newBook.getWidth());
      assertThat(dto.getReturnDate()).isEqualTo(newBook.getReturnDate());

      assertThat(dto.getIsbn()).isEqualTo(newBook.getIsbn());
      assertThat(dto.getAuthor().getId()).isEqualTo(newBook.getAuthorId());
      assertThat(dto.getEditor().getId()).isEqualTo(newBook.getEditorId());
      assertThat(dto.getPages()).isEqualTo(newBook.getPages());
      assertThat(dto.getPublicationDate()).isEqualTo(newBook.getPublicationDate());
      assertThat(dto.getFormat()).isEqualTo(newBook.getFormat().name());
      assertThat(dto.getType()).isEqualTo(newBook.getType().name());
      assertThat(dto.getSummary()).isEqualTo(newBook.getSummary());
   }

   @Test
   @Tag("findAllAuthors")
   @DisplayName("Verify that we get all Books authors")
   void findAllAuthors() {
      Mockito.lenient().when(bookRepository.findAllAuthors()).thenReturn(Arrays.asList(1,2,3));
      List<PersonDTO> personDTOS = bookService.findAllAuthors();
      assertThat(personDTOS.size()).isEqualTo(3);
      assertThat(personDTOS.contains(allPersons.get(0))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(1))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(2))).isTrue();
   }

   @Test
   @Tag("findAllEditors")
   @DisplayName("Verify that we get all Books editors")
   void findAllEditors() {
      Mockito.lenient().when(bookRepository.findAllEditors()).thenReturn(Arrays.asList(10,11,12));
      List<PersonDTO> personDTOS = bookService.findAllEditors();
      assertThat(personDTOS.size()).isEqualTo(3);
      assertThat(personDTOS.contains(allPersons.get(9))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(10))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(11))).isTrue();
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Books titles")
   void findAllTitles() {
      List<String> titles = Arrays.asList("Germinal","Au bonheur des dames","Nana","L'éducation sentimentale","Madame Bovary","Les Misérables (Tome 1)","Les Misérables (Tome 2)");
      Mockito.lenient().when(bookRepository.findAllTitles()).thenReturn(titles);

      List<String> foundTitles = bookService.findAllTitles();
      assertThat(foundTitles.size()).isEqualTo(7);
      for(String title:foundTitles) {
         assertThat(titles.contains(title)).isTrue();
      }
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Book by his EAN number")
   void increaseStock_returnBookWithIncrementedStock_ofOneBook() {
      String ean = newBook.getEan();
      Integer oldStock = newBook.getStock();
      newBook.setStock(oldStock+1);
      Mockito.lenient().when(bookRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newBook));

      bookService.increaseStock(ean);
      BookDTO bookDTO = bookService.findById(ean);
      assertThat(bookDTO.getStock()).isEqualTo(oldStock+1);

      newBook.setStock(oldStock);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Book by his EAN number")
   void decreaseStock_returnBookWithDecrementedStock_ofOneBook() {
      String ean = newBook.getEan();
      Integer oldStock = newBook.getStock();
      newBook.setStock(oldStock-1);
      Mockito.lenient().when(bookRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newBook));

      bookService.decreaseStock(ean);

      BookDTO bookDTO = bookService.findById(ean);
      assertThat(bookDTO.getStock()).isEqualTo(oldStock-1);

      newBook.setStock(oldStock);
   }

}