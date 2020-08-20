package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.controller.GameController;
import com.pedsf.library.libraryapi.model.Game;
import com.pedsf.library.libraryapi.service.GameService;
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

@WebMvcTest(controllers = {GameController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class GameControllerTest {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private GameService gameService;
   private static ObjectMapper mapper = new ObjectMapper();
   private static Game newGame;
   private static GameDTO newGameDTO;

   @BeforeAll
   static void beforeAll() {
      newGameDTO = new GameDTO("954-87sdf797","The green tomato",1,1,
              new PersonDTO(15,"EA","Electronic Arts", Date.valueOf("1982-05-28")));
      newGameDTO.setFormat("NINTENDO_WII");
      newGameDTO.setType("ADVENTURE");
      newGameDTO.setHeight(11);
      newGameDTO.setLength(11);
      newGameDTO.setWidth(11);
      newGameDTO.setWeight(220);
      newGameDTO.setPegi("3+");
      newGameDTO.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
              "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
              "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
              " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
              "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
              "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
              " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
              " comes from a line in section 1.10.32.");
   }

   @Test
   @Tag("findAllGames")
   @DisplayName("Verify that we get NotFound if there are no Games")
   void findAllGames_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(gameService.findAll()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/games"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }

   @Test
   @Tag("findAllAllowedGames")
   @DisplayName("Verify that we get NotFound if there are no allowed Games")
   void findAllAllowedGames_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(gameService.findAllAllowed()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/games/allowed"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we get NotFound if there are no Games")
   void findAllFiltered_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(gameService.findAllFiltered(newGameDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newGameDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/games/searches")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we get NotFound if with wrong Id")
   void findById_returnNotFound_ofWrongGameId() throws Exception {
      // GIVEN
      when(gameService.findById(anyString())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/games/" + 456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("addGame")
   @DisplayName("Verify that we get Conflict if there are ConflictException when save Game")
   void addGame_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(gameService.save(newGameDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newGameDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/games")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("addGame")
   @DisplayName("Verify that we get BadRequest if there are ConflictException when save Game")
   void addGame_returnBadRequest_ofBadRequestException() throws Exception {
      // GIVEN
      when(gameService.save(newGameDTO)).thenThrow(BadRequestException.class);

      String json = mapper.writeValueAsString(newGameDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.post("/games")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isBadRequest())
              .andReturn();
   }

   @Test
   @Tag("updateGame")
   @DisplayName("Verify that we get NotFound if there are no Game to update")
   void updateGame_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(gameService.update(newGameDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newGameDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/games/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("updateGame")
   @DisplayName("Verify that we get Conflict if there are no Game ConflictException when update")
   void updateGame_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(gameService.update(newGameDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newGameDTO);

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.put("/games/"+456)
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().isConflict())
              .andReturn();
   }

   @Test
   @Tag("deleteGame")
   @DisplayName("Verify that we get NotFound if there are no Game to delete")
   void deleteGame_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(gameService).deleteById(anyString());

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.delete("/games/"+456))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

   @Test
   @Tag("getAllGamesTitles")
   @DisplayName("Verify that we get NotFound if there are no Game Titles")
   void getAllGamesTitles_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(gameService).findAllTitles();

      // WHEN
      mockMvc.perform(
              MockMvcRequestBuilders.get("/games/titles"))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andReturn();
   }

}
