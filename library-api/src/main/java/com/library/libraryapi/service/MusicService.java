package com.library.libraryapi.service;

import com.library.libraryapi.dto.business.MusicDTO;
import com.library.libraryapi.dto.business.PersonDTO;
import com.library.libraryapi.exceptions.BadRequestException;
import com.library.libraryapi.exceptions.ConflictException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.model.MediaType;
import com.library.libraryapi.model.Music;
import com.library.libraryapi.repository.MusicRepository;
import com.library.libraryapi.repository.MusicSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("MusicService")
public class MusicService implements GenericService<MusicDTO,Music,Integer>{
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Music with the id : ";
   private static final String CANNOT_SAVE ="Failed to save Music";

   private final MusicRepository musicRepository;
   private final PersonService personService;

   private final ModelMapper modelMapper = new ModelMapper();

   public MusicService(MusicRepository musicRepository, PersonService personService) {
      this.musicRepository = musicRepository;
      this.personService = personService;
   }

   @Override
   public boolean existsById(Integer id) {
      return musicRepository.findById(id).isPresent();
   }

   @Override
   public MusicDTO findById(Integer id) {

      if(existsById(id)) {
         Music music = musicRepository.findById(id).orElse(null);
         return entityToDTO(music);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      }
   }

   @Override
   public List<MusicDTO> findAll() {
      List<Music> musics = musicRepository.findAll();
      List<MusicDTO> musicDTOS = new ArrayList<>();

      for (Music music: musics){
         musicDTOS.add(entityToDTO(music));
      }

      if (!musicDTOS.isEmpty()) {
         return musicDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<MusicDTO> findAllFiltered(MusicDTO filter) {
      Specification<Music> spec = new MusicSpecification(dtoToEntity(filter));

      List<Music> musics = musicRepository.findAll(spec);
      List<MusicDTO> musicDTOS = new ArrayList<>();

      for (Music music: musics){
         musicDTOS.add(entityToDTO(music));
      }

      if (!musicDTOS.isEmpty()) {
         return musicDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public Integer getFirstId(MusicDTO filter) {
      return findAllFiltered(filter).get(0).getId();
   }

   @Override
   public MusicDTO save(MusicDTO musicDTO) {
      if (   !StringUtils.isEmpty(musicDTO.getId()) &&
            !StringUtils.isEmpty(musicDTO.getTitle()) &&
            !StringUtils.isEmpty(musicDTO.getAuthor()) &&
            !StringUtils.isEmpty(musicDTO.getComposer()) &&
            !StringUtils.isEmpty(musicDTO.getInterpreter()) &&
            !StringUtils.isEmpty(musicDTO.getType()) &&
            !StringUtils.isEmpty(musicDTO.getFormat())) {

         try {
            Integer musicId = getFirstId(musicDTO);
            return entityToDTO(musicRepository.findById(musicId).orElse(null));

         } catch (ResourceNotFoundException ex) {
            musicDTO.setId(null);
            return entityToDTO(musicRepository.save(dtoToEntity(musicDTO)));
         }

      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public MusicDTO update(MusicDTO musicDTO) {
      if (  !StringUtils.isEmpty(musicDTO.getId()) &&
            !StringUtils.isEmpty(musicDTO.getTitle()) &&
            !StringUtils.isEmpty(musicDTO.getAuthor()) &&
            !StringUtils.isEmpty(musicDTO.getComposer()) &&
            !StringUtils.isEmpty(musicDTO.getInterpreter()) &&
            !StringUtils.isEmpty(musicDTO.getType()) &&
            !StringUtils.isEmpty(musicDTO.getFormat())) {

         if (!existsById(musicDTO.getId())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + musicDTO.getId());
         }
         Music music = musicRepository.save(dtoToEntity(musicDTO));

         return entityToDTO(music);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }

   }

   @Override
   public void deleteById(Integer id) {
      if (!existsById(id)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      } else {
         musicRepository.deleteById(id);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(musicRepository.count());
   }

   @Override
   public MusicDTO entityToDTO(Music music) {
      MusicDTO musicDTO = modelMapper.map(music, MusicDTO.class);
      PersonDTO author = personService.findById(music.getAuthorId());
      PersonDTO composer = personService.findById(music.getComposerId());
      PersonDTO interpreter = personService.findById(music.getInterpreterId());

      musicDTO.setAuthor(author);
      musicDTO.setComposer(composer);
      musicDTO.setInterpreter(interpreter);

      return musicDTO;
   }

   @Override
   public Music dtoToEntity(MusicDTO musicDTO) {
      Music music = modelMapper.map(musicDTO, Music.class);

      music.setMediaType(MediaType.MUSIC);

      if(musicDTO.getAuthor()!=null) {
         music.setAuthorId(musicDTO.getAuthor().getId());
      }
      if(musicDTO.getComposer()!=null) {
         music.setComposerId(musicDTO.getComposer().getId());
      }
      if(musicDTO.getInterpreter()!=null) {
         music.setInterpreterId(musicDTO.getInterpreter().getId());
      }

      return music;
   }

   public List<PersonDTO> findAllAuthors() {
      List<PersonDTO> authors = new ArrayList<>();

      for( int authorId : musicRepository.findAllAuthors()){
         authors.add(personService.findById(authorId));
      }

      return  authors;
   }

   public List<PersonDTO> findAllComposers() {
      List<PersonDTO> composers = new ArrayList<>();

      for( int composerId : musicRepository.findAllComposers()){
         composers.add(personService.findById(composerId));
      }

      return  composers;
   }

   public List<PersonDTO> findAllInterpreters() {
      List<PersonDTO> interpreters = new ArrayList<>();

      for( int interpreterId : musicRepository.findAllInterpreters()){
         interpreters.add(personService.findById(interpreterId));
      }

      return  interpreters;
   }

   public List<String> findAllTitles() {
      return musicRepository.findAllTitles();
   }
}
