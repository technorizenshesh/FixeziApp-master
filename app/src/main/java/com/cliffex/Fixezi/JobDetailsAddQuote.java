package com.cliffex.Fixezi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.util.ProjectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

public class JobDetailsAddQuote extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_textview;
    private TextView save_send_quote, tvJobId;
    private RelativeLayout NavigationUpIM;
    private EditText jobs_et;
    private EditText price_et;
    private String problemId, notes;
    RadioButton rb7, rb14;
    ArrayList<File> fileArray = new ArrayList<>();
    File file1, file2, file3, file4, file5, file6, file7, file8, file9, file10;
    HashMap<String, File> fileHashMap = new HashMap<>();
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details_add_quote);

        fileArray = (ArrayList<File>) getIntent().getSerializableExtra("filearray");
        problemId = getIntent().getStringExtra("problemId");
        notes = getIntent().getStringExtra("notes");
        type = getIntent().getStringExtra("type");

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        rb7 = findViewById(R.id.rb7);
        rb14 = findViewById(R.id.rb14);

        jobs_et = (EditText) findViewById(R.id.jobs_et);
        tvJobId = findViewById(R.id.tvJobId);
        price_et = (EditText) findViewById(R.id.price_et);

        tvJobId.setText("Job Id " + problemId);

        save_send_quote = (TextView) findViewById(R.id.save_send_quote);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        setFiles();

        rb7.setOnClickListener(v -> {
            rb14.setChecked(false);
        });

        rb14.setOnClickListener(v -> {
            rb7.setChecked(false);
        });

        save_send_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (jobs_et.getText().toString().equalsIgnoreCase("")) {

                    SweetAlertDialog alertDialog = new SweetAlertDialog(JobDetailsAddQuote.this, SweetAlertDialog.NORMAL_TYPE);
                    alertDialog.setTitleText("fixezi");
                    alertDialog.setContentText("Please Enter quote description");
                    alertDialog.show();

                    Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn.setBackgroundColor(ContextCompat.getColor(JobDetailsAddQuote.this, R.color.lightblue));

                } else if (price_et.getText().toString().equalsIgnoreCase("")) {

                    SweetAlertDialog alertDialog = new SweetAlertDialog(JobDetailsAddQuote.this, SweetAlertDialog.NORMAL_TYPE);
                    alertDialog.setTitleText("fixezi");
                    alertDialog.setContentText("Please Enter your price");
                    alertDialog.show();

                    Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn.setBackgroundColor(ContextCompat.getColor(JobDetailsAddQuote.this, R.color.lightblue));

                } else {
                    if (rb7.isChecked()) {
                        sendQuoteToUseApi("7");
                    } else if (rb14.isChecked()) {
                        sendQuoteToUseApi("14");
                    } else {
                        Toast.makeText(JobDetailsAddQuote.this, "Please select Days", Toast.LENGTH_SHORT).show();
                    }
                }
                // finish();
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

    private void sendQuoteToUseApi(String quoteLong) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("problem_id", problemId);
        hashMap.put("quote_description", jobs_et.getText().toString().trim());
        hashMap.put("quotelong", quoteLong);
        hashMap.put("quote_price", price_et.getText().toString().trim());
        hashMap.put("notes_description", notes);
        hashMap.put("notes_type", "");
        hashMap.put("type", "quote");

        fileHashMap.put("image1", file1);
        fileHashMap.put("image2", file2);
        fileHashMap.put("image3", file3);
        fileHashMap.put("image4", file4);
        fileHashMap.put("image5", file5);
        fileHashMap.put("image6", file6);
        fileHashMap.put("image7", file7);
        fileHashMap.put("image8", file8);
        fileHashMap.put("image9", file9);
        fileHashMap.put("image10", file10);

        ProjectUtil.showProgressDialog(JobDetailsAddQuote.this,false,"Please wait...");
        AndroidNetworking.upload(HttpPAth.Urlpath + "add_trademan_notes")
                .addMultipartParameter(hashMap)
                .addMultipartFile(fileHashMap)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        SweetAlertDialog alertDialog = new SweetAlertDialog(JobDetailsAddQuote.this, SweetAlertDialog.NORMAL_TYPE);
                        alertDialog.setTitleText("fixezi");
                        alertDialog.setContentText("Quotes Send To User Success");
                        alertDialog.show();

                        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                        btn.setBackgroundColor(ContextCompat.getColor(JobDetailsAddQuote.this, R.color.lightblue));

                        btn.setOnClickListener(v -> {
                            if("incomplete".equals(type)) {
                                finish();
                                startActivity(new Intent(JobDetailsAddQuote.this,TradesmanActivity.class));
                            } else { finish(); }
                        });

                        Log.e("responseresponse","response = " + response);
                    }
                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

    private void setFiles() {

        try {
            file1 = fileArray.get(0);
        } catch (Exception e) {
            return;
        }

        try {
            file2 = fileArray.get(1);
        } catch (Exception e) {
            return;
        }

        try {
            file3 = fileArray.get(2);
        } catch (Exception e) {
            return;
        }

        try {
            file4 = fileArray.get(3);
        } catch (Exception e) {
            return;
        }

        try {
            file5 = fileArray.get(4);
        } catch (Exception e) {
            return;
        }

        try {
            file6 = fileArray.get(5);
        } catch (Exception e) {
            return;
        }

        try {
            file7 = fileArray.get(6);
        } catch (Exception e) {
            return;
        }

        try {
            file8 = fileArray.get(7);
        } catch (Exception e) {
            return;
        }

        try {
            file9 = fileArray.get(8);
        } catch (Exception e) {
            return;
        }

        try {
            file10 = fileArray.get(9);
        } catch (Exception e) {
            return;
        }

    }

}
