package com.du.de.rasandummy.cart;

import com.du.de.rasandummy.db.Product;

interface OnCartProductSelectListener {
    void onAdd(Product product);
    void onSubtract(Product product);
}
