package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.UserStatus;
import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.ForbiddenException;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

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

      Mockito.lenient().when(mediaService.findOneByEan(anyString())).thenAnswer(
              (InvocationOnMock invocation) -> {
                 for(MediaDTO m : allMediaDTOS) {
                    if(m.getEan().equals((String) invocation.getArguments()[0])) {
                       return m;
                    }
                 }
                 return null;
              });


      allBookingDTOS =bookingService.findAll();
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
      assertThat(allBookingDTOS.size()).isEqualTo(3);

      // add one Booking to increase the list
      BookingDTO added = bookingService.save(newBookingDTO);
      List<BookingDTO> listFound = bookingService.findAll();
      assertThat(listFound.size()).isEqualTo(4);
      assertThat(listFound.contains(added)).isTrue();

      bookingService.deleteById(added.getId());
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Booking by his User")
   void findAllFiltered_returnOnlyOneBooking_ofExistingUser() {
      List<BookingDTO> found;

      for(BookingDTO dto:allBookingDTOS) {
         Mockito.lenient().when(mediaService.findFreeByEan(anyString())).thenReturn(dto.getMedia());

         BookingDTO filter = new BookingDTO();
         filter.setUser(dto.getUser());
         filter.setMedia(dto.getMedia());

         found = bookingService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(dto);
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Booking by User")
   void getFirstId_returnFirstId_ofFilteredBookingByUser() {
      BookingDTO filter = new BookingDTO();


      filter.setUser(allBookingDTOS.get(1).getUser());
      filter.setMedia(allBookingDTOS.get(1).getMedia());

      Integer id = bookingService.getFirstId(filter);

      assertThat(id).isEqualTo(2);
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Booking")
   void save_returnCreatedBooking_ofNewBooking() {

      BookingDTO saved = bookingService.save(newBookingDTO);
      Integer id = saved.getId();

      assertThat(bookingService.existsById(id)).isTrue();
      bookingService.deleteById(id);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update a Booking")
   void update_returnUpdatedBooking_ofBookingAndNewPickDate() {
      BookingDTO bookingDTO = bookingService.findById(2);
      Date oldDate = (Date) bookingDTO.getPickUpDate();

      BookingDTO saved = bookingService.update(bookingDTO);
      assertThat(saved).isEqualTo(bookingDTO);
      BookingDTO found = bookingService.findById(2);
      assertThat(found).isEqualTo(bookingDTO);

      bookingDTO.setPickUpDate(oldDate);
      bookingService.update(bookingDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Booking by his ID")
   void deleteById_returnExceptionWhenGetBookingById_ofDeletedBookingById() {
      BookingDTO saved = bookingService.save(newBookingDTO);
      Integer id = saved.getId();

      assertThat(bookingService.existsById(id)).isTrue();
      bookingService.deleteById(id);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
              ()-> bookingService.findById(id));
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Bookings")
   void count_returnTheNumberOfBookings() {
      assertThat(bookingService.count()).isEqualTo(3);

      // add one borrowing to increase the list
      BookingDTO added = bookingService.save(newBookingDTO);
      assertThat(bookingService.count()).isEqualTo(4);

      bookingService.deleteById(added.getId());
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Booking Entity is converted in right Booking DTO")
   void entityToDTO_returnBookingDTO_ofBookingEntity() {
      List<Booking> bookings = new ArrayList<>();
      BookingDTO dto;

      for(BookingDTO b : allBookingDTOS) {
         bookings.add(bookingService.dtoToEntity(b));
      }

      for(Booking entity : bookings){
         dto = bookingService.entityToDTO(entity);
         assertThat(dto.getId()).isEqualTo(entity.getId());
         assertThat(dto.getUser().getId()).isEqualTo(entity.getUserId());
         assertThat(dto.getMedia().getEan()).isEqualTo(entity.getEan());
         assertThat(dto.getBookingDate()).isEqualTo(entity.getBookingDate());
         assertThat(dto.getPickUpDate()).isEqualTo(entity.getPickUpDate());
         assertThat(dto.getMediaId()).isEqualTo(entity.getMediaId());
         assertThat(dto.getRank()).isEqualTo(entity.getRank());
      }

   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Book Booking is converted in right Booking Entity")
   void dtoToEntity_returnBookingEntity_ofBookingDTO() {
      Booking entity;

      for(BookingDTO dto : allBookingDTOS) {
         entity = bookingService.dtoToEntity(dto);
         assertThat(entity.getId()).isEqualTo(dto.getId());
         assertThat(entity.getUserId()).isEqualTo(dto.getUser().getId());
         assertThat(entity.getEan()).isEqualTo(dto.getMedia().getEan());
         assertThat(entity.getBookingDate()).isEqualTo(dto.getBookingDate());
         assertThat(entity.getPickUpDate()).isEqualTo(dto.getPickUpDate());
         assertThat(entity.getMediaId()).isEqualTo(dto.getMediaId());
         assertThat(entity.getRank()).isEqualTo(dto.getRank());
      }
   }

   @Test
   @Tag("findByEanAndUserId")
   @DisplayName("Verify that we can found a Booking by User Id and Media EAN")
   void findByEanAndUserId_returnBooking_ofUserIdAndMediaEAN() {
      for (BookingDTO dto : allBookingDTOS) {
         BookingDTO found = bookingService.findByEanAndUserId(dto.getMedia().getEan(),dto.getUser().getId());
         assertThat(found).isEqualTo(dto);
      }
   }

   @Test
   @Tag("findNextBookingByMediaId")
   @DisplayName("Verify that we can found the next Booking for a Media EAN")
   void findNextBookingByMediaId_returnNextBooking_forMediaEAN() {
      String ean = "4988064585816";
      BookingDTO toBeRemoved = bookingService.findNextBookingByMediaId(ean);

      assertThat(toBeRemoved).isEqualTo(bookingService.findById(1));

      bookingService.deleteById(1);

      BookingDTO found = bookingService.findNextBookingByMediaId(ean);

      assertThat(found).isEqualTo(bookingService.findById(2));

      bookingService.save(toBeRemoved);
   }

   @Test
   @Tag("booking")
   @DisplayName("Verify that a Booking is recorded")
   void booking_returnBooking_ofUserAndMedia() {
      UserDTO userDTO = allUserDTOS.get(2);
      userDTO.setStatus(UserStatus.BORROWER.name());
      MediaDTO mediaDTO = allMediaDTOS.get(9);

      BookingDTO bookingDTO = bookingService.booking(userDTO.getId(),mediaDTO.getEan());
      assertThat(bookingDTO).isNotNull();
      assertThat(bookingDTO.getUser()).isEqualTo(userDTO);
      // compare only EAN not Media
      assertThat(bookingDTO.getMedia().getEan()).isEqualTo(mediaDTO.getEan());
      assertThat(bookingDTO.getBookingDate()).isEqualToIgnoringHours(Calendar.getInstance().getTime());

      bookingService.cancelBooking(bookingDTO.getId());

   }


   @Test
   @Tag("booking")
   @DisplayName("Verify that a FORBIDDEN User can't Book")
   void booking_throwForbiddenException_ofForbidenUser() {
      UserDTO userDTO = allUserDTOS.get(2);
      String oldStatus = userDTO.getStatus();
      userDTO.setStatus(UserStatus.FORBIDDEN.name());
      MediaDTO mediaDTO = allMediaDTOS.get(9);
      Integer userId = userDTO.getId();
      String mediaEan = mediaDTO.getEan();

      Assertions.assertThrows(ForbiddenException.class,
              () -> bookingService.booking(userId,mediaEan));

      userDTO.setStatus(oldStatus);
   }

   @Test
   @Tag("booking")
   @DisplayName("Verify that a BAN User can't Book")
   void booking_throwForbiddenException_ofBannedUser() {
      UserDTO userDTO = allUserDTOS.get(2);
      String oldStatus = userDTO.getStatus();
      userDTO.setStatus(UserStatus.BAN.name());
      MediaDTO mediaDTO = allMediaDTOS.get(9);
      Integer userId = userDTO.getId();
      String mediaEan = mediaDTO.getEan();


      Assertions.assertThrows(ForbiddenException.class,
            () -> bookingService.booking(userId,mediaEan));

      userDTO.setStatus(oldStatus);
   }

   @Test
   @Tag("booking")
   @DisplayName("Verify that a User can't Book a Media Borrowed by himself")
   void booking_throwForbiddenException_ofMediaBorrowedBySameUser() {
      UserDTO userDTO = allUserDTOS.get(5);
      userDTO.setStatus(UserStatus.BORROWER.name());
      MediaDTO mediaDTO = allMediaDTOS.get(10);
      Integer userId = userDTO.getId();
      String mediaEan = mediaDTO.getEan();

      Assertions.assertThrows(ForbiddenException.class,
            () -> bookingService.booking(userId,mediaEan));

   }

   @Test
   @Tag("booking")
   @DisplayName("Verify that a User can't Book a Media Booked by himself")
   void booking_throwForbiddenException_ofMediaBookedBySameUser() {
      UserDTO userDTO = allUserDTOS.get(5);
      userDTO.setStatus(UserStatus.BORROWER.name());
      MediaDTO mediaDTO = allMediaDTOS.get(1);
      Integer userId = userDTO.getId();
      String mediaEan = mediaDTO.getEan();

      Assertions.assertThrows(ForbiddenException.class,
            () -> bookingService.booking(userId,mediaEan));

   }

   @Test
   @Tag("findBookingsByUserId")
   @DisplayName("Verify that we can found the Booking list of a User")
   void findBookingsByUserId_returnBookingList_ofOneUser() {
      List<BookingDTO> founds = bookingService.findBookingsByUserId(5);
      List<BookingDTO> expected = new ArrayList<>();
      expected.add(allBookingDTOS.get(1));
      expected.add(allBookingDTOS.get(2));

      assertThat(founds.size()).isEqualTo(expected.size());
      for(BookingDTO b : founds) {
         assertThat(expected.contains(b)).isTrue();
      }
   }

   @Test
   @Tag("cancelBooking")
   @DisplayName("Verify that we can cancel a Booking")
   void cancelBooking_throwResourceNotFoundException_ofCancelledBooking() {
      UserDTO userDTO = allUserDTOS.get(2);
      userDTO.setStatus(UserStatus.BORROWER.name());
      MediaDTO mediaDTO = allMediaDTOS.get(9);

      BookingDTO bookingDTO = bookingService.booking(userDTO.getId(),mediaDTO.getEan());
      Integer bookingId = bookingDTO.getId();

      bookingDTO = bookingService.cancelBooking(bookingId);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> bookingService.findById(bookingId));
   }

   @Test
   @Tag("findReadyToPickUp")
   @DisplayName("Verify that we have the list of Media to be PickUp")
   void findReadyToPickUp() {
      UserDTO userDTO = allUserDTOS.get(2);
      userDTO.setStatus(UserStatus.BORROWER.name());
      MediaDTO mediaDTO = allMediaDTOS.get(9);
      BookingDTO bookingDTO = bookingService.booking(userDTO.getId(),mediaDTO.getEan());
      Integer bookingId = bookingDTO.getId();
      Calendar calendar= Calendar.getInstance();
      calendar.add(Calendar.DATE,2);
           // ;;
      bookingService.updatePickUpDate(bookingId,calendar.getTime());

      bookingDTO = bookingService.findById(bookingId);
      List<BookingDTO> founds = bookingService.findReadyToPickUp();

      assertThat(founds.size()).isEqualTo(1);
      assertThat(founds.get(0)).isEqualTo(bookingDTO);
      bookingDTO = bookingService.cancelBooking(bookingId);

   }

   @Test
   @Tag("updatePickUpDate")
   @DisplayName("Verify that we have the list of Media to be PickUp")
   void updatePickUpDate() {
      BookingDTO bookingDTO = bookingService.findById(1);
      Date oldPickUpDate = (Date) bookingDTO.getPickUpDate();
      Date newDate = Date.valueOf("2020-08-11");

      bookingService.updatePickUpDate(bookingDTO.getId(),newDate);

      bookingDTO = bookingService.findById(1);
      assertThat(bookingDTO.getPickUpDate()).isEqualToIgnoringHours(newDate);
      bookingService.updatePickUpDate(bookingDTO.getId(),oldPickUpDate);
      bookingDTO = bookingService.findById(1);
   }

}
