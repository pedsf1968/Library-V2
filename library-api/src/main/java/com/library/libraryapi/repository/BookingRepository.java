package com.library.libraryapi.repository;

import com.library.libraryapi.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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

   @Query(value = "SELECT * FROM booking WHERE ean = :ean AND user_id = :userId", nativeQuery = true)
   Booking findByEanAndUserId(String ean, Integer userId);

   @Query(value = "SELECT * FROM Booking WHERE ean = :ean ORDER BY booking_date LIMIT 1", nativeQuery = true)
   Booking findNextBookingByMediaId(String ean);

   @Query(value = "SELECT COUNT(*)>0 FROM booking WHERE user_id = :userId AND ean = :ean", nativeQuery = true)
   Boolean userHadBooked(Integer userId, String ean);

   @Query(value = "SELECT * FROM booking WHERE pickup_date IS NOT NULL", nativeQuery = true)
   List<Booking> findReadyToPickUp();

   @Query(value = "SELECT * FROM booking WHERE pickup_date < :date", nativeQuery = true)
   List<Booking> findOutOfDate(Date date);


   @Query(value ="SELECT COUNT(*)+1 FROM booking WHERE ean =:ean AND booking_date < :date", nativeQuery = true)
   Integer getRankByEanAndDate(String ean, Date date);

   @Query(value ="SELECT rank FROM booking WHERE ean = :ean ORDER BY rank DESC LIMIT 1;", nativeQuery = true)
   Integer getRankByEan(String ean);

   @Query(value = "SELECT * FROM booking WHERE ean = :ean AND rank = 1", nativeQuery = true)
   Booking getNextByEan(String ean);

   @Modifying
   @Transactional
   @Query(value = "UPDATE booking SET pickup_date = :date WHERE id = :id", nativeQuery = true)
   void updatePickUpDate(Integer id, Date date);

   @Modifying
   @Transactional
   @Query(value = "DELETE FROM booking WHERE ean = :ean AND user_id = :userId", nativeQuery = true)
   void deleteNotPickedUp(String ean, Integer userId);

   @Modifying
   @Transactional
   @Query(value = "DELETE FROM booking WHERE ean = :ean AND user_id = :userId", nativeQuery = true)
   void deleteByEanAndUserId(String ean, Integer userId);

   @Modifying
   @Transactional
   @Query(value = "UPDATE booking SET rank = rank-1 WHERE ean = :ean", nativeQuery = true)
   void decreaseRankByEan(String ean);

   @Modifying
   @Transactional
   @Query(value = "UPDATE booking SET rank = rank+1 WHERE ean = :ean", nativeQuery = true)
   void increaseRankByEan(String ean);


}
