package com.library.libraryapi.service;

import com.library.libraryapi.dto.business.*;
import com.library.libraryapi.dto.global.UserDTO;
import com.library.libraryapi.exceptions.ConflictException;
import com.library.libraryapi.exceptions.ForbiddenException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.model.Booking;
import com.library.libraryapi.model.MediaStatus;
import com.library.libraryapi.model.UserStatus;
import com.library.libraryapi.proxy.UserApiProxy;
import com.library.libraryapi.repository.BookingRepository;
import com.library.libraryapi.repository.BookingSpecification;
import com.library.libraryapi.repository.BorrowingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("BookingService")
public class BookingService implements GenericService<BookingDTO, Booking,Integer> {
   private static final String CANNOT_FIND_WITH_EAN = "Cannot find Booking with the ean : ";
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Booking with the id : ";
   private static final String CANNOT_SAVE ="Failed to save Booking";
   private static final String EXCEPTION_FORBIDDEN ="The user is not authorized, subscription fees not updated !";
   private static final String EXCEPTION_BAN ="The user banned is not authorized !";
   private static final String EXCEPTION_HAD_ALREADY_BORROWED ="The user had already borrowed this Media !";
   private static final String EXCEPTION_CANT_BOOK_MORE ="The booking can't exceed the limit : ";

   @Value("${library.booking.delay}")
   private int daysOfDelay;

   private static final Integer BOOKING_LIMIT_MAX = 2; // limit 2* media quantity

   private final BookingRepository bookingRepository;
   private final BorrowingRepository borrowingRepository;
   private final MediaService mediaService;
   private final UserApiProxy userApiProxy;

   private final ModelMapper modelMapper = new ModelMapper();

   public BookingService(BookingRepository bookingRepository, MediaService mediaService, BorrowingRepository borrowingRepository, UserApiProxy userApiProxy) {
      this.bookingRepository = bookingRepository;
      this.mediaService = mediaService;
      this.borrowingRepository = borrowingRepository;
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
      MediaDTO mediaDTO = mediaService.getNextReturnByEan(booking.getEan());

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

   BookingDTO findByEanAndUserId(String ean, Integer userId) {
      return entityToDTO(bookingRepository.findByEanAndUserId(ean,userId));
   }

   BookingDTO findNextBookingByMediaId(String ean) {
      return entityToDTO(bookingRepository.findNextBookingByMediaId(ean));
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
      MediaDTO mediaDTO = mediaService.findOneByEan(mediaEan);
      Integer quantity = 0;
      Integer stock = 0;

      String userStatus = userDTO.getStatus();
      if(userStatus.equals(UserStatus.FORBIDDEN.name()) ){
         throw new ForbiddenException(EXCEPTION_FORBIDDEN);
      } else if (userStatus.equals(UserStatus.BAN.name())){
         throw new ForbiddenException(EXCEPTION_BAN);
      } else if (borrowingRepository.userHadBorrowed(userId,mediaEan).equals(Boolean.TRUE)) {
         // user had this media
         throw new ForbiddenException(EXCEPTION_HAD_ALREADY_BORROWED);
      } else if (bookingRepository.userHadBooked(userId,mediaEan)) {
         // user want to book the same media again
         throw new ForbiddenException(EXCEPTION_HAD_ALREADY_BORROWED);
      }

      //
      // La liste de réservation ne peut comporter qu’un maximum de personnes correspondant à 2x le nombre d’exemplaires de l’ouvrage.
      quantity = mediaDTO.getQuantity();
      stock = mediaDTO.getStock();


      if(stock > 0) {
         // media available we can put flag to borrow
         mediaDTO = mediaService.findFreeByEan(mediaEan);
         mediaService.setStatus(mediaDTO.getId(), MediaStatus.BOOKED);
         mediaService.decreaseStock(mediaDTO);

         // calculate the restitution date adding 2 days
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(new Date());
         calendar.add(Calendar.DATE, daysOfDelay);
         booking.setPickUpDate(new java.sql.Date(calendar.getTimeInMillis()));
         // record mediaId to borrow
         booking.setMediaId(mediaDTO.getId());
      } else if (-stock <= quantity*BOOKING_LIMIT_MAX) {
         // no media available we anly decrease media counter
         mediaService.decreaseStock(mediaDTO);
         booking.setPickUpDate(null);
      } else {
         // can't book
         throw new ForbiddenException(EXCEPTION_CANT_BOOK_MORE + quantity);
      }


      booking.setUserId(userId);
      booking.setEan(mediaEan);
      booking.setBookingDate(new Date());
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

      if (!bookingDTOS.isEmpty()) {
         return bookingDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   /**
    * method for booking a media
    *
    * @param bookingId id of the booking
    * @return the Booking DTO
    */
   public BookingDTO cancelBooking(Integer bookingId) {
      Booking booking;
      MediaDTO mediaDTO;

      if (existsById(bookingId)) {
         booking = bookingRepository.findById(bookingId).orElse(null);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + bookingId);
      }

      String ean = booking.getEan();
      bookingRepository.deleteById(bookingId);

      if (booking!=null) {
         Integer mediaId = booking.getMediaId();
         if (mediaId != null) {
            mediaDTO = mediaService.findById(mediaId);
            // search if another claim this media
            BookingDTO bookingDTO = findNextBookingByMediaId(ean);
            if (bookingDTO == null) {
               // no other we release is
               mediaService.setStatus(mediaId, MediaStatus.FREE);
            } else {
               // calculate the restitution date adding 2 days
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(new Date());
               calendar.add(Calendar.DATE, daysOfDelay);
               bookingDTO.setPickUpDate(new java.sql.Date(calendar.getTimeInMillis()));
               bookingDTO.setMediaId(mediaId);
            }
         } else {
            mediaDTO = mediaService.findOneByEan(booking.getEan());
         }
         mediaService.increaseStock(mediaDTO);
      }

      return entityToDTO(booking);
   }

   /**
    * Method to find media ready to pick up for sending mail
    * @return
    */
   public List<BookingDTO> findReadyToPickUp() {
      List<Booking> bookings = bookingRepository.findReadyToPickUp();

      List<BookingDTO> bookingDTOS = new ArrayList<>();

      for (Booking b : bookings) {
         bookingDTOS.add(entityToDTO(b));
      }

      return bookingDTOS;
   }

   /**
    * set the pickup date fot one booking
    * @param id booking id
    * @param date new date
    */
   public void updatePickUpDate(Integer id, Date date) {
      bookingRepository.updatePickUpDate( id, new java.sql.Date(date.getTime()));
   }
}
