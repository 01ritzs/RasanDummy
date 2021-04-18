package com.du.de.rasandummy.util;

import android.app.Activity;

import com.du.de.rasandummy.RoomDatabase.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppData extends Activity {

    private static AppData appData = new AppData();

    private List<Product> products = new ArrayList<>();
    private LinkedHashMap<Product, Integer> selectedProduct = new LinkedHashMap<>();

    private AppData() {
    }

    public static AppData getInstance() {
        return appData;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public LinkedHashMap<Product, Integer> getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * Function to Add product in map and if already present its need to be increamented.
     *
     * @param product
     */
    public void addProductToCart(Product product) {
        // Check if the product is already added in map
        if (selectedProduct.containsKey(product)) {
            // Increment value by 1
            int count = selectedProduct.get(product);
            selectedProduct.put(product, ++count);
        } else {
            selectedProduct.put(product, 1);
        }
    }
}
