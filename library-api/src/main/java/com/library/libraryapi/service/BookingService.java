package com.library.libraryapi.service;

import com.library.libraryapi.dto.business.*;
import com.library.libraryapi.dto.global.UserDTO;
import com.library.libraryapi.exceptions.ConflictException;
import com.library.libraryapi.exceptions.ForbiddenException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.model.Booking;
import com.library.libraryapi.model.Game;
import com.library.libraryapi.model.MediaType;
import com.library.libraryapi.model.UserStatus;
import com.library.libraryapi.proxy.UserApiProxy;
import com.library.libraryapi.repository.BookingRepository;
import com.library.libraryapi.repository.BookingSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("BookingService")
public class BookingService implements GenericService<BookingDTO, Booking,Integer> {
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Booking with the id : ";
   private static final String CANNOT_SAVE ="Failed to save Booking";
   private static final String EXCEPTION_FORBIDDEN ="The user is not authorized, subscription fees not updated !";
   private static final String EXCEPTION_BAN ="The user banned is not authorized !";
   private static final String EXCEPTION_HAD_ALREADY_BORROWED ="The user had already borrowed this Media !";
   private static final String EXCEPTION_CANT_BOOK_MORE ="The booking can't exceed the limit : ";

   private final BookingRepository bookingRepository;
   private final BookService bookService;
   private final GameService gameService;
   private final MusicService musicService;
   private final VideoService videoService;
   private final MediaService mediaService;
   private final BorrowingService borrowingService;
   private final UserApiProxy userApiProxy;

   private final ModelMapper modelMapper = new ModelMapper();

   public BookingService(BookingRepository bookingRepository, BookService bookService, GameService gameService, MusicService musicService, VideoService videoService, MediaService mediaService, BorrowingService borrowingService, UserApiProxy userApiProxy) {
      this.bookingRepository = bookingRepository;
      this.bookService = bookService;
      this.gameService = gameService;
      this.musicService = musicService;
      this.videoService = videoService;
      this.mediaService = mediaService;
      this.borrowingService = borrowingService;
      this.userApiProxy = userApiProxy;
   }

   @Override
   public boolean existsById(Integer id) {
      return bookingRepository.findById(id).isPresent();
   }

   @Override
   public BookingDTO findById(Integer id) {
      if(existsById(id)) {
         Booking booking = bookingRepository.findById(id).orElse(null);
         return entityToDTO(booking);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      }
   }

   @Override
   public List<BookingDTO> findAll() {
      List<Booking> bookings = bookingRepository.findAll();
      List<BookingDTO> bookingDTOS = new ArrayList<>();

      for (Booking booking: bookings) {
         bookingDTOS.add(entityToDTO(booking));
      }


      if (!bookingDTOS.isEmpty()) {
         return bookingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<BookingDTO> findAllFiltered(BookingDTO filter) {
      Booking booking = dtoToEntity(filter);

      Specification<Booking> spec = new BookingSpecification(booking);
      List<Booking> bookings = bookingRepository.findAll(spec);
      List<BookingDTO> bookingDTOS = new ArrayList<>();

      for (Booking b: bookings) {
         bookingDTOS.add(entityToDTO(b));
      }


      if (!bookingDTOS.isEmpty()) {
         return bookingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public Integer getFirstId(BookingDTO filter) {
      return findAllFiltered(filter).get(0).getId();
   }

   @Override
   public BookingDTO save(BookingDTO bookingDTO) {
      if (  !StringUtils.isEmpty(bookingDTO.getUser()) &&
            !StringUtils.isEmpty(bookingDTO.getMedia())) {

         bookingDTO.setId(null);
         Booking booking = bookingRepository.save(dtoToEntity(bookingDTO));

         return entityToDTO(booking);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }
   }

   @Override
   public BookingDTO update(BookingDTO bookingDTO) {
      if (  !StringUtils.isEmpty(bookingDTO.getId()) &&
            !StringUtils.isEmpty(bookingDTO.getMedia()) &&
            !StringUtils.isEmpty(bookingDTO.getUser())) {
         if (!existsById(bookingDTO.getId())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + bookingDTO.getId());
         }
         Booking booking = bookingRepository.save(dtoToEntity(bookingDTO));

         return entityToDTO(booking);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }
   }

   @Override
   public void deleteById(Integer id) {
      if (!existsById(id)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + id);
      } else {
         bookingRepository.deleteById(id);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(bookingRepository.count());
   }

   @Override
   public BookingDTO entityToDTO(Booking booking) {
      BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
      UserDTO userDTO = userApiProxy.findUserById( booking.getUserId());
      MediaDTO mediaDTO = mediaService.findOneByEan(booking.getEan());

      if(mediaDTO.getMediaType().equals(MediaType.BOOK.name())) {
         BookDTO bookDTO = bookService.findById(mediaDTO.getEan());
         mediaDTO.setStock(bookDTO.getStock());
      } else if(mediaDTO.getMediaType().equals(MediaType.GAME.name())) {
         GameDTO gameDTO = gameService.findById(mediaDTO.getEan());
         mediaDTO.setStock(gameDTO.getStock());
      } else if(mediaDTO.getMediaType().equals(MediaType.MUSIC.name())) {
         MusicDTO musicDTO = musicService.findById(mediaDTO.getEan());
         mediaDTO.setStock(musicDTO.getStock());
      } else if(mediaDTO.getMediaType().equals(MediaType.VIDEO.name())) {
         VideoDTO videoDTO = videoService.findById(mediaDTO.getEan());
         mediaDTO.setStock(videoDTO.getStock());
      }

      bookingDTO.setUser(userDTO);
      bookingDTO.setMedia(mediaDTO);

      return bookingDTO;
   }

   @Override
   public Booking dtoToEntity(BookingDTO bookingDTO) {
      Booking booking = modelMapper.map(bookingDTO, Booking.class);

      booking.setUserId(bookingDTO.getUser().getId());
      booking.setEan(bookingDTO.getMedia().getEan());

      return booking;
   }

   /**
    * method for booking a media
    *
    * @param userId id of the User who want to borrow
    * @param mediaEan id of the Media wanted
    * @return the Booking DTO
    */
   public BookingDTO booking(Integer userId, String mediaEan) {
      Booking booking = new Booking();
      UserDTO userDTO = userApiProxy.findUserById(userId);
      MediaType mediaType = mediaService.findMediaTypeByEan(mediaEan);
      Integer limit = 0;
      Integer stock = 0;

      String userStatus = userDTO.getStatus();
      if(userStatus.equals(UserStatus.FORBIDDEN.name()) ){
         throw new ForbiddenException(EXCEPTION_FORBIDDEN);
      } else if (userStatus.equals(UserStatus.BAN.name())){
         throw new ForbiddenException(EXCEPTION_BAN);
      } else if (borrowingService.userHadBorrowed(userId,mediaEan)) {
         throw new ForbiddenException(EXCEPTION_HAD_ALREADY_BORROWED);
      }

      // get quantity of the same media
      if (mediaType.equals(MediaType.BOOK)) {
         limit = bookService.findById(mediaEan).getQuantity()*2;
         stock = bookService.findById(mediaEan).getStock();

         if (-stock < limit) {
            bookService.decreaseStock(mediaEan);
         } else {
            throw new ForbiddenException(EXCEPTION_CANT_BOOK_MORE + limit);
         }
      } else if (mediaType.equals(MediaType.GAME)) {
         limit = gameService.findById(mediaEan).getQuantity();
         stock = gameService.findById(mediaEan).getStock();
         if (-stock < limit) {
            gameService.decreaseStock(mediaEan);
         } else {
            throw new ForbiddenException(EXCEPTION_CANT_BOOK_MORE + limit);
         }
      } else if (mediaType.equals(MediaType.MUSIC)) {
         limit = musicService.findById(mediaEan).getQuantity();
         stock = musicService.findById(mediaEan).getStock();
            if (-stock < limit) {
               musicService.decreaseStock(mediaEan);
            } else {
               throw new ForbiddenException(EXCEPTION_CANT_BOOK_MORE + limit);
            }
      } else if (mediaType.equals(MediaType.VIDEO)) {
         limit = videoService.findById(mediaEan).getQuantity();
         stock = videoService.findById(mediaEan).getStock();
               if (-stock < limit) {
                  videoService.decreaseStock(mediaEan);
               } else {
                  throw new ForbiddenException(EXCEPTION_CANT_BOOK_MORE + limit);
               }
      }

      booking.setUserId(userId);
      booking.setEan(mediaEan);
      booking.setBookingDate(new Date());
      booking.setPickUpDate(null);
      booking = bookingRepository.save(booking);

      return entityToDTO(booking);
   }

   /**
    * Method to get user booking's media
    *
    * @param userId ID of the user
    * @return the bookings of the user
    */
   public List<BookingDTO> findBookingsByUserId(Integer userId) {
      List<Booking> bookings = bookingRepository.findByUserId(userId);
      List<BookingDTO> bookingDTOS = new ArrayList<>();

      for (Booking b : bookings) {
         bookingDTOS.add(entityToDTO(b));
      }

      return bookingDTOS;
   }

   /**
    * method for booking a media
    *
    * @param bookingId id of the booking
    * @return the Booking DTO
    */
   public BookingDTO cancelBooking(Integer bookingId) {
      Booking booking;

      if (existsById(bookingId)) {
         booking = bookingRepository.findById(bookingId).orElse(null);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + bookingId);
      }

      if (booking!=null) {
         String mediaEan = booking.getEan();
         MediaType mediaType = mediaService.findMediaTypeByEan(mediaEan);

         bookingRepository.deleteById(bookingId);

         if (mediaType.equals(MediaType.BOOK)) {
            bookService.increaseStock(mediaEan);
         } else if (mediaType.equals(MediaType.GAME)) {
            gameService.increaseStock(mediaEan);
         } else if (mediaType.equals(MediaType.MUSIC)) {
            musicService.increaseStock(mediaEan);
         } else if (mediaType.equals(MediaType.VIDEO)) {
            videoService.increaseStock(mediaEan);
         }
      }

      return entityToDTO(booking);
   }
}
