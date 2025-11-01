package com.example.backend.restaurant;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
  List<Restaurant> findByOwnerEmail(String ownerEmail);

  @Query("""
    select r from Restaurant r
     where (:q is null or lower(r.name) like lower(concat('%',:q,'%')))
       and (:minRating is null or r.rating >= :minRating)
  """)
  Page<Restaurant> search(String q, Double minRating, Pageable p);
}
