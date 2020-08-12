package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.controller.BookController;
import com.pedsf.library.libraryapi.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {BookController.class})
@ExtendWith(SpringExtension.class)
class BookControllerTestIT {
   private static final List<BookDTO> allBookDTOS = new ArrayList<>();
   private static final List<PersonDTO> allPersonDTOS = new ArrayList<>();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private BookService bookService;

   @BeforeAll
   static void beforeAll() {
      allPersonDTOS.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersonDTOS.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersonDTOS.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersonDTOS.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersonDTOS.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersonDTOS.add( new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersonDTOS.add( new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersonDTOS.add( new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersonDTOS.add( new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersonDTOS.add( new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersonDTOS.add( new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersonDTOS.add( new PersonDTO(12,"Larousse","Larousse",null));
      allPersonDTOS.add( new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersonDTOS.add( new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersonDTOS.add( new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersonDTOS.add( new PersonDTO(16,"Microsoft","Microsoft",null));

      allBookDTOS.add( new BookDTO("978-2253004226","Germinal",3,2,"9782253004226",allPersonDTOS.get(0),allPersonDTOS.get(10)));
      allBookDTOS.add( new BookDTO("978-2253002864","Au bonheur des dames",1,0,"9782253002864",allPersonDTOS.get(0),allPersonDTOS.get(10)));
      allBookDTOS.add( new BookDTO("978-2253003656","Nana",2,1,"9782253003656",allPersonDTOS.get(0),allPersonDTOS.get(10)));
      allBookDTOS.add( new BookDTO("978-2253010692","L'éducation sentimentale",2,-4,"9782253010692",allPersonDTOS.get(1),allPersonDTOS.get(10)));
      allBookDTOS.add( new BookDTO("978-2070413119","Madame Bovary",2,-1,"9782070413119",allPersonDTOS.get(1),allPersonDTOS.get(10)));
      allBookDTOS.add( new BookDTO("978-2253096337","Les Misérables (Tome 1)",3,-6,"9782253096337",allPersonDTOS.get(2),allPersonDTOS.get(10)));
      allBookDTOS.add( new BookDTO("978-2253096344","Les Misérables (Tome 2)",3,1,"9782253096344",allPersonDTOS.get(2),allPersonDTOS.get(10)));
   }

   @Disabled
   @Test
   @Tag("findAllBooks")
   @DisplayName("Verify that we get the list of all Books")
   void findAllBooks_returnAllBooks() throws Exception {
      int i = 0;
      // GIVEN
      when(bookService.findAll()).thenReturn(allBookDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<BookDTO> founds = Arrays.asList(mapper.readValue(json, BookDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(7);
      for(BookDTO bookDTO: allBookDTOS) {
         assertThat(bookDTO).isEqualTo(founds.get(i++));
      }

   }

   @Test
   void findAllAllowedBooks() {
   }

   @Test
   void findAllFilteredBooks() {
   }

   @Test
   void findBookById() {
   }

   @Test
   void addBook() {
   }

   @Test
   void updateBook() {
   }

   @Test
   void deleteBook() {
   }

   @Test
   void getAllBooksAuthors() {
   }

   @Test
   void getAllBooksEditors() {
   }

   @Test
   void getAllBooksTitles() {
   }
}