package com.example.penalcode.Model;

public class Crimes {
    private String number, name, description,cid;

    public Crimes() {
    }

    public Crimes(String number, String cid, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.cid = cid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
