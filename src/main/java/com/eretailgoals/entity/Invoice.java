package com.eretailgoals.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice entity representing invoices in the system
 * Migrated from legacy InvoiceBO with modern JPA annotations and relationships
 */
@Entity
@Table(name = "invoices")
@EntityListeners(AuditingEntityListener.class)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @NotBlank(message = "Invoice number is required")
    @Size(max = 50, message = "Invoice number must not exceed 50 characters")
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @NotNull(message = "Invoice date is required")
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type")
    private InvoiceType invoiceType = InvoiceType.STANDARD;

    @Column(name = "invoice_note", columnDefinition = "TEXT")
    private String invoiceNote;

    @NotNull(message = "Invoice amount is required")
    @DecimalMin(value = "0.0", message = "Invoice amount must be non-negative")
    @Column(name = "invoice_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal invoiceAmount;

    @DecimalMin(value = "0.0", message = "VAT amount must be non-negative")
    @Column(name = "vat_amount", precision = 19, scale = 2)
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Paid amount must be non-negative")
    @Column(name = "invoice_paid_amount", precision = 19, scale = 2)
    private BigDecimal invoicePaidAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status", nullable = false)
    private InvoiceStatus invoiceStatus = InvoiceStatus.OPEN;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors
    public Invoice() {}

    public Invoice(String invoiceNumber, LocalDate invoiceDate, User user, BigDecimal invoiceAmount) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.user = user;
        this.invoiceAmount = invoiceAmount;
    }

    // Enums
    public enum InvoiceStatus {
        OPEN("Open"),
        PARTIAL_PAID("Partially Paid"),
        PAID("Paid"),
        OVERDUE("Overdue"),
        CANCELLED("Cancelled");

        private final String displayName;

        InvoiceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum InvoiceType {
        STANDARD("Standard Invoice"),
        CREDIT_NOTE("Credit Note"),
        PROFORMA("Proforma Invoice"),
        RECURRING("Recurring Invoice");

        private final String displayName;

        InvoiceType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getInvoicePaidAmount() {
        return invoicePaidAmount;
    }

    public void setInvoicePaidAmount(BigDecimal invoicePaidAmount) {
        this.invoicePaidAmount = invoicePaidAmount;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Business methods
    public BigDecimal getTotalAmount() {
        return invoiceAmount.add(vatAmount != null ? vatAmount : BigDecimal.ZERO);
    }

    public BigDecimal getOutstandingAmount() {
        return getTotalAmount().subtract(invoicePaidAmount != null ? invoicePaidAmount : BigDecimal.ZERO);
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate) && 
               invoiceStatus != InvoiceStatus.PAID && invoiceStatus != InvoiceStatus.CANCELLED;
    }

    public boolean isPaid() {
        return invoiceStatus == InvoiceStatus.PAID || 
               getOutstandingAmount().compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isPartiallyPaid() {
        return invoicePaidAmount != null && 
               invoicePaidAmount.compareTo(BigDecimal.ZERO) > 0 && 
               invoicePaidAmount.compareTo(getTotalAmount()) < 0;
    }

    public void addInvoiceItem(InvoiceItem item) {
        invoiceItems.add(item);
        item.setInvoice(this);
    }

    public void removeInvoiceItem(InvoiceItem item) {
        invoiceItems.remove(item);
        item.setInvoice(null);
    }

    public void updateStatus() {
        if (isPaid()) {
            this.invoiceStatus = InvoiceStatus.PAID;
        } else if (isPartiallyPaid()) {
            this.invoiceStatus = InvoiceStatus.PARTIAL_PAID;
        } else if (isOverdue()) {
            this.invoiceStatus = InvoiceStatus.OVERDUE;
        } else {
            this.invoiceStatus = InvoiceStatus.OPEN;
        }
    }

    @Override
    public String toString() {
        return STR."Invoice{id=\{id}, invoiceNumber='\{invoiceNumber}', invoiceDate=\{invoiceDate}, invoiceAmount=\{invoiceAmount}, invoiceStatus=\{invoiceStatus}}";
    }
}