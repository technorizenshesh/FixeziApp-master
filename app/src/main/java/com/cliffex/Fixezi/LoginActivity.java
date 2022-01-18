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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.UserDetail;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.Other.AppConfig;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;

import org.ankit.perfectdialog.EasyDialog;
import org.ankit.perfectdialog.EasyDialogListener;
import org.ankit.perfectdialog.Icon;
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

public class LoginActivity extends AppCompatActivity {

    private final int PREFERENCE_PRIVATE_MODE = 0;
    TextView toolbar_title, forgot_password, remembertxt;
    EditText usernameedit, passworddit;
    RelativeLayout loginbtn;
    TextView logiinnnbtntxt;
    String Suser = "", Spass = "", result = "", forgetemailstring = "";
    ProgressBar loginprogress;

    SharedPreferences LoginPref;
    SharedPreferences.Editor LoginPrefEditor;

    SharedPreferences RememberMePref;
    SharedPreferences.Editor RememberMePrefEditor;
    SessionUser sessionUser;
    CheckBox remembercheckbox;

    GoogleCloudMessaging gcm;
    String RegId = "";
    RelativeLayout NavigationUpIM;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginPref = getSharedPreferences("login", PREFERENCE_PRIVATE_MODE);
        LoginPrefEditor = LoginPref.edit();
        RememberMePref = getApplicationContext().getSharedPreferences("RememberMe", PREFERENCE_PRIVATE_MODE);
        RememberMePrefEditor = RememberMePref.edit();

        setContentView(R.layout.activity_login);
        sessionUser = new SessionUser(this);

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

        initComp();
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar_title.setText("Login as User");

        registerBackground();

        if (RememberMePref.getBoolean("IsRememberMeChecked", false)) {

            String username = RememberMePref.getString("Username", "");
            String password = RememberMePref.getString("Password", "");

            if (username.equalsIgnoreCase("")) {

            } else {
                usernameedit.setText(username);
                passworddit.setText(password);
                remembercheckbox.setChecked(true);
            }
        }

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(LoginActivity.this);
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
                            errorToast("Please Enter Email Id");
                        } else if (!Appconstants.isValidEmail(forgetemailstring)) {
                            isError = true;
                            errorToast("Invalid Email ID");
                        }

                        if (!isError) {
                            if (InternetDetect.isConnected(getApplicationContext())) {
                                forgetEmail(forgetemailstring);
                                d.dismiss();
                            } else {
                                errorToast(" Please Check Internet Connection");
                            }
                        }
                    }
                });
                d.show();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isError = false;
                Suser = usernameedit.getText().toString();
                Spass = passworddit.getText().toString();

                Log.e("Suser!", Suser);
                Log.e("Spass!", Spass);

                if (Suser.equals("") && Spass.equals("")) {
                    isError = true;
                    errorToast("Please enter usename and password");
                } else if (Suser.equals("")) {
                    isError = true;
                    errorToast("Please enter username");
                } else if (Spass.equals("")) {
                    isError = true;
                    errorToast("Please enter password");
                }

                if (!isError) {
                    if (InternetDetect.isConnected(getApplicationContext())) {

                        if (RegId.equalsIgnoreCase("")) {
                            registerBackground();
                        } else {
                            new JsonLogin().execute(Suser, Spass, RegId);
                            Log.e("RegId>>", RegId);
                        }

                    } else {

                        errorToast("Check Internet Connection");
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initComp() {

        remembercheckbox = (CheckBox) findViewById(R.id.remembercheckbox);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        remembertxt = (TextView) findViewById(R.id.remembertxt);
        loginprogress = (ProgressBar) findViewById(R.id.loginprogress);
        toolbar = (Toolbar) findViewById(R.id.m_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        logiinnnbtntxt = (TextView) findViewById(R.id.logiinnnbtntxt);
        usernameedit = (EditText) findViewById(R.id.usernameedit);
        passworddit = (EditText) findViewById(R.id.passworddit);
        loginbtn = (RelativeLayout) findViewById(R.id.loginbtn);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
    }

    private void errorToast(String x) {

        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }

    private void forgetEmail(String email) {

        Call<ResponseBody> call = AppConfig.loadInterface().forgetuser(email);

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

                            new EasyDialog.Builder(LoginActivity.this)
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

                                    .build();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sorry Email Id is not Registered", Toast.LENGTH_SHORT).show();

                        }

                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    RegId = gcm.register("741851403308");
                    //Log.e("REGGGG", ">>>>" + RegId);
                } catch (IOException ex) {

                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            protected void onPostExecute(String msg) {

            }

        }.execute(null, null, null);
    }

  /*  private class ForgetJSon extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "forgot_password_ios&email=" + forgetemailstring);
            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                JSONObject job = new JSONObject(obj);
                result = job.getString("result");
     Log.e("FORGET : ", result);

            } catch (Exception e) {
                System.out.println("errror in Forget task " + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (result.equalsIgnoreCase("successfully")) {


                new EasyDialog.Builder(LoginActivity.this)
                        .setTitle("fixezi")
                        .setSubtitle("Sent On Your Email")
                        .isCancellable(true)
                        .setCancelBtnColor("#000000")
                        .setConfirmBtnColor("#fd3621")
                        .setIcon(Icon.SUCCESS)
                        .setConfirmBtn("yes", new EasyDialogListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        })
                        .setCancelBtn("no", new EasyDialogListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .build();


               *//* final Dialog dialog=new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.dialog_sendemail);
                dialog.setCancelable(false);
                Button btn_ok= (Button) dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();*//*

                errorToast("Message has been sent to your Email");
            } else if (result.equalsIgnoreCase("unsuccessfully")) {
                errorToast(" Sorry Email Id is not Registered");
            }
        }
    }*/

    private class JsonLogin extends AsyncTask<String, String, String> {

        String id, name, houseno, street, postcode, city, state, homephone, workphone, mobilephone, email, username, result;
        UserDetail userDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "login&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("username", strings[0]);
                params.put("password", strings[1]);
                params.put("registration_id", strings[2]);

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
                Log.e("JsonLogin", response);

                JSONArray ParentArray = new JSONArray(response);
                JSONObject FinalObject = ParentArray.getJSONObject(0);
                result = FinalObject.getString("result");

                userDetail = new UserDetail();

                userDetail.setId(FinalObject.getString("id"));
                userDetail.setName(FinalObject.getString("name"));
                userDetail.setFirst_name(FinalObject.getString("first_name"));
                userDetail.setLast_name(FinalObject.getString("last_name"));
                userDetail.setPostcode(FinalObject.getString("post_code"));
                userDetail.setCity(FinalObject.getString("city"));
                userDetail.setState(FinalObject.getString("state"));
                userDetail.setHomephone(FinalObject.getString("home_phone"));
                userDetail.setWorkphone(FinalObject.getString("work_phone"));
                userDetail.setMobilephone(FinalObject.getString("mobile_phone"));
                userDetail.setEmail(FinalObject.getString("email"));
                userDetail.setUsername(FinalObject.getString("username"));
                userDetail.setHomeAddress(FinalObject.getString("home_address"));
                userDetail.setUser_lat(FinalObject.getString("user_lat"));
                userDetail.setUser_lon(FinalObject.getString("user_lon"));

                Log.e("userDetailuserDetail", "userDetail lat = " + userDetail.getUser_lat());
                Log.e("userDetailuserDetail", "userDetail lon = " + userDetail.getUser_lon());

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

            loginprogress.setVisibility(View.GONE);

            if (result == null) {
                LoginErrorDialog();
            } else if (result.equalsIgnoreCase("successfully")) {

                if (remembercheckbox.isChecked()) {

                    Log.e("IFCHECKBOX", "yes");
                    Log.e("Suser", "?" + Suser);
                    Log.e("Suser", "?" + Spass);

                    RememberMePrefEditor.putString("Username", Suser);
                    RememberMePrefEditor.putString("Password", Spass);
                    RememberMePrefEditor.putBoolean("IsRememberMeChecked", true);
                    RememberMePrefEditor.commit();
                } else {
                    Log.e("ELSECHECKBOX", "yes");
                    RememberMePrefEditor.putBoolean("IsRememberMeChecked", false);
                    RememberMePrefEditor.commit();
                }

                sessionUser.createLoginSession(userDetail.getUser_lat(), userDetail.getUser_lon(), userDetail.getId(), userDetail.getName(), userDetail.getFirst_name(), userDetail.getLast_name(), userDetail.getHomeAddress(), userDetail.getPostcode(), userDetail.getCity(), userDetail.getState(), userDetail.getHomephone(), userDetail.getWorkphone(), userDetail.getMobilephone(), userDetail.getEmail(), userDetail.getUsername());

                Intent i = new Intent(getApplicationContext(), UserActivity.class);
                i.putExtra("status", "login");
                startActivity(i);
                LoginActivity.this.finish();
                errorToast("Logged in Successfully");

            } /* else if (result.equalsIgnoreCase("Deactive")) {

                final Dialog VerificationDialog = new Dialog(LoginActivity.this);
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

                //DialogTV.setText("A verification code has been sent to your email (" + detailBean.getEmail() + "). Please check your Spam folder just in case the confirmation email got delivered there instead of your inbox. Please enter the code below");
                DialogTV.setText("Your account is not activated. Please activate your account by clicking on activation link sent on your registered email id.");

                SendAgainTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (InternetDetect.isConnected(LoginActivity.this)) {

                            Log.e("USER ID", userDetail.getId());
                            new JsonSendAgain().execute(userDetail.getId(), userDetail.getEmail());
                            VerificationDialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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

            }*/ else if (result.equalsIgnoreCase("unsuccessfully")) {
                Toast.makeText(LoginActivity.this, "Wrong Username and Password", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void LoginErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private class JsonSendAgain extends AsyncTask<String, String, String> {

        String Email = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "resend_mail_user&");
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

                Toast.makeText(LoginActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();

            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(LoginActivity.this, "An activation link has been sent to your registered email id", Toast.LENGTH_SHORT);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
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

            loginprogress.setVisibility(View.GONE);
        }
    }
}
