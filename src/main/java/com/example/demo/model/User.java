package com.example.demo.model;
import javax.persistence.*;

@Entity
//defining class name as Table name
@Table (name="users")
public class User {
    @Id
    @Column(name = "user_no")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer userNo;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "created")
    private String created;
    @Column(name = "updated")
    private String updated;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getUserNo() {
        return userNo;
    }

    public void setUserNo(Integer userNo) {
        this.userNo = userNo;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
