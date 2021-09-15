package com.cliffex.Fixezi;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
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
import static com.cliffex.Fixezi.Other.MySharedPref.saveData;


/**
 * Created by technorizen8 on 2/2/16.
 */
public class BackupSignupActivity extends AppCompatActivity {

    private Toolbar signup_toolbar;
    TextView toolbar_title, statesignup, VerfiyTV;
    Context context;

    RelativeLayout NavigationUpIM;

    View v;
    String statelist[] = {"WA"};
    String citylist[] = {"Perth"};
    EditText namesignup, housenoo, homephone, workphone;
    AutoCompleteTextView postalignupautocomplete;

    EditText mobno, emailadd, usernameeee, passs, confirmpasss, ConfirmEmailET, HomeAddressET;
    Button nobtn, yesbtn, registerbtn;
    String trademen = "";
    int a = 1;
    String OTP = "", mobilestatus = "";
    CheckBox signup2checkbox;
    GoogleCloudMessaging gcm;
    String RegId = "";
    EditText LnameET, FnameET;
    TextView SignUpMsgTV, citysign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComp();

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

        String text = "<font color='#1E90FF'>IF YOU ARE A TRADESMAN,</font> WOULD YOU LIKE TO JOIN FIXEZI , AS A TRADESMAN?";
        SignUpMsgTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        mobilestatus = getData(getApplicationContext(), "mobilestatus", null);

        //  System.out.println("verification_Status_________________"+mobilestatus);


        FnameET.addTextChangedListener(new TextWatcher() {
            int mStart = 0;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                mStart = arg1 + arg3;

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
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
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

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
                    Toast.makeText(BackupSignupActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    saveData(getApplicationContext(), "mob", mobno.getText().toString());
                    //mobno.getText().toString()

                    OTP = "otp";
                    Intent intent = new Intent(BackupSignupActivity.this, MobileVerfication.class);
                    startActivity(intent);
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

        statesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BackupSignupActivity.this);
                builder.setTitle("Select State");
                builder.setSingleChoiceItems(statelist, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        statesignup.setText(statelist[which].toString());
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        citysign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BackupSignupActivity.this);
                builder.setTitle("Select City");
                builder.setSingleChoiceItems(citylist, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        citysign.setText(citylist[which].toString());
                        dialog.dismiss();
                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
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
                        String HomeAdd = HomeAddressET.getText().toString();
                        String PostCode = postalignupautocomplete.getText().toString();
                        String City = citysign.getText().toString();
                        String State = statesignup.getText().toString();
                        String HomePhoneNo = homephone.getText().toString();
                        String WorkPhone = workphone.getText().toString();
                        String MobileNo = mobno.getText().toString();
                        String Email = emailadd.getText().toString().trim();
                        String Username = usernameeee.getText().toString();
                        String Password = confirmpasss.getText().toString();


                        Log.e("ALL VALUE", " " + HouseNo + " " + " " + PostCode + " " + City + " " + State + " " + HomePhoneNo + " " + WorkPhone + " " + MobileNo + " " + Email + " " + Username + " " + Password + " " + trademen);

                        if (RegId.equalsIgnoreCase("")) {

                            registerBackground();

                        } else {

                            new JsonSignUp().execute(FName, LName, HouseNo, HomeAdd, PostCode, City, State, HomePhoneNo, WorkPhone, MobileNo, Email, Username, Password, trademen, RegId);

                        }

                    } else {
                        errorToast("Check Internet Connectivity");
                    }
                }
            }


            private boolean ValidationSuccess() {

                if (FnameET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (LnameET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (housenoo.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter house Number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (HomeAddressET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter Street Name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (postalignupautocomplete.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter post code", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (citysign.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (statesignup.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (workphone.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter work phone number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (mobno.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (OTP.equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Please verify your mobile number", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (emailadd.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!Appconstants.isValidEmail(emailadd.getText().toString().trim())) {

                    Toast.makeText(BackupSignupActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ConfirmEmailET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Confirm email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(ConfirmEmailET.getText().toString().trim().equals(emailadd.getText().toString().trim()))) {

                    Toast.makeText(BackupSignupActivity.this, "Email does not match", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (usernameeee.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (passs.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (passs.getText().toString().length() < 6) {

                    Toast.makeText(BackupSignupActivity.this, "Minimum password length is 6", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (confirmpasss.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(confirmpasss.getText().toString().equals(passs.getText().toString()))) {

                    Log.e("pass", passs.getText().toString());
                    Log.e("confirmpasss", confirmpasss.getText().toString());
                    Toast.makeText(BackupSignupActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (trademen.equalsIgnoreCase("")) {

                    Toast.makeText(BackupSignupActivity.this, "Are you a Tradesman ?", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!(signup2checkbox.isChecked())) {

                    Toast.makeText(BackupSignupActivity.this, "Please confirm above information is true.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }

        });

        if (Appconstants.postCode == null) {

            if (InternetDetect.isConnected(this)) {

                new JsonPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else if (Appconstants.postCode.isEmpty()) {

            if (InternetDetect.isConnected(this)) {

                new JsonPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            postalignupautocomplete.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_checkout, R.id.text1, Appconstants.postCode));
        }
    }

    private void initComp() {

        signup_toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        toolbar_title = (TextView) signup_toolbar.findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) signup_toolbar.findViewById(R.id.NavigationUpIM);
        
        namesignup = (EditText) findViewById(R.id.namesignup);
        postalignupautocomplete = (AutoCompleteTextView) findViewById(R.id.postalignupautocomplete);
        citysign = (TextView) findViewById(R.id.citysign);
        statesignup = (TextView) findViewById(R.id.statesignup);
        VerfiyTV = (TextView) findViewById(R.id.VerfiyTV);
        homephone = (EditText) findViewById(R.id.homephone);
        workphone = (EditText) findViewById(R.id.workphone);

        housenoo = (EditText) findViewById(R.id.housenoo);
        signup2checkbox = (CheckBox) findViewById(R.id.signup2checkbox);
        mobno = (EditText) findViewById(R.id.mobno);
        emailadd = (EditText) findViewById(R.id.emailadd);
        ConfirmEmailET = (EditText) findViewById(R.id.ConfirmEmailET);
        usernameeee = (EditText) findViewById(R.id.usernameeee);
        passs = (EditText) findViewById(R.id.passs);
        confirmpasss = (EditText) findViewById(R.id.confirmpasss);
        nobtn = (Button) findViewById(R.id.nobtn);
        yesbtn = (Button) findViewById(R.id.yesbtn);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        FnameET = (EditText) findViewById(R.id.FnameET);
        LnameET = (EditText) findViewById(R.id.LnameET);
        HomeAddressET = (EditText) findViewById(R.id.HomeAddressET);
        SignUpMsgTV = (TextView) findViewById(R.id.SignUpMsgTV);

    }

    private void errorToast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }


    private class JsonSignUp extends AsyncTask<String, Void, String> {

        String id, name, houseno, street, home_add, postcode, city, state, homephone, workphone, mobilephone, email, username, result = "", error = "";

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
                nameValuePairs.add(new BasicNameValuePair("home_address", params[3]));
                nameValuePairs.add(new BasicNameValuePair("post_code", params[4]));
                nameValuePairs.add(new BasicNameValuePair("city", params[5]));
                nameValuePairs.add(new BasicNameValuePair("state", params[6]));
                nameValuePairs.add(new BasicNameValuePair("home_phone", params[7]));
                nameValuePairs.add(new BasicNameValuePair("work_phone", params[8]));
                nameValuePairs.add(new BasicNameValuePair("mobile_phone", params[9]));
                nameValuePairs.add(new BasicNameValuePair("email", params[10]));
                nameValuePairs.add(new BasicNameValuePair("username", params[11]));
                nameValuePairs.add(new BasicNameValuePair("password", params[12]));
                nameValuePairs.add(new BasicNameValuePair("tradesman", params[13]));
                nameValuePairs.add(new BasicNameValuePair("registration_id", params[14]));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());
                Log.e("JsonData", obj);

                JSONArray ParentArray = new JSONArray(obj);
                JSONObject FinalObject = ParentArray.getJSONObject(0);

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
                    workphone = FinalObject.getString("work_phone");
                    mobilephone = FinalObject.getString("mobile_phone");
                    email = FinalObject.getString("email");
                    username = FinalObject.getString("username");
                    return result;
                }

            } catch (Exception e) {
                System.out.println("eeeerrrrorrrrr in registration ++++++" + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                Toast.makeText(BackupSignupActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (result.equalsIgnoreCase("unsuccessfully")) {
                Toast.makeText(BackupSignupActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
            }

        }
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

                if (InternetDetect.isConnected(BackupSignupActivity.this)) {

                    new JsonPostCode().execute();
                } else {
                    Toast.makeText(BackupSignupActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }

            } else if (result.equalsIgnoreCase("successfully")) {

                postalignupautocomplete.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_checkout, R.id.text1, Appconstants.postCode));

            } else {

                if (InternetDetect.isConnected(BackupSignupActivity.this)) {

                    new JsonPostCode().execute();
                } else {
                    Toast.makeText(BackupSignupActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void registerBackground() {

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";

                try {

                    if (gcm == null) {

                        gcm = GoogleCloudMessaging.getInstance(BackupSignupActivity.this);
                    }

                    RegId = gcm.register("741851403308");

                    Log.e("REGGGG", ">>>>" + RegId);

                } catch (IOException ex) {

                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

        }.execute(null, null, null);
    }

}
