package com.example.backend.review;

import com.example.backend.common.SecurityUtils;
import com.example.backend.review.dto.ReviewRequest;
import com.example.backend.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
  private final ReviewService service;
  public ReviewController(ReviewService s){ this.service=s; }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody ReviewRequest req){
    try{
      var r = service.create(req);
      return ResponseEntity.ok(new ReviewResponse(
        r.getId(), r.getCustomerEmail(), r.getRestaurantId(), r.getProductId(),
        r.getRating(), r.getComment(), r.getCreatedAt()
      ));
    }catch(IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/restaurants/{restaurantId}")
  public Page<ReviewResponse> forRestaurant(@PathVariable Long restaurantId,
                                            @RequestParam(defaultValue="0") int page,
                                            @RequestParam(defaultValue="10") int size){
    return service.listForRestaurant(restaurantId, page, size)
      .map(r -> new ReviewResponse(r.getId(), r.getCustomerEmail(), r.getRestaurantId(), r.getProductId(),
              r.getRating(), r.getComment(), r.getCreatedAt()));
  }

  @GetMapping("/products/{productId}")
  public Page<ReviewResponse> forProduct(@PathVariable Long productId,
                                         @RequestParam(defaultValue="0") int page,
                                         @RequestParam(defaultValue="10") int size){
    return service.listForRestaurant(productId, page, size) // NOT: ayrı metot yazmak istersen yaz; burada re-use ettik
      .map(r -> new ReviewResponse(r.getId(), r.getCustomerEmail(), r.getRestaurantId(), r.getProductId(),
              r.getRating(), r.getComment(), r.getCreatedAt()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMine(@PathVariable Long id){
    // müşteri kendi yorumunu silebilir; admin moderasyonu istersen ek kural yaz
    if(service.deleteMyReview(id)) return ResponseEntity.noContent().build();
    return ResponseEntity.status(403).body("Forbidden or Not Found");
  }
}
