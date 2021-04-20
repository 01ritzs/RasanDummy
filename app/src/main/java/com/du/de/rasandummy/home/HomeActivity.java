package com.du.de.rasandummy.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.du.de.rasandummy.util.NetworkUtil;
import com.du.de.rasandummy.util.ProductUtil;
import com.google.android.material.snackbar.Snackbar;
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
    private ProductsAdapter productsAdapter;
    private ImageView ivCart;
    private TextView tvCartBadge;
    private RelativeLayout rvProgressBar;
    private TextView tvErrorMessage;
    private EditText etSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivCart = findViewById(R.id.ivCart);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        rvProgressBar = findViewById(R.id.rvProgressBar);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etSearch = findViewById(R.id.etSearch);
        ivCart.setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, CartActivity.class))
        );
        initFirebase();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }

    private void initFirebase() {
        if (NetworkUtil.isConnected(this)) {
            setupFirebase();
        } else {
            Snackbar.make(findViewById(android.R.id.content),
                    getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.retry),
                    v -> initFirebase()).show();
            updateErrorStatus(productList);
        }
    }

    private void setupFirebase() {
        rvProgressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rvProgressBar.setVisibility(View.GONE);
                GenericTypeIndicator<ArrayList<Product>> gti = new GenericTypeIndicator<ArrayList<Product>>() {
                };
                // Fetch products from snapshot
                List<Product> products = snapshot.getValue(gti);
                updateErrorStatus(products);
                // Populate product list
                productList.addAll(products);
                // Save product list in app data
                AppData.getInstance().setProducts(products);
                // Populate product list
                productsAdapter.setList(productList);
                initSearch(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("On Cancelled", "There is no data");
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initSearch(final List<Product> productList) {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productsAdapter.setList(ProductUtil.sort(productList, charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void updateErrorStatus(List<Product> products) {
        if (products.size() > 0) {
            tvErrorMessage.setVisibility(View.GONE);
        } else {
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        rvItems = findViewById(R.id.rvProducts);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1));
        productsAdapter = new ProductsAdapter(productList, this);
        rvItems.setAdapter(productsAdapter);
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
