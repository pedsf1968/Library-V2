package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.BookingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.Booking;
import com.pedsf.library.libraryapi.model.MediaStatus;
import com.pedsf.library.libraryapi.model.UserStatus;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.BookingRepository;
import com.pedsf.library.libraryapi.repository.BookingSpecification;
import com.pedsf.library.libraryapi.repository.BorrowingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service("BookingService")
public class BookingService implements GenericService<BookingDTO, Booking,Integer> {
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

   public BookingService(BookingRepository bookingRepository, MediaService mediaService,
                         BorrowingRepository borrowingRepository, UserApiProxy userApiProxy) {
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
      } else if (Boolean.TRUE.equals(bookingRepository.userHadBooked(userId,mediaEan))) {
         // user want to book the same media again
         throw new ForbiddenException(EXCEPTION_HAD_ALREADY_BORROWED);
      }

      //
      // La liste de réservation ne peut comporter qu’un maximum de personnes correspondant à 2x le nombre d’exemplaires de l’ouvrage.
      quantity = mediaDTO.getQuantity();
      stock = mediaDTO.getStock();

      Integer rank = bookingRepository.getRankByEan( mediaEan);
      if (rank == null) {
         rank = 0;
      }

      if(stock > 0) {
         // media available we can put flag BOOKED to be the first to borrow
         mediaDTO = mediaService.findFreeByEan(mediaEan);
         mediaService.setStatus(mediaDTO.getId(), MediaStatus.BOOKED);

         // calculate the restitution date adding 2 days
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(new Date());
         calendar.add(Calendar.DATE, daysOfDelay);
         booking.setPickUpDate(new java.sql.Date(calendar.getTimeInMillis()));
         // record mediaId to borrow
         booking.setMediaId(mediaDTO.getId());
      } else if (-stock > quantity*BOOKING_LIMIT_MAX) {
         // can't book
         throw new ForbiddenException(EXCEPTION_CANT_BOOK_MORE + quantity*BOOKING_LIMIT_MAX);
      }

      mediaService.decreaseStock(mediaDTO);

      booking.setRank(++rank);
      booking.setUserId(userId);
      booking.setEan(mediaEan);
      booking.setBookingDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()) );
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
      String ean = null;
      Integer mediaId = null;

      if (existsById(bookingId)) {
         booking = bookingRepository.findById(bookingId).orElse(null);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + bookingId);
      }

      if (booking!= null){
         ean = booking.getEan();
         mediaId = booking.getMediaId();
         bookingRepository.deleteById(bookingId);
         bookingRepository.decreaseRankByEan(ean);
      }

      if (mediaId != null) {
         // a media is BOOKED find next user who claim the same media
         booking = bookingRepository.getNextByEan(ean);

         if (booking == null) {
            // no other booking for this ean we release it
            mediaService.setStatus(mediaId, MediaStatus.FREE);
         } else {
            // calculate the restitution date adding 2 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, daysOfDelay);
            booking.setPickUpDate(new java.sql.Date(calendar.getTimeInMillis()));
            booking.setMediaId(mediaId);
            // Update the booking of the next user
            bookingRepository.save(booking);
         }
      }

      mediaDTO = mediaService.findOneByEan(ean);
      mediaService.increaseStock(mediaDTO);

      return entityToDTO(booking);
   }

   /**
    * Method to find media ready to pick up for sending mail
    * call the method to cancel out of date booking
    *
    * @return BookingDTO list
    */
   public List<BookingDTO> findReadyToPickUp() {

      cancelOutOfDate();
      List<Booking> bookings = bookingRepository.findReadyToPickUp();

      List<BookingDTO> bookingDTOS = new ArrayList<>();

      for (Booking b : bookings) {
         bookingDTOS.add(entityToDTO(b));
      }

      return bookingDTOS;
   }

   /**
    * Method to cancel delayed booking when the pickup date is before today
    */
   public void cancelOutOfDate() {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DATE, daysOfDelay);
      List<Booking> bookings = bookingRepository.findOutOfDate(new java.sql.Date(calendar.getTimeInMillis()));

      for( Booking b : bookings) {
         cancelBooking(b.getId());
      }
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
