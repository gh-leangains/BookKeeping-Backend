/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Maqsud
 */
public class ExpensesBO {

    private Long id;

    private String expenseType;

    private BigDecimal expenseAmount;

    private Long userid;

    private String expenseNotes;

    private String expenseCategory;

    private Date expenseDate;

    /**
     * @return the expenseAmount
     */
    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    /**
     * @param expenseAmount the expenseAmount to set
     */
    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    /**
     * @return the expenseCategory
     */
    public String getExpenseCategory() {
        return expenseCategory;
    }

    /**
     * @param expenseCategory the expenseCategory to set
     */
    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    /**
     * @return the expenseNotes
     */
    public String getExpenseNotes() {
        return expenseNotes;
    }

    /**
     * @param expenseNotes the expenseNotes to set
     */
    public void setExpenseNotes(String expenseNotes) {
        this.expenseNotes = expenseNotes;
    }

    /**
     * @return the expenseDate
     */
    public Date getExpenseDate() {
        return expenseDate;
    }

    /**
     * @param expenseDate the expenseDate to set
     */
    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public enum ExpenseType{CreditCard, Cash, Cheque, BankTransfer}

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the expenseType
     */
    public String getExpenseType() {
        return expenseType;
    }

    /**
     * @param expenseType the expenseType to set
     */
    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    /**
     * @return the purchasedFrom
     */
    public Long getPurchasedFrom() {
        return userid;
    }

    /**
     * @param purchasedFrom the purchasedFrom to set
     */
    public void setUserid(Long purchasedFrom) {
        this.userid = purchasedFrom;
    }

  

}