package com.du.de.rasandummy.ItemFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.db.Product;
import com.du.de.rasandummy.home.OnProductSelectListener;
import com.du.de.rasandummy.home.ProductsAdapter;
import com.du.de.rasandummy.util.AppData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildFragment extends Fragment implements OnProductSelectListener {

    List<Product> productList = new ArrayList<>();
    private final String message;
    private RecyclerView rvItemsList;
    public View rootView;
    private ProductsAdapter productsAdapter;

    public ChildFragment(String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);
        rootView = inflater.inflate(R.layout.fragment_child, null);
        initRecyclerView();
        initFirebase();
        return rootView;
    }

    private void initFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Product>> gti = new GenericTypeIndicator<ArrayList<Product>>() {
                };
                List<Product> products = snapshot.getValue(gti);
                productList.addAll(products);
                AppData.getInstance().setProducts(products);
                productsAdapter.setList(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecyclerView() {
        rvItemsList = rootView.findViewById(R.id.rvItemsList);
        rvItemsList.setHasFixedSize(true);
        rvItemsList.setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
        productsAdapter = new ProductsAdapter(productList, this);
        rvItemsList.setAdapter(productsAdapter);
    }

    @Override
    public void onSelected(Product product) {
        AppData.getInstance().addProductToCart(product);
        String message = getResources().getString(R.string.added_to_cart);
        Toast.makeText(this.getActivity(), String.format(message, product.getName()), Toast.LENGTH_SHORT).show();
    }
}
