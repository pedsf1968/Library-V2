package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.business.VideoDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.libraryapi.model.Media;
import com.pedsf.library.libraryapi.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MediaServiceTest {
   private static final String MEDIA_EAN_TEST = "978-2253002864";
   private static final Integer MEDIA_EAN_ID = 5;

   @TestConfiguration
   static class mediaServiceTestConfiguration {
      @Autowired
      private MediaRepository mediaRepository;
      @Autowired
      private BookRepository bookRepository;
      @Autowired
      private GameRepository gameRepository;
      @Autowired
      private MusicRepository musicRepository;
      @Autowired
      private VideoRepository videoRepository;
      @Autowired
      private PersonRepository personRepository;


      @Bean
      public MediaService mediaService() {

         PersonService personService = new PersonService(personRepository);
         BookService bookService = new BookService(bookRepository,personService);
         GameService gameService = new GameService(gameRepository,personService);
         MusicService musicService = new MusicService(musicRepository,personService);
         VideoService videoService = new VideoService(videoRepository,personService);

         return new MediaService(mediaRepository,bookService,gameService,musicService,videoService);
      }
   }

   @Autowired
   private MediaService mediaService;

   @Test
   @DisplayName("Verify that return TRUE if the Media exist")
   void existsById_returnTrue_OfAnExistingMediaId() {
      List<MediaDTO> mediaDTOS = mediaService.findAll();

      for(MediaDTO mediaDTO : mediaDTOS) {
         Integer id = mediaDTO.getId();
         assertThat(mediaService.existsById(id)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Media doesn't exist")
   void existsById_returnFalse_OfAnInexistingMediaId() {
      assertThat(mediaService.existsById(55)).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Media by is ID")
   void findById_returnUser_ofExistingMediaId() {
      List<MediaDTO> mediaDTOS = mediaService.findAll();
      MediaDTO found;

      for(MediaDTO mediaDTO : mediaDTOS) {
         Integer id = mediaDTO.getId();
         found = mediaService.findById(id);

         assertThat(found).isEqualTo(mediaDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Media with wrong ID")
   void findById_returnException_ofInexistingMediaId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         MediaDTO found = mediaService.findById(555);
      });
   }


   @Test
   void initialise() {
   }

   @Test
   void findOneByEan() {
   }

   @Test
   void findFreeByEan() {
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Medias")
   void findAll_returnAllMedias() {
      List<MediaDTO> mediaDTOS = mediaService.findAll();
      assertThat(mediaDTOS.size()).isEqualTo(31);
   }
   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Media by his title, media type and ean")
   void findAllFiltered_returnOnlySameMedia_ofExistingTitleAndMediaTypeAndEAN() {
      List<MediaDTO> mediaDTOS = mediaService.findAll();
      List<MediaDTO> found;
      for(MediaDTO m:mediaDTOS) {
         MediaDTO filter = new MediaDTO();
         filter.setTitle(m.getTitle());
         filter.setMediaType(m.getMediaType());
         filter.setEan(m.getEan());

         found = mediaService.findAllFiltered(filter);
         for(MediaDTO mediaFound : found) {
            assertThat(mediaFound.getMediaType()).isEqualTo(m.getMediaType());
            assertThat(mediaFound.getTitle()).isEqualTo(m.getTitle());
            assertThat(mediaFound.getPublicationDate()).isEqualTo(m.getPublicationDate());
            assertThat(mediaFound.getEan()).isEqualTo(m.getEan());
            assertThat(mediaFound.getHeight()).isEqualTo(m.getHeight());
            assertThat(mediaFound.getLength()).isEqualTo(m.getLength());
            assertThat(mediaFound.getWidth()).isEqualTo(m.getWidth());
            assertThat(mediaFound.getWeight()).isEqualTo(m.getWeight());
            assertThat(mediaFound.getQuantity()).isEqualTo(m.getQuantity());
         }
      }
   }

   @Test
   void getFirstId() {
   }

   @Test
   @DisplayName("Verify that we can create a new Media")

   void save_returnCreatedMedia_ofNewMedia() {
   /*   MediaDTO MediaDTO = mediaService.findById(MEDIA_EAN_ID);

      MediaDTO.setId(null);
      MediaDTO.setPublicationDate(null);
      MediaDTO = mediaService.save(MediaDTO);
      Integer newId = MediaDTO.getId();

      assertThat(mediaService.existsById(newId)).isTrue();
      mediaService.deleteById(newId); */
   }

   @Test
   void update() {
   }

   @Test
   void deleteById() {
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Medias")
   void count_returnTheNumberOfMedias() {
      assertThat(mediaService.count()).isEqualTo(31);
   }


   @Test
   void entityToDTO() {
   }

   @Test
   void dtoToEntity() {
   }

   @Test
   void increaseStock() {
   }

   @Test
   void decreaseStock() {
   }

   @Test
   void findMediaTypeByEan() {
   }

   @Test
   void findBlockedByEan() {
   }

   @Test
   void findBoockedByEan() {
   }

   @Test
   void blockFreeByEan() {
   }

   @Test
   void bookedFreeByEan() {
   }

   @Test
   void borrow() {
   }

   @Test
   void release() {
   }

   @Test
   void updateReturnDate() {
   }

   @Test
   void getNextReturnDateByEan() {
   }

   @Test
   void getNextReturnByEan() {
   }

   @Test
   void setStatus() {
   }
}