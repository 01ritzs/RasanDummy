package com.du.de.rasandummy.cart;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.RoomDatabase.Product;
import com.du.de.rasandummy.util.AppData;

import java.util.LinkedHashMap;

public class CartActivity extends AppCompatActivity implements OnCartProductSelectListener {

    RecyclerView rvItems;
    CartProductsAdapter adapter;
    ImageView ivback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ivback = findViewById(R.id.ivBack);
        ivback.setOnClickListener(view -> onBackPressed());
        initRecyclerView();
    }

    private void initRecyclerView() {
        rvItems = findViewById(R.id.rvCart);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CartProductsAdapter(AppData.getInstance().getSelectedProduct(), this);
        rvItems.setAdapter(adapter);
    }

    @Override
    public void onAdd(Product product) {
        // Increase count for the product
        LinkedHashMap<Product, Integer> map = AppData.getInstance().getSelectedProduct();
        int count = map.get(product);
        count++;
        map.put(product, count);
        // Update list
        adapter.setList(map);
    }

    @Override
    public void onSubtract(Product product) {
        LinkedHashMap<Product, Integer> map = AppData.getInstance().getSelectedProduct();
        // Check if the count is 0. If yes, then remove the item from map
        int count = map.get(product);
        if (count <= 1) {
            map.remove(product);
        } else {
            count--;
            map.put(product, count);
        }
        adapter.setList(map);
    }
}
