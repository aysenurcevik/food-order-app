package com.example.backend.review;

import com.example.backend.common.SecurityUtils;
import com.example.backend.order.OrderRepository;
import com.example.backend.restaurant.Restaurant;
import com.example.backend.restaurant.RestaurantRepository;
import com.example.backend.review.dto.ReviewRequest;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
  private final ReviewRepository reviews;
  private final OrderRepository orders;
  private final RestaurantRepository restaurants;

  public ReviewService(ReviewRepository reviews, OrderRepository orders, RestaurantRepository restaurants){
    this.reviews=reviews; this.orders=orders; this.restaurants=restaurants;
  }

  public Page<Review> listForRestaurant(Long restaurantId, int page, int size){
    return reviews.findByRestaurantId(restaurantId, PageRequest.of(page, size, Sort.by("id").descending()));
    // Ürün için benzer (controller’da kullanıyoruz)
  }

  @Transactional
  public Review create(ReviewRequest req){
    String me = SecurityUtils.currentEmail();
    if((req.getRestaurantId()==null) && (req.getProductId()==null))
      throw new IllegalArgumentException("restaurantId or productId required");

    // Satın alma doğrulaması (soft policy): restoran için restoran siparişi, ürün için ürün siparişi
    if(req.getRestaurantId()!=null && !orders.existsByCustomerEmailAndRestaurantId(me, req.getRestaurantId()))
      throw new IllegalArgumentException("Purchase required for restaurant review");
    if(req.getProductId()!=null && !orders.customerBoughtProduct(me, req.getProductId()))
      throw new IllegalArgumentException("Purchase required for product review");

    Review r = new Review();
    r.setCustomerEmail(me);
    r.setRestaurantId(req.getRestaurantId());
    r.setProductId(req.getProductId());
    r.setRating(req.getRating());
    r.setComment(req.getComment());
    Review saved = reviews.save(r);

    // Restoran ortalaması (basit): sadece restoran yorumlarında güncelle
    if(req.getRestaurantId()!=null){
      var page = reviews.findByRestaurantId(req.getRestaurantId(), PageRequest.of(0, Integer.MAX_VALUE));
      double avg = page.stream().mapToInt(Review::getRating).average().orElse(0.0);
      Restaurant rest = restaurants.findById(req.getRestaurantId()).orElse(null);
      if(rest!=null){ rest.setRating(Math.round(avg*10.0)/10.0); restaurants.save(rest); }
    }
    return saved;
  }

  @Transactional
  public boolean deleteMyReview(Long id){
    String me = SecurityUtils.currentEmail();
    var r = reviews.findById(id).orElse(null);
    if(r==null || !r.getCustomerEmail().equalsIgnoreCase(me)) return false;
    reviews.delete(r);
    return true;
  }
}
