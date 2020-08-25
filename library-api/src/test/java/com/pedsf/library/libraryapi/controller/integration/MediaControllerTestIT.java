package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.libraryapi.controller.MediaController;
import com.pedsf.library.libraryapi.service.MediaService;
import lombok.extern.slf4j.Slf4j;
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
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(controllers = {MediaController.class})
@ExtendWith(SpringExtension.class)
public class MediaControllerTestIT {

   private static final List<MediaDTO> allMediaDTOS = new ArrayList<>();

   private static final Map<String, BookDTO> allBookDTOS = new HashMap<>();
   private static final Map<String, GameDTO> allGameDTOS = new HashMap<>();
   private static final Map<String, MusicDTO> allMusicDTOS = new HashMap<>();
   private static final Map<String, VideoDTO> allVideoDTOS = new HashMap<>();
   private static final Map<Integer,PersonDTO> allPersonDTOS = new HashMap<>();
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private MediaService mediaService;

   @BeforeAll
   static void beforeAll() {
      allPersonDTOS.put(1, new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersonDTOS.put(2, new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersonDTOS.put(3, new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersonDTOS.put(4, new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersonDTOS.put(5, new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersonDTOS.put(6, new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersonDTOS.put(7, new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersonDTOS.put(8, new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersonDTOS.put(9, new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersonDTOS.put(10, new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersonDTOS.put(11, new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersonDTOS.put(12, new PersonDTO(12,"Larousse","Larousse",null));
      allPersonDTOS.put(13, new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersonDTOS.put(14, new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersonDTOS.put(15, new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersonDTOS.put(16, new PersonDTO(16,"Microsoft","Microsoft",null));

      allBookDTOS.put( "978-2253004226", new BookDTO("978-2253004226","Germinal",3,2,"9782253004226",allPersonDTOS.get(1),allPersonDTOS.get(11)));
      allBookDTOS.put( "978-2253002864", new BookDTO("978-2253002864","Au bonheur des dames",1,0,"9782253002864",allPersonDTOS.get(1),allPersonDTOS.get(11)));
      allBookDTOS.put( "978-2253003656", new BookDTO("978-2253003656","Nana",2,1,"9782253003656",allPersonDTOS.get(1),allPersonDTOS.get(11)));
      allBookDTOS.put( "978-2253010692", new BookDTO("978-2253010692","L'éducation sentimentale",2,-4,"9782253010692",allPersonDTOS.get(2),allPersonDTOS.get(11)));
      allBookDTOS.put( "978-2070413119", new BookDTO("978-2070413119","Madame Bovary",2,-1,"9782070413119",allPersonDTOS.get(2),allPersonDTOS.get(12)));
      allBookDTOS.put( "978-2253096337", new BookDTO("978-2253096337","Les Misérables (Tome 1)",3,-6,"9782253096337",allPersonDTOS.get(3),allPersonDTOS.get(13)));
      allBookDTOS.put( "978-2253096344", new BookDTO("978-2253096344","Les Misérables (Tome 2)",3,1,"9782253096344",allPersonDTOS.get(3),allPersonDTOS.get(13)));

      allGameDTOS.put( "5035223122470", new GameDTO("5035223122470","NFS Need for Speed™ Heat",2,-4,allPersonDTOS.get(14)));
      allGameDTOS.put( "9879876513246", new GameDTO("9879876513246","NFS Need for Speed™ Paybak",2,-2,allPersonDTOS.get(14)));
      allGameDTOS.put( "3526579879836", new GameDTO("3526579879836","NFS Need for Speed™ No limit",2,-1,allPersonDTOS.get(14)));
      allGameDTOS.put( "0805529340299", new GameDTO("0805529340299","Flight Simulator 2004 : Un Siècle d'Aviation",1,1,allPersonDTOS.get(15)));
      allGameDTOS.put( "0805kuyiuo299", new GameDTO("0805kuyiuo299","Age of Empire",1,-2,allPersonDTOS.get(15)));

      allMusicDTOS.put( "8809634380036", new MusicDTO("8809634380036","Kill This Love",2,2,allPersonDTOS.get(13),allPersonDTOS.get(13),allPersonDTOS.get(13)));
      allMusicDTOS.put( "4988064587100", new MusicDTO("4988064587100","DDU-DU DDU-DU",3,-6,allPersonDTOS.get(13),allPersonDTOS.get(13),allPersonDTOS.get(13)));
      allMusicDTOS.put( "4988064585816", new MusicDTO("4988064585816","RE BLACKPINK",1,1,allPersonDTOS.get(13),allPersonDTOS.get(13),allPersonDTOS.get(13)));
      allMusicDTOS.put( "8809269506764", new MusicDTO("8809269506764","MADE",1,-2,allPersonDTOS.get(14),allPersonDTOS.get(14),allPersonDTOS.get(14)));
      allMusicDTOS.put( "8809265654654", new MusicDTO("8809265654654","Remember",1,0,allPersonDTOS.get(14),allPersonDTOS.get(14),allPersonDTOS.get(14)));

      allVideoDTOS.put( "3475001058980", new VideoDTO("3475001058980","Parasite",3,2,allPersonDTOS.get(3)));
      allVideoDTOS.put( "6546546878980", new VideoDTO("6546546878980","Germinal",1,-2,allPersonDTOS.get(0)));
      allVideoDTOS.put( "2224601058980", new VideoDTO("2224601058980","El tapioca",3,2,allPersonDTOS.get(0)));
      allVideoDTOS.put( "3472089898980", new VideoDTO("3472089898980","Mort aux Trousses",1,1,allPersonDTOS.get(2)));
      allVideoDTOS.put( "8885466546580", new VideoDTO("8885466546580","Banzai",2,-4,allPersonDTOS.get(2)));
      allVideoDTOS.put( "3476546046540", new VideoDTO("3476546046540","Le secret",3,-2,allPersonDTOS.get(3)));

      allBookDTOS.forEach((bookEAN,bookDTO) -> {
         bookDTO.setType(BookType.NOVEL.name());
         bookDTO.setFormat(BookFormat.POCKET.name());
         MediaDTO mediaDTO = new MediaDTO(bookDTO);
         allMediaDTOS.add(mediaDTO);
      });

      allGameDTOS.forEach((gameEAN,gameDTO) -> {
         gameDTO.setType(GameType.ARCADE.name());
         gameDTO.setFormat(GameFormat.NINTENDO_DS.name());
         MediaDTO mediaDTO = new MediaDTO(gameDTO);
         allMediaDTOS.add(mediaDTO);
      });

      allMusicDTOS.forEach((gameEAN,musicDTO) -> {
         musicDTO.setType(MusicType.POP.name());
         musicDTO.setFormat(MusicFormat.CD.name());
         MediaDTO mediaDTO = new MediaDTO(musicDTO);
         allMediaDTOS.add(mediaDTO);
      });

      allVideoDTOS.forEach((gameEAN,videoDTO) -> {
         videoDTO.setType(VideoType.SCI_FI.name());
         videoDTO.setFormat(VideoFormat.DVD.name());
         MediaDTO mediaDTO = new MediaDTO(videoDTO);
         allMediaDTOS.add(mediaDTO);
      });

      Integer mediaId = 1;

      for(MediaDTO mediaDTO:allMediaDTOS) {
         mediaDTO.setId(mediaId++);
         mediaDTO.setStatus(MediaStatus.FREE.name());
      }

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @Test
   @Tag("findAllMedias")
   @DisplayName("Verify that we get the list of all Medias")
   void findAllMedias_returnAllMedias() throws Exception {
      // GIVEN
      when(mediaService.findAll()).thenReturn(allMediaDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/medias"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<MediaDTO> founds = Arrays.asList(mapper.readValue(json, MediaDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(23);
      for(MediaDTO dto: founds) {
         for(MediaDTO expected:allMediaDTOS) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllFilteredMedias")
   @DisplayName("Verify that we get Media list from first title")
   void findAllFilteredMedias() throws Exception {
      List<MediaDTO> filtered = allMediaDTOS.subList(0,3);
      MediaDTO filter = new MediaDTO();

      // GIVEN
      filter.setTitle(filtered.get(0).getTitle());
      when(mediaService.findAllFiltered(any(MediaDTO.class))).thenReturn(filtered);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/medias/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<MediaDTO> founds = Arrays.asList(mapper.readValue(json, MediaDTO[].class));
      List<MediaDTO> found;

      assertThat(founds.size()).isEqualTo(3);
      for(MediaDTO dto: founds) {
         for(MediaDTO expected:filtered) {
            if(dto.getEan().equals(expected.getEan())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }

   }

   @Test
   @Tag("findMediaById")
   @DisplayName("Verify that we can get Media by his EAN")
   void findMediaById()  throws Exception {
      MediaDTO expected = allMediaDTOS.get(3);
      Integer mediaId = expected.getId();

      // GIVEN
      when(mediaService.findById(mediaId)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/medias/" + mediaId))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      MediaDTO found = mapper.readValue(json, MediaDTO.class);

      assertThat(found).isEqualTo(expected);
   }




}
