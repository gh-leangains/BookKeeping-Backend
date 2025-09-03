package com.eretailgoals.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Transaction entity representing financial transactions in the system
 * Migrated from legacy TransactionBO with modern JPA annotations and relationships
 */
@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @NotNull(message = "Transaction date is required")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.0", message = "Transaction amount must be non-negative")
    @Column(name = "transaction_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal transactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "ending_balance", precision = 19, scale = 2)
    private BigDecimal endingBalance;

    @DecimalMin(value = "0.0", message = "Amount received must be non-negative")
    @Column(name = "amount_received", precision = 19, scale = 2)
    private BigDecimal amountReceived = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Amount transferred must be non-negative")
    @Column(name = "amount_transferred", precision = 19, scale = 2)
    private BigDecimal amountTransferred = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_type")
    private ExpenseType expenseType;

    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(name = "is_reconciled")
    private Boolean isReconciled = false;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    // Constructors
    public Transaction() {}

    public Transaction(LocalDate transactionDate, BigDecimal transactionAmount, 
                      TransactionType transactionType, BankAccount bankAccount) {
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.bankAccount = bankAccount;
    }

    // Enums
    public enum TransactionType {
        RECEIVE("Receive Payment"),
        PAYMENT("Make Payment"),
        TRANSFER("Bank Transfer"),
        DEPOSIT("Deposit"),
        WITHDRAWAL("Withdrawal"),
        FEE("Bank Fee"),
        INTEREST("Interest");

        private final String displayName;

        TransactionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum ExpenseType {
        OFFICE_SUPPLIES("Office Supplies"),
        TRAVEL("Travel & Transportation"),
        UTILITIES("Utilities"),
        RENT("Rent & Lease"),
        INSURANCE("Insurance"),
        PROFESSIONAL_SERVICES("Professional Services"),
        MARKETING("Marketing & Advertising"),
        EQUIPMENT("Equipment & Software"),
        MEALS("Meals & Entertainment"),
        OTHER("Other Expenses");

        private final String displayName;

        ExpenseType(String displayName) {
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public BigDecimal getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(BigDecimal amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean getIsReconciled() {
        return isReconciled;
    }

    public void setIsReconciled(Boolean isReconciled) {
        this.isReconciled = isReconciled;
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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // Business methods
    public boolean isCredit() {
        return transactionType == TransactionType.RECEIVE || 
               transactionType == TransactionType.DEPOSIT ||
               transactionType == TransactionType.INTEREST;
    }

    public boolean isDebit() {
        return transactionType == TransactionType.PAYMENT || 
               transactionType == TransactionType.WITHDRAWAL ||
               transactionType == TransactionType.FEE;
    }

    public BigDecimal getEffectiveAmount() {
        return isCredit() ? transactionAmount : transactionAmount.negate();
    }

    public String getFormattedReference() {
        if (referenceNumber != null && !referenceNumber.isEmpty()) {
            return referenceNumber;
        }
        return STR."TXN-\{id}";
    }

    @Override
    public String toString() {
        return STR."Transaction{id=\{id}, transactionDate=\{transactionDate}, transactionAmount=\{transactionAmount}, transactionType=\{transactionType}, bankAccount=\{bankAccount != null ? bankAccount.getAccountName() : "null"}}";
    }
}