package com.pedsf.library.web.web.controller;

import com.pedsf.library.dto.business.BorrowingDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.web.proxy.LibraryApiProxy;
import com.pedsf.library.web.proxy.UserApiProxy;
import com.pedsf.library.web.web.PathTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RefreshScope
public class BorrowingController {
   private final LibraryApiProxy libraryApiProxy;
   private final UserApiProxy userApiProxy;

   @Value("${library.borrowing.delay}")
   private Integer borrowingDelay;

   public BorrowingController(LibraryApiProxy libraryApiProxy, UserApiProxy userApiProxy) {
      this.libraryApiProxy = libraryApiProxy;
      this.userApiProxy = userApiProxy;
   }


   @GetMapping("/borrowings")
   public String borrowingsList(Model model, Locale locale) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
      Date restitutionDate = DateUtils.addDays(new Date(),-borrowingDelay);
      List<BorrowingDTO> borrowingDTOS;

      try {
         borrowingDTOS = libraryApiProxy.findByUserIdNotReturn(userDTO.getId());
         model.addAttribute(PathTable.ATTRIBUTE_BORROWINGS, borrowingDTOS);
      } catch (ResourceNotFoundException exception) {
         log.info(exception.getMessage());
      }

      model.addAttribute(PathTable.ATTRIBUTE_RESTITUTION_DATE, restitutionDate);

      return PathTable.BORROWINGS;
   }

   @GetMapping("/borrowing/{mediaEan}")
   public String borrowing(@PathVariable("mediaEan") String mediaEan){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
         libraryApiProxy.addBorrowing(userDTO.getId(), mediaEan);
      }

      return PathTable.BORROWINGS_R;
   }

   @GetMapping("/borrowings/extend/{mediaId}")
   public String extend(@PathVariable("mediaId") Integer mediaId){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO userDTO = userApiProxy.findUserByEmail(authentication.getName());
         libraryApiProxy.extendBorrowing(userDTO.getId(), mediaId);
      }

      return PathTable.BORROWINGS_R;
   }

   @GetMapping("/help")
   public String help(Model model){
      return PathTable.HELP;
   }
}

