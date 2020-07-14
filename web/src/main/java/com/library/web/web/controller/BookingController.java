package com.library.web.web.controller;

import com.library.web.dto.business.BookingDTO;
import com.library.web.dto.global.UserDTO;
import com.library.web.exceptions.ResourceNotFoundException;
import com.library.web.proxy.LibraryApiProxy;
import com.library.web.proxy.UserApiProxy;
import com.library.web.web.PathTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RefreshScope
public class BookingController {
   private final LibraryApiProxy libraryApiProxy;
   private final UserApiProxy userApiProxy;

   public BookingController(LibraryApiProxy libraryApiProxy, UserApiProxy userApiProxy) {
      this.libraryApiProxy = libraryApiProxy;
      this.userApiProxy = userApiProxy;
   }


   @GetMapping("/bookings")
   public String bookingsList(Model model, Locale locale) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
      List<BookingDTO> bookingDTOS;

      try {
         bookingDTOS = libraryApiProxy.findBookingsByUser(userDTO.getId());
         model.addAttribute(PathTable.ATTRIBUTE_BOOKINGS, bookingDTOS);
      } catch (ResourceNotFoundException ex) {
         bookingDTOS = null;
      }

      return PathTable.BOOKINGS;
   }

   @GetMapping("/booking/{mediaEan}")
   public String booking(@PathVariable("mediaEan") String mediaEan){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
         libraryApiProxy.addBorrowing(userDTO.getId(), mediaEan);
      }

      return PathTable.BOOKINGS_R;
   }

}

