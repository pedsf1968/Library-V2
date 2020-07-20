package com.library.libraryapi.repository;

import com.library.libraryapi.model.Book;
import com.library.libraryapi.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, Integer>, JpaSpecificationExecutor<Music> {
   Optional<Music> findByEan(String ean);
   List<Music> findAll();
   Music save(Music music);
   void deleteByEan(String ean);

   @Query("SELECT DISTINCT authorId FROM Music ORDER BY authorId")
   List<Integer> findAllAuthors();

   @Query("SELECT DISTINCT composerId FROM Music ORDER BY composerId")
   List<Integer> findAllComposers();

   @Query("SELECT DISTINCT interpreterId FROM Music ORDER BY interpreterId")
   List<Integer> findAllInterpreters();

   @Query(value = "SELECT DISTINCT title FROM Music ORDER BY title", nativeQuery = true)
   List<String> findAllTitles();

   @Query("SELECT m FROM Music m WHERE m.stock>(-2*m.quantity)")
   List<Music> findAllAllowed();

   @Modifying
   @Transactional
   @Query(value = "UPDATE Music SET stock = stock + 1 WHERE ean = :ean", nativeQuery = true)
   void increaseStock(String ean);

   @Modifying
   @Transactional
   @Query(value = "UPDATE Music SET stock = stock - 1 WHERE ean = :ean", nativeQuery = true)
   void decreaseStock(String ean);

}
