package com.CommerceCore.repository;

import com.CommerceCore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoSpecification extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // will give findAll(Specification spec, Pageable pageable)
}
