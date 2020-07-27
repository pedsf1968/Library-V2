package com.pedsf.library.userapi.service;

import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.userapi.model.User;
import com.pedsf.library.userapi.repository.RoleRepository;
import com.pedsf.library.userapi.repository.UserRepository;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserServiceImplTest {
   private static final Integer userId = 1;

   @TestConfiguration
   static class testconfiguration {

      @Autowired
      private UserRepository userRepository;
      @Autowired
      private RoleRepository roleRepository;

      @Bean
      public UserService userService() {
         return new UserServiceImpl(userRepository,roleRepository);
      }
   }

   @Autowired
   private UserService userService;

   @Test
   @Tag("userDTOtoUser")
   @DisplayName("Verify that User DTO is converted in right User Entity")
   void userDTOtoUser_returnRightUserDTO_ofUserEntity() {
      List<UserDTO> userDTOS = userService.findAll();
      User user;

      for (UserDTO userDTO : userDTOS) {
         user = userService.userDTOtoUser(userDTO);
         assertThat(user.getId()).isEqualTo(userDTO.getId());
         assertThat(user.getFirstName()).isEqualTo(userDTO.getFirstName());
         assertThat(user.getLastName()).isEqualTo(userDTO.getLastName());
         assertThat(user.getPassword()).isEqualTo(userDTO.getPassword());
         assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
         assertThat(user.getPhone()).isEqualTo(userDTO.getPhone());
         assertThat(user.getPhotoLink()).isEqualTo(userDTO.getPhotoLink());
         assertThat(user.getStatus()).isEqualTo(userDTO.getStatus());
         assertThat(user.getCounter()).isEqualTo(userDTO.getCounter());
         assertThat(user.getStreet1()).isEqualTo(userDTO.getStreet1());
         assertThat(user.getStreet2()).isEqualTo(userDTO.getStreet2());
         assertThat(user.getStreet3()).isEqualTo(userDTO.getStreet3());
         assertThat(user.getZipCode()).isEqualTo(userDTO.getZipCode());
         assertThat(user.getCity()).isEqualTo(userDTO.getCity());
         assertThat(user.getCountry()).isEqualTo(userDTO.getCountry());
      }
   }

   @Test
   @Tag("userToUserDTO")
   @DisplayName("Verify that User Entity is converted in right User DTO")
   void userDTOtoUser_returnRightUserEntity_ofUserDTO() {
      List<UserDTO> userDTOS = userService.findAll();
      List<User> users = new ArrayList<>();
      UserDTO userDTO;

      for (UserDTO uDTO : userDTOS) {
         users.add(userService.userDTOtoUser(uDTO));
      }

      for (User user : users) {
         userDTO = userService.userToUserDTO(user);
         assertThat(userDTO.getId()).isEqualTo(user.getId());
         assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
         assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
         assertThat(userDTO.getPassword()).isEqualTo(user.getPassword());
         assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
         assertThat(userDTO.getPhone()).isEqualTo(user.getPhone());
         assertThat(userDTO.getPhotoLink()).isEqualTo(user.getPhotoLink());
         assertThat(userDTO.getStatus()).isEqualTo(user.getStatus());
         assertThat(userDTO.getCounter()).isEqualTo(user.getCounter());
         assertThat(userDTO.getStreet1()).isEqualTo(user.getStreet1());
         assertThat(userDTO.getStreet2()).isEqualTo(user.getStreet2());
         assertThat(userDTO.getStreet3()).isEqualTo(user.getStreet3());
         assertThat(userDTO.getZipCode()).isEqualTo(user.getZipCode());
         assertThat(userDTO.getCity()).isEqualTo(user.getCity());
         assertThat(userDTO.getCountry()).isEqualTo(user.getCountry());
      }
   }

   @Test
   @Tag("updateCounter")
   @DisplayName("Verify that we can change the counter value for one user")
   void updateCounter_setNewCouterValue_givenUserIdAndNewCounterValue() {
      UserDTO userDTO = userService.findById(userId);


      assertThat(userDTO.getCounter()).isEqualTo(0);
      userService.updateCounter(userId,11);

      userDTO = userService.findById(userId);
      assertThat(userDTO.getCounter()).isEqualTo(11);
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
      List<UserDTO> userDTOS = userService.findAll();
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
   void count_() {
      assertThat(userService.count()).isEqualTo(5);
   }
}