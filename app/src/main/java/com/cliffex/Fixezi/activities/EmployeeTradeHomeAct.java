package com.cliffex.Fixezi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Model.ModelEmployeesTrade;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.R;
import com.cliffex.Fixezi.SessionTradesman;
import com.cliffex.Fixezi.TradesmanSignup2;
import com.cliffex.Fixezi.adapter.AdapterEmployees;
import com.cliffex.Fixezi.ratrofit.ApiClient;
import com.cliffex.Fixezi.ratrofit.TrademanSignup;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeTradeHomeAct extends AppCompatActivity {

    Context mContext = EmployeeTradeHomeAct.this;
    TextView toolbar_textview;
    RecyclerView rvEmployees;
    ImageView image_nav;
    SessionTradesman sessionTradesman;
    ArrayList<ModelEmployeesTrade> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_trade_home);
        sessionTradesman = new SessionTradesman(this);

        Log.e("adadasd","user_id = " + sessionTradesman.getId());

        itit();
    }

    private void itit() {

        toolbar_textview = findViewById(R.id.toolbar_title);
        rvEmployees = findViewById(R.id.rvEmployees);
        image_nav = findViewById(R.id.image_nav);
        toolbar_textview.setText("Employee's Job's");

        image_nav.setOnClickListener(v -> {
            finish();
        });

        getEmployeesApi();

    }

    private void getEmployeesApi() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));
        AndroidNetworking.post(HttpPAth.Urlpath + "worker_user_list")
                .addBodyParameter("tradesman_id",sessionTradesman.getId())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        try {
                            employeeList = new Gson().fromJson(response, new TypeToken<ArrayList<ModelEmployeesTrade>>(){}.getType());
                            AdapterEmployees adapterEmployees = new AdapterEmployees(mContext,employeeList);
                            rvEmployees.setAdapter(adapterEmployees);
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

}