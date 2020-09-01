package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {
   Person getOne(Integer id);
   List<Person> findAll();
   Person save(Person person);
   void deleteById(Integer id);
}
