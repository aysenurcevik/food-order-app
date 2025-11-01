package com.example.backend.restaurant;

import com.example.backend.common.SecurityUtils;
import com.example.backend.restaurant.dto.ProductRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
  private final ProductRepository products;
  private final RestaurantRepository restaurants;

  public ProductService(ProductRepository products, RestaurantRepository restaurants) {
    this.products = products; this.restaurants = restaurants;
  }

  // Public list/search with pagination
  public Page<Product> listPublic(Long restaurantId, String q, int page, int size) {
    Pageable p = PageRequest.of(Math.max(page,0), Math.min(Math.max(size,1), 100), Sort.by("id").descending());
    if (q == null || q.isBlank()) return products.findByRestaurantId(restaurantId, p);
    return products.findByRestaurantIdAndNameContainingIgnoreCase(restaurantId, q.trim(), p);
  }

  private boolean canManage(Long restaurantId, boolean isAdmin) {
    var r = restaurants.findById(restaurantId).orElse(null);
    if (r == null) return false;
    if (isAdmin) return true;
    String me = SecurityUtils.currentEmail();
    return me != null && me.equalsIgnoreCase(r.getOwnerEmail());
  }

  @Transactional
  public Product create(Long restaurantId, ProductRequest req, boolean isAdmin) {
    if (!canManage(restaurantId, isAdmin)) return null;
    Product p = new Product();
    p.setRestaurantId(restaurantId);
    p.setName(req.getName());
    p.setPrice(req.getPrice());
    p.setDescription(req.getDescription());
    p.setStock(req.getStock());
    p.setImageUrl(req.getImageUrl());
    return products.save(p);
  }

  @Transactional
  public Product update(Long restaurantId, Long productId, ProductRequest req, boolean isAdmin) {
    if (!canManage(restaurantId, isAdmin)) return null;
    var p = products.findById(productId).orElse(null);
    if (p == null || !p.getRestaurantId().equals(restaurantId)) return null;
    p.setName(req.getName());
    p.setPrice(req.getPrice());
    p.setDescription(req.getDescription());
    p.setStock(req.getStock());
    p.setImageUrl(req.getImageUrl());
    return products.save(p);
  }

  @Transactional
  public boolean delete(Long restaurantId, Long productId, boolean isAdmin) {
    if (!canManage(restaurantId, isAdmin)) return false;
    var p = products.findById(productId).orElse(null);
    if (p == null || !p.getRestaurantId().equals(restaurantId)) return false;
    products.delete(p);
    return true;
  }
}
