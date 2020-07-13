package com.library.libraryapi.repository;

import com.library.libraryapi.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer>, JpaSpecificationExecutor<Booking> {
   Optional<Booking> findById(Integer id);
   List<Booking> findByMediaId(Integer id);
   List<Booking> findByUserId(Integer id);
   List<Booking> findAll();

   Booking save(Booking booking);
   void deleteById(Integer id);

   @Query("SELECT b FROM Booking b WHERE b.mediaId = ?1 ORDER BY bookingDate LIMIT 1")
   Booking findNextBookingByMediaId(Integer mediaId);

   @Query("SELECT COUNT(*) FROM Booking b WHERE b.mediaId = ?1")
   Integer getNumberOfBookingByMediaId(Integer mediaId);

}
