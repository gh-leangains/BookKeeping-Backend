package com.eretailgoals.controller;

import com.eretailgoals.entity.User;
import com.eretailgoals.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for User management operations
 * Provides endpoints for CRUD operations on users including clients, suppliers, and admin users
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users, clients, and suppliers")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Creates a new user (client, supplier, or admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Permanently deletes a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "User has associated data and cannot be deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deactivate user", description = "Deactivates a user (soft delete)")
    @ApiResponse(responseCode = "200", description = "User deactivated successfully")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Activate user", description = "Activates a previously deactivated user")
    @ApiResponse(responseCode = "200", description = "User activated successfully")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get users by type", description = "Retrieves users filtered by user type")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/type/{userType}")
    public ResponseEntity<List<User>> getUsersByType(
            @Parameter(description = "User type (ADMIN, CLIENT, SUPPLIER)") 
            @PathVariable User.UserType userType) {
        List<User> users = userService.getUsersByType(userType);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get all clients", description = "Retrieves all active client users")
    @ApiResponse(responseCode = "200", description = "Clients retrieved successfully")
    @GetMapping("/clients")
    public ResponseEntity<List<User>> getAllClients() {
        List<User> clients = userService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get all suppliers", description = "Retrieves all active supplier users")
    @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully")
    @GetMapping("/suppliers")
    public ResponseEntity<List<User>> getAllSuppliers() {
        List<User> suppliers = userService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @Operation(summary = "Search users", description = "Search users with pagination and filtering")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(
            @Parameter(description = "User type filter") @RequestParam(required = false) User.UserType userType,
            @Parameter(description = "Admin ID filter") @RequestParam(required = false) Long adminId,
            @Parameter(description = "Active status filter") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Search term") @RequestParam(required = false) String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<User> users = userService.searchUsers(userType, adminId, isActive, searchTerm, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users with outstanding invoices", 
               description = "Retrieves users who have unpaid or partially paid invoices")
    @ApiResponse(responseCode = "200", description = "Users with outstanding invoices retrieved successfully")
    @GetMapping("/outstanding-invoices")
    public ResponseEntity<List<User>> getUsersWithOutstandingInvoices() {
        List<User> users = userService.getUsersWithOutstandingInvoices();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Change user password", description = "Changes a user's password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody Map<String, String> passwordData) {
        
        String newPassword = passwordData.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        userService.changePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user statistics", description = "Retrieves user count statistics by type")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getUserStatistics() {
        Map<String, Long> statistics = Map.of(
            "totalClients", userService.countUsersByType(User.UserType.CLIENT),
            "totalSuppliers", userService.countUsersByType(User.UserType.SUPPLIER),
            "totalAdmins", userService.countUsersByType(User.UserType.ADMIN)
        );
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Get users by admin ID", description = "Retrieves users managed by a specific admin")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<User>> getUsersByAdminId(
            @Parameter(description = "Admin ID") @PathVariable Long adminId) {
        List<User> users = userService.getUsersByAdminId(adminId);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Verify user password", description = "Verifies if the provided password is correct")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password verification result"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/verify-password")
    public ResponseEntity<Map<String, Boolean>> verifyPassword(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody Map<String, String> passwordData) {
        
        String password = passwordData.get("password");
        boolean isValid = userService.verifyPassword(id, password);
        
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}