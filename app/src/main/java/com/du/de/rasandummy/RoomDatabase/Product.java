package com.du.de.rasandummy.RoomDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    int id;
    @SerializedName("image")
    String image;
    @SerializedName("name")
    String name;
    @SerializedName("quantity")
    int quantity;
    @SerializedName("rate")
    int rate;

    public Product(String name, int quantity, int rate) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}