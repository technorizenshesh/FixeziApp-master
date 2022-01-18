package com.cliffex.Fixezi;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.Other.AppConfig;
import com.cliffex.Fixezi.Response.IncomingTrades_Response;
import com.cliffex.Fixezi.util.CallReceiver;
import com.cliffex.Fixezi.util.IabBroadcastReceiver;
// import com.cliffex.Fixezi.util.IabHelper;
import com.cliffex.Fixezi.util.IabResult;
import com.cliffex.Fixezi.util.Inventory;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.cliffex.Fixezi.util.Purchase;
import com.cliffex.Fixezi.util.TimePickerFragment;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingJobRequestDetail extends
        AppCompatActivity implements
        IabBroadcastReceiver.IabBroadcastListener {

    Toolbar toolbar;
    TextView toolbar_textview, ProblemDetail, FlexibleDateDetail,
            FlexibleTimeDetail, PersonOnSiteDetail, JobAddressDetail, TimeFlexibilityTVDetail;
    TextView qotesaccepted, tv_canceljob, HomeNmberDetail, MobileNmberDetail, JobRequestDetail,
            FullNameDetail, FullAddressDetailUser, HomeNmberDetailUser, WorkNmberDetailUser,
            MobileNmberDetailUser, EmailDetail;
    RelativeLayout RLDetail;
    boolean isDateFlexi, isTimeFlexi, isReschedule;
    SessionTradesman sessionTradesman;
    Button AcceptJobBt, RejectJobBt, btn_reschedule;
    public static String ProblemId;
    IncomingRequestBean incomingRequestBean;
    RelativeLayout NavigationUpIM;
    IncomingTrades_Response successResponse;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String WhichPlan = "", tableId = "";
    static String finalTime = "", finalDate = "";
    String mobileNumbe;
    public static TextView DateDetail, TimeDetail;
    public static ImageView img_pensile_two, img_pensile_one;
    public static String strday;
    public static String strdate = null;
    RatingBar ratin1, ratin2, ratin3;
    String paymentPLan = "";

    // Debug tag, for logging
    static final String TAG = "fixezi";

    CallReceiver callReceiver;

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    /* static final String SKU_GAS50 = "buy_50";
       static final String SKU_GAS250 = "buy_250";
       static final String SKU_GAS600 = "buy_600";
       static final String SKU_GAS2000 = "buy_2000"; */

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    // IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    boolean IsAlreadyPurchased = false;
    Purchase myGlobalPurchase;
    private TextView text_number, billPayerUser, ralationUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_job_request_detail);

        sharedPreferences = getApplicationContext()
                .getSharedPreferences("FixeziPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadWhichPlan();

        RLDetail = (RelativeLayout) findViewById(R.id.RLDetail);
        ProblemDetail = (TextView) findViewById(R.id.ProblemDetail);
        billPayerUser = (TextView) findViewById(R.id.billPayerUser);
        ralationUser = (TextView) findViewById(R.id.ralationUser);
        DateDetail = (TextView) findViewById(R.id.DateDetail);
        TimeDetail = (TextView) findViewById(R.id.TimeDetail);
        tv_canceljob = (TextView) findViewById(R.id.tv_canceljob);
        qotesaccepted = (TextView) findViewById(R.id.qotesaccepted);
        FlexibleDateDetail = (TextView) findViewById(R.id.FlexibleDateDetail);
        FlexibleTimeDetail = (TextView) findViewById(R.id.FlexibleTimeDetail);
        PersonOnSiteDetail = (TextView) findViewById(R.id.PersonOnSiteDetail);
        JobAddressDetail = (TextView) findViewById(R.id.JobAddressDetail);
        HomeNmberDetail = (TextView) findViewById(R.id.HomeNmberDetail);
        MobileNmberDetail = (TextView) findViewById(R.id.MobileNmberDetail);
        JobRequestDetail = (TextView) findViewById(R.id.JobRequestDetail);
        FullNameDetail = (TextView) findViewById(R.id.FullNameDetail);
        FullAddressDetailUser = (TextView) findViewById(R.id.FullAddressDetailUser);
        HomeNmberDetailUser = (TextView) findViewById(R.id.HomeNmberDetailUser);
        WorkNmberDetailUser = (TextView) findViewById(R.id.WorkNmberDetailUser);
        MobileNmberDetailUser = (TextView) findViewById(R.id.MobileNmberDetailUser);
        EmailDetail = (TextView) findViewById(R.id.EmailDetail);
        TimeFlexibilityTVDetail = (TextView) findViewById(R.id.TimeFlexibilityTVDetail);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        RejectJobBt = (Button) findViewById(R.id.RejectJobBt);
        AcceptJobBt = (Button) findViewById(R.id.AcceptJobBt);
        btn_reschedule = (Button) findViewById(R.id.btn_reschedule);
        img_pensile_one = (ImageView) findViewById(R.id.img_pensile_one);
        img_pensile_two = (ImageView) findViewById(R.id.img_pensile_two);
        ratin1 = (RatingBar) findViewById(R.id.ratin1);
        ratin2 = (RatingBar) findViewById(R.id.ratin2);
        ratin3 = (RatingBar) findViewById(R.id.ratin3);
        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        text_number = (TextView) findViewById(R.id.text_number);
        toolbar_textview.setText("Job Card");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        ProblemId = extra.getString("ProblemId");
        text_number.setText("" + ProblemId);
        text_number.setTextColor(Color.parseColor("#cd3232"));
        /* text_number.setText(ProblemId); */
        /* text_number.setTextColor(); */

        AcceptJobBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingDialog();
            }
        });

        img_pensile_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect();
            }
        });

        img_pensile_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new TimePickerFragment();
                // Show the time picker dialog fragment
                dFragment.show(getFragmentManager(), "Time Picker");
            }
        });

        btn_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
                dialog.setContentView(R.layout.dialog_call_tradmen);
                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                Button btn_Back = (Button) dialog.findViewById(R.id.btn_Back);

                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isReschedule = true;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileNumbe));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                btn_Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        RejectJobBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        if (InternetDetect.isConnected(this)) {
            // System.out.println(">>>>>>>>>>>>problemID>>>>>>"+ProblemId);
            new JsonTaskGetJobDetail().execute(ProblemId);
            JsonJobDetail(ProblemId);
        } else {
            Toast.makeText(IncomingJobRequestDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        // IAB CODE
        // load game data
        String base64EncodedPublicKey = Appconstants.base64EncodedPublicKey;

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");

        // mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        //mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");

//      mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result) {
//                Log.d(TAG, "Setup finished.");
//
//                if (!result.isSuccess()) {
//                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
//                    return;
//                }
//
//                // Have we been disposed of in the meantime? If so, quit.
//                if (mHelper == null) return;
//                mBroadcastReceiver = new IabBroadcastReceiver(IncomingJobRequestDetail.this);
//                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
//                registerReceiver(mBroadcastReceiver, broadcastFilter);
//
//                // IAB is fully set up. Now, let's get an inventory of stuff we own.
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                try {
//                    mHelper.queryInventoryAsync(mGotInventoryListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error querying inventory. Another async operation in progress.");
//                }
//
//            }
//        });

    }

    private void dateTimeEditDialog() {

        final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
        dialog.setContentView(R.layout.date_time_edit_dialog);

        TextView tvDateTimeText = dialog.findViewById(R.id.tvDateTimeText);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDateFlexi) {
                    img_pensile_one.setVisibility(View.VISIBLE);
                }

                if (isTimeFlexi) {
                    img_pensile_two.setVisibility(View.VISIBLE);
                }

                isReschedule = false;
                dialog.dismiss();

            }

        });

        dialog.show();

    }

    private void rescheduleApiCall() {

        HashMap<String, String> params = new HashMap<>();
        params.put("date", finalDate);
        params.put("time", finalTime);
        if (isDateFlexi) params.put("IsDateFlexible", "yes");
        else params.put("IsDateFlexible", "no");
        if (isTimeFlexi) params.put("IsTimeFlexible", "yes");
        else params.put("IsTimeFlexible", "no");
        params.put("table_id", tableId);
        params.put("Tradesman_id", sessionTradesman.getId());

        Log.e("dasdasdasdas", "param = " + params);

        ProjectUtil.showProgressDialog(IncomingJobRequestDetail.this, false, "Please wait...");
        AndroidNetworking.post(HttpPAth.Urlpath + "reschedule_status_update")
                .addBodyParameter(params)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                ProjectUtil.pauseProgressDialog();
            }

            @Override
            public void onError(ANError anError) {
                ProjectUtil.pauseProgressDialog();
            }
        });

    }

    private void incomingDialog() {
        final Dialog dialog = new Dialog(IncomingJobRequestDetail.this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.incoming_job_request_dialog);

        Button btnReschedule = dialog.findViewById(R.id.btnReschedule);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);

        btnReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
                dialog.setContentView(R.layout.dialog_call_tradmen);

                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                Button btn_Back = (Button) dialog.findViewById(R.id.btn_Back);

                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileNumbe));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                btn_Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.e("paymentPLan", "paymentPLan = " + paymentPLan);
                if (paymentPLan.equals("NoPlan")) {
                    openNoPlanDialog();
                } else if (paymentPLan.equalsIgnoreCase("PayPerJob")) {
                    acceptjobDialog();
                } else if (paymentPLan.equalsIgnoreCase("add_to_wallet")) {
                    acceptjobDialog();
                }

//                else {
//                    if (InternetDetect.isConnected(IncomingJobRequestDetail.this)) {
//                        new JsonTaskAcceptOrReject().execute(ProblemId, "ACCEPTED", incomingRequestBean.getProblem().getUser_id());
//                    } else {
//                        Toast.makeText(IncomingJobRequestDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
//                    }
//                }

            }
        });

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.show();

    }

    private void jobPaymentDialog() {

        final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
        dialog.setContentView(R.layout.job_payment_dialog);
        Button acceptBtGeneral = (Button) dialog.findViewById(R.id.acceptBtGeneral);
        Button cancelBtGeneral = (Button) dialog.findViewById(R.id.cancelBtGeneral);
        TextView MessageTV = (TextView) dialog.findViewById(R.id.MessageTV);

        String text = "A payment of $9.95 will be deducted from your account";

        MessageTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        acceptBtGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ProjectUtil.showProgressDialog(IncomingJobRequestDetail.this, false, "Please wait...");
                if (InternetDetect.isConnected(IncomingJobRequestDetail.this)) {
                    new JsonTaskAcceptOrReject().execute(ProblemId, "ACCEPTED", incomingRequestBean.getProblem().getUser_id());
                } else {
                    Toast.makeText(IncomingJobRequestDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void acceptjobDialog() {

        final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
        dialog.setContentView(R.layout.dialog_accetalrt);
        Button acceptBtGeneral = (Button) dialog.findViewById(R.id.acceptBtGeneral);
        Button cancelBtGeneral = (Button) dialog.findViewById(R.id.cancelBtGeneral);
        TextView MessageTV = (TextView) dialog.findViewById(R.id.MessageTV);

        String text = "Your are on a <font color='#AF6C66'>Pay Per Job</font> Plan. you have to accept this request in order to see the user details.";

        MessageTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        acceptBtGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                jobPaymentDialog();
                //                String payload = sessionTradesman.getId();
//                try {
//                    mHelper.launchPurchaseFlow(IncomingJobRequestDetail.this, Appconstants.SKU_PAY_PER_JOB, RC_REQUEST, mPurchaseFinishedListener, payload);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    e.printStackTrace();
//                }
            }
        });

        cancelBtGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openNoPlanDialog() {

        final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
        dialog.setContentView(R.layout.noplan_dialog);
        Button btnAccept = (Button) dialog.findViewById(R.id.btnAccept);
        Button btn_dismiss = (Button) dialog.findViewById(R.id.btn_dismiss);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(IncomingJobRequestDetail.this, PaymentPlan.class);
                startActivity(intent);
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void openDialog() {

        final Dialog dialog = new Dialog(IncomingJobRequestDetail.this);
        dialog.setContentView(R.layout.dialog_rejectalrt);
        Button acceptBtGeneral = (Button) dialog.findViewById(R.id.btn_ok);
        Button cancelBtGeneral = (Button) dialog.findViewById(R.id.btn_dismiss);

        acceptBtGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetDetect.isConnected(IncomingJobRequestDetail.this)) {
                    new JsonTaskAcceptOrReject().execute(ProblemId, "REJECTED", incomingRequestBean.getProblem().getUser_id());
                } else {
                    Toast.makeText(IncomingJobRequestDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        /*new EasyDialog.Builder(this)
                .setTitle("Alert!")
                .setSubtitle("Are you sure you want to reject this job ?")
                .isCancellable(true)
                .setCancelBtnColor("#000000")
                .setConfirmBtnColor("#fd3621")
                .setIcon(Icon.INFO)
                .setConfirmBtn("yes", new EasyDialogListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (InternetDetect.isConnected(IncomingJobRequestDetail.this)) {

                            new JsonTaskAcceptOrReject().execute(ProblemId, "REJECTED", incomingRequestBean.getProblem().getUser_id());
                        } else {

                            Toast.makeText(IncomingJobRequestDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setCancelBtn("no", new EasyDialogListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .build();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReschedule) {
            if (isDateFlexi || isTimeFlexi) {
                dateTimeEditDialog();
            }
        }
    }

    private void JsonJobDetail(String problem_id) {

        Call<ResponseBody> call = AppConfig.loadInterface().jobDetails(problem_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject obj = jsonArray.getJSONObject(0);

                        JSONArray problemArray = obj.getJSONArray("problem");
                        JSONObject problemObj = problemArray.getJSONObject(0);

                        finalDate = problemObj.getString("date");
                        finalTime = problemObj.getString("time");
                        tableId = obj.getString("id");
                        mobileNumbe = obj.getString("mobile_no");

                        Log.e("sdfsfsdfsd", "finalDate = " + finalDate);
                        Log.e("sdfsfsdfsd", "finalTime = " + finalTime);
                        Log.e("sdfsfsdfsd", "tableId = " + tableId);
                        Log.e("sdfsfsdfsd", "mobileNumbe = " + mobileNumbe);

                        if (problemObj.getString("IsDateFlexible").equals("yes")) {
                            isDateFlexi = true;
                        }

                        if (problemObj.getString("IsTimeFlexible").equals("yes")) {
                            isTimeFlexi = true;
                        }

                        Log.e("sdfsfsdfsd", "responseDataRetrofi = " + responseData);
                        paymentPLan = obj.getString("plan_type");
                        Log.e("sdfsfsdfsd", "paymentPLan = " + paymentPLan);
                        // String message=object.getString("message");
                        System.out.println("JObDetails_Response" + obj);

                        if (obj.getString("exp_status").equals("Valid")) {

                            tv_canceljob.setText(obj.getString("job_cancellations"));
                            qotesaccepted.setText(obj.getString("quotes_accepted_declined"));

                            JSONObject jsonObject = obj.getJSONObject("rating");
                            String easily_contacted = jsonObject.getString("easily_contacted");
                            String ease_of_job = jsonObject.getString("ease_of_job");
                            String payment_on_completion = jsonObject.getString("payment_on_completion");
                            Log.e("easily_contatcted", "" + easily_contacted);
                            ratin3.setRating(Float.parseFloat(payment_on_completion));
                            ratin2.setRating(Float.parseFloat(ease_of_job));
                            ratin1.setRating(Float.parseFloat(easily_contacted));
                        } else {
                            tv_canceljob.setText(obj.getString("job_cancellations"));
                            qotesaccepted.setText(obj.getString("quotes_accepted_declined"));

                            JSONObject jsonObject = obj.getJSONObject("rating");
                            String easily_contacted = jsonObject.getString("easily_contacted");
                            String ease_of_job = jsonObject.getString("ease_of_job");
                            String payment_on_completion = jsonObject.getString("payment_on_completion");
                            Log.e("easily_contatcted", "" + easily_contacted);
                            ratin3.setRating(Float.parseFloat(payment_on_completion));
                            ratin2.setRating(Float.parseFloat(ease_of_job));
                            ratin1.setRating(Float.parseFloat(easily_contacted));
                        }

                    } else ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(IncomingJobRequestDetail.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private class JsonTaskGetJobDetail extends AsyncTask<String, String, IncomingRequestBean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected IncomingRequestBean doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_problem_id_again_Tradesmandata&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", paramss[0]);

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

                Log.e("Job Detail>>>", ">>>>" + response);

                JSONArray jsonArray = new JSONArray(response);

                JSONObject object = jsonArray.getJSONObject(0);

                incomingRequestBean = new IncomingRequestBean();
                incomingRequestBean.setId(object.getString("id"));
                incomingRequestBean.setName(object.getString("name"));
                incomingRequestBean.setFirst_name(object.getString("first_name"));
                incomingRequestBean.setLast_name(object.getString("last_name"));
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
                incomingRequestBean.setHome_address(object.getString("home_address"));

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
                problem.setR_status(ProblemObject.getString("r_status"));
                problem.setR_id(ProblemObject.getString("r_id"));
                problem.setJob_Request(ProblemObject.getString("Job_Request"));
                problem.setStreet(ProblemObject.getString("street"));
                problem.setUser_id(ProblemObject.getString("user_id"));
                problem.setProblem(ProblemObject.getString("problem"));
                problem.setProblem(ProblemObject.getString("problem"));
                problem.setIsTimeFlexible_value(ProblemObject.getString("IsTimeFlexible_value"));

                incomingRequestBean.setProblem(problem);
                return incomingRequestBean;

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("EMESSAGE", e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(IncomingRequestBean result) {
            super.onPostExecute(result);

            Log.e("In Post", "Yes");

            if (result == null) {
                Log.e("result", "null");
            } else {

                Log.e("result>>", "not null");

                RLDetail.setVisibility(View.VISIBLE);
                ProblemDetail.setText(result.getProblem().getProblem());
                DateDetail.setText(result.getProblem().getDate());
                TimeDetail.setText(result.getProblem().getTime());
                FlexibleDateDetail.setText(result.getProblem().getIsDateFlexible());
                FlexibleTimeDetail.setText(result.getProblem().getIsTimeFlexible());
                // PersonOnSiteDetail.setText(result.getProblem().getPersonOnSite());

                int start = result.getProblem().getPersonOnSite().indexOf(' ');
                String firstName = "";

                if (start >= 0) {
                    firstName = result.getProblem().getPersonOnSite().substring(0, start);
                }

                PersonOnSiteDetail.setText(firstName + " #######");

                Log.e("getStreetgetStreet", "" + result.getProblem().getStreet());

                String[] str = result.getProblem().getStreet().split(",");
                String finalString = "";

                for (int i = 0; i < str.length; i++) {
                    if (i == 0) {
                        finalString = finalString + str[i].replaceAll("[a-zA-Z0-9]", "#");
                    } else {
                        finalString = finalString + ", " + str[i];
                    }
                }

                if (result.getProblem().getR_status() != null || !(result.getProblem().getR_status().equals(""))) {

                    ralationUser.setText(result.getProblem().getR_status());

                    if (result.getProblem().getR_id() != null || !(result.getProblem().getR_id().equals(""))) {
                        if (result.getProblem().getR_id().equals("NO")) {
                            billPayerUser.setText("NO");
                            billPayerUser.setTextColor(ContextCompat.getColor(IncomingJobRequestDetail.this, R.color.red));
                        } else {
                            billPayerUser.setText("YES");
                            billPayerUser.setTextColor(ContextCompat.getColor(IncomingJobRequestDetail.this, R.color.green));
                        }
                    }

                }

                JobAddressDetail.setText(finalString);
                // JobAddressDetail.setText(result.getProblem().getJob_Address().replaceAll("[a-zA-Z0-9]", "#") + ", " + result.getPost_code()+ result.getState());

                // HomeNmberDetail.setText(result.getProblem().getHome_Number());
                HomeNmberDetail.setText(result.getProblem().getHome_Number().replaceAll("[a-zA-Z0-9]", "#"));

                // MobileNmberDetail.setText(result.getProblem().getMobile_Number());
                MobileNmberDetail.setText(result.getProblem().getMobile_Number().replaceAll("[a-zA-Z0-9]", "#"));
                // MobileNmberDetail.setText(result.getProblem().getMobile_Number());

                JobRequestDetail.setText(result.getProblem().getJob_Request());
                TimeFlexibilityTVDetail.setText(result.getProblem().getIsTimeFlexible_value());

                FullNameDetail.setText(result.getFirst_name() + " " + result.getLast_name().replaceAll("[a-zA-Z0-9]", "#") + ", " + result.getPost_code());

                String FullAddress = result.getHome_address() + ", " + result.getPost_code() + ", " + result.getCity();

                String[] str1 = result.getHome_address().split(",");
                String finalString1 = "";

                for (int i = 0; i < str1.length; i++) {
                    if (i == 0) {
                        finalString1 = finalString1 + str1[i].replaceAll("[a-zA-Z0-9]", "#");
                    } else {
                        finalString1 = finalString1 + ", " + str1[i];
                    }
                }

                FullAddressDetailUser.setText(finalString1);

                // llAddressDetailUser.setText(FullAddress);
                // FullAddressDetailUser.setText("Address : " + FullAddress.replaceAll("[a-zA-Z0-9]", "#"));

//              HomeNmberDetailUser.setText(result.getHome_phone());
//              WorkNmberDetailUser.setText(result.getWork_phone());
//              MobileNmberDetailUser.setText(result.getMobile_phone());
//              EmailDetail.setText(result.getEmail());

                HomeNmberDetailUser.setText(result.getProblem().getHome_Number().replaceAll("[a-zA-Z0-9]", "#"));
                WorkNmberDetailUser.setText(result.getWork_phone().replaceAll("[a-zA-Z0-9]", "#"));
                MobileNmberDetailUser.setText(result.getMobile_phone().replaceAll("[a-zA-Z0-9]", "#"));
                // MobileNmberDetailUser.setText(result.getMobile_phone());
                EmailDetail.setText(result.getEmail().replaceAll("[a-zA-Z0-9]", "#"));
                // EmailDetail.setText(result.getEmail());

            }
        }
    }

    private class JsonTaskAcceptOrReject extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "ACCEPTED_REJECTED&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", paramss[0]);
                params.put("order_status", paramss[1]);
                params.put("user_id", paramss[2]);

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
                Log.e("JsonAllAddress", response);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String Result = jsonObject.getString("result");

                return Result;

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("EMESSAGE", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ProjectUtil.pauseProgressDialog();
            if (result == null) {
            } else if (result.equalsIgnoreCase("ACCEPTED")) {
                startActivity(new Intent(IncomingJobRequestDetail.this, TradesmanActivity.class));
                finish();
//                try {
//                    mHelper.consumeAsync(myGlobalPurchase, mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                } /* if (WhichPlan.equalsIgnoreCase("Pay Per Job")) {
//                    try {
//                        mHelper.consumeAsync(myGlobalPurchase, mConsumeFinishedListener);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        complain("Error consuming gas. Another async operation in progress.");
//                    }
//                } else {
//                    finish();
//                } */
            } else if (result.equalsIgnoreCase("REJECTED")) {
                finish();
            }
        }

    }

    // Listener that's called when we finish querying the items and subscriptions we own
//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished.");
//
//            Log.e("GAURAV LOG", "QueryInventoryFinished");
//
//            // Have we been disposed of in the meantime? If so, quit.
//            if (mHelper == null) return;
//
//            // Is it a failure?
//            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
//                return;
//            }
//
//            Log.d(TAG, "Query inventory was successful.");
//
//            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
//            Purchase gasPurchase = inventory.getPurchase(Appconstants.SKU_PAY_PER_JOB);
//            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//
//                IsAlreadyPurchased = true;
//                myGlobalPurchase = inventory.getPurchase(Appconstants.SKU_PAY_PER_JOB);
//
//                /*try {
//                    mHelper.consumeAsync(inventory.getPurchase(Appconstants.SKU_PAY_PER_JOB), mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                }*/
//
//            }
//            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
//        }
//    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
//        try {
//            mHelper.queryInventoryAsync(mGotInventoryListener);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error querying inventory. Another async operation in progress.");
//        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
////        if (mHelper == null) return;
//
//        // Pass on the activity result to the helper for handling
////        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
////            // not handled, so handle it ourselves (here's where you'd
////            // perform any handling of activity results not related to in-app
////            // billing...
////            super.onActivityResult(requestCode, resultCode, data);
////        } else {
////            Log.d(TAG, "onActivityResult handled by IABUtil.");
////        }
//    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        Log.e("payloadG", ">>>" + payload);
        return true;
    }

    // Callback for when a purchase is finished
//    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
//
//            Log.e("GAURAV LOG", "IabPurchaseFinished");
//
//            // if we were disposed of in the meantime, quit.
//            if (mHelper == null) return;
//
//            if (result.isFailure()) {
//                complain("Error purchasing: " + result);
//
//                return;
//            }
//            if (!verifyDeveloperPayload(purchase)) {
//                complain("Error purchasing. Authenticity verification failed.");
//
//                return;
//            }
//
//            Log.d(TAG, "Purchase successful.");
//
//            if (purchase.getSku().equals(Appconstants.SKU_PAY_PER_JOB)) {
//
//                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
//                IsAlreadyPurchased = true;
//                myGlobalPurchase = purchase;
//
//                if (InternetDetect.isConnected(IncomingJobRequestDetail.this)) {
//
//                    new JsonTaskAcceptOrReject().execute(ProblemId, "ACCEPTED", incomingRequestBean.getProblem().getUser_id());
//                } else {
//
//                    Toast.makeText(IncomingJobRequestDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
//                }
//
//                /*try {
//                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//
//                    return;
//                }*/
//            }
//        }
//    };
//
//    // Called when consumption is complete
//    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
//        public void onConsumeFinished(Purchase purchase, IabResult result) {
//            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
//
//            Log.e("GAURAV LOG", "ConsumnedFinished");
//
//            // if we were disposed of in the meantime, quit.
//            if (mHelper == null) return;
//
//            // We know this is the "gas" sku because it's the only one we consume,
//            // so we don't check which sku was consumed. If you have more than one
//            // sku, you probably should check...
//
//
//            if (purchase.getSku().equals(Appconstants.SKU_PAY_PER_JOB)) {
//
//                if (result.isSuccess()) {
//                    // successfully consumed, so we apply the effects of the item in our
//                    // game world's logic, which in our case means filling the gas tank a bit
//                    Log.d(TAG, "Consumption successful. Provisioning.");
//
//                    IsAlreadyPurchased = false;
//                    finish();
//
//                } else {
//                    complain("Error while consuming: " + result);
//                }
//            }
//
//            Log.d(TAG, "End consumption flow.");
//        }
//    };


    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
//        if (mHelper != null) {
//            mHelper.disposeWhenFinished();
//            mHelper = null;
//        }
    }


    void complain(String message) {
        Log.e(TAG, "**** SpeedyFax Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    /*  void saveData() {
        editor.putInt("tank", mTank);
        editor.apply();
        //CreditsTV.setText(mTank + " Credits");
        Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
    }
*/

    /* void loadData() {
        pref = getApplicationContext().getSharedPreferences("CreditPref", MODE_PRIVATE);
        editor = pref.edit();
        mTank = pref.getInt("tank", 20);
        // CreditsTV.setText(mTank + " Credits");
        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
    } */

    void loadWhichPlan() {
        WhichPlan = sharedPreferences.getString("WhichPlan", "Pay Per Job");
        Log.e("WhichPlan", WhichPlan);
    }

    private void DateSelect() {
        android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(IncomingJobRequestDetail.this);
        final DatePicker dp = new DatePicker(IncomingJobRequestDetail.this);
        ab.setView(dp);
        ab.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int dom = dp.getDayOfMonth();
                int mm = dp.getMonth();
                int yy = dp.getYear();
                String date = dom + "-" + (mm + 1) + "-" + yy;
                finalDate = date;

                Log.e("sadasdfasdasd", "finalDate = " + finalDate);
                DateDetail.setText(date);
                strday = DateDetail.getText().toString();
                finalDate = strday;
                updatenum(strday, finalTime, ProblemId);
            }
        });

        ab.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updatenum(String date, String time, String problem_id) {

        Log.e("fasdasdas", "date = " + date + " time = " + time + " problem_id = " + problem_id);

        ProjectUtil.showProgressDialog(IncomingJobRequestDetail.this, true, "Please wait...");
        Call<ResponseBody> call = AppConfig.loadInterface().updatedate(date, time, problem_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProjectUtil.pauseProgressDialog();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String message = object.getString("message");

                        Log.e("fasdasdas", "responseDataresponseData = " + responseData);

                        System.out.println("Updatenum" + object);

                        if (object.getString("status").equals("1")) {
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                            }*/
                        } else {
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
                            }*/
                        }

                    } else ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                ProjectUtil.pauseProgressDialog();
                Toast.makeText(IncomingJobRequestDetail.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
