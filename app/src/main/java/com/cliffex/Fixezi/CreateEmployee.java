package com.cliffex.Fixezi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.Model.WorkerBean;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateEmployee extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    EditText UsernameET, PasswordET, ConfirmPasswordET, NameET;
    TextView CreateUserTV, CreateEmpMsgTV;
    TextView PurchaseIdTV;
    SessionTradesman sessionTradesman;

    boolean value = false;
    ProgressBar CreateEmployeePB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);
        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Create Employee Id");
        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        NameET = (EditText) findViewById(R.id.NameET);
        UsernameET = (EditText) findViewById(R.id.UsernameET);
        PasswordET = (EditText) findViewById(R.id.PasswordET);
        ConfirmPasswordET = (EditText) findViewById(R.id.ConfirmPasswordET);
        CreateUserTV = (TextView) findViewById(R.id.CreateUserTV);
        CreateEmpMsgTV = (TextView) findViewById(R.id.CreateEmpMsgTV);
        PurchaseIdTV = (TextView) findViewById(R.id.PurchaseIdTV);
        CreateEmployeePB = (ProgressBar) findViewById(R.id.CreateEmployeePB);

        String text = "<font color='#1E90FF'>We give you 3 Employee Id's to start with,</font> If you need more Id's you can purchase them in the box below.";
        CreateEmpMsgTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        PurchaseIdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateEmployee.this, PurchaseEmployeeId.class);
                startActivity(intent);
            }
        });

        CreateUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (value) {
                    if (Validation()) {

                        if (InternetDetect.isConnected(CreateEmployee.this)) {

                            new JsonCreateUser().execute(sessionTradesman.getId(), NameET.getText().toString(), UsernameET.getText().toString(), PasswordET.getText().toString());

                        } else {

                            Toast.makeText(CreateEmployee.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {

                    Toast.makeText(CreateEmployee.this, "Please Purchase Employee id", Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateEmployee.this);
                    builder1.setMessage("You have reached free Employee Id Limit. If you want to purchase more Employee Ids please view our plans");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "View Plans",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = new Intent(CreateEmployee.this, PurchaseEmployeeId.class);
                                    startActivity(intent);
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                 ;
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }


            }

            private boolean Validation() {

                if (NameET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(CreateEmployee.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (UsernameET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(CreateEmployee.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (PasswordET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(CreateEmployee.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ConfirmPasswordET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(CreateEmployee.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(ConfirmPasswordET.getText().toString().equals(PasswordET.getText().toString()))) {

                    Toast.makeText(CreateEmployee.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });


    }

    private class JsonCreateUser extends AsyncTask<String, String, String> {

        String Error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CreateEmployeePB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "worker_user_sign_up&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", paramss[0]);
                params.put("name", paramss[1]);
                params.put("username", paramss[2]);
                params.put("password", paramss[3]);

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

                Log.e("JsonCreateUser", response);

                //[{"id":"2","name":"gaurav rathore","username":"gaurav1","password":"MTIzNDU2Nzg=","tradesman_id":"1","profile_pic":"http:\/\/www.technorizen.co.in\/WORKSPACE6\/FIXEZI\/image\/","result":"successfully"}]

                JSONArray ParentArray = new JSONArray(response);

                JSONObject Object = ParentArray.getJSONObject(0);
                String result = Object.getString("result");
                if (result.equalsIgnoreCase("unsuccessfully")) {

                    Error = Object.getString("error");
                }
                return result;


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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(CreateEmployee.this, "New User Created", Toast.LENGTH_SHORT).show();
                finish();
            } else if (result.equalsIgnoreCase("unsuccessfully")) {

                Toast.makeText(CreateEmployee.this, "Error : " + Error, Toast.LENGTH_SHORT).show();
            }
            CreateEmployeePB.setVisibility(View.GONE);
        }
    }


    private class JsonGetInfo extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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


            if (jsonresult.equalsIgnoreCase("successfully")) {

                if (InternetDetect.isConnected(CreateEmployee.this)) {

                    Log.e("Employee Count", ">>>>" + result.getEmployee_count());
                    new JsonGetWorker().execute(Integer.parseInt(result.getEmployee_count()));
                } else {

                    Toast.makeText(CreateEmployee.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private class JsonGetWorker extends AsyncTask<Integer, String, List<WorkerBean>> {

        int idsPermitted = 3;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CreateEmployeePB.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<WorkerBean> doInBackground(Integer... integers) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "worker_user_list&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", sessionTradesman.getId());
                idsPermitted = integers[0];

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
                Log.e("JsonGetWorkerList", response);

                List<WorkerBean> workerBeanList = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    WorkerBean workerBean = new WorkerBean();

                    workerBean.setId(object.getString("id"));
                    workerBean.setName(object.getString("name"));
                    workerBean.setUsername(object.getString("username"));
                    workerBean.setPassword(object.getString("password"));

                    workerBeanList.add(workerBean);
                }

                return workerBeanList;


            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<WorkerBean> result) {
            super.onPostExecute(result);
            CreateEmployeePB.setVisibility(View.GONE);
            if (result == null) {
                value = true;
            } else if (result.isEmpty()) {

                value = true;
            } else {


                if (result.size() >= idsPermitted) {

                    value = false;
                } else {

                    value = true;
                }

            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (InternetDetect.isConnected(this)) {

            new JsonGetInfo().execute();
        } else {

            Toast.makeText(CreateEmployee.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }
}
/*

20.10
40.20
60.40*/
