package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.*;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.Media;
import com.pedsf.library.libraryapi.model.MediaStatus;
import com.pedsf.library.libraryapi.repository.MediaRepository;
import com.pedsf.library.libraryapi.repository.MediaSpecification;
import com.pedsf.library.model.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("MediaService")
public class MediaService implements GenericService<MediaDTO, Media,Integer> {
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Media with the id : ";
   private static final String CANNOT_FIND_WITH_EAN = "Cannot find Media with the EAN : ";
   private static final String CANNOT_SAVE ="Failed to save Media";

   private final MediaRepository mediaRepository;
   private final BookService bookService;
   private final GameService gameService;
   private final MusicService musicService;
   private final VideoService videoService;

   private final ModelMapper modelMapper = new ModelMapper();

   public MediaService(MediaRepository mediaRepository, BookService bookService, GameService gameService, MusicService musicService, VideoService videoService) {
      this.mediaRepository = mediaRepository;
      this.bookService = bookService;
      this.gameService = gameService;
      this.musicService = musicService;
      this.videoService = videoService;
   }


   @Override
   public boolean existsById(Integer id) {
      Optional<Media> media = mediaRepository.findById(id);

      return media.isPresent();
   }

   @Override
   public MediaDTO findById(Integer id) {
      MediaDTO mediaDTO;
      Optional<Media> media = mediaRepository.findById(id);

      if (media.isPresent()) {
         mediaDTO = entityToDTO(media.get());
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      }

      return initialise(mediaDTO);
   }

   public MediaDTO initialise(MediaDTO mediaDTO) {
      if(mediaDTO.getMediaType().equals(MediaType.BOOK.name())) {
         BookDTO bookDTO = bookService.findById(mediaDTO.getEan());
         mediaDTO.initialise(bookDTO);
      } else if(mediaDTO.getMediaType().equals(MediaType.GAME.name())) {
         GameDTO gameDTO = gameService.findById(mediaDTO.getEan());
         mediaDTO.initialise(gameDTO);
      } else if(mediaDTO.getMediaType().equals(MediaType.MUSIC.name())) {
         MusicDTO musicDTO = musicService.findById(mediaDTO.getEan());
         mediaDTO.initialise(musicDTO);
      } else if(mediaDTO.getMediaType().equals(MediaType.VIDEO.name())) {
         VideoDTO videoDTO = videoService.findById(mediaDTO.getEan());
         mediaDTO.initialise(videoDTO);
      }
      return mediaDTO;
   }

   public MediaDTO findOneByEan(String ean) {
      MediaDTO mediaDTO;
      Media media = mediaRepository.findOneByEan(ean);

      if (media != null) {
         mediaDTO = entityToDTO(media);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_EAN + ean);
      }

      return initialise(mediaDTO);
   }

   public MediaDTO findFreeByEan(String ean) {
      MediaDTO mediaDTO;
      Media media = mediaRepository.findFreeByEan(ean);

      if (media != null) {
         mediaDTO = entityToDTO(media);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_EAN + ean);
      }
      return initialise(mediaDTO);
   }


   @Override
   public List<MediaDTO> findAll() {
      List<Media> medias = mediaRepository.findAll();
      List<MediaDTO> mediaDTOS = new ArrayList<>();

      for (Media media: medias){
         mediaDTOS.add(entityToDTO(media));
      }

      if (!mediaDTOS.isEmpty()) {
         return mediaDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<MediaDTO> findAllFiltered(MediaDTO filter) {
      Specification<Media> spec = new MediaSpecification(dtoToEntity(filter));
      List<Media> medias = mediaRepository.findAll(spec);
      List<MediaDTO> mediaDTOS = new ArrayList<>();

      for (Media media: medias){
         mediaDTOS.add(entityToDTO(media));
      }

      if (!mediaDTOS.isEmpty()) {
         return mediaDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public Integer getFirstId(MediaDTO filter) {
      return findAllFiltered(filter).get(0).getId();
   }

   @Override
   public MediaDTO save(MediaDTO mediaDTO) {
      if (  !StringUtils.isEmpty(mediaDTO.getId()) &&
            !StringUtils.isEmpty(mediaDTO.getEan()) &&
            !StringUtils.isEmpty(mediaDTO.getMediaType())) {

         try {
            Integer mediaId = getFirstId(mediaDTO);
            return findById(mediaId);

         } catch (ResourceNotFoundException ex) {
            mediaDTO.setId(null);
            return entityToDTO(mediaRepository.save(dtoToEntity(mediaDTO)));
         }

      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public MediaDTO update(MediaDTO mediaDTO) {
      if (  !StringUtils.isEmpty(mediaDTO.getId()) &&
            !StringUtils.isEmpty(mediaDTO.getEan()) &&
            !StringUtils.isEmpty(mediaDTO.getMediaType())) {


         if (!existsById(mediaDTO.getId())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + mediaDTO.getId());
         }

         Media media = mediaRepository.save(dtoToEntity(mediaDTO));
         return entityToDTO(media);

      } else {
         throw new ConflictException(CANNOT_SAVE);
      }
   }

   @Override
   public void deleteById(Integer id) {
      if (!existsById(id)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      } else {
         mediaRepository.deleteById(id);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(mediaRepository.count());
   }

   @Override
   public MediaDTO entityToDTO(Media media) {
      MediaDTO mediaDTO=  modelMapper.map(media, MediaDTO.class);

      return initialise(mediaDTO);
   }

   @Override
   public Media dtoToEntity(MediaDTO mediaDTO) {
      Media media = modelMapper.map(mediaDTO, Media.class);
      String type = mediaDTO.getMediaType();
      media.setMediaType(MediaType.valueOf(type));

      return media;
   }

   public void increaseStock(MediaDTO mediaDTO) {
      if (mediaDTO.getMediaType().equals(MediaType.BOOK.toString())) {
         bookService.increaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.GAME.toString())) {
         gameService.increaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.MUSIC.toString())) {
         musicService.increaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.VIDEO.toString())) {
         videoService.increaseStock(mediaDTO.getEan());
      }
   }

   public void decreaseStock(MediaDTO mediaDTO) {
      if (mediaDTO.getMediaType().equals(MediaType.BOOK.toString())) {
         bookService.decreaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.GAME.toString())) {
         gameService.decreaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.MUSIC.toString())) {
         musicService.decreaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.VIDEO.toString())) {
         videoService.decreaseStock(mediaDTO.getEan());
      }
   }


   /**
    * Method to find the type of a media (BOOK,GAME,MUSIC,VIDEO)
    *
    * @param ean EAN code of the media
    * @return MediaType of the media
    */
   public MediaType findMediaTypeByEan(String ean) {
      String type = mediaRepository.findMediaTypeByEan(ean);
      return MediaType.valueOf(type);
   }


   /**
    * Method to find BLOCKED media by is EAN code
    *
    * @param ean EAN code of the media
    * @return media BLOCKED
    */
   public MediaDTO findBlockedByEan(String ean) {
      MediaDTO mediaDTO;
      Media media = mediaRepository.findBlockedByEan(ean);

      if (media != null) {
         mediaDTO = entityToDTO(media);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_EAN + ean);
      }

      return initialise(mediaDTO);
   }

   /**
    * Method to find BOOKED media by is EAN code
    *
    * @param ean EAN code of the media
    * @return media BOOKED
    */
   public MediaDTO findBoockedByEan(String ean) {
      MediaDTO mediaDTO;
      Media media = mediaRepository.findBoockedByEan(ean);

      if (media != null) {
         mediaDTO = entityToDTO(media);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_EAN + ean);
      }

      return initialise(mediaDTO);
   }

   /**
    * Method to block a FREE media
    *
    * @param ean EAN code of the media
    */
   public void blockFreeByEan(String ean) {
      mediaRepository.blockFreeByEan(ean);
   }

   /**
    * Method to book a FREE media
    *
    * @param ean EAN code of the media
    */
   public void bookedFreeByEan(String ean) {
      mediaRepository.bookedFreeByEan(ean);
   }


   public void borrow(Integer mediaId, Date date) {
      java.sql.Date sDate = new java.sql.Date(date.getTime());
      mediaRepository.borrow(mediaId,sDate);
   }

   public void release(Integer mediaId) {
      mediaRepository.release(mediaId);
   }


   public void updateReturnDate(Integer mediaId, Date date) {
      java.sql.Date sDate = new java.sql.Date(date.getTime());
      mediaRepository.updateReturnDate(sDate, mediaId);
   }

   public Date getNextReturnDateByEan(String ean) {
      return mediaRepository.getNextReturnDateByEan(ean);
   }

   public MediaDTO getNextReturnByEan(String ean) {
      MediaDTO mediaDTO;

      mediaDTO = entityToDTO(mediaRepository.getNextReturnByEan(ean));

      return initialise(mediaDTO);
   }

   public void setStatus(Integer mediaId, MediaStatus status) {
      mediaRepository.setStatus(mediaId, status.name());
   }
}
