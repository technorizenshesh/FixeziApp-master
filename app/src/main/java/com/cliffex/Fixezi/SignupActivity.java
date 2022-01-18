package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.cliffex.Fixezi.Other.MySharedPref.getData;

public class SignupActivity extends AppCompatActivity {

    TextView toolbar_title, VerfiyTV;
    Context context = SignupActivity.this;
    RelativeLayout NavigationUpIM;
    View v;
    String statelist[] = {"WA"};
    String citylist[] = {"Perth"};
    EditText namesignup, housenoo, homephone;
    EditText mobno, emailadd, usernameeee, passs, confirmpasss, ConfirmEmailET;
    Button nobtn, yesbtn, registerbtn;
    String trademen = "";
    int a = 1;
    String OTP = "", mobilestatus = "";
    CheckBox signup2checkbox, signup2checkbox1;
    GoogleCloudMessaging gcm;
    String RegId = "";
    EditText LnameET, FnameET;
    TextView SignUpMsgTV;
    private Toolbar signup_toolbar;
    private String status;
    private String text;
    private RelativeLayout re_yesno;
    private TextView terms_and;
    private String Address_Save;
    private String FName;
    private String LName;
    private String HouseNo;
    private String HomePhoneNo;
    private String MobileNo;
    private String Email;
    private String Confirm_Passwords;
    private String Usernames, Passwords, confirmpass,
            MobileNoNos, HouseNos, Emails, LNames,
            FNames, Password, Username;
    private String status1;
    private String text1;
    private Spinner spinner;
    private Object item;
    private String MobilePhoneSU_string, otp_status, code_select_string, code_select, select_status, ConfirmEmailETs, HomePhoneNos, Confirm_Emails;
    private ImageView verify_otpimg;
    private String latUser, lonUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        verify_otpimg = (ImageView) findViewById(R.id.verify_otpimg);

        ArrayAdapter<String> countryName = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DataCode.countryAreaCodes);

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

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(countryName);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item = parent.getItemAtPosition(pos);
                code_select = String.valueOf(item);
                String[] str = code_select.split("\\+");
                code_select_string = str[1];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                status = "";
            } else {
                status = extras.getString("status");
                select_status = extras.getString("select_status");
            }

        } else {
            status = (String) savedInstanceState.getSerializable("status");
        }

        initComp();

        try {
            if (select_status.equals("quick_search")) {
                re_yesno.setVisibility(View.GONE);
                text = "<font color='#1E90FF'> JUST BEFORE YOU BOOK A TRADESMAN,</font> WE NEED A FEW EXTRA DETAILS FROM YOU ";
                housenoo.setText("");
                homephone.setText("");
                confirmpasss.setText("");
                passs.setText("");
                usernameeee.setText("");
            } else {
                text = "<font color='#1E90FF'> IF YOU ARE A TRADESMAN,</font> WOULD YOU LIKE TO JOIN FIXEZI , AS A TRADESMAN?";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SignUpMsgTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            mobilestatus = getData(getApplicationContext(), "mobilestatus", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FnameET.addTextChangedListener(new TextWatcher() {

            int mStart = 0;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mStart = arg1 + arg3;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable et) {

                String input = et.toString();
                String capitalizedText;

                if (input.length() < 1)
                    capitalizedText = input;
                else if (input.length() > 1 && input.contains(" ")) {
                    String fstr = input.substring(0, input.lastIndexOf(" ") + 1);
                    if (fstr.length() == input.length()) {
                        capitalizedText = fstr;
                    } else {
                        String sstr = input.substring(input.lastIndexOf(" ") + 1);
                        sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1);
                        capitalizedText = fstr + sstr;
                    }
                } else
                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);

                if (!capitalizedText.equals(FnameET.getText().toString())) {

                    FnameET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            FnameET.setSelection(mStart);
                            FnameET.removeTextChangedListener(this);
                        }

                    });

                    FnameET.setText(capitalizedText);

                }

            }

        });

        LnameET.addTextChangedListener(new TextWatcher() {

            int mStart = 0;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mStart = arg1 + arg3;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable et) {

                String input = et.toString();
                String capitalizedText;
                if (input.length() < 1) capitalizedText = input;
                else if (input.length() > 1 && input.contains(" ")) {
                    String fstr = input.substring(0, input.lastIndexOf(" ") + 1);
                    if (fstr.length() == input.length()) {
                        capitalizedText = fstr;
                    } else {
                        String sstr = input.substring(input.lastIndexOf(" ") + 1);
                        sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1);
                        capitalizedText = fstr + sstr;
                    }
                } else

                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);

                if (!capitalizedText.equals(LnameET.getText().toString())) {

                    LnameET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            LnameET.setSelection(mStart);
                            LnameET.removeTextChangedListener(this);
                        }

                    });
                    LnameET.setText(capitalizedText);
                }
            }
        });

        usernameeee.setEnabled(false);
        passs.setEnabled(false);
        confirmpasss.setEnabled(false);
        toolbar_title.setText("Sign up");
        setSupportActionBar(signup_toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FName = FnameET.getText().toString();
                LName = LnameET.getText().toString();
                HouseNo = housenoo.getText().toString();
                HomePhoneNo = homephone.getText().toString();
                MobileNo = mobno.getText().toString();
                Email = emailadd.getText().toString().trim();
                Username = usernameeee.getText().toString();
                Password = passs.getText().toString();
                confirmpass = confirmpasss.getText().toString();
                ConfirmEmailETs = ConfirmEmailET.getText().toString();

                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.FName, FName);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.LName, LName);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.HouseNo, HouseNo);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.HomePhoneNo, HomePhoneNo);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.MobileNo, MobileNo);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Email, Email);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Username, Username);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Password, Password);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Confirm_Password, confirmpass);
                PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Confirm_Email, ConfirmEmailETs);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        registerBackground();

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesbtn.setBackgroundResource(R.drawable.yes);
                nobtn.setBackgroundResource(R.drawable.no);
                trademen = "yes";

                Intent i = new Intent(getApplicationContext(), TrademanTermsActivity.class);
                startActivity(i);

            }
        });

        VerfiyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobno.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    MobilePhoneSU_string = mobno.getText().toString().trim();
                    SendOtpApi(MobilePhoneSU_string);

                    Toast.makeText(context, "success>>" + MobilePhoneSU_string, Toast.LENGTH_SHORT).show();

                }

            }
        });

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesbtn.setBackgroundResource(R.drawable.no);
                nobtn.setBackgroundResource(R.drawable.yes);
                trademen = "no";
            }
        });


        emailadd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameeee.setText(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signup2checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passs.setEnabled(true);
                    confirmpasss.setEnabled(true);
                } else if (!isChecked) {
                    usernameeee.setEnabled(false);
                    passs.setEnabled(false);
                    confirmpasss.setEnabled(false);
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationSuccess()) {

                    if (InternetDetect.isConnected(getApplicationContext())) {

                        String FName = FnameET.getText().toString();
                        String LName = LnameET.getText().toString();
                        String HouseNo = housenoo.getText().toString();
                        String HomePhoneNo = homephone.getText().toString();
                        String MobileNo = mobno.getText().toString();
                        String Email = emailadd.getText().toString().trim();
                        String Username = usernameeee.getText().toString();
                        String Password = confirmpasss.getText().toString();

                        Log.e("ALL VALUE", " " + HouseNo + " " + HomePhoneNo + " " + MobileNo + " " + Email + " " + Username + " " + Password + " " + trademen);

                        if (RegId.equalsIgnoreCase("")) {
                            registerBackground();
                        } else {
                            new JsonSignUp().execute(FName, LName, HouseNo,
                                    HomePhoneNo, MobileNo, Email, Username, Password,
                                    trademen, RegId, latUser, lonUser, HouseNo, HouseNo, HouseNo);
                        }

                    } else {
                        errorToast("Check Internet Connectivity");
                    }

                }

            }

            private boolean ValidationSuccess() {

                if (FnameET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (LnameET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (housenoo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter house Number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (mobno.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (OTP.equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Please verify your mobile number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (emailadd.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!Appconstants.isValidEmail(emailadd.getText().toString().trim())) {
                    Toast.makeText(SignupActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ConfirmEmailET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Confirm email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(ConfirmEmailET.getText().toString().trim().equals(emailadd.getText().toString().trim()))) {
                    Toast.makeText(SignupActivity.this, "Email does not match", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (usernameeee.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (passs.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (passs.getText().toString().length() < 6) {
                    Toast.makeText(SignupActivity.this, "Minimum password length is 6", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (confirmpasss.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(confirmpasss.getText().toString().equals(passs.getText().toString()))) {
                    Log.e("pass", passs.getText().toString());
                    Log.e("confirmpasss", confirmpasss.getText().toString());
                    Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (trademen.equalsIgnoreCase("")) {
                    Toast.makeText(SignupActivity.this, "Are you a Tradesman ?", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(signup2checkbox.isChecked())) {
                    Toast.makeText(SignupActivity.this, "Please confirm above information is true.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

    }

    private class JsonSignUp extends AsyncTask<String, Void, String> {

        String id, name, houseno, street, home_add, postcode, city, state, homephone, mobilephone, email, username, result = "", error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "sign_up");

            try {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("name", params[0] + " " + params[1]));
                nameValuePairs.add(new BasicNameValuePair("first_name", params[0]));
                nameValuePairs.add(new BasicNameValuePair("last_name", params[1]));
                nameValuePairs.add(new BasicNameValuePair("housenoo", params[2]));
                nameValuePairs.add(new BasicNameValuePair("home_phone", params[7]));
                nameValuePairs.add(new BasicNameValuePair("work_phone", params[8]));
                nameValuePairs.add(new BasicNameValuePair("mobile_phone", params[4]));
                nameValuePairs.add(new BasicNameValuePair("email", params[5]));
                nameValuePairs.add(new BasicNameValuePair("username", params[6]));
                nameValuePairs.add(new BasicNameValuePair("password", params[7]));
                nameValuePairs.add(new BasicNameValuePair("tradesman", params[8]));
                nameValuePairs.add(new BasicNameValuePair("registration_id", params[9]));

                nameValuePairs.add(new BasicNameValuePair("user_lat", params[10]));
                nameValuePairs.add(new BasicNameValuePair("user_lon", params[11]));
                nameValuePairs.add(new BasicNameValuePair("home_address", params[12]));
                nameValuePairs.add(new BasicNameValuePair("street", params[13]));
                nameValuePairs.add(new BasicNameValuePair("address", params[14]));

                Log.e("parmsss", String.valueOf(params));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                Log.e("JsonData", obj);
                JSONArray ParentArray = new JSONArray(obj);
                JSONObject FinalObject = ParentArray.getJSONObject(0);

                Log.e("ParentArrayyy", "" + ParentArray);

                result = FinalObject.getString("result");

                if (result.equalsIgnoreCase("unsuccessfully")) {
                    error = FinalObject.getString("error");
                    return result;
                } else if (result.equalsIgnoreCase("successfully")) {
                    id = FinalObject.getString("id");
                    name = FinalObject.getString("name");
                    houseno = FinalObject.getString("housenoo");
                    street = FinalObject.getString("street");
                    home_add = FinalObject.getString("home_address");
                    postcode = FinalObject.getString("post_code");
                    city = FinalObject.getString("city");
                    state = FinalObject.getString("state");
                    homephone = FinalObject.getString("home_phone");
                    mobilephone = FinalObject.getString("mobile_phone");
                    email = FinalObject.getString("email");
                    username = FinalObject.getString("username");
                    return result;
                }
            } catch (Exception e) {
                System.out.println("eeeerrrrorrrrr in registration ++++++" + e);
            }

            return result;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (result == null) {
                Toast.makeText(SignupActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(SignupActivity.this);
                builder1.setTitle("Registration Successful");
                builder1.setIcon(R.drawable.mainlogo);
                builder1.setMessage("A Confirmation Link has been sent to registered email id. Please click on the Activation Link to Activate your account.");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } else if (result.equalsIgnoreCase("unsuccessfully")) {
                Toast.makeText(SignupActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void SendOtpApi(final String mobilePhoneSU_string) {

        String s = mobilePhoneSU_string;
        s = s.replaceFirst("^0*", "");

        Log.e("sss", s);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Log.e("link", "https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?mobile_verify&mobile=" + code_select_string + s);
        AndroidNetworking.get("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?mobile_verify&mobile=" + code_select_string + s)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            // String result = response.getString("result");
                            String status = response.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                String verify_code = response.getString("verify_code");
                                progressDialog.dismiss();
                                Intent intent = new Intent(SignupActivity.this, MobileVerfication.class);
                                intent.putExtra("verify_code", verify_code);
                                intent.putExtra("mobile", code_select_string + mobilePhoneSU_string);
                                startActivityForResult(intent, 3);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        FName = FnameET.getText().toString();
        LName = LnameET.getText().toString();
        HouseNo = housenoo.getText().toString();
        HomePhoneNo = homephone.getText().toString();
        MobileNo = mobno.getText().toString();
        Email = emailadd.getText().toString().trim();
        Username = usernameeee.getText().toString();
        Password = passs.getText().toString();
        confirmpass = confirmpasss.getText().toString();

        ConfirmEmailETs = ConfirmEmailET.getText().toString();

        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.FName, FName);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.LName, LName);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.HouseNo, HouseNo);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.HomePhoneNo, HomePhoneNo);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.MobileNo, MobileNo);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Email, Email);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Username, Username);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Password, Password);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Confirm_Password, confirmpass);
        PreferenceConnector.writeString(SignupActivity.this, PreferenceConnector.Confirm_Email, ConfirmEmailETs);

    }

    private void initComp() {

        Address_Save = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.Address_Save, "");
        signup_toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        toolbar_title = (TextView) signup_toolbar.findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) signup_toolbar.findViewById(R.id.NavigationUpIM);
        re_yesno = (RelativeLayout) findViewById(R.id.re_yesno);
        terms_and = (TextView) findViewById(R.id.terms_and);
        namesignup = (EditText) findViewById(R.id.namesignup);
        VerfiyTV = (TextView) findViewById(R.id.VerfiyTV);

        homephone = (EditText) findViewById(R.id.homephone);
        HomePhoneNos = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.HomePhoneNo, "");
        homephone.setText(HomePhoneNos);

        housenoo = (EditText) findViewById(R.id.housenoo);
        HouseNos = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.HouseNo, "");
        housenoo.setText(HouseNos);

        signup2checkbox = (CheckBox) findViewById(R.id.signup2checkbox);
        signup2checkbox1 = (CheckBox) findViewById(R.id.signup2checkbox1);
        mobno = (EditText) findViewById(R.id.mobno);
        MobileNoNos = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.MobileNo, "");
        mobno.setText(MobileNoNos);
        emailadd = (EditText) findViewById(R.id.emailadd);
        Emails = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.Email, "");
        emailadd.setText(Emails);
        ConfirmEmailET = (EditText) findViewById(R.id.ConfirmEmailET);
        Confirm_Emails = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.Confirm_Email, "");
        ConfirmEmailET.setText(Confirm_Emails);
        usernameeee = (EditText) findViewById(R.id.usernameeee);
        Usernames = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.Username, "");
        usernameeee.setText(Usernames);
        passs = (EditText) findViewById(R.id.passs);
        Passwords = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.Password, "");
        passs.setText(Passwords);

        confirmpasss = (EditText) findViewById(R.id.confirmpasss);
        nobtn = (Button) findViewById(R.id.nobtn);
        yesbtn = (Button) findViewById(R.id.yesbtn);
        registerbtn = (Button) findViewById(R.id.registerbtn);

        FnameET = (EditText) findViewById(R.id.FnameET);
        FNames = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.FName, "");
        FnameET.setText(FNames);

        LnameET = (EditText) findViewById(R.id.LnameET);
        LNames = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.LName, "");
        LnameET.setText(LNames);

        SignUpMsgTV = (TextView) findViewById(R.id.SignUpMsgTV);

        signup2checkbox1.setChecked(true);

        housenoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SignupActivity.this, GooglePlacesAutocompleteActivity.class);
                startActivityForResult(in, 101);
            }
        });

        if (!Address_Save.equals("")) {
            housenoo.setText(Address_Save);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        housenoo = (EditText) findViewById(R.id.housenoo);

        Address_Save = PreferenceConnector.readString(SignupActivity.this, PreferenceConnector.Address_Save, "");

        if (!Address_Save.equals("")) {

            housenoo.setText(Address_Save);

        }
    }

    private void errorToast(String x) {

        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }

    private void registerBackground() {

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";

                try {

                    if (gcm == null) {

                        gcm = GoogleCloudMessaging.getInstance(SignupActivity.this);
                    }

                    RegId = gcm.register("741851403308");

                    Log.e("REGGGG", ">>>>" + RegId);

                } catch (IOException ex) {

                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            protected void onPostExecute(String msg) {

            }

        }.execute(null, null, null);
    }

    private class JsonPostCode extends AsyncTask<String, String, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(HttpPAth.Urlpath + "all_postcode");

                List<NameValuePair> list = new ArrayList<NameValuePair>();

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                Appconstants.postCode = new ArrayList<>();
                String Jsondata = stringBuffer.toString();
                Log.e("JsonPostCode", Jsondata);
                JSONArray parentarray = new JSONArray(Jsondata);

                for (int i = 0; i < parentarray.length(); i++) {
                    JSONObject parentobject = parentarray.getJSONObject(i);

                    String post_code = parentobject.getString("post_code");
                    Appconstants.postCode.add(post_code);
                    result = parentobject.getString("result");
                }
                return result;

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
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
                if (InternetDetect.isConnected(SignupActivity.this)) {
                    new JsonPostCode().execute();
                } else {
                    Toast.makeText(SignupActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            } else if (result.equalsIgnoreCase("successfully")) {
            } else {
                if (InternetDetect.isConnected(SignupActivity.this)) {
                } else {
                    Toast.makeText(SignupActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void getLatLonFromAddress(String address) {
        ProjectUtil.showProgressDialog(SignupActivity.this, false, "Please wait...");
        String postReceiverUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                address.replace(",", "+") + "&key=" + getResources().getString(R.string.places_api_key);
        Log.e("asdasdasds", "Plcaes Url = " + postReceiverUrl);
        AndroidNetworking.get(postReceiverUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                ProjectUtil.pauseProgressDialog();
                Log.e("getLatLonFromAddress", response);
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray resultArray = mainObj.getJSONArray("results");
                    JSONObject resultFirstObj = resultArray.getJSONObject(0);
                    JSONObject geometryObj = resultFirstObj.getJSONObject("geometry");
                    JSONObject locationObj = geometryObj.getJSONObject("location");

                    double lat = locationObj.getDouble("lat");
                    double lon = locationObj.getDouble("lng");

                    latUser = String.valueOf(lat);
                    lonUser = String.valueOf(lon);

//                  Appconstants.servicelocation = diiferent_add.getText().toString().trim();
//                  Appconstants.ServiceLocation = diiferent_add.getText().toString().trim();
//                  Appconstants.SITE_ADDRESS = diiferent_add.getText().toString().trim();
//                  Appconstants.lat = lat;
//                  Appconstants.lon = lon;

                    Log.e("sfsdfsdfsdf", "SITE_ADDRESS = " + Appconstants.SITE_ADDRESS);
                    Log.e("sfsdfsdfsdf", "lat = " + lat);
                    Log.e("sfsdfsdfsdf", "lon = " + lon);
                    Log.e("sfsdfsdfsdf", "lat Add= " + (lat * 1E6));
                    Log.e("sfsdfsdfsdf", "lon Add= " + lon * 1E6);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                ProjectUtil.pauseProgressDialog();
                Log.e("responseresponse", "anError = " + anError.getErrorBody());
                Log.e("responseresponse", "anError = " + anError.getErrorDetail());
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 101) {
            String add = data.getStringExtra("add");
            housenoo.setText(add);
            getLatLonFromAddress(add);
            Log.e("asdadadasd", "address = " + add);
        }

        if (requestCode == 3) {
            try {
                otp_status = data.getStringExtra("otp_status");
                if (otp_status.equalsIgnoreCase("true")) {
                    verify_otpimg.setVisibility(View.VISIBLE);
                    OTP = otp_status;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
