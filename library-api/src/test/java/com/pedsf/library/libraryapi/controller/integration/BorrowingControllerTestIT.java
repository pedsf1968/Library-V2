package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BorrowingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.libraryapi.controller.BorrowingController;
import com.pedsf.library.libraryapi.service.BorrowingService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(controllers = {BorrowingController.class})
@ExtendWith(SpringExtension.class)
class BorrowingControllerTestIT {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private BorrowingService borrowingService;

   private static Map<Integer,UserDTO> allUserDTOS = new HashMap<>();
   private static List<MediaDTO> allMediaDTOS = new ArrayList<>();
   private BorrowingDTO newBorrowingDTO;
   private List<BorrowingDTO> allBorrowingDTOS = new ArrayList<>();
   private static ObjectMapper mapper = new ObjectMapper();

   @BeforeAll
   static void beforeAll() {
      allUserDTOS.put(1, new UserDTO(1,"Admin","ADMIN","admin@library.org","$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie","22, rue de la Paix","75111","Paris"));
      allUserDTOS.put(2, new UserDTO(2,"Staff","STAFF","staff@library.org","$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG","1, rue verte","68121","Strasbourg"));
      allUserDTOS.put(3, new UserDTO(3,"Martin","DUPONT","martin.dupont@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","3, chemin de l’Escale","25000","Besançon"));
      allUserDTOS.put(4, new UserDTO(4,"Emile","ZOLA","emile.zola@free.fr","$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.", "1, Rue de la Paix","75001","Paris"));
      allUserDTOS.put(5, new UserDTO(5,"Victor","HUGO","victor.hugo@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi","24, Rue des cannut","69003","Lyon"));

      allMediaDTOS.add(new MediaDTO( 1,"978-2253004226", "Germinal","BOOK", "BORROWED", 4,2));
      allMediaDTOS.add(new MediaDTO(2,"978-2253004226", "Germinal","BOOK","BORROWED",4,2));
      allMediaDTOS.add(new MediaDTO(3,"978-2253004226", "Germinal","BOOK","FREE",4,2));
      allMediaDTOS.add(new MediaDTO(4,"978-2253004226", "Germinal","BOOK","FREE",4,2));
      allMediaDTOS.add(new MediaDTO(5,"978-2253002864","Au bonheur des dames","BOOK","BORROWED",2,0));
      allMediaDTOS.add(new MediaDTO(6,"978-2253002864","Au bonheur des dames","BOOK","BORROWED",2,0));
      allMediaDTOS.add(new MediaDTO(7,"978-2253003656","Nana","BOOK","BORROWED",2,1));
      allMediaDTOS.add(new MediaDTO(8,"978-2253003656","Nana","BOOK","FREE",2,1));
      allMediaDTOS.add(new MediaDTO(9,"978-2253010692","L'éducation sentimentale","BOOK","FREE",2,0));
      allMediaDTOS.add(new MediaDTO(10,"978-2253010692","L'éducation sentimentale","BOOK","FREE",2,0));
      allMediaDTOS.add(new MediaDTO(11,"978-2070413119","Madame Bovary","BOOK","BORROWED",3,1));
      allMediaDTOS.add(new MediaDTO(12,"978-2070413119","Madame Bovary","BOOK","FREE",3,1));
      allMediaDTOS.add(new MediaDTO(13,"978-2070413119","Madame Bovary","BOOK","FREE",3,1));
      allMediaDTOS.add(new MediaDTO(14,"978-2253096337","Les Misérables (Tome 1)","BOOK","FREE",3,3));
      allMediaDTOS.add(new MediaDTO(15,"978-2253096337","Les Misérables (Tome 1)","BOOK","FREE",3,3));
      allMediaDTOS.add(new MediaDTO(16,"978-2253096337","Les Misérables (Tome 1)","BOOK","FREE",3,3));
      allMediaDTOS.add(new MediaDTO(17,"978-2253096344","Les Misérables (Tome 2)","BOOK","BORROWED",3,2));
      allMediaDTOS.add(new MediaDTO(18,"978-2253096344","Les Misérables (Tome 2)","BOOK","FREE",3,2));
      allMediaDTOS.add(new MediaDTO(19,"978-2253096344","Les Misérables (Tome 2)","BOOK","FREE",3,2));
      allMediaDTOS.add(new MediaDTO(20,"3475001058980","Parasite","VIDEO","BORROWED",3,2));
      allMediaDTOS.add(new MediaDTO(21,"3475001058980","Parasite","VIDEO","FREE",3,2));
      allMediaDTOS.add(new MediaDTO(22,"3475001058980","Parasite","VIDEO","FREE",3,2));
      allMediaDTOS.add(new MediaDTO(23,"8809634380036","Kill This Love","MUSIC","FREE",2,2));
      allMediaDTOS.add(new MediaDTO(24,"8809634380036","Kill This Love","MUSIC","FREE",2,2));
      allMediaDTOS.add(new MediaDTO(25,"4988064587100","DDU-DU DDU-DU","MUSIC","FREE",2,2));
      allMediaDTOS.add(new MediaDTO(26,"4988064587100","DDU-DU DDU-DU","MUSIC","FREE",2,2));
      allMediaDTOS.add(new MediaDTO(27,"4988064585816","RE BLACKPINK","MUSIC","BORROWED",1,0));
      allMediaDTOS.add(new MediaDTO(28,"8809269506764","MADE","MUSIC","FREE",1,1));

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      newBorrowingDTO = new BorrowingDTO(5,allUserDTOS.get(2),allMediaDTOS.get(5), Date.valueOf("2020-08-12"),null);

      allBorrowingDTOS.add(new BorrowingDTO( 1, allUserDTOS.get(4), allMediaDTOS.get(0), Date.valueOf("2020-07-13"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 2, allUserDTOS.get(4), allMediaDTOS.get(4), Date.valueOf("2020-07-20"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 3, allUserDTOS.get(4), allMediaDTOS.get(6), Date.valueOf("2020-07-20"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 4, allUserDTOS.get(4), allMediaDTOS.get(19), Date.valueOf("2020-07-20"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 5, allUserDTOS.get(3), allMediaDTOS.get(1), Date.valueOf("2020-07-13"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 6, allUserDTOS.get(5), allMediaDTOS.get(10), Date.valueOf("2020-07-20"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 7, allUserDTOS.get(5), allMediaDTOS.get(16), Date.valueOf("2020-07-20"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 8, allUserDTOS.get(5), allMediaDTOS.get(26), Date.valueOf("2020-07-13"),null));
      allBorrowingDTOS.add(new BorrowingDTO( 9, allUserDTOS.get(3), allMediaDTOS.get(5), Date.valueOf("2020-07-15"),null));
   }

   @Test
   @Tag("findAllBorrowings")
   @DisplayName("Verify that we have the list of all Borrowings")
   void findAllBorrowings() throws Exception {
      // GIVEN
      when(borrowingService.findAll()).thenReturn(allBorrowingDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/borrowings"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BorrowingDTO> founds = Arrays.asList(mapper.readValue(json, BorrowingDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(allBorrowingDTOS.size());
      for(BorrowingDTO dto: founds) {
         for(BorrowingDTO expected:allBorrowingDTOS) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllFilteredBorrowings")
   @DisplayName("Verify that we get Borrowing list of User")
   void findAllFilteredBorrowings_returnUserBooking_ofUserAndBorrowingList() throws Exception {
      BorrowingDTO filter = new BorrowingDTO();

      // GIVEN
      filter.setUser(allBorrowingDTOS.get(0).getUser());
      when(borrowingService.findAllFiltered(any(BorrowingDTO.class))).thenReturn(allBorrowingDTOS);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/borrowings/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BorrowingDTO> founds = Arrays.asList(mapper.readValue(json, BorrowingDTO[].class));

      assertThat(founds.size()).isEqualTo(allBorrowingDTOS.size());
      for(BorrowingDTO dto: founds) {
         for(BorrowingDTO expected:allBorrowingDTOS) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findByUserIdNotReturn")
   @DisplayName("Verify that we have the list of User Borrowings not returned")
   void findByUserIdNotReturn_returnUserBorrowing_ofUserId() throws Exception {

      // GIVEN
      Integer userId = allBorrowingDTOS.get(1).getUser().getId();
      when(borrowingService.findByUserIdNotReturn(userId)).thenReturn(allBorrowingDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/borrowings/user/" + userId))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BorrowingDTO> founds = Arrays.asList(mapper.readValue(json, BorrowingDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(allBorrowingDTOS.size());
      for(BorrowingDTO dto: founds) {
         for(BorrowingDTO expected:allBorrowingDTOS) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findBorrowingById")
   @DisplayName("Verify that we have the list of User Borrowing")
   void findBorrowingById_returnUserBooking_ofBorrowingId() throws Exception {
      // GIVEN
      Integer borrowingId = newBorrowingDTO.getId();
      when(borrowingService.findById(borrowingId)).thenReturn(newBorrowingDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/borrowings/" + borrowingId))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BorrowingDTO found = mapper.readValue(json, BorrowingDTO.class);

      // THEN
      assertThat(found).isEqualTo(newBorrowingDTO);
   }

   @Test
   @Tag("addBorrowing")
   @DisplayName("Verify that we can add Borrowing")
   void addBorrowing_returnNewBorrowing_ofMediaEanAndUserId() throws Exception {
      Integer userId = newBorrowingDTO.getUser().getId();
      String mediaEan = newBorrowingDTO.getMedia().getEan();

      // GIVEN
      when(borrowingService.borrow(anyInt(),anyString())).thenReturn(newBorrowingDTO);

      // WHEN
      String json = mapper.writeValueAsString(mediaEan);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/borrowings/"  + userId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BorrowingDTO found = mapper.readValue(json, BorrowingDTO.class);

      assertThat(found).isEqualTo(newBorrowingDTO);
   }

   @Test
   @Tag("extendBorrowing")
   @DisplayName("Verify that we can extend a Borrowing")
   void extendBorrowing_returnNewBorrowing_ofMediaEanAndUserId() throws Exception {
      Integer userId = newBorrowingDTO.getUser().getId();
      Integer mediaId = newBorrowingDTO.getMedia().getId();

      // GIVEN
      when(borrowingService.extend(anyInt(),anyInt())).thenReturn(newBorrowingDTO);

      // WHEN
      String json = mapper.writeValueAsString(mediaId);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/borrowings/"  + userId + "/extend")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BorrowingDTO found = mapper.readValue(json, BorrowingDTO.class);

      assertThat(found).isEqualTo(newBorrowingDTO);
   }

   @Test
   @Tag("borrow")
   @DisplayName("Verify that we can create a Borrowing")
   void borrow_returnNewBorrowing_ofMediaEanAndUserId() throws Exception {
      Integer userId = newBorrowingDTO.getUser().getId();
      String mediaEan = newBorrowingDTO.getMedia().getEan();

      // GIVEN
      when(borrowingService.borrow(anyInt(),anyString())).thenReturn(newBorrowingDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/borrowings/"  + userId + "/" + mediaEan))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BorrowingDTO found = mapper.readValue(json, BorrowingDTO.class);

      assertThat(found).isEqualTo(newBorrowingDTO);
   }

   @Test
   @Tag("addGiveBack")
   @DisplayName("Verify that we can restitute a Borrowing")
   void addGiveBack_returnBorrowingRestitute_ofMediaIdAndUserId() throws Exception {
      Integer userId = newBorrowingDTO.getUser().getId();
      Integer mediaId = newBorrowingDTO.getMedia().getId();

      // GIVEN
      when(borrowingService.restitute(anyInt(),anyInt())).thenReturn(newBorrowingDTO);

      // WHEN
      String json = mapper.writeValueAsString(mediaId);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/borrowings/"  + userId + "/restitute")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      BorrowingDTO found = mapper.readValue(json, BorrowingDTO.class);

      assertThat(found).isEqualTo(newBorrowingDTO);
   }

   @Test
   @Tag("findDelayed")
   @DisplayName("Verify that return delayed Borrowing")
   void findDelayed_returnDelayedBorrowing_ofOutOfDateBorrowing() throws Exception {
      Integer userId = newBorrowingDTO.getUser().getId();
      String mediaEan = newBorrowingDTO.getMedia().getEan();

      // GIVEN
      when(borrowingService.findDelayed(any(java.util.Date.class))).thenReturn(allBorrowingDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/borrowings/delayed/11052020"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BorrowingDTO> founds = Arrays.asList(mapper.readValue(json, BorrowingDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(allBorrowingDTOS.size());
      for(BorrowingDTO dto: founds) {
         for(BorrowingDTO expected:allBorrowingDTOS) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

}
