package com.eretailgoals.repository;

import com.eretailgoals.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations
 * Provides CRUD operations and custom queries for user management
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email or username
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Find all users by user type
     */
    List<User> findByUserType(User.UserType userType);

    /**
     * Find all active users by user type
     */
    List<User> findByUserTypeAndIsActiveTrue(User.UserType userType);

    /**
     * Find all users by admin ID
     */
    List<User> findByAdminId(Long adminId);

    /**
     * Find all active users by admin ID
     */
    List<User> findByAdminIdAndIsActiveTrue(Long adminId);

    /**
     * Find users by company name containing (case-insensitive)
     */
    List<User> findByCompanyNameContainingIgnoreCase(String companyName);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Find users by first name and last name
     */
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    /**
     * Search users by multiple criteria
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:userType IS NULL OR u.userType = :userType) AND " +
           "(:adminId IS NULL OR u.adminId = :adminId) AND " +
           "(:isActive IS NULL OR u.isActive = :isActive) AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> searchUsers(@Param("userType") User.UserType userType,
                          @Param("adminId") Long adminId,
                          @Param("isActive") Boolean isActive,
                          @Param("searchTerm") String searchTerm,
                          Pageable pageable);

    /**
     * Find all clients (customers)
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'CLIENT' AND u.isActive = true")
    List<User> findAllClients();

    /**
     * Find all suppliers
     */
    @Query("SELECT u FROM User u WHERE u.userType = 'SUPPLIER' AND u.isActive = true")
    List<User> findAllSuppliers();

    /**
     * Find users with outstanding invoices
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN u.invoices i " +
           "WHERE i.invoiceStatus IN ('OPEN', 'PARTIAL_PAID', 'OVERDUE')")
    List<User> findUsersWithOutstandingInvoices();

    /**
     * Count users by type
     */
    long countByUserType(User.UserType userType);

    /**
     * Count active users by admin ID
     */
    long countByAdminIdAndIsActiveTrue(Long adminId);
}