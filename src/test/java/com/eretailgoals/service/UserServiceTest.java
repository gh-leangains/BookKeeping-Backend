package com.eretailgoals.service;

import com.eretailgoals.entity.User;
import com.eretailgoals.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 * Tests all business logic and edge cases for user management operations
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User testClient;
    private User testSupplier;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setUserType(User.UserType.CLIENT);
        testUser.setIsActive(true);

        testClient = new User();
        testClient.setId(2L);
        testClient.setFirstName("Jane");
        testClient.setLastName("Smith");
        testClient.setEmail("jane.smith@example.com");
        testClient.setUserType(User.UserType.CLIENT);
        testClient.setIsActive(true);

        testSupplier = new User();
        testSupplier.setId(3L);
        testSupplier.setFirstName("Bob");
        testSupplier.setLastName("Johnson");
        testSupplier.setEmail("bob.johnson@example.com");
        testSupplier.setUserType(User.UserType.SUPPLIER);
        testSupplier.setIsActive(true);
    }

    @Test
    void createUser_ValidUser_ShouldCreateSuccessfully() {
        // Given
        User newUser = new User("Alice", "Brown", "alice.brown@example.com", User.UserType.CLIENT);
        newUser.setPassword("password123");
        
        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        User createdUser = userService.createUser(newUser);

        // Then
        assertNotNull(createdUser);
        assertEquals("Alice", createdUser.getFirstName());
        assertEquals("alice.brown@example.com", createdUser.getEmail());
        assertTrue(createdUser.getIsActive());
        verify(userRepository).existsByEmail("alice.brown@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_DuplicateEmail_ShouldThrowException() {
        // Given
        User newUser = new User("Alice", "Brown", "john.doe@example.com", User.UserType.CLIENT);
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(newUser)
        );
        assertEquals("Email already exists: john.doe@example.com", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_DuplicateUsername_ShouldThrowException() {
        // Given
        User newUser = new User("Alice", "Brown", "alice.brown@example.com", User.UserType.CLIENT);
        newUser.setUsername("johndoe");
        
        when(userRepository.existsByEmail("alice.brown@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(newUser)
        );
        assertEquals("Username already exists: johndoe", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_InvalidData_ShouldThrowException() {
        // Given
        User invalidUser = new User();
        invalidUser.setEmail("test@example.com");
        // Missing required fields

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(invalidUser)
        );
        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    void getUserById_ExistingUser_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.getUserById(1L);

        // Then
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("John", foundUser.getFirstName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_NonExistingUser_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.getUserById(999L)
        );
        assertEquals("User not found with id: 999", exception.getMessage());
    }

    @Test
    void updateUser_ValidUpdate_ShouldUpdateSuccessfully() {
        // Given
        User updateData = new User();
        updateData.setFirstName("Johnny");
        updateData.setLastName("Doe");
        updateData.setEmail("johnny.doe@example.com");
        updateData.setUserType(User.UserType.CLIENT);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("johnny.doe@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User updatedUser = userService.updateUser(1L, updateData);

        // Then
        assertNotNull(updatedUser);
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("johnny.doe@example.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void getAllClients_ShouldReturnOnlyClients() {
        // Given
        List<User> clients = Arrays.asList(testUser, testClient);
        when(userRepository.findAllClients()).thenReturn(clients);

        // When
        List<User> result = userService.getAllClients();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(user -> user.getUserType() == User.UserType.CLIENT));
        verify(userRepository).findAllClients();
    }

    @Test
    void getAllSuppliers_ShouldReturnOnlySuppliers() {
        // Given
        List<User> suppliers = Arrays.asList(testSupplier);
        when(userRepository.findAllSuppliers()).thenReturn(suppliers);

        // When
        List<User> result = userService.getAllSuppliers();

        // Then
        assertEquals(1, result.size());
        assertEquals(User.UserType.SUPPLIER, result.get(0).getUserType());
        verify(userRepository).findAllSuppliers();
    }

    @Test
    void searchUsers_WithFilters_ShouldReturnFilteredResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(testUser, testClient);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        
        when(userRepository.searchUsers(
            eq(User.UserType.CLIENT), 
            eq(1L), 
            eq(true), 
            eq("John"), 
            eq(pageable)
        )).thenReturn(userPage);

        // When
        Page<User> result = userService.searchUsers(
            User.UserType.CLIENT, 1L, true, "John", pageable
        );

        // Then
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(userRepository).searchUsers(User.UserType.CLIENT, 1L, true, "John", pageable);
    }

    @Test
    void deactivateUser_ExistingUser_ShouldDeactivate() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.deactivateUser(1L);

        // Then
        assertFalse(testUser.getIsActive());
        verify(userRepository).findById(1L);
        verify(userRepository).save(testUser);
    }

    @Test
    void activateUser_ExistingUser_ShouldActivate() {
        // Given
        testUser.setIsActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.activateUser(1L);

        // Then
        assertTrue(testUser.getIsActive());
        verify(userRepository).findById(1L);
        verify(userRepository).save(testUser);
    }

    @Test
    void deleteUser_UserWithoutAssociations_ShouldDelete() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void changePassword_ValidPassword_ShouldUpdatePassword() {
        // Given
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.changePassword(1L, newPassword);

        // Then
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(testUser);
    }

    @Test
    void verifyPassword_CorrectPassword_ShouldReturnTrue() {
        // Given
        String password = "password123";
        String encodedPassword = "encodedPassword";
        testUser.setPassword(encodedPassword);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // When
        boolean result = userService.verifyPassword(1L, password);

        // Then
        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void verifyPassword_IncorrectPassword_ShouldReturnFalse() {
        // Given
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";
        testUser.setPassword(encodedPassword);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // When
        boolean result = userService.verifyPassword(1L, password);

        // Then
        assertFalse(result);
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void countUsersByType_ShouldReturnCorrectCount() {
        // Given
        when(userRepository.countByUserType(User.UserType.CLIENT)).thenReturn(5L);

        // When
        long count = userService.countUsersByType(User.UserType.CLIENT);

        // Then
        assertEquals(5L, count);
        verify(userRepository).countByUserType(User.UserType.CLIENT);
    }

    @Test
    void getUserByEmail_ExistingEmail_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.getUserByEmail("john.doe@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("john.doe@example.com", result.get().getEmail());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserByEmail_NonExistingEmail_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserByEmail("nonexistent@example.com");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("nonexistent@example.com");
    }
}