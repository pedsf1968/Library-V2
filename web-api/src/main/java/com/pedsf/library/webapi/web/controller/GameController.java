package com.pedsf.library.webapi.web.controller;

import com.pedsf.library.dto.*;
import com.pedsf.library.webapi.dto.business.GameFilter;
import com.pedsf.library.webapi.exceptions.ResourceNotFoundException;
import com.pedsf.library.webapi.proxy.LibraryApiProxy;
import com.pedsf.library.webapi.proxy.UserApiProxy;
import com.pedsf.library.webapi.web.PathTable;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.dto.global.UserDTO;
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
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RefreshScope
public class GameController {
   private final LibraryApiProxy libraryApiProxy;
   private final UserApiProxy userApiProxy;
   private final List<String> gamesTitles;

   @Value("${library.borrowing.quantity.max}")
   private Integer borrowingQuantityMax;

   public GameController(LibraryApiProxy libraryApiProxy, UserApiProxy userApiProxy) {
      this.libraryApiProxy = libraryApiProxy;
      this.userApiProxy = userApiProxy;
      this.gamesTitles = libraryApiProxy.getAllGamesTitles();
   }


   @GetMapping("/games")
   public String booksList(Model model, Locale locale){
      List<GameDTO> gameDTOS = libraryApiProxy.findAllAllowedGames(1);

      model.addAttribute(PathTable.ATTRIBUTE_GAMES, gameDTOS);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, gamesTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, GameType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, GameFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, new GameFilter());

      return PathTable.GAME_ALL;
   }

   @PostMapping("/games")
   public String booksFilteredList(@ModelAttribute("filter") GameFilter filter, Model model, Locale locale) throws URISyntaxException {
      List<GameDTO> gameDTOS;
      GameDTO gameDTO = new GameDTO();
      gameDTO.setTitle(filter.getTitle());
      gameDTO.setType(filter.getType());
      gameDTO.setFormat(filter.getFormat());

      log.info("filter : " + gameDTO);

      try {
         gameDTOS = libraryApiProxy.findAllFilteredGames(1,gameDTO);
         model.addAttribute(PathTable.ATTRIBUTE_GAMES, gameDTOS);
      } catch (ResourceNotFoundException ex) {
         log.info("No GAME !");
      }

      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TITLES, gamesTitles);
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_TYPES, GameType.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER_FORMATS, GameFormat.values());
      model.addAttribute(PathTable.ATTRIBUTE_FILTER, filter);

      return PathTable.GAME_ALL;
   }

   @GetMapping(value="/game/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public String bookView(@PathVariable("gameId") String gameId, Model model, Locale locale){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      
      if(!authentication.getName().equals("anonymousUser")) {
         UserDTO authentifiedUser = userApiProxy.findUserByEmail(authentication.getName());
         Boolean canBorrow = (authentifiedUser.getCounter() < borrowingQuantityMax);
         model.addAttribute(PathTable.ATTRIBUTE_CAN_BORROW, canBorrow);
      }

      GameDTO gameDTO = libraryApiProxy.findGameById(gameId);

      model.addAttribute(PathTable.ATTRIBUTE_GAME, gameDTO);

      return PathTable.GAME_READ;
   }

}
