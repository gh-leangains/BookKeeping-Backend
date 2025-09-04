package com.eretailgoals.controller;

import com.eretailgoals.entity.User;
import com.eretailgoals.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController
 * Tests the complete flow from HTTP request to database operations
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setUserType(User.UserType.CLIENT);
        testUser.setCompanyName("Test Company");
        testUser.setIsActive(true);
        
        testUser = userRepository.save(testUser);
    }

    @Test
    @WithMockUser
    void createUser_ValidUser_ShouldReturn201() throws Exception {
        // Given
        User newUser = new User();
        newUser.setFirstName("Jane");
        newUser.setLastName("Smith");
        newUser.setEmail("jane.smith@example.com");
        newUser.setUserType(User.UserType.CLIENT);
        newUser.setCompanyName("Smith Corp");

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.email", is("jane.smith@example.com")))
                .andExpect(jsonPath("$.userType", is("CLIENT")))
                .andExpect(jsonPath("$.companyName", is("Smith Corp")))
                .andExpect(jsonPath("$.isActive", is(true)));
    }

    @Test
    @WithMockUser
    void createUser_DuplicateEmail_ShouldReturn400() throws Exception {
        // Given
        User duplicateUser = new User();
        duplicateUser.setFirstName("Jane");
        duplicateUser.setLastName("Smith");
        duplicateUser.setEmail("john.doe@example.com"); // Same as existing user
        duplicateUser.setUserType(User.UserType.CLIENT);

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpected(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createUser_InvalidData_ShouldReturn400() throws Exception {
        // Given
        User invalidUser = new User();
        invalidUser.setEmail("invalid-email"); // Invalid email format
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAllUsers_ShouldReturnUserList() throws Exception {
        // When & Then
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")));
    }

    @Test
    @WithMockUser
    void getUserById_ExistingUser_ShouldReturnUser() throws Exception {
        // When & Then
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    @WithMockUser
    void getUserById_NonExistingUser_ShouldReturn404() throws Exception {
        // When & Then
        mockMvc.perform(get("/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateUser_ValidUpdate_ShouldReturnUpdatedUser() throws Exception {
        // Given
        User updateData = new User();
        updateData.setFirstName("Johnny");
        updateData.setLastName("Doe");
        updateData.setEmail("johnny.doe@example.com");
        updateData.setUserType(User.UserType.CLIENT);
        updateData.setCompanyName("Updated Company");

        // When & Then
        mockMvc.perform(put("/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnny")))
                .andExpect(jsonPath("$.email", is("johnny.doe@example.com")))
                .andExpect(jsonPath("$.companyName", is("Updated Company")));
    }

    @Test
    @WithMockUser
    void deleteUser_ExistingUser_ShouldReturn204() throws Exception {
        // When & Then
        mockMvc.perform(delete("/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());

        // Verify user is deleted
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deactivateUser_ShouldDeactivateUser() throws Exception {
        // When & Then
        mockMvc.perform(patch("/users/{id}/deactivate", testUser.getId()))
                .andExpect(status().isOk());

        // Verify user is deactivated
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive", is(false)));
    }

    @Test
    @WithMockUser
    void activateUser_ShouldActivateUser() throws Exception {
        // Given - deactivate user first
        testUser.setIsActive(false);
        userRepository.save(testUser);

        // When & Then
        mockMvc.perform(patch("/users/{id}/activate", testUser.getId()))
                .andExpect(status().isOk());

        // Verify user is activated
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive", is(true)));
    }

    @Test
    @WithMockUser
    void getUsersByType_ShouldReturnFilteredUsers() throws Exception {
        // Given - create a supplier user
        User supplier = new User();
        supplier.setFirstName("Bob");
        supplier.setLastName("Johnson");
        supplier.setEmail("bob.johnson@example.com");
        supplier.setUserType(User.UserType.SUPPLIER);
        supplier.setIsActive(true);
        userRepository.save(supplier);

        // When & Then - get only clients
        mockMvc.perform(get("/users/type/{userType}", "CLIENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userType", is("CLIENT")));

        // When & Then - get only suppliers
        mockMvc.perform(get("/users/type/{userType}", "SUPPLIER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userType", is("SUPPLIER")));
    }

    @Test
    @WithMockUser
    void getAllClients_ShouldReturnOnlyClients() throws Exception {
        // Given - create a supplier user
        User supplier = new User();
        supplier.setFirstName("Bob");
        supplier.setLastName("Johnson");
        supplier.setEmail("bob.johnson@example.com");
        supplier.setUserType(User.UserType.SUPPLIER);
        supplier.setIsActive(true);
        userRepository.save(supplier);

        // When & Then
        mockMvc.perform(get("/users/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userType", is("CLIENT")));
    }

    @Test
    @WithMockUser
    void getAllSuppliers_ShouldReturnOnlySuppliers() throws Exception {
        // Given - create a supplier user
        User supplier = new User();
        supplier.setFirstName("Bob");
        supplier.setLastName("Johnson");
        supplier.setEmail("bob.johnson@example.com");
        supplier.setUserType(User.UserType.SUPPLIER);
        supplier.setIsActive(true);
        userRepository.save(supplier);

        // When & Then
        mockMvc.perform(get("/users/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userType", is("SUPPLIER")));
    }

    @Test
    @WithMockUser
    void searchUsers_WithSearchTerm_ShouldReturnMatchingUsers() throws Exception {
        // Given - create additional users
        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setUserType(User.UserType.CLIENT);
        user2.setIsActive(true);
        userRepository.save(user2);

        // When & Then - search by first name
        mockMvc.perform(get("/users/search")
                .param("searchTerm", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));
    }

    @Test
    @WithMockUser
    void getUserStatistics_ShouldReturnCorrectCounts() throws Exception {
        // Given - create users of different types
        User supplier = new User();
        supplier.setFirstName("Bob");
        supplier.setLastName("Johnson");
        supplier.setEmail("bob.johnson@example.com");
        supplier.setUserType(User.UserType.SUPPLIER);
        supplier.setIsActive(true);
        userRepository.save(supplier);

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@example.com");
        admin.setUserType(User.UserType.ADMIN);
        admin.setIsActive(true);
        userRepository.save(admin);

        // When & Then
        mockMvc.perform(get("/users/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClients", is(1)))
                .andExpect(jsonPath("$.totalSuppliers", is(1)))
                .andExpect(jsonPath("$.totalAdmins", is(1)));
    }

    @Test
    @WithMockUser
    void changePassword_ValidPassword_ShouldReturn200() throws Exception {
        // Given
        String requestBody = "{\"newPassword\":\"newPassword123\"}";

        // When & Then
        mockMvc.perform(patch("/users/{id}/change-password", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void changePassword_InvalidPassword_ShouldReturn400() throws Exception {
        // Given
        String requestBody = "{\"newPassword\":\"\"}"; // Empty password

        // When & Then
        mockMvc.perform(patch("/users/{id}/change-password", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}