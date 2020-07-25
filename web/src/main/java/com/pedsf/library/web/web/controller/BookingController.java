package com.pedsf.library.web.web.controller;

import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.web.proxy.LibraryApiProxy;
import com.pedsf.library.web.proxy.UserApiProxy;
import com.pedsf.library.web.web.PathTable;
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
      } catch (ResourceNotFoundException exception) {
         log.info(exception.getMessage());
         bookingDTOS = null;
      }

      return PathTable.BOOKINGS;
   }

   @GetMapping("/booking/{mediaEan}")
   public String booking(@PathVariable("mediaEan") String mediaEan){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
         try {
            libraryApiProxy.addBooking(userDTO.getId(), mediaEan);
         } catch (Exception exception) {
            log.info(exception.getMessage());
         }
      }

      return PathTable.BOOKINGS_R;
   }

   @GetMapping("/booking/cancel/{bookingId}")
   public String cancelBooking(@PathVariable("bookingId") Integer bookingId){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
         libraryApiProxy.cancelBooking(userDTO.getId(), bookingId);
      }

      return PathTable.BOOKINGS_R;
   }

}

