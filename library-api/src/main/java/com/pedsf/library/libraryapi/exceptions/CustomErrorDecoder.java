package com.pedsf.library.libraryapi.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
   private final ErrorDecoder errorDecoder = new Default();

   @Override
   public Exception decode(String methodKey, Response response) {

      if (response.status() == 400) {
         return new BadRequestException(response.reason());
      } else if (response.status() == 404) {
         return new ResourceNotFoundException(response.reason());
      } else if (response.status() == 409) {
         return new ConflictException(response.reason());
      }
      return errorDecoder.decode(methodKey,response);
   }
}
