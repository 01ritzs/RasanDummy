package com.du.de.rasandummy.cart;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.DummyData;
import com.du.de.rasandummy.R;
import com.du.de.rasandummy.RoomDatabase.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements OnCartProductSelectListener {

    RecyclerView rvItems;
    CartProductsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initRecyclerView();
    }

    private void initRecyclerView() {
        rvItems = findViewById(R.id.rvCart);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CartProductsAdapter(DummyData.products, this);
        rvItems.setAdapter(adapter);
    }

    @Override
    public void onAdd(Product product) {
        Toast.makeText(this, "Add click for" + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubtract(Product product) {
        Toast.makeText(this, "Subtract click for" + product.getName(), Toast.LENGTH_SHORT).show();
    }
}
