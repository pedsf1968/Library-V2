package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.controller.MusicController;
import com.pedsf.library.libraryapi.service.MusicService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
   private static MusicDTO newMusicDTO;

   @BeforeAll
   static void beforeAll() {
      newMusicDTO = new MusicDTO("9876546546","Le petite musique du matin", 1,1,
              new PersonDTO(14,"BigBang","BigBang", Date.valueOf("2006-08-19")),
              new PersonDTO(14,"BigBang","BigBang", Date.valueOf("2006-08-19")),
              new PersonDTO(14,"BigBang","BigBang", Date.valueOf("2006-08-19")));
      newMusicDTO.setDuration(123);
      newMusicDTO.setFormat("SACD");
      newMusicDTO.setType("ELECTRO");
      newMusicDTO.setUrl("http://www.google.co.kr");
      newMusicDTO.setHeight(11);
      newMusicDTO.setLength(11);
      newMusicDTO.setWidth(11);
      newMusicDTO.setWeight(220);
   }

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

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we get NotFound if there are no Musics")
   void findAllFiltered_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(musicService.findAllFiltered(newMusicDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newMusicDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/musics/searches")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we get NotFound if with wrong Id")
   void findById_returnNotFound_ofWrongMusicId() throws Exception {
      // GIVEN
      when(musicService.findById(anyString())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/musics/" + 456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("addMusic")
   @DisplayName("Verify that we get Conflict if there are ConflictException when save Music")
   void addMusic_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(musicService.save(newMusicDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newMusicDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/musics")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("addMusic")
   @DisplayName("Verify that we get BadRequest if there are ConflictException when save Music")
   void addMusic_returnBadRequest_ofBadRequestException() throws Exception {
      // GIVEN
      when(musicService.save(newMusicDTO)).thenThrow(BadRequestException.class);

      String json = mapper.writeValueAsString(newMusicDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/musics")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isBadRequest())
              .andReturn();
   }

   @Test
   @Tag("updateMusic")
   @DisplayName("Verify that we get NotFound if there are no Music to update")
   void updateMusic_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(musicService.update(newMusicDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newMusicDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/musics/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("updateMusic")
   @DisplayName("Verify that we get Conflict if there are no Music ConflictException when update")
   void updateMusic_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(musicService.update(newMusicDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newMusicDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/musics/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("deleteMusic")
   @DisplayName("Verify that we get NotFound if there are no Music to delete")
   void deleteMusic_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(musicService).deleteById(anyString());

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/musics/"+456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllMusicsAuthors")
   @DisplayName("Verify that we get NotFound if there are no Music Author")
   void getAllMusicsAuthors_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(musicService).findAllAuthors();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/musics/authors"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllMusicsInterpreters")
   @DisplayName("Verify that we get NotFound if there are no Music Interpreter")
   void getAllMusicsInterpreters_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(musicService).findAllInterpreters();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/musics/interpreters"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllMusicsComposers")
   @DisplayName("Verify that we get NotFound if there are no Music Composer")
   void getAllMusicsComposers_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(musicService).findAllComposers();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/musics/composers"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllMusicsTitles")
   @DisplayName("Verify that we get NotFound if there are no Music Titles")
   void getAllMusicsTitles_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(musicService).findAllTitles();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/musics/titles"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

}
