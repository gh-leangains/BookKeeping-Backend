package com.eretailgoals.service;

import com.eretailgoals.entity.Invoice;
import com.eretailgoals.entity.InvoiceItem;
import com.eretailgoals.entity.User;
import com.eretailgoals.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Invoice entity operations
 * Provides business logic for invoice management including CRUD operations,
 * invoice calculations, and invoice-related business rules
 */
@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserService userService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }

    /**
     * Create a new invoice
     */
    public Invoice createInvoice(Invoice invoice) {
        validateInvoice(invoice);
        
        // Check if invoice number already exists
        if (invoiceRepository.existsByInvoiceNumber(invoice.getInvoiceNumber())) {
            throw new IllegalArgumentException("Invoice number already exists: " + invoice.getInvoiceNumber());
        }
        
        // Set default values
        if (invoice.getInvoiceDate() == null) {
            invoice.setInvoiceDate(LocalDate.now());
        }
        
        if (invoice.getInvoiceStatus() == null) {
            invoice.setInvoiceStatus(Invoice.InvoiceStatus.OPEN);
        }
        
        if (invoice.getVatAmount() == null) {
            invoice.setVatAmount(BigDecimal.ZERO);
        }
        
        if (invoice.getInvoicePaidAmount() == null) {
            invoice.setInvoicePaidAmount(BigDecimal.ZERO);
        }
        
        // Calculate totals from invoice items if present
        if (!invoice.getInvoiceItems().isEmpty()) {
            calculateInvoiceTotals(invoice);
        }
        
        return invoiceRepository.save(invoice);
    }

    /**
     * Update an existing invoice
     */
    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Invoice existingInvoice = getInvoiceById(id);
        
        // Update fields
        existingInvoice.setInvoiceDate(invoiceDetails.getInvoiceDate());
        existingInvoice.setDueDate(invoiceDetails.getDueDate());
        existingInvoice.setInvoiceType(invoiceDetails.getInvoiceType());
        existingInvoice.setInvoiceNote(invoiceDetails.getInvoiceNote());
        existingInvoice.setInvoiceAmount(invoiceDetails.getInvoiceAmount());
        existingInvoice.setVatAmount(invoiceDetails.getVatAmount());
        
        // Update invoice number if changed and not already taken
        if (!existingInvoice.getInvoiceNumber().equals(invoiceDetails.getInvoiceNumber())) {
            if (invoiceRepository.existsByInvoiceNumber(invoiceDetails.getInvoiceNumber())) {
                throw new IllegalArgumentException("Invoice number already exists: " + invoiceDetails.getInvoiceNumber());
            }
            existingInvoice.setInvoiceNumber(invoiceDetails.getInvoiceNumber());
        }
        
        // Update status and recalculate if needed
        existingInvoice.updateStatus();
        
        return invoiceRepository.save(existingInvoice);
    }

    /**
     * Get invoice by ID
     */
    @Transactional(readOnly = true)
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + id));
    }

    /**
     * Get invoice by invoice number
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceByNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    /**
     * Get all invoices
     */
    @Transactional(readOnly = true)
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll(Sort.by(Sort.Direction.DESC, "invoiceDate"));
    }

    /**
     * Get invoices by user
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByUser(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }

    /**
     * Get invoices by status
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.findByInvoiceStatus(status);
    }

    /**
     * Get invoices by admin ID
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByAdminId(Long adminId) {
        return invoiceRepository.findByAdminId(adminId);
    }

    /**
     * Get invoices by date range
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findByInvoiceDateBetween(startDate, endDate);
    }

    /**
     * Get overdue invoices
     */
    @Transactional(readOnly = true)
    public List<Invoice> getOverdueInvoices() {
        return invoiceRepository.findOverdueInvoices(LocalDate.now());
    }

    /**
     * Get invoices with outstanding amounts
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesWithOutstandingAmounts() {
        return invoiceRepository.findInvoicesWithOutstandingAmounts();
    }

    /**
     * Search invoices with pagination
     */
    @Transactional(readOnly = true)
    public Page<Invoice> searchInvoices(Long userId, Long adminId, Invoice.InvoiceStatus status,
                                       LocalDate startDate, LocalDate endDate, String searchTerm,
                                       Pageable pageable) {
        return invoiceRepository.searchInvoices(userId, adminId, status, startDate, endDate, searchTerm, pageable);
    }

    /**
     * Get recent invoices
     */
    @Transactional(readOnly = true)
    public List<Invoice> getRecentInvoices(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return invoiceRepository.findRecentInvoices(pageable);
    }

    /**
     * Delete invoice
     */
    public void deleteInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        
        // Check if invoice has payments
        if (invoice.getInvoicePaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Cannot delete invoice with payments. Consider cancelling instead.");
        }
        
        invoiceRepository.delete(invoice);
    }

    /**
     * Cancel invoice
     */
    public Invoice cancelInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        invoice.setInvoiceStatus(Invoice.InvoiceStatus.CANCELLED);
        return invoiceRepository.save(invoice);
    }

    /**
     * Add payment to invoice
     */
    public Invoice addPayment(Long id, BigDecimal paymentAmount) {
        Invoice invoice = getInvoiceById(id);
        
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        
        BigDecimal currentPaid = invoice.getInvoicePaidAmount();
        BigDecimal totalAmount = invoice.getTotalAmount();
        BigDecimal newPaidAmount = currentPaid.add(paymentAmount);
        
        if (newPaidAmount.compareTo(totalAmount) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding balance");
        }
        
        invoice.setInvoicePaidAmount(newPaidAmount);
        invoice.updateStatus();
        
        return invoiceRepository.save(invoice);
    }

    /**
     * Add invoice item
     */
    public Invoice addInvoiceItem(Long invoiceId, InvoiceItem item) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.addInvoiceItem(item);
        calculateInvoiceTotals(invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Remove invoice item
     */
    public Invoice removeInvoiceItem(Long invoiceId, Long itemId) {
        Invoice invoice = getInvoiceById(invoiceId);
        InvoiceItem itemToRemove = invoice.getInvoiceItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invoice item not found"));
        
        invoice.removeInvoiceItem(itemToRemove);
        calculateInvoiceTotals(invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Generate next invoice number
     */
    @Transactional(readOnly = true)
    public String generateNextInvoiceNumber() {
        String prefix = "INV-" + LocalDate.now().getYear() + "-";
        long count = invoiceRepository.count() + 1;
        return STR."\{prefix}\{String.format("%06d", count)}";
    }

    /**
     * Calculate total outstanding amount
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalOutstandingAmount() {
        return invoiceRepository.calculateTotalOutstandingAmount();
    }

    /**
     * Calculate total outstanding amount by user
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalOutstandingAmountByUser(Long userId) {
        return invoiceRepository.calculateTotalOutstandingAmountByUser(userId);
    }

    /**
     * Calculate total invoice amount for date range
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalInvoiceAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.calculateTotalInvoiceAmountByDateRange(startDate, endDate);
    }

    /**
     * Get invoices due within specified days
     */
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesDueWithinDays(int days) {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(days);
        return invoiceRepository.findInvoicesDueWithinDays(currentDate, futureDate);
    }

    /**
     * Count invoices by status
     */
    @Transactional(readOnly = true)
    public long countInvoicesByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.countByInvoiceStatus(status);
    }

    /**
     * Calculate invoice totals from items
     */
    private void calculateInvoiceTotals(Invoice invoice) {
        BigDecimal subtotal = invoice.getInvoiceItems().stream()
                .map(InvoiceItem::getNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal vatTotal = invoice.getInvoiceItems().stream()
                .map(InvoiceItem::getVatAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        invoice.setInvoiceAmount(subtotal);
        invoice.setVatAmount(vatTotal);
    }

    /**
     * Validate invoice data
     */
    private void validateInvoice(Invoice invoice) {
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice number is required");
        }
        
        if (invoice.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        
        if (invoice.getInvoiceAmount() == null || invoice.getInvoiceAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invoice amount must be non-negative");
        }
    }
}