package com.example.ludwi.scannerx;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductItemHolder> {

    List<Product> products;

    public ProductAdapter(ProductDao productDao) {
        products = productDao.getAllProducts();
    }

    @NonNull
    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getName());
        holder.name.setText(product.getBeschreibung());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
