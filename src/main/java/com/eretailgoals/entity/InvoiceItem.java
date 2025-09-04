package com.eretailgoals.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * InvoiceItem entity representing individual line items in an invoice
 * Migrated from legacy InvoiceItemsBO with modern JPA annotations
 */
@Entity
@Table(name = "invoice_items")
@EntityListeners(AuditingEntityListener.class)
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item description is required")
    @Size(max = 500, message = "Item description must not exceed 500 characters")
    @Column(name = "item_description", nullable = false, length = 500)
    private String itemDescription;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0", message = "Discount must be non-negative")
    @Column(name = "discount", precision = 19, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "VAT rate must be non-negative")
    @Column(name = "vat_rate", precision = 5, scale = 2)
    private BigDecimal vatRate = BigDecimal.ZERO;

    @Column(name = "line_total", precision = 19, scale = 2)
    private BigDecimal lineTotal;

    @Size(max = 50, message = "Item code must not exceed 50 characters")
    @Column(name = "item_code", length = 50)
    private String itemCode;

    @Size(max = 20, message = "Unit must not exceed 20 characters")
    @Column(name = "unit", length = 20)
    private String unit;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    // Constructors
    public InvoiceItem() {}

    public InvoiceItem(String itemDescription, Integer quantity, BigDecimal unitPrice) {
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateLineTotal();
    }

    public InvoiceItem(String itemDescription, Integer quantity, BigDecimal unitPrice, BigDecimal vatRate) {
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.vatRate = vatRate;
        calculateLineTotal();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateLineTotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateLineTotal();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
        calculateLineTotal();
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        calculateLineTotal();
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // Business methods
    public BigDecimal getSubTotal() {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getDiscountAmount() {
        if (discount == null) {
            return BigDecimal.ZERO;
        }
        return getSubTotal().multiply(discount).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal getNetAmount() {
        return getSubTotal().subtract(getDiscountAmount());
    }

    public BigDecimal getVatAmount() {
        if (vatRate == null) {
            return BigDecimal.ZERO;
        }
        return getNetAmount().multiply(vatRate).divide(BigDecimal.valueOf(100));
    }

    public void calculateLineTotal() {
        if (quantity != null && unitPrice != null) {
            BigDecimal subTotal = getSubTotal();
            BigDecimal discountAmount = getDiscountAmount();
            BigDecimal netAmount = subTotal.subtract(discountAmount);
            BigDecimal vatAmount = getVatAmount();
            this.lineTotal = netAmount.add(vatAmount);
        }
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        calculateLineTotal();
    }

    @Override
    public String toString() {
        return STR."InvoiceItem{id=\{id}, itemDescription='\{itemDescription}', quantity=\{quantity}, unitPrice=\{unitPrice}, lineTotal=\{lineTotal}}";
    }
}