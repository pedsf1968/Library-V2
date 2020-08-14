package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.libraryapi.controller.VideoController;
import com.pedsf.library.libraryapi.service.VideoService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {VideoController.class})
@ExtendWith(SpringExtension.class)
class VideoControllerTestIT {
   private static final String VIDEO_TITLE_TEST = "All about my wife";

   private static final List<VideoDTO> allVideoDTOS = new ArrayList<>();
   private static final List<PersonDTO> allPersonDTOS = new ArrayList<>();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private VideoService videoService;

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

      allVideoDTOS.add(new VideoDTO("3475001058980","Parasite",3,2,allPersonDTOS.get(3)));
      allVideoDTOS.add(new VideoDTO("6546546878980","Germinal",1,-2,allPersonDTOS.get(0)));
      allVideoDTOS.add(new VideoDTO("2224601058980","El tapioca",3,2,allPersonDTOS.get(0)));
      allVideoDTOS.add(new VideoDTO("3472089898980","Mort aux Trousses",1,1,allPersonDTOS.get(2)));
      allVideoDTOS.add(new VideoDTO("8885466546580","Banzai",2,-4,allPersonDTOS.get(2)));
      allVideoDTOS.add(new VideoDTO("3476546046540","Le secret",3,-2,allPersonDTOS.get(3)));
   }

   @Test
   @Tag("findAllVideos")
   @DisplayName("Verify that we get the list of all Videos")
   void findAllVideos_returnAllVideos() throws Exception {
      int i = 0;
      // GIVEN
      when(videoService.findAll()).thenReturn(allVideoDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/videos"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in VideoDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<VideoDTO> founds = Arrays.asList(mapper.readValue(json, VideoDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(6);
      for(VideoDTO dto: founds) {
         for(VideoDTO expected:allVideoDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllAllowedVideos")
   @DisplayName("Verify that we get the right list of allowed Videos")
   void findAllAllowedVideos()  throws Exception {
      int i = 1;
      // GIVEN
      when(videoService.findAllAllowed()).thenReturn(allVideoDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/videos/allowed"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<VideoDTO> founds = Arrays.asList(mapper.readValue(json, VideoDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(6);
      for (VideoDTO dto : founds) {
         for (VideoDTO expected : allVideoDTOS) {
            if (dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findVideoById")
   @DisplayName("Verify that we can get Video by his EAN")
   void findVideoById()  throws Exception {
      VideoDTO expected = allVideoDTOS.get(3);
      String ean = expected.getEan();

      // GIVEN
      when(videoService.findById(ean)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/videos/"+ean))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      VideoDTO found = mapper.readValue(json, VideoDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("addVideo")
   @DisplayName("Verify that we can add Video")
   void addVideo() throws Exception {
      VideoDTO expected = allVideoDTOS.get(4);
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      when(videoService.save(any(VideoDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.post("/videos")
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      VideoDTO found = mapper.readValue(json, VideoDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Disabled
   @Test
   @Tag("updateVideo")
   @DisplayName("Verify that we can update a Video")
   void updateVideo() throws Exception {
      VideoDTO expected = allVideoDTOS.get(4);
      expected.setTitle(VIDEO_TITLE_TEST);
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      when(videoService.update(any(VideoDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.put("/videos/"+ expected.getEan())
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      VideoDTO found = mapper.readValue(json, VideoDTO.class);

      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("deleteVideo")
   @DisplayName("Verify that we can delate a Video")
   void deleteVideo() throws Exception {
      VideoDTO expected = allVideoDTOS.get(2);
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      doNothing().when(videoService).deleteById(expected.getEan());

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.delete("/videos/"+ expected.getEan())
                      .contentType(MediaType.APPLICATION_JSON)
                      .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                      .content(json))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();
   }

   @Test
   @Tag("getAllVideosDirectors")
   @DisplayName("Verify that we get all directors list")
   void getAllVideosDirectors() throws Exception {
      List<PersonDTO> directors = new ArrayList<>();
      for(VideoDTO videoDTO : allVideoDTOS) {
         directors.add(videoDTO.getDirector());
      }

      // GIVEN
      when(videoService.findAllDirectors()).thenReturn(directors);

      // WHEN
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.get("/videos/directors"))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<PersonDTO> founds = Arrays.asList(mapper.readValue(json, PersonDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(directors.size());
      for(PersonDTO dto: founds) {
         for(PersonDTO expected:directors) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("getAllVideosTitles")
   @DisplayName("Verify that we get all titles list")
   void getAllVideosTitles() throws Exception {
      List<String> titles = new ArrayList<>();
      for(VideoDTO videoDTO : allVideoDTOS) {
         titles.add(videoDTO.getTitle());
      }

      // GIVEN
      when(videoService.findAllTitles()).thenReturn(titles);

      // WHEN
      final MvcResult result = mockMvc.perform(
              MockMvcRequestBuilders.get("/videos/titles"))
              .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<String> founds = Arrays.asList(mapper.readValue(json, String[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(titles.size());
      for(String title: founds) {
         assertThat(titles.contains(title)).isTrue();
      }
   }
}
