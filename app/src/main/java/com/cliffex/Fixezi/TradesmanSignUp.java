package com.cliffex.Fixezi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.MyUtils.NonScrollListView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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

import static com.cliffex.Fixezi.helpus.ContextManager.getContext;
import static java.lang.Integer.parseInt;

public class TradesmanSignUp extends AppCompatActivity {

    Context mContext = TradesmanSignUp.this;
    Toolbar ToolbarSU;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    EditText BusinessNameSU, ConfirmEmailSU, Abn_no, EmailSU, MobilePhoneSU, OfficePhoneSU,
            TradingNameSU, CompanyUrlSU, ed_buisness_owner_name, ed_bussnessAddress;
    Button ContinueSU;
    RelativeLayout RLExample;
    NonScrollListView LVServiceLocationSU;
    ArrayList<String> SelectedPostCode;
    RelativeLayout RL101;
    ProgressDialog progressDialog;
    TextView VerfiyTradesmanTV;
    TextView SignUpTV;
    private String Address_Save;
    private LinearLayout location_search;
    private TextView done_text;
    private RelativeLayout activity_main;
    private TextView cancle_text;
    private LinearLayout select_address;
    private TextView text_select_address;
    private TextView location_get;
    private String Address_Save1;
    private TextView seek_bar_text_distance;
    private String Select_radius1;
    private String Select_adress1;
    private String Message;
    private String Select_address_Raedus;
    private String Select_radius;
    private String buisinessnameeS;
    private String Abn;
    private String tradingnameS;
    private String tradeofiicenoS;
    private String trademobileee;
    private String buisness_owner_name;
    private String buisness_address;
    private String tradeemailS;
    private String companywebsiteS;
    private String buisinessnameeS1;
    private String tradingnameS1;
    private String tradeofiicenoS1;
    private String buisness_owner_nameS1;
    private String buisness_addressS1;
    private String tradmenemailS;
    private String ConfirmEmailSU1;
    private String confirm_emailS;
    private String AbnS1;
    private SeekBar seekBar;
    private String MobilePhoneSU_string = "";
    private Spinner spinner;
    private Object item;
    private String code_select;
    private String code_select_string;
    private ImageView verify_otpimg;
    private String status_otp = "false";
    private String otp_status;
    private String OTPStatus;
    private ImageView icon_location;
    private View dialogueView;
    StateProgressBar stateProgressBar;
    public static boolean isVerifyMobile = false;
    String[] step = {"Details","Verify","Upload","Complete"};
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.Please Wait ......");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman_sign_up);

        ArrayAdapter<String> countryName = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DataCode.countryAreaCodes);

        Select_radius1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Select_radius, "");
        Select_adress1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Select_address_Raedus, "");
        OTPStatus = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.OTPStatus, "");

        verify_otpimg = (ImageView) findViewById(R.id.verify_otpimg);
        location_get = (TextView) findViewById(R.id.location_get);

        stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(step);
        stateProgressBar.setStateDescriptionTypeface("fonts/droidderif.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/droidderif.ttf");
        stateProgressBar.setStateSize(25f);
        stateProgressBar.setStateDescriptionSize(15f);
        stateProgressBar.setDescriptionTopSpaceIncrementer(10f);

        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

        if (OTPStatus.equalsIgnoreCase("verify")) {
            verify_otpimg.setVisibility(View.VISIBLE);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }

        status_otp = PreferenceConnector.readString(TradesmanSignUp.this,PreferenceConnector.otp_status,"");
        seekBar = (SeekBar) findViewById(R.id.seekBar_luminosite);

        seekBar.setEnabled(false);

        seek_bar_text_distance = (TextView) findViewById(R.id.seek_bar_text_distance);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(countryName);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item = parent.getItemAtPosition(pos);

                code_select = String.valueOf(item);
                String[] str = code_select.split("\\+");
                code_select_string = str[1];

                Toast.makeText(TradesmanSignUp.this, "" + code_select_string, Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek_bar_text_distance.setText(progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ToolbarSU = (Toolbar) findViewById(R.id.ToolbarSU);
        toolbar_title = (TextView) ToolbarSU.findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) ToolbarSU.findViewById(R.id.NavigationUpIM);
        location_search = (LinearLayout) findViewById(R.id.location_search);

        icon_location = (ImageView) findViewById(R.id.icon_location);

        icon_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog alertDialog;

                LayoutInflater layoutInflater = LayoutInflater.from(TradesmanSignUp.this);
                dialogueView = layoutInflater.inflate(R.layout.layout_dialogue, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TradesmanSignUp.this);
                alertDialogBuilder.setView(dialogueView);

                alertDialog = alertDialogBuilder.create();

                dialogueView.findViewById((R.id.DoneQuote)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        Abn_no = (EditText) findViewById(R.id.Abn_no);
        AbnS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Abn, "");
        Abn_no.setText(AbnS1);

        BusinessNameSU = (EditText) findViewById(R.id.BusinessNameSU);
        buisinessnameeS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.buisinessnameeS, "");
        BusinessNameSU.setText(buisinessnameeS1);

        TradingNameSU = (EditText) findViewById(R.id.TradingNameSU);
        tradingnameS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.tradingnameS, "");
        TradingNameSU.setText(tradingnameS1);

        OfficePhoneSU = (EditText) findViewById(R.id.OfficePhoneSU);

        tradeofiicenoS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.tradeofiicenoS, "");
        OfficePhoneSU.setText(tradeofiicenoS1);

        MobilePhoneSU = (EditText) findViewById(R.id.MobilePhoneSU);
        tradeofiicenoS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.trademobileee, "");
        MobilePhoneSU.setText(tradeofiicenoS1);

        ed_buisness_owner_name = (EditText) findViewById(R.id.ed_buisness_owner_name);
        buisness_owner_nameS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.buisness_owner_name, "");
        ed_buisness_owner_name.setText(buisness_owner_nameS1);

        ed_bussnessAddress = (EditText) findViewById(R.id.ed_bussnessAddress);
        buisness_addressS1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.buisness_address, "");
        ed_bussnessAddress.setText(buisness_addressS1);

        EmailSU = (EditText) findViewById(R.id.EmailSU);
        tradmenemailS = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.tradmenemailS, "");
        EmailSU.setText(tradmenemailS);

        ConfirmEmailSU = (EditText) findViewById(R.id.ConfirmEmailSU);
        confirm_emailS = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.confirm_email, "");
        ConfirmEmailSU.setText(tradmenemailS);

        CompanyUrlSU = (EditText) findViewById(R.id.CompanyUrlSU);
        companywebsiteS = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.companywebsiteS, "");
        CompanyUrlSU.setText(companywebsiteS);
        ContinueSU = (Button) findViewById(R.id.ContinueSU);
        VerfiyTradesmanTV = (TextView) findViewById(R.id.VerfiyTradesmanTV);
        RLExample = (RelativeLayout) findViewById(R.id.RLExample);
        RL101 = (RelativeLayout) findViewById(R.id.RL101);
        SignUpTV = (TextView) findViewById(R.id.SignUpTV);

        LVServiceLocationSU = (NonScrollListView) findViewById(R.id.LVServiceLocationSU);

        ed_bussnessAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.INTERNET)
                                != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TradesmanSignUp.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(TradesmanSignUp.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    ) {
                        Intent in = new Intent(TradesmanSignUp.this, GooglePlacesAutocompleteActivity.class);
                        startActivity(in);
                    } else {
                        ActivityCompat.requestPermissions(TradesmanSignUp.this,
                                new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,},
                                MY_PERMISSIONS_REQUEST_WRITE_FIELS);
                    }
                }else{
                    Intent in = new Intent(TradesmanSignUp.this, GooglePlacesAutocompleteActivity.class);
                    startActivity(in);
                    Toast.makeText(getApplicationContext(), "Success!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        location_search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(TradesmanSignUp.this, Select_Radius.class);
                startActivityForResult(in, 2);
            }
        });

        Address_Save = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Address_Save, "");
        Address_Save1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Address_Save1, "");

        if (!Address_Save.equals("")) {

            ed_bussnessAddress.setText(Address_Save);
        }

        if (!Select_radius1.equals("")) {

            location_get.setText(Select_adress1);
        }

        setSupportActionBar(ToolbarSU);

        toolbar_title.setText("Tradesman Signup");

        String text = "<font color='#1E90FF'>Get Started today ,</font> only $9.95 for any job you accept";

        SignUpTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buisinessnameeS = BusinessNameSU.getText().toString();
                Abn = Abn_no.getText().toString();
                tradingnameS = TradingNameSU.getText().toString();
                tradeofiicenoS = OfficePhoneSU.getText().toString();
                trademobileee = MobilePhoneSU.getText().toString();
                buisness_owner_name = ed_buisness_owner_name.getText().toString();
                buisness_address = ed_bussnessAddress.getText().toString();
                tradeemailS = EmailSU.getText().toString().trim();
                companywebsiteS = CompanyUrlSU.getText().toString();
                ConfirmEmailSU1 = ConfirmEmailSU.getText().toString();

                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.buisinessnameeS, buisinessnameeS);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.Abn, Abn);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.tradingnameS, tradingnameS);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.tradeofiicenoS, tradeofiicenoS);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.trademobileee, trademobileee);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.buisness_owner_name, buisness_owner_name);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.buisness_address, buisness_address);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.companywebsiteS, companywebsiteS);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.tradmenemailS, tradeemailS);
                PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.confirm_email, ConfirmEmailSU1);

                finish();
            }
        });

        SelectedPostCode = new ArrayList<>();

        RL101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        VerfiyTradesmanTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobilePhoneSU_string = MobilePhoneSU.getText().toString().trim();
                if(!MobilePhoneSU_string.isEmpty()) {
                    SendOtpApi(MobilePhoneSU_string);
                } else {
                    Toast.makeText(mContext, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ContinueSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validate()) {

                    StringBuilder s = new StringBuilder();
                    for (int i = 0; i < SelectedPostCode.size(); i++) {

                        if (i == 0) {
                            s.append(SelectedPostCode.get(i));
                            continue;
                        }

                        s.append("," + SelectedPostCode.get(i));
                    }

                    String AllServiceLocations = s.toString();

                    Appconstants.buisinessnameeS = BusinessNameSU.getText().toString();
                    Appconstants.Abn = Abn_no.getText().toString();
                    Appconstants.tradingnameS = TradingNameSU.getText().toString();
                    Appconstants.tradeofiicenoS = OfficePhoneSU.getText().toString();
                    Appconstants.trademobileee = MobilePhoneSU.getText().toString();
                    Appconstants.buisness_owner_name = ed_buisness_owner_name.getText().toString();
                    Appconstants.buisness_address = ed_bussnessAddress.getText().toString();
                    Appconstants.tradeemailS = EmailSU.getText().toString().trim();
                    Appconstants.companywebsiteS = CompanyUrlSU.getText().toString();
                    Appconstants.servicelocation = AllServiceLocations;

                    Intent intent = new Intent(TradesmanSignUp.this, TradesmanSignup2.class);
                    startActivity(intent);

                }

            }

            private boolean Validate() {

                if (BusinessNameSU.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter Business Name", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (OfficePhoneSU.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter Office Number", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (Abn_no.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter ABN ", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (MobilePhoneSU.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (ed_buisness_owner_name.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter Buisness Owner Name", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (ed_bussnessAddress.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter Buisness Address", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (EmailSU.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (!Appconstants.isValidEmail(EmailSU.getText().toString().trim())) {

                    Toast.makeText(TradesmanSignUp.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (ConfirmEmailSU.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(TradesmanSignUp.this, "Please Confirm Email Address", Toast.LENGTH_SHORT).show();
                    return false;

                } else if (!(EmailSU.getText().toString().trim().equals(ConfirmEmailSU.getText().toString().trim()))) {

                    Toast.makeText(TradesmanSignUp.this, "Email Address does not match", Toast.LENGTH_SHORT).show();
                    return false;

                }

                return true;
            }

        });

        if (Appconstants.postCode == null) {
            if(InternetDetect.isConnected(this)) {
                new JsontaskPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else if (Appconstants.postCode.isEmpty()) {
            if (InternetDetect.isConnected(this)) {
                new JsontaskPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TradesmanSignUp.this, R.layout.custom_checkout, R.id.text1, Appconstants.postCode);
        }
    }


    private void SendOtpApi(final String mobilePhoneSU_string) {

        String s = mobilePhoneSU_string;
        s = s.replaceFirst("^0*", "");

        Log.e("sss",s);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(TradesmanSignUp.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.e("link_tradman","https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?mobile_verify&mobile="+code_select_string+s);

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

                            String result = response.getString("result");
                            String status = response.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                String verify_code = response.getString("verify_code");

                                progressDialog.dismiss();

                                Intent intent = new Intent(TradesmanSignUp.this, MobileVerfication.class);
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

        buisinessnameeS = BusinessNameSU.getText().toString();
        Abn = Abn_no.getText().toString();
        tradingnameS = TradingNameSU.getText().toString();
        tradeofiicenoS = OfficePhoneSU.getText().toString();
        trademobileee = MobilePhoneSU.getText().toString();
        buisness_owner_name = ed_buisness_owner_name.getText().toString();
        buisness_address = ed_bussnessAddress.getText().toString();
        tradeemailS = EmailSU.getText().toString().trim();
        companywebsiteS = CompanyUrlSU.getText().toString();
        ConfirmEmailSU1 = ConfirmEmailSU.getText().toString();

        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.buisinessnameeS, buisinessnameeS);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.Abn, Abn);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.tradingnameS, tradingnameS);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.tradeofiicenoS, tradeofiicenoS);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.trademobileee, trademobileee);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.buisness_owner_name, buisness_owner_name);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.buisness_address, buisness_address);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.companywebsiteS, companywebsiteS);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.tradmenemailS, tradeemailS);
        PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.confirm_email, ConfirmEmailSU1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            try {
                Select_radius = data.getStringExtra("Select_radius");
                Select_address_Raedus = data.getStringExtra("Select_address_Raedus");
                location_get.setText(Select_address_Raedus);
                seek_bar_text_distance.setText(Select_radius + "km");
                seekBar.setEnabled(false);
                seekBar.setProgress(parseInt(Select_radius));
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        if (requestCode == 3) {
            try {
                otp_status = data.getStringExtra("otp_status");
                if (otp_status.equalsIgnoreCase("true")) {
                    verify_otpimg.setVisibility(View.VISIBLE);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.OTPStatus, "verify");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        if(isVerifyMobile) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            PreferenceConnector.writeString(TradesmanSignUp.this, PreferenceConnector.OTPStatus,"verify");
        } else {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        }

        if (status_otp.equalsIgnoreCase("true")) {
            verify_otpimg.setVisibility(View.VISIBLE);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }

        ed_bussnessAddress = (EditText) findViewById(R.id.ed_bussnessAddress);
        location_get = (TextView) findViewById(R.id.location_get);

        Address_Save = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Address_Save, "");
        Address_Save1 = PreferenceConnector.readString(TradesmanSignUp.this, PreferenceConnector.Address_Save1, "");

        if (!Address_Save.equals("")) {

            ed_bussnessAddress.setText(Address_Save);

        }

        if (!Address_Save1.equals("")) {

            location_get.setText(Address_Save1);

        }

    }

    private class JsontaskPostCode extends AsyncTask<String, String, String> {

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

                if (InternetDetect.isConnected(TradesmanSignUp.this)) {

                    new JsontaskPostCode().execute();

                } else {
                    Toast.makeText(TradesmanSignUp.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }

            } else if (result.equalsIgnoreCase("successfully")) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TradesmanSignUp.this, R.layout.custom_checkout, R.id.text1, Appconstants.postCode);

            } else {

                if (InternetDetect.isConnected(TradesmanSignUp.this)) {

                    new JsontaskPostCode().execute();

                } else {

                    Toast.makeText(TradesmanSignUp.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class ListViewAdapter extends BaseAdapter {


        Context mContext;
        LayoutInflater inflater;
        private List<String> PostCodeList;

        public ListViewAdapter(Context context,
                               List<String> PostCodeList) {


            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.PostCodeList = PostCodeList;


        }

        public class ViewHolder {
            TextView ServiceLocationName;
            ImageView DeleteLocationIm;
        }

        @Override
        public int getCount() {

            return PostCodeList == null ? 0 : PostCodeList.size();

        }

        @Override
        public Object getItem(int position) {
            return PostCodeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.service_location_rowitem_small, null);
                holder.ServiceLocationName = (TextView) view.findViewById(R.id.ServiceLocationNameSmall);
                holder.DeleteLocationIm = (ImageView) view.findViewById(R.id.DeleteLocationImSmall);
                view.setTag(holder);


            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.ServiceLocationName.setText(PostCodeList.get(position));

            holder.DeleteLocationIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostCodeList.remove(position);
                    notifyDataSetChanged();

                }
            });

            return view;
        }
    }

}
