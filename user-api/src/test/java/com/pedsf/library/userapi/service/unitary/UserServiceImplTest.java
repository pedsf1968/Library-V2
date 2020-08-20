package com.pedsf.library.userapi.service.unitary;

import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.userapi.model.*;
import com.pedsf.library.userapi.repository.*;
import com.pedsf.library.userapi.service.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {

   @Mock
   private UserRepository userRepository;
   @Mock
   private RoleRepository roleRepository;
   private UserService userService;
   private static List<User> allUsers = new ArrayList<>();
   private static List<Role> allRoles = new ArrayList<>();

   private User newUser;
   private UserDTO newUserDTO;


   @BeforeAll
   static void beforeAll() {
      allRoles.add(new Role(1,"ROLE_ADMIN"));
      allRoles.add(new Role(2,"ROLE_STAFF"));
      allRoles.add(new Role(3,"ROLE_USER"));

      allUsers.add(new User(1,"Admin","ADMIN","admin@library.org","$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie", "22, rue de la Paix","75111","Paris"));
      allUsers.add(new User(2,"Staff","STAFF","staff@library.org","$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG", "1, rue verte","68121","Strasbourg"));
      allUsers.add(new User(3,"Martin","DUPONT","martin.dupont@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS", "3, chemin de l’Escale","25000","Besançon"));
      allUsers.add(new User(4,"Emile","ZOLA","emile.zola@free.fr","$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.",  "1, Rue de la Paix","75001","Paris"));
      allUsers.add(new User(5,"Victor","HUGO","victor.hugo@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi",  "24, Rue des cannut","69003","Lyon"));

   }
   @BeforeEach
   void beforeEach() {
      userService = new UserServiceImpl(userRepository,roleRepository);

      newUser = new User(11,"Taeyeong","KIM","taeyeong.kim@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi",  "24, Rue des fleurs","59003","Lille");
      newUser.setCounter(2);
      newUser.setPhone("0324593874");
      newUser.setPhotoLink("http://photolink");
      newUser.setStatus("MEMBER");
      newUserDTO = new UserDTO(11,"Taeyeong","KIM","taeyeong.kim@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi",  "24, Rue des fleurs","59003","Lille");
      newUserDTO.setCounter(2);
      newUserDTO.setPhone("0324593874");
      newUserDTO.setPhotoLink("http://photolink");
      newUserDTO.setStatus("MEMBER");
   }


   @Test
   @Tag("userDTOtoUser")
   @DisplayName("Verify that User DTO is converted in right User Entity")
   void userDTOtoUser_returnRightUserDTO_ofUserEntity() {

      User user = userService.userDTOtoUser(newUserDTO);

      assertThat(user.getId()).isEqualTo(newUserDTO.getId());
      assertThat(user.getFirstName()).isEqualTo(newUserDTO.getFirstName());
      assertThat(user.getLastName()).isEqualTo(newUserDTO.getLastName());
      assertThat(user.getPassword()).isEqualTo(newUserDTO.getPassword());
      assertThat(user.getEmail()).isEqualTo(newUserDTO.getEmail());
      assertThat(user.getPhone()).isEqualTo(newUserDTO.getPhone());
      assertThat(user.getPhotoLink()).isEqualTo(newUserDTO.getPhotoLink());
      assertThat(user.getStatus()).isEqualTo(newUserDTO.getStatus());
      assertThat(user.getCounter()).isEqualTo(newUserDTO.getCounter());
      assertThat(user.getStreet1()).isEqualTo(newUserDTO.getStreet1());
      assertThat(user.getStreet2()).isEqualTo(newUserDTO.getStreet2());
      assertThat(user.getStreet3()).isEqualTo(newUserDTO.getStreet3());
      assertThat(user.getZipCode()).isEqualTo(newUserDTO.getZipCode());
      assertThat(user.getCity()).isEqualTo(newUserDTO.getCity());
      assertThat(user.getCountry()).isEqualTo(newUserDTO.getCountry());
   }

   @Test
   @Tag("userToUserDTO")
   @DisplayName("Verify that User Entity is converted in right User DTO")
   void usertoUserDTO_returnRightUserEntity_ofUserDTO() {
      UserDTO  userDTO = userService.userToUserDTO(newUser);

      assertThat(userDTO.getId()).isEqualTo(newUser.getId());
      assertThat(userDTO.getFirstName()).isEqualTo(newUser.getFirstName());
      assertThat(userDTO.getLastName()).isEqualTo(newUser.getLastName());
      assertThat(userDTO.getPassword()).isEqualTo(newUser.getPassword());
      assertThat(userDTO.getEmail()).isEqualTo(newUser.getEmail());
      assertThat(userDTO.getPhone()).isEqualTo(newUser.getPhone());
      assertThat(userDTO.getPhotoLink()).isEqualTo(newUser.getPhotoLink());
      assertThat(userDTO.getStatus()).isEqualTo(newUser.getStatus());
      assertThat(userDTO.getCounter()).isEqualTo(newUser.getCounter());
      assertThat(userDTO.getStreet1()).isEqualTo(newUser.getStreet1());
      assertThat(userDTO.getStreet2()).isEqualTo(newUser.getStreet2());
      assertThat(userDTO.getStreet3()).isEqualTo(newUser.getStreet3());
      assertThat(userDTO.getZipCode()).isEqualTo(newUser.getZipCode());
      assertThat(userDTO.getCity()).isEqualTo(newUser.getCity());
      assertThat(userDTO.getCountry()).isEqualTo(newUser.getCountry());
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no User")
   void findAll_throwResourceNotFoundException_ofEmptyList() {
      List<User> emptyList = new ArrayList<>();
      Mockito.lenient().when(userRepository.findAll()).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.findAll());
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no User")
   void findAllFiltered_throwResourceNotFoundException_ofEmptyList() {
      List<User> emptyList = new ArrayList<>();
      Mockito.lenient().when(userRepository.findAll(any(UserSpecification.class))).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.findAllFiltered(newUserDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a User without firstName")
   void save_throwBadRequestException_ofNewUserWithoutFirstName() {
      newUserDTO.setFirstName("");
      Assertions.assertThrows(BadRequestException.class, ()-> userService.save(newUserDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a User without lastName")
   void save_throwBadRequestException_ofNewUserWithoutLastName() {
      newUserDTO.setLastName("");
      Assertions.assertThrows(BadRequestException.class, ()-> userService.save(newUserDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a User without email")
   void save_throwBadRequestException_ofNewUserWithoutEmail() {
      newUserDTO.setEmail("");
      Assertions.assertThrows(BadRequestException.class, ()-> userService.save(newUserDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a User with existing email")
   void save_throwConflictException_ofNewUserWithExistingEmail() {
      User expected = allUsers.get(2);
      newUserDTO.setEmail(expected.getEmail());
      Mockito.lenient().when(userRepository.findByEmail(anyString())).thenReturn(expected);

      Assertions.assertThrows(ConflictException.class, ()-> userService.save(newUserDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have BadRequestException when update a User without Id")
   void update_throwBadRequestException_ofUserWithoutId() {
      newUserDTO.setId(null);
      Assertions.assertThrows(BadRequestException.class, ()-> userService.update(newUserDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have BadRequestException when update a User without firstName")
   void update_throwBadRequestException_ofUserWithoutFirstName() {
      newUserDTO.setFirstName("");
      Assertions.assertThrows(BadRequestException.class, ()-> userService.update(newUserDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have BadRequestException when update a User without lastName")
   void update_throwBadRequestException_ofUserWithoutLastName() {
      newUserDTO.setLastName("");
      Assertions.assertThrows(BadRequestException.class, ()-> userService.update(newUserDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have BadRequestException when update a User without email")
   void update_throwBadRequestException_ofUserWithoutEmail() {
      newUserDTO.setEmail("");
      Assertions.assertThrows(BadRequestException.class, ()-> userService.update(newUserDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ResourceNotFoundException when update a User with wrong Id")
   void update_throwResourceNotFoundException_ofUserWithWrongId() {
      Mockito.lenient().when(userRepository.existsById(anyInt())).thenReturn(false);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.update(newUserDTO));
   }

   @Test
   @Tag("delete")
   @DisplayName("Verify that we have ResourceNotFoundException when delete a User with wrong Id")
   void delete_throwResourceNotFoundException_ofUserWithWrongId() {
      Mockito.lenient().when(userRepository.existsById(anyInt())).thenReturn(false);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.deleteById(654));
   }

}