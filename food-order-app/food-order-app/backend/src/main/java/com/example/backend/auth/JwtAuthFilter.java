package com.example.backend.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  public JwtAuthFilter(JwtService jwtService){ this.jwtService = jwtService; }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String auth = req.getHeader("Authorization");
    if (auth != null && auth.startsWith("Bearer ")) {
      try {
        String token = auth.substring(7);
        Claims c = jwtService.parse(token);
        String role = (String) c.get("role");
        var authToken = new UsernamePasswordAuthenticationToken(
            c.get("email"), null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } catch (Exception ignored) {}
    }
    chain.doFilter(req, res);
  }
}
