package com.example.backend.restaurant;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private Long restaurantId;

  @NotBlank @Size(max=140)
  private String name;

  @NotNull @DecimalMin("0.0")
  private BigDecimal price;

  @Size(max=500)
  private String description;

  @NotNull @Min(0)
  private Integer stock = 0;

  @Size(max=255)
  private String imageUrl;

  // getters/setters
  public Long getId(){ return id; }
  public Long getRestaurantId(){ return restaurantId; }
  public void setRestaurantId(Long restaurantId){ this.restaurantId = restaurantId; }
  public String getName(){ return name; }
  public void setName(String name){ this.name = name; }
  public BigDecimal getPrice(){ return price; }
  public void setPrice(BigDecimal price){ this.price = price; }
  public String getDescription(){ return description; }
  public void setDescription(String description){ this.description = description; }
  public Integer getStock(){ return stock; }
  public void setStock(Integer stock){ this.stock = stock; }
  public String getImageUrl(){ return imageUrl; }
  public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }
}
