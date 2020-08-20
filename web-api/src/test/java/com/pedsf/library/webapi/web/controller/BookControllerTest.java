package com.pedsf.library.webapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.webapi.proxy.LibraryApiProxy;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.web.PathTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.sql.Date;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j

@WebMvcTest(controllers = {BookController.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class BookControllerTest {

   private static final List<BookDTO> allBookDTOS = new ArrayList<>();
   private static final Map<Integer, PersonDTO> allPersonDTOS = new HashMap<>();
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private LibraryApiProxy libraryApiProxy;
   @MockBean
   private UserApiProxy userApiProxy;

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

   @Disabled
   @Test
   @Tag("booksList")
   void booksList() throws Exception {
      // GIVEN
      when(libraryApiProxy.findAllAllowedBooks(anyInt())).thenReturn(allBookDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.BOOK_ALL))
            .andReturn();
   }

   @Test
   void booksFilteredList() {
   }

   @Test
   void bookView() {
   }
}