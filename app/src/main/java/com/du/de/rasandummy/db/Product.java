package com.du.de.rasandummy.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    int id;
    @SerializedName("image")
    String image;
    @SerializedName("name")
    String name;
    @SerializedName("quantity")
    String quantity;
    @SerializedName("searchkeys")
    String searchkeys;
    @SerializedName("rate")
    int rate;
    @SerializedName("mrp")
    int mrp;

    public Product(String image, String name, String quantity, int rate, int mrp, String searchkeys) {
        this.image = image;
        this.searchkeys = searchkeys;
        this.mrp = mrp;
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
    }

    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSearchkeys() {
        return searchkeys;
    }

    public void setSearchkeys(String searchkeys) {
        this.searchkeys = searchkeys;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(image, product.image) &&
                Objects.equals(name, product.name) &&
                Objects.equals(quantity, product.quantity) &&
                Objects.equals(mrp, product.mrp) &&
                Objects.equals(searchkeys, product.searchkeys) &&
                Objects.equals(rate, product.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, name, quantity, rate);
    }
}
