package com.library.web.web.controller;

import com.library.libraryapi.dto.business.BookDTO;
import com.library.libraryapi.dto.business.PersonDTO;
import com.library.web.dto.BookFormat;
import com.library.web.dto.BookType;
import com.library.web.dto.business.BookFilter;
import com.library.web.exceptions.ResourceNotFoundException;
import com.library.web.proxy.LibraryApiProxy;
import com.library.web.proxy.UserApiProxy;
import com.library.web.web.PathTable;
import com.user.userapi.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@Controller
@RefreshScope
public class BookController {
   private final LibraryApiProxy libraryApiProxy;
   private final UserApiProxy userApiProxy;
   private final List<String> booksTitles;
   private final Map<Integer, PersonDTO> booksAuthors = new HashMap<>();
   private final Map<Integer,PersonDTO> booksEditors = new HashMap<>();

   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   public BookController(LibraryApiProxy libraryApiProxy, UserApiProxy userApiProxy) {
      this.libraryApiProxy = libraryApiProxy;
      this.userApiProxy = userApiProxy;
      this.booksTitles = libraryApiProxy.getAllBooksTitles();

      for(PersonDTO personDTO : libraryApiProxy.getAllBooksAuthors()){
       booksAuthors.put(personDTO.getId(),personDTO);
      }

      for(PersonDTO personDTO : libraryApiProxy.getAllBooksEditors()){
         booksEditors.put(personDTO.getId(),personDTO);
      }
   }

   @GetMapping("/books")
   public String booksList(Model model, Locale locale){
      List<BookDTO> bookDTOS = libraryApiProxy.findAllAllowedBooks(1);

      model.addAttribute(PathTable.ATTRIBUTE_BOOKS, bookDTOS);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, booksTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_AUTHORS, booksAuthors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_EDITORS, booksEditors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, BookType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, BookFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, new BookFilter());

      return PathTable.BOOK_ALL;
   }

   @PostMapping("/books")
   public String booksFilteredList(@ModelAttribute("filter") BookFilter filter, Model model, Locale locale) throws URISyntaxException {
      List<BookDTO> bookDTOS;
      BookDTO bookDTO = new BookDTO();
      bookDTO.setTitle(filter.getTitle());
      bookDTO.setType(filter.getType());
      bookDTO.setFormat(filter.getFormat());

      if(filter.getAuthorId()!=null) {
         bookDTO.setAuthor(booksAuthors.get(filter.getAuthorId()));
      }

      if(filter.getEditorId()!=null) {
         bookDTO.setEditor(booksEditors.get(filter.getEditorId()));
      }

      log.info("filter : " + bookDTO);

      try {
         bookDTOS = libraryApiProxy.findAllFilteredBooks(1,bookDTO);
         model.addAttribute(PathTable.ATTRIBUTE_BOOKS, bookDTOS);
      } catch (ResourceNotFoundException ex) {
         log.info("No BOOK !");
      }

      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, booksTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_AUTHORS, booksAuthors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_EDITORS, booksEditors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, BookType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, BookFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, filter);

      return PathTable.BOOK_ALL;
   }

   @GetMapping(value="/book/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public String bookView(@PathVariable("bookId") String bookId, Model model, Locale locale){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      
      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO authentifiedUser = userApiProxy.findUserByEmail(authentication.getName());
         Boolean canBorrow = (authentifiedUser.getCounter() < borrowingQuantityMax);
         model.addAttribute(PathTable.ATTRIBUTE_CAN_BORROW, canBorrow);
      }

      BookDTO bookDTO = libraryApiProxy.findBookById(bookId);

      model.addAttribute(PathTable.ATTRIBUTE_BOOK, bookDTO);

      return PathTable.BOOK_READ;
   }

}
