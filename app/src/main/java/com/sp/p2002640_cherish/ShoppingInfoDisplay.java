package com.sp.p2002640_cherish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShoppingInfoDisplay extends AppCompatActivity {
    FloatingActionButton fab_shopping;
    RecyclerView recyclerView_shopping;
    ShoppingHelper databaseHelper_shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_info_display);
        recyclerView_shopping = findViewById(R.id.recyclerView_shopping);
        databaseHelper_shopping = new ShoppingHelper(this);

        showRecord();

        fab_shopping = findViewById(R.id.addFloating_shopping);

        fab_shopping.setOnClickListener(v -> {
            // open input activity
            startActivity(new Intent(ShoppingInfoDisplay.this, ShoppingDataInput.class));
            finish();
        });

    }

    private void showRecord() {
        ShoppingAdapter adapter_shopping = new ShoppingAdapter(ShoppingInfoDisplay.this, databaseHelper_shopping.getAllData(ShoppingConstants.C_ADD_TIMESTAMP_SHOPPING+ " DESC"));
        recyclerView_shopping.setAdapter(adapter_shopping);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRecord();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent(ShoppingInfoDisplay.this,ShoppingGuide.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ShoppingInfoDisplay.this, Home.class);
        startActivity(intent);
    }
}