package com.pedsf.library.webapi.service.global;

public interface SecurityService {
   String findLoggedInUsername();
   void autoLogin(String email, String motDePasse);
}