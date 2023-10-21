package com.du.de.rasandummy.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    private String title;
    @SerializedName("id")
    private int id;
    @SerializedName("searchkeys")
    private String searchkeys;
    @SerializedName("products")
    private List<Product> products;

    public Category() {
    }

    public Category(int id, String searchkeys, String title, List<Product> products) {
        this.id = id;
        this.searchkeys = searchkeys;
        this.title = title;
        this.products = products;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchkeys() {
        return searchkeys;
    }

    public void setSearchkeys(String searchkeys) {
        this.searchkeys = searchkeys;
    }
}
