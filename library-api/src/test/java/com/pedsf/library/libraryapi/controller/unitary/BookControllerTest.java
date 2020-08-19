package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.controller.BookController;
import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
}
