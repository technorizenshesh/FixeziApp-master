package com.cliffex.Fixezi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_Your_Information extends AppCompatActivity {

    private TextView YourInfoTV,Yourrating,PreviousHistoryTV,InvoicesTV;
    private TextView toolbar_title;
    private RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__your__information);

        SetuUI();
    }

    private void SetuUI() {

        YourInfoTV = (TextView) findViewById(R.id.YourInfoTV);
        Yourrating = (TextView) findViewById(R.id.Yourrating);
        PreviousHistoryTV = (TextView) findViewById(R.id.PreviousHistoryTV);
        InvoicesTV = (TextView) findViewById(R.id.InvoicesTV);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar_title.setText("Profile page");

        YourInfoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserInformation.class);
                startActivity(i);
            }
        });

        Yourrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserRating.class);
                startActivity(i);
            }
        });


        PreviousHistoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PreviousHistory.class);
                startActivity(i);
            }
        });

        InvoicesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InvoiceActivity.class);
                startActivity(i);
            }
        });


    }
}
