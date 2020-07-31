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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserServiceImplTest {
   private static final Integer USER_ID_TEST = 1;
   private static final String EMAIL_TEST = "user.test@gmail.com";

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
   void usertoUserDTO_returnRightUserEntity_ofUserDTO() {
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
   void updateCounter_returnNewCounter_ofUpdatedUserCounter() {
      UserDTO userDTO = userService.findById(USER_ID_TEST);
      Integer oldCounter = userDTO.getCounter();
      Integer newCounter = 11;

      userService.updateCounter(USER_ID_TEST,newCounter);
      userDTO = userService.findById(USER_ID_TEST);
      assertThat(userDTO.getCounter()).isEqualTo(newCounter);

      userService.updateCounter(USER_ID_TEST,oldCounter);
      userDTO = userService.findById(USER_ID_TEST);
      assertThat(userDTO.getCounter()).isEqualTo(oldCounter);
   }

   @Test
   @Tag("updateStatus")
   @DisplayName("Verify that we can update the user statusupdateStatus")
   void updateStatus_returnNewStatus_ofUserChangedStatus() {
      UserDTO userDTO = userService.findById(USER_ID_TEST);
      String newStatus = "NewStatus";
      String oldStatus = userDTO.getStatus();

      userService.updateStatus(USER_ID_TEST,newStatus);
      userDTO = userService.findById(USER_ID_TEST);
      assertThat(userDTO.getStatus()).isEqualTo(newStatus);

      userService.updateStatus(USER_ID_TEST,oldStatus);
      userDTO = userService.findById(USER_ID_TEST);
      assertThat(userDTO.getStatus()).isEqualTo(oldStatus);
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the user exist")
   void existsById_returnTrue_OfAnExistingUserId() {
      List<UserDTO> userDTOS = userService.findAll();

      for(UserDTO userDTO : userDTOS) {
         Integer id = userDTO.getId();
         assertThat(userService.existsById(id)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the User doesn't exist")
   void existsById_returnFalse_OfAnInexistingUserId() {
      assertThat(userService.existsById(55)).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find User by is ID")
   void findById_returnUser_ofExistingUserId() {
      List<UserDTO> userDTOS = userService.findAll();
      UserDTO found;

      for(UserDTO userDTO : userDTOS) {
         Integer id = userDTO.getId();
         found = userService.findById(id);

         assertThat(found).isEqualTo(userDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find User with wrong ID")
   void findById_returnException_ofInexistingUserId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         UserDTO found = userService.findById(45);
      });
   }

   @Test
   @Tag("existsByEmail")
   @DisplayName("Verify that return TRUE if the User with this email exist")
   void existsByEmail_returnTrue_ofAnExistingEmail() {
      List<UserDTO> userDTOS = userService.findAll();
      UserDTO found;

      for(UserDTO userDTO : userDTOS) {
         String email = userDTO.getEmail();
         assertThat(userService.existsByEmail(email)).isTrue();
      }
   }

   @Test
   @Tag("existsByEmail")
   @DisplayName("Verify that return FALSE if the User with this email exist")
   void existsByEmail_returnFalse_ofAnInexistingEmail() {
      assertThat(userService.existsByEmail(EMAIL_TEST)).isFalse();
   }

   @Test
   @Tag("findByEmail")
   @DisplayName("Verify that we can find User by existing email")
   void findByEmail_returnUser_ofExistingEmail() {
      List<UserDTO> userDTOS = userService.findAll();
      UserDTO found;

      for(UserDTO userDTO : userDTOS) {
         String email = userDTO.getEmail();
         found = userService.findByEmail(email);

         assertThat(found).isEqualTo(userDTO);
      }
   }

   @Test
   @Tag("findByEmail")
   @DisplayName("Verify that we can find User with wrong email")
   void findByEmail_returnException_ofInexistingEmail() {
      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         UserDTO found = userService.findByEmail("email@lqkjsdhlqksjdh");
      });
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Users")
   void findAll_returnAllUsers() {
      List<UserDTO> userDTOS = userService.findAll();
      assertThat(userDTOS.size()).isEqualTo(5);
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one User by his name and email")
   void findAllFiltered_returnOnlyOneUser_ofExistingFirstNameLastNameAndEmail() {
      List<UserDTO> userDTOS = userService.findAll();
      List<UserDTO> found;
      for(UserDTO u:userDTOS) {
         UserDTO filter = new UserDTO();
         filter.setFirstName(u.getFirstName());
         filter.setLastName(u.getLastName());
         filter.setEmail(u.getEmail());

         found = userService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(u);
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("getFirstId")
   void getFirstId() {
      List<UserDTO> found;
      UserDTO filter = new UserDTO();

      filter.setCity("Paris");
      found = userService.findAllFiltered(filter);
      assertThat(found.size()).isEqualTo(2);
      Integer foundId = userService.getFirstId(filter);

      assertThat(found.get(0).getId()).isEqualTo(foundId);
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new User")
   void save_returnCreatedUser_ofNewUser() {
      UserDTO userDTO = userService.findById(USER_ID_TEST);

      userDTO.setId(null);
      userDTO.setEmail(EMAIL_TEST);
      userDTO = userService.save(userDTO);
      Integer id = userDTO.getId();

      assertThat(userService.existsById(id)).isTrue();
      userService.deleteById(id);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an User")
   void update_returnUpdatedUser_ofUserAndNewEmail() {
      UserDTO userDTO = userService.findById(USER_ID_TEST);
      String oldEmail = userDTO.getEmail();
      userDTO.setEmail(EMAIL_TEST);

      UserDTO userSaved = userService.update(userDTO);
      assertThat(userSaved).isEqualTo(userDTO);
      UserDTO userfound = userService.findByEmail(EMAIL_TEST);
      assertThat(userfound).isEqualTo(userDTO);

      userDTO.setEmail(oldEmail);
      userService.update(userDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a User by his ID")
   void deleteById_returnExceptionWhenGetUserById_ofDeletedUserById() {
      UserDTO userDTO = userService.findById(USER_ID_TEST);

      userDTO.setId(null);
      userDTO.setEmail(EMAIL_TEST);
      userDTO = userService.save(userDTO);
      Integer id = userDTO.getId();

      assertThat(userService.existsById(id)).isTrue();
      userService.deleteById(id);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         userService.findById(id);
      });
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Users")
   void count_returnTheNumberOfUsers() {
      assertThat(userService.count()).isEqualTo(5);
   }
}