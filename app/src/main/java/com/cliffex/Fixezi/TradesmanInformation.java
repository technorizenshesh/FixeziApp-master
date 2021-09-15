package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TradesmanInformation extends AppCompatActivity {
    Toolbar tradesamn_toolbar;
    TextView toolbar_title;
    CheckBox CheckboxTradesman;
    ImageView ImageTradesman;
    RelativeLayout RLSaveTradesman;
    EditText Ownersname, businessAddress, BusinessNameTradesman,
            TradingNameTradesman, OfficePhoneTradesman, MobileNumberTradesman,
            EmailTradesman, WebsiteNameTradesman;
    SessionTradesman sessionTradesman;
    RelativeLayout NavigationUpIM;
    TextView EditInfoTV;
    ProgressDialog progressDialog;
    private String busseness_address;
    private String id;
    private String jsonresult1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.Please Wait ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman_information);

        tradesamn_toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Edit Information");
        setSupportActionBar(tradesamn_toolbar);
        sessionTradesman = new SessionTradesman(this);
        HashMap<String, String> TradesmanInfo = sessionTradesman.getUserDetails();

        id =  sessionTradesman.getId();

        Toast.makeText(this, "idd"+id, Toast.LENGTH_SHORT).show();

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageTradesman = (ImageView) findViewById(R.id.ImageTradesman);
        EditInfoTV = (TextView) findViewById(R.id.EditInfoTV);
        BusinessNameTradesman = (EditText) findViewById(R.id.BusinessNameTradesman);
        TradingNameTradesman = (EditText) findViewById(R.id.TradingNameTradesman);
        OfficePhoneTradesman = (EditText) findViewById(R.id.OfficePhoneTradesman);
        MobileNumberTradesman = (EditText) findViewById(R.id.MobileNumberTradesman);
        EmailTradesman = (EditText) findViewById(R.id.EmailTradesman);
        Ownersname = (EditText) findViewById(R.id.Ownersname);
        businessAddress = (EditText) findViewById(R.id.businessAddress);
        WebsiteNameTradesman = (EditText) findViewById(R.id.WebsiteNameTradesman);
        CheckboxTradesman = (CheckBox) findViewById(R.id.CheckboxTradesman);
        RLSaveTradesman = (RelativeLayout) findViewById(R.id.RLSaveTradesman);

        CheckboxTradesman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    BusinessNameTradesman.setEnabled(true);
                    TradingNameTradesman.setEnabled(true);
                    OfficePhoneTradesman.setEnabled(true);
                    MobileNumberTradesman.setEnabled(true);
                    WebsiteNameTradesman.setEnabled(true);
                    Ownersname.setEnabled(true);
                    businessAddress.setEnabled(true);

                } else {

                    BusinessNameTradesman.setEnabled(false);
                    TradingNameTradesman.setEnabled(false);
                    OfficePhoneTradesman.setEnabled(false);
                    MobileNumberTradesman.setEnabled(false);
                    WebsiteNameTradesman.setEnabled(false);
                    businessAddress.setEnabled(false);
                    Ownersname.setEnabled(false);
                }
            }
        });

        RLSaveTradesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("onclick", "yes");

                if (CheckboxTradesman.isChecked()) {
                    if(Validation()) {
                        if(InternetDetect.isConnected(TradesmanInformation.this)) {
                            new JsonUpdateInfo().execute(sessionTradesman.getId(), BusinessNameTradesman.getText().toString(),
                                    TradingNameTradesman.getText().toString(), OfficePhoneTradesman.getText().toString(),
                                    MobileNumberTradesman.getText().toString(), EmailTradesman.getText().toString(),
                                    WebsiteNameTradesman.getText().toString(), Ownersname.getText().toString(), businessAddress.getText().toString());
                        } else {
                            Toast.makeText(TradesmanInformation.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(TradesmanInformation.this, "Please check the box to edit", Toast.LENGTH_SHORT).show();
                }
            }

             private boolean Validation() {
                if (BusinessNameTradesman.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TradesmanInformation.this, "Enter Business Name", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (OfficePhoneTradesman.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TradesmanInformation.this, "Enter Office Phone", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (MobileNumberTradesman.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TradesmanInformation.this, "Enter Mobile Phone", Toast.LENGTH_SHORT).show();
                    return false;

                }
                return true;
            }
        });

        if (InternetDetect.isConnected(this)) {
            new JsonGetInfo().execute();

        } else {

            Toast.makeText(TradesmanInformation.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class JsonUpdateInfo extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected TradesManBean doInBackground(String... paramss) {
            try {
                URL url = new URL(HttpPAth.Urlpath + "update_trads_men_profile");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", paramss[0]);
                params.put("business_name", paramss[1]);
                params.put("trading_name", paramss[2]);
                params.put("office_no", paramss[3]);
                params.put("mobile_no", paramss[4]);
                params.put("email", paramss[5]);
                params.put("website_url", paramss[6]);
                params.put("businessOwnerrnName", paramss[7]);
                params.put("business_name", paramss[8]);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if(postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();

                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                writer.close();
                reader.close();
                System.out.println(response);
                Log.e("Json", response);
                Gson gson = new Gson();
                JSONArray ParentArray = new JSONArray(response);

                JSONObject Object = ParentArray.getJSONObject(0);
                TradesManBean tradesManBean = gson.fromJson(Object.toString(), TradesManBean.class);

                jsonresult = Object.getString("result");
                return tradesManBean;
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(TradesManBean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(jsonresult.equalsIgnoreCase("successfully")) {

                busseness_address =  result.getBusiness_name();
                BusinessNameTradesman.setText(result.getBusiness_name());
                TradingNameTradesman.setText(result.getTrading_name());
                OfficePhoneTradesman.setText(result.getOffice_no());
                MobileNumberTradesman.setText(result.getMobile_no());
                EmailTradesman.setText(result.getEmail());
                WebsiteNameTradesman.setText(result.getWebsite_url());
                businessAddress.setText(result.getBusiness_address());
                Ownersname.setText(result.getBusinessOwnerrnName());

                Glide.with(TradesmanInformation.this).load(result.getProfile_pic())
                        .thumbnail(0.5f)
                        .into(ImageTradesman);

                sessionTradesman.createLoginSession(result.getId(), result.getBusiness_name(), result.getTrading_name(), result.getOffice_no(), result.getMobile_no(), result.getEmail(),
                        result.getWebsite_url(), result.getProfile_pic(), result.getBzy_status());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

             case android.R.id.home:

                  finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JsonGetInfo extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected TradesManBean doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_tradesman_profile&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", sessionTradesman.getId());
                StringBuilder postData = new StringBuilder();

                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }

                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                writer.close();
                reader.close();
                Log.e("Json", response);

                Gson gson = new Gson();
                JSONArray ParentArray = new JSONArray(response);

                JSONObject Object = ParentArray.getJSONObject(0);

                TradesManBean tradesManBean = gson.fromJson(Object.toString(), TradesManBean.class);
                jsonresult = Object.getString("result");
                jsonresult1 = Object.getString("businessAddress");
                Log.e("jsonresult",jsonresult1);
                return tradesManBean;

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TradesManBean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (jsonresult.equalsIgnoreCase("successfully")) {

                Log.e("jsonresulttrad>-","bussenessname>>"+result.getBusiness_name()+"getTrading_name>>"+
                        result.getTrading_name()+"getTrading_name>>"+result.getBusiness_address());

                BusinessNameTradesman.setText(result.getBusiness_name());
                TradingNameTradesman.setText(result.getTrading_name());
                OfficePhoneTradesman.setText(result.getOffice_no());
                MobileNumberTradesman.setText(result.getMobile_no());
                EmailTradesman.setText(result.getEmail());
                WebsiteNameTradesman.setText(result.getWebsite_url());
                businessAddress.setText(jsonresult1);
                Ownersname.setText(result.getBusinessOwnerrnName());

                Glide.with(TradesmanInformation.this).load(result.getProfile_pic()).thumbnail(0.5f).into(ImageTradesman);

            }
        }
    }
}
