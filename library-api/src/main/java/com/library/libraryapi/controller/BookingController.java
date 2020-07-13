package com.library.libraryapi.controller;

import com.library.libraryapi.service.BorrowingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/booking")
public class BookingController {
   private final BorrowingService borrowingService;


   public BookingController(BorrowingService borrowingService) {
      this.borrowingService = borrowingService;
   }
}
