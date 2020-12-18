package com.example.ravelelectronics.model;

public class CusprofileModel {

    private  String customer_name;
    private  String address;
    private  String address2;
    private  String city;
    private  String postal_code;
    private  String contact_no;

    public CusprofileModel(String customer_name, String address, String address2, String city, String postal_code, String contact_no) {
        this.customer_name = customer_name;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.postal_code = postal_code;
        this.contact_no = contact_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}
