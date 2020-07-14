package com.library.web.proxy;

import com.library.web.dto.business.*;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "library-api")
@RibbonClient(name = "library-api")
public interface LibraryApiProxy {

   // BOOK controller methods
   @GetMapping(value = "/books/allowed", produces = MediaType.APPLICATION_JSON_VALUE)
   List<BookDTO> findAllAllowedBooks(@RequestParam(value = "page", defaultValue = "1") int pageNumber);

   @PostMapping(value = "/books/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   List<BookDTO> findAllFilteredBooks(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestBody BookDTO filter);

   @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
   BookDTO findBookById(@PathVariable("bookId") String bookId);

   @GetMapping("/books/authors")
   List<PersonDTO> getAllBooksAuthors();

   @GetMapping("/books/editors")
   List<PersonDTO> getAllBooksEditors();

   @GetMapping("/books/titles")
   List<String> getAllBooksTitles();

   // GAME controller methods
   @GetMapping(value = "/games/allowed", produces = MediaType.APPLICATION_JSON_VALUE)
   List<GameDTO> findAllAllowedGames(@RequestParam(value = "page", defaultValue = "1") int pageNumber);

   @PostMapping(value = "/games/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   List<GameDTO> findAllFilteredGames(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestBody GameDTO filter);

   @GetMapping(value = "/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
   GameDTO findGameById(@PathVariable("gameId") String gameId);

   @GetMapping("/games/titles")
   List<String> getAllGamesTitles();

   // MUSIC controller methods
   @GetMapping(value = "/musics/allowed", produces = MediaType.APPLICATION_JSON_VALUE)
   List<MusicDTO> findAllAllowedMusics(@RequestParam(value = "page", defaultValue = "1") int pageNumber);

   @PostMapping(value = "/musics/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   List<MusicDTO> findAllfilteredMusics(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestBody MusicDTO filter);

   @GetMapping(value = "/musics/{musicId}", produces = MediaType.APPLICATION_JSON_VALUE)
   MusicDTO findMusicById(@PathVariable("musicId") String musicId);

   @GetMapping("/musics/authors")
   List<PersonDTO> getAllMusicsAuthors();

   @GetMapping("/musics/composers")
   List<PersonDTO> getAllMusicsComposers();

   @GetMapping("/musics/interpreters")
   List<PersonDTO> getAllMusicsInterpreters();

   @GetMapping("/musics/titles")
   List<String> getAllMusicsTitles();

   // VIDEO controller methods
   @GetMapping(value = "/videos/allowed", produces = MediaType.APPLICATION_JSON_VALUE)
   List<VideoDTO> findAllAllowedVideos(@RequestParam(value = "page", defaultValue = "1") int pageNumber);

   @PostMapping(value = "/videos/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   List<VideoDTO> findAllFilteredVideos(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestBody VideoDTO filter);

   @GetMapping(value = "/videos/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
   VideoDTO findVideoById(@PathVariable("videoId") String videoId);

   @GetMapping("/videos/directors")
   List<PersonDTO> getAllVideosDirectors();

   @GetMapping("/videos/actors")
   List<PersonDTO> getAllVideosActors();

   @GetMapping("/videos/titles")
   List<String> getAllVideosTitles();

   // BORROWING controller methods
   @GetMapping(value = "/borrowings/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   List<BorrowingDTO> findByUserIdNotReturn(@PathVariable("userId") Integer userId);

   @PostMapping(value = "/borrowings/{userId}",  produces = MediaType.APPLICATION_JSON_VALUE)
   BorrowingDTO addBorrowing(@PathVariable("userId") Integer userId, @RequestBody String mediaEan);

   @PostMapping(value = "/borrowings/{userId}/extend", produces = MediaType.APPLICATION_JSON_VALUE)
   BorrowingDTO extendBorrowing(@PathVariable("userId") Integer userId, @RequestBody Integer mediaId);

   // BOOKING controller methods
   @GetMapping(value = "/bookings/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
   List<BookingDTO> findBookingsByUser(@PathVariable("userId") Integer userId);

}
