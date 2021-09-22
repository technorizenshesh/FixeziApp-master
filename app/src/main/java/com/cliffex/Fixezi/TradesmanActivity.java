package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.MyUtils.MyFontTextView;
import com.cliffex.Fixezi.activities.EmployeeTradeHomeAct;
import com.cliffex.Fixezi.util.IabBroadcastReceiver;
import com.cliffex.Fixezi.util.IabHelper;
import com.cliffex.Fixezi.util.IabResult;
import com.cliffex.Fixezi.util.Inventory;
import com.cliffex.Fixezi.util.Purchase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TradesmanActivity extends AppCompatActivity implements
        IabBroadcastReceiver.IabBroadcastListener {

    static final String TAG = "fixezi";
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
    Toolbar tradesman_profile_toolbar;
    TextView toolbar_title;
    MyFontTextView tvEmployeeJobs;
    Context mContext = TradesmanActivity.this;
    TextView ContinueAsUserTV, IncomingJobTV, ScheduleJobsTV, RescheduleJobTV, AllJobsTV, SignInAsUserTV, DialyJobTV, CompleteJobTV;
    CheckBox CheckboxSigninAsUser;
    SessionTradesman sessionTradesman;
    ImageView ProfileDashIM;
    RelativeLayout NavigationUpIM;
    Button ActiveBt, OnHoldBt;
    String BusyStatus = "";
    TextView MessageTV2;
    TextView IncReqCountTV, tv_jobPending, tv_completedJOb, tv_allJOb;
    ImageView MinimizeIM, MaximizeIM;
    TextView JobsPendingTV;
    int count, countcomplete, countAccepted, all;
    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;
    // Tracks the currently owned infinite gas SKU, and the options in the Manage dialog
    String mInfiniteGasSku = "";
    // Used to select between purchasing gas on a monthly or yearly basis
    String mSelectedSubscriptionPeriod = "";
    // The helper object
    IabHelper mHelper;

    IabBroadcastReceiver mBroadcastReceiver;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String WhichPlan = "";
    private FirebaseAnalytics mFirebaseAnalytics;

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            if (mHelper == null) return;

            //
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // First find out which subscription is auto renewing
            Purchase economyPlan = inventory.getPurchase(Appconstants.SKU_ECONOMY);
            Purchase fullPlan = inventory.getPurchase(Appconstants.SKU_FUll);

            if (economyPlan != null) {
                mInfiniteGasSku = Appconstants.SKU_ECONOMY;

            } else if (fullPlan != null) {
                mInfiniteGasSku = Appconstants.SKU_FUll;

            } else {
                mInfiniteGasSku = "";

            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteGas = (economyPlan != null && verifyDeveloperPayload(economyPlan))
                    || (fullPlan != null && verifyDeveloperPayload(fullPlan));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) {

                if (mInfiniteGasSku.equalsIgnoreCase("")) {
                    saveData("Pay Per Job");
                } else {
                    saveData(mInfiniteGasSku);
                }

            } else {
                saveData("Pay Per Job");
            }
            Log.e(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    private LinearLayout emegancy_callout_notes;
    private RelativeLayout emergency_callouttext;
    private String AfterHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman);
        Appconstants.AdminType = "Tradesman";
        Appconstants.afterhours = "yes";
        Intent intent = getIntent();
        AfterHour = intent.getStringExtra("AfterHour");
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Hello Akash IDdsas");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        SetuUi();

        sharedPreferences = getApplicationContext().getSharedPreferences("FixeziPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadData();

        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, Appconstants.base64EncodedPublicKey);

        // Toast.makeText(this, AfterHour, Toast.LENGTH_SHORT).show();

        mHelper.enableDebugLogging(true);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                mBroadcastReceiver = new IabBroadcastReceiver(TradesmanActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });

        sessionTradesman = new SessionTradesman(this);
        tradesman_profile_toolbar = (Toolbar) findViewById(R.id.tradesman_profile_toolbar);
        toolbar_title = (TextView) findViewById(R.id.ToolbarTitleDash);
        CheckboxSigninAsUser = (CheckBox) findViewById(R.id.CheckboxSigninAsUser);
        ContinueAsUserTV = (TextView) findViewById(R.id.ContinueAsUserTV);
        ScheduleJobsTV = (TextView) findViewById(R.id.ScheduleJobsTV);
        DialyJobTV = (TextView) findViewById(R.id.DialyJobTV);
        RescheduleJobTV = (TextView) findViewById(R.id.RescheduleJobTV);
        CompleteJobTV = (TextView) findViewById(R.id.CompleteJobTV);
        IncomingJobTV = (TextView) findViewById(R.id.IncomingJobTV);
        AllJobsTV = (TextView) findViewById(R.id.AllJobsTV);
        SignInAsUserTV = (TextView) findViewById(R.id.SignInAsUserTV);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpDashIM);
        ProfileDashIM = (ImageView) findViewById(R.id.ProfileDashIM);
        MessageTV2 = (TextView) findViewById(R.id.MessageTV2);
        IncReqCountTV = (TextView) findViewById(R.id.IncReqCountTV);
        tv_jobPending = (TextView) findViewById(R.id.tv_jobPending);
        tv_allJOb = (TextView) findViewById(R.id.tv_allJOb);
        tv_completedJOb = (TextView) findViewById(R.id.tv_completedJOb);
        MinimizeIM = (ImageView) findViewById(R.id.MinimizeIM);
        MaximizeIM = (ImageView) findViewById(R.id.MaximizeIM);
        JobsPendingTV = (TextView) findViewById(R.id.JobsPendingTV);
        OnHoldBt = (Button) findViewById(R.id.OnHoldBt);
        ActiveBt = (Button) findViewById(R.id.ActiveBt);
        tvEmployeeJobs = findViewById(R.id.tvEmployeeJobs);

        emegancy_callout_notes = (LinearLayout) findViewById(R.id.emegancy_callout_notes);

        if (Appconstants.afterhours.equals("no")) {
            emegancy_callout_notes.setVisibility(View.GONE);
        } else {
            emegancy_callout_notes.setVisibility(View.VISIBLE);
        }

        if (sessionTradesman.getKeyBusystatus().equalsIgnoreCase("0")) {

            ActiveBt.setTextColor(Color.parseColor("#ffffff"));
            ActiveBt.setBackgroundResource(R.drawable.border_black_solid_green);

            OnHoldBt.setTextColor(Color.parseColor("#000000"));
            OnHoldBt.setBackgroundResource(R.drawable.border_black_solid_white);

        } else if (sessionTradesman.getKeyBusystatus().equalsIgnoreCase("1")) {
            OnHoldBt.setTextColor(Color.parseColor("#000000"));
            OnHoldBt.setBackgroundResource(R.drawable.border_black_solid_yellow_two);

            ActiveBt.setTextColor(Color.parseColor("#000000"));
            ActiveBt.setBackgroundResource(R.drawable.border_black_solid_white);
        }

        MinimizeIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaximizeIM.setVisibility(View.VISIBLE);
                MinimizeIM.setVisibility(View.GONE);
                MessageTV2.setVisibility(View.GONE);
            }
        });

        MaximizeIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageTV2.setVisibility(View.VISIBLE);
                MinimizeIM.setVisibility(View.VISIBLE);
                MaximizeIM.setVisibility(View.GONE);
            }
        });

        tvEmployeeJobs.setOnClickListener(v -> {
            startActivity(new Intent(mContext, EmployeeTradeHomeAct.class));
        });

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                startActivity(new Intent(mContext, MainActivity.class));
            }
        });

        ProfileDashIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), TradesmanProfile.class);
                startActivity(i);
            }
        });

        toolbar_title.setText("Admin  Dashboard");

        setSupportActionBar(tradesman_profile_toolbar);

        AllJobsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradesmanActivity.this, AllJobs.class);
                startActivity(intent);
            }
        });

        ScheduleJobsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradesmanActivity.this, ScheduleNew.class);
                startActivity(intent);
            }
        });

        RescheduleJobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TradesmanActivity.this, RescheduleJobs.class);
                startActivity(intent);

            }
        });

        JobsPendingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TradesmanActivity.this, PendingJobs.class);
                startActivity(intent);

            }
        });

        CompleteJobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradesmanActivity.this, CompletedJob.class);
                startActivity(intent);
            }
        });

        DialyJobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());

                ArrayList<String> stringArrayList = new ArrayList<String>();
                stringArrayList.add(formattedDate);

                Intent intent = new Intent(TradesmanActivity.this, ScheduleNewDetail.class);
                intent.putStringArrayListExtra("DateList", stringArrayList);
                intent.putExtra("Position", 0);
                startActivity(intent);

            }
        });

        IncomingJobTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradesmanActivity.this, IncomingJobRequest.class);
                startActivity(intent);
            }
        });

        ContinueAsUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Onclick", "yes");

                if (CheckboxSigninAsUser.isChecked()) {

                    Intent intent = new Intent(TradesmanActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(TradesmanActivity.this, "Please check the box", Toast.LENGTH_SHORT).show();

                }
            }
        });

        ActiveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog postalDialog = new Dialog(TradesmanActivity.this);
                postalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                postalDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                postalDialog.setContentView(R.layout.alert_dialog_general);

                Button cancelBt = (Button) postalDialog.findViewById(R.id.cancelBtGeneral);
                Button acceptBt = (Button) postalDialog.findViewById(R.id.acceptBtGeneral);
                TextView MessageTV = (TextView) postalDialog.findViewById(R.id.MessageTV);
                MessageTV.setText("Do you wish to remove the hold on your company ?");

                cancelBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postalDialog.dismiss();
                    }
                });

                acceptBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postalDialog.dismiss();
                        final Dialog InnerDialog = new Dialog(TradesmanActivity.this);
                        InnerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        InnerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        InnerDialog.setContentView(R.layout.alert_dialog_general);

                        Button cancelBt = (Button) InnerDialog.findViewById(R.id.cancelBtGeneral);
                        Button acceptBt = (Button) InnerDialog.findViewById(R.id.acceptBtGeneral);

                        cancelBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InnerDialog.dismiss();

                            }
                        });

                        acceptBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new JsonTaskBusyFree().execute("nobzy");
                                InnerDialog.dismiss();
                            }
                        });

                        InnerDialog.show();

                    }
                });

                postalDialog.show();
            }
        });

        OnHoldBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog postalDialog = new Dialog(TradesmanActivity.this);
                postalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                postalDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                postalDialog.setContentView(R.layout.alert_dialog_too_busy);

                Button cancelBt = (Button) postalDialog.findViewById(R.id.cancelBt);
                Button acceptBt = (Button) postalDialog.findViewById(R.id.acceptBt);


                cancelBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postalDialog.dismiss();

                    }
                });


                acceptBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        new JsonTaskBusyFree().execute("bzy");
                        postalDialog.dismiss();
                    }
                });

                postalDialog.show();

            }
        });

        if (InternetDetect.isConnected(this)) {
            new JsonGetAllJob().execute();
        } else {
            Toast.makeText(TradesmanActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void SetuUi() {

        emergency_callouttext = (RelativeLayout) findViewById(R.id.emergency_callouttext);
        MessageTV2 = (TextView) findViewById(R.id.MessageTV2);

        if ("no".equals(AfterHour)) {
            emergency_callouttext.setVisibility(View.GONE);
            MessageTV2.setVisibility(View.GONE);
        }

    }

    private class JsonGetInfo extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog.show();
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
            // progressDialog.dismiss();

            if (jsonresult.equalsIgnoreCase("successfully")) {

                Log.e("GetInfo Success", "yes");
                Log.e("Value1", result.getAfter_hours());
                Log.e("Value2", result.getWork_location());

                if (result.getAfter_hours().equalsIgnoreCase("yes")) {
                    AfterHour = "yes";
                    emegancy_callout_notes.setVisibility(View.VISIBLE);
                } else if (result.getAfter_hours().equalsIgnoreCase("no")) {
                    AfterHour = "no";
                    emegancy_callout_notes.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActiveOrNot();
        if (InternetDetect.isConnected(this)) {
            new JsonGetInfo().execute();
            new JsonIncomingReq().execute();
        } else {
            Toast.makeText(TradesmanActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        startActivity(new Intent(mContext, MainActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void ActiveOrNot() {

        SessionTradesman SessionTwo = new SessionTradesman(TradesmanActivity.this);
        BusyStatus = SessionTwo.getKeyBusystatus();


        if (BusyStatus.equalsIgnoreCase("")) {

        } else if (BusyStatus.equalsIgnoreCase("0")) {

            ActiveBt.setTextColor(Color.parseColor("#ffffff"));
            ActiveBt.setBackgroundResource(R.drawable.border_black_solid_green);

            OnHoldBt.setTextColor(Color.parseColor("#000000"));
            OnHoldBt.setBackgroundResource(R.drawable.border_black_solid_white);


        } else if (BusyStatus.equalsIgnoreCase("1")) {

            OnHoldBt.setTextColor(Color.parseColor("#000000"));
            OnHoldBt.setBackgroundResource(R.drawable.border_black_solid_yellow_two);

            ActiveBt.setTextColor(Color.parseColor("#000000"));
            ActiveBt.setBackgroundResource(R.drawable.border_black_solid_white);

        }

    }

    @Override
    public void receivedBroadcast() {
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        Log.e("payload", "???" + payload);

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    void complain(String message) {
        Log.e(TAG, "Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveData(String whichplan) {
        editor.putString("WhichPlan", whichplan);
        editor.apply();


    }

    void loadData() {

        WhichPlan = sharedPreferences.getString("WhichPlan", "Pay Per Job");
        Log.e("WhichPlan", WhichPlan);

    }

    private class JsonTaskBusyFree extends AsyncTask<String, Void, String> {

        String result;

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "bzystatus_change_by_tradesman&tradesman_id=" + sessionTradesman.getId() + "&status=" + params[0]);
            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                JSONArray jsonArray = new JSONArray(obj);
                JSONObject object = jsonArray.getJSONObject(0);
                result = object.getString("result");


            } catch (Exception e) {
                System.out.println("errror in Forget task " + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (result.equalsIgnoreCase("bzy")) {

                sessionTradesman.updateBusyStatus("1");

                Toast.makeText(TradesmanActivity.this, "Busy", Toast.LENGTH_SHORT).show();
                ActiveOrNot();

            } else if (result.equalsIgnoreCase("no bzy")) {

                sessionTradesman.updateBusyStatus("0");

                Toast.makeText(TradesmanActivity.this, "Free", Toast.LENGTH_SHORT).show();
                ActiveOrNot();

            }

        }
    }

    private class JsonGetAllJob extends AsyncTask<String, String, List<IncomingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "get_all_product_detaits&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("Tradesman_id", sessionTradesman.getId());

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
                Log.e("JsonAllJob", response);

                List<IncomingRequestBean> incomingRequestListBeanList = new ArrayList<IncomingRequestBean>();

                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    IncomingRequestBean incomingRequestBean = new IncomingRequestBean();
                    incomingRequestBean.setId(object.getString("id"));
                    incomingRequestBean.setName(object.getString("name"));
                    incomingRequestBean.setPost_code(object.getString("post_code"));
                    incomingRequestBean.setCity(object.getString("city"));
                    incomingRequestBean.setState(object.getString("state"));
                    incomingRequestBean.setHome_phone(object.getString("home_phone"));
                    incomingRequestBean.setWork_phone(object.getString("work_phone"));
                    incomingRequestBean.setMobile_phone(object.getString("mobile_phone"));
                    incomingRequestBean.setEmail(object.getString("email"));
                    incomingRequestBean.setTradesman(object.getString("tradesman"));
                    incomingRequestBean.setUsername(object.getString("username"));
                    incomingRequestBean.setStreet(object.getString("street"));
                    incomingRequestBean.setHousenoo(object.getString("housenoo"));

                    JSONArray ProblemArray = object.getJSONArray("problem");
                    JSONObject ProblemObject = ProblemArray.getJSONObject(0);

                    IncomingRequestBean.Problem problem = new IncomingRequestBean.Problem();

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
                    problem.setProblem(ProblemObject.getString("problem"));
                    problem.setStatus(ProblemObject.getString("order_status"));
                    problem.setSchedule(ProblemObject.getString("schedule"));

                    incomingRequestBean.setProblem(problem);
                    incomingRequestListBeanList.add(incomingRequestBean);

                    for (int j=0; j<ProblemArray.length();j++) {
                        JSONObject obj = ProblemArray.getJSONObject(j);
                        if (obj.getString("order_status").equals("PENDING")) {
                            count++;
                        } else if (obj.getString("order_status").equals("COMPLETED")) {
                            countcomplete++;
                        } else if (obj.getString("status").equals("Pending")) {
                            countAccepted++;
                        }
                    }

                    all = incomingRequestListBeanList.size();

                    Log.e("ListSize", "??" + incomingRequestListBeanList.size());
                }

                return incomingRequestListBeanList;

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
        protected void onPostExecute(List<IncomingRequestBean> result) {
            super.onPostExecute(result);
            if (result == null) {
            } else if (result.isEmpty()) {
            } else {

                String index = null;

                for (IncomingRequestBean bean : result) {

                    if (bean.getProblem().getStatus().equalsIgnoreCase("ACCEPTED")) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String currentDateStr = df.format(c.getTime());
                        Date currentDate = null, jobDate = null;

                        try {

                            currentDate = df.parse(currentDateStr);
                            jobDate = df.parse(bean.getProblem().getDate());

                            Log.e("Current Date", currentDateStr);
                            Log.e("Job Date", bean.getProblem().getDate());
                            Log.e("Compare Date", ">>>" + currentDate.compareTo(jobDate));

                            if (currentDate.compareTo(jobDate) > 0) {

                                Log.e("Index ::", ">>>" + result.indexOf(bean));
                                index = String.valueOf(result.indexOf(bean));
                                break;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }

                if (index == null) {
                } else {
                    Intent intent = new Intent(TradesmanActivity.this, IsJobDone.class);
                    intent.putExtra("ProblemId", result.get(Integer.parseInt(index)).getProblem().getId());
                    startActivity(intent);
                }
            }
        }
    }

    private class JsonIncomingReq extends AsyncTask<String, String, List<IncomingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {
            try {
                URL url = new URL(HttpPAth.Urlpath + "get_bookingList_byid&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("Tradesman_id", sessionTradesman.getId());
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
                Log.e("JsonIncomingJob", response);
                List<IncomingRequestBean> incomingRequestListBeanList = new ArrayList<IncomingRequestBean>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    IncomingRequestBean incomingRequestBean = new IncomingRequestBean();
                    incomingRequestBean.setId(object.getString("id"));
                    incomingRequestBean.setName(object.getString("name"));
                    incomingRequestBean.setPost_code(object.getString("post_code"));
                    incomingRequestBean.setCity(object.getString("city"));
                    incomingRequestBean.setState(object.getString("state"));
                    incomingRequestBean.setHome_phone(object.getString("home_phone"));
                    incomingRequestBean.setWork_phone(object.getString("work_phone"));
                    incomingRequestBean.setMobile_phone(object.getString("mobile_phone"));
                    incomingRequestBean.setEmail(object.getString("email"));
                    incomingRequestBean.setTradesman(object.getString("tradesman"));
                    incomingRequestBean.setUsername(object.getString("username"));
                    incomingRequestBean.setStreet(object.getString("street"));
                    incomingRequestBean.setHousenoo(object.getString("housenoo"));
                    JSONArray ProblemArray = object.getJSONArray("problem");
                    JSONObject ProblemObject = ProblemArray.getJSONObject(0);
                    IncomingRequestBean.Problem problem = new IncomingRequestBean.Problem();
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
                    problem.setProblem(ProblemObject.getString("problem"));
                    incomingRequestBean.setProblem(problem);
                    incomingRequestListBeanList.add(incomingRequestBean);
                    Log.e("ListSize", "??" + incomingRequestListBeanList.size());
                }

                return incomingRequestListBeanList;

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
        protected void onPostExecute(List<IncomingRequestBean> result) {
            super.onPostExecute(result);
            if (result == null) {

                IncReqCountTV.setVisibility(View.GONE);
                tv_jobPending.setVisibility(View.GONE);
                tv_allJOb.setVisibility(View.GONE);
                tv_completedJOb.setVisibility(View.GONE);
            } else if (result.isEmpty()) {
                IncReqCountTV.setVisibility(View.GONE);
                tv_jobPending.setVisibility(View.GONE);
                tv_allJOb.setVisibility(View.GONE);
                tv_completedJOb.setVisibility(View.GONE);
            } else {
                IncReqCountTV.setVisibility(View.VISIBLE);
                tv_jobPending.setVisibility(View.VISIBLE);
                tv_completedJOb.setVisibility(View.VISIBLE);
                tv_allJOb.setVisibility(View.VISIBLE);
                IncReqCountTV.setText(String.valueOf(result.size()));
                tv_jobPending.setText(count + "");
                tv_completedJOb.setText(countcomplete + "");
                tv_allJOb.setText(countAccepted + "");
            }
        }
    }

}
