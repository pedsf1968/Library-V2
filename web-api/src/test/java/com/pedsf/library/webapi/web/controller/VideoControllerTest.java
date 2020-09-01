package com.pedsf.library.webapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.VideoFormat;
import com.pedsf.library.dto.VideoType;
import com.pedsf.library.dto.business.VideoDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.filter.VideoFilter;
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
@Import(VideoController.class)
@WebMvcTest(controllers = {VideoController.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class VideoControllerTest {
   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   private static final List<VideoDTO> allVideoDTOS = new ArrayList<>();
   private static final List<String> videosTitles = new ArrayList<>();
   private static final List<PersonDTO> videosDirectors = new ArrayList<>();
   private static final List<PersonDTO> videosActors = new ArrayList<>();
   private static UserDTO   newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;
   @MockBean
   private LibraryApiProxy libraryApiProxy;
   @MockBean
   private UserApiProxy userApiProxy;

   private VideoController videoController;

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
      videosDirectors.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      videosDirectors.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      videosDirectors.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      videosDirectors.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));

      videosActors.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      videosActors.add(  new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      videosActors.add(  new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      videosActors.add(  new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      videosActors.add(  new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));

      allVideoDTOS.add(new VideoDTO("3475001058980","Parasite",3,2,videosDirectors.get(0)));
      allVideoDTOS.add(new VideoDTO("6546546878980","Germinal",1,-2,videosDirectors.get(1)));
      allVideoDTOS.add(new VideoDTO("2224601058980","El tapioca",3,2,videosDirectors.get(1)));
      allVideoDTOS.add(new VideoDTO("3472089898980","Mort aux Trousses",1,1,videosDirectors.get(2)));
      allVideoDTOS.add(new VideoDTO("8885466546580","Banzai",2,-4,videosDirectors.get(3)));
      allVideoDTOS.add(new VideoDTO("3476546046540","Le secret",3,-2,videosDirectors.get(2)));

      for(VideoDTO b : allVideoDTOS) {
         videosTitles.add(b.getTitle());
      }

      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      when(libraryApiProxy.findAllAllowedVideos(anyInt())).thenReturn(allVideoDTOS);
      when(libraryApiProxy.getAllVideosDirectors()).thenReturn(videosDirectors);
      when(libraryApiProxy.getAllVideosActors()).thenReturn(videosActors);
      when(libraryApiProxy.getAllVideosTitles()).thenReturn(videosTitles);
      videoController = new VideoController(libraryApiProxy,userApiProxy);
      videoController.setBorrowingQuantityMax(borrowingQuantityMax);
      mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();
   }

   @Test
   @Tag("videosList")
   @DisplayName("Verify that the controller send all Video list")
   void videosList_returnVideoListAndLinkedList_ofVideoFilter() throws Exception {
      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/videos"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.VIDEO_ALL))
            .andReturn();

      // THEN
      List<VideoDTO> foundVideos = (List<VideoDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_VIDEOS);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundDirectors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_DIRECTORS);
      Map<Integer,PersonDTO> foundActors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_ACTORS);
      VideoType[] foundTypes = (VideoType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      VideoFormat[] foundFormats = (VideoFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundVideos).hasSameSizeAs(allVideoDTOS);
      assertThat(foundTitles).hasSameSizeAs(videosTitles);
      assertThat(foundDirectors).hasSameSizeAs(videosDirectors);
      assertThat(foundActors).hasSameSizeAs(videosActors);
      assertThat(foundTypes).hasSameSizeAs(VideoType.values());
      assertThat(foundFormats).hasSameSizeAs(VideoFormat.values());


      assertThat(foundVideos).isEqualTo(allVideoDTOS);
      assertThat(foundTitles).isEqualTo(videosTitles);

      for(int i=0; i<videosDirectors.size(); i++) {
         assertThat(foundDirectors).containsEntry(i+1,videosDirectors.get(i));
      }

      for(int i=0; i<videosActors.size(); i++) {
         assertThat(foundActors).containsEntry(i+5,videosActors.get(i));
      }

      assertThat(foundTypes).isEqualTo(VideoType.values());
      assertThat(foundFormats).isEqualTo(VideoFormat.values());
   }

   @Test
   @Tag("videosFilteredList")
   @DisplayName("Verify that the controller send filtered Video list")
   void videosFilteredList_returnFilteredVideoListAndLinkedList_ofVideoFilter()throws Exception {
      VideoFilter filter = new VideoFilter();

      // GIVEN
      when(libraryApiProxy.findAllFilteredVideos(anyInt() , any(VideoDTO.class))).thenReturn(allVideoDTOS);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/videos")
            .flashAttr("filter",filter))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.VIDEO_ALL))
            .andReturn();

      // THEN
      List<VideoDTO> foundVideos = (List<VideoDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_VIDEOS);
      List<String> foundTitles = (List<String>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TITLES);
      Map<Integer,PersonDTO> foundDirectors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_DIRECTORS);
      Map<Integer,PersonDTO> foundActors = (Map<Integer, PersonDTO>) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_ACTORS);
      VideoType[] foundTypes = (VideoType[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_TYPES);
      VideoFormat[] foundFormats = (VideoFormat[]) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_FILTER_FORMATS);

      assertThat(foundVideos).hasSameSizeAs(allVideoDTOS);
      assertThat(foundTitles).hasSameSizeAs(videosTitles);
      assertThat(foundDirectors).hasSameSizeAs(videosDirectors);
      assertThat(foundActors).hasSameSizeAs(videosActors);
      assertThat(foundTypes).hasSameSizeAs(VideoType.values());
      assertThat(foundFormats).hasSameSizeAs(VideoFormat.values());


      assertThat(foundVideos).isEqualTo(allVideoDTOS);
      assertThat(foundTitles).isEqualTo(videosTitles);

      for(int i=0; i<videosDirectors.size(); i++) {
         assertThat(foundDirectors).containsEntry(i+1,videosDirectors.get(i));
      }

      for(int i=0; i<videosActors.size(); i++) {
         assertThat(foundActors).containsEntry(i+5,videosActors.get(i));
      }

      assertThat(foundTypes).isEqualTo(VideoType.values());
      assertThat(foundFormats).isEqualTo(VideoFormat.values());
   }

   @Test
   @Tag("videoView")
   @DisplayName("Verify that the controller send the Video expected")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void videoView_returnVideo_ofOneVideoId() throws Exception {
      VideoDTO expected = allVideoDTOS.get(3);
      String videoEAN = expected.getEan();

      // GIVEN
      when(libraryApiProxy.findVideoById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/video/" + videoEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.VIDEO_READ))
            .andReturn();

      // THEN
      VideoDTO found = (VideoDTO) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_VIDEO);
      Boolean canBorrow = (Boolean) result.getModelAndView().getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(found).isEqualTo(expected);
      assertThat(canBorrow).isTrue();
   }

   @Test
   @Tag("videoView")
   @DisplayName("Verify that the user can't borrow more Video if counter equal max quentity")
   @WithMockUser(username = "user", password = "pwd", roles = "USER")
   void videoView_returnCanBorrowIsFalse_ofUserWithMaxCounter() throws Exception {
      VideoDTO expected = allVideoDTOS.get(3);
      String videoEAN = expected.getEan();

      newUserDTO.setCounter(borrowingQuantityMax);
      // GIVEN
      when(libraryApiProxy.findVideoById(anyString())).thenReturn(expected);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/video/" + videoEAN))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.VIDEO_READ))
            .andReturn();

      // THEN
      Boolean canBorrow = (Boolean) Objects.requireNonNull(result.getModelAndView()).getModel().get(PathTable.ATTRIBUTE_CAN_BORROW);
      assertThat(canBorrow).isFalse();
   }

}