package com.du.de.rasandummy.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.du.de.rasandummy.R;
import com.du.de.rasandummy.db.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CartProductViewHolder> {

    private final OnCartProductSelectListener listener;
    private HashMap<Product, Integer> map;
    private Context context;

    public CartProductsAdapter(HashMap<Product, Integer> map, OnCartProductSelectListener listener) {
        this.map = map;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product_cart, parent, false);
        return new CartProductViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        Product product = (new ArrayList<>(map.keySet())).get(position);
        int count = map.get(product);
        holder.product = product;
        String productQuantity = String.valueOf(product.getQuantity());
        String productRate = String.valueOf(product.getRate());
        String productMrp = String.valueOf(product.getMrp());
        String stringQuantity = context.getResources().getString(R.string.product_quantity);
        String stringRate = context.getResources().getString(R.string.rs_);
        String stringMrp = context.getResources().getString(R.string.mrp_);
        holder.tvItemName.setText(product.getName());
        if (product.getQuantity().equals("")) {
            holder.tvItemQuantity.setVisibility(View.GONE);
        } else {
            holder.tvItemQuantity.setText(String.format(stringQuantity, productQuantity));
        }
        if (product.getMrp() > 0 && product.getRate() < product.getMrp()) {
            holder.tvItemMrp.setVisibility(View.VISIBLE);
            holder.tvItemMrp.setText(String.format(stringMrp, productMrp));
        } else {
            holder.tvItemMrp.setVisibility(View.GONE);
        }
        holder.tvItemRate.setText(String.format(stringRate, productRate));
        holder.tvCount.setText(String.valueOf(count));
        Glide.with(holder.itemView)
                .load(product.getImage())
                .centerCrop()
                .into(holder.ivItem);
    }

    @Override
    public int getItemCount() {
        return map == null ? 0 : map.size();
    }

    public void setList(LinkedHashMap<Product, Integer> products) {
        this.map = products;
        notifyDataSetChanged();
    }

    public static class CartProductViewHolder extends RecyclerView.ViewHolder {

        public Product product;
        public ImageView ivItem;
        public ImageView ivAdd;
        public ImageView ivSubtract;
        public TextView tvItemName;
        public TextView tvCount;
        public TextView tvItemQuantity;
        public TextView tvItemRate;
        public TextView tvItemMrp;

        public CartProductViewHolder(@NonNull View itemView, OnCartProductSelectListener listener) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.ivItem);
            ivAdd = itemView.findViewById(R.id.ivAdd);
            ivSubtract = itemView.findViewById(R.id.ivSubtract);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvItemRate = itemView.findViewById(R.id.tvItemRate);
            tvItemMrp = itemView.findViewById(R.id.tvItemMrp);
            ivAdd.setOnClickListener(view -> listener.onAdd(product));
            ivSubtract.setOnClickListener(view -> listener.onSubtract(product));
        }
    }
}
