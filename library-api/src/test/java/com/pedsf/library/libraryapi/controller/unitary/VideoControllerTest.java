package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.controller.VideoController;
import com.pedsf.library.libraryapi.service.VideoService;
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
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
   private static VideoDTO newVideoDTO;

   @BeforeAll
   static void beforeAll() {
      newVideoDTO = new VideoDTO("sdfsdfds","Video of the Day", 1,1,
              new PersonDTO(6,"Kang-Ho","SONG", Date.valueOf("1967-01-17")));
      newVideoDTO.setDuration(123);
      newVideoDTO.setFormat(VideoFormat.DVD.name());
      newVideoDTO.setType(VideoType.DOCUMENT.name());
      newVideoDTO.setUrl("http://www.google.co.kr");
      newVideoDTO.setHeight(11);
      newVideoDTO.setLength(11);
      newVideoDTO.setWidth(11);
      newVideoDTO.setWeight(220);
      newVideoDTO.setAudience("Tout public");
      newVideoDTO.setDuration(310);
      newVideoDTO.setAudio("5.1");
      newVideoDTO.setActors(new ArrayList<>());   
   }
   
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

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we get NotFound if there are no Videos")
   void findAllFiltered_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(videoService.findAllFiltered(newVideoDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newVideoDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/videos/searches")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we get NotFound if with wrong Id")
   void findById_returnNotFound_ofWrongVideoId() throws Exception {
      // GIVEN
      when(videoService.findById(anyString())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/videos/" + 456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("addVideo")
   @DisplayName("Verify that we get Conflict if there are ConflictException when save Video")
   void addVideo_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(videoService.save(newVideoDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newVideoDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/videos")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("addVideo")
   @DisplayName("Verify that we get BadRequest if there are ConflictException when save Video")
   void addVideo_returnBadRequest_ofBadRequestException() throws Exception {
      // GIVEN
      when(videoService.save(newVideoDTO)).thenThrow(BadRequestException.class);

      String json = mapper.writeValueAsString(newVideoDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/videos")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isBadRequest())
              .andReturn();
   }

   @Test
   @Tag("updateVideo")
   @DisplayName("Verify that we get NotFound if there are no Video to update")
   void updateVideo_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(videoService.update(newVideoDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newVideoDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/videos/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("updateVideo")
   @DisplayName("Verify that we get Conflict if there are no Video ConflictException when update")
   void updateVideo_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(videoService.update(newVideoDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newVideoDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/videos/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("deleteVideo")
   @DisplayName("Verify that we get NotFound if there are no Video to delete")
   void deleteVideo_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(videoService).deleteById(anyString());

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/videos/"+456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllVideosDirectors")
   @DisplayName("Verify that we get NotFound if there are no Video Directors")
   void getAllVideosDirectors_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(videoService).findAllDirectors();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/videos/directors"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllVideosActors")
   @DisplayName("Verify that we get NotFound if there are no Video Actors")
   void getAllVideosActors_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(videoService).findAllActors();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/videos/actors"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllVideosTitles")
   @DisplayName("Verify that we get NotFound if there are no Video Titles")
   void getAllVideosTitles_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(videoService).findAllTitles();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/videos/titles"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

}
