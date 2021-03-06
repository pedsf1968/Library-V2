package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.GameFormat;
import com.pedsf.library.dto.GameType;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.controller.GameController;
import com.pedsf.library.libraryapi.service.GameService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {GameController.class})
@ExtendWith(SpringExtension.class)
class GameControllerTestIT {
   private static final String GAME_TITLE_TEST = "Le Mans";

   private static final List<GameDTO> allGameDTOS = new ArrayList<>();
   private static final List<PersonDTO> allPersonDTOS = new ArrayList<>();
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private GameService gameService;

   @BeforeAll
   static void beforeAll() {
      allPersonDTOS.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersonDTOS.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersonDTOS.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersonDTOS.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersonDTOS.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersonDTOS.add( new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersonDTOS.add( new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersonDTOS.add( new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersonDTOS.add( new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersonDTOS.add( new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersonDTOS.add( new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersonDTOS.add( new PersonDTO(12,"Larousse","Larousse",null));
      allPersonDTOS.add( new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersonDTOS.add( new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersonDTOS.add( new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersonDTOS.add( new PersonDTO(16,"Microsoft","Microsoft",null));

      allGameDTOS.add( new GameDTO("5035223122470","NFS Need for Speed™ Heat",2,-4,allPersonDTOS.get(14)));
      allGameDTOS.add( new GameDTO("9879876513246","NFS Need for Speed™ Paybak",2,-2,allPersonDTOS.get(14)));
      allGameDTOS.add( new GameDTO("3526579879836","NFS Need for Speed™ No limit",2,-1,allPersonDTOS.get(14)));
      allGameDTOS.add( new GameDTO("0805529340299","Flight Simulator 2004 : Un Siècle d'Aviation",1,1,allPersonDTOS.get(15)));
      allGameDTOS.add( new GameDTO("0805kuyiuo299","Age of Empire",1,-2,allPersonDTOS.get(15)));

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @Test
   @Tag("findAllGames")
   @DisplayName("Verify that we get the list of all Games")
   void findAllGames_returnAllGames() throws Exception {
      // GIVEN
      when(gameService.findAll()).thenReturn(allGameDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/games"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in MusicDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<GameDTO> founds = Arrays.asList(mapper.readValue(json, GameDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(5);
      for(GameDTO dto: founds) {
         for(GameDTO expected:allGameDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllAllowedGames")
   @DisplayName("Verify that we get the right list of allowed Games")
   void findAllAllowedGames()  throws Exception {
      int i = 1;
      // GIVEN
      when(gameService.findAllAllowed()).thenReturn(allGameDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/games/allowed"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<GameDTO> founds = Arrays.asList(mapper.readValue(json, GameDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(5);
      for (GameDTO dto : founds) {
         for (GameDTO expected : allGameDTOS) {
            if (dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllFilteredGames")
   @DisplayName("Verify that we get Game list from first editor")
   void findAllFilteredBooks() throws Exception {
      List<GameDTO> filtered = allGameDTOS.subList(0,3);
      GameDTO filter = new GameDTO();

      // GIVEN
      filter.setEditor(filtered.get(0).getEditor());
      when(gameService.findAllFiltered(any(GameDTO.class))).thenReturn(filtered);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/games/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<GameDTO> founds = Arrays.asList(mapper.readValue(json, GameDTO[].class));

      assertThat(founds.size()).isEqualTo(filtered.size());
      for(GameDTO dto: founds) {
         for(GameDTO expected:filtered) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findGameById")
   @DisplayName("Verify that we can get Game by his EAN")
   void findGameById()  throws Exception {
      GameDTO expected = allGameDTOS.get(3);
      String ean = expected.getEan();

      // GIVEN
      when(gameService.findById(ean)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/games/"+ean))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      GameDTO found = mapper.readValue(json, GameDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("addGame")
   @DisplayName("Verify that we can add Game")
   void addGame() throws Exception {
      GameDTO expected = allGameDTOS.get(4);
      expected.setFormat(GameFormat.MICROSOFT_XBOX.name());
      expected.setType(GameType.ADVENTURE.name());

      // GIVEN
      when(gameService.save(any(GameDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.post("/games")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      GameDTO found = mapper.readValue(json, GameDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("updateGame")
   @DisplayName("Verify that we can update a Game")
   void updateGame() throws Exception {
      GameDTO expected = allGameDTOS.get(2);
      expected.setTitle(GAME_TITLE_TEST);
      expected.setFormat(GameFormat.MICROSOFT_XBOX.name());
      expected.setType(GameType.ADVENTURE.name());

      // GIVEN
      when(gameService.update(any(GameDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.put("/games/"+ expected.getEan())
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      GameDTO found = mapper.readValue(json, GameDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("deleteGame")
   @DisplayName("Verify that we can delate a Game")
   void deleteGame() throws Exception {
      GameDTO expected = allGameDTOS.get(2);

      // GIVEN
      doNothing().when(gameService).deleteById(expected.getEan());

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.delete("/games/"+ expected.getEan())
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();
   }

   @Test
   @Tag("getAllBooksEditors")
   @DisplayName("Verify that we get all editors list")
   void getAllBooksEditors() throws Exception {
      List<PersonDTO> editors = new ArrayList<>();
      for(GameDTO gameDTO : allGameDTOS) {
         editors.add(gameDTO.getEditor());
      }

      // GIVEN
      when(gameService.findAllEditors()).thenReturn(editors);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/games/editors"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<PersonDTO> founds = Arrays.asList(mapper.readValue(json, PersonDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(editors.size());
      for(PersonDTO dto: founds) {
         for(PersonDTO expected:editors) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("getAllGamesTitles")
   @DisplayName("Verify that we get all titles list")
   void getAllGamesTitles() throws Exception {
      List<String> titles = new ArrayList<>();
      for(GameDTO gameDTO : allGameDTOS) {
         titles.add(gameDTO.getTitle());
      }

      // GIVEN
      when(gameService.findAllTitles()).thenReturn(titles);

      // WHEN
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.get("/games/titles"))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<String> founds = Arrays.asList(mapper.readValue(json, String[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(titles.size());
      for(String title: founds) {
         assertThat(titles.contains(title)).isTrue();
      }
   }
}

