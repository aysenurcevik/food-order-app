package com.example.backend.review.dto;

import jakarta.validation.constraints.*;

public class ReviewRequest {
  private Long restaurantId;
  private Long productId;

  @NotNull @Min(1) @Max(5)
  private Integer rating;

  @Size(max=1000)
  private String comment;

  public Long getRestaurantId(){return restaurantId;}
  public void setRestaurantId(Long x){this.restaurantId=x;}
  public Long getProductId(){return productId;}
  public void setProductId(Long x){this.productId=x;}
  public Integer getRating(){return rating;}
  public void setRating(Integer r){this.rating=r;}
  public String getComment(){return comment;}
  public void setComment(String c){this.comment=c;}
}
