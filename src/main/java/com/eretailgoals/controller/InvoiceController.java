package com.eretailgoals.controller;

import com.eretailgoals.entity.Invoice;
import com.eretailgoals.entity.InvoiceItem;
import com.eretailgoals.service.InvoiceService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Invoice management operations
 * Provides endpoints for CRUD operations on invoices and invoice-related functionality
 */
@RestController
@RequestMapping("/invoices")
@Tag(name = "Invoice Management", description = "APIs for managing invoices and invoice items")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "Create a new invoice", description = "Creates a new invoice for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Invoice number already exists")
    })
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        Invoice createdInvoice = invoiceService.createInvoice(invoice);
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all invoices", description = "Retrieves a list of all invoices")
    @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoice by ID", description = "Retrieves a specific invoice by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(
            @Parameter(description = "Invoice ID") @PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Get invoice by number", description = "Retrieves a specific invoice by its number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoiceByNumber(
            @Parameter(description = "Invoice number") @PathVariable String invoiceNumber) {
        return invoiceService.getInvoiceByNumber(invoiceNumber)
                .map(invoice -> ResponseEntity.ok(invoice))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update invoice", description = "Updates an existing invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(
            @Parameter(description = "Invoice ID") @PathVariable Long id,
            @Valid @RequestBody Invoice invoiceDetails) {
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDetails);
        return ResponseEntity.ok(updatedInvoice);
    }

    @Operation(summary = "Delete invoice", description = "Permanently deletes an invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Invoice deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "409", description = "Invoice has payments and cannot be deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(
            @Parameter(description = "Invoice ID") @PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel invoice", description = "Cancels an invoice")
    @ApiResponse(responseCode = "200", description = "Invoice cancelled successfully")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Invoice> cancelInvoice(
            @Parameter(description = "Invoice ID") @PathVariable Long id) {
        Invoice cancelledInvoice = invoiceService.cancelInvoice(id);
        return ResponseEntity.ok(cancelledInvoice);
    }

    @Operation(summary = "Get invoices by user", description = "Retrieves all invoices for a specific user")
    @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<Invoice> invoices = invoiceService.getInvoicesByUser(userId);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoices by status", description = "Retrieves invoices filtered by status")
    @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(
            @Parameter(description = "Invoice status") @PathVariable Invoice.InvoiceStatus status) {
        List<Invoice> invoices = invoiceService.getInvoicesByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoices by date range", description = "Retrieves invoices within a date range")
    @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully")
    @GetMapping("/date-range")
    public ResponseEntity<List<Invoice>> getInvoicesByDateRange(
            @Parameter(description = "Start date (YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Invoice> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get overdue invoices", description = "Retrieves all overdue invoices")
    @ApiResponse(responseCode = "200", description = "Overdue invoices retrieved successfully")
    @GetMapping("/overdue")
    public ResponseEntity<List<Invoice>> getOverdueInvoices() {
        List<Invoice> invoices = invoiceService.getOverdueInvoices();
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoices with outstanding amounts", 
               description = "Retrieves invoices that have unpaid amounts")
    @ApiResponse(responseCode = "200", description = "Outstanding invoices retrieved successfully")
    @GetMapping("/outstanding")
    public ResponseEntity<List<Invoice>> getInvoicesWithOutstandingAmounts() {
        List<Invoice> invoices = invoiceService.getInvoicesWithOutstandingAmounts();
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Search invoices", description = "Search invoices with pagination and filtering")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<Invoice>> searchInvoices(
            @Parameter(description = "User ID filter") @RequestParam(required = false) Long userId,
            @Parameter(description = "Admin ID filter") @RequestParam(required = false) Long adminId,
            @Parameter(description = "Invoice status filter") @RequestParam(required = false) Invoice.InvoiceStatus status,
            @Parameter(description = "Start date filter") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date filter") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Search term") @RequestParam(required = false) String searchTerm,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Invoice> invoices = invoiceService.searchInvoices(userId, adminId, status, startDate, endDate, searchTerm, pageable);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get recent invoices", description = "Retrieves the most recently created invoices")
    @ApiResponse(responseCode = "200", description = "Recent invoices retrieved successfully")
    @GetMapping("/recent")
    public ResponseEntity<List<Invoice>> getRecentInvoices(
            @Parameter(description = "Number of invoices to retrieve") @RequestParam(defaultValue = "10") int limit) {
        List<Invoice> invoices = invoiceService.getRecentInvoices(limit);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Add payment to invoice", description = "Records a payment against an invoice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payment amount"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PostMapping("/{id}/payments")
    public ResponseEntity<Invoice> addPayment(
            @Parameter(description = "Invoice ID") @PathVariable Long id,
            @RequestBody Map<String, BigDecimal> paymentData) {
        
        BigDecimal paymentAmount = paymentData.get("amount");
        if (paymentAmount == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Invoice updatedInvoice = invoiceService.addPayment(id, paymentAmount);
        return ResponseEntity.ok(updatedInvoice);
    }

    @Operation(summary = "Add invoice item", description = "Adds a new item to an existing invoice")
    @ApiResponse(responseCode = "200", description = "Invoice item added successfully")
    @PostMapping("/{id}/items")
    public ResponseEntity<Invoice> addInvoiceItem(
            @Parameter(description = "Invoice ID") @PathVariable Long id,
            @Valid @RequestBody InvoiceItem item) {
        Invoice updatedInvoice = invoiceService.addInvoiceItem(id, item);
        return ResponseEntity.ok(updatedInvoice);
    }

    @Operation(summary = "Remove invoice item", description = "Removes an item from an invoice")
    @ApiResponse(responseCode = "200", description = "Invoice item removed successfully")
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Invoice> removeInvoiceItem(
            @Parameter(description = "Invoice ID") @PathVariable Long id,
            @Parameter(description = "Invoice item ID") @PathVariable Long itemId) {
        Invoice updatedInvoice = invoiceService.removeInvoiceItem(id, itemId);
        return ResponseEntity.ok(updatedInvoice);
    }

    @Operation(summary = "Generate next invoice number", description = "Generates the next available invoice number")
    @ApiResponse(responseCode = "200", description = "Invoice number generated successfully")
    @GetMapping("/next-number")
    public ResponseEntity<Map<String, String>> generateNextInvoiceNumber() {
        String nextNumber = invoiceService.generateNextInvoiceNumber();
        return ResponseEntity.ok(Map.of("invoiceNumber", nextNumber));
    }

    @Operation(summary = "Get invoices due within days", 
               description = "Retrieves invoices that are due within the specified number of days")
    @ApiResponse(responseCode = "200", description = "Due invoices retrieved successfully")
    @GetMapping("/due-within/{days}")
    public ResponseEntity<List<Invoice>> getInvoicesDueWithinDays(
            @Parameter(description = "Number of days") @PathVariable int days) {
        List<Invoice> invoices = invoiceService.getInvoicesDueWithinDays(days);
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get invoice statistics", description = "Retrieves invoice statistics and totals")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getInvoiceStatistics() {
        Map<String, Object> statistics = Map.of(
            "totalOpen", invoiceService.countInvoicesByStatus(Invoice.InvoiceStatus.OPEN),
            "totalPaid", invoiceService.countInvoicesByStatus(Invoice.InvoiceStatus.PAID),
            "totalOverdue", invoiceService.countInvoicesByStatus(Invoice.InvoiceStatus.OVERDUE),
            "totalOutstanding", invoiceService.calculateTotalOutstandingAmount()
        );
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Get total outstanding amount by user", 
               description = "Calculates the total outstanding amount for a specific user")
    @ApiResponse(responseCode = "200", description = "Outstanding amount calculated successfully")
    @GetMapping("/user/{userId}/outstanding")
    public ResponseEntity<Map<String, BigDecimal>> getTotalOutstandingAmountByUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        BigDecimal outstandingAmount = invoiceService.calculateTotalOutstandingAmountByUser(userId);
        return ResponseEntity.ok(Map.of("outstandingAmount", outstandingAmount));
    }
}