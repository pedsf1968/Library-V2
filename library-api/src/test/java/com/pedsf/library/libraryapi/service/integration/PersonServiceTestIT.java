package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.model.Person;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class PersonServiceTestIT {
    private static final String PERSON_LASTNAME_TEST = "MARTIN";
    private static PersonService personService;
    private static Person newPerson;
    private static PersonDTO newPersonDTO;
    private static List<PersonDTO> allPersonDTOS;

    @BeforeAll
    static void beforeAll(@Autowired PersonRepository personRepository) {
        personService = new PersonService(personRepository);
    }

    @BeforeEach
    void beforeEach() {
        newPerson = new Person(33,"Edgar","POE", Date.valueOf("1956-05-07"));
        newPersonDTO = new PersonDTO(33,"Edgar","POE", Date.valueOf("1956-05-07"));
        allPersonDTOS = personService.findAll();
    }

    @Test
    @Tag("existsById")
    @DisplayName("Verify that return TRUE if the Person exist")
    void existsById_returnTrue_OfAnExistingPersonId() {
        for(PersonDTO personDTO : allPersonDTOS) {
            Integer personId = personDTO.getId();
            assertThat(personService.existsById(personId)).isTrue();
        }
    }

    @Test
    @Tag("existsById")
    @DisplayName("Verify that return FALSE if the Person doesn't exist")
    void existsById_returnFalse_OfAnInexistingPersonId() {
        assertThat(personService.existsById(333)).isFalse();
    }

    @Test
    @Tag("findById")
    @DisplayName("Verify that we can find Person by is ID")
    void findById_returnPerson_ofExistingPersonId() {
        PersonDTO found;

        for(PersonDTO personDTO : allPersonDTOS) {
            Integer personId = personDTO.getId();
            found = personService.findById(personId);

            assertThat(found).isEqualTo(personDTO);
        }
    }

    @Test
    @Tag("findById")
    @DisplayName("Verify that we can't find Person with wrong ID")
    void findById_throwException_ofInexistingPersonId() {
        Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
                ()->personService.findById(333));
    }

    @Disabled
    @Test
    @Tag("findAll")
    @DisplayName("Verify that we have the list of all Persons")
    void findAll_returnAllPersons() {
        assertThat(allPersonDTOS.size()).isEqualTo(16);

        // add one book to increase the list
        PersonDTO savedPerson = personService.save(newPersonDTO);
        List<PersonDTO> personDTOS = personService.findAll();
        assertThat(personDTOS.size()).isEqualTo(17);
        assertThat(personDTOS.contains(newPersonDTO)).isTrue();

        personService.deleteById(savedPerson.getId());
    }

    @Disabled
    @Test
    @Tag("save")
    @DisplayName("Verify that we can create a new Person")
    void save_returnCreatedPerson_ofNewPerson() {
        PersonDTO savedPerson = personService.save(newPersonDTO);
        Integer personId = savedPerson.getId();

        assertThat(personService.existsById(personId)).isTrue();
        personService.deleteById(personId);
    }

    @Test
    @Tag("update")
    @DisplayName("Verify that we can update a Person")
    void update_returnUpdatedPerson_ofPersonAndNewName() {
        PersonDTO personDTO = personService.findById(11);
        String oldLastName = personDTO.getLastName();
        personDTO.setLastName(PERSON_LASTNAME_TEST);

        PersonDTO updatedPerson = personService.update(personDTO);
        assertThat(updatedPerson).isEqualTo(personDTO);
        PersonDTO foundPerson = personService.findById(personDTO.getId());
        assertThat(foundPerson).isEqualTo(personDTO);

        personDTO.setLastName(oldLastName);
        personService.update(personDTO);
    }

    @Disabled
    @Test
    @Tag("deleteById")
    @DisplayName("Verify that we can delete a Person by his ID")
    void deleteById() {
        PersonDTO savedPerson = personService.save(newPersonDTO);
        Integer personId = savedPerson.getId();

        assertThat(personService.existsById(personId)).isTrue();
        personService.deleteById(personId);

        Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
                ()-> personService.findById(personId));
    }

    @Disabled
    @Test
    @Tag("count")
    @DisplayName("Verify that we have the right number of Persons")
    void count_returnTheNumberOfPersons() {
        assertThat(personService.count()).isEqualTo(16);

        // add an other Person
        PersonDTO savedPerson = personService.save(newPersonDTO);
        assertThat(personService.count()).isEqualTo(17);

        personService.deleteById(savedPerson.getId());
    }

    @Test
    @Tag("entityToDTO")
    @DisplayName("Verify that Person Entity is converted in Person Book DTO")
    void entityToDTO_returnRightPersonDTO_ofPersonEntity() {
        PersonDTO dto = personService.entityToDTO(newPerson);
        assertThat(dto).isEqualTo(newPersonDTO);
    }

    @Test
    @Tag("dtoToEntity")
    @DisplayName("Verify that Person DTO is converted in right Person Entity")
    void dtoToEntity_returnRightPersonEntity_ofPersonDTO() {
        Person entity = personService.dtoToEntity(newPersonDTO);
        assertThat(entity).isEqualTo(newPerson);
    }
}