package com.example.backend.order;

import com.example.backend.common.SecurityUtils;
import com.example.backend.order.dto.CreateOrderRequest;
import com.example.backend.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService service;
  public OrderController(OrderService service){ this.service = service; }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody CreateOrderRequest req){
    try{
      var o = service.create(req);
      return ResponseEntity.ok(OrderService.map(o));
    }catch(IllegalArgumentException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/my")
  public List<OrderResponse> my(){
    return service.myOrders().stream().map(OrderService::map).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> byId(@PathVariable Long id){
    // basit görünüm izni: customer kendi siparişine, owner/admin kendi restoranına erişir
    var list = service.myOrders().stream().filter(o -> o.getId().equals(id)).findFirst();
    return list.<ResponseEntity<?>>map(o -> ResponseEntity.ok(OrderService.map(o)))
            .orElseGet(() -> ResponseEntity.status(403).body("Forbidden"));
  }

  @GetMapping("/admin/all")
  public ResponseEntity<?> allOrders() {
    String role = SecurityUtils.currentRole();
    if (!"ADMIN".equals(role)) {
        return ResponseEntity.status(403).body("Forbidden");
    }
    var list = service.allOrders().stream().map(OrderService::map).toList();
    return ResponseEntity.ok(list);
  }
}