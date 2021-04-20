package com.du.de.rasandummy.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.du.de.rasandummy.R;
import com.du.de.rasandummy.RoomDatabase.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final OnProductSelectListener listener;
    private List<Product> list;
    private Context context;

    public ProductsAdapter(List<Product> list, OnProductSelectListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
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
        holder.cvItemsDetails.setOnClickListener(view -> listener.onSelected(product));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<Product> products) {
        this.list = products;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        CardView cvItemsDetails;

        ImageView ivItem;
        TextView tvItemName;
        TextView tvItemQuantity;
        TextView tvItemRate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItemsDetails = itemView.findViewById(R.id.cvItemsDetails);
            ivItem = itemView.findViewById(R.id.ivItem);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvItemRate = itemView.findViewById(R.id.tvItemRate);
        }
    }
}
