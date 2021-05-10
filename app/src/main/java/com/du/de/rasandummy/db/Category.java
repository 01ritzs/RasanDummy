package com.du.de.rasandummy.db;

import java.util.List;

public class Category {
    private String title;
    private List<Product> products;

    public Category() {
    }

    public Category(String title, List<Product> products) {
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
}
