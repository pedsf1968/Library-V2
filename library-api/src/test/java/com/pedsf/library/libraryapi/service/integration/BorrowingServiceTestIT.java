package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.ForbiddenException;
import com.pedsf.library.libraryapi.model.Borrowing;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.*;
import com.pedsf.library.libraryapi.service.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BorrowingServiceTestIT {

   @Value("${library.borrowing.delay}")
   private int daysOfDelay;
   @Value("${library.borrowing.extention.max}")
   private int maxExtention;
   @Value("${library.borrowing.quantity.max}")
   private int quantityMax;

   private static BorrowingService borrowingService;
   private BorrowingRepository borrowingRepository;

   @Mock
   private static MediaService mediaService;
   @Mock
   private UserApiProxy userApiProxy;
   private static Borrowing newBorrowing;
   private static BorrowingDTO newBorrowingDTO;
   private static List<BorrowingDTO> allBorrowingDTOS = new ArrayList<>();
   private static Map<Integer,UserDTO> allUserDTOS = new HashMap<>();
   private static List<MediaDTO> allMediaDTOS = new ArrayList<>();


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

   @BeforeEach
   void beforeEach(@Autowired BorrowingRepository borrowingRepository, @Autowired BookingRepository bookingRepository) {
      borrowingService = new BorrowingService(borrowingRepository, bookingRepository,mediaService, userApiProxy);
      borrowingService.setDaysOfDelay(daysOfDelay);
      borrowingService.setMaxExtention(maxExtention);

      Mockito.lenient().when(userApiProxy.findUserById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allUserDTOS.get((Integer) invocation.getArguments()[0]));

      Mockito.lenient().when(mediaService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allMediaDTOS.get((Integer) invocation.getArguments()[0]-1));

      newBorrowing = new Borrowing(33,2,11, Date.valueOf("2020-08-10"),Date.valueOf("2020-09-07"));
      UserDTO userDTO = userApiProxy.findUserById(2);
      MediaDTO mediaDTO = mediaService.findById(11);
      newBorrowingDTO = new BorrowingDTO(33,userDTO,mediaDTO, Date.valueOf("2020-08-10"),Date.valueOf("2020-09-07"));

      allBorrowingDTOS = borrowingService.findAll();
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Borrowing exist")
   void existsById_returnTrue_OfExistingBorrowing() {
      for(BorrowingDTO borrowingDTO : allBorrowingDTOS) {
         assertThat(borrowingService.existsById(borrowingDTO.getId())).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Borrowing doesn't exist")
   void existsById_returnFalse_ofInexistingBorrowing() {
      assertThat(borrowingService.existsById(55)).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Borrowing by is ID")
   void findById_returnBorrowing_ofExistingBorrowingId() {
      BorrowingDTO found;

      for(BorrowingDTO borrowingDTO : allBorrowingDTOS) {
         Integer id = borrowingDTO.getId();
         found = borrowingService.findById(id);

         assertThat(found).isEqualTo(borrowingDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Borrowing with wrong ID")
   void findById_returnException_ofInexistingBorrowingId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
              ()-> borrowingService.findById(55));
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Borrowing")
   void findAll() {
      assertThat(allBorrowingDTOS.size()).isEqualTo(9);

      // add one borrowing to increase the list
      BorrowingDTO added = borrowingService.save(newBorrowingDTO);
      List<BorrowingDTO> listFound = borrowingService.findAll();
      assertThat(listFound.size()).isEqualTo(10);
      assertThat(listFound.contains(added)).isTrue();

      borrowingService.deleteById(added.getId());
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Borrowing by his User and Media")
   void findAllFiltered_returnOnlyOneBorrowing_ofExistingUserAndMedia() {
      List<BorrowingDTO> found;

      for(BorrowingDTO b:allBorrowingDTOS) {
         BorrowingDTO filter = new BorrowingDTO();
         filter.setUser(b.getUser());
         filter.setMedia(b.getMedia());

         found = borrowingService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(b);
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Borrowings by User")
   void getFirstId_returnFirstId_ofFilteredBorrowingByUser() {
      BorrowingDTO filter = new BorrowingDTO();

      filter.setUser(allUserDTOS.get(4));

      Integer id = borrowingService.getFirstId(filter);

      assertThat(id).isEqualTo(1);
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Borrowing")
   void save_returnCreatedBorrowing_ofNewBorrowing() {

      BorrowingDTO saved = borrowingService.save(newBorrowingDTO);
      Integer id = saved.getId();

      assertThat(borrowingService.existsById(id)).isTrue();
      borrowingService.deleteById(id);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update a Borrowing")
   void update_returnUpdatedBorrowing_ofBorrowingAndNewTitle() {
      BorrowingDTO borrowingDTO = borrowingService.findById(3);
      Date oldDate = (Date) borrowingDTO.getBorrowingDate();

      BorrowingDTO saved = borrowingService.update(borrowingDTO);
      assertThat(saved).isEqualTo(borrowingDTO);
      BorrowingDTO found = borrowingService.findById(3);
      assertThat(found).isEqualTo(borrowingDTO);

      borrowingDTO.setBorrowingDate(oldDate);
      borrowingService.update(borrowingDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Borrowing by his ID")
   void deleteById_returnExceptionWhenGetBorrowingById_ofDeletedBorrowingById() {
      BorrowingDTO saved = borrowingService.save(newBorrowingDTO);
      Integer id = saved.getId();

      assertThat(borrowingService.existsById(id)).isTrue();
      borrowingService.deleteById(id);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
              ()-> borrowingService.findById(id));
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Borrowings")
   void count_returnTheNumberOfBorrowings() {
      assertThat(borrowingService.count()).isEqualTo(9);

      // add one borrowing to increase the list
      BorrowingDTO added = borrowingService.save(newBorrowingDTO);
      assertThat(borrowingService.count()).isEqualTo(10);

      borrowingService.deleteById(added.getId());
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Borrowing Entity is converted in right Borrowing DTO")
   void entityToDTO_returnBorrowingDTO_ofBorrowingEntity() {
      List<Borrowing> borrowings = new ArrayList<>();
      BorrowingDTO dto;

      for(BorrowingDTO b : allBorrowingDTOS) {
         borrowings.add(borrowingService.dtoToEntity(b));
      }

      for(Borrowing entity : borrowings){
         dto = borrowingService.entityToDTO(entity);
         assertThat(dto.getId()).isEqualTo(entity.getId());
         assertThat(dto.getUser().getId()).isEqualTo(entity.getUserId());
         assertThat(dto.getMedia().getId()).isEqualTo(entity.getMediaId());
         assertThat(dto.getBorrowingDate()).isEqualTo(entity.getBorrowingDate());
         assertThat(dto.getReturnDate()).isEqualTo(entity.getReturnDate());
         assertThat(dto.getExtended()).isEqualTo(entity.getExtended());
      }

   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Book Borrowing is converted in right Borrowing Entity")
   void dtoToEntity_returnBorrowingEntity_ofBorrowingDTO() {
      Borrowing entity;

      for(BorrowingDTO dto : allBorrowingDTOS) {
         entity = borrowingService.dtoToEntity(dto);
         assertThat(entity.getId()).isEqualTo(dto.getId());
         assertThat(entity.getUserId()).isEqualTo(dto.getUser().getId());
         assertThat(entity.getMediaId()).isEqualTo(dto.getMedia().getId());
         assertThat(entity.getBorrowingDate()).isEqualTo(dto.getBorrowingDate());
         assertThat(entity.getReturnDate()).isEqualTo(dto.getReturnDate());
         assertThat(entity.getExtended()).isEqualTo(dto.getExtended());
      }
   }

   @Test
   @Tag("userHadBorrowed")
   @DisplayName("Verify that return TRUE when User has borrowed a media")
   void userHadBorrowed_returnTrue_ofUserAndBorrowedMedia() {
      for(BorrowingDTO borrowingDTO : allBorrowingDTOS) {
         assertThat(borrowingService.userHadBorrowed(borrowingDTO.getUser().getId(),borrowingDTO.getMedia().getEan())).isTrue();
      }
   }

   @Test
   @Tag("userHadBorrowed")
   @DisplayName("Verify that return FALSE when User has not borrowed a media")
   void userHadBorrowed_returnFalse_ofUserAndNotBorrowedMedia() {
      assertThat(borrowingService.userHadBorrowed(2,"MediaEAN")).isFalse();
   }


   @Test
   @Tag("borrow")
   @DisplayName("Verify that a user can't borrow more than QuantityMax")
   void borrow_throwForbiddenException_ofUserCounterGreaterThanQuantityMax() {
      UserDTO userDTO = allUserDTOS.get(2);
      Integer userId = userDTO.getId();
      Integer oldCounter = userDTO.getCounter();
      userDTO.setCounter(quantityMax+1);

      Assertions.assertThrows(ForbiddenException.class,
            ()->borrowingService.borrow(userId,"lkjhlkjh"));

      userDTO.setCounter(oldCounter);
   }

   @Test
   @Tag("borrow")
   @DisplayName("Verify that a FORBIDDEN user can't borrow")
   void borrow_throwForbiddenException_ofUserFORBIDDEN() {
      UserDTO userDTO = allUserDTOS.get(2);
      Integer userId = userDTO.getId();
      String oldStatus = userDTO.getStatus();
      userDTO.setStatus(UserStatus.FORBIDDEN.name());

      Assertions.assertThrows(ForbiddenException.class,
            ()->borrowingService.borrow(userId,"8809269506764"));

      userDTO.setStatus(oldStatus);
   }

   @Test
   @Tag("borrow")
   @DisplayName("Verify that a BAN user can't borrow")
   void borrow_throwForbiddenException_ofUserBAN() {
      UserDTO userDTO = allUserDTOS.get(2);
      Integer userId = userDTO.getId();
      String oldStatus = userDTO.getStatus();
      userDTO.setStatus(UserStatus.BAN.name());

      Assertions.assertThrows(ForbiddenException.class,
            ()->borrowingService.borrow(userId,"8809269506764"));

      userDTO.setStatus(oldStatus);
   }

   @Test
   @Tag("borrow")
   @DisplayName("Verify that we can't borrow an Media not FREE")
   void borrow_throwForbiddenException_ofNotFreeMedia() {
      UserDTO userDTO = allUserDTOS.get(2);
      Integer userId = userDTO.getId();

      Assertions.assertThrows(ForbiddenException.class,
            ()->borrowingService.borrow(userId,"978-2253002864"));
   }

   @Test
   @Tag("borrow")
   @DisplayName("Verify that we can't borrow a media with a not positive stock")
   void borrow_throwForbiddenException_ofNotPositiveStock() {
      UserDTO userDTO = allUserDTOS.get(2);
      Integer userId = userDTO.getId();
      MediaDTO mediaDTO = allMediaDTOS.get(27);
      String ean = mediaDTO.getEan();
      Integer oldStock = mediaDTO.getStock();
      mediaDTO.setStock(0);


      Assertions.assertThrows(ForbiddenException.class,
            ()->borrowingService.borrow(userId,ean));

      mediaDTO.setStock(oldStock);
   }

   @Test
   @Tag("restitute")
   @DisplayName("Verify that the media is release when we restitute")
   void restitute_setMediaFree_ofMediaBorrowed() {
      UserDTO userDTO = allBorrowingDTOS.get(4).getUser();
      Integer userId = userDTO.getId();
      MediaDTO mediaDTO = allBorrowingDTOS.get(4).getMedia();
      String ean = mediaDTO.getEan();
      Integer mediaId = mediaDTO.getId();

      assertThat(mediaDTO.getStatus()).isEqualTo(MediaStatus.BORROWED.name());

      doAnswer(invocation -> {
         Integer id = invocation.getArgument(0);
         assertThat(id).isEqualTo(mediaId);
         mediaDTO.setStatus(MediaStatus.FREE.name());
         return null;
      }).when(mediaService).release(any(Integer.class));
      borrowingService.restitute(userId,mediaId);

      assertThat(mediaDTO.getStatus()).isEqualTo(MediaStatus.FREE.name());

   }

   @Test
   @Tag("findDelayed")
   @DisplayName("Get the list of delayed Borrowing")
   void findDelayed() {


   }

   @Test
   @Tag("findByUserIdNotReturn")
   @DisplayName("Verify thawe get all media borrow by user 4")
   void findByUserIdNotReturn_returnBoorowingList_ofUser4() {
      List<BorrowingDTO> found;
      List<BorrowingDTO> expected = new ArrayList<>();
      Integer userId = 4;

      for(BorrowingDTO b: allBorrowingDTOS) {
         if(b.getUser().getId().equals(userId)) {
            expected.add(b);
         }
      }

      found = borrowingService.findByUserIdNotReturn(userId);

      assertThat(found.size()).isEqualTo(expected.size());

      for(BorrowingDTO b: found) {
         assertThat(expected.contains(b)).isTrue();
      }
   }

   @Test
   @Tag("extend")
   @DisplayName("Verify that extended counter is incremented")
   void extend() {
      BorrowingDTO expected = borrowingService.findById(6);
      Integer userId = expected.getUser().getId();
      Integer mediaId = expected.getMedia().getId();
      Integer oldExtended = expected.getExtended();
      Date oldDate = (Date) expected.getBorrowingDate();
      Date today = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
      expected.setBorrowingDate(today);

      borrowingService.update(expected);
      borrowingService.extend(userId,mediaId);
      BorrowingDTO found = borrowingService.findById(6);

      assertThat(found.getExtended()).isEqualTo(oldExtended+1);

      found.setBorrowingDate(oldDate);
      found.setExtended(oldExtended);

      borrowingService.update(found);
   }
}