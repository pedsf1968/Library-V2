package com.pedsf.library.libraryapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.libraryapi.controller.BookingController;
import com.pedsf.library.libraryapi.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@Slf4j
@WebMvcTest(controllers = {BookingController.class})
@ExtendWith(SpringExtension.class)
class BookingControllerTestIT {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private BookingService bookingService;

   private static final List<UserDTO> allUserDTOS = new ArrayList<>();
   private static List<MediaDTO> allMediaDTOS = new ArrayList<>();
   private static ObjectMapper mapper = new ObjectMapper();
   private BookingDTO newBookingDTO;
   private List<BookingDTO> allBookingDTOS = new ArrayList<>();


   @BeforeAll
   static void beforeAll() {
      allUserDTOS.add( new UserDTO(1,"Admin","ADMIN","admin@library.org","$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie","22, rue de la Paix","75111","Paris"));
      allUserDTOS.add( new UserDTO(2,"Staff","STAFF","staff@library.org","$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG","1, rue verte","68121","Strasbourg"));
      allUserDTOS.add( new UserDTO(3,"Martin","DUPONT","martin.dupont@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","3, chemin de l’Escale","25000","Besançon"));
      allUserDTOS.add( new UserDTO(4,"Emile","ZOLA","emile.zola@free.fr","$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.", "1, Rue de la Paix","75001","Paris"));
      allUserDTOS.add( new UserDTO(5,"Victor","HUGO","victor.hugo@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi","24, Rue des cannut","69003","Lyon"));

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

      allBookingDTOS.add( new BookingDTO(1,allUserDTOS.get(3),allMediaDTOS.get(4), Date.valueOf("2020-08-12"),1));
      allBookingDTOS.add( new BookingDTO(2,allUserDTOS.get(3),allMediaDTOS.get(7), Date.valueOf("2020-08-12"),1));
      allBookingDTOS.add( new BookingDTO(3,allUserDTOS.get(3),allMediaDTOS.get(9), Date.valueOf("2020-08-12"),1));
   }

   @Test
   @Tag("findAllBookings")
   @DisplayName("Verify that we have the list of all Booking")
   void findAllBookings() throws Exception {
      // GIVEN
      when(bookingService.findAll()).thenReturn(allBookingDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/bookings"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BookingDTO> founds = Arrays.asList(mapper.readValue(json, BookingDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(allBookingDTOS.size());
      for(BookingDTO dto: founds) {
         for(BookingDTO expected:allBookingDTOS) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }
   }

   @Test
   @Tag("findAllFilteredBorrowings")
   @DisplayName("Verify that we get Booking list of User")
   void findAllFilteredBorrowings() throws Exception {
      BookingDTO filter = new BookingDTO();

      // GIVEN
      filter.setUser(allBookingDTOS.get(0).getUser());
      when(bookingService.findAllFiltered(any(BookingDTO.class))).thenReturn(allBookingDTOS);

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/bookings/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<BookingDTO> founds = Arrays.asList(mapper.readValue(json, BookingDTO[].class));

      assertThat(founds.size()).isEqualTo(allBookingDTOS.size());
      for(BookingDTO dto: founds) {
         for(BookingDTO expected:allBookingDTOS) {
            if(dto.getId().equals(expected.getId())) {
               assertThat(dto).isEqualTo(expected);
            }
         }
      }

   }

}
