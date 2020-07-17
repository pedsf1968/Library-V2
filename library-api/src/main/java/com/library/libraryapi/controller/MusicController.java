package com.library.libraryapi.controller;

import com.library.libraryapi.dto.business.MusicDTO;
import com.library.libraryapi.dto.business.PersonDTO;
import com.library.libraryapi.exceptions.BadRequestException;
import com.library.libraryapi.exceptions.ConflictException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.service.MusicService;
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
@RequestMapping("/musics")
public class MusicController {
   private final MusicService musicService;

   public MusicController(MusicService musicService) {
      this.musicService = musicService;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MusicDTO>> findAllMusics(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<MusicDTO> musicDTOS;

      try {
         musicDTOS = musicService.findAll();
         return ResponseEntity.ok(musicDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping(value = "/allowed",produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MusicDTO>> findAllAllowedMusics(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<MusicDTO> musicDTOS;

      try {
         musicDTOS = musicService.findAllAllowed();
         return ResponseEntity.ok(musicDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MusicDTO>> findAllfilteredMusics(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody MusicDTO filter) {
      List<MusicDTO> musicDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            musicDTOS = musicService.findAll();
         } else {
            musicDTOS = musicService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      return ResponseEntity.ok(musicDTOS);
   }

   @GetMapping(value = "/{musicId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MusicDTO> findMusicById(@PathVariable("musicId")String musicId){

      try {
         MusicDTO musicDTO = musicService.findById(musicId);
         return ResponseEntity.ok(musicDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MusicDTO> addMusic(@Valid @RequestBody MusicDTO musicDTO) throws URISyntaxException {

      try {
         MusicDTO musicCreated = musicService.save(musicDTO);
         return ResponseEntity.created(new URI( "/music" + musicCreated.getEan())).body(musicCreated);
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

   @PutMapping(value = "/{musicId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MusicDTO> updateMusic(@PathVariable( "musicId" ) Integer musicId, @Valid @RequestBody MusicDTO musicDTO) {

      try {
         musicDTO = musicService.update(musicDTO);
         return ResponseEntity.ok(musicDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }

   @DeleteMapping("/{musicId}")
   public ResponseEntity<Void> deleteMusic(@PathVariable("musicId") String musicId) {
      try {
         musicService.deleteById(musicId);
         return ResponseEntity.ok().build();
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.notFound().build();
      }
   }

   @GetMapping("/authors")
   public ResponseEntity<List<PersonDTO>> getAllMusicsAuthors() {

      try {
         List<PersonDTO> authors = (musicService.findAllAuthors());
         return ResponseEntity.ok(authors);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/composers")
   public ResponseEntity<List<PersonDTO>> getAllMusicsComposers() {

      try {
         List<PersonDTO> composers = (musicService.findAllComposers());
         return ResponseEntity.ok(composers);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/interpreters")
   public ResponseEntity<List<PersonDTO>> getAllMusicsInterpreters() {
      try {
         List<PersonDTO> interpreters = (musicService.findAllInterpreters());
         return ResponseEntity.ok(interpreters);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/titles")
   public ResponseEntity<List<String>> getAllMusicsTitles() {
      try {
         List<String> titles = musicService.findAllTitles();
         return ResponseEntity.ok(titles);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }
}
