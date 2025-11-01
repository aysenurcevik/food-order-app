package com.example.backend.order.dto;

import com.example.backend.order.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponse {
  public Long id;
  public Long restaurantId;
  public String customerEmail;
  public OrderStatus status;
  public BigDecimal total;
  public Instant createdAt;
  public List<OrderItemResponse> items;

  public OrderResponse(Long id, Long restaurantId, String customerEmail, OrderStatus status,
                       BigDecimal total, Instant createdAt, List<OrderItemResponse> items){
    this.id=id; this.restaurantId=restaurantId; this.customerEmail=customerEmail;
    this.status=status; this.total=total; this.createdAt=createdAt; this.items=items;
  }

  public static class OrderItemResponse {
    public Long productId; public String name; public Integer quantity;
    public java.math.BigDecimal unitPrice; public java.math.BigDecimal lineTotal;
    public OrderItemResponse(Long pid, String n, Integer q, java.math.BigDecimal up, java.math.BigDecimal lt){
      productId=pid; name=n; quantity=q; unitPrice=up; lineTotal=lt;
    }
  }
}
