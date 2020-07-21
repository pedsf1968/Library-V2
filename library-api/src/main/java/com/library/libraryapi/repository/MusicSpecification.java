package com.library.libraryapi.repository;

import com.library.libraryapi.model.Music;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MusicSpecification implements Specification<Music> {

   private final Music filter;

   public MusicSpecification(Music filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Music> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getTitle() != null) {
         predicates.add(criteriaBuilder.like(root.get("title"), "%" + filter.getTitle() + "%"));
      }

      if (filter.getEan() != null) {
         predicates.add(criteriaBuilder.like(root.get("ean"), "%" + filter.getEan() + "%"));
      }

      if (filter.getPublicationDate() != null) {
         predicates.add(criteriaBuilder.equal(root.get("publicationDate"), filter.getPublicationDate() ));
      }


      if (filter.getAuthorId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("authorId"), filter.getAuthorId()));
      }

      if (filter.getComposerId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("composerId"), filter.getComposerId()));
      }

      if (filter.getInterpreterId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("interpreterId"), filter.getInterpreterId()));
      }

      if (filter.getType() != null) {
         predicates.add(criteriaBuilder.equal(root.get("type"),  filter.getType()));
      }

      if (filter.getFormat() != null) {
         predicates.add(criteriaBuilder.equal(root.get("format"), filter.getFormat()));
      }


      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
