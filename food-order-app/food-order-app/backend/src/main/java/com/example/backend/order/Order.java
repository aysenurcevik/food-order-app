package com.example.backend.order;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="orders")
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private Long restaurantId;

  @Column(nullable=false, length=120)
  private String customerEmail;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private OrderStatus status = OrderStatus.PREPARING;

  @Column(nullable=false)
  private BigDecimal total = BigDecimal.ZERO;

  @Column(nullable=false)
  private Instant createdAt = Instant.now();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<OrderItem> items = new ArrayList<>();

  // getters/setters
  public Long getId(){ return id; }
  public Long getRestaurantId(){ return restaurantId; }
  public void setRestaurantId(Long restaurantId){ this.restaurantId = restaurantId; }
  public String getCustomerEmail(){ return customerEmail; }
  public void setCustomerEmail(String customerEmail){ this.customerEmail = customerEmail; }
  public OrderStatus getStatus(){ return status; }
  public void setStatus(OrderStatus status){ this.status = status; }
  public BigDecimal getTotal(){ return total; }
  public void setTotal(BigDecimal total){ this.total = total; }
  public Instant getCreatedAt(){ return createdAt; }
  public List<OrderItem> getItems(){ return items; }
}
