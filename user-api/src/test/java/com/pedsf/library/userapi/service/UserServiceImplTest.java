package com.pedsf.library.userapi.service;

import com.pedsf.library.userapi.repository.RoleRepository;
import com.pedsf.library.userapi.repository.UserRepository;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class UserServiceImplTest {

   @MockBean
   private static UserRepository userRepository;
   @MockBean
   private static RoleRepository roleRepository;

   @Autowired
   private static UserServiceImpl userService;

   @TestConfiguration
   static class UserServiceImplTestContextConfiguration {

      @Bean
      public UserService userService() {
         return new UserServiceImpl(userRepository,roleRepository);
      }
   }


   @BeforeAll
   private static void beforeAll() {

   }

   @Test
   @Tag("userDTOtoUser")
   @DisplayName("userDTOtoUser")
   void userDTOtoUser() {
      Integer nb = userService.count();

      assertThat(nb).isEqualTo(0);
   }

   @Test
   @Tag("userToUserDTO")
   @DisplayName("userToUserDTO")
   void userToUserDTO() {
   }

   @Test
   @Tag("updateCounter")
   @DisplayName("updateCounter")
   void updateCounter() {
   }

   @Test
   @Tag("updateStatus")
   @DisplayName("updateStatus")
   void updateStatus() {
   }

   @Test
   @Tag("existsById")
   @DisplayName("existsById")
   void existsById() {
   }

   @Test
   @Tag("existsByEmail")
   @DisplayName("existsByEmail")
   void existsByEmail() {
   }

   @Test
   @Tag("findById")
   @DisplayName("findById")
   void findById() {
   }

   @Test
   @Tag("findByEmail")
   @DisplayName("findByEmail")
   void findByEmail() {
   }

   @Test
   @Tag("findByStatus")
   @DisplayName("findByStatus")
   void findByStatus() {
   }

   @Test
   @Tag("findAll")
   @DisplayName("findAll")
   void findAll() {
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("findAllFiltered")
   void findAllFiltered() {
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("getFirstId")
   void getFirstId() {
   }

   @Test
   @Tag("save")
   @DisplayName("save")
   void save() {
   }

   @Test
   @Tag("update")
   @DisplayName("update")
   void update() {
   }

   @Test
   @Tag("deleteById")
   @DisplayName("deleteById")
   void deleteById() {
   }

   @Test
   @Tag("count")
   @DisplayName("count")
   void count() {
      Integer nb = userService.count();

      assertThat(nb).isEqualTo(0);
   }
}