package codesgesture.app.arshitgr.Models;

import java.io.Serializable;

public class CustomerModel implements Serializable {
    private String Id;
    private String customer_id;
    private String customer_temp_id = null;
    private String customer_name;
    private String customer_email;
    private String customer_mobileno;
    private String customer_gender;
    private String customer_dob;
    private String customer_password;
    private String customer_profilephoto;
    private String email_verification_code;
    private String email_verified;
    private String mobileno_verified;
    private String otp;
    private String customer_date;
    private String customer_time;
    private String customer_status;

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    private String token_id;


    // Getter Methods

    public String getId() {
        return Id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getCustomer_temp_id() {
        return customer_temp_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getCustomer_mobileno() {
        return customer_mobileno;
    }

    public String getCustomer_gender() {
        return customer_gender;
    }

    public String getCustomer_dob() {
        return customer_dob;
    }

    public String getCustomer_password() {
        return customer_password;
    }

    public String getCustomer_profilephoto() {
        return customer_profilephoto;
    }

    public String getEmail_verification_code() {
        return email_verification_code;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public String getMobileno_verified() {
        return mobileno_verified;
    }

    public String getOtp() {
        return otp;
    }

    public String getCustomer_date() {
        return customer_date;
    }

    public String getCustomer_time() {
        return customer_time;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    // Setter Methods

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setCustomer_temp_id(String customer_temp_id) {
        this.customer_temp_id = customer_temp_id;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public void setCustomer_mobileno(String customer_mobileno) {
        this.customer_mobileno = customer_mobileno;
    }

    public void setCustomer_gender(String customer_gender) {
        this.customer_gender = customer_gender;
    }

    public void setCustomer_dob(String customer_dob) {
        this.customer_dob = customer_dob;
    }

    public void setCustomer_password(String customer_password) {
        this.customer_password = customer_password;
    }

    public void setCustomer_profilephoto(String customer_profilephoto) {
        this.customer_profilephoto = customer_profilephoto;
    }

    public void setEmail_verification_code(String email_verification_code) {
        this.email_verification_code = email_verification_code;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public void setMobileno_verified(String mobileno_verified) {
        this.mobileno_verified = mobileno_verified;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setCustomer_date(String customer_date) {
        this.customer_date = customer_date;
    }

    public void setCustomer_time(String customer_time) {
        this.customer_time = customer_time;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }
}