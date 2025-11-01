package com.example.backend.restaurant;

import com.example.backend.restaurant.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/products")
public class ProductPublicController {
  private final ProductService service;
  public ProductPublicController(ProductService service){ this.service = service; }

  @GetMapping
  public Page<ProductResponse> list(@PathVariable Long restaurantId,
                                    @RequestParam(required=false) String q,
                                    @RequestParam(defaultValue="0") int page,
                                    @RequestParam(defaultValue="10") int size) {
    return service.listPublic(restaurantId, q, page, size)
      .map(p -> new ProductResponse(p.getId(), p.getRestaurantId(), p.getName(),
              p.getPrice(), p.getDescription(), p.getStock(), p.getImageUrl()));
  }
}