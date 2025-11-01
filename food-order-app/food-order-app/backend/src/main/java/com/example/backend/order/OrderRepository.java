package com.example.backend.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByCustomerEmailOrderByIdDesc(String customerEmail);
  List<Order> findByRestaurantIdOrderByIdDesc(Long restaurantId);

  boolean existsByCustomerEmailAndRestaurantId(String customerEmail, Long restaurantId);

  @Query("""
    select (count(oi)>0) from Order o
      join o.items oi
     where o.customerEmail = :email and oi.productId = :productId
  """)
  boolean customerBoughtProduct(String email, Long productId);
}
