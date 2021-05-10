package com.du.de.rasandummy.ItemFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.db.Product;
import com.du.de.rasandummy.home.OnProductSelectListener;
import com.du.de.rasandummy.home.ProductsAdapter;
import com.du.de.rasandummy.util.AppData;

import java.util.List;

public class ChildFragment extends Fragment implements OnProductSelectListener {

    List<Product> products;
    private RecyclerView rvItemsList;
    public View rootView;
    private ProductsAdapter productsAdapter;

    public ChildFragment(List<Product> products) {
        this.products = products;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);
        rootView = inflater.inflate(R.layout.fragment_child, null);
        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        rvItemsList = rootView.findViewById(R.id.rvItemsList);
        rvItemsList.setHasFixedSize(true);
        rvItemsList.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        productsAdapter = new ProductsAdapter(products, this);
        rvItemsList.setAdapter(productsAdapter);
    }

    @Override
    public void onSelected(Product product) {
        AppData.getInstance().addProductToCart(product);
        String message = getResources().getString(R.string.added_to_cart);
        Toast.makeText(this.getActivity(), String.format(message, product.getName()), Toast.LENGTH_SHORT).show();
    }
}
