package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.business.VideoDTO;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.repository.VideoRepository;
import com.pedsf.library.libraryapi.service.PersonService;
import com.pedsf.library.libraryapi.service.VideoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class VideoServiceTestIT {
   private static final String VIDEO_EAN_TEST = "3475001058980";
   private static final String VIDEO_TITLE_TEST = "New Video Title";

   private static VideoService videoService;
   private static PersonService personService;
   private static Video newVideo;
   private static VideoDTO newVideoDTO;
   private static List<VideoDTO> allVideoDTOS;

   @BeforeAll
   static void beforeAll(@Autowired VideoRepository videoRepository,
                         @Autowired PersonRepository personRepository) {
      personService = new PersonService(personRepository);
      videoService = new VideoService(videoRepository,personService);
   }

   @BeforeEach
   void beforeEach() {
      newVideo = new Video("sdfsdfds","Video of the Day", 1,1,15);
      newVideo.setDuration(123);
      newVideo.setFormat(VideoFormat.DVD);
      newVideo.setType(VideoType.DOCUMENT);
      newVideo.setUrl("http://www.google.co.kr");
      newVideo.setHeight(11);
      newVideo.setLength(11);
      newVideo.setWidth(11);
      newVideo.setWeight(220);
      newVideo.setAudience("Tout public");
      newVideo.setDuration(310);
      newVideo.setAudio("5.1");

      newVideoDTO = new VideoDTO("sdfsdfds","Video of the Day", 1,1,personService.findById(14));
      newVideoDTO.setDuration(123);
      newVideoDTO.setFormat(VideoFormat.DVD.name());
      newVideoDTO.setType(VideoType.DOCUMENT.name());
      newVideoDTO.setUrl("http://www.google.co.kr");
      newVideoDTO.setHeight(11);
      newVideoDTO.setLength(11);
      newVideoDTO.setWidth(11);
      newVideoDTO.setWeight(220);
      newVideoDTO.setAudience("Tout public");
      newVideoDTO.setDuration(310);
      newVideoDTO.setAudio("5.1");

      allVideoDTOS = videoService.findAll();
   }

   @Test
   @DisplayName("Verify that return TRUE if the Video exist")
   void existsById_returnTrue_OfAnExistingVideoId() {
      for(VideoDTO videoDTO : allVideoDTOS) {
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
   void findById_returnVideo_ofExistingVideoId() {
      VideoDTO found;

      for(VideoDTO videoDTO : allVideoDTOS) {
         String ean = videoDTO.getEan();
         found = videoService.findById(ean);

         assertThat(found).isEqualTo(videoDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find user Video wrong ID")
   void findById_returnException_ofInexistingVideoId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> videoService.findById("klsdjfh"));
   }


   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Videos")
   void findAll_returnAllVideos() {
      assertThat(allVideoDTOS.size()).isEqualTo(1);

      // add new Video
      newVideoDTO = videoService.save(newVideoDTO);
      List<VideoDTO> videoDTOS = videoService.findAll();
      assertThat(videoDTOS.size()).isEqualTo(2);
      assertThat(videoDTOS.contains(newVideoDTO)).isTrue();

      videoService.deleteById(newVideoDTO.getEan());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Videos that can be booked")
   void findAllAllowed_returnBookableVideos_ofAllVideos() {
      newVideo.setStock(-2);
      VideoDTO dto = videoService.entityToDTO(newVideo);
      dto = videoService.save(dto);
      List<VideoDTO> alloweds = videoService.findAllAllowed();

      assertThat(alloweds.contains(dto)).isFalse();


      for(VideoDTO videoDTO: allVideoDTOS) {
         if (alloweds.contains(videoDTO)) {
            // allowed
            assertThat(videoDTO.getStock()).isGreaterThan(-videoDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(videoDTO.getStock()).isLessThanOrEqualTo(-videoDTO.getQuantity()*2);
         }
      }

      newVideo.setStock(1);
      videoService.deleteById(dto.getEan());
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
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Video by Director")
   void getFirstId_returnFirstId_ofFilteredVideoByAuthor() {
      VideoDTO filter = new VideoDTO();
      filter.setDirector(personService.findById(4));

      String ean = videoService.getFirstId(filter);

      assertThat(ean).isEqualTo("3475001058980");
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Video")
   void save_returnCreatedVideo_ofNewVideo() {
      VideoDTO videoDTO = videoService.entityToDTO(newVideo);

      videoDTO = videoService.save(videoDTO);
      String ean = videoDTO.getEan();

      assertThat(videoService.existsById(ean)).isTrue();
      videoService.deleteById(ean);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Video")
   void update_returnUpdatedVideo_ofVideoAndNewTitle() {
      VideoDTO videoDTO = videoService.findById(VIDEO_EAN_TEST);
      String oldTitle = videoDTO.getTitle();
      videoDTO.setTitle(VIDEO_TITLE_TEST);

      VideoDTO videoSaved = videoService.update(videoDTO);
      assertThat(videoSaved).isEqualTo(videoDTO);
      VideoDTO videoFound = videoService.findById(videoDTO.getEan());
      assertThat(videoFound).isEqualTo(videoDTO);

      videoDTO.setTitle(oldTitle);
      videoService.update(videoDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Video by his EAN")
   void deleteById_returnExceptionWhenGetUserById_ofDeletedUserById() {
      VideoDTO videoDTO = videoService.entityToDTO(newVideo);

      videoDTO = videoService.save(videoDTO);
      String ean = videoDTO.getEan();

      assertThat(videoService.existsById(ean)).isTrue();
      videoService.deleteById(ean);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> videoService.findById(ean));
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Videos")
   void count_returnTheNumberOfVideos() {
      VideoDTO videoDTO = videoService.entityToDTO(newVideo);
      assertThat(videoService.count()).isEqualTo(1);

      // add an other video
      videoDTO = videoService.save(videoDTO);
      assertThat(videoService.count()).isEqualTo(2);
      videoService.deleteById(videoDTO.getEan());
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Video DTO is converted in right Video Entity")
   void dtoToEntity_returnRightVideoEntity_ofVideoDTO() {
      Video entity;

      for (VideoDTO dto: allVideoDTOS) {
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
      List<Video> videos = new ArrayList<>();
      VideoDTO dto;

      for (VideoDTO b: allVideoDTOS) {
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
      VideoDTO videoDTO = videoService.entityToDTO(newVideo);
      List<String> titles = videoService.findAllTitles();
      assertThat(titles.size()).isEqualTo(1);

      // add an other video
      videoDTO.setTitle(VIDEO_TITLE_TEST);
      videoDTO = videoService.save(videoDTO);
      titles = videoService.findAllTitles();
      assertThat(titles.size()).isEqualTo(2);
      assertThat(titles.contains(VIDEO_TITLE_TEST)).isTrue();

      videoService.deleteById(videoDTO.getEan());
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