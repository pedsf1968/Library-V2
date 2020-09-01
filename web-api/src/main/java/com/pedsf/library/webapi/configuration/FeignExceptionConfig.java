package com.pedsf.library.webapi.configuration;

import com.pedsf.library.webapi.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

   @Bean
   CustomErrorDecoder myCustomErrorDecoder(){
      return new CustomErrorDecoder();
   }
}
