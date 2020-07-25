package com.pedsf.library.libraryapi.controller;

import com.pedsf.library.dto.business.MediaDTO;

import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/medias")
public class MediaController {
   private final MediaService mediaService;

   public MediaController(MediaService mediaService) {
      this.mediaService = mediaService;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MediaDTO>> findAllMedias(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
      List<MediaDTO> mediaDTOS;

      try {
         mediaDTOS = mediaService.findAll();
         return ResponseEntity.ok(mediaDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

   }

   @PostMapping(value = "/searches", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MediaDTO>> findAllFilteredMedia(
         @RequestParam(value = "page", defaultValue = "1") int pageNumber,
         @RequestBody MediaDTO filter) {
      List<MediaDTO> mediaDTOS;

      if (StringUtils.isEmpty(filter)) {
         filter = new MediaDTO();
      }

      try {
         mediaDTOS = mediaService.findAllFiltered(filter);
         return ResponseEntity.ok(mediaDTOS);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

   }


   @GetMapping(value = "/{mediaId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MediaDTO> findMediaById(@PathVariable("mediaId")Integer mediaId){

      try {
         MediaDTO mediaDTO = mediaService.findById(mediaId);
         return ResponseEntity.ok(mediaDTO);
      } catch (ResourceNotFoundException ex) {
         log.error(ex.getMessage());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

}
