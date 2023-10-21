package com.du.de.rasandummy.util

import android.content.Context
import com.du.de.rasandummy.db.Category
import com.du.de.rasandummy.db.Product

class RecentManager(private val context: Context) {

    fun addProduct(product: Product) {
        val spHelper = SharedPreferenceHelper(context)
        var products: ArrayList<Product> = spHelper.getProducts() as ArrayList<Product>
        if (products == null) {
            products = ArrayList()
        }
        if (!products.contains(product)) {
            products.add(0, product)
        }
        spHelper.save(products)
    }

    fun addRecentCategory(categories: MutableList<Category>): MutableList<Category> {
        val products = SharedPreferenceHelper(context).getProducts()
        if (products.isNotEmpty()) {
            if (categories.size > 0) {
                categories.add(0, Category(100, "", Constants.RECENT, products))
            }
        }
        return categories
    }
}