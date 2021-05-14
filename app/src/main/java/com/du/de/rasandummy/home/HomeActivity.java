package com.du.de.rasandummy.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.cart.CartActivity;
import com.du.de.rasandummy.db.Category;
import com.du.de.rasandummy.db.Product;
import com.du.de.rasandummy.util.AdUtils;
import com.du.de.rasandummy.util.AppData;
import com.du.de.rasandummy.util.Constants;
import com.du.de.rasandummy.util.NetworkUtil;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnProductSelectListener {

    private static final String TAG = HomeActivity.class.getName();

    List<Product> productList = new ArrayList<>();
    RecyclerView rvItemsList;
    //    private ProductsAdapter productsAdapter;
    private ImageView ivCart;
    private RelativeLayout rvProgressBar;
    private TextView tvErrorMessage;
    private EditText etSearch;
    private AdView adView;
    private ViewPager viewPager;
//    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ivCart = findViewById(R.id.ivCart);
        rvProgressBar = findViewById(R.id.rvProgressBar);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etSearch = findViewById(R.id.etSearch);
        adView = findViewById(R.id.adView);
        viewPager = findViewById(R.id.vp);
        ivCart.setOnClickListener(view -> {
            gotoNextScreen();
            AdUtils.getInstance().loadBannerAd(adView);
            AdUtils.getInstance().showInterstitialAd(this);
        });
        initFirebase();
    }

    private void initViewPager(List<Category> categories) {
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), categories));
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void gotoNextScreen() {
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
        AdUtils.getInstance().loadInterstitial(this);
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
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_DB_NAME);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rvProgressBar.setVisibility(View.GONE);
                GenericTypeIndicator<ArrayList<Category>> gti = new GenericTypeIndicator<ArrayList<Category>>() {
                };
                // Fetch products from snapshot
                List<Category> categories = snapshot.getValue(gti);
//                updateErrorStatus(products);
                // Populate product list
//                productList.addAll(categories);
                // Save product list in app data
                AppData.getInstance().setCategories(categories);
                // Populate product list
//                productsAdapter.setList(productList);
//                initSearch(productList);
                initViewPager(categories);
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
//                productsAdapter.setList(ProductUtil.sort(productList, charSequence.toString()));
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

   /* private void initRecyclerView() {
        rvItemsList = rootView.findViewById(R.id.rvItemsList);
        rvItemsList.setHasFixedSize(true);
        rvItemsList.setLayoutManager(new GridLayoutManager(this, 1));
        productsAdapter = new ProductsAdapter(productList, this);
        rvItemsList.setAdapter(productsAdapter);
    }*/

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
            ivCart.setColorFilter(getResources().getColor(R.color.red));
        } else {
            ivCart.setColorFilter(getResources().getColor(R.color.grey_dark));
        }
    }
}
