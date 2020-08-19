package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BorrowingServiceTest {

    @Value("${library.borrowing.delay}")
    private int daysOfDelay;
    @Value("${library.borrowing.extention.max}")
    private int maxExtention;
    @Value("${library.borrowing.quantity.max}")
    private int quantityMax;

    private static final Map<Integer,PersonDTO> allPersons = new HashMap<>();

    @Mock
    private PersonService personService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private static MediaService mediaService;
    @Mock
    private UserApiProxy userApiProxy;
    @Mock
    private BorrowingRepository borrowingRepository;
    private BorrowingService borrowingService;
    private static Borrowing newBorrowing;
    private static BorrowingDTO newBorrowingDTO;
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

        allPersons.put(1, new PersonDTO(1, "Emile", "ZOLA", Date.valueOf("1840-04-02")));
        allPersons.put(2, new PersonDTO(2, "Gustave", "FLAUBERT", Date.valueOf("1821-12-12")));
        allPersons.put(3, new PersonDTO(3, "Victor", "HUGO", Date.valueOf("1802-02-26")));
        allPersons.put(4, new PersonDTO(4, "Joon-Ho", "BONG", Date.valueOf("1969-09-14")));
        allPersons.put(5, new PersonDTO(5, "Sun-Kyun", "LEE", Date.valueOf("1975-03-02")));
        allPersons.put(6, new PersonDTO(6, "Kang-Ho", "SONG", Date.valueOf("1967-01-17")));
        allPersons.put(7, new PersonDTO(7, "Yeo-Jeong", "CHO", Date.valueOf("1981-02-10")));
        allPersons.put(8, new PersonDTO(8, "Woo-Shik", "CHOI", Date.valueOf("1986-03-26")));
        allPersons.put(9, new PersonDTO(9, "So-Dam", "PARK", Date.valueOf("1991-09-08")));
        allPersons.put(10, new PersonDTO(10, "LGF", "Librairie Générale Française", null));
        allPersons.put(11, new PersonDTO(11, "Gallimard", "Gallimard", null));
        allPersons.put(12, new PersonDTO(12, "Larousse", "Larousse", null));
        allPersons.put(13, new PersonDTO(13, "Blackpink", "Blackpink", Date.valueOf("2016-06-01")));
        allPersons.put(14, new PersonDTO(14, "BigBang", "BigBang", Date.valueOf("2006-08-19")));
        allPersons.put(15, new PersonDTO(15, "EA", "Electronic Arts", Date.valueOf("1982-05-28")));
        allPersons.put(16, new PersonDTO(16, "Microsoft", "Microsoft", null));


    }

    @BeforeEach
    void beforeEach() {
        borrowingService = new BorrowingService(borrowingRepository,bookingRepository,mediaService,userApiProxy);
        borrowingService.setDaysOfDelay(daysOfDelay);
        borrowingService.setMaxExtention(maxExtention);

        Mockito.lenient().when(userApiProxy.findUserById(anyInt())).thenAnswer(
              (InvocationOnMock invocation) -> allUserDTOS.get((Integer) invocation.getArguments()[0]));

        Mockito.lenient().when(mediaService.findById(anyInt())).thenAnswer(
              (InvocationOnMock invocation) -> allMediaDTOS.get((Integer) invocation.getArguments()[0]-1));

        Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
              (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]));

        newBorrowing = new Borrowing(33,2,11, Date.valueOf("2020-08-10"),Date.valueOf("2020-09-07"));
        UserDTO userDTO = userApiProxy.findUserById(2);
        MediaDTO mediaDTO = mediaService.findById(11);
        newBorrowingDTO = new BorrowingDTO(33,userDTO,mediaDTO, Date.valueOf("2020-08-10"),Date.valueOf("2020-09-07"));

    }

    @Test
    @Tag("init")
    @DisplayName("Verify that paramerters are read from property file")
    void init_parameter() {
        assertThat(maxExtention).isEqualTo(5);
        assertThat(quantityMax).isEqualTo(5);
        assertThat(daysOfDelay).isEqualTo(14);
        assertThat(bookingRepository).isNotNull();
        assertThat(borrowingRepository).isNotNull();
        assertThat(mediaService).isNotNull();
        assertThat(userApiProxy).isNotNull();
    }

    @Test
    @Tag("findAll")
    @DisplayName("Verify that we have ResourceNotFoundException if there is no Borrowing")
    void findAll_throwResourceNotFoundException_ofEmptyList() {
        List<Borrowing> emptyList = new ArrayList<>();
        Mockito.lenient().when(borrowingRepository.findAll()).thenReturn(emptyList);

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.findAll());
    }

    @Test
    @Tag("findAllFiltered")
    @DisplayName("Verify that we have ResourceNotFoundException if there is no Borrowing")
    void findAllFiltered_throwResourceNotFoundException_ofEmptyList() {
        List<Borrowing> emptyList = new ArrayList<>();
        Mockito.lenient().when(borrowingRepository.findAll(any(BorrowingSpecification.class))).thenReturn(emptyList);

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.findAllFiltered(new BorrowingDTO()));
    }


    @Test
    @Tag("save")
    @DisplayName("Verify that we have BadRequestException when saving a Borrowing with has no User")
    void save_throwBadRequestException_ofNewBorrowingWithNoUser() {
        newBorrowingDTO.setUser(null);
        Assertions.assertThrows(BadRequestException.class, ()-> borrowingService.save(newBorrowingDTO));
    }

    @Test
    @Tag("save")
    @DisplayName("Verify that we have BadRequestException when saving a Borrowing with has no Media")
    void save_throwBadRequestException_ofNewBorrowingWithNoMedia() {
        newBorrowingDTO.setMedia(null);
        Assertions.assertThrows(BadRequestException.class, ()-> borrowingService.save(newBorrowingDTO));
    }

    @Test
    @Tag("update")
    @DisplayName("Verify that we have ConflictException when update a Borrowing with no ID")
    void update_throwConflictException_ofBorrowingWithNoID() {
        newBorrowingDTO.setId(null);
        Assertions.assertThrows(ConflictException.class, ()-> borrowingService.update(newBorrowingDTO));
    }

    @Test
    @Tag("update")
    @DisplayName("Verify that we have ConflictException when update a Borrowing with has no User")
    void update_throwConflictException_ofBorrowingWithNoUser() {
        newBorrowingDTO.setUser(null);
        Assertions.assertThrows(ConflictException.class, ()-> borrowingService.update(newBorrowingDTO));
    }

    @Test
    @Tag("update")
    @DisplayName("Verify that we have ConflictException when update a Borrowing with has no Media")
    void update_throwConflictException_ofBorrowingWithNoMedia() {
        newBorrowingDTO.setMedia(null);
        Assertions.assertThrows(ConflictException.class, ()-> borrowingService.update(newBorrowingDTO));
    }

    @Test
    @Tag("update")
    @DisplayName("Verify that we have ResourceNotFoundException when update a Borrowing with bad ID")
    void update_throwResourceNotFoundException_ofNewBorrowingWithWrongId() {
        newBorrowingDTO.setId(654);
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.update(newBorrowingDTO));
    }

    @Test
    @Tag("deleteById")
    @DisplayName("Verify that we have ResourceNotFoundException when deleting a Borrowing with bad ID")
    void deleteById_throwResourceNotFoundException_ofBorrowingWithBadId() {

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.deleteById(6546));
    }

    @Test
    @Tag("count")
    @DisplayName("Verify that we have the right number of Borrowings")
    void count_returnTheNumberOfBorrowings() {
        Mockito.lenient().when(borrowingRepository.count()).thenReturn(7L);
        assertThat(borrowingService.count()).isEqualTo(7);
    }

    @Test
    @Tag("findDelayed")
    @DisplayName("Verify that we have ResourceNotFoundException if there is no Borrowing")
    void findDelayed_throwResourceNotFoundException_ofEmptyList() {
        List<Borrowing> emptyList = new ArrayList<>();
        Mockito.lenient().when(borrowingRepository.findDelayed(any(Date.class),anyInt(),anyInt())).thenReturn(emptyList);
        Date newDate = Date.valueOf("1900-01-01");

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.findDelayed(newDate));
    }


    @Test
    @Tag("findByUserIdNotReturn")
    @DisplayName("Verify that we have ResourceNotFoundException if there is no Borrowing for the user")
    void findByUserIdNotReturn_throwResourceNotFoundException_ofEmptyList() {
        List<Borrowing> emptyList = new ArrayList<>();
        Mockito.lenient().when(borrowingRepository.findByUserIdNotReturn(anyInt())).thenReturn(emptyList);

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.findByUserIdNotReturn(987));
    }

    @Test
    @Tag("extend")
    @DisplayName("Verify that we have ResourceNotFoundException if there is no Borrowing")
    void extend_throwResourceNotFoundException_ofWrongUserIDAndMediaId() {

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.extend(987,45));
    }


}
