package com.pedsf.library.webapi.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedsf.library.dto.UserStatus;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.service.global.SecurityServiceImpl;
import com.pedsf.library.webapi.web.PathTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import(UserController.class)
@WebMvcTest(controllers = {UserController.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class UserControllerTest {
   private static final String ADMIN_TEST_EMAIL = "admin@testing.com";
   private static final String ADMIN_TEST_PASSWORD = "adminoi654$#R";
   private static final String ADMIN_TEST_ROLE = "ROLE_ADMIN";
   private static final String USER_TEST_EMAIL = "user@testing.com";
   private static final String USER_TEST_PASSWORD = "useroi654$#R";
   private static final String USER_TEST_ROLE = "USER";

   private static final Map<Integer,UserDTO> allUserDTOS = new HashMap<>();
   private static ObjectMapper mapper = new ObjectMapper();

   @Inject
   private MockMvc mockMvc;
   @MockBean
   private UserApiProxy userApiProxy;
   @MockBean
   private SecurityServiceImpl securityService;
   @MockBean
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   private UserController userController;
   private UserDTO newUserDTO;
   private static UserDTO operator = new UserDTO();

   @Configuration
   @EnableWebSecurity
   static class SecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception
      {
         http
               .authorizeRequests()
               .antMatchers("/**").permitAll()
               .anyRequest().anonymous().and()
               .csrf().disable();
      }
   }

   @BeforeAll
   static void beforeAll() {
      allUserDTOS.put( 1, new UserDTO(1,"Admin","ADMIN","admin@library.org","$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie","22, rue de la Paix","75111","Paris"));
      allUserDTOS.put( 2, new UserDTO(2,"Staff","STAFF","staff@library.org","$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG","1, rue verte","68121","Strasbourg"));
      allUserDTOS.put( 3, new UserDTO(3,"Martin","DUPONT","martin.dupont@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","3, chemin de l’Escale","25000","Besançon"));
      allUserDTOS.put( 4, new UserDTO(4,"Emile","ZOLA","emile.zola@free.fr","$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.", "1, Rue de la Paix","75001","Paris"));
      allUserDTOS.put( 5, new UserDTO(5,"Victor","HUGO","victor.hugo@gmail.com","$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi","24, Rue des cannut","69003","Lyon"));

      for(int i=1; i<allUserDTOS.size();i++) {
         String pwd = allUserDTOS.get(i).getPassword();
         allUserDTOS.get(i).setMatchingPassword(pwd);
         allUserDTOS.get(i).setStatus(UserStatus.MEMBER.name());
      }


      operator.setFirstName("Admin");
      operator.setLastName("Admin");
      operator.setEmail(ADMIN_TEST_EMAIL);
      operator.setPassword(ADMIN_TEST_PASSWORD);
      operator.setMatchingPassword(ADMIN_TEST_PASSWORD);
      operator.setRoles(Arrays.asList(ADMIN_TEST_ROLE,USER_TEST_ROLE));
      TimeZone.setDefault(TimeZone.getTimeZone("CET"));
      mapper.setTimeZone(TimeZone.getTimeZone("CET"));
   }

   @BeforeEach
   void beforeEach() {
      userController = new UserController(userApiProxy,securityService,bCryptPasswordEncoder);
      mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
      newUserDTO = new UserDTO(11,"Tony","TIAN","tony.tian@gmail.com","$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS","4, rue du moulin","69003","Lyon");
      newUserDTO.setMatchingPassword(newUserDTO.getPassword());

      when(userApiProxy.findUserByEmail(ADMIN_TEST_EMAIL)).thenReturn(operator);
      when(userApiProxy.findUserById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allUserDTOS.get((Integer) invocation.getArguments()[0]));

   }


   @Test
   @Tag("postLogin")
   @DisplayName("Verify that call the User creation page")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void postLogin_displayLoginPage() throws Exception {
      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.HOME))
            .andReturn();
   }

   @Test
   @Tag("newUser")
   @DisplayName("Verify that call the User creation page")
   void newUser_displayUserCreatePage() throws Exception {
      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/new"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User and redirect to User update page")
   void addUser_returnCreatedUser_ofNewUserData() throws Exception {

      // GIVEN
      when(userApiProxy.addUser(any(UserDTO.class))).thenReturn(newUserDTO);
      when(userApiProxy.findUserByEmail(anyString())).thenThrow(ResourceNotFoundException.class);
      when(bCryptPasswordEncoder.encode(anyString())).thenReturn(newUserDTO.getPassword());
      doNothing().when(securityService).autoLogin(anyString(),anyString());

      String json = mapper.writeValueAsString(newUserDTO);
      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
               .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.USER_UPDATE_R + newUserDTO.getId()))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User with no firstName it redirect to the same page")
   void addUser_returnToTheSamePage_ofUserWithNoFirstName() throws Exception {
      // GIVEN
      newUserDTO.setFirstName(null);
      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User with no lastName it redirect to the same page")
   void addUser_returnToTheSamePage_ofUserWithNoLastName() throws Exception {
      // GIVEN
      newUserDTO.setLastName(null);
      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User with no email it redirect to the same page")
   void addUser_returnToTheSamePage_ofUserWithNoEmail() throws Exception {
      // GIVEN
      newUserDTO.setEmail(null);
      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User with other User email it redirect to the same page")
   void addUser_returnToTheSamePage_ofUserExistingEmail() throws Exception {
      UserDTO other = allUserDTOS.get(4);

      // GIVEN
      newUserDTO.setEmail(null);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(other);
      String json = mapper.writeValueAsString(newUserDTO);
      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User with no password it redirect to the same page")
   void addUser_returnToTheSamePage_ofUserWithNoPassword() throws Exception {
      // GIVEN
      newUserDTO.setEmail(null);
      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User with existing email it redirect to the same page")
   void addUser_returnToTheSamePage_ofUserWithExistingEmail() throws Exception {
     // GIVEN
      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("addUser")
   @DisplayName("Verify that we add a User no matching password redirect to the same page")
   void addUser_returnToTheSamePage_ofUserWithNoMatchingPassword() throws Exception {
      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenThrow(ResourceNotFoundException.class);
      newUserDTO.setMatchingPassword("Not matching");
      String json = mapper.writeValueAsString(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/add")
                  .flashAttr("user",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_ADD))
            .andReturn();
   }

   @Test
   @Tag("editUser")
   @DisplayName("Verify that call the User update page")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void editUser_displayUserUpdatePage_ofIdentifiedUser() throws Exception {

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/edit"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("editUser")
   @DisplayName("Verify that call login page of non identified user")
   void editUser_displayLoginPage_ofNonIdentifiedUser() throws Exception {

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/edit"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_LOGIN))
            .andReturn();
   }

   @Test
   @Tag("editOtherUser")
   @DisplayName("Verify that call the User update page if the user is the Admin")
   @WithMockUser(username = ADMIN_TEST_EMAIL, password = ADMIN_TEST_PASSWORD, roles = "ADMIN")
   void editOtherUser_displayUserUpdatePage_ofUserISAdmin() throws Exception {
      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(operator);
      when(userApiProxy.findUserById(anyInt())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("editOtherUser")
   @DisplayName("Verify that call the User update page if the user is himself")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void editOtherUser_displayUserUpdatePage_ofUserISHimself() throws Exception {

      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(userApiProxy.findUserById(anyInt())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("editOtherUser")
   @DisplayName("Verify that call the login page if the user is not identified")
   void editOtherUser_displayLoginPage_ofNotIdentifiedUser() throws Exception {

      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(userApiProxy.findUserById(anyInt())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_LOGIN))
            .andReturn();
   }

   @Test
   @Tag("editOtherUser")
   @DisplayName("Verify that call the login page if the user is not himself")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void editOtherUser_displayHomePage_ofWrongIdentifiedUser() throws Exception {

      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));
      when(userApiProxy.findUserById(11)).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_LOGIN))
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that we update a User and redirect to Home page")
   void updateUser_returnHome_ofUpdatedUserData() throws Exception {

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/update/" +  newUserDTO.getId())
                  .flashAttr("userDto",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.HOME))
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that when we update a User with a wrong email it redirect to User update page")
   void updateUser_returnUpdateUserPage_ofWrongUserEmail() throws Exception {

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/update/" + newUserDTO.getId())
               .flashAttr("userDto",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that when we update a User without firstName it redirect to User update page")
   void updateUser_returnUpdateUserPage_ofUserWithNoFirstName() throws Exception {

      // GIVEN
      newUserDTO.setFirstName(null);
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/update/" + newUserDTO.getId())
                  .flashAttr("userDto",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that when we update a User without lastName it redirect to User update page")
   void updateUser_returnUpdateUserPage_ofUserWithNoLastName() throws Exception {

      // GIVEN
      newUserDTO.setLastName(null);
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/update/" + newUserDTO.getId())
                  .flashAttr("userDto",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that when we update a User without password it redirect to User update page")
   void updateUser_returnUpdateUserPage_ofUserWithNoPassword() throws Exception {

      // GIVEN
      newUserDTO.setPassword(null);
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/update/" + newUserDTO.getId())
                  .flashAttr("userDto",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("updateUser")
   @DisplayName("Verify that when we update a User without email it redirect to User update page")
   void updateUser_returnUpdateUserPage_ofUserWithNoemail() throws Exception {

      // GIVEN
      newUserDTO.setEmail(null);
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/update/" + newUserDTO.getId())
                  .flashAttr("userDto",newUserDTO))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("editPassword")
   @DisplayName("Verify that call the User password update page if the user is the Admin")
   @WithMockUser(username = ADMIN_TEST_EMAIL, password = ADMIN_TEST_PASSWORD, roles = "USER")
   void editPassword_displayUserUpdatePage_ofUserISAdmin() throws Exception {
      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(operator);
      when(userApiProxy.findUserById(11)).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/password/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE_PASSWORD))
            .andReturn();
   }

   @Test
   @Tag("editPassword")
   @DisplayName("Verify that call the User password update page if the user is himself")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void editPassword_displayUserUpdatePage_ofUserHimself() throws Exception {
      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(userApiProxy.findUserById(11)).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/password/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE_PASSWORD))
            .andReturn();
   }

   @Test
   @Tag("editPassword")
   @DisplayName("Verify that call Home page if the user is not identified")
   void editPassword_displayHomePage_ofNonIdentifiedUser() throws Exception {
      // GIVEN
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(userApiProxy.findUserById(11)).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/password/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.HOME))
            .andReturn();
   }

   @Test
   @Tag("editPassword")
   @DisplayName("Verify that call Home page if the user is not  himself")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void editPassword_displayHomePage_ofNotUserHimself() throws Exception {
      // GIVEN
      UserDTO other =  allUserDTOS.get(3);
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(other);
      when(userApiProxy.findUserById(anyInt())).thenReturn(newUserDTO);

      // WHEN
      final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/password/edit/" + newUserDTO.getId()))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.HOME))
            .andReturn();
   }

   @Test
   @Tag("updatePassword")
   @DisplayName("Verify that we update a password and redirect to User update page")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void updatePassword_returnUpdateUserPage_ofUpdatedPasswordData() throws Exception {
      Integer userId = newUserDTO.getId();

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/password/update/")
                  .flashAttr("userDto",newUserDTO)
                  .param("oldPassword", USER_TEST_PASSWORD)
                  .param("newPassword", USER_TEST_PASSWORD)
                  .param("newMatchingPassword", USER_TEST_PASSWORD))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(view().name(PathTable.USER_UPDATE))
            .andReturn();
   }

   @Test
   @Tag("updatePassword")
   @DisplayName("Verify that can't update a password if the old password is wrong and redirect to update password page")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void updatePassword_returnUpdatePasswordPage_ofWrongOldPassword() throws Exception {
      Integer userId = newUserDTO.getId();

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(false);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/password/update/")
                  .flashAttr("userDto",newUserDTO)
                  .param("oldPassword", USER_TEST_PASSWORD)
                  .param("newPassword", USER_TEST_PASSWORD)
                  .param("newMatchingPassword", USER_TEST_PASSWORD))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.USER_UPDATE_PASSWORD_R + userId))
            .andReturn();
   }

   @Test
   @Tag("updatePassword")
   @DisplayName("Verify that can't update a password if the new password and matching don't match and redirect to update password page")
   @WithMockUser(username = USER_TEST_EMAIL, password = USER_TEST_PASSWORD, roles = USER_TEST_ROLE)
   void updatePassword_returnUpdatePasswordPage_ofNotMatchingPasswords() throws Exception {
      Integer userId = newUserDTO.getId();

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/password/update/")
                  .flashAttr("userDto",newUserDTO)
                  .param("oldPassword", USER_TEST_PASSWORD)
                  .param("newPassword", USER_TEST_PASSWORD)
                  .param("newMatchingPassword", "Not matching"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.USER_UPDATE_PASSWORD_R + userId))
            .andReturn();
   }

   @Test
   @Tag("updatePassword")
   @DisplayName("Verify that can't update a password with non identified User")
   void updatePassword_returnHomePage_ofNotIdentifiedUser() throws Exception {
      Integer userId = newUserDTO.getId();

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(newUserDTO);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(newUserDTO);
      when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/password/update/")
                  .flashAttr("userDto",newUserDTO)
                  .param("oldPassword", USER_TEST_PASSWORD)
                  .param("newPassword", USER_TEST_PASSWORD)
                  .param("newMatchingPassword", USER_TEST_PASSWORD))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.HOME))
            .andReturn();
   }

   @Test
   @Tag("updatePassword")
   @DisplayName("Verify that can't update a password with wrong User")
   void updatePassword_returnHomePage_ofWrongUser() throws Exception {
      UserDTO expected = allUserDTOS.get(2);
      Integer userId = expected.getId();

      // GIVEN
      when(userApiProxy.updateUser(anyInt(),any(UserDTO.class))).thenReturn(expected);
      // set there is no other user with the same email
      when(userApiProxy.findUserByEmail(anyString())).thenReturn(allUserDTOS.get(3));
      when(bCryptPasswordEncoder.matches(anyString(),anyString())).thenReturn(true);

      // WHEN
      final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.post("/user/password/update/")
                  .flashAttr("userDto",expected)
                  .param("oldPassword", USER_TEST_PASSWORD)
                  .param("newPassword", USER_TEST_PASSWORD)
                  .param("newMatchingPassword", USER_TEST_PASSWORD))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(view().name(PathTable.HOME))
            .andReturn();
   }
}