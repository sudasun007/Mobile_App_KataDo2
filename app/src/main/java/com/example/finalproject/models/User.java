package com.example.finalproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    @SerializedName("Email")
    private String email;
    @Expose
    @SerializedName("Password")
    private String password;
    @Expose
    @SerializedName("F_name")
    private String f_name;
    @Expose
    @SerializedName("L_name")
    private String l_name;
    @Expose
    @SerializedName("Active_status")
    private boolean active_status =false;
    public User(){
    }
    public User(String email, String password, String f_name, String l_name){
        this.email = email;
        this.password=password;
        this.f_name=f_name;
        this.l_name=l_name;

    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public boolean isActive_status() {
        return active_status;
    }
    public void setActive_status(boolean active_status) {
        this.active_status = active_status;
    }
}
