package com.sp.p2002640_cherish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FoodDrives extends AppCompatActivity {

    CardView foodbanksingapore,foodfromtheheart,freefoodforall,ronaldmacdonald,societyfortheagedsick,willinghearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_drives);
        foodbanksingapore =findViewById(R.id.foodbanksingapore);
        foodfromtheheart = findViewById(R.id.foodfromtheheart);
        freefoodforall = findViewById(R.id.freefoodforall);
        ronaldmacdonald = findViewById(R.id.ronaldmacdonald);
        societyfortheagedsick = findViewById(R.id.societyfortheagedsick);
        willinghearts = findViewById(R.id.willinghearts);

        foodbanksingapore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDrives.this, FoodBankSingapore.class);
                startActivity(intent);
            }
        });

        foodfromtheheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDrives.this, FoodFromTheHeart.class);
                startActivity(intent);
            }
        });

        freefoodforall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDrives.this, FreeFoodForAll.class);
                startActivity(intent);
            }
        });

        ronaldmacdonald.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDrives.this, RonaldMcDonaldHouseCharities.class);
                startActivity(intent);
            }
        });

        societyfortheagedsick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDrives.this, SocietyForTheAgedSick.class);
                startActivity(intent);
            }
        });

        willinghearts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDrives.this, WillingHearts.class);
                startActivity(intent);
            }
        });
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