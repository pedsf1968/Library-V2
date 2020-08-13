package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.MusicDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.controller.MusicController;
import com.pedsf.library.libraryapi.service.MusicService;
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
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {MusicController.class})
@ExtendWith(SpringExtension.class)
class MusicControllerTestIT {
   private static final List<MusicDTO> allMusicDTOS = new ArrayList<>();
   private static final List<PersonDTO> allPersonDTOS = new ArrayList<>();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private MusicService musicService;

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

      allMusicDTOS.add(new MusicDTO("8809634380036","Kill This Love",2,2,allPersonDTOS.get(13),allPersonDTOS.get(13),allPersonDTOS.get(13)));
      allMusicDTOS.add(new MusicDTO("4988064587100","DDU-DU DDU-DU",3,-6,allPersonDTOS.get(13),allPersonDTOS.get(13),allPersonDTOS.get(13)));
      allMusicDTOS.add(new MusicDTO("4988064585816","RE BLACKPINK",1,1,allPersonDTOS.get(13),allPersonDTOS.get(13),allPersonDTOS.get(13)));
      allMusicDTOS.add(new MusicDTO("8809269506764","MADE",1,-2,allPersonDTOS.get(14),allPersonDTOS.get(14),allPersonDTOS.get(14)));
      allMusicDTOS.add(new MusicDTO("8809265654654","Remember",1,0,allPersonDTOS.get(14),allPersonDTOS.get(14),allPersonDTOS.get(14)));
   }

   @Disabled
   @Test
   @Tag("findAllMusics")
   @DisplayName("Verify that we get the list of all Musics")
   void findAllMusics_returnAllMusics() throws Exception {
      int i = 0;
      // GIVEN
      when(musicService.findAll()).thenReturn(allMusicDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/musics"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in MusicDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<MusicDTO> founds = Arrays.asList(mapper.readValue(json, MusicDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(5);
      for(MusicDTO dto: founds) {
         for(MusicDTO expected:allMusicDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllAllowedMusics")
   @DisplayName("Verify that we get the right list of allowed Musics")
   void findAllAllowedMusics()  throws Exception {
      int i = 1;
      // GIVEN
      when(musicService.findAllAllowed()).thenReturn(allMusicDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/musics/allowed"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<MusicDTO> founds = Arrays.asList(mapper.readValue(json, MusicDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(5);
      for(MusicDTO dto: founds) {
         for(MusicDTO expected:allMusicDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findMusicById")
   @DisplayName("Verify that we can get Music by his EAN")
   void findMusicById()  throws Exception {
      MusicDTO expected = allMusicDTOS.get(3);
      String ean = expected.getEan();

      // GIVEN
      when(musicService.findById(ean)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/musics/"+ean))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      MusicDTO found = mapper.readValue(json, MusicDTO.class);

      assertThat(found).isEqualTo(expected);
   }

}
