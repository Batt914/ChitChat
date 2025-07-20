package com.example.chitchat;

public class HelperClass {
    String id,username,email,pass,repass, imageuri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRepass() {
        return repass;
    }

    public void setRepass(String repass) {
        this.repass = repass;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public HelperClass(String username, String email, String pass, String repass, String id, String imageuri) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.repass = repass;
        this.id = id;
        this.imageuri = imageuri;
    }

}
