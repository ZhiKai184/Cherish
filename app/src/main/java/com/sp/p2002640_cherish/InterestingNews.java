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

public class InterestingNews extends AppCompatActivity {
    CardView news1,news2,news3,news4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting_news);
        news1=findViewById(R.id.newscard1);
        news2=findViewById(R.id.newscard2);
        news3=findViewById(R.id.newscard3);
        news4=findViewById(R.id.newscard4);

        news1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestingNews.this, News1.class);
                startActivity(intent);
            }
        });

        news2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestingNews.this, News2.class);
                startActivity(intent);
            }
        });

        news3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestingNews.this, News3.class);
                startActivity(intent);
            }
        });

        news4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestingNews.this, News4.class);
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