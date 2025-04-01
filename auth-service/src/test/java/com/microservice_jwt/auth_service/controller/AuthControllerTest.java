package com.microservice_jwt.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice_jwt.auth_service.DTO.LoginRequest;
import com.microservice_jwt.auth_service.DTO.RegisterRequest;
import com.microservice_jwt.auth_service.model.Role;
import com.microservice_jwt.auth_service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.MockConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService; // ✅ Now injected from MockConfig

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Define a static configuration class for mocks
    static class MockConfig {
        @Bean
        AuthService authService() {
            return Mockito.mock(AuthService.class);
        }
    }

    @Test
    @WithMockUser(roles = "USER") // ✅ Mock a user role
    void register_ShouldReturnSuccessMessage() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setEmail("newUser@example.com");
        request.setPassword("password");
        request.setRoles(Set.of(Role.USER));

        Mockito.when(authService.register(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anySet()))
                .thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))  // ✅ Include CSRF token
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @WithMockUser(roles = "USER") // ✅ Ensure a mock user is authenticated with proper roles
    void login_ShouldReturnToken_WhenValidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("user@example.com");
        request.setPassword("password");

        // Ensure that the mock authentication service responds as expected
        Mockito.when(authService.login(Mockito.anyString(), Mockito.anyString())).thenReturn("mockToken");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))  // ✅ Include CSRF token
                .andExpect(status().isOk())  // Expect 200 OK status
                .andExpect(content().json("{\"token\":\"mockToken\"}"));
    }
}
