package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.GameRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.service.GameService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GameServiceTestIT {
   private static final String GAME_EAN_TEST = "0805529340299";
   private static final String GAME_TITLE_TEST = "The green tomato";

   private static GameService gameService;
   private static Game newGame;
   private static GameDTO newGameDTO = new GameDTO();
   private static List<GameDTO> allGameDTOS;


   @BeforeAll
   static void beforeAll(@Autowired GameRepository gameRepository, @Autowired PersonRepository personRepository) {

      PersonService personService = new PersonService(personRepository);
      gameService =  new GameService(gameRepository,personService);
   }

   @BeforeEach
   void beforeEach() {
      newGame = new Game();

      newGame.setTitle("The green tomato");
      newGame.setEditorId(3);
      newGame.setEan("954-87sdf797");
      newGame.setFormat(GameFormat.NINTENDO_WII);
      newGame.setType(GameType.ADVENTURE);
      newGame.setHeight(11);
      newGame.setLength(11);
      newGame.setWidth(11);
      newGame.setWeight(220);
      newGame.setStock(1);
      newGame.setQuantity(1);
      newGame.setPegi("3+");
      newGame.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      newGameDTO = gameService.entityToDTO(newGame);
      allGameDTOS = gameService.findAll();
   }


   @Test
   @DisplayName("Verify that return TRUE if the Game exist")
   void existsById_returnTrue_OfAnExistingGameId() {
      for(GameDTO gameDTO : allGameDTOS) {
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
      GameDTO found;

      for(GameDTO gameDTO : allGameDTOS) {
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
      GameDTO newGameDTO = gameService.entityToDTO(newGame);
      assertThat(allGameDTOS.size()).isEqualTo(2);

      // add new Game to increase the list
      newGameDTO = gameService.save(newGameDTO);
      List<GameDTO> gameDTOS = gameService.findAll();
      assertThat(gameDTOS.size()).isEqualTo(3);
      assertThat(gameDTOS.contains(newGameDTO)).isTrue();

      gameService.deleteById(newGameDTO.getEan());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Games that can be booked")
   void findAllAllowed_returnBookableGames_ofAllGames() {
      newGame.setStock(-2);
      GameDTO newGameDTO = gameService.entityToDTO(newGame);
      newGameDTO = gameService.save(newGameDTO);
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

      newGame.setStock(1);
      gameService.deleteById(newGameDTO.getEan());

   }


   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Game by his title and editor")
   void findAllFiltered_returnOnlyOneGame_ofExistingTitleAndEditor() {
      List<GameDTO> found;
      for(GameDTO g:allGameDTOS) {
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
      GameDTO newGameDTO = gameService.entityToDTO(newGame);

      newGameDTO = gameService.save(newGameDTO);
      String ean = newGameDTO.getEan();

      assertThat(gameService.existsById(ean)).isTrue();
      gameService.deleteById(ean);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Game")
   void update_returnUpdatedGame_ofGameAndNewTitle() {
      GameDTO gameDTO = gameService.findById(GAME_EAN_TEST);
      String oldTitle = gameDTO.getTitle();
      gameDTO.setTitle(GAME_TITLE_TEST);

      GameDTO gameSaved = gameService.update(gameDTO);
      assertThat(gameSaved).isEqualTo(gameDTO);
      GameDTO gameFound = gameService.findById(gameDTO.getEan());
      assertThat(gameFound).isEqualTo(gameDTO);

      gameDTO.setTitle(oldTitle);
      gameService.update(gameDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Game by his EAN")
   void deleteById_returnExceptionWhenGetGameById_ofDeletedGameById() {
      GameDTO newGameDTO = gameService.entityToDTO(newGame);

      newGameDTO = gameService.save(newGameDTO);
      String ean = newGameDTO.getEan();

      assertThat(gameService.existsById(ean)).isTrue();
      gameService.deleteById(ean);

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
      Game entity;

      for (GameDTO dto: allGameDTOS) {
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
      List<Game> games = new ArrayList<>();
      GameDTO dto;

      for (GameDTO g: allGameDTOS) {
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
      GameDTO newGameDTO = gameService.entityToDTO(newGame);
      List<String> titles = gameService.findAllTitles();

      assertThat(titles.size()).isEqualTo(2);

      // add new Game
      newGameDTO.setTitle(GAME_TITLE_TEST);
      newGameDTO = gameService.save(newGameDTO);
      titles = gameService.findAllTitles();
      assertThat(titles.size()).isEqualTo(3);
      assertThat(titles.contains(GAME_TITLE_TEST)).isTrue();

      gameService.deleteById(newGameDTO.getEan());
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