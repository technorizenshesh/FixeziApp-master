package com.cliffex.Fixezi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.MyUtils.TouchImageView;

public class MyFullScreenActivity extends AppCompatActivity {

    TouchImageView imgDisplay;
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_full_screen);

        Bundle extra = getIntent().getExtras();
        String ImagePath = extra.getString("ImagePath");

        imgDisplay = (TouchImageView) findViewById(R.id.imgDisplay2);
        btnClose = (Button) findViewById(R.id.btnClose2);


        Glide.with(MyFullScreenActivity.this)
                .load(ImagePath)
                .into(imgDisplay);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}