package com.pedsf.library.webapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.filter.MusicFilter;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import(BookController.class)
@WebMvcTest(controllers = {MusicController.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class MusicControllerTest {
   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   private static final List<MusicDTO> allMusicDTOS = new ArrayList<>();
   private static final Map<Integer, PersonDTO> allPersonDTOS = new HashMap<>();
   private static final List<String> musicsTitles = new ArrayList<>();
   private static final List<PersonDTO> musicsAuthors = new ArrayList<>();
   private static final List<PersonDTO> musicComposers = new ArrayList<>();
   private static final List<PersonDTO> musicInterpreters = new ArrayList<>();
   private static UserDTO newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;
   @MockBean
   private LibraryApiProxy libraryApiProxy;
   @MockBean
   private UserApiProxy userApiProxy;

   private MusicController musicController;

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
      musicsAuthors.add(new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      musicsAuthors.add(new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      musicsAuthors.add(new PersonDTO(15,"SNSD","SNSD",Date.valueOf("2002-07-04")));

      musicComposers.add(new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      musicComposers.add(new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      musicComposers.add(new PersonDTO(15,"SNSD","SNSD",Date.valueOf("2002-07-04")));

      musicInterpreters.add(new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      musicInterpreters.add(new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      musicInterpreters.add(new PersonDTO(15,"SNSD","SNSD",Date.valueOf("2002-07-04")));

      allMusicDTOS.add(new MusicDTO("8809634380036","Kill This Love",2,2,musicsAuthors.get(0),musicComposers.get(0),musicInterpreters.get(0)));
      allMusicDTOS.add(new MusicDTO("4988064587100","DDU-DU DDU-DU",3,-6,musicsAuthors.get(0),musicComposers.get(0),musicInterpreters.get(0)));
      allMusicDTOS.add(new MusicDTO("4988064585816","RE BLACKPINK",1,1,musicsAuthors.get(0),musicComposers.get(0),musicInterpreters.get(0)));
      allMusicDTOS.add(new MusicDTO("8809269506764","MADE",1,-2, musicsAuthors.get(1),musicComposers.get(1),musicInterpreters.get(1)));
      allMusicDTOS.add(new MusicDTO("8809265654654","Remember",1,0, musicsAuthors.get(1),musicComposers.get(1),musicInterpreters.get(1)));
      allMusicDTOS.add(new MusicDTO("6546897989654","Run Devil Run",1,0, musicsAuthors.get(2),musicComposers.get(2),musicInterpreters.get(2)));
      allMusicDTOS.add(new MusicDTO("6546897989654","The Boys",2,-4, musicsAuthors.get(2),musicComposers.get(2),musicInterpreters.get(2)));
      allMusicDTOS.add(new MusicDTO("9879879865464","Lion Heart",1,0, musicsAuthors.get(2),musicComposers.get(2),musicInterpreters.get(2)));

      for(MusicDTO b : allMusicDTOS) {
         musicsTitles.add(b.getTitle());
      }

      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      when(libraryApiProxy.findAllAllowedMusics(anyInt())).thenReturn(allMusicDTOS);
      when(libraryApiProxy.getAllMusicsAuthors()).thenReturn(musicsAuthors);
      when(libraryApiProxy.getAllMusicsComposers()).thenReturn(musicComposers);
      when(libraryApiProxy.getAllMusicsInterpreters()).thenReturn(musicInterpreters);
      when(libraryApiProxy.getAllMusicsTitles()).thenReturn(musicsTitles);
      musicController = new MusicController(libraryApiProxy,userApiProxy);
      musicController.setBorrowingQuantityMax(borrowingQuantityMax);
      mockMvc = MockMvcBuilders.standaloneSetup(musicController).build();
   }

   @Test
   @Tag("musicsList")
   @DisplayName("Verify that the controller send all Music list")
   void musicsList_returnMusicListAndLinkedList_ofMusicFilter() throws Exception {
      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/musics"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.MUSIC_ALL))
            .andReturn();

      // THEN
      List<MusicDTO> foundMusics = (List<MusicDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_MUSICS);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundAuthors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_AUTHORS);
      Map<Integer,PersonDTO> foundComposer = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_COMPOSERS);
      Map<Integer,PersonDTO> foundIntepreters = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_INTERPRETERS);
      MusicType[] foundTypes = (MusicType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      MusicFormat[] foundFormats = (MusicFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundMusics).hasSameSizeAs(allMusicDTOS);
      assertThat(foundTitles).hasSameSizeAs(musicsTitles);
      assertThat(foundAuthors).hasSameSizeAs(musicsAuthors);
      assertThat(foundComposer).hasSameSizeAs(musicComposers);
      assertThat(foundIntepreters).hasSameSizeAs(musicInterpreters);
      assertThat(foundTypes).hasSameSizeAs(MusicType.values());
      assertThat(foundFormats).hasSameSizeAs(MusicFormat.values());

      assertThat(foundMusics).isEqualTo(allMusicDTOS);
      assertThat(foundTitles).isEqualTo(musicsTitles);

      for(Integer i=0; i<musicsAuthors.size(); i++) {
         assertThat(foundAuthors).containsEntry(i+13,musicsAuthors.get(i));
      }

      for(int i=0; i<musicComposers.size(); i++) {
         assertThat(foundComposer).containsEntry(i+13,musicComposers.get(i));
      }

      for(int i=0; i<musicInterpreters.size(); i++) {
         assertThat(foundIntepreters).containsEntry(i+13,musicInterpreters.get(i));
      }

      assertThat(foundTypes).isEqualTo(MusicType.values());
      assertThat(foundFormats).isEqualTo(MusicFormat.values());
   }

   @Test
   @Tag("musicsFilteredList")
   @DisplayName("Verify that the controller send filtered Music list")
   void musicsFilteredList_returnFilteredMusicListAndLinkedList_ofMusicFilter()throws Exception {
      MusicFilter filter = new MusicFilter();

      // GIVEN
      when(libraryApiProxy.findAllfilteredMusics(anyInt() , any(MusicDTO.class))).thenReturn(allMusicDTOS);


      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/musics")
            .flashAttr("filter",filter))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.MUSIC_ALL))
            .andReturn();

      // THEN
      // THEN
      List<MusicDTO> foundMusics = (List<MusicDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_MUSICS);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundAuthors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_AUTHORS);
      Map<Integer,PersonDTO> foundComposer = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_COMPOSERS);
      Map<Integer,PersonDTO> foundIntepreters = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_INTERPRETERS);
      MusicType[] foundTypes = (MusicType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      MusicFormat[] foundFormats = (MusicFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundMusics).hasSameSizeAs(allMusicDTOS);
      assertThat(foundTitles).hasSameSizeAs(musicsTitles);
      assertThat(foundAuthors).hasSameSizeAs(musicsAuthors);
      assertThat(foundComposer).hasSameSizeAs(musicComposers);
      assertThat(foundIntepreters).hasSameSizeAs(musicInterpreters);
      assertThat(foundTypes).hasSameSizeAs(MusicType.values());
      assertThat(foundFormats).hasSameSizeAs(MusicFormat.values());

      assertThat(foundMusics).isEqualTo(allMusicDTOS);
      assertThat(foundTitles).isEqualTo(musicsTitles);

      for(Integer i=0; i<musicsAuthors.size(); i++) {
         assertThat(foundAuthors).containsEntry(i+13,musicsAuthors.get(i));
      }

      for(int i=0; i<musicComposers.size(); i++) {
         assertThat(foundComposer).containsEntry(i+13,musicComposers.get(i));
      }

      for(int i=0; i<musicInterpreters.size(); i++) {
         assertThat(foundIntepreters).containsEntry(i+13,musicInterpreters.get(i));
      }

      assertThat(foundTypes).isEqualTo(MusicType.values());
      assertThat(foundFormats).isEqualTo(MusicFormat.values());
   }

   @Test
   @Tag("musicView")
   @DisplayName("Verify that the controller send the Music expected")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void musicView_returnMusic_ofOneMusicId() throws Exception {
      MusicDTO expected = allMusicDTOS.get(4);
      String musicEAN = expected.getEan();

      // GIVEN
      when(libraryApiProxy.findMusicById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/music/" + musicEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.MUSIC_READ))
            .andReturn();

      // THEN
      MusicDTO found = (MusicDTO) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_MUSIC);
      Boolean canBorrow = (Boolean) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(found).isEqualTo(expected);
      assertThat(canBorrow).isTrue();
   }

   @Test
   @Tag("musicView")
   @DisplayName("Verify that the user can't borrow more Music if counter equal max quantity")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void musicView_returnCanBorrowIsFalse_ofUserWithMaxCounter() throws Exception {
      MusicDTO expected = allMusicDTOS.get(4);
      String musicEAN = expected.getEan();
      newUserDTO.setCounter(borrowingQuantityMax);
      // GIVEN
      when(libraryApiProxy.findMusicById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/music/" + musicEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.MUSIC_READ))
            .andReturn();

      // THEN
      Boolean canBorrow = (Boolean) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(canBorrow).isFalse();
   }

}
