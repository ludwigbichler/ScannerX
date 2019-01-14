package com.example.ludwi.scannerx;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemHolder> {

    List<Product> products;
    ProductDao dao;

    public ProductAdapter(ProductDao productDao)
    {
        dao = productDao;
        products = productDao.getAllProducts();
    }

    public void updateData(ProductDao birthdayDao) {
        this.products = birthdayDao.getAllProducts();
        this.notifyDataSetChanged();
    }

    public class ProductItemHolder extends RecyclerView.ViewHolder
    {

        public TextView bezeichnung;
        public TextView hersteller;
        public TextView preis;

        public int position;

        public ProductItemHolder(View itemView) {
            super(itemView);

            bezeichnung = itemView.findViewById(R.id.bezeichnung_list_item);
            hersteller = itemView.findViewById(R.id.hersteller_list_item);
            preis = itemView.findViewById(R.id.preis_list_item);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick (View v) {
                    ProductAdapter.this.dao.deleteProduct(products.get(position));
                    products.remove(position);
                    ProductAdapter.this.notifyDataSetChanged();
                    ProductAdapter.this.notifyItemRemoved(position);
                    return true;
                }
            });
        }
    }



    @NonNull
    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        Product product = products.get(position);
        holder.bezeichnung.setText(product.getBezeichnung());
        holder.hersteller.setText(product.getHersteller());
        holder.preis.setText(product.getPreis());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
