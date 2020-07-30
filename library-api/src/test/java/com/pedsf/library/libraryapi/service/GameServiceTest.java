package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.libraryapi.model.Book;
import com.pedsf.library.libraryapi.model.Game;
import com.pedsf.library.libraryapi.repository.GameRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GameServiceTest {
   private static final String GAME_EAN_TEST = "0805529340299";

   @TestConfiguration
   static class gameServiceTestConfiguration {
      @Autowired
      private GameRepository gameRepository;
      @Autowired
      private PersonRepository personRepository;


      @Bean
      public GameService gameService() {
         PersonService personService = new PersonService(personRepository);

         return new GameService(gameRepository,personService);
      }
   }

   @Autowired
   private GameService gameService;

   @Test
   @DisplayName("Verify that return TRUE if the Game exist")
   void existsById_returnTrue_OfAnExistingGameId() {
      List<GameDTO> gameDTOS = gameService.findAll();

      for(GameDTO gameDTO : gameDTOS) {
         String ean = gameDTO.getEan();
         assertThat(gameService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Game doesn't exist")
   void existsById_returnFalse_OfAnInexistingGameId() {
      assertThat(gameService.existsById("5lkjh5")).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Game by is ID")
   void findById_returnUser_ofExistingGameId() {
      List<GameDTO> gameDTOS = gameService.findAll();
      GameDTO found;

      for(GameDTO gameDTO : gameDTOS) {
         String ean = gameDTO.getEan();
         found = gameService.findById(ean);

         assertThat(found).isEqualTo(gameDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Game with wrong ID")
   void findById_returnException_ofInexistingGameId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         GameDTO found = gameService.findById("klgqsdf");
      });
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Games")
   void findAll_returnAllGamesUser() {
      List<GameDTO> gameDTOS = gameService.findAll();
      assertThat(gameDTOS.size()).isEqualTo(2);
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Games that can be booked")
   void findAllAllowed_returnBookableGames_ofAllGames() {
      List<GameDTO> gameDTOS = gameService.findAll();
      List<GameDTO> alloweds = gameService.findAllAllowed();

      for(GameDTO gameDTO: gameDTOS) {
         if (alloweds.contains(gameDTO)) {
            // allowed
            assertThat(gameDTO.getStock()).isGreaterThan(-gameDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(gameDTO.getStock()).isLessThanOrEqualTo(-gameDTO.getQuantity()*2);
         }
      }
   }


   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Game by his title and editor")
   void findAllFiltered_returnOnlyOneGame_ofExistingTitleAndEditor() {
      List<GameDTO> gameDTOS = gameService.findAll();
      List<GameDTO> found;
      for(GameDTO g:gameDTOS) {
         GameDTO filter = new GameDTO();
         filter.setTitle(g.getTitle());
         filter.setEditor(g.getEditor());

         found = gameService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(g);
      }
   }

   @Test
   void getFirstId() {
   }

   @Test
   @DisplayName("Verify that we can create a new Game")
   void save_returnCreatedGame_ofNewGame() {
      GameDTO gameDTO = gameService.findById(GAME_EAN_TEST);
      String newEan = "newGameEAN";
      String newTitle = "NewGameTitle";

      gameDTO.setEan(newEan);
      gameDTO.setTitle(newTitle);
      gameDTO.setReturnDate(null);
      gameDTO.setPublicationDate(null);
      gameDTO = gameService.save(gameDTO);
      String ean = gameDTO.getEan();

      assertThat(gameService.existsById(ean)).isTrue();
      gameService.deleteById(ean);
   }

   @Test
   void update() {
   }

   @Test
   void deleteById() {
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Games")
   void count_returnTheNumberOfGames() {
      assertThat(gameService.count()).isEqualTo(2);
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Game DTO is converted in right Game Entity")
   void dtoToEntity_returnRightGameEntity_ofGameDTO() {
      List<GameDTO> gameDTOS = gameService.findAll();
      Game entity;

      for (GameDTO dto: gameDTOS) {
         entity = gameService.dtoToEntity(dto);
         assertThat(entity.getEan()).isEqualTo(dto.getEan());
         assertThat(entity.getTitle()).isEqualTo(dto.getTitle());
         assertThat(entity.getQuantity()).isEqualTo(dto.getQuantity());
         assertThat(entity.getStock()).isEqualTo(dto.getStock());
         assertThat(entity.getHeight()).isEqualTo(dto.getHeight());
         assertThat(entity.getLength()).isEqualTo(dto.getLength());
         assertThat(entity.getWeight()).isEqualTo(dto.getWeight());
         assertThat(entity.getWidth()).isEqualTo(dto.getWidth());
         assertThat(entity.getReturnDate()).isEqualTo(dto.getReturnDate());

         assertThat(entity.getEditorId()).isEqualTo(dto.getEditor().getId());
         assertThat(entity.getUrl()).isEqualTo(dto.getUrl());
         assertThat(entity.getPublicationDate()).isEqualTo(dto.getPublicationDate());
         assertThat(entity.getFormat().name()).isEqualTo(dto.getFormat());
         assertThat(entity.getType().name()).isEqualTo(dto.getType());
         assertThat(entity.getPegi()).isEqualTo(dto.getPegi());
         assertThat(entity.getSummary()).isEqualTo(dto.getSummary());
      }
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Game Entity is converted in right Game DTO")
   void dtoToEntity_returnRightGameDTO_ofGameEntity() {
      List<GameDTO> gameDTOS = gameService.findAll();
      List<Game> games = new ArrayList<>();
      GameDTO dto;

      for (GameDTO g: gameDTOS) {
         games.add(gameService.dtoToEntity(g));
      }

      for (Game entity: games) {
         dto = gameService.entityToDTO(entity);
         assertThat(dto.getEan()).isEqualTo(entity.getEan());
         assertThat(dto.getTitle()).isEqualTo(entity.getTitle());
         assertThat(dto.getQuantity()).isEqualTo(entity.getQuantity());
         assertThat(dto.getStock()).isEqualTo(entity.getStock());
         assertThat(dto.getHeight()).isEqualTo(entity.getHeight());
         assertThat(dto.getLength()).isEqualTo(entity.getLength());
         assertThat(dto.getWeight()).isEqualTo(entity.getWeight());
         assertThat(dto.getWidth()).isEqualTo(entity.getWidth());
         assertThat(dto.getReturnDate()).isEqualTo(entity.getReturnDate());

         assertThat(dto.getEditor().getId()).isEqualTo(entity.getEditorId());
         assertThat(dto.getUrl()).isEqualTo(entity.getUrl());
         assertThat(dto.getPublicationDate()).isEqualTo(entity.getPublicationDate());
         assertThat(dto.getFormat()).isEqualTo(entity.getFormat().name());
         assertThat(dto.getType()).isEqualTo(entity.getType().name());
         assertThat(dto.getPegi()).isEqualTo(entity.getPegi());
         assertThat(dto.getSummary()).isEqualTo(entity.getSummary());
      }
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Games titles")
   void findAllTitles() {
      List<String> titles = gameService.findAllTitles();
      assertThat(titles.size()).isEqualTo(2);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Game by his EAN number")
   void increaseStock_returnGameWithIncrementedStock_ofOneGame() {
      GameDTO gameDTO = gameService.findById(GAME_EAN_TEST);
      Integer oldStock = gameDTO.getStock();
      gameService.increaseStock(GAME_EAN_TEST);
      gameDTO = gameService.findById(GAME_EAN_TEST);
      assertThat(gameDTO.getStock()).isEqualTo(oldStock+1);
      gameDTO.setStock(oldStock);
      gameService.update(gameDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Game by his EAN number")
   void decreaseStock_returnGameWithDecrementedStock_ofOneGame() {
      GameDTO gameDTO = gameService.findById(GAME_EAN_TEST);
      Integer oldStock = gameDTO.getStock();
      gameService.decreaseStock(GAME_EAN_TEST);
      gameDTO = gameService.findById(GAME_EAN_TEST);
      assertThat(gameDTO.getStock()).isEqualTo(oldStock-1);
      gameDTO.setStock(oldStock);
      gameService.update(gameDTO);
   }
}