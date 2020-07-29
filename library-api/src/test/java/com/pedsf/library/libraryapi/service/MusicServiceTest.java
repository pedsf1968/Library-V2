package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.MusicDTO;
import com.pedsf.library.libraryapi.repository.MusicRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MusicServiceTest {
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
   void findAllAllowed() {
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
      String newEan = "newEAN";
      String newTitle = "NewTitle";

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
   void entityToDTO() {
   }

   @Test
   void dtoToEntity() {
   }

   @Test
   void findAllAuthors() {
   }

   @Test
   void findAllComposers() {
   }

   @Test
   void findAllInterpreters() {
   }

   @Test
   void findAllTitles() {
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