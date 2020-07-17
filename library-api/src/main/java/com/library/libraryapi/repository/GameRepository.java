package com.library.libraryapi.repository;

import com.library.libraryapi.model.Game;
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

   @Query(value = "SELECT DISTINCT title FROM Game ORDER BY title", nativeQuery = true)
   List<String> findAllTitles();

   @Query("SELECT g FROM Game g WHERE g.stock>(-2*g.quantity)")
   List<Game> findAllAllowed();

   @Modifying
   @Transactional
   @Query("UPDATE Game g SET g.stock = (SELECT s.stock FROM Game s WHERE s.ean = ?1) +1")
   void increaseStock(String ean);

   @Modifying
   @Transactional
   @Query("UPDATE Game g SET g.stock = (SELECT s.stock FROM Game s WHERE s.ean = ?1) -1")
   void decreaseStock(String ean);

}
