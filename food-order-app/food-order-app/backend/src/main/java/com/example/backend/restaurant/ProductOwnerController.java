package com.example.backend.restaurant;

import com.example.backend.common.SecurityUtils;
import com.example.backend.restaurant.dto.ProductRequest;
import com.example.backend.restaurant.dto.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/restaurants/{restaurantId}/products")
public class ProductOwnerController {
  private final ProductService service;
  public ProductOwnerController(ProductService service){ this.service = service; }

  private boolean isAdmin(){ return "ADMIN".equals(SecurityUtils.currentRole()); }
  private boolean isOwnerOrAdmin(){ 
    String r = SecurityUtils.currentRole(); 
    return "ADMIN".equals(r) || "RESTAURANT_OWNER".equals(r); 
  }

  @PostMapping
  public ResponseEntity<?> create(@PathVariable Long restaurantId, @Valid @RequestBody ProductRequest req){
    if(!isOwnerOrAdmin()) return ResponseEntity.status(403).build();
    var p = service.create(restaurantId, req, isAdmin());
    if(p==null) return ResponseEntity.status(403).body("Forbidden or Restaurant Not Found");
    return ResponseEntity.ok(new ProductResponse(p.getId(), p.getRestaurantId(), p.getName(),
      p.getPrice(), p.getDescription(), p.getStock(), p.getImageUrl()));
  }

  @PutMapping("/{productId}")
  public ResponseEntity<?> update(@PathVariable Long restaurantId, @PathVariable Long productId,
                                  @Valid @RequestBody ProductRequest req){
    if(!isOwnerOrAdmin()) return ResponseEntity.status(403).build();
    var p = service.update(restaurantId, productId, req, isAdmin());
    if(p==null) return ResponseEntity.status(403).body("Forbidden or Not Found");
    return ResponseEntity.ok(new ProductResponse(p.getId(), p.getRestaurantId(), p.getName(),
      p.getPrice(), p.getDescription(), p.getStock(), p.getImageUrl()));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> delete(@PathVariable Long restaurantId, @PathVariable Long productId){
    if(!isOwnerOrAdmin()) return ResponseEntity.status(403).build();
    boolean ok = service.delete(restaurantId, productId, isAdmin());
    if(!ok) return ResponseEntity.status(403).body("Forbidden or Not Found");
    return ResponseEntity.noContent().build();
  }
}
