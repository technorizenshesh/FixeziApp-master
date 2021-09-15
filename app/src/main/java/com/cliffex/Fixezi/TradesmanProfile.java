package com.cliffex.Fixezi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Model.ModelEmployeesTrade;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.adapter.AdapterEmployees;
import com.cliffex.Fixezi.ratrofit.ApiClient;
import com.cliffex.Fixezi.ratrofit.ApiInterface;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradesmanProfile extends AppCompatActivity {

    TextView TellaFriendTV,WhatdoyouthinkTV,UnsubscribeTV,SubscribeTV,CreateUserTV;
    SessionTradesman sessionTradesman;
    TextView toolbar_title, TradesmanInfoTV;
    Toolbar tradesman_profile_toolbar;
    RelativeLayout NavigationUpIM,rel_rating;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String WhichPlan = "";
    private RelativeLayout fb_link;
    private Button active;
    String holdStatus = "0";
    Context mContext = TradesmanProfile.this;
    private Button hodl;
    String dialogHoldText="You are able to put a temporary 'hold' on your company, all user's will not be ablr to view/select your company as it will show that you are unavailable, simply select <font color='#7CC25E'>'YES' </font>below to put a 'hold' on your company. You can always remove the 'hold' by selecting 'active' at any time.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman_profile);

        sharedPreferences = getApplicationContext().getSharedPreferences("FixeziPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadData();

        sessionTradesman = new SessionTradesman(this);
        TradesmanInfoTV = (TextView) findViewById(R.id.TradesmanInfoTV);
        TellaFriendTV = (TextView) findViewById(R.id.TellaFriendTV);
        WhatdoyouthinkTV = (TextView) findViewById(R.id.WhatdoyouthinkTV);
        SubscribeTV = (TextView) findViewById(R.id.SubscribeTV);
        UnsubscribeTV = (TextView) findViewById(R.id.UnsubscribeTV);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tradesman_profile_toolbar = (Toolbar) findViewById(R.id.tradesman_profile_toolbar);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        CreateUserTV = (TextView) findViewById(R.id.CreateUserTV);
        rel_rating= (RelativeLayout) findViewById(R.id.rel_rating);
        active  = (Button) findViewById(R.id.active);
        hodl = (Button) findViewById(R.id.hodl);

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog postalDialog = new Dialog(TradesmanProfile.this);
                postalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                postalDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                postalDialog.setContentView(R.layout.alert_dailoge_active);

                Button cancelBt = (Button) postalDialog.findViewById(R.id.cancelBt);
                Button acceptBt = (Button) postalDialog.findViewById(R.id.acceptBt);

                acceptBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeStatus("12345");
                        postalDialog.dismiss();
                    }
                });

                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postalDialog.dismiss();
                    }
                });
                postalDialog.show();
            }
        });

        hodl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holdDialog();
            }
        });

        fb_link  = (RelativeLayout) findViewById(R.id.fb_link);

        rel_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradesmanProfile.this,   RatingActivity.class);
                startActivity(intent);
            }
        });

        fb_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TradesmanProfile.this, Nav_FB.class);
                startActivity(intent);
            }
        });

        toolbar_title.setText("Company Profile");

        setSupportActionBar(tradesman_profile_toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        CreateUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WhichPlan.equalsIgnoreCase(Appconstants.SKU_ECONOMY)){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(TradesmanProfile.this);
                    builder1.setMessage("Your current plan(Economy) does not support this feature.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else{

                    Intent intent = new Intent(TradesmanProfile.this, CreateEmployee.class);
                    startActivity(intent);
                }
            }
        });

        UnsubscribeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TradesmanProfile.this, RemoveAccount.class);
                startActivity(i);

            }
        });

        SubscribeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /* WeeklyClicked(); */
                Intent intent = new Intent(TradesmanProfile.this, PaymentPlan.class);
                startActivity(intent);
            }
        });

        TellaFriendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TradesmanProfile.this, TellaFriendAbout.class);
                startActivity(i);

            }
        });

        WhatdoyouthinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TradesmanProfile.this, WhatYouthink.class);
                startActivity(i);
            }
        });

        TradesmanInfoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TradesmanProfile.this, YourInformation.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
    }

    void loadData() {
        WhichPlan = sharedPreferences.getString("WhichPlan", "Pay Per Job");
        Log.e("WhichPlan", WhichPlan);
    }

    private void getProfileRetrofit() {
        ApiInterface apiInterface = ApiClient.getClient(mContext).create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.onHoldApi(sessionTradesman.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                try {
                    String stringResponse = response.body().string();
                    JSONArray jsonArray = new JSONArray(stringResponse);
                    JSONObject result = jsonArray.getJSONObject(0);
                    Log.e("getProfileRetrofit","result = " + result.getString("bzy_status"));
                    holdStatus = result.getString("bzy_status");

                    if("0".equals(holdStatus)) {
                        active.setBackgroundResource(R.drawable.btn_hold);
                        active.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                        hodl.setBackgroundResource(R.drawable.border_black_solid_yellow);
                    } else {
                        active.setBackgroundResource(R.drawable.btn_active_bg);
                        hodl.setBackgroundResource(R.drawable.btn_hold);
                        active.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("getProfileRetrofit","Throwable = " + t.getMessage());
            }
        });
    }

    private void getProfile() {
        AndroidNetworking.post(HttpPAth.Urlpath + "get_tradesman_profile")
                .addBodyParameter("tradesman_id",sessionTradesman.getId())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        try {
                            Log.e("response","response = " + response);
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject result = jsonArray.getJSONObject(0);
                            Log.e("get_tradesman_profile","result = " + result.getString("bzy_status"));
                            holdStatus = result.getString("bzy_status");

                            if("1".equals(holdStatus)) {
                                active.setBackgroundResource(R.drawable.btn_hold);
                                active.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                                hodl.setBackgroundResource(R.drawable.border_black_solid_yellow);
                            } else {
                                active.setBackgroundResource(R.drawable.btn_active_bg);
                                hodl.setBackgroundResource(R.drawable.btn_hold);
                                active.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });
    }

    private void changeStatus(String status) {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));

        Log.e("dfsasfdasff","tradesman_id = "+sessionTradesman.getId());
        Log.e("dfsasfdasff","status = "+status);

        AndroidNetworking.post(HttpPAth.Urlpath + "bzystatus_change_by_tradesman")
                .addBodyParameter("tradesman_id",sessionTradesman.getId())
                .addBodyParameter("status",status)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        if(status.equals("12345")) {
                            holdStatus = "0";
                        } else {
                            holdStatus = "1";
                        }
                        try {
                            Log.e("changeStatus","changeStatus = " + response);
                            if("1".equals(holdStatus)) {
                                active.setBackgroundResource(R.drawable.btn_hold);
                                active.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                                hodl.setBackgroundResource(R.drawable.border_black_solid_yellow);
                            } else {
                                active.setBackgroundResource(R.drawable.btn_active_bg);
                                hodl.setBackgroundResource(R.drawable.btn_hold);
                                active.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });
    }

    private void holdDialog() {
        final Dialog postalDialog = new Dialog(TradesmanProfile.this);
        postalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        postalDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        postalDialog.setContentView(R.layout.alert_dailoge_active);

        Button cancelBt = (Button) postalDialog.findViewById(R.id.cancelBt);
        Button acceptBt = (Button) postalDialog.findViewById(R.id.acceptBt);

        acceptBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus("bzy");
                postalDialog.dismiss();
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalDialog.dismiss();
            }
        });

        postalDialog.show();

    }

}
