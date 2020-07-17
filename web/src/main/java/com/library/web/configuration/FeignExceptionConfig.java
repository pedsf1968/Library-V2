package com.library.web.configuration;

import com.library.web.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

   @Bean
   CustomErrorDecoder myCustomErrorDecoder(){
      return new CustomErrorDecoder();
   }
}
