package com.library.libraryapi.service;

import com.library.libraryapi.dto.business.*;
import com.library.libraryapi.dto.global.UserDTO;
import com.library.libraryapi.exceptions.BadRequestException;
import com.library.libraryapi.exceptions.ConflictException;
import com.library.libraryapi.exceptions.ForbiddenException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.model.Borrowing;
import com.library.libraryapi.model.MediaType;
import com.library.libraryapi.model.UserStatus;
import com.library.libraryapi.proxy.UserApiProxy;
import com.library.libraryapi.repository.*;
import org.apache.commons.lang.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("BorrowingService")
public class BorrowingService implements GenericService<BorrowingDTO, Borrowing,Integer> {
   private static final String CANNOT_FIND_WITH_EAN = "Cannot find Borrowing with the EAN : ";
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Borrowing with the id : ";
   private static final String CANNOT_SAVE ="Failed to save Borrowing";
   private static final String EXCEPTION_NO_MEDIA ="The Media is borrowed !";
   private static final String EXCEPTION_FORBIDDEN ="The user is not authorized, subscription fees not updated !";
   private static final String EXCEPTION_BAN ="The user banned is not authorized !";
   private static final String EXCEPTION_NO_MORE ="The user can't borrow more Media !";

   @Value("${library.borrowing.delay}")
   private int daysOfDelay;
   @Value("${library.borrowing.extention.max}")
   private int maxExtention;
   @Value("${library.borrowing.quantity.max}")
   private int quantityMax;

   private final BorrowingRepository borrowingRepository;
   private final BookService bookService;
   private final GameService gameService;
   private final MusicService musicService;
   private final VideoService videoService;
   private final MediaService mediaService;
   private final UserApiProxy userApiProxy;
   private final ModelMapper modelMapper = new ModelMapper();

   public BorrowingService(BorrowingRepository borrowingRepository,
                           BookService bookService, GameService gameService,
                           MusicService musicService, VideoService videoService,
                           UserApiProxy userApiProxy, MediaService mediaService) {
      this.borrowingRepository = borrowingRepository;
      this.bookService = bookService;
      this.gameService = gameService;
      this.musicService = musicService;
      this.videoService = videoService;
      this.userApiProxy = userApiProxy;
      this.mediaService = mediaService;
   }


   @Override
   public boolean existsById(Integer id) {
      return borrowingRepository.findById(id).isPresent();
   }

   @Override
   public BorrowingDTO findById(Integer id) {
      if(existsById(id)) {
         Borrowing borrowing = borrowingRepository.findById(id).orElse(null);
         return entityToDTO(borrowing);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      }
   }

   @Override
   public List<BorrowingDTO> findAll() {
      List<Borrowing> borrowings = borrowingRepository.findAll();
      List<BorrowingDTO> borrowingDTOS = new ArrayList<>();

      for (Borrowing borrowing: borrowings){
         borrowingDTOS.add(entityToDTO(borrowing));
      }

      if (!borrowingDTOS.isEmpty()) {
         return borrowingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<BorrowingDTO> findAllFiltered(BorrowingDTO filter) {
      Specification<Borrowing> spec = new BorrowingSpecification(modelMapper.map(filter, Borrowing.class));
      List<Borrowing> borrowings = borrowingRepository.findAll(spec);
      List<BorrowingDTO> borrowingDTOS = new ArrayList<>();

      for (Borrowing borrowing: borrowings){
         borrowingDTOS.add(entityToDTO(borrowing));
      }

      if (!borrowingDTOS.isEmpty()) {
         return borrowingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public Integer getFirstId(BorrowingDTO filter) {
      return findAllFiltered(filter).get(0).getId();
   }

   @Override
   public BorrowingDTO save(BorrowingDTO borrowingDTO) {
      if (  !StringUtils.isEmpty(borrowingDTO.getUser()) &&
            !StringUtils.isEmpty(borrowingDTO.getMedia())) {

        borrowingDTO.setId(null);
        return entityToDTO(borrowingRepository.save(dtoToEntity(borrowingDTO)));
      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public BorrowingDTO update(BorrowingDTO borrowingDTO) {
      if (  !StringUtils.isEmpty(borrowingDTO.getId()) &&
            !StringUtils.isEmpty(borrowingDTO.getUser()) &&
            !StringUtils.isEmpty(borrowingDTO.getMedia())) {
         if (!existsById(borrowingDTO.getId())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + borrowingDTO.getId());
         }
         Borrowing borrowing = borrowingRepository.save(dtoToEntity(borrowingDTO));

         return entityToDTO(borrowing);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }
   }

   @Override
   public void deleteById(Integer id) {
      if (!existsById(id)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      } else {
         borrowingRepository.deleteById(id);
      }
   }

   @Override
   public Integer count() {
         return Math.toIntExact(borrowingRepository.count());
   }

   @Override
   public BorrowingDTO entityToDTO(Borrowing borrowing) {
      BorrowingDTO borrowingDTO = modelMapper.map(borrowing, BorrowingDTO.class);

      UserDTO userDTO = userApiProxy.findUserById( borrowing.getUserId());
      MediaDTO mediaDTO = mediaService.findById(borrowing.getMediaId());

      borrowingDTO.setUser(userDTO);
      borrowingDTO.setMedia(mediaDTO);

      return borrowingDTO;
   }

   @Override
   public Borrowing dtoToEntity(BorrowingDTO borrowingDTO) {
      Borrowing borrowing = modelMapper.map(borrowingDTO,Borrowing.class);

      borrowing.setUserId(borrowingDTO.getUser().getId());
      borrowing.setMediaId(borrowingDTO.getMedia().getId());

      return borrowing;
   }


   Boolean userHadBorrowed(Integer userId, String ean) {
      return borrowingRepository.userHadBorrowed( userId, ean);
   }

   /**
    * method for borowing a media
    *
    * @param userId id of the User who want to borrow
    * @param mediaEan id of the Media wanted
    * @return the Borrowing DTO
    */
   public BorrowingDTO borrow(Integer userId, String mediaEan) {
      Borrowing borrowing = new Borrowing();
      UserDTO userDTO = userApiProxy.findUserById(userId);
      MediaDTO mediaDTO = null;
      Integer stock;
      // calculate the restitution date adding 4 weeks 28 days
      Date today = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(today);
      calendar.add(Calendar.DATE, daysOfDelay);
      java.sql.Date returnDate = new java.sql.Date(calendar.getTimeInMillis());

      // verify that the user can borrow
      int counter = userDTO.getCounter();
      if(counter>quantityMax){
         throw new ForbiddenException(EXCEPTION_NO_MORE);
      }

      String userStatus = userDTO.getStatus();
      if(userStatus.equals(UserStatus.FORBIDDEN.name()) ){
         throw new ForbiddenException(EXCEPTION_FORBIDDEN);
      } else if (userStatus.equals(UserStatus.BAN.name())){
         throw new ForbiddenException(EXCEPTION_BAN);
      }

      // block a media
      mediaService.blockFreeByEan(mediaEan);
      // get the media blocked
      mediaDTO = mediaService.findBlockedByEan(mediaEan);

      if(mediaDTO == null) {
         throw new ForbiddenException(EXCEPTION_NO_MEDIA);
      }

      if(mediaDTO.getMediaType().equals(MediaType.BOOK.toString())) {
         // Media is a BOOK
         BookDTO bookDTO = bookService.findById(mediaDTO.getEan());
         stock = bookDTO.getStock();
         //verify the stock
         if(stock > 0) {
            bookService.decreaseStock(mediaEan);
         } else {
            throw new ForbiddenException(EXCEPTION_NO_MEDIA);
         }

      } else if(mediaDTO.getMediaType().equals(MediaType.GAME.toString())) {
         // Media is a GAME
         GameDTO gameDTO = gameService.findById(mediaDTO.getEan());
         stock = gameDTO.getStock();
         //verify the stock
         if(stock > 0) {
            gameService.decreaseStock(mediaEan);
         } else {
            throw new ForbiddenException(EXCEPTION_NO_MEDIA);
         }
      } else if(mediaDTO.getMediaType().equals(MediaType.MUSIC.toString())) {
         // Media is a MUSIC
         MusicDTO musicDTO = musicService.findById(mediaDTO.getEan());
         stock = musicDTO.getStock();
         //verify the stock
         if(stock > 0) {
            musicService.decreaseStock(mediaEan);
         } else {
            throw new ForbiddenException(EXCEPTION_NO_MEDIA);
         }
      } else if(mediaDTO.getMediaType().equals(MediaType.VIDEO.toString())) {
         // Media is a VIDEO
         VideoDTO videoDTO = videoService.findById(mediaDTO.getEan());
         stock = videoDTO.getStock();
         //verify the stock
         if(stock > 0) {
            videoService.decreaseStock(mediaEan);
         } else {
            throw new ForbiddenException(EXCEPTION_NO_MEDIA);
         }
      }

      // record the borrowing in media table
      mediaService.borrow(mediaDTO.getId(),returnDate);

      if(userDTO.getStatus().equals("MEMBER")) {
         userApiProxy.updateUserStatus(userId,UserStatus.BORROWER.name());
      }

      // increase user borrowing counter
      userApiProxy.updateUserCounter(userId,++counter);

      borrowing.setMediaId(mediaDTO.getId());
      borrowing.setUserId(userId);
      borrowing.setBorrowingDate(today);
      borrowing.setExtended(0);
      borrowing.setReturnDate(returnDate);

      // record the borrowing in borrowing table
      borrowingRepository.save(borrowing);

      return entityToDTO(borrowing);
   }

   public BorrowingDTO restitute(Integer userId, Integer mediaId) {
      Borrowing borrowing = borrowingRepository.findByUserIdAndMediaId(userId, mediaId);
      UserDTO userDTO = userApiProxy.findUserById(userId);
      MediaDTO mediaDTO = mediaService.findById(mediaId);

      // Increase stock
      if (mediaDTO.getMediaType().equals(MediaType.BOOK.toString())) {
         bookService.increaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.GAME.toString())) {
         gameService.increaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.MUSIC.toString())) {
         musicService.increaseStock(mediaDTO.getEan());
      } else if (mediaDTO.getMediaType().equals(MediaType.VIDEO.toString())) {
         videoService.increaseStock(mediaDTO.getEan());
      }

      // increase user counter
      int counter = userDTO.getCounter() - 1;
      userApiProxy.updateUserCounter(userId,counter);

      // release the media
      mediaService.release(mediaId);

      borrowing.setReturnDate(new Date());
      borrowingRepository.save(borrowing);

      return entityToDTO(borrowing);
   }

   // after 8 weeks
   public List<BorrowingDTO> findDelayed(Date date){
      java.sql.Date sDate = new java.sql.Date(date.getTime());
      List<Borrowing> borrowings = new ArrayList<>();
      List<BorrowingDTO> borrowingDTOS = new ArrayList<>();

      borrowings = borrowingRepository.findDelayed( sDate, daysOfDelay, maxExtention);
      for (Borrowing borrowing: borrowings){
         borrowingDTOS.add(entityToDTO(borrowing));
      }

      if (!borrowingDTOS.isEmpty()) {
         return borrowingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }

   }

   public List<BorrowingDTO> findByUserIdNotReturn(Integer userId) {
      List<BorrowingDTO> borrowingDTOS = new ArrayList<>();

      for(Borrowing borrowing: borrowingRepository.findByUserIdNotReturn(userId)){
         borrowingDTOS.add(entityToDTO(borrowing));
      }

      if (!borrowingDTOS.isEmpty()) {
         return borrowingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   public BorrowingDTO extend(Integer userId, Integer mediaId) {
      Borrowing borrowing = borrowingRepository.findByUserIdAndMediaId(userId, mediaId);

      if(borrowing==null) {
         throw new ResourceNotFoundException("This borrowing doesn't exist !");
      }

      // increase extension
      Integer extension = 0;

      if(borrowing.getExtended() != null) {
         extension = borrowing.getExtended();
      }

      if(extension<maxExtention) {
         borrowing.setExtended(++extension);

         // increase borrowing date
         Date date = borrowing.getBorrowingDate();
         date = DateUtils.addDays(date, daysOfDelay);
         borrowing.setBorrowingDate(date);

         // delete borrowing
         borrowing = borrowingRepository.save(borrowing);
      }

      return entityToDTO(borrowing);
   }
}
