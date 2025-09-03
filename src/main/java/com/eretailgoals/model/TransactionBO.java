/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maqsud
 */
public class TransactionBO extends BankBO{

    private long id;

    private long userID;
    
    private long invoiceID;

    private long bankID;

    private BigDecimal endingBalance;

    private BigDecimal transactionAmount;

    private String notes;

    /*TODO: change this from String to enum TransactionType*/
    private String transactionType;
    
    private int expenseType;

    private String type;

    private String transactionDate;
    
    private String amountReceived;
    
    private String amountTransfered;

    public TransactionBO(){
        
    }

    public TransactionBO(long adminID, long userID, long bankID, BigDecimal endingBalance, BigDecimal transactionAmount, String notes, String transactionType, int expenseType, String transactionDate, String amountReceived, String amountTransfered) {
        setAdminID(adminID);
        this.userID = userID;
        this.bankID = bankID;
        this.endingBalance = endingBalance;
        this.transactionAmount = transactionAmount;
        this.notes = notes;
        this.transactionType = transactionType;
        this.expenseType = expenseType;
        this.transactionDate = transactionDate;
        this.amountReceived = amountReceived;
        this.amountTransfered = amountTransfered;
    }

    
    public TransactionBO(ResultSet rs){
        try {
            //this.bankID = rs.getLong("bank_id");
            this.id = rs.getLong("id");
            this.currentBalance = rs.getBigDecimal("current_balance");
            this.notes = rs.getString("notes");
            this.amountReceived = rs.getString("amount_received");
            this.amountTransfered = rs.getString("amount_transfered");
            this.endingBalance = rs.getBigDecimal("ending_balance");
            if(amountTransfered==null || amountTransfered.equals("0")){
                amountTransfered="";
            }
            if(amountReceived==null || amountReceived.equals("0")){
                amountReceived="";
            }
            this.transactionType =  rs.getString("transaction_type").equals("0")?"":rs.getString("transaction_type");
            this.type = rs.getString("type");
            setAccountName(rs.getString("account_name"));
            this.transactionDate = rs.getString("transaction_date");
            
        } catch (SQLException ex) {
            Logger.getLogger(TransactionBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }


    @Override
    public Long getId() {
        return id;
    }

    
    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    /**
     * @return the invoiceID
     */
    public long getInvoiceID() {
        return invoiceID;
    }

    /**
     * @param invoiceID the invoiceID to set
     */
    public void setInvoiceID(long invoiceID) {
        this.invoiceID = invoiceID;
    }

    /**
     * @return the bankID
     */
    public long getBankID() {
        return bankID;
    }

    /**
     * @param bankID the bankID to set
     */
    public void setBankID(long bankID) {
        this.bankID = bankID;
    }

    /**
     * @return the amount
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param amount the amount to set
     */
    public void setTransactionAmount(BigDecimal amount) {
        this.transactionAmount = amount;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        if(notes==null)
            return "";
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the transactionType
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the transactionDate
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the expenseType
     */
    public int getExpenseType() {
        return expenseType;
    }

    /**
     * @param expenseType the expenseType to set
     */
    public void setExpenseType(int expenseType) {
        this.expenseType = expenseType;
    }

    /**
     * @return the userID
     */
    public long getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(long userID) {
        this.userID = userID;
    }
    /**
     * REC=Receive payment from client
     * PAY=Make payment to supplier
     */
    public enum TransactionType{REC,PAY};

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountTransfered(String amountTransfered) {
        this.amountTransfered = amountTransfered;
    }

    public String getAmountTransfered() {
        return amountTransfered;
    }



}