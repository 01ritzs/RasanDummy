package com.du.de.rasandummy.home;

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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {


    private List<Product> list;

    public ProductsAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.tvItemName.setText(list.get(position).getName());
        holder.tvItemQuantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.tvItemRate.setText(String.valueOf(list.get(position).getRate()));
        Glide.with(holder.itemView)
                .load(list.get(position).getImage())
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

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItem;
        TextView tvItemName;
        TextView tvItemQuantity;
        TextView tvItemRate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.ivItem);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvItemRate = itemView.findViewById(R.id.tvItemRate);
        }
    }
}
