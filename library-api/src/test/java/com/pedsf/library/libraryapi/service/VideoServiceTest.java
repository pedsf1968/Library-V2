package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.business.VideoDTO;
import com.pedsf.library.libraryapi.model.Video;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.repository.VideoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class VideoServiceTest {
   private static final String VIDEO_EAN_TEST = "3475001058980";

   @TestConfiguration
   static class videoServiceTestConfiguration {
      @Autowired
      private VideoRepository videoRepository;
      @Autowired
      private PersonRepository personRepository;


      @Bean
      public VideoService videoService() {
         PersonService personService = new PersonService(personRepository);

         return new VideoService(videoRepository,personService);
      }
   }

   @Autowired
   private VideoService videoService;

   @Test
   @DisplayName("Verify that return TRUE if the Video exist")
   void existsById_returnTrue_OfAnExistingVideoId() {
      List<VideoDTO> videoDTOS = videoService.findAll();

      for(VideoDTO videoDTO : videoDTOS) {
         String ean = videoDTO.getEan();
         assertThat(videoService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Video doesn't exist")
   void existsById_returnFalse_OfAnInexistingVideoId() {
      assertThat(videoService.existsById("5lkjh5")).isFalse();
   }


   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Video by is ID")
   void findById_returnUser_ofExistingVideoId() {
      List<VideoDTO> videoDTOS = videoService.findAll();
      VideoDTO found;

      for(VideoDTO videoDTO : videoDTOS) {
         String ean = videoDTO.getEan();
         found = videoService.findById(ean);

         assertThat(found).isEqualTo(videoDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find user Video wrong ID")
   void findById_returnException_ofInexistingVideoId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         VideoDTO found = videoService.findById("klsdjfh");
      });
   }


   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Videos")
   void findAll_returnAllVideos() {
      List<VideoDTO> videoDTOS = videoService.findAll();
      assertThat(videoDTOS.size()).isEqualTo(1);
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Videos that can be booked")
   void findAllAllowed_returnBookableVideos_ofAllVideos() {
      List<VideoDTO> videoDTOS = videoService.findAll();
      List<VideoDTO> alloweds = videoService.findAllAllowed();

      for(VideoDTO videoDTO: videoDTOS) {
         if (alloweds.contains(videoDTO)) {
            // allowed
            assertThat(videoDTO.getStock()).isGreaterThan(-videoDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(videoDTO.getStock()).isLessThanOrEqualTo(-videoDTO.getQuantity()*2);
         }
      }
   }


   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Video by his title and director")
   void findAllFiltered_returnOnlyOneVideo_ofExistingTitleAndDirector() {
      List<VideoDTO> videoDTOS = videoService.findAll();
      List<VideoDTO> found;
      for(VideoDTO v:videoDTOS) {
         VideoDTO filter = new VideoDTO();
         filter.setTitle(v.getTitle());
         filter.setDirector(v.getDirector());

         found = videoService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(v);
      }
   }

   @Test
   void getFirstId() {
   }

  /* @Test
   @DisplayName("Verify that we can create a new Video")
   void save_returnCreatedVideo_ofNewVideo() {
      VideoDTO videoDTO = videoService.findById(VIDEO_EAN_TEST);
      String newEan = "newVideoEAN";
      String newTitle = "NewVideoTitle";

      videoDTO.setEan(newEan);
      videoDTO.setTitle(newTitle);
      videoDTO.setReturnDate(null);
      videoDTO.setPublicationDate(null);
      videoDTO = videoService.save(videoDTO);
      String ean = videoDTO.getEan();

      assertThat(videoService.existsById(ean)).isTrue();
      videoService.deleteById(ean);
   }
*/
   @Test
   void update() {
   }

   @Test
   void deleteById() {
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Videos")
   void count_returnTheNumberOfVideos() {
      assertThat(videoService.count()).isEqualTo(1);
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Video DTO is converted in right Video Entity")
   void dtoToEntity_returnRightVideoEntity_ofVideoDTO() {
      List<VideoDTO> videoDTOS = videoService.findAll();
      String essai;
      Video entity;

      for (VideoDTO dto: videoDTOS) {
         entity = videoService.dtoToEntity(dto);
         assertThat(entity.getEan()).isEqualTo(dto.getEan());
         assertThat(entity.getTitle()).isEqualTo(dto.getTitle());
         assertThat(entity.getQuantity()).isEqualTo(dto.getQuantity());
         assertThat(entity.getStock()).isEqualTo(dto.getStock());
         assertThat(entity.getHeight()).isEqualTo(dto.getHeight());
         assertThat(entity.getLength()).isEqualTo(dto.getLength());
         assertThat(entity.getWeight()).isEqualTo(dto.getWeight());
         assertThat(entity.getWidth()).isEqualTo(dto.getWidth());
         assertThat(entity.getReturnDate()).isEqualTo(dto.getReturnDate());

         assertThat(entity.getDirectorId()).isEqualTo(dto.getDirector().getId());
         assertThat(entity.getDuration()).isEqualTo(dto.getDuration());
         assertThat(entity.getImage()).isEqualTo(dto.getImage());
         assertThat(entity.getAudience()).isEqualTo(dto.getAudience());
         assertThat(entity.getAudio()).isEqualTo(dto.getAudio());
         assertThat(entity.getUrl()).isEqualTo(dto.getUrl());
         assertThat(entity.getPublicationDate()).isEqualTo(dto.getPublicationDate());
         assertThat(entity.getFormat().name()).isEqualTo(dto.getFormat());
         assertThat(entity.getType().name()).isEqualTo(dto.getType());
         assertThat(entity.getSummary()).isEqualTo(dto.getSummary());
      }
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Video Entity is converted in right Video DTO")
   void dtoToEntity_returnRightBookDTO_ofBookEntity() {
      List<VideoDTO> videoDTOS = videoService.findAll();
      List<Video> videos = new ArrayList<>();
      VideoDTO dto;

      for (VideoDTO b: videoDTOS) {
         videos.add(videoService.dtoToEntity(b));
      }

      for (Video entity: videos) {
         dto = videoService.entityToDTO(entity);
         assertThat(dto.getEan()).isEqualTo(entity.getEan());
         assertThat(dto.getTitle()).isEqualTo(entity.getTitle());
         assertThat(dto.getQuantity()).isEqualTo(entity.getQuantity());
         assertThat(dto.getStock()).isEqualTo(entity.getStock());
         assertThat(dto.getHeight()).isEqualTo(entity.getHeight());
         assertThat(dto.getLength()).isEqualTo(entity.getLength());
         assertThat(dto.getWeight()).isEqualTo(entity.getWeight());
         assertThat(dto.getWidth()).isEqualTo(entity.getWidth());
         assertThat(dto.getReturnDate()).isEqualTo(entity.getReturnDate());

         assertThat(dto.getDirector().getId()).isEqualTo(entity.getDirectorId());
         assertThat(dto.getDuration()).isEqualTo(entity.getDuration());
         assertThat(dto.getImage()).isEqualTo(entity.getImage());
         assertThat(dto.getAudience()).isEqualTo(entity.getAudience());
         assertThat(dto.getAudio()).isEqualTo(entity.getAudio());
         assertThat(dto.getUrl()).isEqualTo(entity.getUrl());
         assertThat(dto.getPublicationDate()).isEqualTo(entity.getPublicationDate());
         assertThat(dto.getFormat()).isEqualTo(entity.getFormat().name());
         assertThat(dto.getType()).isEqualTo(entity.getType().name());
         assertThat(dto.getSummary()).isEqualTo(entity.getSummary());
      }
   }


   @Test
   @Tag("findAllDirectors")
   @DisplayName("Verify that we get all Videos Director")
   void findAllDirectors() {
      List<PersonDTO> personDTOS = videoService.findAllDirectors();
      assertThat(personDTOS.size()).isEqualTo(1);
   }

   @Test
   @Tag("findAllActors")
   @DisplayName("Verify that we get all Videos actors")
   void findAllActors() {
      List<PersonDTO> personDTOS = videoService.findAllActors();
      assertThat(personDTOS.size()).isEqualTo(5);
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Videos titles")
   void findAllTitles() {
      List<String> titles = videoService.findAllTitles();
      assertThat(titles.size()).isEqualTo(1);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Video by his EAN number")
   void increaseStock_returnVideoWithIncrementedStock_ofOneVideo() {
      VideoDTO videoDTO = videoService.findById(VIDEO_EAN_TEST);
      Integer oldStock = videoDTO.getStock();
      videoService.increaseStock(VIDEO_EAN_TEST);
      videoDTO = videoService.findById(VIDEO_EAN_TEST);
      assertThat(videoDTO.getStock()).isEqualTo(oldStock+1);
      videoDTO.setStock(oldStock);
      videoService.update(videoDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Video by his EAN number")
   void decreaseStock_returnVideoWithDecrementedStock_ofOneVideo() {
      VideoDTO videoDTO = videoService.findById(VIDEO_EAN_TEST);
      Integer oldStock = videoDTO.getStock();

      videoService.decreaseStock(VIDEO_EAN_TEST);
      videoDTO = videoService.findById(VIDEO_EAN_TEST);
      assertThat(videoDTO.getStock()).isEqualTo(oldStock-1);
      videoDTO.setStock(oldStock);
      videoService.update(videoDTO);
   }
}