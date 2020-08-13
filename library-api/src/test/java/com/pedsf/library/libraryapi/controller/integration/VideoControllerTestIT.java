package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.dto.business.MusicDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.business.VideoDTO;
import com.pedsf.library.libraryapi.controller.VideoController;
import com.pedsf.library.libraryapi.service.VideoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {VideoController.class})
@ExtendWith(SpringExtension.class)
class VideoControllerTestIT {
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


}
