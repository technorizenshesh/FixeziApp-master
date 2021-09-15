package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.Other.AppConfig;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.ankit.perfectdialog.EasyDialog;
import org.ankit.perfectdialog.EasyDialogListener;
import org.ankit.perfectdialog.Icon;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

import static com.cliffex.Fixezi.Other.MySharedPref.saveData;

public class TradesmanLogin extends AppCompatActivity {

    private static final int PREFERENCE_PRIVATE_MODE = 0;
    Toolbar Trade_log_tool;
    TextView toolbar_title, forgot_password_tradesman;
    CheckBox Tradesmanremembercheckbox;
    EditText TradesmanUsernameLogin, TradesmanPasswordLogin;
    TextView Tradesmanloginbtn;
    SessionTradesman sessionTradesman;
    SharedPreferences RememberMeTradesmanPref;
    SharedPreferences.Editor RememberMeTradesmanPrefEditor;
    String forgetemailstring = "";
    String PasswordTradesman, UsernameTradesman;
    GoogleCloudMessaging gcm;
    String RegId = "";
    RelativeLayout NavigationUpIM;
    ProgressBar TrademanLoginPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman_login);
        sessionTradesman = new SessionTradesman(this);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener
                (token -> {
            if (!TextUtils.isEmpty(token)) {
                RegId = token;
                Log.e("tokentoken", "retrieve token successful : " + RegId);
            } else {
                Log.e("tokentoken", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
        }).addOnCanceledListener(() -> {
        }).addOnCompleteListener(task -> Log.e("tokentoken", "This is the token : " + task.getResult()));

        RememberMeTradesmanPref = getApplicationContext().getSharedPreferences("RememberMeTradesman", PREFERENCE_PRIVATE_MODE);
        RememberMeTradesmanPrefEditor = RememberMeTradesmanPref.edit();
        Trade_log_tool = (Toolbar) findViewById(R.id.Trade_log_tool);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        TradesmanUsernameLogin = (EditText) findViewById(R.id.TradesmanUsernameLogin);
        TradesmanPasswordLogin = (EditText) findViewById(R.id.TradesmanPasswordLogin);
        Tradesmanremembercheckbox = (CheckBox) findViewById(R.id.Tradesmanremembercheckbox);
        Tradesmanloginbtn = (TextView) findViewById(R.id.Tradesmanloginbtn);
        forgot_password_tradesman = (TextView) findViewById(R.id.forgot_password_tradesman);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        TrademanLoginPB = (ProgressBar) findViewById(R.id.TrademanLoginPB);

        setSupportActionBar(Trade_log_tool);
        toolbar_title.setText("Login as Admin");

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (InternetDetect.isConnected(this)) {
            new JsonGCMTask().execute();
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        forgot_password_tradesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(TradesmanLogin.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.forget_pass_dialog);

                final EditText forgetemailtext = (EditText) d.findViewById(R.id.forgetemailtext);
                Button forgetsubmitbtn = (Button) d.findViewById(R.id.forgetsubmitbtn);

                forgetsubmitbtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        boolean isError = false;

                        forgetemailstring = forgetemailtext.getText().toString();
                        if (forgetemailstring.equals("")) {
                            isError = true;
                            Toast.makeText(TradesmanLogin.this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                        } else if (!Appconstants.isValidEmail(forgetemailstring)) {
                            isError = true;
                            Toast.makeText(TradesmanLogin.this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                        }

                        if (!isError) {
                            if (InternetDetect.isConnected(getApplicationContext())) {
                                forgetEmail(forgetemailstring);
                                d.dismiss();
                            } else {
                                Toast.makeText(TradesmanLogin.this, "Please Check Internet Connection", Toast.LENGTH_SHORT);

                            }
                        }

                    }
                });
                d.show();


            }
        });


        if (RememberMeTradesmanPref.getBoolean("IsRememberMeChecked", false)) {

            String username = RememberMeTradesmanPref.getString("Username", "");
            String password = RememberMeTradesmanPref.getString("Password", "");

            if (username.equalsIgnoreCase("")) {

            } else {
                TradesmanUsernameLogin.setText(username);
                TradesmanPasswordLogin.setText(password);
                Tradesmanremembercheckbox.setChecked(true);
            }
        }

        Tradesmanloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidSuccess()) {
                    UsernameTradesman = TradesmanUsernameLogin.getText().toString();
                    PasswordTradesman = TradesmanPasswordLogin.getText().toString();
                    if (RegId.equalsIgnoreCase("")) {
                        new JsonGCMTask().execute();
                    } else {
                        Log.e("onsuccess", "YES");
                        Log.e("Regid", RegId);
                        new JsonTradesmanLogin().execute(UsernameTradesman, PasswordTradesman, RegId);
                    }
                }


            }


            private boolean ValidSuccess() {

                if (TradesmanUsernameLogin.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanLogin.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TradesmanPasswordLogin.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanLogin.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }

        });
    }

    private void forgetEmail(String email) {

        Call<ResponseBody> call = AppConfig.loadInterface().forgettrademan(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray array = new JSONArray(responseData);
                        JSONObject object = (JSONObject) array.get(0);
                        String message = object.getString("result");
                        System.out.println("Forgetemail" + object);
                        if (message.equals("successfully")) {
                            //Toast.makeText(LoginActivity.this, "Data Send", Toast.LENGTH_SHORT).show();
                            new EasyDialog.Builder(TradesmanLogin.this)
                                    .setTitle("fixezi")
                                    .setSubtitle("Sent On Your Email")
                                    .isCancellable(true)
                                    .setCancelBtnColor("#000000")
                                    .setConfirmBtnColor("#fd3621")
                                    .setIcon(Icon.SUCCESS)
                                    .setConfirmBtnColor("#51d11a")
                                    .setConfirmBtn("OK", new EasyDialogListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    /* .setCancelBtn("no", new EasyDialogListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {

                                         }
                                     })*/
                                    .build();
                        } else {
                            Toast.makeText(TradesmanLogin.this, "Sorry Email Id is not Registered", Toast.LENGTH_SHORT).show();

                        }

                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(TradesmanLogin.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class JsonTradesmanLogin extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TrademanLoginPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected TradesManBean doInBackground(String... paramss) {

            try {

                URL url = new URL(HttpPAth.Urlpath + "tradesman_login");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("username_email", paramss[0]);
                params.put("password", paramss[1]);
                params.put("registration_id", paramss[2]);

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
                Log.e("JsonLoginTradesman", response);

                Gson gson = new Gson();
                JSONArray ParentArray = new JSONArray(response);

                JSONObject Object = ParentArray.getJSONObject(0);

                TradesManBean tradesManBean = gson.fromJson(Object.toString(), TradesManBean.class);
                String str_tradmanID = Object.getString("id");

                saveData(getApplicationContext(), "tradmen_id", str_tradmanID);
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
        protected void onPostExecute(final TradesManBean result) {
            super.onPostExecute(result);

            Log.e("kjgkjsgdfkjgdkjsf", "result = " + new Gson().toJson(result));

            TrademanLoginPB.setVisibility(View.GONE);

            if (result == null) {
                LoginErrorDialog();
            }

            if (jsonresult.equalsIgnoreCase("successfully")) {

                if (Tradesmanremembercheckbox.isChecked()) {
                    RememberMeTradesmanPrefEditor.putString("Username", UsernameTradesman);
                    RememberMeTradesmanPrefEditor.putString("Password", PasswordTradesman);
                    RememberMeTradesmanPrefEditor.putBoolean("IsRememberMeChecked", true);
                    RememberMeTradesmanPrefEditor.commit();
                } else {
                    RememberMeTradesmanPrefEditor.putBoolean("IsRememberMeChecked", false);
                    RememberMeTradesmanPrefEditor.commit();
                }

                sessionTradesman.createLoginSession(result.getId(), result.getBusiness_name(), result.getTrading_name(), result.getOffice_no(), result.getMobile_no(), result.getEmail(), result.getWebsite_url(), result.getProfile_pic(), result.getBzy_status());

                Intent intent = new Intent(TradesmanLogin.this, TradesmanActivity.class);
                intent.putExtra("AfterHour", "AfterHour");
                startActivity(intent);
                finish();

            } else if (jsonresult.equalsIgnoreCase("Deactive")) {

                final Dialog VerificationDialog = new Dialog(TradesmanLogin.this);
                VerificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                VerificationDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                VerificationDialog.setContentView(R.layout.verification_dialog);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = VerificationDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                TextView DialogTV = (TextView) VerificationDialog.findViewById(R.id.DialogTV);
                TextView SendAgainTV = (TextView) VerificationDialog.findViewById(R.id.SendAgainTV);
                TextView OkayTV = (TextView) VerificationDialog.findViewById(R.id.OkayTV);

                // DialogTV.setText("A verification code has been sent to your email (" + detailBean.getEmail() + "). Please check your Spam folder just in case the confirmation email got delivered there instead of your inbox. Please enter the code below");
                DialogTV.setText("Your account is not activated. Please activate your account by clicking on activation link sent on your registered email id.");

                SendAgainTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (InternetDetect.isConnected(TradesmanLogin.this)) {

                            Toast.makeText(TradesmanLogin.this, result.getId(), Toast.LENGTH_SHORT).show();

                            Log.e("USER ID", result.getId());

                            new JsonSendAgain().execute(result.getId(), result.getEmail());

                            VerificationDialog.dismiss();

                        } else {
                            Toast.makeText(TradesmanLogin.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                OkayTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        VerificationDialog.dismiss();

                    }
                });


                VerificationDialog.show();


            } else if (jsonresult.equalsIgnoreCase("unsuccessfully")) {

                Toast.makeText(TradesmanLogin.this, "Wrong Username and Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void LoginErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TradesmanLogin.this);
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

    private class ForgetJSon extends AsyncTask<String, Void, String> {

        String result;

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "forgot_password_tradesman_ios&email=" + forgetemailstring);
            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                JSONObject job = new JSONObject(obj);
                result = job.getString("result");


            } catch (Exception e) {
                System.out.println("errror in Forget task " + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (result.equalsIgnoreCase("successfully")) {


                final Dialog dialog = new Dialog(TradesmanLogin.this);
                dialog.setContentView(R.layout.dialog_sendemail);
                dialog.setCancelable(false);
                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                // Toast.makeText(TradesmanLogin.this, "Message has been sent to your Email", Toast.LENGTH_SHORT).show();

            } else if (result.equalsIgnoreCase("unsuccessfully")) {
                Toast.makeText(TradesmanLogin.this, " Sorry Email Id is not Registered", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class JsonGCMTask extends AsyncTask<String, String, String> {

        String msg = "";

        @Override
        protected String doInBackground(String... strings) {

            try {

                if (gcm == null) {

                    gcm = GoogleCloudMessaging.getInstance(TradesmanLogin.this);
                }

                RegId = gcm.register("741851403308");

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

    private class JsonSendAgain extends AsyncTask<String, String, String> {

        String Email = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TrademanLoginPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "resend_mail_tradesman&");

                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", strings[0]);

                Email = strings[1];
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
                Log.e("JsonSendAgain", response);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject MainObject = jsonArray.getJSONObject(0);
                String result = MainObject.getString("result");

                return result;

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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(TradesmanLogin.this, "Server Problem", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(TradesmanLogin.this, "An activation link has been sent to your registered email id", Toast.LENGTH_SHORT);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(TradesmanLogin.this);
                builder1.setTitle("fixezi App Team");
                builder1.setIcon(R.drawable.mainlogo);
                builder1.setMessage("A activation link has been sent to your registered email id (" + Email + "). Please click on the Activation Link to Activate your account.");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } else if (result.equalsIgnoreCase("unsuccessfully")) {

            }

            TrademanLoginPB.setVisibility(View.GONE);
        }
    }

}
