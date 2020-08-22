package com.pedsf.library.webapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.BookFormat;
import com.pedsf.library.dto.BookType;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.filter.BookFilter;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.webapi.proxy.LibraryApiProxy;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.web.PathTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import(BookController.class)
@WebMvcTest(controllers = {BookController.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class BookControllerTest {
   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   private static final List<BookDTO> allBookDTOS = new ArrayList<>();
   private static final List<String> booksTitles = new ArrayList<>();
   private static final List<PersonDTO> booksAuthors = new ArrayList<>();
   private static final List<PersonDTO> booksEditors = new ArrayList<>();
   private static UserDTO   newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;
   @MockBean
   private LibraryApiProxy libraryApiProxy;
   @MockBean
   private UserApiProxy userApiProxy;

   private BookController bookController;

   @Configuration
   @EnableWebSecurity
   static class SecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception
      {
         http
               .authorizeRequests()
               .antMatchers("/**").permitAll()
               .anyRequest().anonymous().and()
               .csrf().disable();
      }
   }

   @BeforeAll
   static void beforeAll() {
      booksAuthors.add(new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      booksAuthors.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      booksAuthors.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));

      booksEditors.add( new PersonDTO(10,"LGF","Librairie Générale Française",null));
      booksEditors.add( new PersonDTO(11,"Gallimard","Gallimard",null));
      booksEditors.add( new PersonDTO(12,"Larousse","Larousse",null));

      allBookDTOS.add( new BookDTO("978-2253004226","Germinal",3,2,"9782253004226",booksAuthors.get(0),booksEditors.get(0)));
      allBookDTOS.add( new BookDTO("978-2253002864","Au bonheur des dames",1,0,"9782253002864",booksAuthors.get(0),booksEditors.get(0)));
      allBookDTOS.add( new BookDTO("978-2253003656","Nana",2,1,"9782253003656",booksAuthors.get(0),booksEditors.get(0)));
      allBookDTOS.add( new BookDTO("978-2253010692","L'éducation sentimentale",2,-4,"9782253010692",booksAuthors.get(1),booksEditors.get(0)));
      allBookDTOS.add( new BookDTO("978-2070413119","Madame Bovary",2,-1,"9782070413119",booksAuthors.get(1),booksEditors.get(1)));
      allBookDTOS.add( new BookDTO("978-2253096337","Les Misérables (Tome 1)",3,-6,"9782253096337",booksAuthors.get(2),booksEditors.get(2)));
      allBookDTOS.add( new BookDTO("978-2253096344","Les Misérables (Tome 2)",3,1,"9782253096344",booksAuthors.get(2),booksEditors.get(2)));

      for(BookDTO b : allBookDTOS) {
         booksTitles.add(b.getTitle());
      }

      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      when(libraryApiProxy.findAllAllowedBooks(anyInt())).thenReturn(allBookDTOS);
      when(libraryApiProxy.getAllBooksAuthors()).thenReturn(booksAuthors);
      when(libraryApiProxy.getAllBooksEditors()).thenReturn(booksEditors);
      when(libraryApiProxy.getAllBooksTitles()).thenReturn(booksTitles);
      bookController = new BookController(libraryApiProxy,userApiProxy);
      bookController.setBorrowingQuantityMax(borrowingQuantityMax);
      mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
   }

   @Test
   @Tag("booksList")
   @DisplayName("Verify that the controller send all Book list")
   void booksList_returnBookListAndLinkedList_ofBookFilter() throws Exception {
      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.BOOK_ALL))
            .andReturn();

      // THEN
      List<BookDTO> foundBooks = (List<BookDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_BOOKS);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundAuthors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_AUTHORS);
      Map<Integer,PersonDTO> foundEditors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_EDITORS);
      BookType[] foundTypes = (BookType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      BookFormat[] foundFormats = (BookFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundBooks).hasSameSizeAs(allBookDTOS);
      assertThat(foundTitles).hasSameSizeAs(booksTitles);
      assertThat(foundAuthors).hasSameSizeAs(booksAuthors);
      assertThat(foundEditors).hasSameSizeAs(booksEditors);
      assertThat(foundTypes).hasSameSizeAs(BookType.values());
      assertThat(foundFormats).hasSameSizeAs(BookFormat.values());


      assertThat(foundBooks).isEqualTo(allBookDTOS);
      assertThat(foundTitles).isEqualTo(booksTitles);

      for(int i=0; i<booksAuthors.size(); i++) {
         assertThat(foundAuthors).containsEntry(i+1,booksAuthors.get(i));
      }

      for(int i=0; i<booksEditors.size(); i++) {
         assertThat(foundEditors).containsEntry(i+10,booksEditors.get(i));
      }

      assertThat(foundTypes).isEqualTo(BookType.values());
      assertThat(foundFormats).isEqualTo(BookFormat.values());
   }

   @Test
   @Tag("booksFilteredList")
   @DisplayName("Verify that the controller send filtered Book list")
   void booksFilteredList_returnFilteredBookListAndLinkedList_ofBookFilter()throws Exception {
      BookFilter filter = new BookFilter();

      // GIVEN
      when(libraryApiProxy.findAllFilteredBooks(anyInt() , any(BookDTO.class))).thenReturn(allBookDTOS);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/books")
            .flashAttr("filter",filter))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.BOOK_ALL))
            .andReturn();

      // THEN
      List<BookDTO> foundBooks = (List<BookDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_BOOKS);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundAuthors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_AUTHORS);
      Map<Integer,PersonDTO> foundEditors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_EDITORS);
      BookType[] foundTypes = (BookType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      BookFormat[] foundFormats = (BookFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundBooks).hasSameSizeAs(allBookDTOS);
      assertThat(foundTitles).hasSameSizeAs(booksTitles);
      assertThat(foundAuthors).hasSameSizeAs(booksAuthors);
      assertThat(foundEditors).hasSameSizeAs(booksEditors);
      assertThat(foundTypes).hasSameSizeAs(BookType.values());
      assertThat(foundFormats).hasSameSizeAs(BookFormat.values());


      assertThat(foundBooks).isEqualTo(allBookDTOS);
      assertThat(foundTitles).isEqualTo(booksTitles);

      for(int i=0; i<booksAuthors.size(); i++) {
         assertThat(foundAuthors).containsEntry(i+1,booksAuthors.get(i));
      }
      for(int i=0; i<booksEditors.size(); i++) {
         assertThat(foundEditors).containsEntry(i+10,booksEditors.get(i));
      }

      assertThat(foundTypes).isEqualTo(BookType.values());
      assertThat(foundFormats).isEqualTo(BookFormat.values());
   }

   @Test
   @Tag("bookView")
   @DisplayName("Verify that the controller send the Book expected")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void bookView_returnBook_ofOneBookId() throws Exception {
      BookDTO expected = allBookDTOS.get(3);
      String bookEAN = expected.getEan();

      // GIVEN
      when(libraryApiProxy.findBookById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/book/" + bookEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.BOOK_READ))
            .andReturn();

      // THEN
      BookDTO found = (BookDTO) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_BOOK);
      Boolean canBorrow = (Boolean) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(found).isEqualTo(expected);
      assertThat(canBorrow).isTrue();
   }

   @Test
   @Tag("bookView")
   @DisplayName("Verify that the user can't borrow more Book if counter equal max quentity")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void bookView_returnCanBorrowIsFalse_ofUserWithMaxCounter() throws Exception {
      BookDTO expected = allBookDTOS.get(3);
      String bookEAN = expected.getEan();

      newUserDTO.setCounter(borrowingQuantityMax);
      // GIVEN
      when(libraryApiProxy.findBookById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/book/" + bookEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.BOOK_READ))
            .andReturn();

      // THEN
      Boolean canBorrow = (Boolean) Objects.requireNonNull(result.getModelAndView()).getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(canBorrow).isFalse();
   }

}