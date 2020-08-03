package com.pedsf.library.libraryapi.configuration;


import com.pedsf.library.libraryapi.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

   @Bean
   CustomErrorDecoder myCustomErrorDecoder(){
      return new CustomErrorDecoder();
   }
}
