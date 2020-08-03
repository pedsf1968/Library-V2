package com.pedsf.library.web.configuration;

import com.pedsf.library.web.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

   @Bean
   CustomErrorDecoder myCustomErrorDecoder(){
      return new CustomErrorDecoder();
   }
}
