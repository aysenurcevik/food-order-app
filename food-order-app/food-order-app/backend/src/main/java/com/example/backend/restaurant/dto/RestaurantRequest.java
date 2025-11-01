package com.example.backend.restaurant.dto;

import jakarta.validation.constraints.*;

public class RestaurantRequest {
  @NotBlank @Size(max=120)
  private String name;
  @Size(max=255)
  private String logoUrl;
  @Size(max=255)
  private String address;
  @DecimalMin("0.0") @DecimalMax("5.0")
  private Double rating = 0.0;
  @Size(max=500)
  private String description;

  // getters/setters
  public String getName(){ return name; }
  public void setName(String name){ this.name = name; }
  public String getLogoUrl(){ return logoUrl; }
  public void setLogoUrl(String logoUrl){ this.logoUrl = logoUrl; }
  public String getAddress(){ return address; }
  public void setAddress(String address){ this.address = address; }
  public Double getRating(){ return rating; }
  public void setRating(Double rating){ this.rating = rating; }
  public String getDescription(){ return description; }
  public void setDescription(String description){ this.description = description; }
}
