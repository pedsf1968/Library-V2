package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.pedsf.library.dto.BookFormat;
import com.pedsf.library.dto.BookType;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.controller.BookController;
import com.pedsf.library.libraryapi.service.BookService;
import lombok.extern.slf4j.Slf4j;
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
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(controllers = {BookController.class})
@ExtendWith(SpringExtension.class)
class BookControllerTestIT {
   private static final String BOOK_TITLE_TEST = "Le Horla";

   private static final List<BookDTO> allBookDTOS = new ArrayList<>();
   private static final Map<Integer,PersonDTO> allPersonDTOS = new HashMap<>();
   private static ObjectMapper mapper = new ObjectMapper();



   @Inject
   private MockMvc mockMvc;

   @MockBean
   private BookService bookService;

   @BeforeAll
   static void beforeAll() {
      allPersonDTOS.put(1, new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersonDTOS.put(2, new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersonDTOS.put(3, new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersonDTOS.put(4, new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersonDTOS.put(5, new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersonDTOS.put(6, new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersonDTOS.put(7, new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersonDTOS.put(8, new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersonDTOS.put(9, new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersonDTOS.put(10, new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersonDTOS.put(11, new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersonDTOS.put(12, new PersonDTO(12,"Larousse","Larousse",null));
      allPersonDTOS.put(13, new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersonDTOS.put(14, new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersonDTOS.put(15, new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersonDTOS.put(16, new PersonDTO(16,"Microsoft","Microsoft",null));

      allBookDTOS.add( new BookDTO("978-2253004226","Germinal",3,2,"9782253004226",allPersonDTOS.get(1),allPersonDTOS.get(11)));
      allBookDTOS.add( new BookDTO("978-2253002864","Au bonheur des dames",1,0,"9782253002864",allPersonDTOS.get(1),allPersonDTOS.get(11)));
      allBookDTOS.add( new BookDTO("978-2253003656","Nana",2,1,"9782253003656",allPersonDTOS.get(1),allPersonDTOS.get(11)));
      allBookDTOS.add( new BookDTO("978-2253010692","L'éducation sentimentale",2,-4,"9782253010692",allPersonDTOS.get(2),allPersonDTOS.get(11)));
      allBookDTOS.add( new BookDTO("978-2070413119","Madame Bovary",2,-1,"9782070413119",allPersonDTOS.get(2),allPersonDTOS.get(12)));
      allBookDTOS.add( new BookDTO("978-2253096337","Les Misérables (Tome 1)",3,-6,"9782253096337",allPersonDTOS.get(3),allPersonDTOS.get(13)));
      allBookDTOS.add( new BookDTO("978-2253096344","Les Misérables (Tome 2)",3,1,"9782253096344",allPersonDTOS.get(3),allPersonDTOS.get(13)));
      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @Test
   @Tag("findAllBooks")
   @DisplayName("Verify that we get the list of all Books")
   void findAllBooks_returnAllBooks() throws Exception {
      // GIVEN
      when(bookService.findAll()).thenReturn(allBookDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BookDTO> founds = Arrays.asList(mapper.readValue(json, BookDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(7);
      for(BookDTO dto: founds) {
         for(BookDTO expected:allBookDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllAllowedBooks")
   @DisplayName("Verify that we get the right list of allowed Books")
   void findAllAllowedBooks()  throws Exception {
      // GIVEN
      when(bookService.findAllAllowed()).thenReturn(allBookDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books/allowed"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BookDTO> founds = Arrays.asList(mapper.readValue(json, BookDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(7);
      for(BookDTO dto: founds) {
         for(BookDTO expected:allBookDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllFilteredBooks")
   @DisplayName("Verify that we get Book list from first author")
   void findAllFilteredBooks() throws Exception {
      List<BookDTO> filtered = allBookDTOS.subList(0,3);
      BookDTO filter = new BookDTO();

      // GIVEN
      filter.setAuthor(filtered.get(0).getAuthor());
      when(bookService.findAllFiltered(any(BookDTO.class))).thenReturn(filtered);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/books/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BookDTO> founds = Arrays.asList(mapper.readValue(json, BookDTO[].class));
      List<BookDTO> found;

      assertThat(founds.size()).isEqualTo(3);
      for(BookDTO dto: founds) {
         for(BookDTO expected:filtered) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }

   }

   @Test
   @Tag("findBookById")
   @DisplayName("Verify that we can get Book by his EAN")
   void findBookById()  throws Exception {
      BookDTO expected = allBookDTOS.get(3);
      String ean = expected.getEan();

      // GIVEN
      when(bookService.findById(ean)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books/"+ean))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BookDTO found = mapper.readValue(json, BookDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("addBook")
   @DisplayName("Verify that we can add Book")
   void addBook() throws Exception {
      BookDTO expected = allBookDTOS.get(4);
      expected.setFormat(BookFormat.POCKET.name());
      expected.setType(BookType.HUMOR.name());

      // GIVEN
      when(bookService.save(any(BookDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/books")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BookDTO found = mapper.readValue(json, BookDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("updateBook")
   @DisplayName("Verify that we can update a Book")
   void updateBook() throws Exception {
      BookDTO expected = allBookDTOS.get(4);
      expected.setTitle(BOOK_TITLE_TEST);
      expected.setFormat(BookFormat.POCKET.name());
      expected.setType(BookType.HUMOR.name());

      // GIVEN
      when(bookService.update(any(BookDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/books/"+ expected.getEan())
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BookDTO found = mapper.readValue(json, BookDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("deleteBook")
   @DisplayName("Verify that we can delate a Book")
   void deleteBook() throws Exception {
      BookDTO expected = allBookDTOS.get(2);

      // GIVEN
      doNothing().when(bookService).deleteById(expected.getEan());

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.delete("/books/"+ expected.getEan())
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();
   }

   @Test
   @Tag("getAllBooksAuthors")
   @DisplayName("Verify that we get all authors list")
   void getAllBooksAuthors() throws Exception {
      List<PersonDTO> authors = new ArrayList<>();
      for(BookDTO bookDTO : allBookDTOS) {
         authors.add(bookDTO.getAuthor());
      }

      // GIVEN
      when(bookService.findAllAuthors()).thenReturn(authors);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books/authors"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<PersonDTO> founds = Arrays.asList(mapper.readValue(json, PersonDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(authors.size());
      for(PersonDTO dto: founds) {
         for(PersonDTO expected:authors) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("getAllBooksEditors")
   @DisplayName("Verify that we get all editors list")
   void getAllBooksEditors() throws Exception {
      List<PersonDTO> editors = new ArrayList<>();
      for(BookDTO bookDTO : allBookDTOS) {
         editors.add(bookDTO.getEditor());
      }

      // GIVEN
      when(bookService.findAllEditors()).thenReturn(editors);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books/editors"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<PersonDTO> founds = Arrays.asList(mapper.readValue(json, PersonDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(editors.size());
      for(PersonDTO dto: founds) {
         for(PersonDTO expected:editors) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("getAllBooksTitles")
   @DisplayName("Verify that we get all titles list")
   void getAllBooksTitles() throws Exception {
      List<String> titles = new ArrayList<>();
      for(BookDTO bookDTO : allBookDTOS) {
         titles.add(bookDTO.getTitle());
      }

      // GIVEN
      when(bookService.findAllTitles()).thenReturn(titles);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/books/titles"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<String> founds = Arrays.asList(mapper.readValue(json, String[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(titles.size());
      for(String title: founds) {
         assertThat(titles.contains(title)).isTrue();
      }
   }
}