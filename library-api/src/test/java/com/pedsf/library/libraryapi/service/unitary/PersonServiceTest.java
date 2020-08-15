package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.Person;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.repository.PersonSpecification;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class PersonServiceTest {
   private static final List<Person> allPersons = new ArrayList<>();

   @Mock
   private PersonRepository personRepository;
   private PersonService personService;
   private static Person newPerson;
   private static PersonDTO newPersonDTO;

   @BeforeAll
   static void beforeAll() {
      allPersons.add( new Person(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersons.add( new Person(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersons.add( new Person(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersons.add( new Person(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersons.add( new Person(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersons.add( new Person(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersons.add( new Person(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersons.add( new Person(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersons.add( new Person(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersons.add( new Person(10,"LGF","Librairie Générale Française",null));
      allPersons.add( new Person(11,"Gallimard","Gallimard",null));
      allPersons.add( new Person(12,"Larousse","Larousse",null));
      allPersons.add( new Person(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersons.add( new Person(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersons.add( new Person(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersons.add( new Person(16,"Microsoft","Microsoft",null));
   }

   @BeforeEach
   void beforeEach() {
      personService = new PersonService(personRepository);
      newPerson = new Person(55,"Jean","MARTIN",Date.valueOf("1979-07-11"));
      newPersonDTO = new PersonDTO(55,"Jean","MARTIN",Date.valueOf("1979-07-11"));

   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no Person")
   void findAll_throwResourceNotFoundException_ofEmptyList() {
      List<Person> emptyList = new ArrayList<>();
      Mockito.lenient().when(personRepository.findAll()).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> personService.findAll());
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no Person in filtered list")
   void findAllFiltered_throwResourceNotFoundException_ofEmptyList() {
      List<Person> emptyList = new ArrayList<>();
      Mockito.lenient().when(personRepository.findAll(any(PersonSpecification.class))).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class,
            () -> personService.findAllFiltered(newPersonDTO));
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Person")
   void getFirstId_returnFirstPersonId_ofPersonList() {
      List<Person> filteredList = allPersons.subList(3,9);
      Mockito.lenient().when(personRepository.findAll(any(PersonSpecification.class))).thenReturn(filteredList);

      // add one book to increase the list
      assertThat( personService.getFirstId(new PersonDTO())).isEqualTo(4);
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Person with has no firstName")
   void save_throwBadRequestException_ofNewPersonWithNoFirstname() {
      PersonDTO personDTO = new PersonDTO();
      personDTO.setLastName(allPersons.get(3).getLastName());

      Assertions.assertThrows(BadRequestException.class, ()-> personService.save(personDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Person with no lastName")
   void save_throwBadRequestException_ofNewPersonWithNoLastname() {
      PersonDTO personDTO = new PersonDTO();
      personDTO.setFirstName(allPersons.get(3).getFirstName());

      Assertions.assertThrows(BadRequestException.class, ()-> personService.save(personDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ResourceNotFoundException when updating a Person with bad ID")
   void update_throwResourceNotFoundException_ofPersonWithBadID() {
      PersonDTO personDTO = new PersonDTO();
      personDTO.setId(123);
      personDTO.setFirstName(allPersons.get(4).getFirstName());
      personDTO.setLastName(allPersons.get(4).getLastName());
      personDTO.setBirthDate(allPersons.get(4).getBirthDate());

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> personService.update(personDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when updating a Person with no firstName")
   void update_throwConflictException_ofPersonWithNoFirstname() {
      PersonDTO personDTO = new PersonDTO();
      personDTO.setLastName(allPersons.get(3).getLastName());

      Assertions.assertThrows(ConflictException.class, ()-> personService.update(personDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when updating a Person with no lastName")
   void update_throwConflictException_ofPersonWithNoLastname() {
      PersonDTO personDTO = new PersonDTO();
      personDTO.setFirstName(allPersons.get(3).getFirstName());

      Assertions.assertThrows(ConflictException.class, ()-> personService.update(personDTO));
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we have ResourceNotFoundException when deleting a Person with bad ID")
   void deleteById_throwResourceNotFoundException_ofPersonWithBadID() {

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> personService.deleteById(123));
   }

}
