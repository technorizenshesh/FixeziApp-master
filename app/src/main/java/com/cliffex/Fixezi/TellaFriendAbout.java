package com.cliffex.Fixezi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

/**
 * Created by technorizen8 on 17/3/16.
 */
public class TellaFriendAbout extends AppCompatActivity
        implements View.OnClickListener {

    Toolbar tellafriend_toolbar;
    TextView toolbar_title;
    RelativeLayout fb, linkedin, viber, whatsapp, twitter, email, btn_pintrest, btn_instagram;
    Dialog dialog;
    RelativeLayout NavigationUpIM;
    Intent sharingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tellafriendaboutfixezi);
        tellafriend_toolbar = (Toolbar) findViewById(R.id.tellafriend_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Tell a friend about fixezi");
        setSupportActionBar(tellafriend_toolbar);


        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fb = (RelativeLayout) findViewById(R.id.fb);
        linkedin = (RelativeLayout) findViewById(R.id.linkedin);
        viber = (RelativeLayout) findViewById(R.id.viber);
        whatsapp = (RelativeLayout) findViewById(R.id.whatsapp);
        btn_pintrest = (RelativeLayout) findViewById(R.id.btn_pintrest);
        btn_instagram = (RelativeLayout) findViewById(R.id.btn_instagram);
        twitter = (RelativeLayout) findViewById(R.id.twitter);
        email = (RelativeLayout) findViewById(R.id.email);

        fb.setOnClickListener(this);
        linkedin.setOnClickListener(this);
        viber.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        btn_pintrest.setOnClickListener(this);
        btn_instagram.setOnClickListener(this);
        twitter.setOnClickListener(this);
        email.setOnClickListener(this);
        sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Try fixezi for Android!");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "I'm using fixezi for Android and I recommend it. Click here: https://play.google.com/store/apps/details?id=");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb:

                try {

                    sharingIntent.setPackage("com.facebook.katana");
                    startActivity(sharingIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Facebook not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.linkedin:

                try {

                    sharingIntent.setPackage("com.linkedin.android");
                    startActivity(sharingIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Linkedin not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.viber:

                try {

                    sharingIntent.setPackage("com.viber.voip");
                    startActivity(sharingIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Viber not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.whatsapp:
                try {

                    sharingIntent.setPackage("com.whatsapp");
                    startActivity(sharingIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp not Installed.", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.twitter:

                try {

                    sharingIntent.setPackage("com.twitter.android");
                    startActivity(sharingIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Twitter not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.email:

                try {

                    sharingIntent.setPackage("com.google.android.gm");
                    startActivity(sharingIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Gmail not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_pintrest:

                try {

                    sharingIntent.setPackage("com.pinterest");
                    startActivity(sharingIntent);

                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getApplicationContext(), "Pinterest not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_instagram:

                try {
                    sharingIntent.setPackage("com.instagram.android");
                    startActivity(sharingIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Instagram not Installed.", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }
}

