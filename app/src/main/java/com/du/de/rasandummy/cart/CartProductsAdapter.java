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
import com.du.de.rasandummy.RoomDatabase.Product;

import java.util.List;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CartProductViewHolder> {

    private final OnCartProductSelectListener listener;
    private List<Product> list;
    private Context context;

    public CartProductsAdapter(List<Product> list, OnCartProductSelectListener listener) {
        this.list = list;
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
        Product product = list.get(position);
        holder.product = product;
        String productQuantity = String.valueOf(product.getQuantity());
        String productRate = String.valueOf(product.getRate());
        String stringQuantity = context.getResources().getString(R.string.product_quantity);
        String stringRate = context.getResources().getString(R.string.rs_);
        holder.tvItemName.setText(product.getName());
        holder.tvItemQuantity.setText(String.format(stringQuantity, productQuantity));
        holder.tvItemRate.setText(String.format(stringRate, productRate));
        Glide.with(holder.itemView)
                .load(product.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivItem);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<Product> products) {
        this.list = products;
        notifyDataSetChanged();
    }

    public static class CartProductViewHolder extends RecyclerView.ViewHolder {

        public Product product;
        ImageView ivItem;
        ImageView ivAdd;
        ImageView ivSubtract;
        TextView tvItemName;
        TextView tvCount;
        TextView tvItemQuantity;
        TextView tvItemRate;

        public CartProductViewHolder(@NonNull View itemView, OnCartProductSelectListener listener) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.ivItem);
            ivAdd = itemView.findViewById(R.id.ivAdd);
            ivSubtract = itemView.findViewById(R.id.ivSubtract);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvItemRate = itemView.findViewById(R.id.tvItemRate);
            ivAdd.setOnClickListener(view -> listener.onAdd(product));
            ivSubtract.setOnClickListener(view -> listener.onSubtract(product));
        }
    }
}