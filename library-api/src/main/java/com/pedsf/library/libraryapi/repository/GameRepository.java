package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer>, JpaSpecificationExecutor<Game> {
   Optional<Game> findByEan(String ean);
   List<Game> findAll();
   Game save(Game game);
   void deleteByEan(String ean);

   @Query("SELECT DISTINCT editorId FROM Game ORDER BY editorId")
   List<Integer> findAllEditors();

   @Query(value = "SELECT DISTINCT title FROM Game ORDER BY title", nativeQuery = true)
   List<String> findAllTitles();

   @Query("SELECT g FROM Game g WHERE g.stock>(-2*g.quantity)")
   List<Game> findAllAllowed();

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE Game SET stock = stock + 1 WHERE ean = :ean", nativeQuery = true)
   void increaseStock(String ean);

   @Transactional
   @Modifying(clearAutomatically = true)
   @Query(value = "UPDATE Game SET stock = stock - 1 WHERE ean = :ean", nativeQuery = true)
   void decreaseStock(String ean);

}
