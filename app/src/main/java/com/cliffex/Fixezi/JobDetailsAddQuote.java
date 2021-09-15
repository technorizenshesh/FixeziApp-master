package com.cliffex.Fixezi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JobDetailsAddQuote extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_textview;
    private TextView save_send_quote;
    private RelativeLayout NavigationUpIM;
    private EditText jobs_et;
    private EditText price_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details_add_quote);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);

        jobs_et = (EditText) findViewById(R.id.jobs_et);
        price_et = (EditText) findViewById(R.id.price_et);

        save_send_quote = (TextView) findViewById(R.id.save_send_quote);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        save_send_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (jobs_et.getText().toString().equalsIgnoreCase("")){

                    SweetAlertDialog alertDialog = new SweetAlertDialog(JobDetailsAddQuote.this, SweetAlertDialog.NORMAL_TYPE);
                    alertDialog.setTitleText("fixezi");
                    alertDialog.setContentText("Please Enter quote description ");
                    alertDialog.show();

                    Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn.setBackgroundColor(ContextCompat.getColor(JobDetailsAddQuote.this, R.color.lightblue));
                }

                else if (price_et.getText().toString().equalsIgnoreCase("")){

                    SweetAlertDialog alertDialog = new SweetAlertDialog(JobDetailsAddQuote.this, SweetAlertDialog.NORMAL_TYPE);
                    alertDialog.setTitleText("fixezi");
                    alertDialog.setContentText("Please Enter your price ");
                    alertDialog.show();

                    Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn.setBackgroundColor(ContextCompat.getColor(JobDetailsAddQuote.this, R.color.lightblue));
                }

                else {


                }

             //   finish();
            }
        });


        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        toolbar_textview.setText("Job Detail - Add Quote");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }
}
