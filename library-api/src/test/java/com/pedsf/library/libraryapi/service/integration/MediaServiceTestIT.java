package com.pedsf.library.libraryapi.service.integration;

import com.pedsf.library.Parameters;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.*;
import com.pedsf.library.libraryapi.service.*;
import com.pedsf.library.model.MediaType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class MediaServiceTestIT {
   private static final String MEDIA_EAN_TEST = "978-2253002864";
   private static final Integer MEDIA_ID_TEST = 5;
   private static final Integer BOOK_ID_TEST = 5;
   private static final Integer GAME_ID_TEST = 30;
   private static final Integer MUSIC_ID_TEST = 24;
   private static final Integer VIDEO_ID_TEST = 22;
   private static final String MEDIA_TITLE_TEST = "Nouveau titre";

   static private MediaService mediaService;
   static private PersonService personService;
   static private BookService bookService;
   static private GameService gameService;
   static private MusicService musicService;
   static private VideoService videoService;
   private static Media newMedia;
   private static MediaDTO newMediaDto;
   private static List<MediaDTO> allMediaDTOS;

   @BeforeAll
   static void beforeAll( @Autowired MediaRepository mediaRepository,
                          @Autowired BookRepository bookRepository,
                          @Autowired GameRepository gameRepository,
                          @Autowired MusicRepository musicRepository,
                          @Autowired VideoRepository videoRepository,
                          @Autowired PersonRepository personRepository) {

      personService = new PersonService(personRepository);
      bookService = new BookService(bookRepository, personService);
      gameService = new GameService(gameRepository, personService);
      musicService = new MusicService(musicRepository, personService);
      videoService = new VideoService(videoRepository, personService);

      mediaService = new MediaService(mediaRepository, bookService, gameService, musicService, videoService);
   }

   @BeforeEach
   void beforeEach() {
      newMedia = new Media(44,"954-8789797",MediaType.BOOK,MediaStatus.BOOKED);
      newMediaDto = new MediaDTO();

      newMediaDto.setId(44);
      newMediaDto.setEan("954-8789797");
      newMediaDto.setMediaType("BOOK");
      newMediaDto.setStatus("BOOKED");
      newMediaDto.setReturnDate(null);

      allMediaDTOS = mediaService.findAll();
   }

   @Test
   @DisplayName("Verify that return TRUE if the Media exist")
   void existsById_returnTrue_OfAnExistingMediaId() {
      for(MediaDTO mediaDTO : allMediaDTOS) {
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
      MediaDTO found;

      for(MediaDTO mediaDTO : allMediaDTOS) {
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
   @Tag("initialise")
   void initialise_returnMedia_ofBook() {
      MediaDTO mediaDTO = new MediaDTO();
      BookDTO bookDTO = bookService.findById("978-2253010692");
      mediaDTO.initialise(bookDTO);

      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.BOOK.name());
      assertThat(mediaDTO.getReturnDate()).isEqualTo(bookDTO.getReturnDate());
      assertThat(mediaDTO.getTitle()).isEqualTo(bookDTO.getTitle());
      assertThat(mediaDTO.getPublicationDate()).isEqualTo(bookDTO.getPublicationDate());
      assertThat(mediaDTO.getWeight()).isEqualTo(bookDTO.getWeight());
      assertThat(mediaDTO.getLength()).isEqualTo(bookDTO.getLength());
      assertThat(mediaDTO.getWidth()).isEqualTo(bookDTO.getWidth());
      assertThat(mediaDTO.getHeight()).isEqualTo(bookDTO.getHeight());
      assertThat(mediaDTO.getQuantity()).isEqualTo(bookDTO.getQuantity());
      assertThat(mediaDTO.getStock()).isEqualTo(bookDTO.getStock());
   }

   @Test
   @Tag("initialise")
   void initialise_returnMedia_ofGame() {
      MediaDTO mediaDTO = new MediaDTO();
      GameDTO gameDTO = gameService.findById("0805529340299");
      mediaDTO.initialise(gameDTO);

      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.GAME.name());
      assertThat(mediaDTO.getReturnDate()).isEqualTo(gameDTO.getReturnDate());
      assertThat(mediaDTO.getTitle()).isEqualTo(gameDTO.getTitle());
      assertThat(mediaDTO.getPublicationDate()).isEqualTo(gameDTO.getPublicationDate());
      assertThat(mediaDTO.getWeight()).isEqualTo(gameDTO.getWeight());
      assertThat(mediaDTO.getLength()).isEqualTo(gameDTO.getLength());
      assertThat(mediaDTO.getWidth()).isEqualTo(gameDTO.getWidth());
      assertThat(mediaDTO.getHeight()).isEqualTo(gameDTO.getHeight());
      assertThat(mediaDTO.getQuantity()).isEqualTo(gameDTO.getQuantity());
      assertThat(mediaDTO.getStock()).isEqualTo(gameDTO.getStock());
   }

   @Test
   @Tag("initialise")
   void initialise_returnMedia_ofMusic() {
      MediaDTO mediaDTO = new MediaDTO();
      MusicDTO musicDTO = musicService.findById("4988064585816");
      mediaDTO.initialise(musicDTO);

      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.MUSIC.name());
      assertThat(mediaDTO.getReturnDate()).isEqualTo(musicDTO.getReturnDate());
      assertThat(mediaDTO.getTitle()).isEqualTo(musicDTO.getTitle());
      assertThat(mediaDTO.getPublicationDate()).isEqualTo(musicDTO.getPublicationDate());
      assertThat(mediaDTO.getWeight()).isEqualTo(musicDTO.getWeight());
      assertThat(mediaDTO.getLength()).isEqualTo(musicDTO.getLength());
      assertThat(mediaDTO.getWidth()).isEqualTo(musicDTO.getWidth());
      assertThat(mediaDTO.getHeight()).isEqualTo(musicDTO.getHeight());
      assertThat(mediaDTO.getQuantity()).isEqualTo(musicDTO.getQuantity());
      assertThat(mediaDTO.getStock()).isEqualTo(musicDTO.getStock());
   }

   @Test
   @Tag("initialise")
   void initialise_returnMedia_ofVideo() {
      MediaDTO mediaDTO = new MediaDTO();
      VideoDTO videoDTO = videoService.findById("3475001058980");
      mediaDTO.initialise(videoDTO);

      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.VIDEO.name());
      assertThat(mediaDTO.getReturnDate()).isEqualTo(videoDTO.getReturnDate());
      assertThat(mediaDTO.getTitle()).isEqualTo(videoDTO.getTitle());
      assertThat(mediaDTO.getPublicationDate()).isEqualTo(videoDTO.getPublicationDate());
      assertThat(mediaDTO.getWeight()).isEqualTo(videoDTO.getWeight());
      assertThat(mediaDTO.getLength()).isEqualTo(videoDTO.getLength());
      assertThat(mediaDTO.getWidth()).isEqualTo(videoDTO.getWidth());
      assertThat(mediaDTO.getHeight()).isEqualTo(videoDTO.getHeight());
      assertThat(mediaDTO.getQuantity()).isEqualTo(videoDTO.getQuantity());
      assertThat(mediaDTO.getStock()).isEqualTo(videoDTO.getStock());
   }

   @Test
   @Tag("findOneByEan")
   void findOneByEan_returnMedia_ofMediaEAN() {
      for(MediaDTO mediaDTO:allMediaDTOS) {
         MediaDTO found = mediaService.findOneByEan(mediaDTO.getEan());
         assertThat(found.getMediaType()).isEqualTo(mediaDTO.getMediaType());
         assertThat(found.getPublicationDate()).isEqualTo(mediaDTO.getPublicationDate());
      }
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
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Media by EAN title and type")
   void getFirstId_returnFirstId_ofFilteredMediaByTitle() {
      MediaDTO filter = new MediaDTO();
      filter.setEan("978-2253002864");
      filter.setMediaType(MediaType.BOOK.name());
      filter.setTitle(allMediaDTOS.get(6).getTitle());

      Integer id = mediaService.getFirstId(filter);

      assertThat(id).isEqualTo(5);
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Media")
   @Disabled
   void save_returnCreatedMedia_ofNewMedia() {
      MediaDTO MediaDTO = mediaService.entityToDTO(newMedia);

      MediaDTO = mediaService.save(MediaDTO);
      Integer newId = MediaDTO.getId();

      assertThat(mediaService.existsById(newId)).isTrue();
      mediaService.deleteById(newId);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update a Madia")
   void update_returnUpdatedMedia_ofMediaAndNewTitle() {
      MediaDTO mediaDTO = mediaService.findById(MEDIA_ID_TEST);
      String oldTitle = mediaDTO.getTitle();
      mediaDTO.setTitle(MEDIA_TITLE_TEST);

      MediaDTO mediaSaved = mediaService.update(mediaDTO);
      assertThat(mediaSaved).isEqualTo(mediaDTO);
      MediaDTO mediaFound = mediaService.findById(mediaDTO.getId());
      assertThat(mediaFound).isEqualTo(mediaDTO);

      mediaDTO.setTitle(oldTitle);
      mediaService.update(mediaDTO);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Media by his ID")
   @Disabled
   void deleteById_returnExceptionWhenGetMediaById_ofDeletedMediaById() {
      MediaDTO mediaDTO = new MediaDTO();
      mediaDTO.initialise(bookService.findById("978-2253003656"));
      mediaDTO = mediaService.save(mediaDTO);
      Integer id = mediaDTO.getId();

      assertThat(mediaService.existsById(id)).isTrue();
      mediaService.deleteById(id);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         mediaService.findById(id);
      });
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Medias")
   @Disabled
   void count_returnTheNumberOfMedias() {
      MediaDTO mediaDTO = mediaService.entityToDTO(newMedia);
      assertThat(mediaService.count()).isEqualTo(31);

      // add an other media
      mediaDTO = mediaService.save(mediaDTO);
      assertThat(mediaService.count()).isEqualTo(32);

      mediaService.deleteById(mediaDTO.getId());
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Media DTO is converted in right Media Entity")
   void dtoToEntity_returnRightMediaEntity_ofMediaDTO() {
      Media entity;

      for (MediaDTO dto: allMediaDTOS) {
         entity = mediaService.dtoToEntity(dto);
         assertThat(entity.getId()).isEqualTo(dto.getId());
         assertThat(entity.getEan()).isEqualTo(dto.getEan());
         assertThat(entity.getMediaType().name()).isEqualTo(dto.getMediaType());
         assertThat(entity.getStatus().name()).isEqualTo(dto.getStatus());
         assertThat(entity.getReturnDate()).isEqualTo(dto.getReturnDate());
      }
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Media Entity is converted in right Media DTO")
   void dtoToEntity_returnRightMediaDTO_ofMediaEntity() {
      List<Media> medias = new ArrayList<>();
      MediaDTO dto;

      for (MediaDTO m: allMediaDTOS) {
         medias.add(mediaService.dtoToEntity(m));
      }

      for (Media entity: medias) {
         dto = mediaService.entityToDTO(entity);
         assertThat(dto.getId()).isEqualTo(entity.getId());
         assertThat(dto.getEan()).isEqualTo(entity.getEan());
         assertThat(dto.getMediaType()).isEqualTo(entity.getMediaType().name());
         assertThat(dto.getStatus()).isEqualTo(entity.getStatus().name());
         assertThat(dto.getReturnDate()).isEqualTo(entity.getReturnDate());
      }
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Book by his EAN number")
   void increaseStock_returnBookWithIncrementedStock_ofOneBook() {
      MediaDTO mediaDTO = mediaService.findById(BOOK_ID_TEST);
      String ean = mediaDTO.getEan();
      BookDTO bookDTO = bookService.findById(ean);
      Integer oldStock = bookDTO.getStock();

      mediaService.increaseStock(mediaDTO);
      bookDTO = bookService.findById(ean);

      assertThat(bookDTO.getStock()).isEqualTo(oldStock+1);

      bookDTO.setStock(oldStock);
      bookService.update(bookDTO);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Music by his EAN number")
   void increaseStock_returnMusicWithIncrementedStock_ofOneMusic() {
      MediaDTO mediaDTO = mediaService.findById(MUSIC_ID_TEST);
      String ean = mediaDTO.getEan();
      MusicDTO musicDTO= musicService.findById(ean);
      Integer oldStock = musicDTO.getStock();

      mediaService.increaseStock(mediaDTO);
      musicDTO = musicService.findById(ean);

      assertThat(musicDTO.getStock()).isEqualTo(oldStock+1);

      musicDTO.setStock(oldStock);
      musicService.update(musicDTO);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Game by his EAN number")
   void increaseStock_returnGameWithIncrementedStock_ofOneGame() {
      MediaDTO mediaDTO = mediaService.findById(GAME_ID_TEST);
      String ean = mediaDTO.getEan();
      GameDTO gameDTO = gameService.findById(ean);
      Integer oldStock = gameDTO.getStock();

      mediaService.increaseStock(mediaDTO);
      gameDTO = gameService.findById(ean);

      assertThat(gameDTO.getStock()).isEqualTo(oldStock+1);

      gameDTO.setStock(oldStock);
      gameService.update(gameDTO);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Video by his EAN number")
   void increaseStock_returnMusicVideoDecrementedStock_ofOneVideo() {
      MediaDTO mediaDTO = mediaService.findById(VIDEO_ID_TEST);
      String ean = mediaDTO.getEan();
      VideoDTO videoDTO= videoService.findById(ean);
      Integer oldStock = videoDTO.getStock();

      mediaService.increaseStock(mediaDTO);
      videoDTO = videoService.findById(ean);

      assertThat(videoDTO.getStock()).isEqualTo(oldStock+1);

      videoDTO.setStock(oldStock);
      videoService.update(videoDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Book by his EAN number")
   void decreaseStock_returnBookWithDecrementedStock_ofOneBook() {
      MediaDTO mediaDTO = mediaService.findById(BOOK_ID_TEST);
      String ean = mediaDTO.getEan();
      BookDTO bookDTO = bookService.findById(ean);
      Integer oldStock = bookDTO.getStock();

      mediaService.decreaseStock(mediaDTO);
      bookDTO = bookService.findById(ean);

      assertThat(bookDTO.getStock()).isEqualTo(oldStock-1);

      bookDTO.setStock(oldStock);
      bookService.update(bookDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can increase the stock of a Game by his EAN number")
   void decreaseStock_returnGameWithDecrementedStock_ofOneGame() {
      MediaDTO mediaDTO = mediaService.findById(GAME_ID_TEST);
      String ean = mediaDTO.getEan();
      GameDTO gameDTO = gameService.findById(ean);
      Integer oldStock = gameDTO.getStock();

      mediaService.decreaseStock(mediaDTO);
      gameDTO = gameService.findById(ean);

      assertThat(gameDTO.getStock()).isEqualTo(oldStock-1);

      gameDTO.setStock(oldStock);
      gameService.update(gameDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Music by his EAN number")
   void decreaseStock_returnMusicWithDecrementedStock_ofOneMusic() {
      MediaDTO mediaDTO = mediaService.findById(MUSIC_ID_TEST);
      String ean = mediaDTO.getEan();
      MusicDTO musicDTO= musicService.findById(ean);
      Integer oldStock = musicDTO.getStock();

      mediaService.decreaseStock(mediaDTO);
      musicDTO = musicService.findById(ean);

      assertThat(musicDTO.getStock()).isEqualTo(oldStock-1);

      musicDTO.setStock(oldStock);
      musicService.update(musicDTO);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can increase the stock of a Video by his EAN number")
   void decreaseStock_returnMusicVideoDecrementedStock_ofOneVideo() {
      MediaDTO mediaDTO = mediaService.findById(VIDEO_ID_TEST);
      String ean = mediaDTO.getEan();
      VideoDTO videoDTO= videoService.findById(ean);
      Integer oldStock = videoDTO.getStock();

      mediaService.decreaseStock(mediaDTO);
      videoDTO = videoService.findById(ean);

      assertThat(videoDTO.getStock()).isEqualTo(oldStock-1);

      videoDTO.setStock(oldStock);
      videoService.update(videoDTO);
   }

   @Test
   @Tag("findMediaTypeByEan")
   @DisplayName("Give the media type by his EAN code")
   void findMediaTypeByEan_returnMediaType_ofMediaByEAN() {
      MediaDTO mediaDTO = mediaService.findById(BOOK_ID_TEST);
      MediaType mediaType =  mediaService.findMediaTypeByEan(mediaDTO.getEan());
      assertThat(mediaType).isEqualTo(MediaType.BOOK);

      mediaDTO = mediaService.findById(GAME_ID_TEST);
      mediaType =  mediaService.findMediaTypeByEan(mediaDTO.getEan());
      assertThat(mediaType).isEqualTo(MediaType.GAME);

      mediaDTO = mediaService.findById(MUSIC_ID_TEST);
      mediaType =  mediaService.findMediaTypeByEan(mediaDTO.getEan());
      assertThat(mediaType).isEqualTo(MediaType.MUSIC);

      mediaDTO = mediaService.findById(VIDEO_ID_TEST);
      mediaType =  mediaService.findMediaTypeByEan(mediaDTO.getEan());
      assertThat(mediaType).isEqualTo(MediaType.VIDEO);
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