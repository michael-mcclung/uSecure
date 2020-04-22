// packages
package com.example.usecure;

// register user information class
public class RegisterUserInformation {

    // vaiables
    private String email, password, lname, address, phoneNum;
    String fname;

    // function used to set user information
    public RegisterUserInformation(String email, String password, String fname, String lname, String address, String phoneNum) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    // getting user email
    public String getEmail() {
        return email;
    }

    // setting user email
    public void setEmail(String email) {
        this.email = email;
    }

}
