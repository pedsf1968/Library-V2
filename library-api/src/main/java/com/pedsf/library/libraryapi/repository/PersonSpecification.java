package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Person;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PersonSpecification implements Specification<Person> {

   private final transient Person filter;

   public PersonSpecification(Person filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getFirstName() != null) {
         predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + filter.getFirstName() + "%"));
      }

      if (filter.getLastName() != null) {
         predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + filter.getLastName() + "%"));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
