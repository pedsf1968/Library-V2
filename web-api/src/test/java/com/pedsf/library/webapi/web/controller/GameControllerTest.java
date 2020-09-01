package com.pedsf.library.webapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.GameFormat;
import com.pedsf.library.dto.GameType;
import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.filter.GameFilter;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.webapi.proxy.LibraryApiProxy;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.web.PathTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import(GameController.class)
@WebMvcTest(controllers = {GameController.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class GameControllerTest {
   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   private static final List<GameDTO> allGameDTOS = new ArrayList<>();
   private static final List<String> gamesTitles = new ArrayList<>();
   private static final List<PersonDTO> gamesEditors = new ArrayList<>();
   private static UserDTO   newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;
   @MockBean
   private LibraryApiProxy libraryApiProxy;
   @MockBean
   private UserApiProxy userApiProxy;

   private GameController gameController;

   @Configuration
   @EnableWebSecurity
   static class SecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception
      {
         http
               .authorizeRequests()
               .antMatchers("/**").permitAll()
               .anyRequest().anonymous().and()
               .csrf().disable();
      }
   }

   @BeforeAll
   static void beforeAll() {
      gamesEditors.add( new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      gamesEditors.add( new PersonDTO(16,"Microsoft","Microsoft",null));

      allGameDTOS.add( new GameDTO("5035223122470","NFS Need for Speed™ Heat",2,-4,gamesEditors.get(0)));
      allGameDTOS.add( new GameDTO("9879876513246","NFS Need for Speed™ Paybak",2,-2,gamesEditors.get(0)));
      allGameDTOS.add( new GameDTO("3526579879836","NFS Need for Speed™ No limit",2,-1,gamesEditors.get(0)));
      allGameDTOS.add( new GameDTO("0805529340299","Flight Simulator 2004 : Un Siècle d'Aviation",1,1,gamesEditors.get(1)));
      allGameDTOS.add( new GameDTO("0805kuyiuo299","Age of Empire",1,-2,gamesEditors.get(1)));

      for(GameDTO gameDTO:allGameDTOS) {
         gameDTO.setFormat(GameFormat.SONY_PS3.name());
         gameDTO.setType(GameType.SIMULATION.name());
      }

      for(GameDTO b : allGameDTOS) {
         gamesTitles.add(b.getTitle());
      }

      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      when(libraryApiProxy.findAllAllowedGames(anyInt())).thenReturn(allGameDTOS);
      when(libraryApiProxy.getAllGamesTitles()).thenReturn(gamesTitles);
      when(libraryApiProxy.getAllGamesEditors()).thenReturn(gamesEditors);
      gameController = new GameController(libraryApiProxy,userApiProxy);
      gameController.setBorrowingQuantityMax(borrowingQuantityMax);
      mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
   }

   @Test
   @Tag("gamesList")
   @DisplayName("Verify that the controller send all Game list")
   void gamesList_returnGameListAndLinkedList_ofGameFilter() throws Exception {
      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/games"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.GAME_ALL))
            .andReturn();

      // THEN
      List<GameDTO> foundGames = (List<GameDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_GAMES);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundEditors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_EDITORS);
      GameType[] foundTypes = (GameType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      GameFormat[] foundFormats = (GameFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundGames).hasSameSizeAs(allGameDTOS);
      assertThat(foundTitles).hasSameSizeAs(gamesTitles);
      assertThat(foundEditors).hasSameSizeAs(gamesEditors);
      assertThat(foundTypes).hasSameSizeAs(GameType.values());
      assertThat(foundFormats).hasSameSizeAs(GameFormat.values());

      assertThat(foundGames).isEqualTo(allGameDTOS);
      assertThat(foundTitles).isEqualTo(gamesTitles);

      for(int i=0; i<gamesEditors.size(); i++) {
         assertThat(foundEditors).containsEntry(i+15,gamesEditors.get(i));
      }

      assertThat(foundTypes).isEqualTo(GameType.values());
      assertThat(foundFormats).isEqualTo(GameFormat.values());
   }

   @Test
   @Tag("gamesFilteredList")
   @DisplayName("Verify that the controller send filtered Game list")
   void gamesFilteredList_returnFilteredGameListAndLinkedList_ofGameFilter()throws Exception {
      GameFilter filter = new GameFilter();

      // GIVEN
      when(libraryApiProxy.findAllFilteredGames(anyInt() , any(GameDTO.class))).thenReturn(allGameDTOS);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/games")
            .flashAttr("filter",filter))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.GAME_ALL))
            .andReturn();

      // THEN
      List<GameDTO> foundGames = (List<GameDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_GAMES);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundEditors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_EDITORS);
      GameType[] foundTypes = (GameType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      GameFormat[] foundFormats = (GameFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundGames).hasSameSizeAs(allGameDTOS);
      assertThat(foundTitles).hasSameSizeAs(gamesTitles);
      assertThat(foundEditors).hasSameSizeAs(gamesEditors);
      assertThat(foundTypes).hasSameSizeAs(GameType.values());
      assertThat(foundFormats).hasSameSizeAs(GameFormat.values());

      assertThat(foundGames).isEqualTo(allGameDTOS);
      assertThat(foundTitles).isEqualTo(gamesTitles);

      for(int i=0; i<gamesEditors.size(); i++) {
         assertThat(foundEditors).containsEntry(i+15,gamesEditors.get(i));
      }

      assertThat(foundTypes).isEqualTo(GameType.values());
      assertThat(foundFormats).isEqualTo(GameFormat.values());

   }

   @Test
   @Tag("gameView")
   @DisplayName("Verify that the controller send the Game expected")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void gameView_returnGame_ofOneGameId() throws Exception {
      GameDTO expected = allGameDTOS.get(3);
      String gameEAN = expected.getEan();

      // GIVEN
      when(libraryApiProxy.findGameById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/game/" + gameEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.GAME_READ))
            .andReturn();

      // THEN
      GameDTO found = (GameDTO) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_GAME);
      Boolean canBorrow = (Boolean) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(found).isEqualTo(expected);
      assertThat(canBorrow).isTrue();
   }

   @Test
   @Tag("gameView")
   @DisplayName("Verify that the user can't borrow more Game if counter equal max quentity")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void gameView_returnCanBorrowIsFalse_ofUserWithMaxCounter() throws Exception {
      GameDTO expected = allGameDTOS.get(3);
      String gameEAN = expected.getEan();

      newUserDTO.setCounter(borrowingQuantityMax);
      // GIVEN
      when(libraryApiProxy.findGameById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/game/" + gameEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.GAME_READ))
            .andReturn();

      // THEN
      Boolean canBorrow = (Boolean) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(canBorrow).isFalse();
   }

}