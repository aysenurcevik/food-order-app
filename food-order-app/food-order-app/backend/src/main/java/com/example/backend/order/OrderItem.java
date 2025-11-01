package com.example.backend.order;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="order_items")
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional=false) @JoinColumn(name="order_id")
  private Order order;

  @Column(nullable=false)
  private Long productId;

  @Column(nullable=false, length=140)
  private String name;

  @Column(nullable=false)
  private BigDecimal unitPrice;

  @Column(nullable=false)
  private Integer quantity;

  @Column(nullable=false)
  private BigDecimal lineTotal;

  // getters/setters
  public Long getId(){ return id; }
  public Order getOrder(){ return order; }
  public void setOrder(Order order){ this.order = order; }
  public Long getProductId(){ return productId; }
  public void setProductId(Long productId){ this.productId = productId; }
  public String getName(){ return name; }
  public void setName(String name){ this.name = name; }
  public BigDecimal getUnitPrice(){ return unitPrice; }
  public void setUnitPrice(BigDecimal unitPrice){ this.unitPrice = unitPrice; }
  public Integer getQuantity(){ return quantity; }
  public void setQuantity(Integer quantity){ this.quantity = quantity; }
  public BigDecimal getLineTotal(){ return lineTotal; }
  public void setLineTotal(BigDecimal lineTotal){ this.lineTotal = lineTotal; }
}
