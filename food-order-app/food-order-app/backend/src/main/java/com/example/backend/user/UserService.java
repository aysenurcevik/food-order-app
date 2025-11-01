package com.example.backend.user;

import com.example.backend.auth.model.Role;
import com.example.backend.auth.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo; this.encoder = encoder;
    }

    public User createUser(String email, String rawPassword, String fullName, Role role) {
        User u = new User();
        u.setEmail(email.toLowerCase());
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setFullName(fullName);
        u.setRole(role);
        return repo.save(u);
    }
}
