package com.pedsf.library.userapi.repository;

import com.pedsf.library.userapi.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

   private final User filter;

   public UserSpecification(User filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getFirstName() != null) {
         predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + filter.getFirstName() + "%"));
      }

      if (filter.getLastName() != null) {
         predicates.add(criteriaBuilder.like(root.get("lastname"), "%" + filter.getLastName() + "%"));
      }

      if (filter.getEmail() != null) {
         predicates.add(criteriaBuilder.like(root.get("email"), "%" + filter.getEmail() + "%"));
      }

      if (filter.getPhone()!= null) {
         predicates.add(criteriaBuilder.like(root.get("phone"), "%" + filter.getPhone() + "%"));
      }

      if (filter.getStatus()!= null) {
         predicates.add(criteriaBuilder.like(root.get("status"), "%" + filter.getStatus() + "%"));
      }

      if (filter.getCounter()!= null) {
         predicates.add(criteriaBuilder.equal(root.get("counter"), filter.getCounter() ));
      }


      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
