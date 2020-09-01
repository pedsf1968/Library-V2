package com.pedsf.library.userapi.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.userapi.controller.UserController;
import com.pedsf.library.userapi.model.Role;
import com.pedsf.library.userapi.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTestIT {
   private static final List<UserDTO> allUserDTOS = new ArrayList<>();

   @Inject
   private MockMvc mockMvc;

   @MockBean
   private UserService userService;

   @MockBean
   private Role role;

   private ObjectMapper mapper = new ObjectMapper();

   @BeforeAll
   static void beforeAll() {
      allUserDTOS.add( new UserDTO(1,"Admin","ADMIN","admin@library.org","$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie","22, rue de la Paix","75111","Paris"));
      allUserDTOS.add( new UserDTO(2,"Staff","STAFF","staff@library.org","$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG","1, rue verte","68121","Strasbourg"));
      allUserDTOS.add( new UserDTO(3,"Martin","DUPONT","martin.dupont@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","3, chemin de l’Escale","25000","Besançon"));
      allUserDTOS.add( new UserDTO(4,"Emile","ZOLA","emile.zola@free.fr","$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.", "1, Rue de la Paix","75001","Paris"));
      allUserDTOS.add( new UserDTO(5,"Victor","HUGO","victor.hugo@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi","24, Rue des cannut","69003","Lyon"));
   }

   @Test
   @Tag("findAllUsers")
   @DisplayName("Verify that we get the list of all users")
   void findAllUsers_returnAllUser() throws Exception {
      int i = 0;
      // GIVEN
      when(userService.findAll()).thenReturn(allUserDTOS);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      List<UserDTO> founds = Arrays.asList(mapper.readValue(json, UserDTO[].class));

      // THEN
      assertThat(founds.size()).isEqualTo(5);
      for(UserDTO userDTO: allUserDTOS) {
      assertThat(userDTO).isEqualTo(founds.get(i++));
      }
   }

   @Test
   @Tag("findAllFilteredUsers")
   @DisplayName("Verify that we get one user by his firstName and lastName")
   void findAllFilteredUsers_returnUser_ofUserName() throws Exception {
      UserDTO filter = new UserDTO();

      // GIVEN
      filter.setFirstName(allUserDTOS.get(0).getFirstName());
      filter.setLastName(allUserDTOS.get(0).getLastName());
      when(userService.findAllFiltered(any(UserDTO.class))).thenReturn(Collections.singletonList(allUserDTOS.get(0)));

      // WHEN
      String json = mapper.writeValueAsString(filter);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/users/searches")
                  .contentType(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      List<UserDTO> founds = Arrays.asList(mapper.readValue(json, UserDTO[].class));

      assertThat(founds.size()).isEqualTo(1);
      assertThat(founds.get(0)).isEqualTo(allUserDTOS.get(0));
   }

   @Test
   @Tag("findUserById")
   @DisplayName("Verify that we get one users by is ID")
   void findUserById_returnUser_ofOneId() throws Exception {
      UserDTO expected = allUserDTOS.get(2);
      Integer userId = expected.getId();

      // GIVEN
      when(userService.findById(userId)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/"+userId))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      UserDTO found = mapper.readValue(json, UserDTO.class);

      // THEN
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("findUserByEmail")
   @DisplayName("Verify that we get one users by is email")
   void findUserByEmail_returnUser_ofOneEmail()  throws Exception {
      UserDTO expected = allUserDTOS.get(2);
      String email = expected.getEmail();

      // GIVEN
      when(userService.findByEmail(email)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/users/email/"+email))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      ObjectMapper mapper = new ObjectMapper();
      UserDTO found = mapper.readValue(json, UserDTO.class);

      // THEN
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we can save one User and get the User saved")
   void addUser_saveUserSaved_ofUser() throws Exception {
      UserDTO expected = allUserDTOS.get(2);
      expected.setMatchingPassword(expected.getPassword());
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      when(userService.save(any(UserDTO.class))).thenReturn(expected);

      // WHEN
      String json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      UserDTO found = mapper.readValue(json, UserDTO.class);

      // THEN
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that we can update one User and get the User saved")
   void updateUser_returnUserUpdated_ofUserAndId() throws Exception {
      UserDTO expected = allUserDTOS.get(3);
      Integer userId = expected.getId();
      expected.setMatchingPassword(expected.getPassword());
      String json;
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      when(userService.update(any(UserDTO.class))).thenReturn(expected);

      // WHEN
      json = mapper.writeValueAsString(expected);
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+userId)
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      UserDTO found = mapper.readValue(json, UserDTO.class);

      // THEN
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("updateUserCounter")
   @DisplayName("Verify that we can update User counter")
   void updateUserCounter() throws Exception {
      UserDTO expected = allUserDTOS.get(3);
      Integer userId = expected.getId();
      Integer counter = 55;
      expected.setMatchingPassword(expected.getPassword());
      expected.setCounter(counter);
      String json;
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      when(userService.updateCounter(userId,counter)).thenReturn(expected);
      json = mapper.writeValueAsString(counter);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+userId + "/counter")
            .queryParam("userId", userId.toString())
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      UserDTO found = mapper.readValue(json, UserDTO.class);

      // THEN
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("updateUserStatus")
   @DisplayName("Verify that we can update User status")
   void updateUserStatus_returnUpdatedUser_ofUserWithNewData() throws Exception {
      UserDTO expected = allUserDTOS.get(3);
      Integer userId = expected.getId();
      String status = "NEW STATUS";
      expected.setMatchingPassword(expected.getPassword());
      expected.setStatus(status);
      String json;
      ObjectMapper mapper = new ObjectMapper();

      // GIVEN
      when(userService.updateStatus(userId,status)).thenReturn(expected);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+userId + "/status")
                  .queryParam("userId", userId.toString())
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(status))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn();

      // convert result in UserDTO list
      json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
      UserDTO found = mapper.readValue(json, UserDTO.class);

      // THEN
      assertThat(found).isEqualTo(expected);
   }

   @Test
   @Tag("deleteUser")
   @DisplayName("Verify that we cans delete User that exist with his ID")
   void deleteUser_returnNothing_ofExistingUserId() throws Exception {
      Integer userId = 44;

      // GIVEN
      doNothing().when(userService).deleteById(userId);

      // WHEN
      mockMvc.perform(
         MockMvcRequestBuilders.delete("/users/"+userId))
         .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
         .andReturn();

   }
}