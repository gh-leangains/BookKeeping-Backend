/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maqsud
 */
public class BankBO {

    private static final Logger logger =  Logger.getLogger(BankBO.class.getName());

    private Long id;

    private Long adminID;

    protected String accountType;

    protected Long accountNumber;

    private String sortCode;

    private String accountName;

    protected BigDecimal openingBalance;

    protected BigDecimal currentBalance;

    private Date accountAdded;

    /**
     * @return the currentBalance
     */
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    /**
     * @param currentBalance the currentBalance to set
     */
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public enum AccountType{Current, Savings, Cash}


    public BankBO(){

    }

    public BankBO(ResultSet rs){
        try {
            this.id = rs.getLong("id");
            this.adminID = rs.getLong("admin_id");
            this.accountName = rs.getString("account_name");
            this.accountNumber = rs.getLong("account_number");
            this.openingBalance = rs.getBigDecimal("opening_balance");
            this.sortCode = rs.getString("sort_code");
            this.accountType = rs.getString("account_type");
            this.currentBalance=rs.getBigDecimal("current_balance");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
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
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the sortCode
     */
    public String getSortCode() {
        return sortCode;
    }

    /**
     * @param sortCode the sortCode to set
     */
    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the openingBalance
     */
    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    /**
     * @param openingBalance the openingBalance to set
     */
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    /**
     * @return the accountAdded
     */
    public Date getAccountAdded() {
        return accountAdded;
    }

    /**
     * @param accountAdded the accountAdded to set
     */
    public void setAccountAdded(Date accountAdded) {
        this.accountAdded = accountAdded;
    }

    public void setAdminID(long adminID) {
        this.adminID = adminID;
    }

    public long getAdminID() {
        if(adminID==null)
            return 0;
        return adminID;
    }


    

}