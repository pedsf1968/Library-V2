package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.Person;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.repository.PersonSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("PersonService")
public class PersonService implements GenericService<PersonDTO,Person,Integer>{
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Person with the id : ";
   private static final String CANNOT_SAVE ="Failed to save Person";

   private final PersonRepository personRepository;

      private final ModelMapper modelMapper = new ModelMapper();

   public PersonService(PersonRepository personRepository) {
      this.personRepository = personRepository;
   }

   public boolean existsById(Integer id) {
      return personRepository.existsById(id);
   }

   public PersonDTO findById(Integer id) {
      Person person = personRepository.findById(id).orElse(null);

      if (person!=null) {
         return entityToDTO(person);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      }
   }

   public List<PersonDTO> findAll() {
      List<Person> persons = personRepository.findAll();
      List<PersonDTO> personDTOS = new ArrayList<>();

      for (Person person: persons){
         personDTOS.add(entityToDTO(person));
      }

      if (!personDTOS.isEmpty()) {
         return personDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }


   public List<PersonDTO> findAllFiltered(PersonDTO filter) {
      Specification<Person> spec = new PersonSpecification(modelMapper.map(filter, Person.class));

      List<Person> persons = personRepository.findAll(spec);
      List<PersonDTO> personDTOS = new ArrayList<>();

      for (Person person: persons){
         personDTOS.add(entityToDTO(person));
      }

      if (!personDTOS.isEmpty()) {
         return personDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   public Integer getFirstId(PersonDTO filter){

      return findAllFiltered(filter).get(0).getId();
   }

   public PersonDTO save(PersonDTO personDTO) {
      if (  !StringUtils.isEmpty(personDTO.getFirstName()) &&
            !StringUtils.isEmpty(personDTO.getLastName())) {

         try {
            // try to find existing Person
            Integer personId = getFirstId(personDTO);
            return entityToDTO(personRepository.getOne(personId));

         } catch (ResourceNotFoundException ex) {
            // if there is no Person then create
            personDTO.setId(null);
            return entityToDTO(personRepository.save(dtoToEntity(personDTO)));
         }

      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public PersonDTO update(PersonDTO personDTO) {
      if (  !StringUtils.isEmpty(personDTO.getFirstName()) &&
            !StringUtils.isEmpty(personDTO.getLastName())) {

         if (!existsById(personDTO.getId())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + personDTO.getId());
         }
         Person person = personRepository.save(dtoToEntity(personDTO));

         return entityToDTO(person);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }
   }

    public void deleteById(Integer id) {
      if (!existsById(id)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      } else {
         personRepository.deleteById(id);
      }

   }

   @Override
   public Integer count() {
      return Math.toIntExact(personRepository.count());
   }

   @Override
   public PersonDTO entityToDTO(Person person) {
      return modelMapper.map(person, PersonDTO.class);
   }

   @Override
   public Person dtoToEntity(PersonDTO bookDTO) {
      return modelMapper.map(bookDTO, Person.class);
   }
}
