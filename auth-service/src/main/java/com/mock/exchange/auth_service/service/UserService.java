package com.mock.exchange.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.mock.exchange.auth_service.entity.User;
import com.mock.exchange.auth_service.repository.UserRepository;

public class UserService {
private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public User register(String email, String rawPassword) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Email already exists");
        });
        String encoded = passwordEncoder.encode(rawPassword);
        User toSave = new User(null, email, encoded);
        return userRepository.save(toSave);
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Bad credentials");
        }
        return jwtProvider.generateToken(user.getId(), user.getEmail());
    }
}
