package com.example.backend.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Entity @Table(name="reviews",
  uniqueConstraints = @UniqueConstraint(columnNames={"customerEmail","restaurantId","productId"}))
public class Review {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length=120)
  private String customerEmail;


  private Long restaurantId;
  private Long productId;

  @NotNull @Min(1) @Max(5)
  private Integer rating;

  @Size(max=1000)
  private String comment;

  @Column(nullable=false)
  private Instant createdAt = Instant.now();

  // getters/setters
  public Long getId(){return id;}
  public String getCustomerEmail(){return customerEmail;}
  public void setCustomerEmail(String e){this.customerEmail=e;}
  public Long getRestaurantId(){return restaurantId;}
  public void setRestaurantId(Long r){this.restaurantId=r;}
  public Long getProductId(){return productId;}
  public void setProductId(Long p){this.productId=p;}
  public Integer getRating(){return rating;}
  public void setRating(Integer rating){this.rating=rating;}
  public String getComment(){return comment;}
  public void setComment(String c){this.comment=c;}
  public Instant getCreatedAt(){return createdAt;}
}
