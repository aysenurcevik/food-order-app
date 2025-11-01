package com.example.backend.restaurant.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest {
  @NotBlank @Size(max=140)
  private String name;

  @NotNull @DecimalMin("0.0")
  private BigDecimal price;

  @Size(max=500)
  private String description;

  @NotNull @Min(0)
  private Integer stock;

  @Size(max=255)
  private String imageUrl;

  // getters/setters
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
