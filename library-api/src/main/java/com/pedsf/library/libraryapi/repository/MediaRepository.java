package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Media;
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
public interface MediaRepository extends JpaRepository<Media, Integer>, JpaSpecificationExecutor<Media> {
   Optional<Media> findById(Integer id);
   List<Media> findByEan(String ean);
   List<Media> findAll();
   Media save(Media media);
   void deleteById(Integer id);


   @Query("SELECT COUNT(*) FROM Media m WHERE m.status = 'FREE' AND m.ean = ?1")
   Integer remaining(Integer mediaId);

   @Query(value = "SELECT * FROM Media WHERE ean = :ean LIMIT 1", nativeQuery = true)
   Media findOneByEan(String ean);

   @Query(value = "SELECT * FROM Media WHERE status = 'FREE' AND ean = :ean LIMIT 1", nativeQuery = true)
   Media findFreeByEan(String ean);

   @Query(value = "SELECT * FROM Media WHERE status = 'BLOCKED' AND ean = :ean LIMIT 1", nativeQuery = true)
   Media findBlockedByEan(String ean);

   @Query(value = "SELECT * FROM Media WHERE status = 'BOOKED' AND ean = :ean LIMIT 1", nativeQuery = true)
   Media findBoockedByEan(String ean);

   @Query(value="SELECT media_type FROM Media WHERE ean = :ean LIMIT 1", nativeQuery = true)
   String findMediaTypeByEan(String ean);

   @Query(value ="SELECT * FROM media WHERE ean = :ean ORDER BY return_date LIMIT 1", nativeQuery = true)
   Media getNextReturnByEan(String ean);


   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE Media SET status = 'BLOCKED' WHERE id = (SELECT f.id FROM media f WHERE f.ean = :ean AND f.status = 'FREE' LIMIT 1)", nativeQuery = true)
   void blockFreeByEan(String ean);

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE Media SET status = 'BOOKED' WHERE id = (SELECT f.id FROM media f WHERE f.ean = :ean AND f.status = 'FREE' LIMIT 1)", nativeQuery = true)
   void bookedFreeByEan(String ean);

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE media SET status = 'BORROWED', return_date = :date WHERE id = :mediaId", nativeQuery = true)
   void borrow(Integer mediaId, Date date);

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE media SET status = 'FREE', return_date = NULL WHERE id = :mediaId", nativeQuery = true)
   void release(Integer mediaId);

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE media SET return_date = :returnDate WHERE id = :mediaId", nativeQuery = true)
   void updateReturnDate(Integer mediaId,Date returnDate);

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE media SET status = :status WHERE id = :mediaId", nativeQuery = true)
   void setStatus(Integer mediaId, String status);
}
