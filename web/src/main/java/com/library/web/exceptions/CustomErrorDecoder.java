package com.library.web.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
   private final ErrorDecoder errorDecoder = new Default();

   @Override
   public Exception decode(String methodKey, Response response) {

      if (response.status() == 404) {
         return new ResourceNotFoundException("User not found !");
      }
      return errorDecoder.decode(methodKey,response);
   }
}
