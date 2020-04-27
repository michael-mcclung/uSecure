// packages
package com.example.usecure;

// register user information class
public class RegisterUserInformation {

    // vaiables
    private String email, password, lname;
    private String address, phoneNum, fname;

    // function used to set user information
    public RegisterUserInformation(String email, String password, String fname, String lname, String address, String phoneNum) {
        getEmail();
        setEmail(email);

        getAddress();
        setAddress(address);

        getFname();
        setFname(fname);

        getLname();
        setLname(lname);

        getPassword();
        setPassword(password);

        getPhoneNum();
        setPhoneNum(phoneNum);
    }

    // getting user email
    public String getEmail() {
        return email;
    }

    // setting user email
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

}
