package com.du.de.rasandummy.cart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
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
import com.du.de.rasandummy.util.ProductUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements OnCartProductSelectListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 12;
    private final int CREATE_CONTACT_CODE = 100;
    public FloatingActionButton fabShare;
    public Button btnOrder;
    private RecyclerView rvItems;
    private CartProductsAdapter adapter;
    private ImageView ivBack;
    private ImageView ivDelete;
    private TextView tvErrorMessage;
    private TextView tvTotal;
    private TextView tvSave;
    private TextView tvCart;
    private TextView tvShareMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        tvTotal = findViewById(R.id.tvTotal);
        tvSave = findViewById(R.id.tvSave);
        tvCart = findViewById(R.id.tvCart);
        tvShareMessage = findViewById(R.id.tvShareMessage);
        ivBack = findViewById(R.id.ivBack);
        ivDelete = findViewById(R.id.ivDelete);
        fabShare = findViewById(R.id.fabShare);
        btnOrder = findViewById(R.id.btnOrder);
        LinkedHashMap<Product, Integer> selectedProducts = AppData.getInstance().getSelectedProduct();
        ivBack.setOnClickListener(v -> onBackPressed());
        ivDelete.setOnClickListener(v -> onDeletePress(selectedProducts));
        fabShare.setOnClickListener(view -> shareGroceryList());
        tvShareMessage.setOnClickListener((view) -> {
            onClickSaveContact();
        });
        btnOrder.setOnClickListener(v -> {
            onClickOrderButton();
        });
        setTotal(selectedProducts);
        initRecyclerView(selectedProducts);
        updateErrorStatus(selectedProducts);
    }

    private void updateCartCount() {
        LinkedHashMap<Product, Integer> selectedProducts = AppData.getInstance().getSelectedProduct();
        int itemCount = 0;
        for (Map.Entry<Product, Integer> item : selectedProducts.entrySet()) {
            itemCount += item.getValue();
        }
        if (itemCount > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(getResources().getString(R.string.item_cart));
            sb.append("(").append(String.valueOf(itemCount)).append(")");
            tvCart.setText(sb.toString());
        }
    }

    private void onClickOrderButton() {
        checkPermissionForContact();
    }

    public boolean contactExists(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {
                ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur != null && cur.moveToFirst()) {
                cur.close();
                return true;
            }
        } catch (Exception e) {
            if (cur != null) cur.close();
            return false;
        }
        return false;
    }

    private void onClickSaveContact() {
        Intent i = new Intent(ContactsContract.Intents.Insert.ACTION);
        i.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        i.putExtra(ContactsContract.Intents.Insert.NAME, "Usha Mart");
        i.putExtra(ContactsContract.Intents.Insert.PHONE, "6201543329");
        startActivityForResult(i, CREATE_CONTACT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_CONTACT_CODE) {
            shareGroceryList();
        }
    }

    private void onDeletePress(HashMap<Product, Integer> selectedProducts) {
        if (selectedProducts.size() > 0) {
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog).setTitle(getResources().getString(R.string.delete_cart)).setMessage(getResources().getString(R.string.all_item_from_the)).setPositiveButton(R.string.delete, (dialog, which) -> {
                dialog.dismiss();
                AppData.getInstance().clearSelectedItems();
                adapter.setList(AppData.getInstance().getSelectedProduct());
                setTotal(AppData.getInstance().getSelectedProduct());
                updateErrorStatus(AppData.getInstance().getSelectedProduct());
            }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_item_in_cart), Toast.LENGTH_SHORT).show();
        }
    }

    private void setTotal(HashMap<Product, Integer> selectedProducts) {
        int total = getTotal(selectedProducts);
        int save = getSave(selectedProducts);
        tvTotal.setText(String.format(getResources().getString(R.string.total), total));
        if (save > 0) {
            tvSave.setVisibility(View.VISIBLE);
            tvSave.setText(String.format(getResources().getString(R.string.save_), save));
        } else {
            tvSave.setVisibility(View.GONE);
        }
        updateCartCount();
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
            if (product.getRate() > 0) {
                int count = entry.getValue();
                int rate = product.getRate();
                int itemCost = rate * count;
                total += itemCost;
            }
        }
        return total;
    }

    private int getSave(HashMap<Product, Integer> selectedProducts) {
        int total = 0;
        int totalMrp = 0;
        for (Map.Entry<Product, Integer> entry : selectedProducts.entrySet()) {
            Product product = entry.getKey();
            if (product.getRate() > 0) {
                int count = entry.getValue();
                int rate = product.getRate();
                int mrp = product.getMrp();
                int itemCost = rate * count;
                int itemMrps = mrp * count;
                total += itemCost;
                totalMrp += itemMrps;
            }
        }
        return totalMrp - total;
    }

    public void shareGroceryList() {
        LinkedHashMap<Product, Integer> selectedProducts = AppData.getInstance().getSelectedProduct();
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

    private void checkPermissionForContact() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            checkToAddNewContact();
        }
    }

    private void requestPermission() {
        String[] permissions = {Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
        requestPermissions(permissions, PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                checkToAddNewContact();
            } else {
                showAlertDialogForPermission();
            }
        }
    }

    private void showAlertDialogForPermission() {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
                .setTitle("Grant permission?")
                .setMessage("We request your permission to be able to save contact for you.")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    requestPermission();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("No", ((dialogInterface, i) -> {
                    shareGroceryList();
                    dialogInterface.dismiss();
                })).show();
    }

    private void checkToAddNewContact() {
        if (contactExists(this, "6201543329")) {
            shareGroceryList();
        } else {
            onClickSaveContact();
        }
    }
}
