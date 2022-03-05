package com.cliffex.Fixezi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.HttpPAth;

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

public class ChangePassword extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    TextView ChangePasswordTV;
    EditText PasswordETCP, ConfirmPasswordETCP;
    String workerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        toolbar = (Toolbar) findViewById(R.id.ToolbarCP);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Change Password");
        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ChangePasswordTV = (TextView) findViewById(R.id.ChangePasswordTV);
        PasswordETCP = (EditText) findViewById(R.id.PasswordETCP);
        ConfirmPasswordETCP = (EditText) findViewById(R.id.ConfirmPasswordETCP);

        Bundle bundle = getIntent().getExtras();
        workerId = bundle.getString("id");

        ChangePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {

                    if (InternetDetect.isConnected(ChangePassword.this)) {

                        new JsonChangePassword().execute(workerId, PasswordETCP.getText().toString());
                    } else {

                        Toast.makeText(ChangePassword.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean Validation() {

                if (PasswordETCP.getText().toString().equals("")) {

                    Toast.makeText(ChangePassword.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ConfirmPasswordETCP.getText().toString().equals("")) {

                    Toast.makeText(ChangePassword.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (!(PasswordETCP.getText().toString().equals(ConfirmPasswordETCP.getText().toString()))) {

                    Toast.makeText(ChangePassword.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }
        });
    }

    private class JsonChangePassword extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... paramss) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "update_worker_password&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("worker_id", paramss[0]);
                params.put("password", paramss[1]);

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
                Log.e("JsonChangePassord", response);


                JSONObject object = new JSONObject(response);
                String result = object.getString("result");


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
                Toast.makeText(ChangePassword.this, "Server Error", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(ChangePassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                finish();

            }

        }
    }
}
