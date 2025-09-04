package com.eretailgoals.service;

import com.eretailgoals.entity.User;
import com.eretailgoals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for User entity operations
 * Provides business logic for user management including CRUD operations,
 * authentication, and user-related business rules
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user
     */
    public User createUser(User user) {
        validateUser(user);
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        // Check if username already exists (if provided)
        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        // Encode password if provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Set default values
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        
        return userRepository.save(user);
    }

    /**
     * Update an existing user
     */
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);
        
        // Update fields
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setCompanyName(userDetails.getCompanyName());
        existingUser.setAddress(userDetails.getAddress());
        existingUser.setPostcode(userDetails.getPostcode());
        existingUser.setShippingAddress(userDetails.getShippingAddress());
        existingUser.setShippingPostcode(userDetails.getShippingPostcode());
        existingUser.setPhoneOffice(userDetails.getPhoneOffice());
        existingUser.setPhoneHome(userDetails.getPhoneHome());
        existingUser.setMobile(userDetails.getMobile());
        existingUser.setVatNumber(userDetails.getVatNumber());
        existingUser.setFax(userDetails.getFax());
        existingUser.setUserType(userDetails.getUserType());
        
        // Update email if changed and not already taken
        if (!existingUser.getEmail().equals(userDetails.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + userDetails.getEmail());
            }
            existingUser.setEmail(userDetails.getEmail());
        }
        
        // Update username if changed and not already taken
        if (userDetails.getUsername() != null && 
            !userDetails.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new IllegalArgumentException("Username already exists: " + userDetails.getUsername());
            }
            existingUser.setUsername(userDetails.getUsername());
        }
        
        return userRepository.save(existingUser);
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get users by type
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByType(User.UserType userType) {
        return userRepository.findByUserTypeAndIsActiveTrue(userType);
    }

    /**
     * Get all clients
     */
    @Transactional(readOnly = true)
    public List<User> getAllClients() {
        return userRepository.findAllClients();
    }

    /**
     * Get all suppliers
     */
    @Transactional(readOnly = true)
    public List<User> getAllSuppliers() {
        return userRepository.findAllSuppliers();
    }

    /**
     * Get users by admin ID
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByAdminId(Long adminId) {
        return userRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    /**
     * Search users with pagination
     */
    @Transactional(readOnly = true)
    public Page<User> searchUsers(User.UserType userType, Long adminId, Boolean isActive, 
                                 String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(userType, adminId, isActive, searchTerm, pageable);
    }

    /**
     * Deactivate user (soft delete)
     */
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Activate user
     */
    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(true);
        userRepository.save(user);
    }

    /**
     * Delete user permanently
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        
        // Check if user has associated invoices or transactions
        if (!user.getInvoices().isEmpty() || !user.getTransactions().isEmpty()) {
            throw new IllegalStateException("Cannot delete user with associated invoices or transactions. " +
                                          "Consider deactivating instead.");
        }
        
        userRepository.delete(user);
    }

    /**
     * Change user password
     */
    public void changePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Verify user password
     */
    @Transactional(readOnly = true)
    public boolean verifyPassword(Long id, String password) {
        User user = getUserById(id);
        return user.getPassword() != null && passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Get users with outstanding invoices
     */
    @Transactional(readOnly = true)
    public List<User> getUsersWithOutstandingInvoices() {
        return userRepository.findUsersWithOutstandingInvoices();
    }

    /**
     * Count users by type
     */
    @Transactional(readOnly = true)
    public long countUsersByType(User.UserType userType) {
        return userRepository.countByUserType(userType);
    }

    /**
     * Count active users by admin ID
     */
    @Transactional(readOnly = true)
    public long countActiveUsersByAdminId(Long adminId) {
        return userRepository.countByAdminIdAndIsActiveTrue(adminId);
    }

    /**
     * Validate user data
     */
    private void validateUser(User user) {
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (user.getUserType() == null) {
            throw new IllegalArgumentException("User type is required");
        }
    }
}