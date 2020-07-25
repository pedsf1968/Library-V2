package com.pedsf.library.libraryapi.controller;

import com.pedsf.library.libraryapi.dto.business.BorrowingDTO;
import com.pedsf.library.libraryapi.exceptions.ResourceNotFoundException;
import com.pedsf.library.libraryapi.service.BorrowingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/borrowings")
public class BorrowingController {
   private final BorrowingService borrowingService;


   public BorrowingController(BorrowingService borrowingService) {
      this.borrowingService = borrowingService;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BorrowingDTO>> findAllBorrowings(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<BorrowingDTO> borrowingDTOS;

      try {
         borrowingDTOS = borrowingService.findAll();
         return ResponseEntity.ok(borrowingDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BorrowingDTO>> findAllFilteredBorrowings(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody BorrowingDTO filter) {
      List<BorrowingDTO> borrowingDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            borrowingDTOS = borrowingService.findAll();
         } else {
            borrowingDTOS = borrowingService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      return ResponseEntity.ok(borrowingDTOS);
   }

   @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BorrowingDTO>> findByUserIdNotReturn(@PathVariable("userId") Integer userId) {
      List<BorrowingDTO> borrowingDTOS;
      try {
         borrowingDTOS = borrowingService.findByUserIdNotReturn(userId);
         return ResponseEntity.ok(borrowingDTOS);
      } catch (ResourceNotFoundException exception) {
         log.error(exception.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }


   @GetMapping(value = "/{borrowingId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BorrowingDTO> findBorrowingById(@PathVariable("borrowingId")Integer borrowingId){

      try {
         BorrowingDTO borrowingDTO = borrowingService.findById(borrowingId);
         return ResponseEntity.ok(borrowingDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }


   @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BorrowingDTO> addBorrowing(@PathVariable("userId") Integer userId,
                                                 @RequestBody String mediaEan
   ) {
      try {

         BorrowingDTO borrowingCreated = borrowingService.borrow(userId, mediaEan);
         return ResponseEntity.ok(borrowingCreated);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/{userId}/extend", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BorrowingDTO> extendBorrowing(@PathVariable("userId") Integer userId,
                                                    @RequestBody Integer mediaId
   ) {
      try {
         BorrowingDTO borrowingUpdated = borrowingService.extend(userId, mediaId);
         return ResponseEntity.ok(borrowingUpdated);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping(value = "/{userId}/{mediaId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BorrowingDTO> borrow(@PathVariable("userId") Integer userId,
                                              @PathVariable("mediaId") String mediaEan) {

      BorrowingDTO borrowingCreated = borrowingService.borrow(userId, mediaEan);

      if(borrowingCreated!=null) {
         return ResponseEntity.ok(borrowingCreated);
      } else {
         return ResponseEntity.badRequest().build();
      }
   }


   @PostMapping(value = "/{userId}/restitute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BorrowingDTO> addGiveBack(@PathVariable("userId") Integer userId,
                                                   @RequestBody Integer mediaId
   ) {
      BorrowingDTO borrowingUpdated = borrowingService.restitute(userId, mediaId);

      if(borrowingUpdated!=null) {
         return ResponseEntity.ok(borrowingUpdated);
      } else {
         return ResponseEntity.badRequest().build();
      }

   }


   @GetMapping(value = "/delayed/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BorrowingDTO>> findDelayed(
         @PathVariable(value = "date", required = true)
         @DateTimeFormat(pattern = "ddMMyyyy") Date date){
      List<BorrowingDTO> borrowingDTOS;

      try {
         borrowingDTOS = borrowingService.findDelayed( date);
         return  ResponseEntity.ok(borrowingDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

}
