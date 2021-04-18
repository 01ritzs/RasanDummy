package com.du.de.rasandummy.cart;

import com.du.de.rasandummy.RoomDatabase.Product;

interface OnCartProductSelectListener {
    void onAdd(Product product);
    void onSubtract(Product product);
}
