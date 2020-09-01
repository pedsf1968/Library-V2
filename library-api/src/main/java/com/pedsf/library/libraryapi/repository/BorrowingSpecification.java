package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Borrowing;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BorrowingSpecification implements Specification<Borrowing> {

   private final transient Borrowing filter;

   public BorrowingSpecification(Borrowing filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Borrowing> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("id"),  filter.getId() ));
      }
      if (filter.getMediaId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("mediaId"),  filter.getMediaId() ));
      }

      if (filter.getUserId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("userId"), filter.getUserId()));
      }

      if (filter.getBorrowingDate() != null) {
         predicates.add(criteriaBuilder.equal(root.get("borrowingDate"), filter.getBorrowingDate()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
