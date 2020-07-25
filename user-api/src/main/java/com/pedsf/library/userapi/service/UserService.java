package com.pedsf.library.userapi.service;

import com.pedsf.library.userapi.model.User;
import com.pedsf.library.userapi.dto.UserDTO;

import java.util.List;

public interface UserService {
   boolean existsById(Integer id);
   boolean existsByEmail(String email);
   UserDTO findById(Integer id);
   UserDTO findByStatus(String status);
   Integer getFirstId(UserDTO filter);
   UserDTO findByEmail(String email);
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
