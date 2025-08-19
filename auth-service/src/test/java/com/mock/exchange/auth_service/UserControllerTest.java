package com.mock.exchange.auth_service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.exchange.auth_service.controller.AuthController;
import com.mock.exchange.auth_service.dto.LoginRequest;
import com.mock.exchange.auth_service.dto.RegisterRequest;
import com.mock.exchange.auth_service.service.UserService;

@WebMvcTest(controllers = AuthController.class )
@AutoConfigureMockMvc(addFilters = false)
@Import(RestClientException.class)
public class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean UserService userService;

    @Test
    @DisplayName("Post /api/auth/register ->200, returns id & email")
    void register_ok() throws Exception {
        var req = new RegisterRequest("alice@example.com", "P@ssw0rd!");
        when(userService.register(req.email(), req.password()))
                .thenReturn(new User(1L, req.email(), "$bcrypt$..."));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        verify(userService).register("alice@example.com", "P@ssw0rd!");
    }

    @Test
    @DisplayName("POST /api/auth/register -> 400 when email exists")
    void register_dupEmail() throws Exception {
        var req = new RegisterRequest("dup@example.com", "whatever1");
        when(userService.register(req.email(), req.password()))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }

    @Test
    @DisplayName("POST /api/auth/register -> 400 for invalid payload")
    void register_invalidPayload() throws Exception {
        // 空密码 & 非法邮箱，触发 @Valid
        var bad = """
                  {"email":"not-an-email","password":""}
                  """;
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bad))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    @DisplayName("POST /api/auth/login -> 200, returns JWT")
    void login_ok() throws Exception {
        var req = new LoginRequest("bob@example.com", "s3cret");
        when(userService.login(req.email(), req.password()))
                .thenReturn("jwt-abc");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-abc"));

        verify(userService).login("bob@example.com", "s3cret");
    }

    @Test
    @DisplayName("POST /api/auth/login -> 400 for bad credentials")
    void login_badCreds() throws Exception {
        var req = new LoginRequest("eve@example.com", "wrong");
        when(userService.login(req.email(), req.password()))
                .thenThrow(new IllegalArgumentException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad credentials"));
    }

  // 参数化测试：弱口令集合
    @ParameterizedTest
    @ValueSource(strings = {"12345", "abcdef", "123456", "password", "qwerty"})
    @DisplayName("POST /api/auth/register -> 400 for weak passwords by @Size(min=6) or custom rule")
    void register_weakPasswords(String weak) throws Exception {
        var req = new RegisterRequest("weak@example.com", weak);
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }


}
