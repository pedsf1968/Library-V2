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
   List<Booking> findByEan(String ean);
   List<Booking> findByUserId(Integer id);
   List<Booking> findAll();

   Booking save(Booking booking);
   void deleteById(Integer id);

   @Query(value = "SELECT * FROM Booking WHERE ean = :ean ORDER BY booking_date LIMIT 1", nativeQuery = true)
   Booking findNextBookingByMediaId(String ean);

}
