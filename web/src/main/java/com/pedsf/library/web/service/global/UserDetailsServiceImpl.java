package com.pedsf.library.web.service.global;

import com.pedsf.library.web.proxy.UserApiProxy;
import com.pedsf.library.userapi.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
   private final UserApiProxy userApiProxy;

   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   public UserDetailsServiceImpl(UserApiProxy userApiProxy) {
      this.userApiProxy = userApiProxy;
   }


   @Override
   public UserDetails loadUserByUsername(String email) {

      UserDTO userDTO = userApiProxy.findUserByEmail(email);

      if (userDTO == null) throw new UsernameNotFoundException("User not found with email :" + email);

      Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

      for (String role : userDTO.getRoles()){
         grantedAuthorities.add(new SimpleGrantedAuthority(role));
      }

      return new org.springframework.security.core.userdetails.User(userDTO.getEmail(), userDTO.getMatchingPassword(), grantedAuthorities);

   }

}