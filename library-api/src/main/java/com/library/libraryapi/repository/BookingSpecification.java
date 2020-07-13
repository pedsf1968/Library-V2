package com.library.libraryapi.repository;

import com.library.libraryapi.model.Booking;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class BookingSpecification implements Specification<Booking> {

   private final Booking filter;

   public BookingSpecification(Booking filter) {
      this.filter = filter;
   }

   @Override
   public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getMediaId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("mediaId"), filter.getMediaId()));
      }

      if (filter.getEan() != null) {
         predicates.add(criteriaBuilder.like(root.get("ean"), "%" + filter.getEan() + "%"));
      }

      if (filter.getUserId() != null) {
         predicates.add(criteriaBuilder.equal(root.get("userId"), filter.getUserId()));
      }

      if (filter.getBookingDate() != null) {
         predicates.add(criteriaBuilder.like(root.get("bookingDate"), "%" + filter.getBookingDate() + "%"));
      }

      if (filter.getPickUpDate() != null) {
         predicates.add(criteriaBuilder.like(root.get("pickUpDate"), "%" + filter.getPickUpDate() + "%"));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
   }
}
