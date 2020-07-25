package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Game;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GameSpecification implements Specification<Game> {

   private final transient Game filter;

   public GameSpecification(Game filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getTitle() != null) {
         predicates.add(criteriaBuilder.like(root.get("title"), "%" + filter.getTitle() + "%"));
      }

      if (filter.getEan() != null) {
         predicates.add(criteriaBuilder.like(root.get("ean"), "%" + filter.getEan() + "%"));
      }

      if (filter.getPublicationDate() != null) {
         predicates.add(criteriaBuilder.like(root.get("publicationDate"), "%" + filter.getPublicationDate() + "%"));
      }

      if (filter.getTitle() != null) {
         predicates.add(criteriaBuilder.like(root.get("title"), "%" + filter.getTitle() + "%"));
      }

      if (filter.getPublicationDate() != null) {
         predicates.add(criteriaBuilder.like(root.get("publicationDate"), "%" + filter.getPublicationDate() + "%"));
      }

      if (filter.getType() != null) {
         predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
      }

      if (filter.getFormat() != null) {
         predicates.add(criteriaBuilder.equal(root.get("format"), filter.getFormat() ));
      }


      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
