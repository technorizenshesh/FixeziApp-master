package com.cliffex.Fixezi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class YourInformation extends AppCompatActivity {

    Toolbar ToolbarYourInfo;
    TextView toolbar_title, EditProfileInfoTV, EditServiceLocationTV, EditAddTrade, EditCompanyInfo, EditCallout, ManageUserTV;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_information);

        ToolbarYourInfo = (Toolbar) findViewById(R.id.ToolbarYourInfo);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Your Information");
        setSupportActionBar(ToolbarYourInfo);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditProfileInfoTV = (TextView) findViewById(R.id.EditProfileInfoTV);
        EditServiceLocationTV = (TextView) findViewById(R.id.EditServiceLocationTV);
        EditAddTrade = (TextView) findViewById(R.id.EditAddTrade);
        EditCallout = (TextView) findViewById(R.id.EditCallout);
        EditCompanyInfo = (TextView) findViewById(R.id.EditCompanyInfo);
        ManageUserTV = (TextView) findViewById(R.id.ManageUserTV);

        ManageUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(YourInformation.this, EditEmployeeId.class);
                startActivity(intent);

            }
        });

        EditProfileInfoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YourInformation.this, TradesmanInformation.class);
                startActivity(intent);

            }
        });

        EditServiceLocationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(YourInformation.this, EditServiceLocation.class);
                startActivity(intent);
            }
        });

        EditAddTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(YourInformation.this, EditTrade.class);
                startActivity(intent);

            }
        });

        EditCallout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(YourInformation.this, EditCallout.class);
                startActivity(intent);

            }
        });

        EditCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YourInformation.this, EditCompanyImages.class);
                startActivity(intent);
            }
        });
    }
}
