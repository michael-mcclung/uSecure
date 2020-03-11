package com.example.usecure;

import android.widget.EditText;

import java.util.Random;

public class RegisterUserInformation {

    public Object voicePath;
    private String email, password, fname, lname, address, phoneNum;

    public RegisterUserInformation() {

    }

    public RegisterUserInformation(String email, String password, String fname, String lname, String address, String phoneNum) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
