package com.example.backend.restaurant.dto;

import java.math.BigDecimal;

public class ProductResponse {
  public Long id;
  public Long restaurantId;
  public String name;
  public BigDecimal price;
  public String description;
  public Integer stock;
  public String imageUrl;

  public ProductResponse(Long id, Long restaurantId, String name, BigDecimal price,
                         String description, Integer stock, String imageUrl) {
    this.id=id; this.restaurantId=restaurantId; this.name=name; this.price=price;
    this.description=description; this.stock=stock; this.imageUrl=imageUrl;
  }
}
