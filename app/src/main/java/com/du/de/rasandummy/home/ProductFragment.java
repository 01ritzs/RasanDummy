package com.du.de.rasandummy.home;

import android.content.Context;
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
import com.du.de.rasandummy.util.AppData;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements OnProductSelectListener {

    private List<Product> products = new ArrayList<>();
    private RecyclerView rvItemsList;
    public View rootView;
    private ProductsAdapter productsAdapter;
    private OnProductSelectListener mListener;

    public static ProductFragment getInstance() {
        return new ProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);
        rootView = inflater.inflate(R.layout.fragment_products, null);
        initRecyclerView();
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProductSelectListener) {
            mListener = (OnProductSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    private void initRecyclerView() {
        rvItemsList = rootView.findViewById(R.id.rvProducts);
        rvItemsList.setHasFixedSize(true);
        rvItemsList.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        productsAdapter = new ProductsAdapter(products, this);
        rvItemsList.setAdapter(productsAdapter);
    }

    @Override
    public void onSelected(Product product) {
        mListener.onSelected(product);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
