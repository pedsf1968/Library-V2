package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.dto.business.MusicDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.libraryapi.model.Music;
import com.pedsf.library.libraryapi.model.MusicFormat;
import com.pedsf.library.libraryapi.model.MusicType;
import com.pedsf.library.libraryapi.repository.MusicRepository;
import com.pedsf.library.libraryapi.repository.PersonRepository;
import com.pedsf.library.libraryapi.service.MusicService;
import com.pedsf.library.libraryapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class MusicServiceTestIT {
   private static final String MUSIC_EAN_TEST = "4988064587100";
   private static final String MUSIC_TITLE_TEST = "New Music Title";

   private static MusicService musicService;
   private static PersonService personService;
   private static Music newMusic;
   private static MusicDTO newMusicDTO;
   private static List<MusicDTO> allMusicDTOS;

   @BeforeAll
   static void beforeAll(@Autowired MusicRepository musicRepository,
                         @Autowired PersonRepository personRepository) {
      personService = new PersonService(personRepository);
      musicService = new MusicService(musicRepository,personService);
   }

   @BeforeEach
   void beforeEach() {
      newMusic = new Music("9876546546","Le petite musique du matin", 1,1,15,15,15);
      newMusic.setDuration(123);
      newMusic.setFormat(MusicFormat.SACD);
      newMusic.setType(MusicType.ELECTRO);
      newMusic.setUrl("http://www.google.co.kr");
      newMusic.setHeight(11);
      newMusic.setLength(11);
      newMusic.setWidth(11);
      newMusic.setWeight(220);

      newMusicDTO = new MusicDTO("9876546546","Le petite musique du matin", 1,1,personService.findById(15),personService.findById(15),personService.findById(15));
      newMusicDTO.setDuration(123);
      newMusicDTO.setFormat("SACD");
      newMusicDTO.setType("ELECTRO");
      newMusicDTO.setUrl("http://www.google.co.kr");
      newMusicDTO.setHeight(11);
      newMusicDTO.setLength(11);
      newMusicDTO.setWidth(11);
      newMusicDTO.setWeight(220);

      allMusicDTOS = musicService.findAll();
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Music exist")
   void existsById_returnTrue_OfAnExistingMusicId() {
      for(MusicDTO musicDTO : allMusicDTOS) {
         String ean = musicDTO.getEan();
         assertThat(musicService.existsById(ean)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Music doesn't exist")
   void existsById_returnFalse_OfAnInexistingMusicId() {
      assertThat(musicService.existsById("5ldssdqkjh5")).isFalse();
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Music by is ID")
   void findById_returnMusic_ofExistingMusicId() {
      MusicDTO found;

      for(MusicDTO musicDTO : allMusicDTOS) {
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
      assertThat(allMusicDTOS.size()).isEqualTo(4);

      // add new Music to increase the list
      newMusicDTO = musicService.save(newMusicDTO);
      List<MusicDTO> musicDTOS = musicService.findAll();
      assertThat(musicDTOS.size()).isEqualTo(5);
      assertThat(musicDTOS.contains(newMusicDTO)).isTrue();

      musicService.deleteById(newMusicDTO.getEan());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Musics that can be booked")
   void findAllAllowed_returnBookableMusics_ofAllMusics() {
      newMusic.setStock(-2);
      MusicDTO dto = musicService.entityToDTO(newMusic);
      dto = musicService.save(dto);
      List<MusicDTO> alloweds = musicService.findAllAllowed();

      assertThat(alloweds.contains(dto)).isFalse();

      for(MusicDTO musicDTO: allMusicDTOS) {
         if (alloweds.contains(musicDTO)) {
            // allowed
            assertThat(musicDTO.getStock()).isGreaterThan(-musicDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(musicDTO.getStock()).isLessThanOrEqualTo(-musicDTO.getQuantity()*2);
         }
      }

      newMusic.setStock(1);
      musicService.deleteById(dto.getEan());
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
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Music by Author")
   void getFirstId_returnFirstId_ofFilteredMusicByAuthor() {
      MusicDTO filter = new MusicDTO();
      filter.setAuthor(personService.findById(14));

      String ean = musicService.getFirstId(filter);

      assertThat(ean).isEqualTo("8809634380036");
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Music")
   void save_returnCreatedMusic_ofNewMusic() {
      MusicDTO musicDTO = musicService.entityToDTO(newMusic);

      musicDTO = musicService.save(musicDTO);
      String ean = musicDTO.getEan();

      assertThat(musicService.existsById(ean)).isTrue();
      musicService.deleteById(ean);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Music")
   void update_returnUpdatedMusic_ofMusicAndNewTitle() {
      MusicDTO musicDTO = musicService.findById(MUSIC_EAN_TEST);
      String oldTitle = musicDTO.getTitle();
      musicDTO.setTitle(MUSIC_TITLE_TEST);

      MusicDTO musicSaved = musicService.update(musicDTO);
      assertThat(musicSaved).isEqualTo(musicDTO);
      MusicDTO musicFound = musicService.findById(musicDTO.getEan());
      assertThat(musicFound).isEqualTo(musicDTO);

      musicDTO.setTitle(oldTitle);
      musicService.update(musicDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Book by his EAN")
   void deleteById_returnExceptionWhenGetUserById_ofDeletedUserById() {
      MusicDTO musicDTO = musicService.entityToDTO(newMusic);

      musicDTO = musicService.save(musicDTO);
      String ean = musicDTO.getEan();

      assertThat(musicService.existsById(ean)).isTrue();
      musicService.deleteById(ean);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         musicService.findById(ean);
      });
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Musics")
   void count_returnTheNumberOfMusics() {
      MusicDTO musicDTO = musicService.entityToDTO(newMusic);
      assertThat(musicService.count()).isEqualTo(4);

      // add an other music
      musicDTO = musicService.save(musicDTO);
      assertThat(musicService.count()).isEqualTo(5);
      musicService.deleteById(musicDTO.getEan());
   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Music DTO is converted in right Music Entity")
   void dtoToEntity_returnRightMusicEntity_ofMusicDTO() {
      Music entity;

      for (MusicDTO dto: allMusicDTOS) {
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
      List<Music> music = new ArrayList<>();
      MusicDTO dto;

      for (MusicDTO m: allMusicDTOS) {
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
      MusicDTO musicDTO = musicService.entityToDTO(newMusic);
      List<String> titles = musicService.findAllTitles();
      assertThat(titles.size()).isEqualTo(4);

      // add an other book
      musicDTO.setTitle(MUSIC_TITLE_TEST);
      musicDTO = musicService.save(musicDTO);
      titles = musicService.findAllTitles();
      assertThat(titles.size()).isEqualTo(5);
      assertThat(titles.contains(MUSIC_TITLE_TEST)).isTrue();

      musicService.deleteById(musicDTO.getEan());
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