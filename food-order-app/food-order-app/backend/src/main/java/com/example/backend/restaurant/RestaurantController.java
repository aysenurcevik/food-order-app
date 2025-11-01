package com.example.backend.restaurant;

import com.example.backend.common.SecurityUtils;
import com.example.backend.restaurant.dto.RestaurantRequest;
import com.example.backend.restaurant.dto.RestaurantResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class RestaurantController {
  private final RestaurantService service;
  public RestaurantController(RestaurantService service){ this.service = service; }

  // --- CUSTOMER/OWNER/ADMIN: listeleme & görüntüleme ---
  @GetMapping("/api/restaurants")
  public List<RestaurantResponse> all(){
    return service.listAll().stream()
      .map(r -> new RestaurantResponse(r.getId(), r.getName(), r.getLogoUrl(), r.getAddress(),
              r.getRating(), r.getDescription(), r.getOwnerEmail()))
      .toList();
  }

  @GetMapping("/api/restaurants/{id}")
  public ResponseEntity<?> byId(@PathVariable Long id){
    var r = service.get(id);
    if(r==null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(new RestaurantResponse(r.getId(), r.getName(), r.getLogoUrl(),
      r.getAddress(), r.getRating(), r.getDescription(), r.getOwnerEmail()));
  }

  // --- OWNER/ADMIN: kendi restoranlarını listele ---
  @GetMapping("/api/owner/restaurants/mine")
  public List<RestaurantResponse> mine(){
    return service.listMine().stream()
      .map(r -> new RestaurantResponse(r.getId(), r.getName(), r.getLogoUrl(), r.getAddress(),
        r.getRating(), r.getDescription(), r.getOwnerEmail()))
      .toList();
  }

  // --- OWNER/ADMIN: oluştur ---
  @PostMapping("/api/owner/restaurants")
  public ResponseEntity<?> create(@Valid @RequestBody RestaurantRequest req){
    String role = SecurityUtils.currentRole(); // ADMIN veya RESTAURANT_OWNER olmalı
    if(!"ADMIN".equals(role) && !"RESTAURANT_OWNER".equals(role))
      return ResponseEntity.status(403).body("Forbidden");
    var r = service.create(req);
    return ResponseEntity.ok(new RestaurantResponse(r.getId(), r.getName(), r.getLogoUrl(),
      r.getAddress(), r.getRating(), r.getDescription(), r.getOwnerEmail()));
  }

  // --- OWNER/ADMIN: güncelle (sahiplik kontrolü service'te) ---
  @PutMapping("/api/owner/restaurants/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest req){
    String role = SecurityUtils.currentRole();
    boolean isAdmin = "ADMIN".equals(role);
    if(!isAdmin && !"RESTAURANT_OWNER".equals(role)) return ResponseEntity.status(403).build();
    var r = service.update(id, req, isAdmin);
    if(r==null) return ResponseEntity.status(403).body("Forbidden or Not Found");
    return ResponseEntity.ok(new RestaurantResponse(r.getId(), r.getName(), r.getLogoUrl(),
      r.getAddress(), r.getRating(), r.getDescription(), r.getOwnerEmail()));
  }

  // --- OWNER/ADMIN: sil ---
  @DeleteMapping("/api/owner/restaurants/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    String role = SecurityUtils.currentRole();
    boolean isAdmin = "ADMIN".equals(role);
    if(!isAdmin && !"RESTAURANT_OWNER".equals(role)) return ResponseEntity.status(403).build();
    boolean ok = service.delete(id, isAdmin);
    if(!ok) return ResponseEntity.status(403).body("Forbidden or Not Found");
    return ResponseEntity.noContent().build();
  }

  // --- ADMIN: dilerse hepsini yönetmek için shortcut endpointler ---
  @DeleteMapping("/api/admin/restaurants/{id}")
  public ResponseEntity<?> adminDelete(@PathVariable Long id){
    boolean ok = service.delete(id, true);
    return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
