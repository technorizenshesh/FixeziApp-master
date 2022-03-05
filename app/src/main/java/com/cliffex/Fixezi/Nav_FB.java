package com.cliffex.Fixezi;

import android.os.Bundle;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Nav_FB extends AppCompatActivity {

    private ImageView img_back;
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__fb);
        img_back= (ImageView) findViewById(R.id.img_back);
        webview= (WebView) findViewById(R.id.webview);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        webview.loadUrl("https://www.facebook.com/Fixezi/");
    }
}
