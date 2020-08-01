package com.pedsf.library.libraryapi.controller;

import com.pedsf.library.dto.business.BookingDTO;

import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
public class BookingController {
   private final BookingService bookingService;

   public BookingController(BookingService bookingService) {
      this.bookingService = bookingService;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookingDTO>> findAllBorrowings(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<BookingDTO> bookingDTOS;

      try {
         bookingDTOS = bookingService.findAll();
         return ResponseEntity.ok(bookingDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookingDTO>> findAllFilteredBorrowings(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody BookingDTO filter) {
      List<BookingDTO> bookingDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            bookingDTOS = bookingService.findAll();
         } else {
            bookingDTOS = bookingService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      return ResponseEntity.ok(bookingDTOS);
   }

   @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookingDTO>> findBookingsByUserId(@PathVariable("userId") Integer userId) {
      List<BookingDTO> bookingDTOS;

      try {
         bookingDTOS = bookingService.findBookingsByUserId(userId);
         return ResponseEntity.ok(bookingDTOS);
      } catch (ResourceNotFoundException exception) {
         log.error(exception.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

   }

   @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BookingDTO> addBooking(@PathVariable("userId") Integer userId,
                                                    @RequestBody String mediaEan ) {
      try {
         BookingDTO bookingCreated = bookingService.booking(userId, mediaEan);
         return ResponseEntity.ok(bookingCreated);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/cancel/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BookingDTO> cancelBooking(@PathVariable("userId") Integer userId,
                                                @RequestBody Integer bookingId ) {
      try {
         BookingDTO bookingCanceled = bookingService.cancelBooking( bookingId);
         return ResponseEntity.ok(bookingCanceled);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping(value = "/pickup", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookingDTO>> findReadyToPickUp(){
      List<BookingDTO> bookingDTOS;

      try {
         bookingDTOS = bookingService.findReadyToPickUp();
         return  ResponseEntity.ok(bookingDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping(value = "/release")
   public ResponseEntity.BodyBuilder cancelOutOfDate(){
         bookingService.cancelOutOfDate();
         return ResponseEntity.status(HttpStatus.OK);
   }

}
