package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.business.BorrowingDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.model.Borrowing;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.BookingRepository;
import com.pedsf.library.libraryapi.repository.BorrowingRepository;
import com.pedsf.library.libraryapi.service.BorrowingService;
import com.pedsf.library.libraryapi.service.MediaService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;

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

    @BeforeAll
    static void beforeAll() {
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

        Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
                (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]));
    }

    @Test
    @Tag("findAll")
    @DisplayName("Verify that we have ResourceNotFoundException if there is no Borrowing")
    void findAll_throwResourceNotFoundException_ofEmptyList() {
        List<Borrowing> emptyList = new ArrayList<>();
        Mockito.lenient().when(borrowingRepository.findAll()).thenReturn(emptyList);

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> borrowingService.findAll());
    }
}
