package com.library.libraryapi.repository;

import com.library.libraryapi.model.Media;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MediaSpecification implements Specification<Media> {

   private final Media filter;

   public MediaSpecification(Media filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Media> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getMediaType() != null) {
         predicates.add(criteriaBuilder.equal(root.get("mediaType"),  filter.getMediaType() ));
      }

      if (filter.getEan() != null) {
         predicates.add(criteriaBuilder.like(root.get("ean"), "%" + filter.getEan() + "%"));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
