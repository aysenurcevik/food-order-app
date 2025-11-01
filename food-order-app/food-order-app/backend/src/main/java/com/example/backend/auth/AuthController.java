package com.example.backend.auth;

import com.example.backend.auth.model.Role;
import com.example.backend.auth.model.User;
import com.example.backend.user.UserRepository;
import com.example.backend.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, UserService userService, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.userService = userService; this.encoder = encoder; this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String fullName = body.get("fullName");
        Role role = Role.valueOf(body.getOrDefault("role", "CUSTOMER"));

        if (users.existsByEmail(email))
            return ResponseEntity.badRequest().body(Map.of("error", "Email already used"));

        User u = userService.createUser(email, password, fullName, role);
        String token = jwt.generateToken(u.getId(), u.getEmail(), u.getFullName(), u.getRole());
        return ResponseEntity.ok(Map.of("token", token, "email", u.getEmail(), "role", u.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        User u = users.findByEmail(email).orElse(null);
        if (u == null || !encoder.matches(password, u.getPasswordHash()))
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));

        String token = jwt.generateToken(u.getId(), u.getEmail(), u.getFullName(), u.getRole());
        return ResponseEntity.ok(Map.of("token", token, "email", u.getEmail(), "role", u.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        String email = com.example.backend.common.SecurityUtils.currentEmail();
        if (email == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authenticated"));
        }

        var u = users.findByEmail(email).orElse(null);
        if (u == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "role", u.getRole().name()
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("newPassword");

        var user = users.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Email bulunamadı"));
        }

        user.setPasswordHash(encoder.encode(newPassword));
        users.save(user);

        return ResponseEntity.ok(Map.of("message", "Şifre başarıyla güncellendi"));
    }
    
}
