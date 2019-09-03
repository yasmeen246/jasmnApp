package com.example.jasmn.jasmnapp;

import com.google.firebase.auth.FirebaseUser;

public class users {
    public String name;
    public String status;
    public String image;
String Key;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public users( ) {


    }

    public users(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
