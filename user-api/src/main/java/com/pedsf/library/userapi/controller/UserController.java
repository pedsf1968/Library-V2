package com.pedsf.library.userapi.controller;

import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.userapi.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

   private final UserService userService;

   UserController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<UserDTO>> findAllUsers(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<UserDTO> userDTOS;

      try {
         userDTOS = userService.findAll();
         return ResponseEntity.ok(userDTOS);
      } catch (ResourceNotFoundException exception) {
         log.error(exception.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<UserDTO>> findAllFilteredUsers(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody UserDTO filter) {
      List<UserDTO> userDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            userDTOS = userService.findAll();
         } else {
            userDTOS = userService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException exception) {
         log.error(exception.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      return ResponseEntity.ok(userDTOS);
   }

   @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDTO> findUserById(@PathVariable("userId")Integer userId){

      try {
         UserDTO userDto = userService.findById(userId);
         return ResponseEntity.ok(userDto);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDTO> findUserByEmail(@PathVariable("email")String email){

      try {
         UserDTO userDto = userService.findByEmail(email);
         return ResponseEntity.ok(userDto);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO)
   {

      try {
         // to avoid conflict with id
         userDTO.setId(null);
         UserDTO userCreated = userService.save(userDTO);
         return ResponseEntity.ok( userCreated);
      } catch (ConflictException ex) {
         // log exception first, then return Conflict (409)
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      } catch (BadRequestException ex) {
         // log exception first, then return Bad Request (400)
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
   }

   @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDTO> updateUser(@PathVariable( "userId" ) Integer userId,
                                             @Valid @RequestBody UserDTO userDTO) {

      try {
         userDTO = userService.update(userDTO);
         return ResponseEntity.ok(userDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }

   @PutMapping(value = "/{userId}/counter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDTO> updateUserCounter(@PathVariable("userId") Integer userId,
                                             @RequestBody Integer counter) {

      try {
         UserDTO userDTO = userService.updateCounter(userId,counter);
         return ResponseEntity.ok(userDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }


   @PutMapping(value = "/{userId}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDTO> updateUserStatus(@PathVariable("userId") Integer userId,
                                                    @RequestBody String status) {

      try {
         UserDTO userDTO = userService.updateStatus(userId,status);
         return ResponseEntity.ok(userDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }

   @DeleteMapping(value = "/{userId}")
   public ResponseEntity<Void> deleteUser(@PathVariable("userId") Integer id) {
      try {
         userService.deleteById(id);
         return ResponseEntity.ok().build();
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.notFound().build();
      }

   }
}
