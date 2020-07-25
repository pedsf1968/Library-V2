package com.pedsf.library.libraryapi.service;

import com.pedsf.library.libraryapi.dto.business.GameDTO;
import com.pedsf.library.libraryapi.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.exceptions.BadRequestException;
import com.pedsf.library.libraryapi.exceptions.ConflictException;
import com.pedsf.library.libraryapi.exceptions.ResourceNotFoundException;
import com.pedsf.library.libraryapi.model.Game;
import com.pedsf.library.libraryapi.repository.GameRepository;
import com.pedsf.library.libraryapi.repository.GameSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("GameService")
public class GameService implements GenericMediaService<GameDTO,Game,String> {
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Game with the EAN : ";
   private static final String CANNOT_SAVE ="Failed to save Game";

   private final GameRepository gameRepository;
   private final PersonService personService;

   private final ModelMapper modelMapper = new ModelMapper();

   public GameService(GameRepository gameRepository, PersonService personService) {
      this.gameRepository = gameRepository;
      this.personService = personService;
   }

   @Override
   public boolean existsById(String ean) {
      return gameRepository.findByEan(ean).isPresent();
   }

   @Override
   public GameDTO findById(String ean) {

      if (existsById(ean)) {
         Game game = gameRepository.findByEan(ean).orElse(null);
         return entityToDTO(game);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + ean);
      }

   }

   @Override
   public List<GameDTO> findAll() {
      List<Game> games = gameRepository.findAll();
      List<GameDTO> gameDTOS = new ArrayList<>();

      for (Game game: games){
         gameDTOS.add(entityToDTO(game));
      }

      if (!gameDTOS.isEmpty()) {
         return gameDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<GameDTO> findAllAllowed() {
      List<Game> games = gameRepository.findAllAllowed();
      List<GameDTO> gameDTOS = new ArrayList<>();

      for (Game game: games){
         gameDTOS.add(entityToDTO(game));
      }

      if (!gameDTOS.isEmpty()) {
         return gameDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<GameDTO> findAllFiltered(GameDTO filter) {
      Specification<Game> spec = new GameSpecification(dtoToEntity(filter));

      List<Game> games = gameRepository.findAll(spec);
      List<GameDTO> gameDTOS = new ArrayList<>();

      for (Game game: games){
         gameDTOS.add(entityToDTO(game));
      }

      if (!gameDTOS.isEmpty()) {
         return gameDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public String getFirstId(GameDTO filter){

      return findAllFiltered(filter).get(0).getEan();
   }

   @Override
   public GameDTO save(GameDTO gameDTO) {
      if (  !StringUtils.isEmpty(gameDTO.getEan()) &&
            !StringUtils.isEmpty(gameDTO.getTitle()) &&
            !StringUtils.isEmpty(gameDTO.getType()) &&
            !StringUtils.isEmpty(gameDTO.getFormat())) {

         try {
            // try to find existing Game
            String ean = getFirstId(gameDTO);
            Game game = gameRepository.findByEan(ean).orElse(null);
            return entityToDTO(game);
         } catch (ResourceNotFoundException ex) {
            return entityToDTO(gameRepository.save(dtoToEntity(gameDTO)));
         }
      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }

   }

   @Override
   public GameDTO update(GameDTO gameDTO) {
      if (  !StringUtils.isEmpty(gameDTO.getEan()) &&
            !StringUtils.isEmpty(gameDTO.getTitle()) &&
            !StringUtils.isEmpty(gameDTO.getType()) &&
            !StringUtils.isEmpty(gameDTO.getFormat())) {
         if (!existsById(gameDTO.getEan())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + gameDTO.getEan());
         }
         Game game = gameRepository.save(dtoToEntity(gameDTO));

         return entityToDTO(game);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }

   }

   @Override
   public void deleteById(String ean) {
      if (!existsById(ean)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + ean);
      } else {
         gameRepository.deleteByEan(ean);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(gameRepository.count());
   }

   @Override
   public GameDTO entityToDTO(Game game) {
      GameDTO gameDTO = modelMapper.map(game, GameDTO.class);
      PersonDTO editor = personService.findById(game.getEditorId());

      gameDTO.setEditor(editor);

      return gameDTO;
   }

   @Override
   public Game dtoToEntity(GameDTO gameDTO) {
      Game game = modelMapper.map(gameDTO, Game.class);

      if(gameDTO.getEditor()!=null) {
         game.setEditorId(gameDTO.getEditor().getId());
      }
      return game;
   }

   public List<String> findAllTitles() {
      return gameRepository.findAllTitles();
   }

   void increaseStock(String ean) {
      gameRepository.increaseStock(ean);
   }

   void decreaseStock(String ean) {
      gameRepository.decreaseStock(ean);
   }

}
