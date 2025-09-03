/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maqsud
 */
public class UserTransactions extends InvoiceBO{

    private final static Logger logger = Logger.getLogger(UserTransactions.class.getName());

    private long id;

    private long adminid;

    private long userid;

    private String transactionType;

    private BigDecimal amount;

    private String notes;

    private Timestamp transactionDate;

    private long paymentReference;


    public UserTransactions(ResultSet rs, String type){
        try {
                this.transactionDate = rs.getTimestamp("transaction_date");
                this.transactionType = rs.getString("transaction_type");
                this.amount = rs.getBigDecimal("amount_received");
                this.notes = rs.getString("notes");
                this.invoiceStatus="Payment";
                this.paymentReference=rs.getLong("payment_reference");
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the adminid
     */
    public long getAdminid() {
        return adminid;
    }

    /**
     * @param adminid the adminid to set
     */
    public void setAdminid(long adminid) {
        this.adminid = adminid;
    }

    /**
     * @return the userid
     */
    public long getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(long userid) {
        this.userid = userid;
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
     * @return the amount
     */
    public BigDecimal getamount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setamount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the transactionDate
     */
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        if(notes==null || notes.isEmpty()){
            notes="None";
        }
        return notes;
    }

    /**
     * @return the paymentReference
     */
    public long getPaymentReference() {
        return paymentReference;
    }

    /**
     * @param paymentReference the paymentReference to set
     */
    public void setPaymentReference(long paymentReference) {
        this.paymentReference = paymentReference;
    }

    

}