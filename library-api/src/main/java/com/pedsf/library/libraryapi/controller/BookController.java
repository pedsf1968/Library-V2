package com.pedsf.library.libraryapi.controller;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {
   private final BookService bookService;

   public BookController(BookService bookService) {
      this.bookService = bookService;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookDTO>> findAllBooks(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<BookDTO> bookDTOS;

      try {
         bookDTOS = bookService.findAll();
         log.info("bookDTOS : {}", bookDTOS);
         return ResponseEntity.ok(bookDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

   }

   @GetMapping(value = "/allowed", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookDTO>> findAllAllowedBooks(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<BookDTO> bookDTOS;

      try {
         bookDTOS = bookService.findAllAllowed();
         log.info("bookDTOS : {}", bookDTOS);
         return ResponseEntity.ok(bookDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

   }


   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<BookDTO>> findAllFilteredBooks(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody BookDTO filter) {
      List<BookDTO> bookDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            bookDTOS = bookService.findAll();
         } else {
            bookDTOS = bookService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      log.info("bookDTOS : " + bookDTOS);
      return ResponseEntity.ok(bookDTOS);
   }


   @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BookDTO> findBookById(@PathVariable("bookId") String bookId){

      try {
         BookDTO bookDTO = bookService.findById(bookId);
         return ResponseEntity.ok(bookDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) throws URISyntaxException {

      try {
         BookDTO bookCreated = bookService.save(bookDTO);
         return ResponseEntity.created(new URI( "/book" + bookCreated.getEan())).body(bookCreated);
      } catch (ConflictException ex) {
         // log exception first, then return Conflict (409)
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      } catch (BadRequestException ex) {
         // log exception first, then return Bad Request (400)
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
   }

   @PutMapping(value = "/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<BookDTO> updateBook(@PathVariable( "bookId" ) String bookId, @Valid @RequestBody BookDTO bookDTO) {

      try {
         bookDTO = bookService.update(bookDTO);
         return ResponseEntity.ok(bookDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }

   @DeleteMapping("/{bookId}")
   public ResponseEntity<Void> deleteBook(@PathVariable("bookId") String bookId) {
      try {
         bookService.deleteById(bookId);
         return ResponseEntity.ok().build();
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.notFound().build();
      }
   }

   @GetMapping("/authors")
   public ResponseEntity<List<PersonDTO>> getAllBooksAuthors() {
      try {
         List<PersonDTO> authors = (bookService.findAllAuthors());
         return ResponseEntity.ok(authors);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/editors")
   public ResponseEntity<List<PersonDTO>> getAllBooksEditors() {
      try {
         List<PersonDTO> editors = bookService.findAllEditors();
         return ResponseEntity.ok(editors);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/titles")
   public ResponseEntity<List<String>> getAllBooksTitles() {
      try {
         List<String> titles = bookService.findAllTitles();
         return ResponseEntity.ok(titles);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }
}
