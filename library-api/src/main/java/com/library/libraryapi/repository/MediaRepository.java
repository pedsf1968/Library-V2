package com.library.libraryapi.repository;

import com.library.libraryapi.model.Media;
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

   @Query(value = "SELECT * FROM Media WHERE status = 'FREE' AND ean = :ean LIMIT 1", nativeQuery = true)
   Media findFreeByEan(String ean);

   @Query(value = "SELECT * FROM Media WHERE status = 'BLOCKED' AND ean = :ean", nativeQuery = true)
   Media findBlockedByEan(String ean);

   @Modifying
   @Transactional
   @Query(value = "UPDATE Media SET status = 'BLOCKED' WHERE id = (SELECT f.id FROM media f WHERE f.ean = :ean AND f.status = 'FREE' LIMIT 1)", nativeQuery = true)
   void blockFreeByEan(String ean);

   @Modifying
   @Transactional
   @Query(value = "UPDATE Media SET status = 'BORROWED', return_date = :date WHERE id = :mediaId", nativeQuery = true)
   void borrow(Integer mediaId, Date date);

   @Modifying
   @Transactional
   @Query("UPDATE Media m SET m.status = 'FREE', m.returnDate = null WHERE m.id = ?1")
   void release(Integer mediaId);

   @Modifying
   @Transactional
   @Query("UPDATE Media m SET m.returnDate = ?1 WHERE m.id = ?2")
   void updateReturnDate(Date date, Integer mediaId);
}
