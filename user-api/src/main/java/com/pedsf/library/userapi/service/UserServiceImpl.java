package com.pedsf.library.userapi.service;

import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.userapi.repository.RoleRepository;
import com.pedsf.library.userapi.repository.UserRepository;
import com.pedsf.library.userapi.repository.UserSpecification;
import com.pedsf.library.userapi.model.Role;
import com.pedsf.library.userapi.model.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service("UserService")
public class UserServiceImpl implements UserService {
   private static final String ALREADY_EXIST = " already exists";
   private static final String CANNOT_FIND_WITH_ID = "Cannot find User with the id : ";
   private static final String CANNOT_FIND_WITH_EMAIL = "Cannot find User with the email : ";
   private static final String CANNOT_SAVE ="Failed to save User";

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;


   private final ModelMapper modelMapper = new ModelMapper();

   public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
      this.userRepository = userRepository;
      this.roleRepository = roleRepository;
   }

   @Override
   public User userDTOtoUser(UserDTO userDto) {
      User user = modelMapper.map(userDto, User.class);

      if( userDto.getRoles()!= null) {
         Set<Role> roles = new HashSet<>();

         for (String role : userDto.getRoles()) {
            roles.add(roleRepository.findByName(role));
         }

         user.setRoles(roles);
      }

      return user;
   }

   @Override
   public UserDTO userToUserDTO(User user) {
      UserDTO userDto = modelMapper.map(user, UserDTO.class);

      userDto.setMatchingPassword(user.getPassword());

      if( user.getRoles()!= null) {
         List<String> roles = new ArrayList<>();

         for (Role role : user.getRoles()) {
            roles.add(role.getName());
         }

         userDto.setRoles(roles);
      }

      return userDto;
   }

   @Override
   public UserDTO updateCounter(Integer userId, Integer counter) {
      userRepository.updateCounter(userId,counter);
      return findById(userId);
   }

   @Override
   public UserDTO updateStatus(Integer userId, String status) {
      userRepository.updateStatus(userId,status);
      return findById(userId);
   }

   @Override
   public boolean existsById(Integer id) {
      return userRepository.existsById(id);
   }

   @Override
   public boolean existsByEmail(String email) {
      return userRepository.findByEmail(email)!=null;
   }


   @Override
   public UserDTO findById(Integer id) {
      User user = userRepository.findById(id).orElse(null);

      if (user!=null) {
         return userToUserDTO(user);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      }
   }

   @Override
   public UserDTO findByEmail(String email) {
      User user = userRepository.findByEmail(email);

      if (user!=null) {
         return userToUserDTO(user);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_EMAIL + email);
      }
   }

   @Override
   public List<UserDTO> findAll() {
      List<UserDTO> userDTOS = new ArrayList<>();

      for( User user: userRepository.findAll()) {
         userDTOS.add(userToUserDTO(user));
      }

      if (!userDTOS.isEmpty()) {
         return userDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<UserDTO> findAllFiltered(UserDTO filter) {
      List<UserDTO> userDTOS = new ArrayList<>();

      Specification<User> spec = new UserSpecification(userDTOtoUser(filter));

      for( User u: userRepository.findAll(spec)) {
         userDTOS.add(userToUserDTO(u));
      }

      if (!userDTOS.isEmpty()) {
         return userDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public Integer getFirstId(UserDTO filter){

      return findAllFiltered(filter).get(0).getId();
   }


   @Override
   public UserDTO save(UserDTO userDto) {
      if (  !StringUtils.isEmpty(userDto.getFirstName()) &&
            !StringUtils.isEmpty(userDto.getLastName()) &&
            !StringUtils.isEmpty(userDto.getEmail())) {
         // can't overwrite user
         if ( userDto.getId()!=null && existsById(userDto.getId())) {
            throw new ConflictException("User with id : " + userDto.getId() +  ALREADY_EXIST);
         }

         // email is unique for identification
         if (userDto.getEmail() != null && existsByEmail(userDto.getEmail())) {
            throw new ConflictException("User with email : " + userDto.getEmail() +  ALREADY_EXIST);
         }

         User user = userDTOtoUser(userDto);
         user = userRepository.save(user);


         return userToUserDTO(user);
      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public UserDTO update(UserDTO userDto) {

      if (  !StringUtils.isEmpty(userDto.getFirstName()) &&
            !StringUtils.isEmpty(userDto.getId()) &&
            !StringUtils.isEmpty(userDto.getLastName()) &&
            !StringUtils.isEmpty(userDto.getEmail())) {

         if (!existsById(userDto.getId())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + userDto.getId());
         }

         User user = userRepository.save(userDTOtoUser(userDto));

         return userToUserDTO(user);
      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public void deleteById(Integer id) {
      if ( !existsById(id)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      } else {
         userRepository.deleteById(id);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(userRepository.count());
   }
}