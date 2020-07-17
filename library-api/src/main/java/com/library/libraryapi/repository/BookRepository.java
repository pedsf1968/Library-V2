package com.library.libraryapi.repository;

import com.library.libraryapi.model.Book;
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
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
   Optional<Book> findByEan(String ean);
   List<Book> findAll();
   Book save(Book book);
   void deleteByEan(String ean);

   @Query("SELECT DISTINCT authorId FROM Book ORDER BY authorId")
   List<Integer> findAllAuthors();

   @Query("SELECT DISTINCT editorId FROM Book ORDER BY editorId")
   List<Integer> findAllEditors();

   @Query(value = "SELECT DISTINCT title FROM Book ORDER BY title", nativeQuery = true)
   List<String> findAllTitles();

   @Query("SELECT b FROM Book b WHERE b.stock>(-2*b.quantity)")
   List<Book> findAllAllowed();

   @Modifying
   @Transactional
   @Query("UPDATE Book b SET b.stock = (SELECT s.stock FROM Book s WHERE s.ean = ?1) +1")
   void increaseStock(String ean);

   @Modifying
   @Transactional
   @Query("UPDATE Book b SET b.stock = (SELECT s.stock FROM Book s WHERE s.ean = ?1) -1")
   void decreaseStock(String ean);
}
