package com.cliffex.Fixezi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.Other.AppConfig;
import com.cliffex.Fixezi.R;
import com.cliffex.Fixezi.SessionTradesman;
import com.cliffex.Fixezi.SessionUser;
import com.cliffex.Fixezi.TradesmanActivity;
import com.cliffex.Fixezi.UserRating;
import com.cliffex.Fixezi.util.ProjectUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRatingAfterInvoiceAct extends AppCompatActivity {

    Context mContext = UserRatingAfterInvoiceAct.this;
    String userId = "", userName = "";
    TextView toolbar_title, qotesaccepted, tv_canceljob, tvCustomerName;
    RelativeLayout NavigationUpIM, doneratesystem, backratesystem;
    RatingBar ratin1, ratin2, ratin3;
    ProgressDialog progressDialog;
    SessionUser sessionUser;
    SessionTradesman sessionTradesman;
    private String problemId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating_after_invoice);

        progressDialog = new ProgressDialog(this);
        sessionUser = new SessionUser(this);
        sessionTradesman = new SessionTradesman(this);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        qotesaccepted = (TextView) findViewById(R.id.qotesaccepted);
        doneratesystem = findViewById(R.id.doneratesystem);
        backratesystem = findViewById(R.id.backratesystem);
        tv_canceljob = (TextView) findViewById(R.id.tv_canceljob);
        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        ratin1 = findViewById(R.id.ratin1);
        ratin2 = findViewById(R.id.ratin2);
        ratin3 = findViewById(R.id.ratin3);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        toolbar_title.setText("User rating");

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("username");
        problemId = getIntent().getStringExtra("problemId");

        tvCustomerName.setText(userName);

        Log.e("asfasfdsfdsfdsf", "userIduserId = " + userId);
        Log.e("asfasfdsfdsfdsf", "userName = " + userName);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "DroidSerif.ttf");
        toolbar_title.setTypeface(custom_font);

        doneratesystem.setOnClickListener(v -> {
            callRatingApis();
        });

        backratesystem.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(mContext, TradesmanActivity.class));
        });

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUserRating();

    }

    private void callRatingApis() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", userId);
        param.put("tradesman_id", sessionTradesman.getId());
        param.put("problem_id", problemId);
        param.put("easily_contacted", String.valueOf(ratin1.getRating()));
        param.put("ease_of_job", String.valueOf(ratin2.getRating()));
        param.put("payment_on_completion", String.valueOf(ratin3.getRating()));

        Log.e("asdasdasd", "paramparamparam = " + param);

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait));
        AndroidNetworking.post(HttpPAth.Urlpath + "rating_tradesman")
                .addBodyParameter(param)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        finish();
                        startActivity(new Intent(mContext, TradesmanActivity.class));
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

    private void getUserRating() {

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", userId);

        Call<ResponseBody> call = AppConfig.loadInterface().getUserRatingApiCall(param);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        Log.e("sdfsfsdfsd", "responseDataRetrofi = " + responseData);

                        // String message=object.getString("message");

                        tv_canceljob.setText(obj.getString("job_cancellations"));
                        qotesaccepted.setText(obj.getString("quotes_accepted_declined"));

                        JSONObject jsonObject = obj.getJSONObject("rating");
                        String easily_contacted = jsonObject.getString("easily_contacted");
                        String ease_of_job = jsonObject.getString("ease_of_job");
                        String payment_on_completion = jsonObject.getString("payment_on_completion");

                        Log.e("easily_contatcted", "" + easily_contacted);

//                        ratin3.setRating(Float.parseFloat(payment_on_completion));
//                        ratin2.setRating(Float.parseFloat(ease_of_job));
//                        ratin1.setRating(Float.parseFloat(easily_contacted));

                    } else ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(UserRatingAfterInvoiceAct.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }

        });

    }

}