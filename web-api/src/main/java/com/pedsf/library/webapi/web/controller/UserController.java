package com.pedsf.library.webapi.web.controller;

import com.google.common.collect.Lists;
import com.pedsf.library.exception.*;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.service.global.SecurityServiceImpl;
import com.pedsf.library.webapi.web.PathTable;
import com.pedsf.library.dto.global.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@Slf4j
public class UserController {
   private static final String ROLE_ADMIN = "ROLE_ADMIN";
   private static final String ROLE_USER = "ROLE_USER";

   private final UserApiProxy userApiProxy;
   private final SecurityServiceImpl securityService;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;

   public UserController(UserApiProxy userApiProxy, SecurityServiceImpl securityService, BCryptPasswordEncoder bCryptPasswordEncoder) {
      this.userApiProxy = userApiProxy;
      this.securityService = securityService;
      this.bCryptPasswordEncoder = bCryptPasswordEncoder;
   }


   @GetMapping("/login")
   public String login(Model model, String error, String logout) {

      if (error != null)
         model.addAttribute("error", "Your email and password is invalid.");

      if (logout != null)
         model.addAttribute("message", "You have been logged out successfully.");

      return PathTable.USER_LOGIN;
   }

   @PostMapping("/login")
   public String postLogin(Model model){

      return PathTable.HOME;
   }

   @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication!= null){
         new SecurityContextLogoutHandler().logout(request, response, authentication);
      }

      return PathTable.USER_LOGIN_R;
   }

   @GetMapping({"/", "/index"})
   public String home(Model model) {
      return "index";
   }

   @GetMapping("/user/new")
   public String newUser(Model model) {
      UserDTO userDTO = new UserDTO();
      userDTO.setPhotoLink("avatar.png");
      userDTO.setCounter(0);
      model.addAttribute(PathTable.ATTRIBUTE_USER, userDTO);

      log.info("/user/new user : " + userDTO);
      return PathTable.USER_ADD;
   }

   @PostMapping("/user/add")
   public String addUser(@ModelAttribute("user") @Valid UserDTO userDTO, @NotNull BindingResult bindingResultUser,
                         Model model) {

      if (bindingResultUser.hasErrors()) {
         return PathTable.USER_ADD;
      }

      try {
         userApiProxy.findUserByEmail(userDTO.getEmail());
         bindingResultUser.rejectValue("email", "5", "Email already exist !");
         return PathTable.USER_ADD;
      } catch (ResourceNotFoundException ex) {
         log.info("Email not used !");
      }

      if(!userDTO.getPassword().equals(userDTO.getMatchingPassword())){
         bindingResultUser.rejectValue("matchingPassword", "7", "Bad matching password !");
         return PathTable.USER_ADD;
      }

      // set default role
      userDTO.setRoles(Lists.newArrayList(ROLE_USER));

      log.info("/user/add user : " + userDTO);

      // encode password because we get clear password
      userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
      userDTO.setMatchingPassword(userDTO.getPassword());

      // create user
      userDTO = userApiProxy.addUser(userDTO);

      // login using email
      securityService.autoLogin(userDTO.getEmail(), userDTO.getMatchingPassword());

      return PathTable.USER_UPDATE_R + userDTO.getId();
   }

   @GetMapping("/user/edit")
   public String editUser( Model model){
      // get the authentified user and his address
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      try {
         UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());

         log.info("/user/edit user : " + userDTO);

         model.addAttribute(PathTable.ATTRIBUTE_USER, userDTO);

         return PathTable.USER_UPDATE;
      } catch (NullPointerException exception) {
         // not identified
         return PathTable.USER_LOGIN;
      }

   }

   @GetMapping("/user/edit/{userId}")
   public String editOtherUser(@PathVariable("userId") Integer userId, Model model){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      try {
         // get the authentified user and his address
         UserDTO userDTO = userApiProxy.findUserById(userId);
         UserDTO operator = userApiProxy.findUserByEmail(authentication.getName());

         if (userDTO.equals(operator) || operator.getRoles().contains(ROLE_ADMIN)) {
            log.info("/user/edit/" + userId + " user : " + userDTO);

            model.addAttribute(PathTable.ATTRIBUTE_USER, userDTO);

            return PathTable.USER_UPDATE;
         }
      } catch (NullPointerException exception) {
         // not identified
         return PathTable.USER_LOGIN;
      }

      return PathTable.HOME;
   }

   @PostMapping("/user/update/{userId}")
   public String updateUser(@PathVariable("userId") Integer userId, @ModelAttribute("userDto") @Valid UserDTO userDTO, @NotNull BindingResult bindingResultUser,
                          Model model) {

      if (bindingResultUser.hasErrors() ) {
         return PathTable.USER_UPDATE;
      }

      // test if the email is valid
      UserDTO otherUser = userApiProxy.findUserByEmail(userDTO.getEmail());
      if(otherUser!=null && !otherUser.getId().equals(userDTO.getId())){
            bindingResultUser.rejectValue("email", "5", "Email already exist !");
            return PathTable.USER_UPDATE;
      }

      userDTO.setRoles(Lists.newArrayList(ROLE_USER));

      userApiProxy.updateUser(userId, userDTO);

      return PathTable.HOME;
   }


   @GetMapping("/user/password/edit/{userId}")
   public String editPassword(@PathVariable("userId") Integer userId, Model model){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      try {
         // get the authentified user and his address
         UserDTO userDTO = userApiProxy.findUserById(userId);
         UserDTO operator = userApiProxy.findUserByEmail(authentication.getName());

         if (userDTO.equals(operator) || operator.getRoles().contains(ROLE_ADMIN)) {
            log.info("/user/edit user : " + userDTO);
            model.addAttribute(PathTable.ATTRIBUTE_USER, userDTO);

            return PathTable.USER_UPDATE_PASSWORD;
         }
      } catch (NullPointerException exception) {
         log.error(exception.getMessage());
      }

      return PathTable.HOME;
   }

   @PostMapping("/user/password/update")
   public String updatePassword(@ModelAttribute("userDto") @Valid UserDTO userDTO, BindingResult bindingResultUser,
                                @Param("oldPassword") String oldPassword,
                                @Param("newPassword") String newPassword,
                                @Param("newMatchingPassword") String newMatchingPassword, Model model) {

      // verify the matching with old password
      if(!bCryptPasswordEncoder.matches(oldPassword,userDTO.getPassword())){
         bindingResultUser.rejectValue("password", "6", "Bad password !");
         return PathTable.USER_UPDATE_PASSWORD_R + userDTO.getId();
      }

      // verify matching between two password
      if(!newPassword.equals(newMatchingPassword)){
         bindingResultUser.rejectValue("password", "7", "Bad matching password !");
         return PathTable.USER_UPDATE_PASSWORD_R + userDTO.getId();
      }

      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

         // get the authentified user and his address
         UserDTO operator = userApiProxy.findUserByEmail(authentication.getName());


         if (operator.equals(userDTO) || operator.getRoles().contains(ROLE_ADMIN)) {

            // encode password
            userDTO.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userDTO.setMatchingPassword(userDTO.getPassword());

            userApiProxy.updateUser(userDTO.getId(), userDTO);
            securityService.autoLogin(userDTO.getEmail(), newPassword);

            model.addAttribute(PathTable.ATTRIBUTE_USER, userDTO);

            return PathTable.USER_UPDATE;
         }
      } catch (NullPointerException exception) {
         log.error(exception.getMessage());
      }
      return PathTable.HOME;
   }


}
