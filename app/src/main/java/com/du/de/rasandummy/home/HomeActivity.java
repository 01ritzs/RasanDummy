package com.du.de.rasandummy.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.DummyData;
import com.du.de.rasandummy.R;
import com.du.de.rasandummy.RoomDatabase.Product;
import com.du.de.rasandummy.cart.CartActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity implements OnProductSelectListener {

    //  RasanDatabase rasanDatabase;
    List<Product> productList = new ArrayList<>();
    RecyclerView rvItems;
    ProductsAdapter adapter;
    ImageView ivCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivCart = findViewById(R.id.ivCart);
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
                ArrayList<Product> products = snapshot.getValue(gti);
                productList.addAll(products);
                DummyData.products = productList;
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
        // TODO: Add item to cart
    }
}
