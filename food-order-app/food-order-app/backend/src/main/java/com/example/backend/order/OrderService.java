package com.example.backend.order;

import com.example.backend.common.SecurityUtils;
import com.example.backend.restaurant.Product;
import com.example.backend.restaurant.ProductRepository;
import com.example.backend.restaurant.RestaurantRepository;
import com.example.backend.order.dto.CreateOrderRequest;
import com.example.backend.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
  private final OrderRepository orders;
  private final ProductRepository products;
  private final RestaurantRepository restaurants;

  public OrderService(OrderRepository orders, ProductRepository products, RestaurantRepository restaurants){
    this.orders = orders; this.products = products; this.restaurants = restaurants;
  }

  public List<Order> myOrders(){
    return orders.findByCustomerEmailOrderByIdDesc(SecurityUtils.currentEmail());
  }

  public List<Order> forRestaurant(Long restaurantId){
    return orders.findByRestaurantIdOrderByIdDesc(restaurantId);
  }

  @Transactional
  public Order create(CreateOrderRequest req){

    var r = restaurants.findById(req.getRestaurantId()).orElse(null);
    if(r == null) throw new IllegalArgumentException("Restaurant not found");


    List<Product> items = new ArrayList<>();
    for(var it : req.getItems()){
      Product p = products.findById(it.getProductId()).orElse(null);
      if(p == null || !p.getRestaurantId().equals(req.getRestaurantId()))
        throw new IllegalArgumentException("Product invalid: "+it.getProductId());
      if(p.getStock() < it.getQuantity())
        throw new IllegalArgumentException("Insufficient stock for: "+p.getName());
      items.add(p);
    }


    Order o = new Order();
    o.setRestaurantId(req.getRestaurantId());
    o.setCustomerEmail(SecurityUtils.currentEmail());
    BigDecimal total = BigDecimal.ZERO;

    for(int i=0;i<req.getItems().size();i++){
      var in = req.getItems().get(i);
      var p  = items.get(i);
      var lineTotal = p.getPrice().multiply(new BigDecimal(in.getQuantity()));

      OrderItem oi = new OrderItem();
      oi.setOrder(o);
      oi.setProductId(p.getId());
      oi.setName(p.getName());
      oi.setUnitPrice(p.getPrice());
      oi.setQuantity(in.getQuantity());
      oi.setLineTotal(lineTotal);
      o.getItems().add(oi);

      total = total.add(lineTotal);

      p.setStock(p.getStock() - in.getQuantity());
      products.save(p);
    }

    o.setTotal(total);
    return orders.save(o);
  }

  @Transactional
  public Order updateStatus(Long orderId, OrderStatus status, boolean permitted){
    if(!permitted) return null;
    var o = orders.findById(orderId).orElse(null);
    if(o == null) return null;

    if(o.getStatus() == OrderStatus.DELIVERED) return o;
    o.setStatus(status);
    return orders.save(o);
  }

  public static OrderResponse map(Order o){
    return new OrderResponse(
      o.getId(), o.getRestaurantId(), o.getCustomerEmail(), o.getStatus(),
      o.getTotal(), o.getCreatedAt(),
      o.getItems().stream().map(i ->
        new OrderResponse.OrderItemResponse(i.getProductId(), i.getName(), i.getQuantity(), i.getUnitPrice(), i.getLineTotal())
      ).toList()
    );
  }

  public List<Order> allOrders() {
    return orders.findAll().stream()
            .sorted((a,b)->b.getId().compareTo(a.getId()))
            .toList();
  }
}
