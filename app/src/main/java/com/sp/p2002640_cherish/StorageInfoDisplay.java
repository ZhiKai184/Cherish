package com.sp.p2002640_cherish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class StorageInfoDisplay extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    StorageHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_info_display);


        recyclerView = findViewById(R.id.recyclerView);
        databaseHelper = new StorageHelper(this);

        showRecord();

        fab = findViewById(R.id.addFloating);

        fab.setOnClickListener(v -> {
            // open input activity
            startActivity(new Intent(StorageInfoDisplay.this, StorageDataInput.class));
            finish();
        });

    }

    private void showRecord() {
        StorageAdapter adapter = new StorageAdapter(StorageInfoDisplay.this, databaseHelper.getAllData(StorageConstants.C_ADD_TIMESTAMP + " DESC"));
        recyclerView.setAdapter(adapter);
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
        Intent intent=new Intent(StorageInfoDisplay.this,StorageGuide.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(StorageInfoDisplay.this, Home.class);
        startActivity(intent);
    }

}
