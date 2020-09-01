package com.pedsf.library.userapi.controller.unitary;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.userapi.controller.UserController;
import com.pedsf.library.userapi.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {UserController.class})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class UserControllerTest {
   @Inject
   private MockMvc mockMvc;

   @MockBean
   private UserService userService;
   private static ObjectMapper mapper = new ObjectMapper();
   private static UserDTO newUserDTO;

   @BeforeAll
   static void beforeAll() {
      newUserDTO =  new UserDTO(11,"John","DOE","john.doe@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","11, rue de la Paix","25000","Besançon");
      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

   }

   @Test
   @Tag("findAllUsers")
   @DisplayName("Verify that we get NotFound if there are no Users")
   void findAllUsers_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(userService.findAll()).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/users"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }



   @Test
   @Tag("findAllFilteredUsers")
   @DisplayName("Verify that we get NotFound if there are no Userss")
   void findAllFilteredUsers_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(userService.findAllFiltered(newUserDTO)).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.post("/users/searches")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we get NotFound if with wrong Id")
   void findById_returnNotFound_ofWrongUserId() throws Exception {
      // GIVEN
      when(userService.findById(anyInt())).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/users/" + 456))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("findUserByEmail")
   @DisplayName("Verify that we get NotFound if with wrong email")
   void findUserByEmail_returnNotFound_ofWrongUserEmail() throws Exception {
      // GIVEN
      String wrongEmail = "jeain.moon@kakao.kr";
      when(userService.findByEmail(wrongEmail)).thenThrow(ResourceNotFoundException.class);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.get("/users/email/" + wrongEmail))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("addUsers")
   @DisplayName("Verify that we get Conflict if there are ConflictException when save Users")
   void addUsers_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(userService.save(any(UserDTO.class))).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();
   }

   @Test
   @Tag("addUsers")
   @DisplayName("Verify that we get BadRequest if there are BadRequestException when save Users")
   void addUsers_returnBadRequest_ofBadRequestException() throws Exception {
      // GIVEN
      when(userService.save(any(UserDTO.class))).thenThrow(BadRequestException.class);

      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
   }

   @Test
   @Tag("updateUsers")
   @DisplayName("Verify that we get NotFound if there are no Users to update")
   void updateUsers_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      when(userService.update(any(UserDTO.class))).thenThrow(ResourceNotFoundException.class);

      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+456)
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that we get Conflict if there are no Users ConflictException when update")
   void updateUser_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      when(userService.update(newUserDTO)).thenThrow(ConflictException.class);

      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+456)
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();
   }

   @Test
   @Tag("deleteUsers")
   @DisplayName("Verify that we get NotFound if there are no Users to delete")
   void deleteUsers_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(userService).deleteById(anyInt());

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.delete("/users/"+456))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("updateUserCounter")
   @DisplayName("Verify that we get NotFound when updating the counter with bad Users Id")
   void updateUserCounter_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(userService).updateCounter(anyInt(),anyInt());
      String json = mapper.writeValueAsString(456);

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+456+"/counter")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("updateUserCounter")
   @DisplayName("Verify that we get Conflict when updating the User counter with conflicts")
   void updateUserCounter_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      doThrow(ConflictException.class).when(userService).updateCounter(anyInt(),anyInt());
      String json = mapper.writeValueAsString(456);
      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+2+"/counter")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();
   }

   @Test
   @Tag("updateUserStatus")
   @DisplayName("Verify that we get NotFound when updating the status with bad Users Id")
   void updateUserStatus_returnNotFound_ofResourceNotFoundException() throws Exception {
      // GIVEN
      doThrow(ResourceNotFoundException.class).when(userService).updateStatus(anyInt(),anyString());
      String json = mapper.writeValueAsString("MEMBER");

      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+456+"/status")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @Tag("updateUserStatus")
   @DisplayName("Verify that we get Conflict when updating User status with conflicts")
   void updateUserStatus_returnConflict_ofConflictException() throws Exception {
      // GIVEN
      doThrow(ConflictException.class).when(userService).updateStatus(anyInt(),anyString());
      String json = mapper.writeValueAsString("MEMBER");
      // WHEN
      mockMvc.perform(
            MockMvcRequestBuilders.put("/users/"+2+"/status")
                  .contentType(MediaType.APPLICATION_JSON)
                  .characterEncoding(String.valueOf(StandardCharsets.UTF_8))
                  .content(json))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();
   }



}
