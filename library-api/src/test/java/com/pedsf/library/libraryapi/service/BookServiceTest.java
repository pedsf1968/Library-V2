package com.pedsf.library.libraryapi.service;

import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Disabled
class BookServiceTest {

   @TestConfiguration
   static class bookServiceTestConfiguration {
      @Autowired
      private BookRepository bookRepository;
      @Autowired
      private PersonRepository personRepository;


      @Bean
      public BookService bookService() {
         PersonService personService = new PersonService(personRepository);

         return new BookService(bookRepository,personService);
      }
   }

   @Autowired
   private BookService bookService;

   @Test
   void existsById() {
   }

   @Test
   void findById() {
   }

   @Test
   void findAll() {
   }

   @Test
   void findAllAllowed() {
   }

   @Test
   void findAllFiltered() {
   }

   @Test
   void getFirstId() {
   }

   @Test
   void save() {
   }

   @Test
   void update() {
   }

   @Test
   void deleteById() {
   }

   @Test
   void count() {
      assertThat(bookService.count()).isEqualTo(10);
   }

   @Test
   void entityToDTO() {
   }

   @Test
   void dtoToEntity() {
   }

   @Test
   void findAllAuthors() {
   }

   @Test
   void findAllEditors() {
   }

   @Test
   void findAllTitles() {
   }

   @Test
   void increaseStock() {
   }

   @Test
   void decreaseStock() {
   }
}