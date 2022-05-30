package com.example.application.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sijan Bhandari
 */
public class UserModel {
    String uid;

    String name;
    String username;
    String emailID;
    String password;
    String imageUrl;
    Status status;
    List<Status> roles;

    public UserModel() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    //validate user
    public boolean validateUser() {
        if (this.getName() == null || this.getName().isEmpty()) {
            return false;
        }
        if (this.getUsername() == null || this.getUsername().isEmpty()) {
            return false;
        }
        if (this.getEmailID() == null || this.getEmailID().isEmpty()) {
            return false;
        }
        if (this.getPassword() == null || this.getPassword().isEmpty()) {
            return false;
        }
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "UserModel{" + "uid=" + uid + ", name=" + name + ", username=" + username + ", emailID=" + emailID + ", password=" + password + ", imageUrl=" + imageUrl + ", status=" + status + '}';
    }

    public List<Status> getRoles() {
        ArrayList<Status> arrayList = new ArrayList<Status>();
        arrayList.add(status);
        return arrayList;

    }

    public void setRoles(List<Status> roles) {
        this.roles = roles;
    }

    public enum Status {
        USER, ADMIN
    }
}
