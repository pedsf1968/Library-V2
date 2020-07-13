package com.library.libraryapi.service;

import com.library.libraryapi.dto.business.BookingDTO;
import com.library.libraryapi.exceptions.ConflictException;
import com.library.libraryapi.exceptions.ResourceNotFoundException;
import com.library.libraryapi.model.Booking;
import com.library.libraryapi.repository.BookingRepository;
import com.library.libraryapi.repository.BookingSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("BookingService")
public class BookingService implements GenericService<BookingDTO, Booking,Integer> {
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Booking with the id : ";
   private static final String CANNOT_SAVE ="Failed to save Booking";

   private final BookingRepository bookingRepository;
   private final ModelMapper modelMapper = new ModelMapper();

   public BookingService(BookingRepository bookingRepository) {
      this.bookingRepository = bookingRepository;
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
      if (  !StringUtils.isEmpty(bookingDTO.getMediaId()) &&
            !StringUtils.isEmpty(bookingDTO.getUserId())) {

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
            !StringUtils.isEmpty(bookingDTO.getMediaId()) &&
            !StringUtils.isEmpty(bookingDTO.getUserId())) {
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

      return modelMapper.map(booking, BookingDTO.class);
   }

   @Override
   public Booking dtoToEntity(BookingDTO bookingDTO) {

      return modelMapper.map(bookingDTO, Booking.class);
   }
}
