package com.pedsf.library.libraryapi.controller.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.controller.BookingController;
import com.pedsf.library.libraryapi.service.BookingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = {BookingController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BookingControllerTest {

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private BookingService bookingService;

   private static final List<UserDTO> allUserDTOS = new ArrayList<>();
   private static final List<MediaDTO> allMediaDTOS = new ArrayList<>();
   private List<BookingDTO> allBookingDTOS = new ArrayList<>();
   private static BookingDTO newBookingDTO;
   private static ObjectMapper mapper = new ObjectMapper();

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

      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      newBookingDTO = new BookingDTO(5,allUserDTOS.get(2),allMediaDTOS.get(5), Date.valueOf("2020-08-12"),1);

      allBookingDTOS.add( new BookingDTO(1,allUserDTOS.get(3),allMediaDTOS.get(2), Date.valueOf("2020-08-12"),1));
      allBookingDTOS.add( new BookingDTO(2,allUserDTOS.get(3),allMediaDTOS.get(4), Date.valueOf("2020-08-12"),1));
      allBookingDTOS.add( new BookingDTO(3,allUserDTOS.get(3),allMediaDTOS.get(8), Date.valueOf("2020-08-12"),1));
   }

   @Test
   @Tag("findAllBookings")
   @DisplayName("Verify that we get NotFound if there are no Booking")
   void findAllBookings_returnNotFound_ofResourceNotFoundException() throws Exception {

      // GIVEN
      Mockito.lenient().when(bookingService.findAll()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/bookings"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we get NotFound if there are no Booking")
   void findAllFiltered_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookingService.findAllFiltered(newBookingDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newBookingDTO);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.post("/bookings/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("findBookingsByUserId")
   @DisplayName("Verify that we get NotFound if there are no Booking")
   void findBookingsByUserId_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookingService.findBookingsByUserId(anyInt())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/bookings/user/" + 654))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("addBooking")
   @DisplayName("Verify that we get NotFound if there are no Media")
   void addBooking_returnNotFound_ofResourceNotFoundException() throws Exception {
      Integer userId = newBookingDTO.getUser().getId();
      String mediaEan = newBookingDTO.getMedia().getEan();

      // GIVEN
      when(bookingService.booking(anyInt(),anyString())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      String json = mapper.writeValueAsString(mediaEan);
      mockMvc.perform(
            MockMvcRequestBuilders.post("/bookings/"  + userId)
                  .contentType(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("cancelBooking")
   @DisplayName("Verify that we get NotFound if there are no Booking")
   void cancelBooking_returnNotFound_ofResourceNotFoundException() throws Exception {
      Integer userId = newBookingDTO.getUser().getId();
      Integer bookingId = newBookingDTO.getId();

      // GIVEN
      when(bookingService.cancelBooking(bookingId)).thenThrow(ResourceNotFoundException.class);

      // WHEN
      String json = mapper.writeValueAsString(bookingId);
      mockMvc.perform(
            MockMvcRequestBuilders.post("/bookings/cancel/"  + userId)
                  .contentType(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("findReadyToPickUp")
   @DisplayName("Verify that we get NotFound if there are no Booking to pickup")
   void findReadyToPickUp_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(bookingService.findReadyToPickUp()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/bookings/pickup"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }



}
