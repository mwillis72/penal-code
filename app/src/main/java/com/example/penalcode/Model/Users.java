package com.example.penalcode.Model;

public class Users {
    private  String Name, Phone, Password, image;

    public Users() {
    }

    public Users(String name, String phone, String password, String image) {
        this.Name = name;
        this.Phone = phone;
        this.Password = password;
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
