/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maqsud
 */
public class InvoiceBO {

    private final static Logger logger = Logger.getLogger(InvoiceBO.class.getName());

    protected long invoiceID;

    protected long adminID;

    protected long userid;

    protected String invoiceDate;

    protected String invoiceNumber;

    protected String dueDate;

    protected String invoiceType;

    protected String invoiceNote;

    protected BigDecimal invoiceAmount;
    
    protected BigDecimal vat;

    protected BigDecimal invoicePaidAmount;

    protected String invoiceStatus;
    
    protected UserBO user;
    
    private List<InvoiceItemsBO> invoiceItems;

    /**
     * @return the invoicePaidAmount
     */
    public BigDecimal getInvoicePaidAmount() {
        return invoicePaidAmount;
    }

    /**
     * @param invoicePaidAmount the invoicePaidAmount to set
     */
    public void setInvoicePaidAmount(BigDecimal invoicePaidAmount) {
        this.invoicePaidAmount = invoicePaidAmount;
    }

    public enum InvoiceStatus{CLOSE,PARTPAID,OPEN};

    public InvoiceBO(){}
    public InvoiceBO(ResultSet rs, String type){
        try {
                this.invoiceAmount = rs.getBigDecimal("invoice_amount");
                this.adminID = rs.getLong("admin_id");
//              this.invoiceDate=rs.getString("date_created");
                
//              this.invoiceName=rs.getString("invoice_name");
                this.invoiceNote=rs.getString("invoice_note");
                this.invoiceNumber=rs.getString("invoice_number");
                this.userid=rs.getLong("user_id");
                this.invoiceStatus=rs.getString("invoice_status");
                this.invoiceID=rs.getLong("id");
                this.invoicePaidAmount = rs.getBigDecimal("invoice_paid_amount");
                SimpleDateFormat sdf = new SimpleDateFormat("EEE,MMM dd,''yyyy");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                Date dt = df.parse(rs.getString("date_created"));
                
                this.invoiceDate = sdf.format(dt);
                if("invoice".equalsIgnoreCase(type)){
                    Date dat = df.parse(rs.getString("invoice_duedate"));
                    this.dueDate = sdf.format(dat);
                }
                

            
        } catch (ParseException ex) {
            Logger.getLogger(InvoiceBO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, null, ex);
        }
    }
    /**
     * use this constructor to fetch invoice details.
     * Populates the user asssociated with the invoice
     * also fetches individual items for the invoice
     * @param rs
     * @param usr 
     */
    public InvoiceBO(ResultSet rs, UserBO usr){
        try {
                this.invoiceAmount = rs.getBigDecimal("invoice_amount");
                this.vat = rs.getBigDecimal("vat");
                this.adminID = rs.getLong("admin_id");
                this.invoiceNote=rs.getString("invoice_note");
                this.invoiceNumber=rs.getString("invoice_number");
                this.userid=rs.getLong("user_id");
                this.invoiceStatus=rs.getString("invoice_status");
                this.invoiceID=rs.getLong("id");
                this.invoicePaidAmount = rs.getBigDecimal("invoice_paid_amount");
                user = new UserBO(rs, "CLIENT");
                invoiceItems = new ArrayList<InvoiceItemsBO>();
                invoiceItems.add(new InvoiceItemsBO(rs));
                SimpleDateFormat sdf = new SimpleDateFormat("EEE,MMM dd,''yyyy");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                Date dt = df.parse(rs.getString("date_created"));
                
                this.invoiceDate = sdf.format(dt);
                
                Date dat = df.parse(rs.getString("invoice_duedate"));
                this.dueDate = sdf.format(dat);
                
            
        } catch (ParseException ex) {
            Logger.getLogger(InvoiceBO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, null, ex);
        }
    }

   
    /**
     * @return the invoiceDate
     */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate the invoiceDate to set
     */
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return the dueDate
     */
    public String getDueDate() {
        if(dueDate==null || dueDate.isEmpty()){
            dueDate="0000-00-00 00:00:00";
        }
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the invoiceType
     */
    public String getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * @return the invoiceNote
     */
    public String getInvoiceNote() {
        return invoiceNote;
    }

    /**
     * @param invoiceNote the invoiceNote to set
     */
    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }

    /**
     * @return the invoiceAmount
     */
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * @param invoiceAmount the invoiceAmount to set
     */
    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getUserid() {
        return userid;
    }

    /**
     * @return the invoiceStatus
     */
    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    /**
     * @param invoiceStatus the invoiceStatus to set
     */
    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
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

    public void setAdminID(long adminID) {
        this.adminID = adminID;
    }

    public long getAdminID() {
        return adminID;
    }

    public void setInvoiceItems(List<InvoiceItemsBO> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public List<InvoiceItemsBO> getInvoiceItems() {
        return invoiceItems;
    }

    public void setUser(UserBO user) {
        this.user = user;
    }

    public UserBO getUser() {
        return user;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getVat() {
        return vat;
    }
    

}