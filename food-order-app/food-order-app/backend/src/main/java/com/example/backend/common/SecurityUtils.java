package com.example.backend.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
  public static String currentEmail(){
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    return a == null ? null : (String)a.getPrincipal();
  }
  public static String currentRole(){
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    if(a == null || a.getAuthorities()==null || a.getAuthorities().isEmpty()) return null;
    // "ROLE_ADMIN" -> "ADMIN"
    String auth = a.getAuthorities().iterator().next().getAuthority();
    return auth.startsWith("ROLE_") ? auth.substring(5) : auth;
  }
}
