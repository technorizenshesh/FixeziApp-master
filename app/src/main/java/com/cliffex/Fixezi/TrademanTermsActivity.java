package com.cliffex.Fixezi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.MyUtils.MyFontBoldTextView;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class TrademanTermsActivity extends AppCompatActivity {

    private MyFontBoldTextView ContinueSTTV;
    private TextView toolbar_title;
    private RelativeLayout NavigationUpIM;
    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trademan_terms);

        PreferenceConnector.writeString(this, PreferenceConnector.Select_radius, "");
        PreferenceConnector.writeString(this, PreferenceConnector.Select_address_Raedus, "");

        ContinueSTTV = (MyFontBoldTextView) findViewById(R.id.ContinueSTTV);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        toolbar_title.setText("Tradesmen");

        Typeface typeface = ResourcesCompat.getFont(TrademanTermsActivity.this,R.font.montserrat_medium);
        toolbar_title.setTypeface(typeface);

        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ContinueSTTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TradesmanSignUp.class);
                startActivity(i);
                finish();
            }
        });

    }


}
