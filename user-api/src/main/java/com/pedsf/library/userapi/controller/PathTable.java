package com.pedsf.library.userapi.controller;

public final class PathTable {
   public static final String USER_REGISTRATION = "user/registration";

   public static final String USER_ADD = "user/user-add";

   public static final String USER_READ = "user/user-read";
   public static final String USER_READ_R = "redirect:/user/read/";

   public static final String USER_UPDATE = "user/user-update";
   public static final String USER_UPDATE_R = "redirect:/user/edit/";
   public static final String USER_UPDATE_PASSWORD = "user/user-password";

   public static final    String ATTRIBUTE_ADDRESS = "addressDto";
   public static final String ATTRIBUTE_USER = "userDto";

   private PathTable() {
   }
}
