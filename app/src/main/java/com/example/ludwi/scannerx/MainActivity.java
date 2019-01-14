package com.example.ludwi.scannerx;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Datenbank erstellen
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "product_db")
                .allowMainThreadQueries()
                .build();


        recyclerView = findViewById(R.id.product_list);
        LinearLayoutManager lom = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(lom);

        recyclerView.setAdapter(new ProductAdapter(database.getProductDao()));

        DividerItemDecoration did = new DividerItemDecoration(this,lom.getOrientation());
        recyclerView.addItemDecoration(did);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

//  Das habe ich hinzugefügt, aber ich weiß nicht, was ihm nicht gefällt

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iconScan:
                startActivity(new Intent(MainActivity.this, ScanningActivity.class));
                break;
        }

        return false;
    }

}
