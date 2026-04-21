package com.CommerceCore.repository;

import com.CommerceCore.entity.CartItem;
import com.CommerceCore.entity.Product;
import com.CommerceCore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
