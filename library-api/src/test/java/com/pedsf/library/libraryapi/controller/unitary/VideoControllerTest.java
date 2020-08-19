package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.controller.BookController;
import com.pedsf.library.libraryapi.controller.VideoController;
import com.pedsf.library.libraryapi.service.BookService;
import com.pedsf.library.libraryapi.service.VideoService;
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

@WebMvcTest(controllers = {VideoController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class VideoControllerTest {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private VideoService videoService;
   private static ObjectMapper mapper = new ObjectMapper();

   @Test
   @Tag("findAllVideos")
   @DisplayName("Verify that we get NotFound if there are no Videos")
   void findAllVideos_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(videoService.findAll()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/videos"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }

   @Test
   @Tag("findAllAllowedVideos")
   @DisplayName("Verify that we get NotFound if there are no allowed Videos")
   void findAllAllowedVideos_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(videoService.findAllAllowed()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/videos/allowed"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }
}
