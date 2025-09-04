package com.eretailgoals.repository;

import com.eretailgoals.entity.Invoice;
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
import java.util.Optional;

/**
 * Repository interface for Invoice entity operations
 * Provides CRUD operations and custom queries for invoice management
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * Find invoice by invoice number
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    /**
     * Find all invoices by user
     */
    List<Invoice> findByUser(User user);

    /**
     * Find all invoices by user ID
     */
    List<Invoice> findByUserId(Long userId);

    /**
     * Find all invoices by status
     */
    List<Invoice> findByInvoiceStatus(Invoice.InvoiceStatus status);

    /**
     * Find all invoices by admin ID
     */
    List<Invoice> findByAdminId(Long adminId);

    /**
     * Find invoices by date range
     */
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find invoices by due date range
     */
    List<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find overdue invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :currentDate AND i.invoiceStatus NOT IN ('PAID', 'CANCELLED')")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDate currentDate);

    /**
     * Find invoices by user and status
     */
    List<Invoice> findByUserAndInvoiceStatus(User user, Invoice.InvoiceStatus status);

    /**
     * Find invoices by user ID and status
     */
    List<Invoice> findByUserIdAndInvoiceStatus(Long userId, Invoice.InvoiceStatus status);

    /**
     * Check if invoice number exists
     */
    boolean existsByInvoiceNumber(String invoiceNumber);

    /**
     * Search invoices with multiple criteria
     */
    @Query("SELECT i FROM Invoice i WHERE " +
           "(:userId IS NULL OR i.user.id = :userId) AND " +
           "(:adminId IS NULL OR i.adminId = :adminId) AND " +
           "(:status IS NULL OR i.invoiceStatus = :status) AND " +
           "(:startDate IS NULL OR i.invoiceDate >= :startDate) AND " +
           "(:endDate IS NULL OR i.invoiceDate <= :endDate) AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.invoiceNote) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.user.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.user.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.user.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Invoice> searchInvoices(@Param("userId") Long userId,
                                @Param("adminId") Long adminId,
                                @Param("status") Invoice.InvoiceStatus status,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("searchTerm") String searchTerm,
                                Pageable pageable);

    /**
     * Find invoices with outstanding amounts
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceStatus IN ('OPEN', 'PARTIAL_PAID', 'OVERDUE')")
    List<Invoice> findInvoicesWithOutstandingAmounts();

    /**
     * Calculate total outstanding amount
     */
    @Query("SELECT COALESCE(SUM(i.invoiceAmount + COALESCE(i.vatAmount, 0) - COALESCE(i.invoicePaidAmount, 0)), 0) " +
           "FROM Invoice i WHERE i.invoiceStatus IN ('OPEN', 'PARTIAL_PAID', 'OVERDUE')")
    BigDecimal calculateTotalOutstandingAmount();

    /**
     * Calculate total outstanding amount by user
     */
    @Query("SELECT COALESCE(SUM(i.invoiceAmount + COALESCE(i.vatAmount, 0) - COALESCE(i.invoicePaidAmount, 0)), 0) " +
           "FROM Invoice i WHERE i.user.id = :userId AND i.invoiceStatus IN ('OPEN', 'PARTIAL_PAID', 'OVERDUE')")
    BigDecimal calculateTotalOutstandingAmountByUser(@Param("userId") Long userId);

    /**
     * Calculate total invoice amount for a date range
     */
    @Query("SELECT COALESCE(SUM(i.invoiceAmount + COALESCE(i.vatAmount, 0)), 0) " +
           "FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalInvoiceAmountByDateRange(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    /**
     * Find recent invoices
     */
    @Query("SELECT i FROM Invoice i ORDER BY i.dateCreated DESC")
    List<Invoice> findRecentInvoices(Pageable pageable);

    /**
     * Count invoices by status
     */
    long countByInvoiceStatus(Invoice.InvoiceStatus status);

    /**
     * Count invoices by user
     */
    long countByUser(User user);

    /**
     * Count invoices by admin ID
     */
    long countByAdminId(Long adminId);

    /**
     * Find invoices due within specified days
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN :currentDate AND :futureDate " +
           "AND i.invoiceStatus NOT IN ('PAID', 'CANCELLED')")
    List<Invoice> findInvoicesDueWithinDays(@Param("currentDate") LocalDate currentDate,
                                           @Param("futureDate") LocalDate futureDate);

    /**
     * Find top clients by invoice amount
     */
    @Query("SELECT i.user, SUM(i.invoiceAmount + COALESCE(i.vatAmount, 0)) as totalAmount " +
           "FROM Invoice i " +
           "WHERE i.invoiceDate BETWEEN :startDate AND :endDate " +
           "GROUP BY i.user " +
           "ORDER BY totalAmount DESC")
    List<Object[]> findTopClientsByInvoiceAmount(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                Pageable pageable);
}