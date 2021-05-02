package com.du.de.rasandummy.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.db.Product;
import com.du.de.rasandummy.util.AdUtils;
import com.du.de.rasandummy.util.AppData;
import com.du.de.rasandummy.util.ProductUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements OnCartProductSelectListener {

    private RecyclerView rvItems;
    private CartProductsAdapter adapter;
    private ImageView ivBack;
    private TextView tvErrorMessage;
    private TextView tvTotal;
    private HashMap<Product, Integer> selectedProducts;
    public FloatingActionButton fabShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        tvTotal = findViewById(R.id.tvTotal);
        ivBack = findViewById(R.id.ivBack);
        fabShare = findViewById(R.id.fabShare);
        ivBack.setOnClickListener(view -> onBackPressed());
        selectedProducts = AppData.getInstance().getSelectedProduct();
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.instance.showInterstitialAd(CartActivity.this);
                shareGroceryList(selectedProducts);
            }
        });
        fabShare.setOnClickListener(view -> shareGroceryList(selectedProducts));
        setTotal(selectedProducts);
        initRecyclerView(selectedProducts);
        updateErrorStatus(selectedProducts);
    }

    private void setTotal(HashMap<Product, Integer> selectedProducts) {
        int total = getTotal(selectedProducts);
        tvTotal.setText(String.format(getResources().getString(R.string.total), total));
    }

    private void initRecyclerView(HashMap<Product, Integer> selectedProducts) {
        rvItems = findViewById(R.id.rvCart);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CartProductsAdapter(selectedProducts, this);
        rvItems.setAdapter(adapter);
    }

    private void updateErrorStatus(HashMap<Product, Integer> selectedProducts) {
        if (selectedProducts != null && selectedProducts.size() > 0) {
            tvErrorMessage.setVisibility(View.GONE);
        } else {
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
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
        setTotal(map);
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
        setTotal(map);
        updateErrorStatus(map);
    }

    private int getTotal(HashMap<Product, Integer> selectedProducts) {
        int total = 0;
        for (Map.Entry<Product, Integer> entry : selectedProducts.entrySet()) {
            Product product = entry.getKey();
            int count = entry.getValue();
            int itemCost = Integer.parseInt(product.getRate()) * count;
            total += itemCost;
        }
        return total;
    }

    public void shareGroceryList(HashMap<Product, Integer> selectedProducts) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, ProductUtil.getListToShare(selectedProducts));
        startActivity(Intent.createChooser(intent, getString(R.string.share_using)));
    }
}
