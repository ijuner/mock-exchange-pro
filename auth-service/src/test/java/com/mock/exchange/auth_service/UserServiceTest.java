package com.mock.exchange.auth_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mock.exchange.auth_service.entity.User;
import com.mock.exchange.auth_service.repository.UserRepository;
import com.mock.exchange.auth_service.service.JwtProvider;
import com.mock.exchange.auth_service.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtProvider jwtProvider;

    @InjectMocks UserService userService;
    @Captor ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, jwtProvider);
    }

    @Nested
    @DisplayName("register()")
    class RegisterTests{

        
        @Test
        @DisplayName("should create user when email not exists")
        void register_ok() {
            // arrange
            String email = "alice@example.com";
            String rawPwd = "P@ssw0rd!";
            String encoded = "$bcrypt$...";

            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            when(passwordEncoder.encode(rawPwd)).thenReturn(encoded);
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> {
                        User u = invocation.getArgument(0);
                        return new User(1L, u.email(), u.password()); // 模拟DB回写ID
                    });

            // act
            User saved = userService.register(email, rawPwd);

            // assert
            assertThat(saved.id()).isEqualTo(1L);
            assertThat(saved.email()).isEqualTo(email);
            assertThat(saved.password()).isEqualTo(encoded);

            // verify interactions & captured value
            verify(userRepository).findByEmail(email);
            verify(passwordEncoder).encode(rawPwd);
            verify(userRepository).save(userCaptor.capture());

            User toSave = userCaptor.getValue();
            assertThat(toSave.id()).isNull(); // 保存前 id 应该是 null
            assertThat(toSave.password()).isEqualTo(encoded);


        }


        @Test
        @DisplayName("should fail when email already exists")
        void register_dupEmail() {
            String email = "dup@example.com";

            when(userRepository.findByEmail(email))
                    .thenReturn(Optional.of(new User(9L, email, "x")));

            assertThatThrownBy(() -> userService.register(email, "any"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Email already exists");

            verify(userRepository).findByEmail(email);
            verify(userRepository, never()).save(any());
            verifyNoInteractions(passwordEncoder, jwtProvider);
        }


    }


@Nested
    @DisplayName("login()")
    class LoginTests {

        @Test
        @DisplayName("should return JWT when credentials correct")
        void login_ok() {
            String email = "bob@example.com";
            String rawPwd = "s3cret";
            String encoded = "$bcrypt$...";
            User bob = new User(2L, email, encoded);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(bob));
            when(passwordEncoder.matches(rawPwd, encoded)).thenReturn(true);
            when(jwtProvider.generateToken(2L, email)).thenReturn("jwt-token-abc");

            String jwt = userService.login(email, rawPwd);

            assertThat(jwt).isEqualTo("jwt-token-abc");

            verify(userRepository).findByEmail(email);
            verify(passwordEncoder).matches(rawPwd, encoded);
            verify(jwtProvider).generateToken(2L, email);
        }

        @Test
        @DisplayName("should fail when user not found")
        void login_userNotFound() {
            when(userRepository.findByEmail("absent@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.login("absent@example.com", "x"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User not found");

            verify(userRepository).findByEmail("absent@example.com");
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(passwordEncoder, jwtProvider);
        }

        @Test
        @DisplayName("should fail when password mismatched")
        void login_badPassword() {
            String email = "eve@example.com";
            String encoded = "$bcrypt$...";
            User eve = new User(3L, email, encoded);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(eve));
            when(passwordEncoder.matches("wrong", encoded)).thenReturn(false);

            assertThatThrownBy(() -> userService.login(email, "wrong"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Bad credentials");

            verify(passwordEncoder).matches("wrong", encoded);
            verify(jwtProvider, never()).generateToken(any(), any());
        }
    }



}
