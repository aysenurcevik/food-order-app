package com.example.backend.restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findByRestaurantId(Long restaurantId, Pageable pageable);
  Page<Product> findByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String q, Pageable pageable);
  Page<Product> findByNameContainingIgnoreCase(String q, Pageable p);
}
