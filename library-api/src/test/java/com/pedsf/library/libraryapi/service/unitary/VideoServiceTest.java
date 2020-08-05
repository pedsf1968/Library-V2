package com.pedsf.library.libraryapi.service.unitary;

import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.dto.business.VideoDTO;
import com.pedsf.library.exception.ResourceNotFoundException;
import com.pedsf.library.libraryapi.model.Person;
import com.pedsf.library.libraryapi.model.Video;
import com.pedsf.library.libraryapi.model.VideoFormat;
import com.pedsf.library.libraryapi.model.VideoType;
import com.pedsf.library.libraryapi.repository.VideoRepository;
import com.pedsf.library.libraryapi.repository.VideoSpecification;
import com.pedsf.library.libraryapi.service.PersonService;
import com.pedsf.library.libraryapi.service.VideoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
class VideoServiceTest {
   private static final String VIDEO_TITLE_TEST = "New Video Title";
   private static final List<PersonDTO> allPersons = new ArrayList<>();
   private static final List<Video> allVideos = new ArrayList<>();
   private static final List<Video> allAllowedVideos = new ArrayList<>();

   @Mock
   private PersonService personService;
   @Mock
   private VideoRepository videoRepository;
   private static VideoService videoService;
   private static Video newVideo;
   private static VideoDTO newVideoDTO;

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

      allVideos.add(new Video("3475001058980","Parasite",3,2,4));
      allVideos.add(new Video("6546546878980","Germinal",1,-2,1));
      allVideos.add(new Video("2224601058980","El tapioca",3,2,1));
      allVideos.add(new Video("3472089898980","Mort aux Trousses",1,1,3));
      allVideos.add(new Video("8885466546580","Banzai",2,-4,3));
      allVideos.add(new Video("3476546046540","Le secret",3,-2,4));

      allAllowedVideos.add(new Video("3475001058980","Parasite",3,2,4));
      allAllowedVideos.add(new Video("2224601058980","El tapioca",3,2,1));
      allAllowedVideos.add(new Video("3472089898980","Mort aux Trousses",1,1,3));
      allAllowedVideos.add(new Video("3476546046540","Le secret",3,-2,4));

   }

   @BeforeEach
   void beforeEach() {
      videoService = new VideoService(videoRepository,personService);

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
      newVideo.setActors(new HashSet<>());

      newVideoDTO = new VideoDTO("sdfsdfds","Video of the Day", 1,1,allPersons.get(14));
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
      newVideoDTO.setActors(new ArrayList<>());

      Mockito.lenient().when(videoRepository.findAll()).thenReturn(allVideos);
      Mockito.lenient().when(videoRepository.findAllAllowed()).thenReturn(allAllowedVideos);
      Mockito.lenient().when(videoRepository.findByEan("3475001058980")).thenReturn(java.util.Optional.ofNullable(allVideos.get(0)));
      Mockito.lenient().when(videoRepository.findByEan("6546546878980")).thenReturn(java.util.Optional.ofNullable(allVideos.get(1)));
      Mockito.lenient().when(videoRepository.findByEan("2224601058980")).thenReturn(java.util.Optional.ofNullable(allVideos.get(2)));
      Mockito.lenient().when(videoRepository.findByEan("3472089898980")).thenReturn(java.util.Optional.ofNullable(allVideos.get(3)));
      Mockito.lenient().when(videoRepository.findByEan("8885466546580")).thenReturn(java.util.Optional.ofNullable(allVideos.get(4)));
      Mockito.lenient().when(videoRepository.findByEan("3476546046540")).thenReturn(java.util.Optional.ofNullable(allVideos.get(5)));
      Mockito.lenient().when(videoRepository.findByEan("sdfsdfds")).thenReturn(java.util.Optional.ofNullable(newVideo));

      ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
      Mockito.lenient().when(personService.findById(anyInt())).thenAnswer(
            (InvocationOnMock invocation) -> allPersons.get((Integer) invocation.getArguments()[0]-1));

   }

   @Test
   @DisplayName("Verify that return TRUE if the Video exist")
   void existsById_returnTrue_OfAnExistingVideoId() {
      for(Video video : allVideos) {
         String ean = video.getEan();
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
      VideoDTO found;

      for(Video video : allVideos) {
         String ean = video.getEan();
         found = videoService.findById(ean);

         assertThat(found).isEqualTo(videoService.entityToDTO(video));
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

      assertThat(videoDTOS.size()).isEqualTo(6);

      for(Video video: allVideos) {
         VideoDTO videoDTO = videoService.entityToDTO(video);
         assertThat(videoDTOS.contains(videoDTO)).isTrue();
      }
   }

   @Test
   @Tag("findAllAllowed")
   @DisplayName("Verify that we got the list of Videos that can be booked")
   @Disabled
   void findAllAllowed_returnBookableVideos_ofAllVideos() {
      List<VideoDTO> alloweds = videoService.findAllAllowed();
      assertThat(alloweds.size()).isEqualTo(4);

      for(Video video: allVideos) {
         VideoDTO videoDTO = videoService.entityToDTO(video);

         if (containVideo(alloweds,videoDTO)) {
            // allowed
            assertThat(videoDTO.getStock()).isGreaterThan(-videoDTO.getQuantity()*2);
         } else {
            // not allowed
            assertThat(videoDTO.getStock()).isLessThanOrEqualTo(-videoDTO.getQuantity()*2);
         }
      }
   }

   boolean containVideo(List<VideoDTO> videoDTOList, VideoDTO videoDTO) {
      boolean result = false;

      for (VideoDTO v : videoDTOList) {
         if(v.equals(videoDTO)) {
            return true;
         }
      }
      return result;
   }

   @Test
   @Tag("findAllFiltered")
   @DisplayName("Verify that we can find one Video by his title and director")
   void findAllFiltered_returnOnlyOneVideo_ofExistingTitleAndDirector() {
      List<VideoDTO> found;

      for(Video video:allVideos) {
         Video filter = new Video();
         filter.setTitle(video.getTitle());
         filter.setDirectorId(video.getDirectorId());

         Mockito.lenient().when(videoRepository.findAll(any(VideoSpecification.class))).thenReturn(Arrays.asList(video));

         VideoDTO filterDTO = videoService.entityToDTO(filter);
         found = videoService.findAllFiltered(filterDTO);

         assertThat(found.size()).isEqualTo(1);
         assertThat(found.get(0)).isEqualTo(videoService.entityToDTO(video));
      }
   }

   @Test
   @Tag("getFirstId")
   @DisplayName("Verify that we get the first ID of a list of filtered Video by Director")
   void getFirstId_returnFirstId_ofFilteredVideoByAuthor() {
      VideoDTO filter = new VideoDTO();
      filter.setDirector(personService.findById(3));

      Mockito.lenient().when(videoRepository.findAll(any(VideoSpecification.class))).thenReturn(Arrays.asList(allVideos.get(3),allVideos.get(4)));

      String ean = videoService.getFirstId(filter);

      assertThat(ean).isEqualTo("3472089898980");
   }

   @Test
   @Tag("save")
   @DisplayName("Verify that we can create a new Video")
   void save_returnCreatedVideo_ofNewVideo() {
      Mockito.lenient().when(videoRepository.save(any(Video.class))).thenReturn(newVideo);

      VideoDTO found = videoService.save(newVideoDTO);
      String ean = found.getEan();

      assertThat(videoService.existsById(ean)).isTrue();
   }

   @Test
   @Tag("update")
   @DisplayName("Verify that we can update an Video")
   void update_returnUpdatedVideo_ofVideoAndNewTitle() {
      String oldTitle = newVideo.getTitle();
      newVideo.setTitle(VIDEO_TITLE_TEST);
      newVideoDTO.setTitle(VIDEO_TITLE_TEST);
      Mockito.lenient().when(videoRepository.save(any(Video.class))).thenReturn(newVideo);

      VideoDTO videoSaved = videoService.update(newVideoDTO);
      assertThat(videoSaved).isEqualTo(newVideoDTO);

      newVideo.setTitle(oldTitle);
      newVideoDTO.setTitle(oldTitle);
   }

   @Test
   @Tag("deleteById")
   @DisplayName("Verify that we can delete a Video by his EAN")
   void deleteById_returnExceptionWhenGetUserById_ofDeletedUserById() {
      String ean = newVideoDTO.getEan();

      assertThat(videoService.existsById(ean)).isTrue();
      videoService.deleteById(ean);
      Mockito.lenient().when(videoRepository.findByEan(ean)).thenThrow(ResourceNotFoundException.class);

      Assertions.assertThrows(com.pedsf.library.exception.ResourceNotFoundException.class, ()-> {
         videoService.findById(ean);
      });
   }

   @Test
   @Tag("count")
   @DisplayName("Verify that we have the right number of Videos")
   void count_returnTheNumberOfVideos() {
      Mockito.lenient().when(videoRepository.count()).thenReturn(5l);
      assertThat(videoService.count()).isEqualTo(5);
   }


   @Test
   @Tag("dtoToEntity")
   @DisplayName("Verify that Video DTO is converted in right Video Entity")
   void dtoToEntity_returnRightVideoEntity_ofVideoDTO() {
      Video entity = videoService.dtoToEntity(newVideoDTO);
      assertThat(entity.getEan()).isEqualTo(newVideoDTO.getEan());
      assertThat(entity.getTitle()).isEqualTo(newVideoDTO.getTitle());
      assertThat(entity.getQuantity()).isEqualTo(newVideoDTO.getQuantity());
      assertThat(entity.getStock()).isEqualTo(newVideoDTO.getStock());
      assertThat(entity.getHeight()).isEqualTo(newVideoDTO.getHeight());
      assertThat(entity.getLength()).isEqualTo(newVideoDTO.getLength());
      assertThat(entity.getWeight()).isEqualTo(newVideoDTO.getWeight());
      assertThat(entity.getWidth()).isEqualTo(newVideoDTO.getWidth());
      assertThat(entity.getReturnDate()).isEqualTo(newVideoDTO.getReturnDate());

      assertThat(entity.getDirectorId()).isEqualTo(newVideoDTO.getDirector().getId());
      assertThat(entity.getDuration()).isEqualTo(newVideoDTO.getDuration());
      assertThat(entity.getImage()).isEqualTo(newVideoDTO.getImage());
      assertThat(entity.getAudience()).isEqualTo(newVideoDTO.getAudience());
      assertThat(entity.getAudio()).isEqualTo(newVideoDTO.getAudio());
      assertThat(entity.getUrl()).isEqualTo(newVideoDTO.getUrl());
      assertThat(entity.getPublicationDate()).isEqualTo(newVideoDTO.getPublicationDate());
      assertThat(entity.getFormat().name()).isEqualTo(newVideoDTO.getFormat());
      assertThat(entity.getType().name()).isEqualTo(newVideoDTO.getType());
      assertThat(entity.getSummary()).isEqualTo(newVideoDTO.getSummary());
   }

   @Test
   @Tag("entityToDTO")
   @DisplayName("Verify that Video Entity is converted in right Video DTO")
   void dtoToEntity_returnRightBookDTO_ofBookEntity() {
      VideoDTO dto = videoService.entityToDTO(newVideo);
      assertThat(dto.getEan()).isEqualTo(newVideo.getEan());
      assertThat(dto.getTitle()).isEqualTo(newVideo.getTitle());
      assertThat(dto.getQuantity()).isEqualTo(newVideo.getQuantity());
      assertThat(dto.getStock()).isEqualTo(newVideo.getStock());
      assertThat(dto.getHeight()).isEqualTo(newVideo.getHeight());
      assertThat(dto.getLength()).isEqualTo(newVideo.getLength());
      assertThat(dto.getWeight()).isEqualTo(newVideo.getWeight());
      assertThat(dto.getWidth()).isEqualTo(newVideo.getWidth());
      assertThat(dto.getReturnDate()).isEqualTo(newVideo.getReturnDate());

      assertThat(dto.getDirector().getId()).isEqualTo(newVideo.getDirectorId());
      assertThat(dto.getDuration()).isEqualTo(newVideo.getDuration());
      assertThat(dto.getImage()).isEqualTo(newVideo.getImage());
      assertThat(dto.getAudience()).isEqualTo(newVideo.getAudience());
      assertThat(dto.getAudio()).isEqualTo(newVideo.getAudio());
      assertThat(dto.getUrl()).isEqualTo(newVideo.getUrl());
      assertThat(dto.getPublicationDate()).isEqualTo(newVideo.getPublicationDate());
      assertThat(dto.getFormat()).isEqualTo(newVideo.getFormat().name());
      assertThat(dto.getType()).isEqualTo(newVideo.getType().name());
      assertThat(dto.getSummary()).isEqualTo(newVideo.getSummary());
   }


   @Test
   @Tag("findAllDirectors")
   @DisplayName("Verify that we get all Videos Director")
   void findAllDirectors() {
      Mockito.lenient().when(videoRepository.findAllDirectors()).thenReturn(Arrays.asList(1,3,4));
      List<PersonDTO> personDTOS = videoService.findAllDirectors();
      assertThat(personDTOS.size()).isEqualTo(3);
      assertThat(personDTOS.contains(allPersons.get(0))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(2))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(3))).isTrue();
   }

   @Test
   @Tag("findAllActors")
   @DisplayName("Verify that we get all Videos actors")
   void findAllActors() {
      Mockito.lenient().when(videoRepository.findAllActors()).thenReturn(Arrays.asList(5,6,7,8,9));
      List<PersonDTO> personDTOS = videoService.findAllActors();
      assertThat(personDTOS.size()).isEqualTo(5);
      assertThat(personDTOS.contains(allPersons.get(4))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(5))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(6))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(7))).isTrue();
      assertThat(personDTOS.contains(allPersons.get(8))).isTrue();
   }

   @Test
   @Tag("findAllTitles")
   @DisplayName("Verify that we get all Videos titles")
   void findAllTitles() {
      List<String> titles = Arrays.asList("Parasite","Germinal","El tapioca","Mort aux Trousses","Banzai","Le secret");
      Mockito.lenient().when(videoRepository.findAllTitles()).thenReturn(titles);

      List<String> foundTitles = videoService.findAllTitles();
      assertThat(foundTitles.size()).isEqualTo(6);
      for(String title:foundTitles) {
         assertThat(titles.contains(title)).isTrue();
      }
   }

   @Test
   @Tag("increaseStock")
   @DisplayName("Verify that we can increase the stock of a Video by his EAN number")
   void increaseStock_returnVideoWithIncrementedStock_ofOneVideo() {
      String ean = newVideo.getEan();
      Integer oldStock = newVideo.getStock();
      newVideo.setStock(oldStock+1);
      Mockito.lenient().when(videoRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newVideo));

      videoService.increaseStock(ean);
      VideoDTO videoDTO = videoService.findById(ean);
      assertThat(videoDTO.getStock()).isEqualTo(oldStock+1);

      newVideo.setStock(oldStock);
   }

   @Test
   @Tag("decreaseStock")
   @DisplayName("Verify that we can decrease the stock of a Video by his EAN number")
   void decreaseStock_returnVideoWithDecrementedStock_ofOneVideo() {
      String ean = newVideo.getEan();
      Integer oldStock = newVideo.getStock();
      newVideo.setStock(oldStock-1);
      Mockito.lenient().when(videoRepository.findByEan(ean)).thenReturn(java.util.Optional.ofNullable(newVideo));

      videoService.decreaseStock(ean);
      VideoDTO videoDTO = videoService.findById(ean);
      assertThat(videoDTO.getStock()).isEqualTo(oldStock-1);

      newVideo.setStock(oldStock);
   }
}