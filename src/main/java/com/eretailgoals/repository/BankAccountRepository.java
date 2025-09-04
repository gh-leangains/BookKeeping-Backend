package com.eretailgoals.repository;

import com.eretailgoals.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BankAccount entity operations
 * Provides CRUD operations and custom queries for bank account management
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    /**
     * Find bank account by account number
     */
    Optional<BankAccount> findByAccountNumber(String accountNumber);

    /**
     * Find all bank accounts by account type
     */
    List<BankAccount> findByAccountType(BankAccount.AccountType accountType);

    /**
     * Find all active bank accounts
     */
    List<BankAccount> findByIsActiveTrue();

    /**
     * Find all bank accounts by admin ID
     */
    List<BankAccount> findByAdminId(Long adminId);

    /**
     * Find all active bank accounts by admin ID
     */
    List<BankAccount> findByAdminIdAndIsActiveTrue(Long adminId);

    /**
     * Find bank accounts by account name containing (case-insensitive)
     */
    List<BankAccount> findByAccountNameContainingIgnoreCase(String accountName);

    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Find bank accounts by account type and admin ID
     */
    List<BankAccount> findByAccountTypeAndAdminId(BankAccount.AccountType accountType, Long adminId);

    /**
     * Find bank accounts with balance greater than specified amount
     */
    List<BankAccount> findByCurrentBalanceGreaterThan(BigDecimal amount);

    /**
     * Find bank accounts with balance less than specified amount
     */
    List<BankAccount> findByCurrentBalanceLessThan(BigDecimal amount);

    /**
     * Calculate total balance across all active accounts
     */
    @Query("SELECT COALESCE(SUM(ba.currentBalance), 0) FROM BankAccount ba WHERE ba.isActive = true")
    BigDecimal calculateTotalBalance();

    /**
     * Calculate total balance by admin ID
     */
    @Query("SELECT COALESCE(SUM(ba.currentBalance), 0) FROM BankAccount ba WHERE ba.adminId = :adminId AND ba.isActive = true")
    BigDecimal calculateTotalBalanceByAdminId(@Param("adminId") Long adminId);

    /**
     * Calculate total balance by account type
     */
    @Query("SELECT COALESCE(SUM(ba.currentBalance), 0) FROM BankAccount ba WHERE ba.accountType = :accountType AND ba.isActive = true")
    BigDecimal calculateTotalBalanceByAccountType(@Param("accountType") BankAccount.AccountType accountType);

    /**
     * Find bank accounts with recent transactions
     */
    @Query("SELECT DISTINCT ba FROM BankAccount ba " +
           "JOIN ba.transactions t " +
           "WHERE t.transactionDate >= :sinceDate " +
           "ORDER BY ba.accountName")
    List<BankAccount> findAccountsWithRecentTransactions(@Param("sinceDate") java.time.LocalDate sinceDate);

    /**
     * Search bank accounts by multiple criteria
     */
    @Query("SELECT ba FROM BankAccount ba WHERE " +
           "(:adminId IS NULL OR ba.adminId = :adminId) AND " +
           "(:accountType IS NULL OR ba.accountType = :accountType) AND " +
           "(:isActive IS NULL OR ba.isActive = :isActive) AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(ba.accountName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ba.accountNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ba.sortCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<BankAccount> searchBankAccounts(@Param("adminId") Long adminId,
                                        @Param("accountType") BankAccount.AccountType accountType,
                                        @Param("isActive") Boolean isActive,
                                        @Param("searchTerm") String searchTerm);

    /**
     * Count active bank accounts by admin ID
     */
    long countByAdminIdAndIsActiveTrue(Long adminId);

    /**
     * Count bank accounts by account type
     */
    long countByAccountType(BankAccount.AccountType accountType);

    /**
     * Find bank accounts ordered by current balance descending
     */
    List<BankAccount> findByIsActiveTrueOrderByCurrentBalanceDesc();

    /**
     * Find bank accounts ordered by account name
     */
    List<BankAccount> findByIsActiveTrueOrderByAccountNameAsc();
}