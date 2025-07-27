package com.mock.exchange.auth_service.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
/**
 * Represents the JWT response containing the token and user details.
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token; // JWT token
    private String username; // Username of the authenticated user
    private String role; // Role of the user (e.g., ROLE_USER, ROLE_ADMIN)

    public JwtResponse(String token) {
        this.token = token;
    }
}
