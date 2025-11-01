package com.example.backend.search;

import com.example.backend.restaurant.*;
import com.example.backend.restaurant.dto.ProductResponse;
import com.example.backend.restaurant.dto.RestaurantResponse;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {
  private final RestaurantRepository restaurants;
  private final ProductRepository products;

  public SearchController(RestaurantRepository r, ProductRepository p){ this.restaurants=r; this.products=p; }

  @GetMapping("/restaurants")
  public Page<RestaurantResponse> restaurants(@RequestParam(required=false) String q,
                                              @RequestParam(required=false) Double minRating,
                                              @RequestParam(defaultValue="0") int page,
                                              @RequestParam(defaultValue="10") int size){
    Page<Restaurant> res = restaurants.search(q, minRating, PageRequest.of(page, size, Sort.by("rating").descending()));
    return res.map(r -> new RestaurantResponse(r.getId(), r.getName(), r.getLogoUrl(), r.getAddress(),
                                               r.getRating(), r.getDescription(), r.getOwnerEmail()));
  }

  @GetMapping("/products")
  public Page<ProductResponse> products(@RequestParam String q,
                                        @RequestParam(defaultValue="0") int page,
                                        @RequestParam(defaultValue="10") int size){
    return products.findByNameContainingIgnoreCase(q, PageRequest.of(page, size, Sort.by("id").descending()))
      .map(p -> new ProductResponse(p.getId(), p.getRestaurantId(), p.getName(),
                                    p.getPrice(), p.getDescription(), p.getStock(), p.getImageUrl()));
  }
}
