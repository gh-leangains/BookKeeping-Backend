/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Maqsud
 */
public class InvoiceItemsBO extends InvoiceBO{


    private final static Logger logger = Logger.getLogger(InvoiceItemsBO.class.getName());

    private String invoiceName;

    private Integer quantity;

    private BigDecimal unitPrice;
    
    private Long itemId;

    public InvoiceItemsBO() {
    }


    public InvoiceItemsBO(ResultSet rs){
        try {
            this.invoiceName=rs.getString("item_name");
            this.quantity=rs.getInt("quantity");
            this.unitPrice=rs.getBigDecimal("unit_price");
            this.itemId = rs.getLong("itemid");
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("Error fetching results "+ ex);
        }
    }

    public InvoiceItemsBO(ResultSet rs, String type){
        try {
            this.invoiceAmount = rs.getBigDecimal("invoice_amount");
            this.adminID = rs.getLong("admin_id");
            this.invoiceDate=rs.getString("date_created");
            if("invoice".equalsIgnoreCase(type)){
                this.dueDate=rs.getString("invoice_duedate");
            }
            this.invoiceNote=rs.getString("invoice_note");
            this.invoiceNumber=rs.getString("invoice_number");
            this.userid=rs.getLong("user_id");
            this.invoiceStatus=rs.getString("invoice_status");
            this.invoiceID=rs.getLong("id");
            this.invoiceName=rs.getString("item_name");
            this.quantity=rs.getInt("quantity");
            this.unitPrice=rs.getBigDecimal("unit_price");
            this.itemId = rs.getLong("itemid");
            invoicePaidAmount = rs.getBigDecimal("invoice_paid_amount");
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error("Error fetching results "+ ex);
        }
    }

    /**
     * @return the invoiceName
     */
    public String getInvoiceName() {
        return invoiceName;
    }

    /**
     * @param invoiceName the invoiceName to set
     */
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }

    

}