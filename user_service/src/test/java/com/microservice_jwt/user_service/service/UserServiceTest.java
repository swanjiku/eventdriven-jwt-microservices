package com.microservice_jwt.user_service.service;

import com.microservice_jwt.user_service.model.Role;
import com.microservice_jwt.user_service.model.User;
import com.microservice_jwt.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private StreamOperations<String, Object, Object> streamOperations;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForStream()).thenReturn(streamOperations);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<User> mockUsers = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() {
        User user = new User();
        user.setId("123");
        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById("123");

        assertTrue(result.isPresent());
        assertEquals("123", result.get().getId());
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void updateUser_shouldUpdateFields_andPublishToRedis() {
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setUsername("oldUser");
        existingUser.setEmail("old@example.com");

        User updatedUser = new User();
        updatedUser.setUsername("newUser");
        updatedUser.setEmail("new@example.com");
        updatedUser.setPassword("newpass");

        mockAdminAuthentication();

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser("1", updatedUser);

        assertEquals("newUser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("encodedPass", result.getPassword());
        verify(streamOperations, times(1)).add(any(ObjectRecord.class));
    }

    @Test
    void updatePassword_shouldEncodePassword_andPublishToRedis() {
        User user = new User();
        user.setId("1");
        user.setUsername("john");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNew");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        userService.updatePassword("1", "newpass");

        assertEquals("encodedNew", user.getPassword());
        verify(streamOperations).add(any(ObjectRecord.class));
    }

    @Test
    void partialUpdateUser_shouldUpdateFieldsCorrectly() {
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setUsername("old");
        existingUser.setEmail("old@example.com");

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", "newName");
        updates.put("email", "new@example.com");
        updates.put("password", "newPass");

        mockAdminAuthentication();

        when(passwordEncoder.encode("newPass")).thenReturn("encoded");
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.partialUpdateUser("1", updates);

        assertEquals("newName", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("encoded", result.getPassword());
    }

    @Test
    void deleteUser_shouldRemoveUser_whenExists() {
        when(userRepository.existsById("123")).thenReturn(true);
        userService.deleteUser("123");
        verify(userRepository).deleteById("123");
    }

    @Test
    void deleteUser_shouldThrow_whenUserNotFound() {
        when(userRepository.existsById("999")).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser("999"));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void updateUser_shouldThrow_whenUnauthorizedToChangeRoles() {
        User existingUser = new User();
        existingUser.setId("1");

        User updatedUser = new User();
        updatedUser.setRoles(Set.of(Role.ADMIN));

        mockUserWithoutAdminRole();

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));

        assertThrows(RuntimeException.class, () -> userService.updateUser("1", updatedUser));
    }

    // --- Helpers for mocking security context ---
    private void mockAdminAuthentication() {
        var auth = new TestingAuthenticationToken(
                "admin", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void mockUserWithoutAdminRole() {
        var auth = new TestingAuthenticationToken(
                "user", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
