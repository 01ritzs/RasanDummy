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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.cart.CartActivity;
import com.du.de.rasandummy.db.Category;
import com.du.de.rasandummy.db.Product;
import com.du.de.rasandummy.util.AdUtils;
import com.du.de.rasandummy.util.AppData;
import com.du.de.rasandummy.util.Constants;
import com.du.de.rasandummy.util.NetworkUtil;
import com.du.de.rasandummy.util.ProductUtil;
import com.du.de.rasandummy.util.RecentManager;
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

    List<Category> categoryList = new ArrayList<>();
    private TextView tvCart;
    private ImageView ivCancel;
    private RelativeLayout rvProgressBar;
    private TextView tvErrorMessage;
    private EditText etSearch;
    private AdView adView;
    private ViewPager viewPager;
    private FragmentTransaction fragmentTransaction;
    private SearchFragment searchFragment;
    private boolean isSearchEnabled = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvCart = findViewById(R.id.tvCart);
        ivCancel = findViewById(R.id.ivCancel);
        rvProgressBar = findViewById(R.id.rvProgressBar);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etSearch = findViewById(R.id.etSearch);
        adView = findViewById(R.id.adView);
        viewPager = findViewById(R.id.vp);
        tvCart.setOnClickListener(view -> {
            gotoNextScreen();
//            AdUtils.getInstance().showInterstitialAd(this);
        });
        ivCancel.setOnClickListener(view -> {
            etSearch.setText("");
            closeSearchView();
        });
        //AdUtils.getInstance().loadBannerAd(adView);
//        AdUtils.getInstance().loadInterstitial(this);
        initFirebase();
        initSearchView();
    }

    private void initFragmentTransaction() {
        fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
    }

    private void initSearchView() {
        searchFragment = SearchFragment.newInstance();
        initFragmentTransaction();
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
//        AdUtils.getInstance().loadInterstitial(this);
    }

    private void initFirebase() {
        if (NetworkUtil.isConnected(this)) {
            setupFirebase();
        } else {
            Snackbar.make(findViewById(android.R.id.content),
                    getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE).setAction(getResources().getString(R.string.retry),
                    v -> initFirebase()).show();
            updateErrorStatus(categoryList);
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
                List<Category> categories = snapshot.getValue(gti);


                categories = new RecentManager(HomeActivity.this).addRecentCategory(categories);
                updateErrorStatus(categories);
                AppData.getInstance().setCategories(categories);
                initViewPager(categories);
                initSearch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("On Cancelled", "There is no data");
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onSearchTextChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void onSearchTextChanged(CharSequence charSequence) {
        if (charSequence.length() > 0) {
            ivCancel.setVisibility(View.VISIBLE);
            if (!isSearchEnabled) {
                openSearchView();
            }
            searchFragment.searchWith(charSequence.toString());
        } else {
            ivCancel.setVisibility(View.INVISIBLE);
            closeSearchView();
        }
    }

    private void updateErrorStatus(List<Category> categories) {
        if (categories.size() > 0) {
            tvErrorMessage.setVisibility(View.GONE);
        } else {
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSelected(Product product) {
        // Add product in Map
        AppData.getInstance().addProductToCart(product);
        // Show message for adding item in cart
        String message = getResources().getString(R.string.added_to_cart);
        Toast.makeText(this, String.format(message, product.getName()), Toast.LENGTH_SHORT).show();
        updateBadge();
        new RecentManager(this).addProduct(product);
    }

    private void updateBadge() {
        int selectedItemCount = AppData.getInstance().getSelectedProductSize();
        if (selectedItemCount > 0) {
            tvCart.setText(String.valueOf(selectedItemCount));
        } else {
            tvCart.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchEnabled) {
            closeSearchView();
        } else {
            super.onBackPressed();
        }
    }

    private void openSearchView() {
        isSearchEnabled = true;
        initFragmentTransaction();
        fragmentTransaction.add(R.id.fcvSearch, searchFragment, null).commit();
    }

    private void closeSearchView() {
        isSearchEnabled = false;
        initFragmentTransaction();
        fragmentTransaction.remove(searchFragment).commit();
    }
}
