package com.cliffex.Fixezi;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.MyFontBoldTextView;

/**
 * Created by technorizen8 on 17/3/16.
 */
public class WhatYouthink extends AppCompatActivity {

    MyFontBoldTextView sndthinkbtnbtn;
    Toolbar whatthinktoolbar;
    TextView toolbar_title;
    EditText etComments;
    TextView FIXEZITV;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whatdoyouthinkoffixezi);

        whatthinktoolbar = (Toolbar) findViewById(R.id.whatthinktoolbar);
        toolbar_title = (TextView) whatthinktoolbar.findViewById(R.id.toolbar_title);
        sndthinkbtnbtn = (MyFontBoldTextView) findViewById(R.id.sndthinkbtnbtn);
        etComments = (EditText) findViewById(R.id.et_comments);
        FIXEZITV = (TextView) findViewById(R.id.FIXEZITV);

        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("What do you think of fixezi ?");
        toolbar_title.setTextSize(16);
        setSupportActionBar(whatthinktoolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "DroidSerif.ttf");
        toolbar_title.setTypeface(custom_font);
        FIXEZITV.setTypeface(custom_font);

        sndthinkbtnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = etComments.getText().toString().trim();
                if (txt.length() > 0) {
                    sendMail(txt);
                } else {
                    etComments.setError("Please Enter Your Comment Here");
                }
            }
        });
    }

    String txt = "";

    public void sendMail(String text) {

        try {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "fixeziteam@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Think About fixezi   ");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, "fixeziteam@gmail.com");
            emailIntent.putExtra(Intent.EXTRA_TEXT,text);
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(WhatYouthink.this, "There are no email installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
