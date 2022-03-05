package com.cliffex.Fixezi;

import android.app.Activity;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Model.ModelMyCards;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.adapter.AdapterMyCards;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseCardAct extends AppCompatActivity {

    Context mContext = ChooseCardAct.this;
    ImageView ivAddCard, image_nav;
    SwipeRefreshLayout swipLayout;
    RecyclerView rvChooseCard;
    String cusId = "";
    SessionTradesman sessionTradesman;
    public static Activity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        instance = this;
        sessionTradesman = new SessionTradesman(this);
        itit();
    }

    private void itit() {

        ivAddCard = findViewById(R.id.ivAddCard);
        swipLayout = findViewById(R.id.swipLayout);
        rvChooseCard = findViewById(R.id.rvChooseCard);
        image_nav = findViewById(R.id.image_nav);

        getCards();

        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCards();
            }
        });

        ivAddCard.setOnClickListener(v -> {
            startActivity(new Intent(mContext, CardPayment_Activity.class)
                    .putExtra("cusid", cusId));
        });

        image_nav.setOnClickListener(v -> {
            finish();
        });

    }

    private void getCards() {

        ProjectUtil.showProgressDialog(mContext, true, "Please wait...");

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionTradesman.getId());

        Log.e("addCardApi", "param = " + param);

        AndroidNetworking.post(HttpPAth.Urlpath + "get_user_card")
                .addBodyParameter(param)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("addCardApi", "response = " + response);
                        ProjectUtil.pauseProgressDialog();
                        swipLayout.setRefreshing(false);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {

                                JSONObject resultObj = jsonObject.getJSONObject("result");
                                cusId = resultObj.getString("id");

                                String str = resultObj.getJSONObject("sources").getString("data");

                                ArrayList<ModelMyCards.Data> cardList = new Gson().fromJson(str, new TypeToken<ArrayList<ModelMyCards.Data>>() {
                                }.getType());

                                AdapterMyCards adapterMyCards = new AdapterMyCards(mContext, cardList, ChooseCardAct.this::addPaymentApiCallback);
                                rvChooseCard.setAdapter(adapterMyCards);

                            } else {
                                AdapterMyCards adapterMyCards = new AdapterMyCards(mContext, null, ChooseCardAct.this::addPaymentApiCallback);
                                rvChooseCard.setAdapter(adapterMyCards);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipLayout.setRefreshing(false);
                        ProjectUtil.pauseProgressDialog();
                        Log.e("adfasdasdas", "ANError = " + anError.getErrorDetail());
                        Log.e("adfasdasdas", "ANError = " + anError.getErrorBody());
                    }

                });

    }

    public void addPaymentApiCallback(ModelMyCards.Data data) {
        addPaymentUpdateApiCall(data);
        // addPaymentApi(data);
    }

    private void addPaymentUpdateApiCall(ModelMyCards.Data data) {

        HashMap<String, String> param = new HashMap<>();

        param.put("user_id", sessionTradesman.getId());
        param.put("plan_type", "PayPerJob");
        param.put("total_amount", "9.95");
        param.put("payment_method", "Card");
        param.put("token", data.getCustomer());
        param.put("currency", "AUD");

        Log.e("addPaymentApi", "paramparamparam = " + param);

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait));
        AndroidNetworking.post(HttpPAth.Urlpath + "add_payment")
                .addBodyParameter(param)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        PaymentPlan.instance.finish();
                        finish();
                        // startActivity(new Intent(mContext, TradesmanActivity.class));
                        Log.e("addPaymentApi", "addPaymentApi = " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                        Log.e("addPaymentApi", "anError = " + anError.getErrorBody());
                        Log.e("addPaymentApi", "anError = " + anError.getErrorDetail());
                    }
                });

    }

    private void addPaymentApi(ModelMyCards.Data data) {

        HashMap<String, String> param = new HashMap<>();
        param.put("cust_id", data.getCustomer());
        param.put("plan_type", "PayPerJob");
        param.put("user_id", sessionTradesman.getId());
        param.put("card_id", data.getId());

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait));
        AndroidNetworking.post(HttpPAth.Urlpath + "update_plan_type")
                .addBodyParameter(param)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        startActivity(new Intent(mContext, TradesmanActivity.class));
                        Log.e("addPaymentApi", "addPaymentApi = " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                        Log.e("addPaymentApi", "anError = " + anError.getErrorBody());
                        Log.e("addPaymentApi", "anError = " + anError.getErrorDetail());
                    }
                });

    }

}