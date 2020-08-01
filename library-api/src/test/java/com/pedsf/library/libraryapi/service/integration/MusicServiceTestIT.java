package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.MusicDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.model.Music;
import com.pedsf.library.libraryapi.repository.MusicRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.service.MusicService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
class MusicServiceTestIT {
   private static final String MUSIC_EAN_TEST = "4988064587100";

   @TestConfiguration
   static class musicServiceTestConfiguration {
      @Autowired
      private MusicRepository musicRepository;
      @Autowired
      private PersonRepository personRepository;


      @Bean
      public MusicService musicService() {
         PersonService personService = new PersonService(personRepository);

         return new MusicService(musicRepository,personService);
      }
   }

   @Autowired
   private MusicService musicService;

   @Test
   @DisplayName("Verify that return TRUE if the Music exist")
   void existsById_returnTrue_OfAnExistingMusicId() {
      List<MusicDTO> musicDTOS = musicService.findAll();

      for(MusicDTO musicDTO : musicDTOS) {
         String ean = musicDTO.getEan();
         assertThat(musicService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Music doesn't exist")
   void existsById_returnFalse_OfAnInexistingMusicId() {
      assertThat(musicService.existsById("5lkjh5")).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Music by is ID")
   void findById_returnUser_ofExistingMusicId() {
      List<MusicDTO> musicDTOS = musicService.findAll();
      MusicDTO found;

      for(MusicDTO musicDTO : musicDTOS) {
         String ean = musicDTO.getEan();
         found = musicService.findById(ean);

         assertThat(found).isEqualTo(musicDTO);
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Music with wrong ID")
   void findById_returnException_ofInexistingMusicId() {

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         MusicDTO found = musicService.findById("liuzae");
      });
   }


   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Musics")
   void findAll_returnAllMusics() {
      List<MusicDTO> musicDTOS = musicService.findAll();
      assertThat(musicDTOS.size()).isEqualTo(4);
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Musics that can be booked")
   void findAllAllowed_returnBookableMusics_ofAllMusics() {
      List<MusicDTO> musicDTOS = musicService.findAll();
      List<MusicDTO> alloweds = musicService.findAllAllowed();

      for(MusicDTO musicDTO: musicDTOS) {
         if (alloweds.contains(musicDTO)) {
            // allowed
            assertThat(musicDTO.getStock()).isGreaterThan(-musicDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(musicDTO.getStock()).isLessThanOrEqualTo(-musicDTO.getQuantity()*2);
         }
      }
   }


   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Music by his title and interpreter")
   void findAllFiltered_returnOnlyOneMusic_ofExistingTitleAndInterpreter() {
      List<MusicDTO> musicDTOS = musicService.findAll();
      List<MusicDTO> found;
      for(MusicDTO m:musicDTOS) {
         MusicDTO filter = new MusicDTO();
         filter.setTitle(m.getTitle());
         filter.setInterpreter(m.getInterpreter());

         found = musicService.findAllFiltered(filter);
         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(m);
      }
   }

   @Test
   void getFirstId() {
   }

   @Test
   @DisplayName("Verify that we can create a new Music")
   void save_returnCreatedMusic_ofNewMusic() {
      MusicDTO musicDTO = musicService.findById(MUSIC_EAN_TEST);
      String newEan = "newMusicEAN";
      String newTitle = "NewMusicTitle";

      musicDTO.setEan(newEan);
      musicDTO.setTitle(newTitle);
      musicDTO.setReturnDate(null);
      musicDTO.setPublicationDate(null);
      musicDTO = musicService.save(musicDTO);
      String ean = musicDTO.getEan();

      assertThat(musicService.existsById(ean)).isTrue();
      musicService.deleteById(ean);
   }

   @Test
   void update() {
   }

   @Test
   void deleteById() {
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Musics")
   void count_returnTheNumberOfMusics() {
      assertThat(musicService.count()).isEqualTo(4);
   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Music DTO is converted in right Music Entity")
   void dtoToEntity_returnRightMusicEntity_ofMusicDTO() {
      List<MusicDTO> musicDTOS = musicService.findAll();
      Music entity;

      for (MusicDTO dto: musicDTOS) {
         entity = musicService.dtoToEntity(dto);
         assertThat(entity.getEan()).isEqualTo(dto.getEan());
         assertThat(entity.getTitle()).isEqualTo(dto.getTitle());
         assertThat(entity.getQuantity()).isEqualTo(dto.getQuantity());
         assertThat(entity.getStock()).isEqualTo(dto.getStock());
         assertThat(entity.getHeight()).isEqualTo(dto.getHeight());
         assertThat(entity.getLength()).isEqualTo(dto.getLength());
         assertThat(entity.getWeight()).isEqualTo(dto.getWeight());
         assertThat(entity.getWidth()).isEqualTo(dto.getWidth());
         assertThat(entity.getReturnDate()).isEqualTo(dto.getReturnDate());

         assertThat(entity.getDuration()).isEqualTo(dto.getDuration());
         assertThat(entity.getAuthorId()).isEqualTo(dto.getAuthor().getId());
         assertThat(entity.getComposerId()).isEqualTo(dto.getComposer().getId());
         assertThat(entity.getInterpreterId()).isEqualTo(dto.getInterpreter().getId());
         assertThat(entity.getUrl()).isEqualTo(dto.getUrl());
         assertThat(entity.getPublicationDate()).isEqualTo(dto.getPublicationDate());
         assertThat(entity.getFormat().name()).isEqualTo(dto.getFormat());
         assertThat(entity.getType().name()).isEqualTo(dto.getType());
      }
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Music Entity is converted in right Music DTO")
   void dtoToEntity_returnRightMusicDTO_ofMusicEntity() {
      List<MusicDTO> musicDTOS = musicService.findAll();
      List<Music> music = new ArrayList<>();
      MusicDTO dto;

      for (MusicDTO m: musicDTOS) {
         music.add(musicService.dtoToEntity(m));
      }

      for (Music entity: music) {
         dto = musicService.entityToDTO(entity);
         assertThat(dto.getEan()).isEqualTo(entity.getEan());
         assertThat(dto.getTitle()).isEqualTo(entity.getTitle());
         assertThat(dto.getQuantity()).isEqualTo(entity.getQuantity());
         assertThat(dto.getStock()).isEqualTo(entity.getStock());
         assertThat(dto.getHeight()).isEqualTo(entity.getHeight());
         assertThat(dto.getLength()).isEqualTo(entity.getLength());
         assertThat(dto.getWeight()).isEqualTo(entity.getWeight());
         assertThat(dto.getWidth()).isEqualTo(entity.getWidth());
         assertThat(dto.getReturnDate()).isEqualTo(entity.getReturnDate());

         assertThat(dto.getDuration()).isEqualTo(entity.getDuration());
         assertThat(dto.getAuthor().getId()).isEqualTo(entity.getAuthorId());
         assertThat(dto.getComposer().getId()).isEqualTo(entity.getComposerId());
         assertThat(dto.getInterpreter().getId()).isEqualTo(entity.getInterpreterId());
         assertThat(dto.getUrl()).isEqualTo(entity.getUrl());
         assertThat(dto.getPublicationDate()).isEqualTo(entity.getPublicationDate());
         assertThat(dto.getFormat()).isEqualTo(entity.getFormat().name());
         assertThat(dto.getType()).isEqualTo(entity.getType().name());
      }
   }

   @Test
   @Tag("findAllAuthors")
   @DisplayName("Verify that we get all Musics authors")
   void findAllAuthors() {
      List<PersonDTO> personDTOS = musicService.findAllAuthors();
      assertThat(personDTOS.size()).isEqualTo(2);
   }

   @Test
   @Tag("findAllComposers")
   @DisplayName("Verify that we get all Musics composers")
   void findAllComposers() {
      List<PersonDTO> personDTOS = musicService.findAllComposers();
      assertThat(personDTOS.size()).isEqualTo(2);
   }

   @Test
   @Tag("findAllInterpreters")
   @DisplayName("Verify that we get all Musics interpreters")
   void findAllInterpreters() {
      List<PersonDTO> personDTOS = musicService.findAllInterpreters();
      assertThat(personDTOS.size()).isEqualTo(2);
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Books titles")
   void findAllTitles() {
      List<String> titles = musicService.findAllTitles();
      assertThat(titles.size()).isEqualTo(4);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Musics by his EAN number")
   void increaseStock_returnMusicsWithIncrementedStock_ofOneMusics() {
      MusicDTO musicDTO = musicService.findById(MUSIC_EAN_TEST);
      Integer oldStock = musicDTO.getStock();
      musicService.increaseStock(MUSIC_EAN_TEST);
      musicDTO = musicService.findById(MUSIC_EAN_TEST);
      assertThat(musicDTO.getStock()).isEqualTo(oldStock+1);
      musicDTO.setStock(oldStock);
      musicService.update(musicDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Musics by his EAN number")
   void decreaseStock_returnMusicsWithDecrementedStock_ofOneMusics() {
      MusicDTO musicDTO = musicService.findById(MUSIC_EAN_TEST);
      Integer oldStock = musicDTO.getStock();
      musicService.decreaseStock(MUSIC_EAN_TEST);
      musicDTO = musicService.findById(MUSIC_EAN_TEST);
      assertThat(musicDTO.getStock()).isEqualTo(oldStock-1);
      musicDTO.setStock(oldStock);
      musicService.update(musicDTO);
   }
}