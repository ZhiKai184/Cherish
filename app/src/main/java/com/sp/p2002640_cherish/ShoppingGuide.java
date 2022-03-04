package com.sp.p2002640_cherish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShoppingGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_guide);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ShoppingGuide.this, ShoppingInfoDisplay.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.about):
                setContentView(R.layout.activity_about1);
                break;
            case (R.id.contact):
                setContentView(R.layout.activity_contact1);
                TextView email = (TextView) findViewById(R.id.email);
                email.setText(Html.fromHtml("<a href=\"mailto:ZHIKAI.20@ichat.sp.edu.sg\">ZHIKAI.20@ichat.sp.edu.sg</a>"));
                email.setMovementMethod(LinkMovementMethod.getInstance());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}