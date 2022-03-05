package com.cliffex.Fixezi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class Subscribe extends AppCompatActivity {

    Toolbar ToolbarYourInfo;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM, FirstOption, SecondOption, ThirdOption, FourOption;
    TextView MakePayTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        ToolbarYourInfo = (Toolbar) findViewById(R.id.ToolbarSubscribe);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Subscribe");
        setSupportActionBar(ToolbarYourInfo);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FirstOption = (RelativeLayout) findViewById(R.id.FirstOption);
        SecondOption = (RelativeLayout) findViewById(R.id.SecondOption);
        ThirdOption = (RelativeLayout) findViewById(R.id.ThirdOption);
        FourOption = (RelativeLayout) findViewById(R.id.FourOption);
        MakePayTV = (TextView) findViewById(R.id.MakePayTV);

        FirstOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Subscribe.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        });

        MakePayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Subscribe.this, SubscriptionActivity.class);
                startActivity(intent);

            }
        });

        SecondOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Subscribe.this, SubscriptionActivity.class);
                startActivity(intent);

            }
        });


        ThirdOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Subscribe.this, SubscriptionActivity.class);
                startActivity(intent);

            }
        });


        FourOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Subscribe.this, SubscriptionActivity.class);
                startActivity(intent);

            }
        });


    }
}
