package com.CommerceCore.entity;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterProduct(
            String keyword,
            String category,
            Double minPrice,
            Double maxPrice
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // keyword search
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + keyword.toLowerCase() + "%"
                        )
                );
            }
            // category filter
            if (category != null && !category.isEmpty()) {
                predicates.add(
                        cb.equal(
                                cb.lower(root.get("category").get("name")),
                                category.toLowerCase()
                        )
                );
            }
            // min price
            if (minPrice != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice)
                );
            }
            // max price
            if (maxPrice != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice)
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
