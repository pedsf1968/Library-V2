package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.GameRepository;
import com.pedsf.library.libraryapi.repository.GameSpecification;
import com.pedsf.library.libraryapi.service.GameService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class GameServiceTest {
   private static final String GAME_TITLE_TEST = "The green tomato";
   private static final List<PersonDTO> allPersons = new ArrayList<>();
   private static final List<Game> allGames = new ArrayList<>();
   private static final List<Game> allAllowedGames = new ArrayList<>();

   @Mock
   private static PersonService personService;
   @Mock
   private static GameRepository gameRepository;
   private static GameService gameService;
   private static Game newGame;
   private static GameDTO newGameDTO;

   @BeforeAll
   static void beforeAll() {
      allPersons.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersons.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersons.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersons.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersons.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersons.add( new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersons.add( new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersons.add( new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersons.add( new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersons.add( new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersons.add( new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersons.add( new PersonDTO(12,"Larousse","Larousse",null));
      allPersons.add( new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersons.add( new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersons.add( new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersons.add( new PersonDTO(16,"Microsoft","Microsoft",null));

      allGames.add( new Game("5035223122470","NFS Need for Speed™ Heat",2,-4,15));
      allGames.add( new Game("9879876513246","NFS Need for Speed™ Paybak",2,-2,15));
      allGames.add( new Game("3526579879836","NFS Need for Speed™ No limit",2,-1,15));
      allGames.add( new Game("0805529340299","Flight Simulator 2004 : Un Siècle d'Aviation",1,1,16));
      allGames.add( new Game("0805kuyiuo299","Age of Empire",1,-2,16));

      allAllowedGames.add( new Game("9879876513246","NFS Need for Speed™ Paybak",2,-2,15));
      allAllowedGames.add( new Game("3526579879836","NFS Need for Speed™ No limit",2,-1,15));
      allAllowedGames.add( new Game("0805529340299","Flight Simulator 2004 : Un Siècle d'Aviation",1,1,16));

      for(Game game:allGames) {
         game.setFormat(GameFormat.SONY_PS3);
         game.setType(GameType.SIMULATION);
      }

      for(Game game:allAllowedGames) {
         game.setFormat(GameFormat.SONY_PS3);
         game.setType(GameType.SIMULATION);
      }
   }

   @BeforeEach
   void beforeEach() {
      gameService = new GameService(gameRepository,personService);

      newGame = new Game("954-87sdf797","The green tomato",1,1,16);
      newGame.setFormat(GameFormat.NINTENDO_WII);
      newGame.setType(GameType.ADVENTURE);
      newGame.setHeight(11);
      newGame.setLength(11);
      newGame.setWidth(11);
      newGame.setWeight(220);
      newGame.setPegi("3+");
      newGame.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      newGameDTO = new GameDTO("954-87sdf797","The green tomato",1,1,allPersons.get(15));
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

      Mockito.lenient().when(gameRepository.findAll()).thenReturn(allGames);
      Mockito.lenient().when(gameRepository.findAllAllowed()).thenReturn(allAllowedGames);
      Mockito.lenient().when(gameRepository.findByEan("5035223122470")).thenReturn(java.util.Optional.ofNullable(allGames.get(0)));
      Mockito.lenient().when(gameRepository.findByEan("9879876513246")).thenReturn(java.util.Optional.ofNullable(allGames.get(1)));
      Mockito.lenient().when(gameRepository.findByEan("3526579879836")).thenReturn(java.util.Optional.ofNullable(allGames.get(2)));
      Mockito.lenient().when(gameRepository.findByEan("0805529340299")).thenReturn(java.util.Optional.ofNullable(allGames.get(3)));
      Mockito.lenient().when(gameRepository.findByEan("0805kuyiuo299")).thenReturn(java.util.Optional.ofNullable(allGames.get(4)));
      Mockito.lenient().when(gameRepository.findByEan("954-87sdf797")).thenReturn(java.util.Optional.ofNullable(newGame));

      Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]-1));
   }


   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Game exist")
   void existsById_returnTrue_OfAnExistingGameId() {
      for(Game game : allGames) {
         String ean = game.getEan();
         assertThat(gameService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Game doesn't exist")
   void existsById_returnFalse_OfAnInexistingGameId() {
      assertThat(gameService.existsById("44lk65465")).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Game by is ID")
   void findById_returnGame_ofExistingGameId() {
      GameDTO found;

      for(Game game : allGames) {
         String ean = game.getEan();
         found = gameService.findById(ean);

         assertThat(found).isEqualTo(gameService.entityToDTO(game));
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Game with wrong ID")
   void findById_returnException_ofInexistingGameId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> gameService.findById("klgqsdf"));
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Games")
      void findAll_returnAllGames() {
      List<GameDTO> gameDTOS = gameService.findAll();

      assertThat(gameDTOS.size()).isEqualTo(5);

      for(Game game: allGames) {
         GameDTO gameDTO = gameService.entityToDTO(game);
         assertThat(gameDTOS.contains(gameDTO)).isTrue();
      }
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Games that can be booked")
   void findAllAllowed_returnBookableGames_ofAllGames() {
      List<GameDTO> alloweds = gameService.findAllAllowed();
      assertThat(alloweds.size()).isEqualTo(3);

      for(Game game: allGames) {
         GameDTO gameDTO = gameService.entityToDTO(game);

         if (alloweds.contains(gameDTO)) {
            // allowed
            assertThat(gameDTO.getQuantity()*2).isGreaterThan(gameDTO.getStock());
         } else {
            // not allowed
            assertThat(gameDTO.getQuantity()*2).isEqualTo(-gameDTO.getStock());
         }
      }
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Game by his title and editor")
   void findAllFiltered_returnOnlyOneGame_ofExistingTitleAndEditor() {
      List<GameDTO> found;

      for (Game game:allGames) {
         Game filter = new Game();
         filter.setTitle(game.getTitle());
         filter.setEditorId(game.getEditorId());

         Mockito.lenient().when(gameRepository.findAll(any(GameSpecification.class))).thenReturn(Collections.singletonList(game));

         GameDTO filterDTO = gameService.entityToDTO(filter);
         found = gameService.findAllFiltered(filterDTO);

         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(gameService.entityToDTO(game));
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Game by Editor")
   void getFirstId_returnFirstId_ofFilteredBookByEditor() {
      GameDTO filter = new GameDTO();
      filter.setEditor(personService.findById(16));

      Mockito.lenient().when(gameRepository.findAll(any(GameSpecification.class))).thenReturn(Arrays.asList(allGames.get(0),allGames.get(1),allGames.get(2)));

      String ean = gameService.getFirstId(filter);

      assertThat(ean).isEqualTo("5035223122470");
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Game")
   void save_returnCreatedGame_ofNewGame() {
      Mockito.lenient().when(gameRepository.save(any(Game.class))).thenReturn(newGame);

      GameDTO found = gameService.save(newGameDTO);
      String ean = found.getEan();

      assertThat(gameService.existsById(ean)).isTrue();
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Game")
   void update_returnUpdatedGame_ofGameAndNewTitle() {
      String oldTitle = newGame.getTitle();
      newGame.setTitle(GAME_TITLE_TEST);
      newGameDTO.setTitle(GAME_TITLE_TEST);
      Mockito.lenient().when(gameRepository.save(any(Game.class))).thenReturn(newGame);

      GameDTO gameSaved = gameService.update(newGameDTO);
      assertThat(gameSaved).isEqualTo(newGameDTO);

      newGame.setTitle(oldTitle);
      newGameDTO.setTitle(oldTitle);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Game by his EAN")
   void deleteById_returnExceptionWhenGetGameById_ofDeletedGameById() {
      String ean = newGameDTO.getEan();

      assertThat(gameService.existsById(ean)).isTrue();
      gameService.deleteById(ean);

      Mockito.lenient().when(gameRepository.findByEan(ean)).thenThrow(ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> gameService.findById(ean));

   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Games")
   void count_returnTheNumberOfGames() {
      Mockito.lenient().when(gameRepository.count()).thenReturn(3L);
      assertThat(gameService.count()).isEqualTo(3);
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Game DTO is converted in right Game Entity")
   void dtoToEntity_returnRightGameEntity_ofGameDTO() {
      Game entity = gameService.dtoToEntity(newGameDTO);
      assertThat(entity.getEan()).isEqualTo(newGameDTO.getEan());
      assertThat(entity.getTitle()).isEqualTo(newGameDTO.getTitle());
      assertThat(entity.getQuantity()).isEqualTo(newGameDTO.getQuantity());
      assertThat(entity.getStock()).isEqualTo(newGameDTO.getStock());
      assertThat(entity.getHeight()).isEqualTo(newGameDTO.getHeight());
      assertThat(entity.getLength()).isEqualTo(newGameDTO.getLength());
      assertThat(entity.getWeight()).isEqualTo(newGameDTO.getWeight());
      assertThat(entity.getWidth()).isEqualTo(newGameDTO.getWidth());
      assertThat(entity.getReturnDate()).isEqualTo(newGameDTO.getReturnDate());

      assertThat(entity.getEditorId()).isEqualTo(newGameDTO.getEditor().getId());
      assertThat(entity.getUrl()).isEqualTo(newGameDTO.getUrl());
      assertThat(entity.getPublicationDate()).isEqualTo(newGameDTO.getPublicationDate());
      assertThat(entity.getFormat().name()).isEqualTo(newGameDTO.getFormat());
      assertThat(entity.getType().name()).isEqualTo(newGameDTO.getType());
      assertThat(entity.getPegi()).isEqualTo(newGameDTO.getPegi());
      assertThat(entity.getSummary()).isEqualTo(newGameDTO.getSummary());
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Game Entity is converted in right Game DTO")
   void dtoToEntity_returnRightGameDTO_ofGameEntity() {
      GameDTO dto = gameService.entityToDTO(newGame);
      assertThat(dto.getEan()).isEqualTo(newGame.getEan());
      assertThat(dto.getTitle()).isEqualTo(newGame.getTitle());
      assertThat(dto.getQuantity()).isEqualTo(newGame.getQuantity());
      assertThat(dto.getStock()).isEqualTo(newGame.getStock());
      assertThat(dto.getHeight()).isEqualTo(newGame.getHeight());
      assertThat(dto.getLength()).isEqualTo(newGame.getLength());
      assertThat(dto.getWeight()).isEqualTo(newGame.getWeight());
      assertThat(dto.getWidth()).isEqualTo(newGame.getWidth());
      assertThat(dto.getReturnDate()).isEqualTo(newGame.getReturnDate());

      assertThat(dto.getEditor().getId()).isEqualTo(newGame.getEditorId());
      assertThat(dto.getUrl()).isEqualTo(newGame.getUrl());
      assertThat(dto.getPublicationDate()).isEqualTo(newGame.getPublicationDate());
      assertThat(dto.getFormat()).isEqualTo(newGame.getFormat().name());
      assertThat(dto.getType()).isEqualTo(newGame.getType().name());
      assertThat(dto.getPegi()).isEqualTo(newGame.getPegi());
      assertThat(dto.getSummary()).isEqualTo(newGame.getSummary());
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Games titles")
   void findAllTitles() {
      List<String> titles = Arrays.asList("NFS Need for Speed™ Heat","NFS Need for Speed™ Paybak","NFS Need for Speed™ No limit","Flight Simulator 2004 : Un Siècle d'Aviation","Age of Empire");
      Mockito.lenient().when(gameRepository.findAllTitles()).thenReturn(titles);

      List<String> foundTitles = gameService.findAllTitles();
      assertThat(titles.size()).isEqualTo(5);
      for(String title:foundTitles) {
         assertThat(titles.contains(title)).isTrue();
      }
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Game by his EAN number")
   void increaseStock_returnGameWithIncrementedStock_ofOneGame() {
      String ean = newGame.getEan();
      Integer oldStock = newGame.getStock();
      newGame.setStock(oldStock+1);
      Mockito.lenient().when(gameRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newGame));

      gameService.increaseStock(ean);

      GameDTO gameDTO = gameService.findById(ean);

      assertThat(gameDTO.getStock()).isEqualTo(oldStock+1);

      newGame.setStock(oldStock);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Game by his EAN number")
   void decreaseStock_returnGameWithDecrementedStock_ofOneGame() {
      String ean = newGame.getEan();
      Integer oldStock = newGame.getStock();
      newGame.setStock(oldStock-1);
      Mockito.lenient().when(gameRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newGame));

      gameService.decreaseStock(ean);

      GameDTO gameDTO = gameService.findById(ean);

      assertThat(gameDTO.getStock()).isEqualTo(oldStock-1);

      newGame.setStock(oldStock);
   }
}