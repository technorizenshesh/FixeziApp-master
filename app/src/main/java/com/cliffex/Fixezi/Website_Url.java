package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class Website_Url extends AppCompatActivity {

    private Toolbar trade_toolbar2;
    private String Url;
    private CheckBox remembercheckbox;
    private TextView remembertxt;
    private TextView tvCon;
    private WebView TermsWV;
    boolean loadingFinished = true;
    boolean redirect = false;
    private RelativeLayout remember_layout;
    private TextView toolbar_title;
    private RelativeLayout NavigationUpIM;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website__url);
        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading.Please Wait ......");

        PreferenceConnector.writeString(Website_Url.this, PreferenceConnector.Address_Save, "");
        PreferenceConnector.writeString(Website_Url.this, PreferenceConnector.Address_Save1, "");

        trade_toolbar2 = (Toolbar) findViewById(R.id.trade_toolbar2);
        setSupportActionBar(trade_toolbar2);

        initComp();

        // String Url = "http://technorizen.co.in/WORKSPACE7/grs/appTerms/appTerms.html";
        Url = "https://fixezi.com.au";

        TermsWV.getSettings().setLoadsImagesAutomatically(true);
        TermsWV.getSettings().setJavaScriptEnabled(true);
        TermsWV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        TermsWV.setWebViewClient(new HelloWebViewClient());
        TermsWV.getSettings().setDomStorageEnabled(true);
        TermsWV.getSettings().setAppCacheEnabled(true);
        TermsWV.getSettings().setLoadsImagesAutomatically(true);
        TermsWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        TermsWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TermsWV.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            // TermsWV.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        TermsWV.loadUrl(Url);

        TermsWV.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                progressBar.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.dismiss();
            }

        });

    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initComp() {

        TermsWV = (WebView) findViewById(R.id.TermsWV);
        remember_layout = (RelativeLayout) findViewById(R.id.remember_layout);
        toolbar_title = (TextView) trade_toolbar2.findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) trade_toolbar2.findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Website");

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });

    }
}
