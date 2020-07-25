package com.pedsf.library.mailapi.configuration;



import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

   @Bean
   public OpenAPI customOpenAPI() {
      return new OpenAPI()
         .components(new Components())
         .info(new Info()
         .title("Mail API")
         .description("Mail management with Kafka consumer and RESTful controller using springdoc-openapi and OpenAPI 3."));
   }

}
