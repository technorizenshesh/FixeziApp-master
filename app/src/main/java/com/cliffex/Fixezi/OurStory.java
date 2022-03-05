package com.cliffex.Fixezi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

import androidx.appcompat.app.AppCompatActivity;

public class OurStory extends AppCompatActivity {

    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_story);
        img_back= (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
