package com.library.libraryapi.repository;

import com.library.libraryapi.model.Person;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PersonSpecification implements Specification<Person> {

   private final Person filter;

   public PersonSpecification(Person filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getFirstName() != null) {
         predicates.add(criteriaBuilder.like(root.get("firstname"), "%" + filter.getFirstName() + "%"));
      }

      if (filter.getLastName() != null) {
         predicates.add(criteriaBuilder.like(root.get("lastname"), "%" + filter.getLastName() + "%"));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
