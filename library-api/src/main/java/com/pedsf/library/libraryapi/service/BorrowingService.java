package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.BorrowingDTO;
import com.pedsf.library.dto.business.MediaDTO;
import com.pedsf.library.dto.global.UserDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.Booking;
import com.pedsf.library.libraryapi.model.Borrowing;
import com.pedsf.library.libraryapi.model.MediaStatus;
import com.pedsf.library.libraryapi.model.UserStatus;
import com.pedsf.library.libraryapi.proxy.UserApiProxy;
import com.pedsf.library.libraryapi.repository.BookingRepository;
import com.pedsf.library.libraryapi.repository.BorrowingRepository;
import com.pedsf.library.libraryapi.repository.BorrowingSpecification;
import lombok.Data;
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

@Data
@Service("BorrowingService")
public class BorrowingService implements GenericService<BorrowingDTO, Borrowing,Integer> {
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

   private final BookingRepository bookingRepository;
   private final BorrowingRepository borrowingRepository;
   private final MediaService mediaService;
   private final UserApiProxy userApiProxy;
   private final ModelMapper modelMapper = new ModelMapper();

   public BorrowingService(BorrowingRepository borrowingRepository, BookingRepository bookingRepository,
                           MediaService mediaService, UserApiProxy userApiProxy) {
      this.borrowingRepository = borrowingRepository;
      this.bookingRepository = bookingRepository;
      this.mediaService = mediaService;
      this.userApiProxy = userApiProxy;
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


   /**
    * Method to know if a media is borrowed by a user
    *
    * @param userId identification of the user
    * @param ean identification of the media
    * @return true if the user has already borrowed this media
    */
   public Boolean userHadBorrowed(Integer userId, String ean) {
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
      // calculate the restitution date adding 4 weeks 28 days
      java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
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
      if(userStatus == null) {
         userDTO.setStatus(UserStatus.MEMBER.name());
      } else if (userStatus.equals(UserStatus.FORBIDDEN.name()) ){
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

      if(mediaDTO.getStock() > 0) {
         mediaService.decreaseStock(mediaDTO);
      } else {
         // stock problem
         throw new ForbiddenException(EXCEPTION_NO_MEDIA);
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

      // record the borrowing in borrowing table
      borrowingRepository.save(borrowing);

      return entityToDTO(borrowing);
   }

   /**
    * Method for media restitution
    *
    * @param userId identification of the user
    * @param mediaId identification of the media
    * @return modified borrowing datas
    */
   public BorrowingDTO restitute(Integer userId, Integer mediaId) {
      Borrowing borrowing = borrowingRepository.findByUserIdAndMediaId(userId, mediaId);
      UserDTO userDTO = userApiProxy.findUserById(userId);
      MediaDTO mediaDTO = mediaService.findById(mediaId);

      borrowing.setReturnDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
      borrowingRepository.save(borrowing);

      // Increase stock
      mediaService.increaseStock(mediaDTO);

      // increase user counter
      int counter = userDTO.getCounter() - 1;
      userApiProxy.updateUserCounter(userId,counter);


      // release the media
      mediaService.release(mediaId);

      // search if it's booked
      Booking booking = bookingRepository.findNextBookingByMediaId(mediaDTO.getEan());

      if(booking!=null) {
         // set this media booked to borrow only by the user
         mediaService.setStatus(mediaId, MediaStatus.BOOKED);

         // calculate the restitution date adding 2 days
         java.util.Date today = new java.util.Date();
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(today);
         calendar.add(Calendar.DATE, daysOfDelay);

         bookingRepository.updatePickUpDate(booking.getId(), new java.sql.Date(calendar.getTimeInMillis()));
      }

      return entityToDTO(borrowing);
   }

   /**
    * method to find borrowing expiration
    *
    * @param date when the borrowing is expirated
    * @return list of expirated borowings
    */
   public List<BorrowingDTO> findDelayed(Date date){
      java.sql.Date sDate = new java.sql.Date(date.getTime());

      List<Borrowing> borrowings;
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

   /**
    * Method to find user borrowings
    *
    * @param userId identification of the user
    * @return list of curent borrowings
    */
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

   /**
    * method to extend a borrowing
    *
    * @param userId identification of the user
    * @param mediaId identification of the media
    * @return return the borrowing modified
    */
   public BorrowingDTO extend(Integer userId, Integer mediaId) {
      Borrowing borrowing = borrowingRepository.findByUserIdAndMediaId(userId, mediaId);
      Calendar calendar = Calendar.getInstance();


      if(borrowing==null) {
         throw new ResourceNotFoundException("This borrowing doesn't exist !");
      }

      // increase extension
      Integer extension = 0;

      if(borrowing.getExtended() != null) {
         extension = borrowing.getExtended();
      }

      java.sql.Date limitDate = new java.sql.Date(DateUtils.addDays(new Date(),-daysOfDelay).getTime());
      java.sql.Date borrowingDate = borrowing.getBorrowingDate();


      if(extension<maxExtention && borrowingDate.after(limitDate)) {
         borrowing.setExtended(++extension);

         // increase borrowing date
         calendar.setTime(borrowingDate);
         calendar.add(Calendar.DATE, daysOfDelay);

         borrowing.setBorrowingDate(new java.sql.Date(calendar.getTimeInMillis()));

         // delete borrowing
         borrowing = borrowingRepository.save(borrowing);
      }

      return entityToDTO(borrowing);
   }
}
