package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.content.Intent;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileVerfication extends AppCompatActivity {

    TextView SubmitOTPTV, tv_regenerate;
    EditText OTPET;
    ImageView BackNMIM;
    String code;
    String mobnumber, mobilestatus = "";
    private String verify_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verfication);

        SubmitOTPTV = (TextView) findViewById(R.id.SubmitOTPTV);
        tv_regenerate = (TextView) findViewById(R.id.tv_regenerate);
        OTPET = (EditText) findViewById(R.id.OTPET);
        BackNMIM = (ImageView) findViewById(R.id.BackNMIM);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                verify_code = null;
            } else {
                verify_code = extras.getString("verify_code");
                mobnumber = extras.getString("mobile");
            }
        } else {
            verify_code = (String) savedInstanceState.getSerializable("verify_code");
        }

       // Toast.makeText(this, verify_code, Toast.LENGTH_SHORT).show();

        BackNMIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_regenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpVerify(mobnumber);
            }
        });

        SubmitOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OTPET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(MobileVerfication.this, "Enter OTP Please", Toast.LENGTH_SHORT).show();
                } else {
                    if (verify_code.equals(OTPET.getText().toString())) {
                        TradesmanSignUp.isVerifyMobile = true;
                        Intent intent = new Intent();
                        intent.putExtra("otp_status", "true");
                        setResult(3, intent);
                        finish();
                        Toast.makeText(MobileVerfication.this, "OTP Correct", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MobileVerfication.this, "OTP Not Correct", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void otpVerify(String mobile) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MobileVerfication.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        AndroidNetworking.get("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?mobile_verify&mobile=" + mobile)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String result = response.getString("result");
                            String status = response.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                verify_code = response.getString("verify_code");
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

       /* Toast.makeText(getApplicationContext(), mobile, Toast.LENGTH_SHORT).show();
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MobileVerfication.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Call<ResponseBody> call = AppConfig.loadInterface().otpverify(mobile);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                try {

                    if (response.isSuccessful()) {

                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String message = object.getString("message");

                        if (object.getString("status").equals("1")) {

                            code = object.getString("verify_code");

                            *//*  Toast.makeText(getApplicationContext(),"code!"+code,Toast.LENGTH_SHORT).show();*//*

                        } else {

                            Toast.makeText(MobileVerfication.this, "" + message, Toast.LENGTH_SHORT).show();
                        }

                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(MobileVerfication.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}
