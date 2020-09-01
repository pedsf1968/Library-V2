package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.*;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.*;
import com.pedsf.library.libraryapi.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class MusicServiceTest {
   private static final String MUSIC_TITLE_TEST = "New Music Title";
   private static final List<PersonDTO> allPersons = new ArrayList<>();
   private static final List<Music> allMusics = new ArrayList<>();
   private static final List<Music> allAllowedMusics = new ArrayList<>();

   @Mock
   private static PersonService personService;
   @Mock
   private static MusicRepository musicRepository;
   private static MusicService musicService;
   private static Music newMusic;
   private static MusicDTO newMusicDTO;

   @BeforeAll
   static void beforeAll() {
      allPersons.add( new PersonDTO(1,"Emile","ZOLA", Date.valueOf("1840-04-02")));
      allPersons.add( new PersonDTO(2,"Gustave","FLAUBERT", Date.valueOf("1821-12-12")));
      allPersons.add( new PersonDTO(3,"Victor","HUGO", Date.valueOf("1802-02-26")));
      allPersons.add( new PersonDTO(4,"Joon-Ho","BONG",Date.valueOf("1969-09-14")));
      allPersons.add( new PersonDTO(5,"Sun-Kyun","LEE",Date.valueOf("1975-03-02")));
      allPersons.add( new PersonDTO(6,"Kang-Ho","SONG",Date.valueOf("1967-01-17")));
      allPersons.add( new PersonDTO(7,"Yeo-Jeong","CHO",Date.valueOf("1981-02-10")));
      allPersons.add( new PersonDTO(8,"Woo-Shik","CHOI",Date.valueOf("1986-03-26")));
      allPersons.add( new PersonDTO(9,"So-Dam","PARK", Date.valueOf("1991-09-08")));
      allPersons.add( new PersonDTO(10,"LGF","Librairie Générale Française",null));
      allPersons.add( new PersonDTO(11,"Gallimard","Gallimard",null));
      allPersons.add( new PersonDTO(12,"Larousse","Larousse",null));
      allPersons.add( new PersonDTO(13,"Blackpink","Blackpink",Date.valueOf("2016-06-01")));
      allPersons.add( new PersonDTO(14,"BigBang","BigBang",Date.valueOf("2006-08-19")));
      allPersons.add( new PersonDTO(15,"EA","Electronic Arts",Date.valueOf("1982-05-28")));
      allPersons.add( new PersonDTO(16,"Microsoft","Microsoft",null));

      allMusics.add(new Music("8809634380036","Kill This Love",2,2,14,14,14));
      allMusics.add(new Music("4988064587100","DDU-DU DDU-DU",3,-6,14,14,14));
      allMusics.add(new Music("4988064585816","RE BLACKPINK",1,1,14,14,14));
      allMusics.add(new Music("8809269506764","MADE",1,-2,15,15,15));
      allMusics.add(new Music("8809265654654","Remember",1,0,15,15,15));

      allAllowedMusics.add(new Music("8809634380036","Kill This Love",2,2,14,14,14));
      allAllowedMusics.add(new Music("4988064585816","RE BLACKPINK",1,1,14,14,14));
      allAllowedMusics.add(new Music("8809265654654","Remember",1,0,15,15,15));

      for(Music music:allMusics) {
         music.setFormat(MusicFormat.CD);
         music.setType(MusicType.POP);
      }
      for(Music music:allAllowedMusics) {
         music.setFormat(MusicFormat.CD);
         music.setType(MusicType.POP);
      }
   }

   @BeforeEach
   void beforeEach() {
      musicService = new MusicService(musicRepository,personService);

      newMusic = new Music("9876546546","Le petite musique du matin", 1,1,15,15,15);
      newMusic.setDuration(123);
      newMusic.setFormat(MusicFormat.SACD);
      newMusic.setType(MusicType.ELECTRO);
      newMusic.setUrl("http://www.google.co.kr");
      newMusic.setHeight(11);
      newMusic.setLength(11);
      newMusic.setWidth(11);
      newMusic.setWeight(220);

      newMusicDTO = new MusicDTO("9876546546","Le petite musique du matin", 1,1,allPersons.get(14),allPersons.get(14),allPersons.get(14));
      newMusicDTO.setDuration(123);
      newMusicDTO.setFormat("SACD");
      newMusicDTO.setType("ELECTRO");
      newMusicDTO.setUrl("http://www.google.co.kr");
      newMusicDTO.setHeight(11);
      newMusicDTO.setLength(11);
      newMusicDTO.setWidth(11);
      newMusicDTO.setWeight(220);

      Mockito.lenient().when(musicRepository.findAll()).thenReturn(allMusics);
      Mockito.lenient().when(musicRepository.findAllAllowed()).thenReturn(allAllowedMusics);
      Mockito.lenient().when(musicRepository.findByEan("8809634380036")).thenReturn(java.util.Optional.ofNullable(allMusics.get(0)));
      Mockito.lenient().when(musicRepository.findByEan("4988064587100")).thenReturn(java.util.Optional.ofNullable(allMusics.get(1)));
      Mockito.lenient().when(musicRepository.findByEan("4988064585816")).thenReturn(java.util.Optional.ofNullable(allMusics.get(2)));
      Mockito.lenient().when(musicRepository.findByEan("8809269506764")).thenReturn(java.util.Optional.ofNullable(allMusics.get(3)));
      Mockito.lenient().when(musicRepository.findByEan("8809265654654")).thenReturn(java.util.Optional.ofNullable(allMusics.get(4)));
      Mockito.lenient().when(musicRepository.findByEan("9876546546")).thenReturn(java.util.Optional.ofNullable(newMusic));

      Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]-1));
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Music exist")
   void existsById_returnTrue_OfAnExistingMusicId() {
      for(Music music : allMusics) {
         String ean = music.getEan();
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

      for(Music music : allMusics) {
         String ean = music.getEan();
         found = musicService.findById(ean);

         assertThat(found).isEqualTo(musicService.entityToDTO(music));
      }
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Music with wrong ID")
   void findById_returnException_ofInexistingMusicId() {
      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> musicService.findById("liuzae"));
   }


   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Musics")
   void findAll_returnAllMusics() {
      List<MusicDTO> musicDTOS = musicService.findAll();

      assertThat(musicDTOS.size()).isEqualTo(5);

      for(Music music:allMusics) {
         MusicDTO musicDTO = musicService.entityToDTO(music);
         assertThat(musicDTOS.contains(musicDTO)).isTrue();
      }
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no Music")
   void findAll_throwResourceNotFoundException_ofEmptyList() {
      List<Music> emptyList = new ArrayList<>();
      Mockito.lenient().when(musicRepository.findAll()).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> musicService.findAll());
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Musics that can be booked")
   void findAllAllowed_returnBookableMusics_ofAllMusics() {
      List<MusicDTO> alloweds = musicService.findAllAllowed();
      assertThat(alloweds.size()).isEqualTo(3);

      for(Music music: allMusics) {
         MusicDTO musicDTO = musicService.entityToDTO(music);

         if (alloweds.contains(musicDTO)) {
            // allowed
            assertThat(musicDTO.getQuantity()*2).isGreaterThan(-musicDTO.getStock());
         } else {
            // not allowed
            assertThat(musicDTO.getQuantity()*2).isEqualTo(-musicDTO.getStock());
         }
      }
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Music by his title and interpreter")
   void findAllFiltered_returnOnlyOneMusic_ofExistingTitleAndInterpreter() {
      List<MusicDTO> found;

      for(Music music:allMusics) {
         Music filter = new Music();
         filter.setTitle(music.getTitle());
         filter.setInterpreterId(music.getInterpreterId());

         Mockito.lenient().when(musicRepository.findAll(any(MusicSpecification.class))).thenReturn(Collections.singletonList(music));

         MusicDTO filterDTO = musicService.entityToDTO(filter);
         found = musicService.findAllFiltered(filterDTO);

         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(musicService.entityToDTO(music));
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Music by Author")
   void getFirstId_returnFirstId_ofFilteredMusicByAuthor() {
      MusicDTO filter = new MusicDTO();
      filter.setAuthor(personService.findById(14));

      Mockito.lenient().when(musicRepository.findAll(any(MusicSpecification.class))).thenReturn(Arrays.asList(allMusics.get(0),allMusics.get(1),allMusics.get(2)));

      String ean = musicService.getFirstId(filter);

      assertThat(ean).isEqualTo("8809634380036");
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Music")
   void save_returnCreatedMusic_ofNewMusic() {
      Mockito.lenient().when(musicRepository.save(any(Music.class))).thenReturn(newMusic);

      MusicDTO found = musicService.save(newMusicDTO);
      String ean = found.getEan();

      assertThat(musicService.existsById(ean)).isTrue();
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Music with has no title")
   void save_throwBadRequestException_ofNewMusicWithNoTitle() {
      newMusicDTO.setTitle("");
      Assertions.assertThrows(BadRequestException.class, ()-> musicService.save(newMusicDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Music with has no Interpreter")
   void save_throwBadRequestException_ofNewMusicWithNoInterpreter() {
      newMusicDTO.setInterpreter(null);
      Assertions.assertThrows(BadRequestException.class, ()-> musicService.save(newMusicDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Music with has no Composer")
   void save_throwBadRequestException_ofNewMusicWithNoComposer() {
      newMusicDTO.setComposer(null);
      Assertions.assertThrows(BadRequestException.class, ()-> musicService.save(newMusicDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Music with has no Author")
   void save_throwBadRequestException_ofNewMusicWithNoAuthor() {
      newMusicDTO.setAuthor(null);
      Assertions.assertThrows(BadRequestException.class, ()-> musicService.save(newMusicDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Music with has no Format")
   void save_throwBadRequestException_ofNewMusicWithNoFormat() {
      newMusicDTO.setFormat(null);
      Assertions.assertThrows(BadRequestException.class, ()-> musicService.save(newMusicDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Music with has no Type")
   void save_throwBadRequestException_ofNewMusicWithNoType() {
      newMusicDTO.setType(null);
      Assertions.assertThrows(BadRequestException.class, ()-> musicService.save(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Music")
   void update_returnUpdatedMusic_ofMusicAndNewTitle() {
      String oldTitle = newMusic.getTitle();
      newMusic.setTitle(MUSIC_TITLE_TEST);
      newMusicDTO.setTitle(MUSIC_TITLE_TEST);
      Mockito.lenient().when(musicRepository.save(any(Music.class))).thenReturn(newMusic);

      MusicDTO musicSaved = musicService.update(newMusicDTO);
      assertThat(musicSaved).isEqualTo(newMusicDTO);

      newMusic.setTitle(oldTitle);
      newMusicDTO.setTitle(oldTitle);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when saving a Music with has no title")
   void update_throwConflictException_ofNewMusicWithNoTitle() {
      newMusicDTO.setTitle("");
      Assertions.assertThrows(ConflictException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Music with has no Interpreter")
   void update_throwConflictException_ofNewMusicWithNoInterpreter() {
      newMusicDTO.setInterpreter(null);
      Assertions.assertThrows(ConflictException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Music with has no Composer")
   void update_throwConflictException_ofNewMusicWithNoComposer() {
      newMusicDTO.setComposer(null);
      Assertions.assertThrows(ConflictException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have BadRequestException when update a Music with has no Author")
   void update_throwConflictException_ofNewMusicWithNoAuthor() {
      newMusicDTO.setAuthor(null);
      Assertions.assertThrows(ConflictException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Music with has no Format")
   void update_throwConflictException_ofNewMusicWithNoFormat() {
      newMusicDTO.setFormat(null);
      Assertions.assertThrows(ConflictException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Music with has no Type")
   void update_throwConflictException_ofNewMusicWithNoType() {
      newMusicDTO.setType(null);
      Assertions.assertThrows(ConflictException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ResourceNotFoundException when update a Music with bad ID")
   void update_throwResourceNotFoundException_ofNewMusicWithWrongId() {
      newMusicDTO.setEan("mlkhmlkjmlk");
      Assertions.assertThrows(ResourceNotFoundException.class, ()-> musicService.update(newMusicDTO));
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Music by his EAN")
   void deleteById_returnExceptionWhenGetMusicById_ofDeletedMusicById() {
      String ean = newMusicDTO.getEan();

      assertThat(musicService.existsById(ean)).isTrue();
      musicService.deleteById(ean);

      Mockito.lenient().when(musicRepository.findByEan(ean)).thenThrow(ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> musicService.findById(ean));
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we have ResourceNotFoundException when deleting a Music with bad EAN")
   void deleteById_throwResourceNotFoundException_ofMusicWithBadEAN() {

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> musicService.deleteById("WRONG EAN"));
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Musics")
   void count_returnTheNumberOfMusics() {
      Mockito.lenient().when(musicRepository.count()).thenReturn(5L);
      assertThat(musicService.count()).isEqualTo(5);
   }

   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Music DTO is converted in right Music Entity")
   void dtoToEntity_returnRightMusicEntity_ofMusicDTO() {
      Music entity = musicService.dtoToEntity(newMusicDTO);
      assertThat(entity.getEan()).isEqualTo(newMusicDTO.getEan());
      assertThat(entity.getTitle()).isEqualTo(newMusicDTO.getTitle());
      assertThat(entity.getQuantity()).isEqualTo(newMusicDTO.getQuantity());
      assertThat(entity.getStock()).isEqualTo(newMusicDTO.getStock());
      assertThat(entity.getHeight()).isEqualTo(newMusicDTO.getHeight());
      assertThat(entity.getLength()).isEqualTo(newMusicDTO.getLength());
      assertThat(entity.getWeight()).isEqualTo(newMusicDTO.getWeight());
      assertThat(entity.getWidth()).isEqualTo(newMusicDTO.getWidth());
      assertThat(entity.getReturnDate()).isEqualTo(newMusicDTO.getReturnDate());

      assertThat(entity.getDuration()).isEqualTo(newMusicDTO.getDuration());
      assertThat(entity.getAuthorId()).isEqualTo(newMusicDTO.getAuthor().getId());
      assertThat(entity.getComposerId()).isEqualTo(newMusicDTO.getComposer().getId());
      assertThat(entity.getInterpreterId()).isEqualTo(newMusicDTO.getInterpreter().getId());
      assertThat(entity.getUrl()).isEqualTo(newMusicDTO.getUrl());
      assertThat(entity.getPublicationDate()).isEqualTo(newMusicDTO.getPublicationDate());
      assertThat(entity.getFormat().name()).isEqualTo(newMusicDTO.getFormat());
      assertThat(entity.getType().name()).isEqualTo(newMusicDTO.getType());
   }


   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Music Entity is converted in right Music DTO")
   void dtoToEntity_returnRightMusicDTO_ofMusicEntity() {
      MusicDTO dto = musicService.entityToDTO(newMusic);
      assertThat(dto.getEan()).isEqualTo(newMusic.getEan());
      assertThat(dto.getTitle()).isEqualTo(newMusic.getTitle());
      assertThat(dto.getQuantity()).isEqualTo(newMusic.getQuantity());
      assertThat(dto.getStock()).isEqualTo(newMusic.getStock());
      assertThat(dto.getHeight()).isEqualTo(newMusic.getHeight());
      assertThat(dto.getLength()).isEqualTo(newMusic.getLength());
      assertThat(dto.getWeight()).isEqualTo(newMusic.getWeight());
      assertThat(dto.getWidth()).isEqualTo(newMusic.getWidth());
      assertThat(dto.getReturnDate()).isEqualTo(newMusic.getReturnDate());

      assertThat(dto.getDuration()).isEqualTo(newMusic.getDuration());
      assertThat(dto.getAuthor().getId()).isEqualTo(newMusic.getAuthorId());
      assertThat(dto.getComposer().getId()).isEqualTo(newMusic.getComposerId());
      assertThat(dto.getInterpreter().getId()).isEqualTo(newMusic.getInterpreterId());
      assertThat(dto.getUrl()).isEqualTo(newMusic.getUrl());
      assertThat(dto.getPublicationDate()).isEqualTo(newMusic.getPublicationDate());
      assertThat(dto.getFormat()).isEqualTo(newMusic.getFormat().name());
      assertThat(dto.getType()).isEqualTo(newMusic.getType().name());
   }

   @Test
   @Tag("findAllAuthors")
   @DisplayName("Verify that we get all Musics authors")
   void findAllAuthors() {
      Mockito.lenient().when(musicRepository.findAllAuthors()).thenReturn(Arrays.asList(14,15));

      List<PersonDTO> personDTOS = musicService.findAllAuthors();

      assertThat(personDTOS.size()).isEqualTo(2);
      assertThat(personDTOS.contains(allPersons.get(13))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(14))).isTrue();
   }

   @Test
   @Tag("findAllComposers")
   @DisplayName("Verify that we get all Musics composers")
   void findAllComposers() {
      Mockito.lenient().when(musicRepository.findAllComposers()).thenReturn(Arrays.asList(14,15));

      List<PersonDTO> personDTOS = musicService.findAllComposers();

      assertThat(personDTOS.size()).isEqualTo(2);
      assertThat(personDTOS.contains(allPersons.get(13))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(14))).isTrue();
   }

   @Test
   @Tag("findAllInterpreters")
   @DisplayName("Verify that we get all Musics interpreters")
   void findAllInterpreters() {
      Mockito.lenient().when(musicRepository.findAllInterpreters()).thenReturn(Arrays.asList(14,15));

      List<PersonDTO> personDTOS = musicService.findAllInterpreters();

      assertThat(personDTOS.size()).isEqualTo(2);
      assertThat(personDTOS.contains(allPersons.get(13))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(14))).isTrue();
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Books titles")
   void findAllTitles() {
      List<String> titles = Arrays.asList("Kill This Love","DDU-DU DDU-DU","RE BLACKPINK","MADE","Remember");

      Mockito.lenient().when(musicRepository.findAllTitles()).thenReturn(titles);

      List<String> foundTitles = musicService.findAllTitles();
      assertThat(foundTitles.size()).isEqualTo(5);
      for(String title:foundTitles) {
         assertThat(titles.contains(title)).isTrue();
      }
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Musics by his EAN number")
   void increaseStock_returnMusicsWithIncrementedStock_ofOneMusics() {
      String ean = newMusic.getEan();
      Integer oldStock = newMusic.getStock();
      newMusic.setStock(oldStock+1);
      Mockito.lenient().when(musicRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newMusic));

      musicService.increaseStock(ean);

      MusicDTO musicDTO = musicService.findById(ean);
      assertThat(musicDTO.getStock()).isEqualTo(oldStock+1);

      newMusic.setStock(oldStock);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Musics by his EAN number")
   void decreaseStock_returnMusicsWithDecrementedStock_ofOneMusics() {
      String ean = newMusic.getEan();
      Integer oldStock = newMusic.getStock();
      newMusic.setStock(oldStock-1);
      Mockito.lenient().when(musicRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newMusic));

      musicService.decreaseStock(ean);

      MusicDTO musicDTO = musicService.findById(ean);
      assertThat(musicDTO.getStock()).isEqualTo(oldStock-1);

      newMusic.setStock(oldStock);
   }
}