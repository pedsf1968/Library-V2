package com.library.libraryapi.service;

import com.library.libraryapi.dto.business.PersonDTO;
import com.library.libraryapi.dto.business.VideoDTO;
import com.library.libraryapi.exceptions.BadRequestException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.model.*;
import com.library.libraryapi.repository.VideoRepository;
import com.library.libraryapi.repository.VideoSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("VideoService")
public class VideoService implements GenericMediaService<VideoDTO,Video,String> {
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Video with the EAN : ";
   private static final String CANNOT_SAVE ="Failed to save Video";

   private final VideoRepository videoRepository;
   private final PersonService personService;

   private final ModelMapper modelMapper = new ModelMapper();

   public VideoService(VideoRepository videoRepository, PersonService personService) {
      this.videoRepository = videoRepository;
      this.personService = personService;
   }

   @Override
   public boolean existsById(String ean) {
      return videoRepository.findByEan(ean).isPresent();
   }

   @Override
   public VideoDTO findById(String ean) {

      if(existsById(ean)){
         Video video = videoRepository.findByEan(ean).orElse(null);
         return entityToDTO(video);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + ean);
      }
   }

   @Override
   public List<VideoDTO> findAll() {
      List<Video> videos = videoRepository.findAll();
      List<VideoDTO> videoDTOS = new ArrayList<>();

      for (Video video: videos){
         videoDTOS.add(entityToDTO(video));
      }

      if (!videoDTOS.isEmpty()) {
         return videoDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<VideoDTO> findAllAllowed() {
      List<Video> videos = videoRepository.findAllAllowed();
      List<VideoDTO> videoDTOS = new ArrayList<>();

      for (Video video: videos){
         videoDTOS.add(entityToDTO(video));
      }

      if (!videoDTOS.isEmpty()) {
         return videoDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<VideoDTO> findAllFiltered(VideoDTO filter) {
      Video video = dtoToEntity(filter);
      Specification<Video> spec = new VideoSpecification(video);
      List<Video> videos = videoRepository.findAll(spec);
      List<VideoDTO> videoDTOS = new ArrayList<>();

      for (Video v: videos){
         videoDTOS.add(entityToDTO(v));
      }

      if (!videoDTOS.isEmpty()) {
         return videoDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public String getFirstId(VideoDTO filter){

      return findAllFiltered(filter).get(0).getEan();
   }


   @Override
   public VideoDTO save(VideoDTO videoDTO) {
      if (   !StringUtils.isEmpty(videoDTO.getEan()) &&
            !StringUtils.isEmpty(videoDTO.getTitle()) &&
            !StringUtils.isEmpty(videoDTO.getAudience()) &&
            !StringUtils.isEmpty(videoDTO.getDirector()) &&
            !StringUtils.isEmpty(videoDTO.getDuration()) &&
            !StringUtils.isEmpty(videoDTO.getAudio()) &&
            !StringUtils.isEmpty(videoDTO.getType()) &&
            !StringUtils.isEmpty(videoDTO.getFormat())) {

         try {
            // try to find existing Video
            String ean = getFirstId(videoDTO);
            Video video = videoRepository.findByEan(ean).orElse(null);
            return entityToDTO(video);
         } catch (ResourceNotFoundException ex) {
            return entityToDTO(videoRepository.save(dtoToEntity(videoDTO)));
         }

      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public VideoDTO update(VideoDTO videoDTO) {
      if (  !StringUtils.isEmpty(videoDTO.getEan()) &&
            !StringUtils.isEmpty(videoDTO.getTitle()) &&
            !StringUtils.isEmpty(videoDTO.getDirector()) &&
            !StringUtils.isEmpty(videoDTO.getType()) &&
            !StringUtils.isEmpty(videoDTO.getFormat())) {


         if (!existsById(videoDTO.getEan())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + videoDTO.getEan());
         }

         Video video = videoRepository.save(dtoToEntity(videoDTO));

         return entityToDTO(video);
      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public void deleteById(String ean) {
      if (!existsById(ean)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + ean);
      } else {
         videoRepository.deleteByEan(ean);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(videoRepository.count());
   }

   @Override
   public VideoDTO entityToDTO(Video video) {
      VideoDTO videoDTO = modelMapper.map(video, VideoDTO.class);
      PersonDTO director = personService.findById(video.getDirectorId());
      List<PersonDTO> actorDTOs = new ArrayList<>();

      if(video.getActors()!=null) {
         for (Person person : video.getActors()) {
            actorDTOs.add(personService.entityToDTO(person));
         }
      }
      videoDTO.setDirector(director);
      videoDTO.setActors(actorDTOs);

      return videoDTO;
   }

   @Override
   public Video dtoToEntity(VideoDTO videoDTO) {
      Video video = modelMapper.map(videoDTO, Video.class);
      Set<Person> personSet = new HashSet<>();

      if(videoDTO.getActors()!=null) {
         for (PersonDTO personDTO : videoDTO.getActors()) {
            personSet.add(personService.dtoToEntity(personDTO));
         }
      }
      video.setActors(personSet);

      if(videoDTO.getDirector()!=null) {
         video.setDirectorId(videoDTO.getDirector().getId());
      }

      return video;
   }

   public List<PersonDTO> findAllDirectors() {
      List<PersonDTO> directors = new ArrayList<>();

      for( int directorId : videoRepository.findAllDirectors()){
         directors.add(personService.findById(directorId));
      }

      return  directors;
   }

   public List<PersonDTO> findAllActors() {
      List<PersonDTO> actors = new ArrayList<>();

      for( int actorsId : videoRepository.findAllActors()){
         actors.add(personService.findById(actorsId));
      }

      return  actors;
   }

   public List<String> findAllTitles() {
      return videoRepository.findAllTitles();
   }

   void increaseStock(String ean) {
      videoRepository.increaseStock(ean);
   }

   void decreaseStock(String ean) {
      videoRepository.decreaseStock(ean);
   }

}
