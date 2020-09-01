package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.*;
import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.BadRequestException;
import com.pedsf.library.exception.ConflictException;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.model.*;
import com.pedsf.library.libraryapi.repository.*;
import com.pedsf.library.libraryapi.service.*;
import com.pedsf.library.model.MediaType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class MediaServiceTest {
   private static final String MEDIA_TITLE_TEST = "Nouveau titre";
   private static final List<PersonDTO> allPersons = new ArrayList<>();
   private static final List<Media> allMedias = new ArrayList<>();
   private static final List<Media> allFreeMedias = new ArrayList<>();


   @Mock
   private PersonService personService;
   @Mock
   private BookService bookService;
   @Mock
   private GameService gameService;
   @Mock
   private MusicService musicService;
   @Mock
   private VideoService videoService;
   @Mock
   private MediaRepository mediaRepository;
   private MediaService mediaService;
   private static Media newMedia;
   private static MediaDTO newMediaDTO;
   private static BookDTO bookDTO;
   private static GameDTO gameDTO;
   private static MusicDTO musicDTO;
   private static VideoDTO videoDTO;

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

      allMedias.add(new Media( 1,"978-2253004226",MediaType.BOOK,MediaStatus.BORROWED, Date.valueOf("2020-08-10")));
      allMedias.add(new Media(2,"978-2253004226",MediaType.BOOK,MediaStatus.BORROWED,Date.valueOf("2020-08-17")));
      allMedias.add(new Media(3,"978-2253004226",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(4,"978-2253004226",MediaType.BOOK, MediaStatus.FREE,null));
      allMedias.add(new Media(5,"978-2253002864",MediaType.BOOK,MediaStatus.BORROWED,Date.valueOf("2020-08-10")));
      allMedias.add(new Media(6,"978-2253002864",MediaType.BOOK,MediaStatus.BORROWED,Date.valueOf("2020-08-12")));
      allMedias.add(new Media(7,"978-2253003656",MediaType.BOOK,MediaStatus.BORROWED,Date.valueOf("2020-08-17")));
      allMedias.add(new Media(8,"978-2253003656",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(9,"978-2253010692",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(10,"978-2253010692",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(11,"978-2070413119",MediaType.BOOK,MediaStatus.BORROWED,Date.valueOf("2020-08-17")));
      allMedias.add(new Media(12,"978-2070413119",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(13,"978-2070413119",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(14,"978-2253096337",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(15,"978-2253096337",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(16,"978-2253096337",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(17,"978-2253096344",MediaType.BOOK,MediaStatus.BORROWED,Date.valueOf("2020-08-17")));
      allMedias.add(new Media(18,"978-2253096344",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(19,"978-2253096344",MediaType.BOOK,MediaStatus.FREE,null));
      allMedias.add(new Media(20,"3475001058980",MediaType.VIDEO,MediaStatus.BORROWED,Date.valueOf("2020-08-17")));
      allMedias.add(new Media(21,"3475001058980",MediaType.VIDEO,MediaStatus.FREE,null));
      allMedias.add(new Media(22,"3475001058980",MediaType.VIDEO,MediaStatus.FREE,null));
      allMedias.add(new Media(23,"8809634380036",MediaType.MUSIC,MediaStatus.FREE,null));
      allMedias.add(new Media(24,"8809634380036",MediaType.MUSIC,MediaStatus.FREE,null));
      allMedias.add(new Media(25,"4988064587100",MediaType.MUSIC,MediaStatus.FREE,null));
      allMedias.add(new Media(26,"4988064587100",MediaType.MUSIC,MediaStatus.FREE,null));
      allMedias.add(new Media(27,"4988064585816",MediaType.MUSIC,MediaStatus.BORROWED,Date.valueOf("2020-08-10")));
      allMedias.add(new Media(28,"8809269506764",MediaType.MUSIC,MediaStatus.FREE,null));
      allMedias.add(new Media(29,"5035223122470",MediaType.GAME,MediaStatus.FREE,null));
      allMedias.add(new Media(30,"5035223122470",MediaType.GAME,MediaStatus.FREE,null));
      allMedias.add(new Media(31,"0805529340299",MediaType.GAME,MediaStatus.FREE,null));

      allFreeMedias.add(new Media(3,"978-2253004226",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(4,"978-2253004226",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(8,"978-2253003656",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(9,"978-2253010692",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(10,"978-2253010692",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(12,"978-2070413119",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(13,"978-2070413119",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(14,"978-2253096337",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(15,"978-2253096337",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(16,"978-2253096337",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(18,"978-2253096344",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(19,"978-2253096344",MediaType.BOOK,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(21,"3475001058980",MediaType.VIDEO,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(22,"3475001058980",MediaType.VIDEO,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(23,"8809634380036",MediaType.MUSIC,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(24,"8809634380036",MediaType.MUSIC,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(25,"4988064587100",MediaType.MUSIC,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(26,"4988064587100",MediaType.MUSIC,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(28,"8809269506764",MediaType.MUSIC,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(29,"5035223122470",MediaType.GAME,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(30,"5035223122470",MediaType.GAME,MediaStatus.FREE,null));
      allFreeMedias.add(new Media(31,"0805529340299",MediaType.GAME,MediaStatus.FREE,null));

      bookDTO = new BookDTO("954-8789797","The green tomato",1,1,"9548789797",allPersons.get(1),allPersons.get(9));
      bookDTO.setPages(125);
      bookDTO.setFormat(BookFormat.COMICS.name());
      bookDTO.setType(BookType.HUMOR.name());
      bookDTO.setHeight(11);
      bookDTO.setLength(11);
      bookDTO.setWidth(11);
      bookDTO.setWeight(220);
      bookDTO.setReturnDate(Date.valueOf("1999-07-11"));
      bookDTO.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      gameDTO = new GameDTO("954-87sdf797","The green tomato",1,1,allPersons.get(15));
      gameDTO.setFormat("NINTENDO_WII");
      gameDTO.setType("ADVENTURE");
      gameDTO.setHeight(11);
      gameDTO.setLength(11);
      gameDTO.setWidth(11);
      gameDTO.setWeight(220);
      gameDTO.setPegi("3+");
      gameDTO.setSummary("Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
            "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin " +
            "professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur," +
            " from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered " +
            "the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of de Finibus Bonorum et " +
            "Malorum (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory" +
            " of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, Lorem ipsum dolor sit amet.." +
            " comes from a line in section 1.10.32.");

      musicDTO = new MusicDTO("9876546546","Le petite musique du matin", 1,1,allPersons.get(14),allPersons.get(14),allPersons.get(14));
      musicDTO.setDuration(123);
      musicDTO.setFormat(MusicFormat.SACD.name());
      musicDTO.setType(MusicType.ELECTRO.name());
      musicDTO.setUrl("http://www.google.co.kr");
      musicDTO.setHeight(11);
      musicDTO.setLength(11);
      musicDTO.setWidth(11);
      musicDTO.setWeight(220);

      videoDTO = new VideoDTO("sdfsdfds","Video of the Day", 1,1,allPersons.get(14));
      videoDTO.setDuration(123);
      videoDTO.setFormat(VideoFormat.DVD.name());
      videoDTO.setType(VideoType.DOCUMENT.name());
      videoDTO.setUrl("http://www.google.co.kr");
      videoDTO.setHeight(11);
      videoDTO.setLength(11);
      videoDTO.setWidth(11);
      videoDTO.setWeight(220);
      videoDTO.setAudience("Tout public");
      videoDTO.setDuration(310);
      videoDTO.setAudio("5.1");
      videoDTO.setActors(new ArrayList<>());
   }

   @BeforeEach
   void beforeEach() {
      mediaService = new MediaService(mediaRepository, bookService, gameService, musicService,videoService);

      newMedia = new Media(44,bookDTO.getEan(),MediaType.BOOK,MediaStatus.BOOKED,Date.valueOf("1999-07-11"));
      newMediaDTO = new MediaDTO();
      newMediaDTO.setId(44);
      newMediaDTO.setStatus(MediaStatus.BOOKED.name());
      newMediaDTO.initialise(bookDTO);

      Mockito.lenient().when(mediaRepository.findAll()).thenReturn(allMedias);
      Mockito.lenient().when(bookService.findById(any())).thenReturn(bookDTO);
      Mockito.lenient().when(gameService.findById(any())).thenReturn(gameDTO);
      Mockito.lenient().when(musicService.findById(any())).thenReturn(musicDTO);
      Mockito.lenient().when(videoService.findById(any())).thenReturn(videoDTO);

      Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]-1));

   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return TRUE if the Media exist")
   void existsById_returnTrue_OfAnExistingMediaId() {
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> java.util.Optional.ofNullable(allMedias.get((Integer) invocation.getArguments()[0]-1)));

      for(Media media : allMedias) {
         Integer id = media.getId();
         assertThat(mediaService.existsById(id)).isTrue();
      }
   }

   @Test
   @Tag("existsById")
   @DisplayName("Verify that return FALSE if the Media doesn't exist")
   void existsById_returnFalse_OfAnInexistingMediaId() {
      Mockito.lenient().when(mediaRepository.findById(55)).thenThrow(com.pedsf.library.exception.ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,()->mediaService.existsById(55));
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can find Media by is ID")
   void findById_returnMedia_ofExistingMediaId() {
      Integer mediaId = newMedia.getId();
      String ean = newMedia.getEan();
      Mockito.lenient().when(bookService.findById(ean)).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.findById(mediaId)).thenReturn(java.util.Optional.ofNullable(newMedia));

      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found).isEqualTo(newMediaDTO);
   }

   @Test
   @Tag("findById")
   @DisplayName("Verify that we can't find Media with wrong ID")
   void findById_returnException_ofInexistingMediaId() {
      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> mediaService.findById(555));
   }


   @Test
   @Tag("initialise")
   @DisplayName("Verify that we can initialise MediaDTO with BookDTO")
   void initialise_returnMedia_ofBook() {
      MediaDTO mediaDTO = new MediaDTO();
      mediaDTO.initialise(bookDTO);

      assertThat(mediaDTO.getEan()).isEqualTo(bookDTO.getEan());
      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.BOOK.name());
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
   @DisplayName("Verify that we can initialise MediaDTO with GameDTO")
   void initialise_returnMedia_ofGame() {
      MediaDTO mediaDTO = new MediaDTO();
      mediaDTO.initialise(gameDTO);

      assertThat(mediaDTO.getEan()).isEqualTo(gameDTO.getEan());
      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.GAME.name());
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
   @DisplayName("Verify that we can initialise MediaDTO with MusicDTO")
   void initialise_returnMedia_ofMusic() {
      MediaDTO mediaDTO = new MediaDTO();
      mediaDTO.initialise(musicDTO);

      assertThat(mediaDTO.getEan()).isEqualTo(musicDTO.getEan());
      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.MUSIC.name());
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
   @DisplayName("Verify that we can initialise MediaDTO with VideoDTO")
   void initialise_returnMedia_ofVideo() {
      MediaDTO mediaDTO = new MediaDTO();
      mediaDTO.initialise(videoDTO);

      assertThat(mediaDTO.getEan()).isEqualTo(videoDTO.getEan());
      assertThat(mediaDTO.getMediaType()).isEqualTo(MediaType.VIDEO.name());
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
   @DisplayName("Verify that we can find Media by his EAN")
   void findOneByEan_returnMedia_ofMediaEAN() {
      String ean = newMedia.getEan();

      Mockito.lenient().when(bookService.findById(anyString())).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.findOneByEan(ean)).thenReturn(newMedia);

      MediaDTO found = mediaService.findOneByEan(ean);
      assertThat(found).isEqualTo(newMediaDTO);
   }

   @Test
   @Tag("findOneByEan")
   @DisplayName("Verify that we get ResourceNotFoundException finding Media by wrong EAN")
   void findOneByEan_throwResourceNotFoundException_ofWrongEAN() {
      Mockito.lenient().when(mediaRepository.findOneByEan(anyString())).thenThrow(com.pedsf.library.exception.ResourceNotFoundException.class);
      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> mediaService.findOneByEan("WRONGEAN)"));
   }

   @Test
   @Tag("findFreeByEan")
   @DisplayName("Verify that we got one free Media above free EAN list")
   void findFreeByEan_returnMediaFreeList_ofEANCodeMedia() {
      String ean = newMedia.getEan();
      Mockito.lenient().when(bookService.findById(anyString())).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.findFreeByEan(ean)).thenReturn(newMedia);

      MediaDTO found = mediaService.findFreeByEan(ean);

      assertThat(found).isEqualTo(newMediaDTO);
   }

   @Test
   @Tag("findFreeByEan")
   @DisplayName("Verify that we get ResourceNotFoundException finding Media FREE by wrong EAN")
   void findFreeByEan_throwResourceNotFoundException_ofWrongEAN() {
      Mockito.lenient().when(mediaRepository.findFreeByEan(anyString())).thenThrow(com.pedsf.library.exception.ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> mediaService.findFreeByEan("WRONGEAN)"));
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have the list of all Medias")
   void findAll_returnAllMedias() {
      List<MediaDTO> mediaDTOS = mediaService.findAll();
      assertThat(mediaDTOS.size()).isEqualTo(31);
   }

   @Test
   @Tag("findAll")
   @DisplayName("Verify that we have ResourceNotFoundException if there is no Media")
   void findAll_throwResourceNotFoundException_ofEmptyList() {
      List<Media> emptyList = new ArrayList<>();
      Mockito.lenient().when(mediaRepository.findAll()).thenReturn(emptyList);

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> mediaService.findAll());
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Media by his title, media type and ean")
   void findAllFiltered_returnOnlySameMedia_ofExistingTitleAndMediaTypeAndEAN() {
      List<MediaDTO> found;

      for(Media media:allMedias) {
         MediaDTO mediaDTO = mediaService.entityToDTO(media);
         MediaDTO filter = new MediaDTO();
         filter.setTitle(mediaDTO.getTitle());
         filter.setMediaType(mediaDTO.getMediaType());
         filter.setEan(mediaDTO.getEan());

         Mockito.lenient().when(mediaRepository.findAll(any(MediaSpecification.class))).thenReturn(Collections.singletonList(media));

         found = mediaService.findAllFiltered(filter);

         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(mediaService.entityToDTO(media));
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Media by EAN title and type")
   void getFirstId_returnFirstId_ofFilteredMediaByTitle() {
      MediaDTO filter = new MediaDTO();
      filter.setTitle("Les Misérables (Tome 1)");
      filter.setMediaType(MediaType.BOOK.name());

      Mockito.lenient().when(mediaRepository.findAll(any(MediaSpecification.class))).thenReturn(Arrays.asList(allMedias.get(13),allMedias.get(14),allMedias.get(15)));


      Integer mediaId = mediaService.getFirstId(filter);

      assertThat(mediaId).isEqualTo(14);
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Media")
   void save_returnCreatedMedia_ofNewMedia() {
      Mockito.lenient().when(mediaRepository.save(any(Media.class))).thenReturn(newMedia);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(Optional.of(newMedia));

      MediaDTO mediaDTO = mediaDTO = mediaService.save(newMediaDTO);
      Integer newId = mediaDTO.getId();

      assertThat(mediaService.existsById(newId)).isTrue();
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Media with has no EAN")
   void save_throwBadRequestException_ofNewMediaWithNoEAN() {
      newMediaDTO.setEan(null);
      Assertions.assertThrows(BadRequestException.class, ()-> mediaService.save(newMediaDTO));
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we have BadRequestException when saving a Media with has no MediaType")
   void save_throwBadRequestException_ofNewMediaWithNoMediaType() {
      newMediaDTO.setMediaType(null);
      Assertions.assertThrows(BadRequestException.class, ()-> mediaService.save(newMediaDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update a Media")
   void update_returnUpdatedMedia_ofMediaAndNewTitle() {
      String oldTitle = bookDTO.getTitle();
      bookDTO.setTitle(MEDIA_TITLE_TEST);

      Mockito.lenient().when(bookService.findById(anyString())).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(Optional.ofNullable(newMedia));
      Mockito.lenient().when(mediaRepository.save(any(Media.class))).thenReturn(newMedia);
      newMediaDTO.setTitle(MEDIA_TITLE_TEST);

      MediaDTO mediaSaved = mediaService.update(newMediaDTO);
      assertThat(mediaSaved).isEqualTo(newMediaDTO);

      bookDTO.setTitle(oldTitle);
      newMediaDTO.setTitle(oldTitle);
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Media with has no EAN")
   void update_throwConflictException_ofNewMediaWithNoEAN() {
      newMediaDTO.setEan(null);
      Assertions.assertThrows(ConflictException.class, ()-> mediaService.update(newMediaDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ConflictException when update a Media with has no MediaType")
   void update_throwConflictException_ofNewMediaWithNoMediaType() {
      newMediaDTO.setMediaType(null);
      Assertions.assertThrows(ConflictException.class, ()-> mediaService.update(newMediaDTO));
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we have ResourceNotFoundException when update a Media with wrong ID")
   void update_throwResourceNotFoundException_ofNewMediaWithWrongId() {
      newMediaDTO.setId(654);
      Assertions.assertThrows(ResourceNotFoundException.class, ()-> mediaService.update(newMediaDTO));
   }


   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Media by his ID")
   void deleteById_returnExceptionWhenGetMediaById_ofDeletedMediaById() {
      Integer mediaId = newMedia.getId();

      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(Optional.ofNullable(newMedia));

      mediaService.deleteById(mediaId);
      Mockito.lenient().when(mediaRepository.findById(mediaId)).thenThrow(ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()-> mediaService.findById(mediaId));
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we have ResourceNotFoundException when deleting a Media with bad ID")
   void deleteById_throwResourceNotFoundException_ofMediaWithBadId() {

      Assertions.assertThrows(ResourceNotFoundException.class, ()-> mediaService.deleteById(123465));
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Medias")
   void count_returnTheNumberOfMedias() {
      Mockito.lenient().when(mediaRepository.count()).thenReturn(31L);
      assertThat(mediaService.count()).isEqualTo(31);
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Media DTO is converted in right Media Entity")
   void dtoToEntity_returnRightMediaEntity_ofMediaDTO() {
      Media entity = mediaService.dtoToEntity(newMediaDTO);

      assertThat(entity.getId()).isEqualTo(newMediaDTO.getId());
      assertThat(entity.getEan()).isEqualTo(newMediaDTO.getEan());
      assertThat(entity.getMediaType().name()).isEqualTo(newMediaDTO.getMediaType());
      assertThat(entity.getStatus().name()).isEqualTo(newMediaDTO.getStatus());
      assertThat(entity.getReturnDate()).isEqualTo(newMediaDTO.getReturnDate());
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Media Entity is converted in right Media DTO")
   void dtoToEntity_returnRightMediaDTO_ofMediaEntity() {
      MediaDTO dto = mediaService.entityToDTO(newMedia);

      assertThat(dto.getId()).isEqualTo(newMedia.getId());
      assertThat(dto.getEan()).isEqualTo(newMedia.getEan());
      assertThat(dto.getMediaType()).isEqualTo(newMedia.getMediaType().name());
      assertThat(dto.getStatus()).isEqualTo(newMedia.getStatus().name());
      assertThat(dto.getReturnDate()).isEqualTo(newMedia.getReturnDate());
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Book by his EAN number")
   void increaseStock_returnBookWithIncrementedStock_ofOneBook() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();

      bookDTO.setStock(oldStock+1);
      Mockito.lenient().when(bookService.findById(anyString())).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.findById(mediaId)).thenReturn(java.util.Optional.ofNullable(newMedia));

      mediaService.increaseStock(newMediaDTO);
      MediaDTO mediaDTO = mediaService.findById(mediaId);

      assertThat(mediaDTO.getStock()).isEqualTo(oldStock+1);

      bookDTO.setStock(oldStock);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Music by his EAN number")
   void increaseStock_returnMusicWithIncrementedStock_ofOneMusic() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();
      MediaType oldMediaType = newMedia.getMediaType();
      newMedia.setMediaType(MediaType.MUSIC);

      musicDTO.setStock(oldStock+1);
      Mockito.lenient().when(musicService.findById(anyString())).thenReturn(musicDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(java.util.Optional.of(newMedia));

      mediaService.increaseStock(newMediaDTO);
      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found.getStock()).isEqualTo(oldStock+1);

      musicDTO.setStock(oldStock);
      newMedia.setMediaType(oldMediaType);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Game by his EAN number")
   void increaseStock_returnGameWithIncrementedStock_ofOneGame() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();
      MediaType oldMediaType = newMedia.getMediaType();
      newMedia.setMediaType(MediaType.GAME);

      gameDTO.setStock(oldStock+1);
      Mockito.lenient().when(gameService.findById(anyString())).thenReturn(gameDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(java.util.Optional.of(newMedia));

      mediaService.increaseStock(newMediaDTO);
      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found.getStock()).isEqualTo(oldStock+1);

      gameDTO.setStock(oldStock);
      newMedia.setMediaType(oldMediaType);
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Video by his EAN number")
   void increaseStock_returnVideoWithIncrementedStock_ofOneVideo() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();
      MediaType oldMediaType = newMedia.getMediaType();
      newMedia.setMediaType(MediaType.VIDEO);

      videoDTO.setStock(oldStock+1);

      Mockito.lenient().when(videoService.findById(anyString())).thenReturn(videoDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(java.util.Optional.of(newMedia));

      mediaService.increaseStock(newMediaDTO);
      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found.getStock()).isEqualTo(oldStock+1);

      videoDTO.setStock(oldStock);
      newMedia.setMediaType(oldMediaType);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Book by his EAN number")
   void decreaseStock_returnBookWithDecrementedStock_ofOneBook() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();

      bookDTO.setStock(oldStock-1);
      Mockito.lenient().when(bookService.findById(anyString())).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.findById(mediaId)).thenReturn(java.util.Optional.ofNullable(newMedia));

      mediaService.decreaseStock(newMediaDTO);
      MediaDTO mediaDTO = mediaService.findById(mediaId);

      assertThat(mediaDTO.getStock()).isEqualTo(oldStock-1);

      bookDTO.setStock(oldStock);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can increase the stock of a Game by his EAN number")
   void decreaseStock_returnGameWithDecrementedStock_ofOneGame() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();
      MediaType oldMediaType = newMedia.getMediaType();
      newMedia.setMediaType(MediaType.GAME);

      gameDTO.setStock(oldStock-1);
      Mockito.lenient().when(gameService.findById(anyString())).thenReturn(gameDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(java.util.Optional.of(newMedia));

      mediaService.decreaseStock(newMediaDTO);
      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found.getStock()).isEqualTo(oldStock-1);

      gameDTO.setStock(oldStock);
      newMedia.setMediaType(oldMediaType);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Music by his EAN number")
   void decreaseStock_returnMusicWithDecrementedStock_ofOneMusic() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();
      MediaType oldMediaType = newMedia.getMediaType();
      newMedia.setMediaType(MediaType.MUSIC);

      musicDTO.setStock(oldStock-1);
      Mockito.lenient().when(musicService.findById(anyString())).thenReturn(musicDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(java.util.Optional.of(newMedia));

      mediaService.decreaseStock(newMediaDTO);
      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found.getStock()).isEqualTo(oldStock-1);

      musicDTO.setStock(oldStock);
      newMedia.setMediaType(oldMediaType);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can increase the stock of a Video by his EAN number")
   void decreaseStock_returnMusicVideoDecrementedStock_ofOneVideo() {
      Integer mediaId = newMediaDTO.getId();
      Integer oldStock = newMediaDTO.getStock();
      MediaType oldMediaType = newMedia.getMediaType();
      newMedia.setMediaType(MediaType.VIDEO);

      videoDTO.setStock(oldStock-1);
      Mockito.lenient().when(videoService.findById(anyString())).thenReturn(videoDTO);
      Mockito.lenient().when(mediaRepository.findById(anyInt())).thenReturn(java.util.Optional.of(newMedia));

      mediaService.decreaseStock(newMediaDTO);
      MediaDTO found = mediaService.findById(mediaId);

      assertThat(found.getStock()).isEqualTo(oldStock-1);

      videoDTO.setStock(oldStock);
      newMedia.setMediaType(oldMediaType);
   }

   @Test
   @Tag("findMediaTypeByEan")
   @DisplayName("Give the media type by his EAN code")
   void findMediaTypeByEan_returnMediaType_ofMediaByEAN() {
      Mockito.lenient().when(mediaRepository.findMediaTypeByEan("BOOK_EAN")).thenReturn(MediaType.BOOK.name());
      Mockito.lenient().when(mediaRepository.findMediaTypeByEan("GAME_EAN")).thenReturn(MediaType.GAME.name());
      Mockito.lenient().when(mediaRepository.findMediaTypeByEan("MUSIC_EAN")).thenReturn(MediaType.MUSIC.name());
      Mockito.lenient().when(mediaRepository.findMediaTypeByEan("VIDEO_EAN")).thenReturn(MediaType.VIDEO.name());

      assertThat(mediaService.findMediaTypeByEan("BOOK_EAN")).isEqualTo(MediaType.BOOK);
      assertThat(mediaService.findMediaTypeByEan("GAME_EAN")).isEqualTo(MediaType.GAME);
      assertThat(mediaService.findMediaTypeByEan("MUSIC_EAN")).isEqualTo(MediaType.MUSIC);
      assertThat(mediaService.findMediaTypeByEan("VIDEO_EAN")).isEqualTo(MediaType.VIDEO);
   }

   @Test
   @Tag("findBlockedByEan")
   @DisplayName("Verify that we can find the Media BLOCKED with his EAN")
   void findBlockedByEan_returnBlockedMedia_ofMediaEANBlocked() {
      MediaStatus oldStatus = newMedia.getStatus();
      newMedia.setStatus(MediaStatus.BLOCKED);
      newMediaDTO.setStatus(MediaStatus.BLOCKED.name());
      Mockito.lenient().when(mediaRepository.findBlockedByEan(anyString())).thenReturn(newMedia);

      mediaService.setStatus(newMediaDTO.getId(),MediaStatus.BLOCKED);

      MediaDTO found = mediaService.findBlockedByEan(newMediaDTO.getEan());
      assertThat(found).isEqualTo(newMediaDTO);

      newMedia.setStatus(oldStatus);
      newMediaDTO.setStatus(oldStatus.name());
   }

   @Test
   @Tag("findBoockedByEan")
   @DisplayName("Verify that we can find the Media BOOKED with his EAN")
   void findBoockedByEan_returnBookedMedia_ofMediaEANBooked() {
      MediaStatus oldStatus = newMedia.getStatus();
      newMedia.setStatus(MediaStatus.BOOKED);
      newMediaDTO.setStatus(MediaStatus.BOOKED.name());
      Mockito.lenient().when(mediaRepository.findBoockedByEan(anyString())).thenReturn(newMedia);

      mediaService.setStatus(newMediaDTO.getId(),MediaStatus.BOOKED);

      MediaDTO found = mediaService.findBoockedByEan(newMediaDTO.getEan());
      assertThat(found).isEqualTo(newMediaDTO);

      newMedia.setStatus(oldStatus);
      newMediaDTO.setStatus(oldStatus.name());
   }

   @Test
   @Tag("blockFreeByEan")
   @DisplayName("Verify that we call right method to BLOCK a FREE Media")
   void blockFreeByEan_setBLOCKED_ofFREEMedia() {
      String ean = "kljh2253002864";
      ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);

      mediaService.blockFreeByEan(ean);
      verify(mediaRepository).blockFreeByEan(argument.capture());

      assertThat(ean).isEqualTo(argument.getValue());
   }

   @Test
   @Tag("blockFreeByEan")
   @DisplayName("Verify that we don't BLOCK a not FREE Media")
   void blockFreeByEan_statusNotChanged_ofNotFREEMedia() {
      String ean = "978-2253002864";
      Mockito.lenient().when(mediaRepository.findBlockedByEan(anyString())).thenThrow(com.pedsf.library.exception.ResourceNotFoundException.class);
      mediaService.blockFreeByEan(ean);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()->mediaService.findBlockedByEan(ean));
   }



   @Test
   @Tag("bookedFreeByEan")
   @DisplayName("Verify that we call right method to BOOK a FREE Media")
   void bookedFreeByEan_setBOOKED_ofFREEMedia() {
      String ean = "kljh2253002864";
      ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);

      mediaService.bookedFreeByEan(ean);

      verify(mediaRepository).bookedFreeByEan(argument.capture());
      assertThat(ean).isEqualTo(argument.getValue());
   }

   @Test
   @Tag("bookedFreeByEan")
   @DisplayName("Verify tha we don't BOOK a not FREE Media")
   void bookedFreeByEan_statusNotChanged_ofNotFREEMedia() {
      String ean = "kljh2253002864";

      mediaService.bookedFreeByEan(ean);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class,
            ()->mediaService.findBlockedByEan(ean));
   }

   @Test
   @Tag("release")
   @DisplayName("Verify hat we can reinitialise the status and the returnDate of a Media")
   void release_setFREEAndReturnDateNull_ofMediaByID() {
      Integer mediaId = 45;
      ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);

      mediaService.release(mediaId);
      verify(mediaRepository).release(argument.capture());

      assertThat(mediaId).isEqualTo(argument.getValue());
   }

   @Test
   @Tag("updateReturnDate")
   @DisplayName("Verify tha we can set the new returnDate")
   void updateReturnDate_returnMediaWithNewDate_ofMediaAndDate() {
      Date newDate = new Date(Calendar.getInstance().getTimeInMillis());
      Integer mediaId = 45;
      ArgumentCaptor<Integer> integerArgument = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<Date> dateArgument = ArgumentCaptor.forClass(Date.class);

      mediaService.updateReturnDate(mediaId, newDate);
      verify(mediaRepository).updateReturnDate(integerArgument.capture(),dateArgument.capture());

      assertThat(mediaId).isEqualTo(integerArgument.getValue());
      assertThat(newDate).isEqualTo(dateArgument.getValue());
   }

   @Test
   @Tag("getNextReturnByEan")
   @DisplayName("Verify that we can get the next return Media by EAN")
   void getNextReturnByEan() {
      Mockito.lenient().when(mediaRepository.getNextReturnByEan(anyString())).thenReturn(newMedia);

      MediaDTO found = mediaService.getNextReturnByEan("978-2253004226");

      assertThat(found).isEqualTo(newMediaDTO);
   }

   @Test
   @Tag("getNextReturnByEan")
   @DisplayName("Verify that we return null if a Media of this AEN is available")
   void getNextReturnByEan_returnNull_ofAvailableMediaEAN() {
      Date oldMediaDate = newMedia.getReturnDate();
      Date oldBookDate = newMedia.getReturnDate();
      MediaStatus oldMediaStatus = newMedia.getStatus();
      newMedia.setReturnDate(null);
      newMedia.setStatus(MediaStatus.FREE);
      bookDTO.setReturnDate(null);

      Mockito.lenient().when(bookService.findById(anyString())).thenReturn(bookDTO);
      Mockito.lenient().when(mediaRepository.getNextReturnByEan(anyString())).thenReturn(newMedia);

      MediaDTO found = mediaService.getNextReturnByEan("978-2253010692");

      assertThat(found.getReturnDate()).isNull();
      newMedia.setReturnDate(oldMediaDate);
      newMedia.setStatus(oldMediaStatus);
      bookDTO.setReturnDate(oldBookDate);
   }


   @Test
   @Tag("setStatus")
   @DisplayName("Verify that we can change the MediaStatus")
   void setStatus_setStatus_ofMediaAndMediaStatus() {
      Integer mediaId = 55;
      MediaStatus newStatus = MediaStatus.BLOCKED;
      ArgumentCaptor<Integer> integerArgument = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<String> stringArgument = ArgumentCaptor.forClass(String.class);

      mediaService.setStatus(mediaId,newStatus);
      verify(mediaRepository).setStatus(integerArgument.capture(),stringArgument.capture());

      assertThat(mediaId).isEqualTo(integerArgument.getValue());
      assertThat(newStatus.name()).isEqualTo(stringArgument.getValue());

   }
}