package com.example.backend.config;

import com.example.backend.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;
  public SecurityConfig(JwtAuthFilter jwtAuthFilter){ this.jwtAuthFilter = jwtAuthFilter; }

  @Bean
  PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      // CORS + CSRF
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .csrf(csrf -> csrf.disable())
      // Oturum ayarları
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      // Yetkilendirme kuralları
      .authorizeHttpRequests(auth -> auth
      .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/reset-password", "/h2-console/**").permitAll()
      .requestMatchers("/api/admin/**").hasRole("ADMIN")
      .requestMatchers("/api/owner/**").hasAnyRole("RESTAURANT_OWNER","ADMIN")
      .requestMatchers("/api/auth/me").authenticated()
      .requestMatchers("/api/restaurants/**").permitAll()
      .requestMatchers("/api/orders/**").authenticated()
      .requestMatchers("/api/reviews/**").authenticated()
      .requestMatchers("/api/search/**").permitAll()
      .anyRequest().authenticated()
    );

    // H2 console için frameOptions devre dışı
    http.headers(h -> h.frameOptions(f -> f.disable()));

    // JWT filtresini ekle
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // 🔹 CORS yapılandırması
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173")); // frontend adresi
    configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(List.of("Authorization","Content-Type", "Accept"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
