package com.example.ludwi.scannerx;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ProductItemHolder extends RecyclerView.ViewHolder {

    public TextView bezeichnung;
    public TextView hersteller;
    public TextView preis;


    public ProductItemHolder(View itemView) {
        super(itemView);

        bezeichnung = itemView.findViewById(R.id.bezeichnung_list_item);
        hersteller = itemView.findViewById(R.id.hersteller_list_item);
        preis = itemView.findViewById(R.id.preis_list_item);

    }
}
