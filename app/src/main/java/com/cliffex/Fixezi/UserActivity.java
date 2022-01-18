package com.cliffex.Fixezi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.Model.CategoryBean;
import com.cliffex.Fixezi.Model.PendingRequestBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.GPSTracker;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.MyUtils.NonScrollListView;
import com.cliffex.Fixezi.util.MyApp;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialogSet;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity
        implements TimePickerDialogSet.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, OnMapReadyCallback {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    private final Integer THRESHOLD = 2;
    private final int count = 0;
    Toolbar dash_toolbar;
    String Loginuserid = "";
    Context mContext = UserActivity.this;
    TextView toolbar_title, tradetxt, select_problem,
            pleseeseleccttttxtv, enter_adddress, setdatetimeee,
            selecttrade, YourAddressTV;
    Button acceptbtn, commbtn, ressbtnn;
    String redcolor = "#00A69B";
    LinearLayout sellecttttradinglayyy, uplayy, selectproblembackkk;
    RelativeLayout findnearbylayyy, enterpostaladress;
    String selectAddress = "";
    ImageView CallOutFeeInfoIM;
    Dialog SelectProblemDailog;
    ProgressBar subprogress;
    ArrayList<CategoryBean> catarray = new ArrayList<>();
    ArrayList<String> subcatarraylist = new ArrayList<>();
    String[] subcategoryArray;
    String[] mainCategoryArray;
    String[] posID;
    int year;
    TextView name;
    TextView email;
    SessionUser sessionUser;
    HashMap<String, String> UserDetail;
    EditText Any_Job_Request;
    ArrayList<String> Selecteddata;
    ImageView ProfileIM;
    RelativeLayout NavigationUpIM;
    RelativeLayout CommercialInfo, ResidentialInfo, YourAddressInfo, OtherAddressInfo;
    int jobCount = 0;
    TextView JobCountTV;
    RelativeLayout SelectProblemInfoRL, SelectTradeInfoRL;
    private LayoutInflater li;
    private View promptsView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView done_text;
    private TextView cancle_text;
    private LocationManager locationManager;
    private Location location;
    private double longitude;
    private double latitude;
    private AutoCompleteTextView gettypedlocation;
    private String order_landmarkadd;
    private GPSTracker gpsTracker;
    private LinearLayout select_address;
    private String search_address;
    private TextView text_select_address;
    private String Address_Save = "", saveLat = "", saveLon = "";
    private RelativeLayout activity_main;
    private RelativeLayout uplayy2;
    private TextView select_date_tv;
    private TextView time_set_tv;
    private RelativeLayout uplayy1;
    private LinearLayout select_date_rl, select_time_rl;
    private String status = "";
    private FirebaseAnalytics mFirebaseAnalytics;
    public static String address = "", selectDate = "";
    private Tracker mTracker;
    // private PlacesClient placesClient;

    public void fetchLatLonFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address != null) {
                try {
                    Address location = address.get(0);
                    Log.e("dsfasdadas", "getLatitude = " + location.getLatitude());
                    Log.e("dsfasdadas", "getLongitude = " + location.getLongitude());
                } catch (Exception er) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MyApp application = (MyApp) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("User Screen Akash");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Address_Save = PreferenceConnector.readString(UserActivity.this,
                PreferenceConnector.Address_Save, "");

        PreferenceConnector.writeString(UserActivity.this, PreferenceConnector.Address_Save, "");
        PreferenceConnector.writeString(UserActivity.this, PreferenceConnector.Address_Save1, "");

        if (!Address_Save.equals("")) {
            // Toast.makeText(getApplicationContext(),"after" + Address_Save, Toast.LENGTH_SHORT).show();
        }

        // getLatLonFromAddress("1/32 Alday St, St James WA 6102, Australia");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                search_address = "";
                status = "";
            } else {
                search_address = extras.getString("search_address");
                status = extras.getString("status");
            }
        } else {
            search_address = (String) savedInstanceState.getSerializable("search_address");
            status = (String) savedInstanceState.getSerializable("status");
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(UserActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessionUser = new SessionUser(this);
        UserDetail = sessionUser.getUserDetails();

        Log.e("UserDetail", "UserDetail Lat = " + UserDetail.get(SessionUser.KEY_LAT));
        Log.e("UserDetail", "UserDetail Lon = " + UserDetail.get(SessionUser.KEY_LON));
        Log.e("UserDetail", "UserDetail Address = " + UserDetail.get(SessionUser.KEY_HOME_ADDRESS));

        Address_Save = UserDetail.get(SessionUser.KEY_HOME_ADDRESS);
        saveLat = UserDetail.get(SessionUser.KEY_LAT);
        saveLon = UserDetail.get(SessionUser.KEY_LON);

        initComp();

        CommercialInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog callFeeDialog = new Dialog(UserActivity.this);
                callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                callFeeDialog.setContentView(R.layout.alert_dialog_comm_resi);

                final Button okBt = callFeeDialog.findViewById(R.id.OkayBTCR);
                TextView MyText = callFeeDialog.findViewById(R.id.MyText);
                MyText.setText("Commercial is any business e.g. Shop, Hospital, Restaurant, Industrial area");

                okBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callFeeDialog.dismiss();
                    }
                });
                callFeeDialog.show();
            }

        });

        ResidentialInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog callFeeDialog = new Dialog(UserActivity.this);
                callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                callFeeDialog.setContentView(R.layout.alert_dialog_comm_resi);
                final Button okBt = callFeeDialog.findViewById(R.id.OkayBTCR);
                TextView MyText = callFeeDialog.findViewById(R.id.MyText);
                MyText.setText("Residential / Domestic is any House, Apartment, Studio or Residential building");

                okBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callFeeDialog.dismiss();
                    }
                });

                callFeeDialog.show();

            }
        });

        ProfileIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Profile.class);
                i.putExtra("jobCount", String.valueOf(jobCount));
                startActivity(i);
            }
        });

        year = Calendar.getInstance().get(Calendar.YEAR);
        toolbar_title.setText("Job information");
        setSupportActionBar(dash_toolbar);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "DroidSerif.ttf");
        toolbar_title.setTypeface(custom_font);
        setdatetimeee.setTypeface(custom_font);
        tradetxt.setTypeface(custom_font);
        select_problem.setTypeface(custom_font);
        acceptbtn.setTypeface(custom_font);
        commbtn.setTypeface(custom_font);
        ressbtnn.setTypeface(custom_font);
        selecttrade.setTypeface(custom_font);
        pleseeseleccttttxtv.setTypeface(custom_font);
        select_date_tv.setTypeface(custom_font);
        time_set_tv.setTypeface(custom_font);

        acceptbtn.setTypeface(custom_font);

        uplayy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyDateAndTimePick.class);
                intent.putExtra("type", "date");
                UserActivity.this.startActivityForResult(intent, 1);
            }
        });

        select_date_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyDateAndTimePick.class);
                intent.putExtra("type", "date");
                UserActivity.this.startActivityForResult(intent, 1);
            }
        });

        select_time_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyDateAndTimePick.class);
                intent.putExtra("type", "time");
                UserActivity.this.startActivityForResult(intent, 2);
            }
        });

        SelectProblemInfoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog SPDialog = new Dialog(UserActivity.this);
                SPDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                SPDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                SPDialog.setContentView(R.layout.alert_dialog_comm_resi);
                final Button okBt = SPDialog.findViewById(R.id.OkayBTCR);
                TextView MyText = SPDialog.findViewById(R.id.MyText);
                MyText.setText("The problem list is here to help you identify the job you need fixing, if you have any other problems, or you need to inform the tradesman about the property, or you have any questions please let the tradesman know in the box below \"Do you have any job requests?\"");

                okBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPDialog.dismiss();
                    }
                });

                SPDialog.show();
            }
        });

        SelectTradeInfoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog STDialog = new Dialog(UserActivity.this);
                STDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                STDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                STDialog.setContentView(R.layout.alert_dialog_comm_resi);
                final Button okBt = STDialog.findViewById(R.id.OkayBTCR);
                TextView MyText = STDialog.findViewById(R.id.MyText);
                MyText.setText("Please select the trade you need to fix your job carefully, if you are not sure what trade you need, the problem list can help, otherwise select the trade you think is right and ask the tradesman a question in \"Do you have any job requests?\"");

                okBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        STDialog.dismiss();
                    }
                });

                STDialog.show();

            }
        });

        CallOutFeeInfoIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog callFeeDialog = new Dialog(UserActivity.this);
                callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                callFeeDialog.setContentView(R.layout.alert_dialog_call_out_fee);

                final Button okBt = callFeeDialog.findViewById(R.id.okBt);

                okBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callFeeDialog.dismiss();
                    }
                });
                callFeeDialog.show();
            }
        });

        YourAddressInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog YourAddDialog = new Dialog(UserActivity.this);
                YourAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                YourAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                YourAddDialog.setContentView(R.layout.alert_dialog_address);

                final Button OkayBT = YourAddDialog.findViewById(R.id.OkayBT);

                OkayBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YourAddDialog.dismiss();
                    }
                });

                YourAddDialog.show();

            }
        });

        OtherAddressInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog OtherAddDialog = new Dialog(UserActivity.this);
                OtherAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                OtherAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                OtherAddDialog.setContentView(R.layout.alert_dialog_other_address);

                final Button OkayBT = OtherAddDialog.findViewById(R.id.OkayBTOther);

                OkayBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OtherAddDialog.dismiss();
                    }
                });

                OtherAddDialog.show();

            }
        });

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTracker.setTitle("Find Tradesmen Button Clicked");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Hello Akash ID");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                boolean isError = false;

                if (Appconstants.Category.equals("")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select Category", Toast.LENGTH_SHORT).show();
                } else if (selecttrade.getText().toString().equalsIgnoreCase("SELECT TRADE")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select Trade", Toast.LENGTH_SHORT).show();
                } else if (select_problem.getText().toString().equalsIgnoreCase("Select Problem")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select problem", Toast.LENGTH_SHORT).show();
                } else if (selectAddress.equalsIgnoreCase("")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select address category", Toast.LENGTH_SHORT).show();
                }

                Log.e("sfasdasdas", "adasdasda = " + Appconstants.lat);
                Log.e("sfasdasdas", "adasdasda = " + Appconstants.lon);
                Log.e("sfasdasdas", "sfasdasdas = " + Appconstants.SITE_ADDRESS);

                if (!isError) {
                    if (address != null || !(address.equals(""))) {
                        Appconstants.SITE_ADDRESS = address;
                    }

                    Toast.makeText(getApplicationContext(), Appconstants.SITE_ADDRESS, Toast.LENGTH_SHORT).show();

                    Appconstants.DATE_SELECTED = selectDate;
                    Log.e("sfasdasdas", "Selected Adresss =>>>>> " + Appconstants.SITE_ADDRESS);
                    Appconstants.JOB_REQUEST = Any_Job_Request.getText().toString();
                    Intent i = new Intent(getApplicationContext(), SelectTrademan.class);
                    i.putExtra("address", Appconstants.SITE_ADDRESS);

                    if (sessionUser.IsLoggedIn()) {
                        i.putExtra("status", "login");
                    } else {
                        i.putExtra("status", "accept");
                    }
                    i.putExtra("select_status", "quick_search");
                    startActivity(i);
                }
            }

        });

        commbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.Category = "COMMERCIAL";
                commbtn.setBackgroundColor(Color.parseColor(redcolor));
                commbtn.setTextColor(Color.parseColor("#ffffff"));
                ressbtnn.setBackgroundColor(Color.parseColor("#e8e8e7"));
                ressbtnn.setTextColor(Color.parseColor("#000000"));
            }
        });

        enterpostaladress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddress = "your address";
//              Toast.makeText(gpsTracker, "Hello", Toast.LENGTH_SHORT).show();
//              enterpostaladress.setBackgroundResource(R.drawable.outline_blue_10);
//              enter_adddress.setTextColor(Color.parseColor("#ffffff"));
//              findnearbylayyy.setBackgroundResource(R.drawable.outline_round_10);
//              YourAddressTV.setTextColor(Color.parseColor("#000000"));
                Intent intent = new Intent(UserActivity.this, EnterDifferentAddress.class);
                startActivityForResult(intent, 10);
            }
        });

        findnearbylayyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectAddress = "your address";
                boolean isError = false;

                if (Appconstants.Category.equals("")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select Category", Toast.LENGTH_SHORT).show();
                } else if (selecttrade.getText().toString().equalsIgnoreCase("SELECT TRADE")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select Trade", Toast.LENGTH_SHORT).show();
                } else if (select_problem.getText().toString().equalsIgnoreCase("Select Problem")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select problem", Toast.LENGTH_SHORT).show();
                } else if (selectAddress.equalsIgnoreCase("")) {
                    isError = true;
                    Toast.makeText(getApplicationContext(), "Please select address category ", Toast.LENGTH_SHORT).show();
                }

                if (!isError) {

                    Appconstants.JOB_REQUEST = Any_Job_Request.getText().toString();
                    selectAddress = "your address";
                    Appconstants.WHICH_TYPE_ADDRESS = "Main Address";

                    Appconstants.SITE_ADDRESS = UserDetail.get(SessionUser.KEY_HOME_ADDRESS) + ", " + UserDetail.get(SessionUser.KEY_POSTCODE) + ", " + UserDetail.get(SessionUser.KEY_CITY);
                    Log.e("site address", Appconstants.SITE_ADDRESS);
                    Appconstants.PERSON_ON_SITE = UserDetail.get(SessionUser.KEY_NAME);
                    Appconstants.SITE_HOME_NUMBER = UserDetail.get(SessionUser.KEY_HOMEPHONE);
                    Appconstants.SITE_MOBILE_NUMBER = UserDetail.get(SessionUser.KEY_MOBILEPHONE);
                    Appconstants.ServiceLocation = Address_Save;

                    try {
                        Appconstants.lat = Double.parseDouble(saveLat);
                        Appconstants.lon = Double.parseDouble(saveLon);
                    } catch (Exception e) {
                        Appconstants.lat = 0.0;
                        Appconstants.lon = 0.0;
                    }

                    Appconstants.DATE_SELECTED = selectDate;

                    Appconstants.RELATION_ID = "";
                    Appconstants.RELATION_STATUS = "";

                    Log.e("DATE_SELECTed123", "User Activity = " + Appconstants.DATE_SELECTED);

                    findnearbylayyy.setBackgroundResource(R.drawable.outline_blue_10);
                    YourAddressTV.setTextColor(Color.parseColor("#ffffff"));

                    enterpostaladress.setBackgroundResource(R.drawable.outline_round_10);
                    enter_adddress.setTextColor(Color.parseColor("#000000"));

                }

                //                if (status.equals("login")) {
//
//
//
//                } else {
//                    AddressAlertDailoge();
//                }

            }
        });

        ressbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.Category = "RESIDENTIAL";

                ressbtnn.setBackgroundColor(Color.parseColor(redcolor));
                ressbtnn.setTextColor(Color.parseColor("#ffffff"));
                commbtn.setBackgroundColor(Color.parseColor("#e8e8e7"));
                commbtn.setTextColor(Color.parseColor("#000000"));
            }
        });

        selectproblembackkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (subcategoryArray.length > 0) {

                    final ArrayList<String> ProblemList = new ArrayList<String>(Arrays.asList(subcategoryArray));

                    for (int i = 0; i < ProblemList.size(); i++) {
                        Log.e("old ProblemList", ProblemList.get(i));
                    }
                    String carName = "Also See";
                    String carName2 = "other";
                    HashMap<Integer, String> hm = new HashMap<Integer, String>();
                    ArrayList problemIndexList = new ArrayList();
                    int index = -1;
                    for (int i = 0; i < subcategoryArray.length; i++) {
                        if (subcategoryArray[i].toLowerCase().contains(carName.toLowerCase())) {
                            index = i;
                            hm.put(index, subcategoryArray[i]);
                            problemIndexList.add(index);
                        }

                        if (subcategoryArray[i].toLowerCase().contains(carName2.toLowerCase())) {
                            index = i;
                            hm.put(index, subcategoryArray[i]);
                            problemIndexList.add(index);
                        }

                    }
                    int value = -1;
                    for (int i = problemIndexList.size() - 1; i >= 0; i--) {
                        value = (int) problemIndexList.get(i);
                        ProblemList.remove(value);
                    }

                    for (int i = 0; i < ProblemList.size(); i++) {
                        Log.e("New ProblemList", ProblemList.get(i));
                    }
                    SelectProblemDailog = new Dialog(UserActivity.this);
                    SelectProblemDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    SelectProblemDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    SelectProblemDailog.setContentView(R.layout.select_problem_alert_dailog);

                    NonScrollListView SelectProblemList = SelectProblemDailog.findViewById(R.id.SelectProblemList);
                    Button CancelProblemTV = SelectProblemDailog.findViewById(R.id.CancelProblemTV);
                    Button AcceptProblemTV = SelectProblemDailog.findViewById(R.id.AcceptProblemTV);
                    Selecteddata = new ArrayList<String>();

                    LinearLayout LL = SelectProblemDailog.findViewById(R.id.LL);

                    for (int i = 0; i < hm.size(); i++) {
                        TextView textView = new TextView(UserActivity.this);
                        textView.setText(hm.get(problemIndexList.get(i)));
                        textView.setTextSize(16);
                        textView.setTextColor(Color.parseColor("#FF0000"));
                        textView.setPadding(0, 10, 0, 10);
                        LL.addView(textView);
                    }

                    ListViewAdapter adapter2 = new ListViewAdapter(UserActivity.this, ProblemList);
                    SelectProblemList.setAdapter(adapter2);
                    SelectProblemList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    SelectProblemList.setItemsCanFocus(false);

                    CancelProblemTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SelectProblemDailog.dismiss();
                        }
                    });

                    AcceptProblemTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Selecteddata.isEmpty()) {
                            } else {
                                StringBuilder s = new StringBuilder();
                                for (int i = 0; i < Selecteddata.size(); i++) {
                                    if (i == 0) {
                                        s.append(Selecteddata.get(i));
                                        continue;
                                    }
                                    s.append("," + Selecteddata.get(i));
                                }
                                select_problem.setPadding(25, 20, 0, 20);
                                String AllFollowerID = s.toString();
                                Log.e("AllFollowerID", "??" + AllFollowerID);
                                select_problem.setText(AllFollowerID);
                                Appconstants.PROBLEM_SELECTED = AllFollowerID;
                                select_problem.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                selectproblembackkk.setBackgroundColor(Color.parseColor(redcolor));
                            }
                            SelectProblemDailog.dismiss();
                        }

                    });

                    SelectProblemDailog.show();

                } else {
                    Toast.makeText(getApplicationContext(), "No subcategories found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sellecttttradinglayyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog SelectTradeDailog = new Dialog(UserActivity.this);
                SelectTradeDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                SelectTradeDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                SelectTradeDailog.setContentView(R.layout.select_trade_alert_dailog);
                ListView SelectTradeList = SelectTradeDailog.findViewById(R.id.SelectTradeList);
                Button CancelTV = SelectTradeDailog.findViewById(R.id.CancelTV);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserActivity.this, R.layout.select_trade_row_item, mainCategoryArray);
                SelectTradeList.setAdapter(adapter);

                CancelTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectTradeDailog.dismiss();
                    }
                });

                SelectTradeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        selecttrade.setText(mainCategoryArray[position]);
                        String id2 = posID[position];
                        Appconstants.PREVIOUSTRADE_SELECTED = mainCategoryArray[position];
                        Appconstants.PROBLEM_SELECTED = "";
                        selecttrade.setTextColor(Color.WHITE);
                        sellecttttradinglayyy.setBackgroundColor(Color.parseColor(redcolor));

                        select_problem.setText("Select Problem");
                        select_problem.setPadding(20, 20, 0, 20);
                        // select_problem.setHeight(80);
                        select_problem.setTextColor(Color.BLACK);
                        selectproblembackkk.setBackgroundResource(R.drawable.roundedcorners_gray);

                        if (InternetDetect.isConnected(getApplicationContext())) {
                            new CatrequestById(id2).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Check Inrternet Connection", Toast.LENGTH_SHORT).show();
                        }
                        SelectTradeDailog.dismiss();
                    }
                });

                SelectTradeDailog.show();

            }
        });

        if (InternetDetect.isConnected(this)) {
            new AsyncCategory().execute();
            new JsonGetAllJobs().execute();
        } else {
            Toast.makeText(UserActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
    }

    private void getLatLonFromAddress(String address) {
        String postReceiverUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                address.replace(",", "+") + "&key=" + getResources().getString(R.string.places_api_key);
        Log.e("asdasdasds", "Plcaes Url = " + postReceiverUrl);
        AndroidNetworking.get(postReceiverUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.e("responseresponse", response);
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray resultArray = mainObj.getJSONArray("results");
                    JSONObject resultFirstObj = resultArray.getJSONObject(0);
                    JSONObject geometryObj = resultFirstObj.getJSONObject("geometry");
                    JSONObject locationObj = geometryObj.getJSONObject("location");

                    double lat = locationObj.getDouble("lat");
                    double lon = locationObj.getDouble("lng");

                    Log.e("sfsdfsdfsdf", "lat = " + lat);
                    Log.e("sfsdfsdfsdf", "lon = " + lon);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.e("responseresponse", "anError = " + anError.getErrorBody());
                Log.e("responseresponse", "anError = " + anError.getErrorDetail());
            }
        });
    }

    private void AddressAlertDailoge() {

        done_text = findViewById(R.id.done_text);
        activity_main = findViewById(R.id.activity_main);
        activity_main.setVisibility(View.VISIBLE);

        done_text = findViewById(R.id.done_text);
        cancle_text = findViewById(R.id.cancle_text);
        select_address = findViewById(R.id.select_address);
        text_select_address = findViewById(R.id.text_select_address);

        if (!Address_Save.equals("")) {
            text_select_address.setText(Address_Save);
        }

        select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserActivity.this, GooglePlacesAutocompleteActivity.class);
                startActivity(in);
            }
        });

        done_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_main.setVisibility(View.GONE);
            }
        });

        cancle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_main.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "UserActivity");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "UserActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        Log.e("asfdasdasdasdas", "Appconstants.ServiceLocation = " + Appconstants.ServiceLocation);
        Log.e("asfdasdasdasdas", "Appconstants.lat = " + Appconstants.lat);
        Log.e("asfdasdasdasdas", "Appconstants.lon = " + Appconstants.lon);

        text_select_address = findViewById(R.id.text_select_address);

        Address_Save = PreferenceConnector.readString(UserActivity.this, PreferenceConnector.Address_Save, "");

        if (!Address_Save.equals("")) {
            text_select_address.setText(Address_Save);
        }

    }

    private void initComp() {

        findnearbylayyy = findViewById(R.id.findnearbylayyy);
        selectproblembackkk = findViewById(R.id.selectproblembackkk);
        selectproblembackkk.setEnabled(false);
        enterpostaladress = findViewById(R.id.enterpostaladress);
        select_date_rl = findViewById(R.id.select_date_rl);
        select_time_rl = findViewById(R.id.select_time_rl);
        uplayy1 = findViewById(R.id.uplayy1);
        dash_toolbar = findViewById(R.id.dash_toolbar);
        commbtn = findViewById(R.id.commbtn);
        ressbtnn = findViewById(R.id.ressbtnn);
        pleseeseleccttttxtv = findViewById(R.id.pleseeseleccttttxtv);
        tradetxt = findViewById(R.id.tradetxt);
        select_problem = findViewById(R.id.select_problem);
        enter_adddress = findViewById(R.id.enter_adddress);
        acceptbtn = findViewById(R.id.acceptbtn);
        setdatetimeee = findViewById(R.id.setdatetimeee);
        selecttrade = findViewById(R.id.selecttrade);
        YourAddressTV = findViewById(R.id.YourAddressTV);
        sellecttttradinglayyy = findViewById(R.id.sellecttttradinglayyy);
        sellecttttradinglayyy.setEnabled(false);
        subprogress = findViewById(R.id.subprogress);
        CallOutFeeInfoIM = findViewById(R.id.CallOutFeeInfoIM);
        Any_Job_Request = findViewById(R.id.Any_Job_Request);
        toolbar_title = dash_toolbar.findViewById(R.id.ToolbarTitleDash);
        NavigationUpIM = findViewById(R.id.NavigationUpDashIM);
        ProfileIM = findViewById(R.id.ProfileDashIM);
        YourAddressInfo = findViewById(R.id.YourAddressInfo);
        OtherAddressInfo = findViewById(R.id.OtherAddressInfo);
        CommercialInfo = findViewById(R.id.CommercialInfo);
        ResidentialInfo = findViewById(R.id.ResidentialInfo);
        JobCountTV = dash_toolbar.findViewById(R.id.JobCountTV);
        SelectTradeInfoRL = findViewById(R.id.SelectTradeInfoRL);
        SelectProblemInfoRL = findViewById(R.id.SelectProblemInfoRL);
        JobCountTV.setVisibility(View.VISIBLE);
        Loginuserid = sessionUser.getId();
        select_date_tv = findViewById(R.id.select_date_tv);
        time_set_tv = findViewById(R.id.time_set_tv);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                startActivity(new Intent(mContext, MainActivity.class));
            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                select_date_rl.setBackgroundColor(getResources().getColor(R.color.skyblue));
                select_date_tv.setText("DATE : " + Appconstants.DATE_SELECTED);
                selectDate = Appconstants.DATE_SELECTED;
                Log.e("DATE_SELECTed123", "User onActivityResult = " + Appconstants.DATE_SELECTED);
                select_date_tv.setTextColor(Color.WHITE);
            }

            if (requestCode == 2) {
                select_time_rl.setBackgroundColor(getResources().getColor(R.color.skyblue));
                time_set_tv.setText(" TIME : " + Appconstants.TIME_SELECTED);
                time_set_tv.setTextColor(Color.WHITE);
            }

            if (requestCode == 10) {
                selectAddress = "different";
//                Appconstants.ServiceLocation = data.getStringExtra("add");
//                Appconstants.lat = data.getDoubleExtra("lat",0.0);
//                Appconstants.lon = data.getDoubleExtra("lon",0.0);

                Log.e("dasdasfsfdas", "ServiceLocation = " + data.getStringExtra("add"));
                Log.e("dasdasfsfdas", "Appconstants.lat = " + data.getDoubleExtra("lat", 0.0));
                Log.e("dasdasfsfdas", "Appconstants.lon = " + data.getDoubleExtra("lon", 0.0));
                // Toast.makeText(UserActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                enterpostaladress.setBackgroundResource(R.drawable.outline_blue_10);
                enter_adddress.setTextColor(Color.parseColor("#ffffff"));
                findnearbylayyy.setBackgroundResource(R.drawable.outline_round_10);
                YourAddressTV.setTextColor(Color.parseColor("#000000"));
            }

        }

    }

    public class CatrequestById extends AsyncTask<String, Void, String> {

        String id;

        CatrequestById(String id) {
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            subcatarraylist.clear();
            subprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "sub_category_byid&category_id=" + id);

            try {
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                Log.e("Subcategory" + id, result);
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getJSONObject(i).getString("name");
                    subcatarraylist.add(name);
                }
            } catch (Exception e) {

                System.out.println("errror in subcat" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            subprogress.setVisibility(View.GONE);
            subcategoryArray = null;
            subcategoryArray = new String[subcatarraylist.size()];
            subcategoryArray = subcatarraylist.toArray(subcategoryArray);
            for (String sub : subcategoryArray) {
                System.out.println("sub++++" + sub);
            }

            String carName = "Also See";
            int index = -1;
            for (int i = 0; i < subcategoryArray.length; i++) {
                if (subcategoryArray[i].toLowerCase().contains(carName.toLowerCase())) {
                    index = i;

                    Log.e("Index value", String.valueOf(index));
                    Log.e("Index value", subcategoryArray[i]);
                    continue;
                }
            }

            selectproblembackkk.setEnabled(true);
        }
    }

    private class AsyncCategory extends AsyncTask<String, Void, String> {

        String id = "", name = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            catarray.clear();
            subprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "category");
            try {
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());

                Log.e("CAtegory", result);
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    CategoryBean categoryBean = new CategoryBean();

                    id = jsonArray.getJSONObject(i).getString("id");
                    name = jsonArray.getJSONObject(i).getString("name");

                    categoryBean.setId(id);
                    categoryBean.setName(name);

                    catarray.add(categoryBean);

                }

            } catch (Exception e) {

                System.out.println("json category error+++" + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            subprogress.setVisibility(View.GONE);

            sellecttttradinglayyy.setEnabled(true);

            Collections.sort(catarray, new Comparator<CategoryBean>() {
                @Override
                public int compare(CategoryBean v1, CategoryBean v2) {
                    return v1.getName().compareTo(v2.getName());
                }
            });

            mainCategoryArray = null;
            posID = null;
            mainCategoryArray = new String[catarray.size()];
            posID = new String[catarray.size()];

            int index = 0;

            for (CategoryBean value : catarray) {
                mainCategoryArray[index] = value.getName();
                posID[index] = value.getId();
                index++;
                System.out.println("Main category" + value.getName());
            }

        }

    }

    private class ListViewAdapter extends BaseAdapter {

        private final ArrayList<String> data;
        Context mContext;
        LayoutInflater inflater;

        public ListViewAdapter(Context context, ArrayList<String> data) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.select_problem_row_list, null);
                holder.ProblemNameTV = view.findViewById(R.id.ProblemNameTV);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            // Set the results into TextViews
            holder.ProblemNameTV.setText(data.get(position));
            holder.ProblemNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.ProblemNameTV.isChecked()) {
                        holder.ProblemNameTV.setChecked(false);
                        Selecteddata.remove(data.get(position));
                    } else {
                        if (Selecteddata.size() <= 3) {
                            holder.ProblemNameTV.setChecked(true);
                            Selecteddata.add(data.get(position));
                        } else {
                            Toast.makeText(UserActivity.this, "Please Select any 4 problems", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            return view;
        }

        public class ViewHolder {
            CheckedTextView ProblemNameTV;
        }
    }

    private class JsonGetAllJobs extends AsyncTask<String, String, List<PendingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<PendingRequestBean> doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_bookingList_by_userid&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", sessionUser.getId());

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
                Log.e("JsonPendingRequest", response);

                List<PendingRequestBean> pendingRequestListBeanList = new ArrayList<PendingRequestBean>();

                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    PendingRequestBean pendingRequestBean = new PendingRequestBean();
                    pendingRequestBean.setId(object.getString("id"));
                    pendingRequestBean.setBusiness_name(object.getString("business_name"));
                    pendingRequestBean.setTrading_name(object.getString("trading_name"));
                    pendingRequestBean.setOffice_no(object.getString("office_no"));
                    pendingRequestBean.setMobile_no(object.getString("mobile_no"));
                    pendingRequestBean.setEmail(object.getString("email"));
                    pendingRequestBean.setWebsite_url(object.getString("website_url"));
                    pendingRequestBean.setAfter_hours(object.getString("after_hours"));
                    pendingRequestBean.setSelect_trade(object.getString("select_trade"));

                    JSONArray ProblemArray = object.getJSONArray("problem");
                    JSONObject ProblemObject = ProblemArray.getJSONObject(0);

                    PendingRequestBean.Problem problem = new PendingRequestBean.Problem();
                    problem.setId(ProblemObject.getString("id"));
                    problem.setDate(ProblemObject.getString("date"));
                    problem.setTime(ProblemObject.getString("time"));
                    problem.setIsDateFlexible(ProblemObject.getString("IsDateFlexible"));
                    problem.setIsTimeFlexible(ProblemObject.getString("IsTimeFlexible"));
                    problem.setPersonOnSite(ProblemObject.getString("PersonOnSite"));
                    problem.setJob_Address(ProblemObject.getString("Job_Address"));
                    problem.setHome_Number(ProblemObject.getString("Home_Number"));
                    problem.setMobile_Number(ProblemObject.getString("Mobile_Number"));
                    problem.setJob_Request(ProblemObject.getString("Job_Request"));
                    problem.setUser_id(ProblemObject.getString("user_id"));
                    problem.setTradesman_id(ProblemObject.getString("Tradesman_id"));
                    problem.setProblem(ProblemObject.getString("problem"));
                    problem.setOrder_status(ProblemObject.getString("order_status"));
                    problem.setSchedule(ProblemObject.getString("schedule"));
                    pendingRequestBean.setProblem(problem);
                    pendingRequestListBeanList.add(pendingRequestBean);
                }

                return pendingRequestListBeanList;

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
        protected void onPostExecute(List<PendingRequestBean> result) {
            super.onPostExecute(result);

            if (result == null) {

            } else if (result.isEmpty()) {

            } else {

                String index = null;

                for (PendingRequestBean bean : result) {
                    if (bean.getProblem().getOrder_status().equalsIgnoreCase("COMPLETED")) {
                        index = String.valueOf(result.indexOf(bean));
                        break;
                    }
                }

                if (!(index == null)) {

                    Intent intent = new Intent(UserActivity.this, RatingSystem.class);

                    intent.putExtra("TradesmanId", result.get(Integer.parseInt(index)).getProblem().getTradesman_id());
                    intent.putExtra("ProblemId", result.get(Integer.parseInt(index)).getProblem().getId());
                    startActivity(intent);

                } else {

                }

                jobCount = result.size();
                String jobCountStr = String.valueOf(jobCount);
                JobCountTV.setText(jobCountStr);

            }

        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(mContext, MainActivity.class));
    }

}


