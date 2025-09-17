package de.wxdb.wxdb_masterthesis;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
  ResponseEntity<?> handleSize(MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(413).body("File too large");
  }
 

  @ExceptionHandler(NoSuchElementException.class)
  ResponseEntity<?> handleNotFound(NoSuchElementException ex) {
    return ResponseEntity.badRequest().body("Unknown weatherStation");
  }
}