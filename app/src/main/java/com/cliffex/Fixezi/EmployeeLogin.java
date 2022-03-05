package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
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

public class EmployeeLogin extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    EditText WorkerUsernameET,WorkerPasswordET;
    TextView LoginWorkerTV;
    SessionWorker sessionWorker;
    SharedPreferences RememberMeWorkerPref;
    CheckBox WorkerRememberCheckbox;
    SharedPreferences.Editor RememberMeWorkerPrefEditor;
    GoogleCloudMessaging gcm;
    String RegId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                RegId = token;
                Log.e("tokentoken", "retrieve token successful : " + RegId);
            } else {
                Log.e("tokentoken", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
        }).addOnCanceledListener(() -> {
        }).addOnCompleteListener(task -> Log.e("tokentoken", "This is the token : " + task.getResult()));

        Appconstants.AdminType = "Worker";

        RememberMeWorkerPref = getApplicationContext().getSharedPreferences("RememberMeWorker", 0);
        RememberMeWorkerPrefEditor = RememberMeWorkerPref.edit();

        sessionWorker = new SessionWorker(this);

        toolbar = (Toolbar) findViewById(R.id.ToolbarWorker);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        WorkerUsernameET = (EditText) findViewById(R.id.WorkerUsernameET);
        WorkerPasswordET = (EditText) findViewById(R.id.WorkerPasswordET);
        LoginWorkerTV = (TextView) findViewById(R.id.LoginWorkerTV);
        WorkerRememberCheckbox = (CheckBox) findViewById(R.id.WorkerRememberCheckbox);

        setSupportActionBar(toolbar);
        toolbar_title.setText("Login as Employee");

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LoginWorkerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    if (InternetDetect.isConnected(EmployeeLogin.this)) {
                        new JsonWorkerLogin().execute(WorkerUsernameET.getText().toString(), WorkerPasswordET.getText().toString());
                    } else {
                        Toast.makeText(EmployeeLogin.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean Validation() {
                if (WorkerUsernameET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EmployeeLogin.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (WorkerPasswordET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EmployeeLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

        if (RememberMeWorkerPref.getBoolean("IsRememberMeChecked", false)) {

            String username = RememberMeWorkerPref.getString("Username", "");
            String password = RememberMeWorkerPref.getString("Password", "");

            if (username.equalsIgnoreCase("")) {
            } else {
                WorkerUsernameET.setText(username);
                WorkerPasswordET.setText(password);
                WorkerRememberCheckbox.setChecked(true);
            }
        }

        if (InternetDetect.isConnected(this)) {
            new JsonGCMTask().execute();
        } else {
            Toast.makeText(EmployeeLogin.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class JsonWorkerLogin extends AsyncTask<String, String, String> {

        String name = "", username = "", trademanID = "", id = "";
        String usernameWorker = "", passwordWorker = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(HttpPAth.Urlpath + "worker_login");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("username", strings[0]);
                params.put("password", strings[1]);
                params.put("device_token", RegId);

                usernameWorker = strings[0];
                passwordWorker = strings[1];

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
                Log.e("JsonLoginWorker", response);

                Gson gson = new Gson();
                JSONArray ParentArray = new JSONArray(response);
                JSONObject Object = ParentArray.getJSONObject(0);
                String result = Object.getString("result");
                if (result.equalsIgnoreCase("successfully")) {

                    id = Object.getString("id");
                    name = Object.getString("name");
                    username = Object.getString("username");
                    trademanID = Object.getString("tradesman_id");
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
                LoginErrorDialog();
            } else if (result.equalsIgnoreCase("successfully")) {

                if (WorkerRememberCheckbox.isChecked()) {
                    RememberMeWorkerPrefEditor.putString("Username", usernameWorker);
                    RememberMeWorkerPrefEditor.putString("Password", passwordWorker);
                    RememberMeWorkerPrefEditor.putBoolean("IsRememberMeChecked", true);
                    RememberMeWorkerPrefEditor.commit();
                } else {
                    RememberMeWorkerPrefEditor.putBoolean("IsRememberMeChecked", false);
                    RememberMeWorkerPrefEditor.commit();
                }

                sessionWorker.createLoginSession(id, name, username, trademanID);
                Intent intent = new Intent(EmployeeLogin.this, EmployeeActivity.class);
                startActivity(intent);
                finish();

            } else if (result.equalsIgnoreCase("unsuccessfully")) {
                LoginErrorDialog();
                // Toast.makeText(EmployeeLogin.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void LoginErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeLogin.this);
        builder.setCancelable(false)
                .setTitle("Fixezi")
                .setMessage("Your Email or Password Not Exist")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private class JsonGCMTask extends AsyncTask<String, String, String> {
        String msg = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(EmployeeLogin.this);
                }
                RegId = gcm.register("741851403308");
                Log.e("RegId ", ">>>" + RegId);
                return RegId;

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

                new JsonGCMTask().execute();

            } else {

            }
        }
    }
}
