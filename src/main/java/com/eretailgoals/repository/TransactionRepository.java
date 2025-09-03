package com.eretailgoals.repository;

import com.eretailgoals.entity.BankAccount;
import com.eretailgoals.entity.Transaction;
import com.eretailgoals.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Transaction entity operations
 * Provides CRUD operations and custom queries for transaction management
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions by bank account
     */
    List<Transaction> findByBankAccount(BankAccount bankAccount);

    /**
     * Find all transactions by bank account ID
     */
    List<Transaction> findByBankAccountId(Long bankAccountId);

    /**
     * Find all transactions by user
     */
    List<Transaction> findByUser(User user);

    /**
     * Find all transactions by user ID
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * Find all transactions by transaction type
     */
    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);

    /**
     * Find all transactions by admin ID
     */
    List<Transaction> findByAdminId(Long adminId);

    /**
     * Find transactions by date range
     */
    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find transactions by bank account and date range
     */
    List<Transaction> findByBankAccountAndTransactionDateBetween(BankAccount bankAccount, 
                                                                LocalDate startDate, 
                                                                LocalDate endDate);

    /**
     * Find transactions by bank account ID and date range
     */
    List<Transaction> findByBankAccountIdAndTransactionDateBetween(Long bankAccountId, 
                                                                  LocalDate startDate, 
                                                                  LocalDate endDate);

    /**
     * Find unreconciled transactions
     */
    List<Transaction> findByIsReconciledFalse();

    /**
     * Find unreconciled transactions by bank account
     */
    List<Transaction> findByBankAccountAndIsReconciledFalse(BankAccount bankAccount);

    /**
     * Find transactions by reference number
     */
    List<Transaction> findByReferenceNumber(String referenceNumber);

    /**
     * Search transactions with multiple criteria
     */
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:bankAccountId IS NULL OR t.bankAccount.id = :bankAccountId) AND " +
           "(:userId IS NULL OR t.user.id = :userId) AND " +
           "(:adminId IS NULL OR t.adminId = :adminId) AND " +
           "(:transactionType IS NULL OR t.transactionType = :transactionType) AND " +
           "(:startDate IS NULL OR t.transactionDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.transactionDate <= :endDate) AND " +
           "(:isReconciled IS NULL OR t.isReconciled = :isReconciled) AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(t.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.referenceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Transaction> searchTransactions(@Param("bankAccountId") Long bankAccountId,
                                        @Param("userId") Long userId,
                                        @Param("adminId") Long adminId,
                                        @Param("transactionType") Transaction.TransactionType transactionType,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("isReconciled") Boolean isReconciled,
                                        @Param("searchTerm") String searchTerm,
                                        Pageable pageable);

    /**
     * Calculate total income for date range
     */
    @Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM Transaction t " +
           "WHERE t.transactionType IN ('RECEIVE', 'DEPOSIT', 'INTEREST') " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalIncomeByDateRange(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * Calculate total expenses for date range
     */
    @Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM Transaction t " +
           "WHERE t.transactionType IN ('PAYMENT', 'WITHDRAWAL', 'FEE') " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpensesByDateRange(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    /**
     * Calculate net cash flow for date range
     */
    @Query("SELECT COALESCE(SUM(CASE " +
           "WHEN t.transactionType IN ('RECEIVE', 'DEPOSIT', 'INTEREST') THEN t.transactionAmount " +
           "WHEN t.transactionType IN ('PAYMENT', 'WITHDRAWAL', 'FEE') THEN -t.transactionAmount " +
           "ELSE 0 END), 0) FROM Transaction t " +
           "WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateNetCashFlowByDateRange(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * Find transactions by expense type
     */
    List<Transaction> findByExpenseType(Transaction.ExpenseType expenseType);

    /**
     * Calculate total expenses by expense type and date range
     */
    @Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM Transaction t " +
           "WHERE t.expenseType = :expenseType " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpensesByTypeAndDateRange(@Param("expenseType") Transaction.ExpenseType expenseType,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    /**
     * Find recent transactions
     */
    @Query("SELECT t FROM Transaction t ORDER BY t.transactionDate DESC, t.dateCreated DESC")
    List<Transaction> findRecentTransactions(Pageable pageable);

    /**
     * Find transactions by bank account ordered by date descending
     */
    List<Transaction> findByBankAccountOrderByTransactionDateDescDateCreatedDesc(BankAccount bankAccount);

    /**
     * Count transactions by transaction type
     */
    long countByTransactionType(Transaction.TransactionType transactionType);

    /**
     * Count unreconciled transactions
     */
    long countByIsReconciledFalse();

    /**
     * Count transactions by bank account
     */
    long countByBankAccount(BankAccount bankAccount);

    /**
     * Find monthly transaction summary
     */
    @Query("SELECT YEAR(t.transactionDate), MONTH(t.transactionDate), " +
           "SUM(CASE WHEN t.transactionType IN ('RECEIVE', 'DEPOSIT', 'INTEREST') THEN t.transactionAmount ELSE 0 END), " +
           "SUM(CASE WHEN t.transactionType IN ('PAYMENT', 'WITHDRAWAL', 'FEE') THEN t.transactionAmount ELSE 0 END) " +
           "FROM Transaction t " +
           "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(t.transactionDate), MONTH(t.transactionDate) " +
           "ORDER BY YEAR(t.transactionDate), MONTH(t.transactionDate)")
    List<Object[]> findMonthlyTransactionSummary(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
}