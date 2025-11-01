package com.example.backend.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByRestaurantId(Long restaurantId, Pageable p);
  Page<Review> findByProductId(Long productId, Pageable p);
  boolean existsByCustomerEmailAndRestaurantId(String email, Long restaurantId);
  boolean existsByCustomerEmailAndProductId(String email, Long productId);
}
