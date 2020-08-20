package com.pedsf.library.libraryapi.repository;

import com.pedsf.library.libraryapi.model.Video;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class VideoSpecification implements Specification<Video> {

   private final transient Video filter;

   public VideoSpecification(Video filter) {
      super();
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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

      if (filter.getDirectorId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("directorId"), filter.getDirectorId()));
      }

      if (filter.getType() != null) {
         predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
      }

      if (filter.getFormat() != null) {
         predicates.add(criteriaBuilder.equal(root.get("format"), filter.getFormat()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
