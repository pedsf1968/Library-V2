package com.pedsf.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class FileStorageException extends RuntimeException {
   public FileStorageException() {
      super();
   }

   public FileStorageException(final String message, final Throwable cause) {
      super(message, cause);
   }

   public FileStorageException(final String message) {
      super(message);
   }

   public FileStorageException(final Throwable cause) {
      super(cause);
   }

}
