package com.library.libraryapi.repository;

import com.library.libraryapi.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Integer>, JpaSpecificationExecutor<Borrowing> {
   Optional<Borrowing> findById(Integer integer);
   List<Borrowing> findAll();
   Borrowing save(Borrowing borrowing);
   void deleteById(Integer id);

   @Query("SELECT b FROM Borrowing b WHERE b.userId = ?1 AND b.mediaId = ?2")
   Borrowing findByUserIdAndMediaId(Integer userId, Integer mediaId);

   @Query(value = "SELECT * FROM Borrowing b " +
               "WHERE (b.return_date IS NULL) AND ( ( (b.borrowing_date + (b.extended+1)*:delay) < :date ) OR (b.extended>=:max) )",
         nativeQuery = true)
   List<Borrowing> findDelayed(@Param("date")Date date, @Param("delay") Integer delay, @Param("max") Integer maxExtension);

   @Query("SELECT b FROM Borrowing b WHERE  b.returnDate IS NULL AND  b.userId = ?1")
   List<Borrowing> findByUserIdNotReturn(Integer userId);

}
