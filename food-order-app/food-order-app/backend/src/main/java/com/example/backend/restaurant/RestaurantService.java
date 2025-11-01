package com.example.backend.restaurant;

import com.example.backend.common.SecurityUtils;
import com.example.backend.restaurant.dto.RestaurantRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RestaurantService {
  private final RestaurantRepository repo;
  public RestaurantService(RestaurantRepository repo){ this.repo = repo; }

  public List<Restaurant> listAll(){ return repo.findAll(); }
  public Restaurant get(Long id){ return repo.findById(id).orElse(null); }
  public List<Restaurant> listMine(){ return repo.findByOwnerEmail(SecurityUtils.currentEmail()); }

  @Transactional
  public Restaurant create(RestaurantRequest req){
    Restaurant r = new Restaurant();
    r.setName(req.getName());
    r.setLogoUrl(req.getLogoUrl());
    r.setAddress(req.getAddress());
    r.setRating(req.getRating());
    r.setDescription(req.getDescription());
    r.setOwnerEmail(SecurityUtils.currentEmail());
    return repo.save(r);
  }

  @Transactional
  public Restaurant update(Long id, RestaurantRequest req, boolean isAdmin){
    Restaurant r = repo.findById(id).orElse(null);
    if (r == null) return null;
    String me = SecurityUtils.currentEmail();
    if (!isAdmin && !r.getOwnerEmail().equalsIgnoreCase(me)) return null; // yetkisiz
    r.setName(req.getName());
    r.setLogoUrl(req.getLogoUrl());
    r.setAddress(req.getAddress());
    r.setRating(req.getRating());
    r.setDescription(req.getDescription());
    return repo.save(r);
  }

  @Transactional
  public boolean delete(Long id, boolean isAdmin){
    Restaurant r = repo.findById(id).orElse(null);
    if (r == null) return false;
    String me = SecurityUtils.currentEmail();
    if (!isAdmin && !r.getOwnerEmail().equalsIgnoreCase(me)) return false;
    repo.delete(r);
    return true;
  }
}
