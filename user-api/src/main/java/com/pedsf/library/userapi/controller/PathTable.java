package com.pedsf.library.userapi.controller;

public interface PathTable {
   String USER_REGISTRATION = "user/registration";

   String USER_ADD = "user/user-add";

   String USER_READ = "user/user-read";
   String USER_READ_R = "redirect:/user/read/";

   String USER_UPDATE = "user/user-update";
   String USER_UPDATE_R = "redirect:/user/edit/";
   String USER_UPDATE_PASSWORD = "user/user-password";

   String ATTRIBUTE_ADDRESS = "addressDto";
   String ATTRIBUTE_USER = "userDto";
}
