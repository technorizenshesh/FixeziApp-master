package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.Other.AppConfig;
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
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cliffex.Fixezi.Other.MySharedPref.getData;

public class EditCallout extends AppCompatActivity {

    SessionTradesman sessionTradesman;
    TextView toolbar_textview, tv_value_chage;
    RelativeLayout NavigationUpIM;
    Button BothBT, AfterHourNoBt, ResidentialBT,
           CommercialBT, AfterHourYesBt, UpdateCalloutBT;
    CheckBox EditCheckBox;
    String AfterHour = "", ServiceCategory = "";
    ProgressDialog progressDialog;
    SeekBar seekBar_book;
    String TradesmanID, str_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.Please Wait ......");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_callout);

        sessionTradesman = new SessionTradesman(this);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title_EC);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpEC);

        AfterHourYesBt = (Button) findViewById(R.id.AfterHourYesBt);
        AfterHourNoBt = (Button) findViewById(R.id.AfterHourNoBt);
        ResidentialBT = (Button) findViewById(R.id.ResidentialBT);
        CommercialBT = (Button) findViewById(R.id.CommercialBT);
        BothBT = (Button) findViewById(R.id.BothBT);
        EditCheckBox = (CheckBox) findViewById(R.id.EditCheckBox);
        UpdateCalloutBT = (Button) findViewById(R.id.UpdateCalloutBT);
        seekBar_book = (SeekBar) findViewById(R.id.seekBar_book);
        tv_value_chage = (TextView) findViewById(R.id.tv_value_chage);

        TradesmanID = getData(getApplicationContext(), "tradmen_id", null);

        System.out.println("TTradesmanID__________" + TradesmanID);

        tradmenprofile(TradesmanID);
        toolbar_textview.setText("Edit Callout and Service Category");
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        AfterHourYesBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AfterHour = "yes";
                AfterHourYesBt.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                AfterHourYesBt.setTextColor(Color.parseColor("#ffffff"));
                AfterHourNoBt.setBackgroundResource(R.drawable.border_black_solid_white);
                AfterHourNoBt.setTextColor(Color.parseColor("#000000"));
            }
        });

        AfterHourNoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AfterHour = "no";
                AfterHourNoBt.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                AfterHourNoBt.setTextColor(Color.parseColor("#ffffff"));
                AfterHourYesBt.setBackgroundResource(R.drawable.border_black_solid_white);
                AfterHourYesBt.setTextColor(Color.parseColor("#000000"));
            }
        });

        seekBar_book.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        CommercialBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceCategory = "commercial";

                CommercialBT.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                CommercialBT.setTextColor(Color.parseColor("#ffffff"));
                ResidentialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                ResidentialBT.setTextColor(Color.parseColor("#000000"));
                BothBT.setBackgroundResource(R.drawable.border_black_solid_white);
                BothBT.setTextColor(Color.parseColor("#000000"));

            }
        });


        ResidentialBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceCategory = "residential";

                ResidentialBT.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                ResidentialBT.setTextColor(Color.parseColor("#ffffff"));

                CommercialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                CommercialBT.setTextColor(Color.parseColor("#000000"));
                BothBT.setBackgroundResource(R.drawable.border_black_solid_white);
                BothBT.setTextColor(Color.parseColor("#000000"));
            }
        });

        BothBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceCategory = "both";
                BothBT.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                BothBT.setTextColor(Color.parseColor("#ffffff"));
                ResidentialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                ResidentialBT.setTextColor(Color.parseColor("#000000"));
                CommercialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                CommercialBT.setTextColor(Color.parseColor("#000000"));
            }
        });


        EditCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    BothBT.setEnabled(true);
                    ResidentialBT.setEnabled(true);
                    CommercialBT.setEnabled(true);
                    AfterHourNoBt.setEnabled(true);
                    AfterHourYesBt.setEnabled(true);

                } else {

                    BothBT.setEnabled(false);
                    ResidentialBT.setEnabled(false);
                    CommercialBT.setEnabled(false);
                    AfterHourNoBt.setEnabled(false);
                    AfterHourYesBt.setEnabled(false);

                }


            }
        });


        UpdateCalloutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (EditCheckBox.isChecked()) {

                    if (Validation()) {
                        new JsonUpdate().execute(sessionTradesman.getId(), AfterHour, ServiceCategory);
                    }
                } else {

                    Toast.makeText(EditCallout.this, "Please check the box", Toast.LENGTH_SHORT).show();

                }

            }

            private boolean Validation() {

                if (AfterHour.equalsIgnoreCase("")) {

                    Toast.makeText(EditCallout.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ServiceCategory.equalsIgnoreCase("")) {

                    Toast.makeText(EditCallout.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

        if (InternetDetect.isConnected(this)) {
            new JsonGetInfo().execute();
        } else {
            Toast.makeText(EditCallout.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class JsonUpdate extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected TradesManBean doInBackground(String... paramss) {
            try {
                URL url = new URL(HttpPAth.Urlpath + "work_location_after_hours_Upadte&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", paramss[0]);
                params.put("after_hours", paramss[1]);
                params.put("work_location", paramss[2]);

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
            if (jsonresult.equalsIgnoreCase("successfully")) {
                Toast.makeText(EditCallout.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(EditCallout.this, TradesmanActivity.class);
                in.putExtra("AfterHour",AfterHour);
                startActivity(in);
                finish();
            }
        }
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

            if (jsonresult.equalsIgnoreCase("successfully")) {

                Log.e("GetInfo Success", "yes");
                Log.e("Value1", result.getAfter_hours());
                Log.e("Value2", result.getWork_location());

                if (result.getAfter_hours().equalsIgnoreCase("yes")) {
                    AfterHour = "yes";
                    AfterHourYesBt.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                    AfterHourYesBt.setTextColor(Color.parseColor("#ffffff"));
                    AfterHourNoBt.setBackgroundResource(R.drawable.border_black_solid_white);
                    AfterHourNoBt.setTextColor(Color.parseColor("#000000"));

                } else if (result.getAfter_hours().equalsIgnoreCase("no")) {

                    AfterHour = "no";
                    AfterHourNoBt.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                    AfterHourNoBt.setTextColor(Color.parseColor("#ffffff"));
                    AfterHourYesBt.setBackgroundResource(R.drawable.border_black_solid_white);
                    AfterHourYesBt.setTextColor(Color.parseColor("#000000"));

                }

                if (result.getWork_location().equalsIgnoreCase("residential")) {

                    ServiceCategory = "residential";
                    ResidentialBT.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                    ResidentialBT.setTextColor(Color.parseColor("#ffffff"));

                    CommercialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                    CommercialBT.setTextColor(Color.parseColor("#000000"));
                    BothBT.setBackgroundResource(R.drawable.border_black_solid_white);
                    BothBT.setTextColor(Color.parseColor("#000000"));


                } else if (result.getWork_location().equalsIgnoreCase("commercial")) {

                    ServiceCategory = "commercial";
                    CommercialBT.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                    CommercialBT.setTextColor(Color.parseColor("#ffffff"));

                    ResidentialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                    ResidentialBT.setTextColor(Color.parseColor("#000000"));
                    BothBT.setBackgroundResource(R.drawable.border_black_solid_white);
                    BothBT.setTextColor(Color.parseColor("#000000"));


                } else if (result.getWork_location().equalsIgnoreCase("both")) {

                    ServiceCategory = "both";

                    BothBT.setBackgroundResource(R.drawable.border_black_solid_skyblue);
                    BothBT.setTextColor(Color.parseColor("#ffffff"));
                    ResidentialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                    ResidentialBT.setTextColor(Color.parseColor("#000000"));
                    CommercialBT.setBackgroundResource(R.drawable.border_black_solid_white);
                    CommercialBT.setTextColor(Color.parseColor("#000000"));

                }
            }
        }
    }

    private void tradmenprofile(String tradesman_id) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(EditCallout.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().tradmentrating(tradesman_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    try {

                        String responedata = response.body().string();
                        JSONArray arr = new JSONArray(responedata);

                        if (arr.length() > 0) {

                            JSONObject object = arr.getJSONObject(0);

                            if (object.getString("result").equals("successfully")) {

                                str_value = object.getString("hour_min");

                                Toast.makeText(EditCallout.this, str_value, Toast.LENGTH_SHORT).show();

                                Log.e("str_value", str_value);

                                tv_value_chage.setText("$  " + str_value);

                                try {

                                    seekBar_book.setProgress(Integer.parseInt(str_value));
                                } catch (NumberFormatException ex) { // handle your exception
                                    ex.printStackTrace();
                                }

                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    //Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(EditCallout.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
