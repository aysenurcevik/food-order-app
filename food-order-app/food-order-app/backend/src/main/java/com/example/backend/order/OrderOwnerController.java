package com.example.backend.order;

import com.example.backend.common.SecurityUtils;
import com.example.backend.order.dto.OrderResponse;
import com.example.backend.order.dto.UpdateStatusRequest;
import com.example.backend.restaurant.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/owner/orders")
public class OrderOwnerController {
  private final OrderService service;
  private final RestaurantRepository restaurants;
  public OrderOwnerController(OrderService service, RestaurantRepository restaurants){
    this.service = service; this.restaurants = restaurants;
  }

  private boolean isAdmin(){ return "ADMIN".equals(SecurityUtils.currentRole()); }
  private boolean owns(Long restaurantId){
    if(isAdmin()) return true;
    var r = restaurants.findById(restaurantId).orElse(null);
    return r != null && r.getOwnerEmail().equalsIgnoreCase(SecurityUtils.currentEmail());
  }

  @GetMapping("/{restaurantId}")
  public ResponseEntity<?> list(@PathVariable Long restaurantId){
    if(!owns(restaurantId)) return ResponseEntity.status(403).build();
    List<OrderResponse> out = service.forRestaurant(restaurantId).stream().map(OrderService::map).toList();
    return ResponseEntity.ok(out);
  }

  @PatchMapping("/{orderId}/status")
  public ResponseEntity<?> update(@PathVariable Long orderId, @Valid @RequestBody UpdateStatusRequest req){
    boolean permitted = "ADMIN".equals(SecurityUtils.currentRole()) || "RESTAURANT_OWNER".equals(SecurityUtils.currentRole());
    var o = service.updateStatus(orderId, req.getStatus(), permitted);
    if(o == null) return ResponseEntity.status(403).body("Forbidden or Not Found");
    return ResponseEntity.ok(OrderService.map(o));
  }
}
