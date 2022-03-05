package com.cliffex.Fixezi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cliffex.Fixezi.Constant.PreferenceConnector;

public class Activity_Quick_Search extends AppCompatActivity implements
        View.OnClickListener {

    private TextView Search_local_tradman;
    private TextView LoginAsEmployeeTV;
    private RelativeLayout NavigationUpIM;
    private TextView toolbar_title;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__quick__search);

        Bundle bundle = getIntent().getExtras();

        if (!bundle.equals("")) {
            status = bundle.getString("status");
        }

        PreferenceConnector.writeString(Activity_Quick_Search.this, PreferenceConnector.Address_Save, "");
        SetuUi();

    }

    private void SetuUi() {

        Search_local_tradman = (TextView) findViewById(R.id.Search_local_tradman);
        LoginAsEmployeeTV = (TextView) findViewById(R.id.LoginAsEmployeeTV);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("Register/Login");
        Search_local_tradman.setOnClickListener(this);
        LoginAsEmployeeTV.setOnClickListener(this);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.Search_local_tradman:

                Intent in = new Intent(Activity_Quick_Search.this, UserActivity.class);
                in.putExtra("status", status);
                startActivity(in);
                break;

            case R.id.LoginAsEmployeeTV:
                Intent intent = new Intent(Activity_Quick_Search.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceConnector.writeString(Activity_Quick_Search.this, PreferenceConnector.Address_Save, "");
    }
}
