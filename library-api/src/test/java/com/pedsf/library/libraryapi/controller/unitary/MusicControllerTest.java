package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.controller.BookController;
import com.pedsf.library.libraryapi.controller.MusicController;
import com.pedsf.library.libraryapi.service.BookService;
import com.pedsf.library.libraryapi.service.MusicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {MusicController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class MusicControllerTest {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private MusicService musicService;
   private static ObjectMapper mapper = new ObjectMapper();

   @Test
   @Tag("findAllMusics")
   @DisplayName("Verify that we get NotFound if there are no Musics")
   void findAllMusics_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(musicService.findAll()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/musics"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }

   @Test
   @Tag("findAllAllowedMusics")
   @DisplayName("Verify that we get NotFound if there are no allowed Musics")
   void findAllAllowedMusics_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(musicService.findAllAllowed()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/musics/allowed"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }
}
