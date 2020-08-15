package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.libraryapi.model.Booking;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.BookingRepository;
import com.pedsf.library.libraryapi.repository.BorrowingRepository;
import com.pedsf.library.libraryapi.service.BookingService;
import com.pedsf.library.libraryapi.service.MediaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BookingServiceTestIT {

   @Value("${library.booking.delay}")
   private int daysOfDelay;

   private static BookingService bookingService;
   private BookingRepository bookingRepository;

   @Mock
   private static MediaService mediaService;
   @Mock
   private UserApiProxy userApiProxy;
   private static Booking newBooking;
   private static BookingDTO newBookingDTO;
   private static List<BookingDTO> allBookingDTOS = new ArrayList<>();
   private static Map<Integer,UserDTO> allUserDTOS = new HashMap<>();
   private static List<MediaDTO> allMediaDTOS = new ArrayList<>();

   @BeforeAll
   static void beforeAll(@Autowired BookingRepository bookingRepository) {
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

      allBookingDTOS.add(new BookingDTO(1, allUserDTOS.get(4), allMediaDTOS.get(26),Date.valueOf("2020-07-20"),1));
      allBookingDTOS.add(new BookingDTO(2, allUserDTOS.get(5), allMediaDTOS.get(26),Date.valueOf("2020-07-20"),2));
      allBookingDTOS.add(new BookingDTO(3, allUserDTOS.get(3), allMediaDTOS.get(26),Date.valueOf("2020-07-20"),3));
      allBookingDTOS.add(new BookingDTO(4,allUserDTOS.get(5),allMediaDTOS.get(5),Date.valueOf("2020-07-20"), 1));
      allBookingDTOS.add(new BookingDTO(5,allUserDTOS.get(4),allMediaDTOS.get(5),Date.valueOf("2020-07-20"), 2));
      allBookingDTOS.add(new BookingDTO(6,allUserDTOS.get(3),allMediaDTOS.get(5),Date.valueOf("2020-07-20"), 3));
   }

   @BeforeEach
   void beforeEach(@Autowired BookingRepository bookingRepository, @Autowired BorrowingRepository borrowingRepository) {
      bookingService = new BookingService(bookingRepository,mediaService, borrowingRepository,userApiProxy);
      bookingService.setDaysOfDelay(daysOfDelay);

      Mockito.lenient().when(userApiProxy.findUserById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allUserDTOS.get((Integer) invocation.getArguments()[0]));

      Mockito.lenient().when(mediaService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allMediaDTOS.get((Integer) invocation.getArguments()[0]-1));

      newBooking = new Booking(44,"978-2253002864",4,Date.valueOf("2020-08-12"),1);
      UserDTO userDTO = userApiProxy.findUserById(4);
      MediaDTO mediaDTO = mediaService.findById(5);
      newBookingDTO = new BookingDTO(44,userDTO,mediaDTO,Date.valueOf("2020-08-12"),1);

      allBookingDTOS = bookingService.findAll();
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Booking exist")
   void existsById_returnTrue_OfExistingBooking() {
      for(BookingDTO dto : allBookingDTOS) {
         assertThat(bookingService.existsById(dto.getId())).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Booking doesn't exist")
   void existsById_returnFalse_ofInexistingBooking() {
      assertThat(bookingService.existsById(55)).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Booking by is ID")
   void findById_returnBooking_ofExistingBookingId() {
      BookingDTO found;

      for(BookingDTO dto : allBookingDTOS) {
         Integer id = dto.getId();
         found = bookingService.findById(id);

         assertThat(found).isEqualTo(dto);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Booking with wrong ID")
   void findById_returnException_ofInexistingBookingId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> bookingService.findById(55));
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Booking")
   void findAll() {
      assertThat(allBookingDTOS.size()).isEqualTo(2);

      // add one Booking to increase the list
      BookingDTO added = bookingService.save(newBookingDTO);
      List<BookingDTO> listFound = bookingService.findAll();
      assertThat(listFound.size()).isEqualTo(3);
      assertThat(listFound.contains(added)).isTrue();

      bookingService.deleteById(added.getId());
   }

   @Disabled
   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Booking by his User and Media")
   void findAllFiltered_returnOnlyOneBooking_ofExistingUserAndMedia() {
      List<BookingDTO> found;

      for(BookingDTO dto:allBookingDTOS) {
         BookingDTO filter = new BookingDTO();
         filter.setUser(dto.getUser());
         filter.setMedia(dto.getMedia());

         found = bookingService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(dto);
      }
   }
}
