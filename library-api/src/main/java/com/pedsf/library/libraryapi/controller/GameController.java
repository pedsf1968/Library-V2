package com.pedsf.library.libraryapi.controller;

import com.pedsf.library.dto.business.GameDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.service.GameService;
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
@RequestMapping("/games")
public class GameController {
   private final GameService gameService;

   public GameController(GameService gameService) {
      this.gameService = gameService;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<GameDTO>> findAllGames(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<GameDTO> gameDTOS;

      try {
         gameDTOS = gameService.findAll();
         return ResponseEntity.ok(gameDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping(value = "/allowed",produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<GameDTO>> findAllAllowedGames(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<GameDTO> gameDTOS;

      try {
         gameDTOS = gameService.findAllAllowed();
         return ResponseEntity.ok(gameDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }


   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<GameDTO>> findAllFilteredGames(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody GameDTO filter) {
      List<GameDTO> gameDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            gameDTOS = gameService.findAll();
         } else {
            gameDTOS = gameService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      return ResponseEntity.ok(gameDTOS);
   }

   @GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<GameDTO> findGameById(@PathVariable("gameId")String gameId){

      try {
         GameDTO gameDTO = gameService.findById(gameId);
         return ResponseEntity.ok(gameDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<GameDTO> addGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {

      try {
         GameDTO gameCreated = gameService.save(gameDTO);
         return ResponseEntity.created(new URI( "/book" + gameCreated.getEan())).body(gameCreated);
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

   @PutMapping(value = "/{gameId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<GameDTO> updateGame(@PathVariable( "gameId" ) String gameId, @Valid @RequestBody GameDTO gameDTO) {

      try {
         gameDTO = gameService.update(gameDTO);
         return ResponseEntity.ok(gameDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }

   @DeleteMapping("/{gameId}")
   public ResponseEntity<Void> deleteGame(@PathVariable("gameId") String gameId) {
      try {
         gameService.deleteById(gameId);
         return ResponseEntity.ok().build();
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.notFound().build();
      }
   }

   @GetMapping("/titles")
   public ResponseEntity<List<String>> getAllGamesTitles() {
      try {
         List<String> titles = gameService.findAllTitles();
         return ResponseEntity.ok(titles);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }
}
