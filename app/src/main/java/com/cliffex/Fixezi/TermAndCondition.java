package com.cliffex.Fixezi;

import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Constant.PreferenceConnector;

import ir.alirezabdn.wp7progress.WP10ProgressBar;

/**
 * Created by technorizen8 on 30/1/16.
 */
public class TermAndCondition extends AppCompatActivity {

    TextView remembertxt,toolbar_title,tvCon;
    CheckBox remembercheckbox;
    String From = "";
    WebView TermsWV;
    String Url;
    RelativeLayout remember_layout,NavigationUpIM;
    boolean loadingFinished = true;
    boolean redirect = false;
    private Toolbar trade_toolbar2;
    private WP10ProgressBar loader_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        loader_page = findViewById(R.id.loader_page);

        loader_page.showProgressBar();

        PreferenceConnector.writeString(TermAndCondition.this, PreferenceConnector.Address_Save, "");
        PreferenceConnector.writeString(TermAndCondition.this, PreferenceConnector.Address_Save1, "");

        trade_toolbar2 = findViewById(R.id.trade_toolbar2);
        setSupportActionBar(trade_toolbar2);

        initComp();

        //String Url = "http://technorizen.co.in/WORKSPACE7/grs/appTerms/appTerms.html";

        Url = "https://fixezi.com.au/fixezi_admin/admin/view_page/terms_app";

        //  Url = "https://fixezi.com.au/fixezi_admin/admin/view_page/contract_app";
        TermsWV.getSettings().setLoadsImagesAutomatically(true);
        TermsWV.getSettings().setJavaScriptEnabled(true);
        TermsWV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        TermsWV.setWebViewClient(new HelloWebViewClient());
        TermsWV.getSettings().setDomStorageEnabled(true);
        TermsWV.getSettings().setAppCacheEnabled(true);
        TermsWV.getSettings().setLoadsImagesAutomatically(true);
        TermsWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        TermsWV.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TermsWV.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            //TermsWV.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        TermsWV.loadUrl(Url);

        //TermsWV.loadUrl("http://www.tutorialspoint.com");

        TermsWV.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                loader_page.showProgressBar();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT IS NT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    remember_layout.setVisibility(View.VISIBLE);
                    loader_page.hideProgressBar();
                    //HIDE LOADING IT HAS FINISHED
                } else {
                    redirect = false;
                }

            }
        });

        Bundle extra = getIntent().getExtras();
        From = extra.getString("From");

        remembertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              remembercheckbox.setChecked(!remembercheckbox.isChecked());
            }
        });

        tvCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (remembercheckbox.isChecked()) {

                    if (From.equalsIgnoreCase("TradesmanTwo")) {

                        // Toast.makeText(getApplicationContext(), "unsuccess!!!", Toast.LENGTH_SHORT).show();

                        finish();

                    } else if (From.equalsIgnoreCase("Main")) {

                        Toast.makeText(getApplicationContext(), "success!!!", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(getApplicationContext(), SignupActivity.class);
                        j.putExtra("select_status", "sign_up");
                        j.putExtra("status", "signuptradmenn1");
                        j.putExtra("status1", "");

                        startActivity(j);
                        finish();

                        //PreferenceConnector.writeString(TermAndCondition.this, PreferenceConnector.Address_Save, "");
                    }

                } else {
                    Toast.makeText(TermAndCondition.this, "Please Select Check Box", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void initComp() {

        remembercheckbox = findViewById(R.id.remembercheckbox);
        remembertxt = findViewById(R.id.remembertxt);
        tvCon = findViewById(R.id.tv_continuous);
        loader_page = findViewById(R.id.loader_page);
        TermsWV = findViewById(R.id.TermsWV);
        remember_layout = findViewById(R.id.remember_layout);
        toolbar_title = trade_toolbar2.findViewById(R.id.toolbar_title);
        NavigationUpIM = trade_toolbar2.findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Terms  & conditions");
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}
