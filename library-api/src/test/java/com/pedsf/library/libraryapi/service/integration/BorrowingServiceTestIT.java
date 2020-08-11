package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.BorrowingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.libraryapi.model.Borrowing;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.BookingRepository;
import com.pedsf.library.libraryapi.repository.BorrowingRepository;
import com.pedsf.library.libraryapi.repository.MediaRepository;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BorrowingServiceTestIT {
   private static BorrowingService borrowingService;
   @Mock
   private static MediaService mediaService;
   @Mock
   private UserApiProxy userApiProxy;
   private static Borrowing newBorrowing;
   private static BorrowingDTO newBorrowingDTO;
   private static List<BorrowingDTO> allBorrowingDTOS;
   private static List<UserDTO> allUserDTOS = new ArrayList<>();
   private static List<MediaDTO> allMediaDTOS = new ArrayList<>();


   @BeforeAll
   static void beforeAll(@Autowired MediaRepository mediaRepository) {

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

   }

   @BeforeEach
   void beforeEach(@Autowired BorrowingRepository borrowingRepository, @Autowired BookingRepository bookingRepository) {
      borrowingService = new BorrowingService(borrowingRepository, bookingRepository,mediaService, userApiProxy);

      Mockito.lenient().when(userApiProxy.findUserById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allUserDTOS.get((Integer) invocation.getArguments()[0]-1));

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
   void findAllFiltered() {
   }

   @Test
   void getFirstId() {
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
      BorrowingDTO dto;



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
   void userHadBorrowed() {
   }

   @Test
   void borrow() {
   }

   @Test
   void restitute() {
   }

   @Test
   void findDelayed() {
   }

   @Test
   void findByUserIdNotReturn() {
   }

   @Test
   void extend() {
   }
}