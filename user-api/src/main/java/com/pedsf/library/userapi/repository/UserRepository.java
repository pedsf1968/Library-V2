package com.pedsf.library.userapi.repository;


import com.pedsf.library.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserRepository extends PagingAndSortingRepository, an interface that provides generic CRUD operations
 * and add methods to retrieve entities using the pagination and sorting abstraction.
 *
 *  also extend JpaSpecificationExecutor<T> interface.
 *  This interface provides methods that can be used to invoke database queries using JPA Criteria API.
 *  T describes the type of the queried entity, in our case is Contact.
 *  To specify the conditions of the invoked database query, we need to create a new implementation of
 *  Specification<T>: UserSpecification class.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
   User getOne(Integer id);
   User findByEmail(String email);
   User findByStatus(String status);
   List<User> findAll();
   User save(User user);
   void deleteById(Integer id);


   @Transactional
   @Modifying
   @Query("UPDATE User u SET u.counter = :counter WHERE u.id = :userId")
   int updateCounter(@Param("userId") Integer userId, @Param("counter") Integer counter);


   @Transactional
   @Modifying
   @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
   int updateStatus(@Param("userId") Integer userId, @Param("status") String status);
}
