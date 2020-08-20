package com.pedsf.library.userapi.service;

import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.userapi.model.User;

import java.util.List;

public interface UserService {
   boolean existsById(Integer id);
   boolean existsByEmail(String email);
   UserDTO findById(Integer id);
   UserDTO findByEmail(String email);
   Integer getFirstId(UserDTO filter);
   List<UserDTO> findAll();
   List<UserDTO> findAllFiltered(UserDTO filter);
   UserDTO save(UserDTO userDto);
   UserDTO update(UserDTO userDto);
   UserDTO updateCounter(Integer userId, Integer counter);
   UserDTO updateStatus(Integer userId, String status);

   void deleteById(Integer id);
   Integer count();
   User userDTOtoUser(UserDTO userDTO);
   UserDTO userToUserDTO(User user);

}
