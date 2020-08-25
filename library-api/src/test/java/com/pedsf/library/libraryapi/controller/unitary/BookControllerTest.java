package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.BadRequestException;
import com.pedsf.library.exception.ConflictException;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.controller.BookController;
import com.pedsf.library.libraryapi.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = {BookController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BookControllerTest {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private BookService bookService;
   private static ObjectMapper mapper = new ObjectMapper();
   private static BookDTO newBookDTO;

   @BeforeAll
   static void beforeAll() {
      newBookDTO = new BookDTO("954-8789797","The green tomato",1,1,"9548789797",
              new PersonDTO(3, "Victor", "HUGO", Date.valueOf("1802-02-26")),
              new PersonDTO(10, "LGF", "Librairie Générale Française", null));
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
   }

   @Test
   @Tag("findAllBooks")
   @DisplayName("Verify that we get NotFound if there are no Books")
   void findAllBooks_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookService.findAll()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/books"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("findAllAllowedBooks")
   @DisplayName("Verify that we get NotFound if there are no allowed Books")
   void findAllAllowedBooks_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookService.findAllAllowed()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/books/allowed"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we get NotFound if there are no Books")
   void findAllFiltered_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookService.findAllFiltered(newBookDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newBookDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/books/searches")
              .contentType(MediaType.APPLICATION_JSON)
              .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
              .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we get NotFound if with wrong Id")
   void findById_returnNotFound_ofWrongBookId() throws Exception {
      // GIVEN
      when(bookService.findById(anyString())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/books/" + 456))
                      .andExpect(MockMvcResultMatchers.status().isNotFound())
                      .andReturn();
   }

   @Test
   @Tag("addBook")
   @DisplayName("Verify that we get Conflict if there are ConflictException when save Book")
   void addBook_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(bookService.save(newBookDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newBookDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/books")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("addBook")
   @DisplayName("Verify that we get BadRequest if there are BadRequestException when save Book")
   void addBook_returnBadRequest_ofBadRequestException() throws Exception {
      // GIVEN
      when(bookService.save(newBookDTO)).thenThrow(BadRequestException.class);

      String json = mapper.writeValueAsString(newBookDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/books")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isBadRequest())
              .andReturn();
   }

   @Test
   @Tag("updateBook")
   @DisplayName("Verify that we get NotFound if there are no Book to update")
   void updateBook_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookService.update(newBookDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newBookDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/books/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("updateBook")
   @DisplayName("Verify that we get Conflict if there are no Book ConflictException when update")
   void updateBook_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(bookService.update(newBookDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newBookDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/books/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("deleteBook")
   @DisplayName("Verify that we get NotFound if there are no Book to delete")
   void deleteBook_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(bookService).deleteById(anyString());

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/books/"+456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllBooksAuthors")
   @DisplayName("Verify that we get NotFound if there are no Book Author")
   void getAllBooksAuthors_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(bookService).findAllAuthors();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/books/authors"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllBooksAllEditors")
   @DisplayName("Verify that we get NotFound if there are no Book Editors")
   void getAllBooksEditors_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(bookService).findAllEditors();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/books/editors"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllBooksTitles")
   @DisplayName("Verify that we get NotFound if there are no Book Titles")
   void getAllBooksTitles_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(bookService).findAllTitles();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/books/titles"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

}
