package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.GameDTO;
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
   void findAllAllowed() {
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
      String newEan = "newEAN";
      String newTitle = "NewTitle";

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
   void entityToDTO() {
   }

   @Test
   void dtoToEntity() {
   }

   @Test
   void findAllTitles() {
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