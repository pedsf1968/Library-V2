package com.pedsf.library.libraryapi.controller;

import com.pedsf.library.libraryapi.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.dto.business.VideoDTO;
import com.pedsf.library.libraryapi.exceptions.BadRequestException;
import com.pedsf.library.libraryapi.exceptions.ConflictException;
import com.pedsf.library.libraryapi.exceptions.ResourceNotFoundException;
import com.pedsf.library.libraryapi.service.VideoService;
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
@RequestMapping("/videos")
public class VideoController {
   private final VideoService videoService;

   public VideoController(VideoService videoService) {
      this.videoService = videoService;
   }

   @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<VideoDTO>> findAllVideos(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {

      List<VideoDTO> videoDTOS;

      try {
         videoDTOS = videoService.findAll();
         return ResponseEntity.ok(videoDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping( value = "/allowed",produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<VideoDTO>> findAllAllowedVideos(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {

      List<VideoDTO> videoDTOS;

      try {
         videoDTOS = videoService.findAllAllowed();
         return ResponseEntity.ok(videoDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<VideoDTO>> findAllFilteredVideos(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody VideoDTO filter) {
      List<VideoDTO> videoDTOS;

      try {
         if (StringUtils.isEmpty(filter)) {
            videoDTOS = videoService.findAll();
         } else {
            videoDTOS = videoService.findAllFiltered(filter);
         }
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      return ResponseEntity.ok(videoDTOS);

   }

   @GetMapping(value = "/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<VideoDTO> findVideoById(@PathVariable("videoId")String videoId){

      try {
         VideoDTO videoDTO = videoService.findById(videoId);
         return ResponseEntity.ok(videoDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<VideoDTO> addVideo(@Valid @RequestBody VideoDTO videoDTO) throws URISyntaxException {

      try {
         VideoDTO videoCreated = videoService.save(videoDTO);
         return ResponseEntity.created(new URI( "/book" + videoCreated.getEan())).body(videoCreated);
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

   @PutMapping(value = "/{videoId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<VideoDTO> updateVideo(@PathVariable( "videoId" ) Integer videoId, @Valid @RequestBody VideoDTO videoDTO) {

      try {
         videoDTO = videoService.update(videoDTO);
         return ResponseEntity.ok(videoDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      } catch (ConflictException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
   }

   @DeleteMapping("/{videoId}")
   public ResponseEntity<Void> deleteVideo(@PathVariable("videoId") String videoId) {
      try {
         videoService.deleteById(videoId);
         return ResponseEntity.ok().build();
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.notFound().build();
      }
   }


   @GetMapping("/directors")
   public ResponseEntity<List<PersonDTO>> getAllVideosDirectors() {

      try {
         List<PersonDTO> directors = (videoService.findAllDirectors());
         return ResponseEntity.ok(directors);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/actors")
   public ResponseEntity<List<PersonDTO>> getAllVideosActors() {

      try {
         List<PersonDTO> actors = (videoService.findAllActors());
         return ResponseEntity.ok(actors);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @GetMapping("/titles")
   public ResponseEntity<List<String>> getAllVideosTitles() {
      try {
         List<String> titles = videoService.findAllTitles();
         return ResponseEntity.ok(titles);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }
}
