/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eretailgoals.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maqsud
 */
public class UserBO {

    private long id;
    
    private Long adminID;

    private String firstname;

    private String lastname;

    private String email;

    private String companyname;

    private String username;

    private String password;

    private String address;

    private String postcode;

    private String shippingAddress;

    private String shippingPostcode;

    private String phoneOffice;

    private String phoneHome;

    private String mobile;

    private String vat;

    private String fax;

    private String userType;

    private Timestamp dateCreated;

    private Timestamp loginTimestamp;

    public UserBO(){
        
    }

    public UserBO(ResultSet rs, String usertype){
        try {
            this.id=rs.getLong("id");
            this.adminID = rs.getLong("admin_id");
            if (usertype.equals(UserType.ADMIN.toString())) {
                this.username=rs.getString("username");
                this.loginTimestamp = rs.getTimestamp("login_ts");
                this.firstname = rs.getString("firstname");
                this.lastname = rs.getString("lastname");
                this.address = rs.getString("address");
                this.postcode = rs.getString("postcode");
                this.companyname = rs.getString("company_name");
                this.email = rs.getString("email");
            }else{
                this.firstname = rs.getString("first_name");
                this.lastname = rs.getString("last_name");
                this.address = rs.getString("billing_address");
                this.postcode = rs.getString("billing_postcode");
                this.companyname = rs.getString("company_name");
                this.email = rs.getString("email");
                this.fax = rs.getString("fax");
                this.mobile = rs.getString("phone_mobile");
                this.phoneHome = rs.getString("phone_home");
                this.phoneOffice = rs.getString("phone_office");
                this.vat = rs.getString("vat_number");
                this.userType = rs.getString("user_type");
                this.dateCreated = rs.getTimestamp("date_created");
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(UserBO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the dateCreated
     */
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the loginTimestamp
     */
    public Timestamp getLoginTimestamp() {
        return loginTimestamp;
    }

    /**
     * @param loginTimestamp the loginTimestamp to set
     */
    public void setLoginTimestamp(Timestamp loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }
   

    public enum UserType{ADMIN,SUPPLIER,CLIENT};

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
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the companyName
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyname(String companyName) {
        this.companyname = companyName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the shippingAddress
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @param shippingAddress the shippingAddress to set
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * @return the shippingPostcode
     */
    public String getShippingPostcode() {
        return shippingPostcode;
    }

    /**
     * @param shippingPostcode the shippingPostcode to set
     */
    public void setShippingPostcode(String shippingPostcode) {
        this.shippingPostcode = shippingPostcode;
    }

    /**
     * @return the phoneOffice
     */
    public String getPhoneOffice() {
        return phoneOffice;
    }

    /**
     * @param phoneOffice the phoneOffice to set
     */
    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    /**
     * @return the phoneHome
     */
    public String getPhoneHome() {
        return phoneHome;
    }

    /**
     * @param phoneHome the phoneHome to set
     */
    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the vat
     */
    public String getVat() {
        return vat;
    }

    /**
     * @param vat the vat to set
     */
    public void setVat(String vat) {
        this.vat = vat;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setAdminID(Long adminID) {
        this.adminID = adminID;
    }

    public Long getAdminID() {
        return adminID;
    }
    
}