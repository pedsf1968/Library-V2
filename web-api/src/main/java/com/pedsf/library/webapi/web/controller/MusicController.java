package com.pedsf.library.webapi.web.controller;

import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.dto.*;
import com.pedsf.library.webapi.dto.business.MusicFilter;
import com.pedsf.library.webapi.exceptions.ResourceNotFoundException;
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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Controller
@RefreshScope
public class MusicController {
   private final LibraryApiProxy libraryApiProxy;
   private final UserApiProxy userApiProxy;
   private final List<String> musicsTitles;
   private final Map<Integer, PersonDTO> musicsAuthors = new HashMap<>();
   private final Map<Integer,PersonDTO> musicsComposers = new HashMap<>();
   private final Map<Integer,PersonDTO> musicsInterpreters = new HashMap<>();

   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   public MusicController(LibraryApiProxy libraryApiProxy, UserApiProxy userApiProxy) {
      this.libraryApiProxy = libraryApiProxy;
      this.userApiProxy = userApiProxy;
      this.musicsTitles = libraryApiProxy.getAllMusicsTitles();

      for(PersonDTO personDTO : libraryApiProxy.getAllMusicsAuthors()){
         musicsAuthors.put(personDTO.getId(),personDTO);
      }

      for(PersonDTO personDTO : libraryApiProxy.getAllMusicsComposers()){
         musicsComposers.put(personDTO.getId(),personDTO);
      }

      for(PersonDTO personDTO : libraryApiProxy.getAllMusicsInterpreters()){
         musicsInterpreters.put(personDTO.getId(),personDTO);
      }

   }

   @GetMapping("/musics")
   public String booksList(Model model, Locale locale){
      List<MusicDTO> musicDTOS = libraryApiProxy.findAllAllowedMusics(1);

      model.addAttribute(PathTable.ATTRIBUTE_MUSICS, musicDTOS);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, musicsTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_AUTHORS, musicsAuthors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_COMPOSERS, musicsComposers);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_INTERPRETERS, musicsInterpreters);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, MusicType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, MusicFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, new MusicFilter());

      return PathTable.MUSIC_ALL;
   }

   @PostMapping("/musics")
   public String booksFilteredList(@ModelAttribute("filter") MusicFilter filter, Model model, Locale locale) throws URISyntaxException {
      List<MusicDTO> musicDTOS;
      MusicDTO musicDTO = new MusicDTO();
      musicDTO.setTitle(filter.getTitle());
      musicDTO.setType(filter.getType());
      musicDTO.setFormat(filter.getFormat());

      if(filter.getAuthorId()!=null) {
         musicDTO.setAuthor(musicsAuthors.get(filter.getAuthorId()));
      }

      if(filter.getComposerId()!=null) {
         musicDTO.setComposer(musicsComposers.get(filter.getComposerId()));
      }

      if(filter.getInterpreterId()!=null) {
         musicDTO.setInterpreter(musicsInterpreters.get(filter.getInterpreterId()));
      }

      log.info("filter : " + musicDTO);

      try {
         musicDTOS = libraryApiProxy.findAllfilteredMusics(1,musicDTO);
         model.addAttribute(PathTable.ATTRIBUTE_MUSICS, musicDTOS);
      } catch (ResourceNotFoundException ex) {
         log.info("No MUSIC !");
      }

      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, musicsTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_AUTHORS, musicsAuthors);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_COMPOSERS, musicsComposers);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_INTERPRETERS, musicsInterpreters);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, MusicType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, MusicFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, filter);

      return PathTable.MUSIC_ALL;
   }

   @GetMapping(value="/music/{musicId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public String bookView(@PathVariable("musicId") String musicId, Model model, Locale locale){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      
      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO authentifiedUser = userApiProxy.findUserByEmail(authentication.getName());
         Boolean canBorrow = (authentifiedUser.getCounter() < borrowingQuantityMax);
         model.addAttribute(PathTable.ATTRIBUTE_CAN_BORROW, canBorrow);
      }

      MusicDTO musicDTO = libraryApiProxy.findMusicById(musicId);

      model.addAttribute(PathTable.ATTRIBUTE_MUSIC, musicDTO);

      return PathTable.MUSIC_READ;
   }

}
