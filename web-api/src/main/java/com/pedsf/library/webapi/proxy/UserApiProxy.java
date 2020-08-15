package com.pedsf.library.webapi.proxy;

import com.pedsf.library.dto.global.UserDTO;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "user-api")
@RibbonClient(name = "user-api")
public interface UserApiProxy {

   @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   UserDTO findUserById(@PathVariable("userId")Integer userId);

   @GetMapping(value = "/users/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
   UserDTO findUserByEmail(@PathVariable("email")String email);

   @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   UserDTO addUser(@Valid @RequestBody UserDTO userDTO);

   @PutMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   UserDTO updateUser(@PathVariable( "userId" ) Integer userId, @Valid @RequestBody UserDTO userDTO);
}
