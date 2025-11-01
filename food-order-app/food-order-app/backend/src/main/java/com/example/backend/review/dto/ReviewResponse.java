package com.example.backend.review.dto;

import java.time.Instant;

public class ReviewResponse {
  public Long id; public String customerEmail;
  public Long restaurantId; public Long productId;
  public Integer rating; public String comment;
  public Instant createdAt;

  public ReviewResponse(Long id,String e,Long r,Long p,Integer rating,String c,Instant t){
    this.id=id; this.customerEmail=e; this.restaurantId=r; this.productId=p; this.rating=rating; this.comment=c; this.createdAt=t;
  }
}
