package com.example.backend.restaurant.dto;

public class RestaurantResponse {
  public Long id;
  public String name;
  public String logoUrl;
  public String address;
  public Double rating;
  public String description;
  public String ownerEmail;

  public RestaurantResponse(Long id, String name, String logoUrl, String address,
                            Double rating, String description, String ownerEmail){
    this.id=id; this.name=name; this.logoUrl=logoUrl; this.address=address;
    this.rating=rating; this.description=description; this.ownerEmail=ownerEmail;
  }
}
