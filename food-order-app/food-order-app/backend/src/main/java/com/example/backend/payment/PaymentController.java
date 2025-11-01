package com.example.backend.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
  private final Random rnd = new Random();
  @PostMapping("/simulate")
  public ResponseEntity<?> simulate(@RequestBody Map<String,Object> body){
    //burayı silecem %80 başarı ile simüle ettim değişebilir
    boolean paid = rnd.nextDouble() < 0.8;
    return ResponseEntity.ok(Map.of(
      "status", paid ? "PAID" : "FAILED",
      "txId", "TX-" + Math.abs(rnd.nextLong())
    ));
  }
}
