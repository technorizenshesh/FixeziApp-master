package com.cliffex.Fixezi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;

public class TradesmanLoginNew extends AppCompatActivity {

    TextView LoginAsAdminTV, LoginAsEmployeeTV;
    Toolbar toolbar;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    String RegId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman_login_new);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                RegId = token;
                Log.e("tokentoken", "retrieve token successful : " + RegId);
            } else {
                Log.e("tokentoken", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
        }).addOnCanceledListener(() -> {
        }).addOnCompleteListener(task -> Log.e("tokentoken", "This is the token : " + task.getResult()));

        toolbar = (Toolbar) findViewById(R.id.ToolbarTradesmanNew);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        setSupportActionBar(toolbar);
        toolbar_title.setText("Login as Tradesman");

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LoginAsAdminTV = (TextView) findViewById(R.id.LoginAsAdminTV);
        LoginAsEmployeeTV = (TextView) findViewById(R.id.LoginAsEmployeeTV);

        LoginAsEmployeeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), EmployeeLogin.class);
                startActivity(intent3);
            }
        });


        LoginAsAdminTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), TradesmanLogin.class);
                startActivity(intent3);
            }
        });
    }
}
