package com.cliffex.Fixezi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.util.ProjectUtil;

import org.ankit.perfectdialog.EasyDialog;
import org.ankit.perfectdialog.EasyDialogListener;
import org.ankit.perfectdialog.Icon;
import org.json.JSONException;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import org.json.JSONObject;

public class PaymentPlan extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    Context mContext = PaymentPlan.this;
    LinearLayout SubscribePlanEconomy, PayPerJobPlanLL,
            SubscribePlanFull, PayPerJobPlanLL2;
    TextView CurrentPlanTV, tv_perJob, tvAlreadyPurchased;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String WhichPlan = "";
    String plainn;
    SessionTradesman sessionTradesman;
    public static Activity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_payment_plan);
        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.ToolbarPP);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvAlreadyPurchased = (TextView) findViewById(R.id.tvAlreadyPurchased);
        tv_perJob = (TextView) findViewById(R.id.tv_perJob);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        toolbar_title.setText("Payment Plan");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        PayPerJobPlanLL = (LinearLayout) findViewById(R.id.PayPerJobPlanLL);
        PayPerJobPlanLL2 = (LinearLayout) findViewById(R.id.PayPerJobPlanLL2);
        CurrentPlanTV = (TextView) findViewById(R.id.CurrentPlanTV);

        sharedPreferences = getApplicationContext().getSharedPreferences("FixeziPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        PayPerJobPlanLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payPerJobDialog();
            }
        });

        PayPerJobPlanLL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2();
            }
        });

        getPaymentStatusApi();

    }

    private void getPaymentStatusApi() {

        ProjectUtil.showProgressDialog(mContext, true, getString(R.string.please_wait));
        AndroidNetworking.post(HttpPAth.Urlpath + "get_payment_status")
                .addBodyParameter("user_id", sessionTradesman.getId())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            WhichPlan = jsonObject.getString("plan_type");
                            loadData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("asdasdasdas", "response = " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

    private void openDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentPlan.this);
        builder.setTitle("Fixezi");
        builder.setMessage("Sorry,this plan is temporary unavailable until Fixezi has enough user's to generate ongoing work.");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void payPerJobDialog() {

        Dialog dialog = new Dialog(PaymentPlan.this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.which_payment_plan_dialog);

        Button btNo = dialog.findViewById(R.id.btNo);
        Button btYes = dialog.findViewById(R.id.btYes);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btYes.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(PaymentPlan.this, ChooseCardAct.class);
            startActivity(intent);
        });

        btNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }

    private void openDialog() {
        new EasyDialog.Builder(this)
                .setTitle("Alert!")
                .setSubtitle("You have selected 'Pay Per Job' is this correct ?")
                .isCancellable(true)
                .setCancelBtnColor("#000000")
                .setIcon(Icon.INFO)
                .setConfirmBtn("yes", new EasyDialogListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PaymentPlan.this, ChooseCardAct.class);
                        startActivity(intent);
                    }
                }).setCancelBtn("no", new EasyDialogListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).build();
    }

    void loadData() {

        WhichPlan = sharedPreferences.getString("WhichPlan", "Pay Per Job");
        Log.e("WhichPlan", WhichPlan);

        if (WhichPlan.equalsIgnoreCase(Appconstants.SKU_FUll)) {
            CurrentPlanTV.setText("12 Months Full Plan");
            tvAlreadyPurchased.setVisibility(View.GONE);
        } else if (WhichPlan.equalsIgnoreCase(Appconstants.PAY_PER_JOB)) {
            CurrentPlanTV.setText("Currently you have Pay Per Job Plan");
            tvAlreadyPurchased.setVisibility(View.VISIBLE);
        } else if (WhichPlan.equalsIgnoreCase(Appconstants.SKU_ECONOMY)) {
            CurrentPlanTV.setText("12 Months Economy Plan");
            tvAlreadyPurchased.setVisibility(View.GONE);
        } else {
            CurrentPlanTV.setText("Currently you have no plan");
            tvAlreadyPurchased.setVisibility(View.GONE);
        }

    }

}
