package com.du.de.rasandummy.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.RoomDatabase.Product;
import com.du.de.rasandummy.cart.CartActivity;
import com.du.de.rasandummy.util.AppData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity implements OnProductSelectListener {

    List<Product> productList = new ArrayList<>();
    RecyclerView rvItems;
    ProductsAdapter adapter;
    ImageView ivCart;
    TextView tvCartBadge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivCart = findViewById(R.id.ivCart);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        ivCart.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });
        initRecyclerView();

    }

    private void setupFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Product>> gti = new GenericTypeIndicator<ArrayList<Product>>() {
                };
                // Fetch products from snapshot
                ArrayList<Product> products = snapshot.getValue(gti);
                // Populate product list
                productList.addAll(products);
                // Save product list in app data
                AppData.getInstance().setProducts(products);
                // Populate product list
                adapter.setList(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("On Cancelled", "There is no data");
            }
        });

    }

    private void initRecyclerView() {
        rvItems = findViewById(R.id.rvProducts);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1));
        initFirebase();
        adapter = new ProductsAdapter(productList, this);
        rvItems.setAdapter(adapter);
    }

    private void initFirebase() {
        setupFirebase();
    }

    @Override
    public void onSelected(Product product) {
        // Add product in Map
        AppData.getInstance().addProductToCart(product);
        // Show message for adding item in cart
        String message = getResources().getString(R.string.added_to_cart);
        Toast.makeText(this, String.format(message, product.getName()), Toast.LENGTH_SHORT).show();
        updateBadge();
    }

    private void updateBadge() {
        int selectedItemCount = AppData.getInstance().getSelectedProduct().size();
        if (selectedItemCount > 0) {
            tvCartBadge.setVisibility(View.VISIBLE);
        } else {
            tvCartBadge.setVisibility(View.INVISIBLE);
        }
    }
}
