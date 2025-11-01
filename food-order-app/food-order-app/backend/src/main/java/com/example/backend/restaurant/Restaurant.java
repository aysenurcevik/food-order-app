package com.example.backend.restaurant;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="restaurants")
public class Restaurant {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  // Basitlik için ilişkisel yerine owner email/id saklıyoruz
  @Column(nullable = false, length = 120)
  private String ownerEmail;

  // getters/setters
  public Long getId(){ return id; }
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
  public String getOwnerEmail(){ return ownerEmail; }
  public void setOwnerEmail(String ownerEmail){ this.ownerEmail = ownerEmail; }
}
