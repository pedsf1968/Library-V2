package com.library.libraryapi.repository;

import com.library.libraryapi.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>, JpaSpecificationExecutor<Video> {
   Optional<Video> findByEan(String ean);
   List<Video> findAll();
   Video save(Video video);
   void deleteByEan(String ean);

   @Query("SELECT DISTINCT directorId FROM Video ORDER BY directorId")
   List<Integer> findAllDirectors();

   @Query(value = "SELECT DISTINCT v.actor_id FROM video_actors v ORDER BY v.actor_id", nativeQuery = true)
   List<Integer> findAllActors();

   @Query(value = "SELECT DISTINCT title FROM Video ORDER BY title", nativeQuery = true)
   List<String> findAllTitles();

   @Query("SELECT v FROM Video v WHERE v.stock>(-2*v.quantity)")
   List<Video> findAllAllowed();

   @Modifying
   @Transactional
   @Query("UPDATE Video v SET v.stock = (SELECT s.stock FROM Video s WHERE s.ean = ?1) +1")
   void increaseStock(String ean);

   @Modifying
   @Transactional
   @Query("UPDATE Video v SET v.stock = (SELECT s.stock FROM Video s WHERE s.ean = ?1) -1")
   void decreaseStock(String ean);

}
