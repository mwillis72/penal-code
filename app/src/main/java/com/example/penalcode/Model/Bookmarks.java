package com.example.penalcode.Model;

public class Bookmarks
 {
    private String cid, name, number, description;

     public Bookmarks() {
     }

     public Bookmarks(String cid, String name, String number, String description) {
         this.cid = cid;
         this.name = name;
         this.number = number;
         this.description = description;
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

     public String getNumber() {
         return number;
     }

     public void setNumber(String number) {
         this.number = number;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     /*public Bookmarks() {
    }

    public Bookmarks(String cid, String pname, String price, String quantity, String discount) {
        this.cid = cid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }*/
}
