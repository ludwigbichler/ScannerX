package com.example.ludwi.scannerx;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ProductItemHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView beschreibung;

    public ProductItemHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name_list_item);
        beschreibung = itemView.findViewById(R.id.beschreibung_list_item);

    }
}
