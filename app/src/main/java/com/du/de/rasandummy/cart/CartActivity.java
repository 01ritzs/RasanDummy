package com.du.de.rasandummy.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.db.Product;
import com.du.de.rasandummy.util.AppData;
import com.du.de.rasandummy.util.Constants;
import com.du.de.rasandummy.util.ProductUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements OnCartProductSelectListener {

    private RecyclerView rvItems;
    private CartProductsAdapter adapter;
    private ImageView ivBack;
    private ImageView ivDelete;
    private TextView tvErrorMessage;
    private TextView tvTotal;
    public FloatingActionButton fabShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        tvTotal = findViewById(R.id.tvTotal);
        ivBack = findViewById(R.id.ivBack);
        ivDelete = findViewById(R.id.ivDelete);
        fabShare = findViewById(R.id.fabShare);
        LinkedHashMap<Product, Integer> selectedProducts = AppData.getInstance().getSelectedProduct();
        ivBack.setOnClickListener(v -> onBackPressed());
        ivDelete.setOnClickListener(v -> onDeletePress(selectedProducts));
        fabShare.setOnClickListener(view -> shareGroceryList(selectedProducts));
        setTotal(selectedProducts);
        initRecyclerView(selectedProducts);
        updateErrorStatus(selectedProducts);
    }

    private void onDeletePress(HashMap<Product, Integer> selectedProducts) {
        if (selectedProducts.size() > 0) {
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                    .setTitle(getResources().getString(R.string.delete_cart))
                    .setMessage(getResources().getString(R.string.all_item_from_the))
                    .setPositiveButton(R.string.delete, (dialog, which) -> {
                        dialog.dismiss();
                        AppData.getInstance().clearSelectedItems();
                        adapter.setList(AppData.getInstance().getSelectedProduct());
                        setTotal(AppData.getInstance().getSelectedProduct());
                        updateErrorStatus(AppData.getInstance().getSelectedProduct());
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_item_in_cart), Toast.LENGTH_SHORT).show();
        }
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
            if (!product.getRate().equals("")) {
                int count = entry.getValue();
                int rate = Integer.parseInt(product.getRate());
                int itemCost = rate * count;
                total += itemCost;
            }
        }
        return total;
    }

    public void shareGroceryList(HashMap<Product, Integer> selectedProducts) {
        if (selectedProducts.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share));
            intent.putExtra(android.content.Intent.EXTRA_TEXT, ProductUtil.getListToShare(selectedProducts));
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)));
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_item_in_cart), Toast.LENGTH_SHORT).show();
        }
    }
}
