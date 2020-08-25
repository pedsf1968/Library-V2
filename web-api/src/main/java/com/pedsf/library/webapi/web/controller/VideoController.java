package com.pedsf.library.webapi.web.controller;

import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.dto.*;
import com.pedsf.library.dto.filter.VideoFilter;
import com.pedsf.library.exception.*;
import com.pedsf.library.webapi.proxy.LibraryApiProxy;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.web.PathTable;
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

import java.util.*;

@Slf4j
@Controller
@RefreshScope
public class VideoController {
   private final LibraryApiProxy libraryApiProxy;
   private final UserApiProxy userApiProxy;
   private final List<String> videosTitles;
   private final Map<Integer,PersonDTO> videosDirectors = new HashMap<>();
   private final Map<Integer,PersonDTO> videosActors = new HashMap<>();

   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   public VideoController(LibraryApiProxy libraryApiProxy, UserApiProxy userApiProxy) {
      this.libraryApiProxy = libraryApiProxy;
      this.userApiProxy = userApiProxy;
      this.videosTitles = libraryApiProxy.getAllVideosTitles();

      for(PersonDTO personDTO : libraryApiProxy.getAllVideosDirectors()){
         videosDirectors.put(personDTO.getId(),personDTO);
      }

      for(PersonDTO personDTO : libraryApiProxy.getAllVideosActors()){
         videosActors.put(personDTO.getId(),personDTO);
      }
   }

   public void setBorrowingQuantityMax(Integer borrowingQuantityMax) {
      this.borrowingQuantityMax = borrowingQuantityMax;
   }

   @GetMapping("/videos")
   public String booksList(Model model, Locale locale){
      List<VideoDTO> videoDTOS = libraryApiProxy.findAllAllowedVideos(1);

      model.addAttribute(PathTable.ATTRIBUTE_VIDEOS, videoDTOS);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, videosTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_DIRECTORS, videosDirectors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_ACTORS, videosActors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, VideoType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, VideoFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, new VideoFilter());

      return PathTable.VIDEO_ALL;
   }

   @PostMapping("/videos")
   public String booksFilteredList(@ModelAttribute("filter") VideoFilter filter, Model model, Locale locale) {
      List<VideoDTO> videoDTOS;
      VideoDTO videoDTO = new VideoDTO();
      videoDTO.setTitle(filter.getTitle());
      videoDTO.setType(filter.getType());
      videoDTO.setFormat(filter.getFormat());

      if(filter.getDirectorId()!=null) {
         videoDTO.setDirector(videosDirectors.get(filter.getDirectorId()));
      }

      if(filter.getActorId()!=null) {
         videoDTO.setActors(Collections.singletonList(videosActors.get(filter.getActorId())));
      }

      log.info("filter : " + videoDTO);

      try {
         videoDTOS = libraryApiProxy.findAllFilteredVideos(1,videoDTO);
         model.addAttribute(PathTable.ATTRIBUTE_VIDEOS, videoDTOS);
      } catch (ResourceNotFoundException ex) {
         log.info("No VIDEO !");
      }

      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, videosTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_DIRECTORS, videosDirectors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_ACTORS, videosActors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, VideoType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, VideoFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, filter);

      return PathTable.VIDEO_ALL;
   }

   @GetMapping(value="/video/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public String bookView(@PathVariable("videoId") String videoId, Model model, Locale locale){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      
      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO authentifiedUser = userApiProxy.findUserByEmail(authentication.getName());
         Boolean canBorrow = (authentifiedUser.getCounter() < borrowingQuantityMax);
         model.addAttribute(PathTable.ATTRIBUTE_CAN_BORROW, canBorrow);
      }

      VideoDTO videoDTO = libraryApiProxy.findVideoById(videoId);

      model.addAttribute(PathTable.ATTRIBUTE_VIDEO, videoDTO);

      return PathTable.VIDEO_READ;
   }

}
