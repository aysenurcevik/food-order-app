package com.example.backend.order.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class CreateOrderRequest {
  @NotNull
  private Long restaurantId;

  @NotEmpty
  private List<Item> items;

  public static class Item {
    @NotNull private Long productId;
    @NotNull @Min(1) private Integer quantity;

    public Long getProductId(){ return productId; }
    public void setProductId(Long productId){ this.productId = productId; }
    public Integer getQuantity(){ return quantity; }
    public void setQuantity(Integer quantity){ this.quantity = quantity; }
  }

  public Long getRestaurantId(){ return restaurantId; }
  public void setRestaurantId(Long restaurantId){ this.restaurantId = restaurantId; }
  public List<Item> getItems(){ return items; }
  public void setItems(List<Item> items){ this.items = items; }
}
