package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.util.IabBroadcastReceiver;
//import com.cliffex.Fixezi.util.IabHelper;
import com.cliffex.Fixezi.util.IabResult;
import com.cliffex.Fixezi.util.Inventory;
import com.cliffex.Fixezi.util.Purchase;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    Toolbar toolbar;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    TextView SubscribeBT;
    TextView ContentTV;

    static final String TAG = "fixezi";

    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    // Tracks the currently owned infinite gas SKU, and the options in the Manage dialog
    String mInfiniteGasSku = "";

    // Used to select between purchasing gas on a monthly or yearly basis
    String mSelectedSubscriptionPeriod = "";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
   // IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String WhichPlan = "";
    TextView CurrentPlanTV;
    SessionTradesman sessionTradesman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        toolbar = (Toolbar) findViewById(R.id.ToolbarPayment);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_title.setText("Subscribe");

        sessionTradesman = new SessionTradesman(this);
        SubscribeBT = (TextView) findViewById(R.id.SubscribeBT);
        ContentTV = (TextView) findViewById(R.id.ContentTV);

        sharedPreferences = getApplicationContext().getSharedPreferences("FixeziPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        final String Plan = bundle.getString("Plan");

        SubscribeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Plan.equalsIgnoreCase("Economy")) {

                    runEconomyPlan();

                } else if (Plan.equalsIgnoreCase("Full")) {

                    runFullPlan();
                }
            }
        });

        loadData();

        Log.d(TAG, "Creating IAB helper.");
       // mHelper = new IabHelper(this, Appconstants.base64EncodedPublicKey);

       // mHelper.enableDebugLogging(true);

        Log.d(TAG, "Starting setup.");
       // mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
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
//
//                mBroadcastReceiver = new IabBroadcastReceiver(SubscriptionActivity.this);
//                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
//                registerReceiver(mBroadcastReceiver, broadcastFilter);
//
//
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                try {
//                    mHelper.queryInventoryAsync(mGotInventoryListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error querying inventory. Another async operation in progress.");
//                }
//            }
//        });

    }


//    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished.");
//
//            if (mHelper == null) return;
//
//            //
//            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
//                return;
//            }
//
//            Log.d(TAG, "Query inventory was successful.");
//
//            /*
//             * Check for items we own. Notice that for each purchase, we check
//             * the developer payload to see if it's correct! See
//             * verifyDeveloperPayload().
//             */
//
//
//            // First find out which subscription is auto renewing
//            Purchase economyPlan = inventory.getPurchase(Appconstants.SKU_ECONOMY);
//            Purchase fullPlan = inventory.getPurchase(Appconstants.SKU_FUll);
//
//            if (economyPlan != null) {
//                mInfiniteGasSku = Appconstants.SKU_ECONOMY;
//
//            } else if (fullPlan != null) {
//                mInfiniteGasSku = Appconstants.SKU_FUll;
//
//            } else {
//                mInfiniteGasSku = "";
//
//            }
//
//            // The user is subscribed if either subscription exists, even if neither is auto
//            // renewing
//            mSubscribedToInfiniteGas = (economyPlan != null && verifyDeveloperPayload(economyPlan))
//                    || (fullPlan != null && verifyDeveloperPayload(fullPlan));
//            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
//                    + " infinite gas subscription.");
//            if (mSubscribedToInfiniteGas) {
//
//                if (mInfiniteGasSku.equalsIgnoreCase("")) {
//                    saveData("Pay Per Job");
//                } else {
//                    saveData(mInfiniteGasSku);
//                }
//
//            } else {
//                saveData("Pay Per Job");
//            }
//
//            Log.e(TAG, "Initial inventory query finished; enabling main UI.");
//        }
//    };

    @Override
    public void receivedBroadcast() {
//        Log.d(TAG, "Received broadcast notification. Querying inventory.");
//        try {
//            mHelper.queryInventoryAsync(mGotInventoryListener);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error querying inventory. Another async operation in progress.");
//        }
    }


    public void runEconomyPlan() {

//        if (!mHelper.subscriptionsSupported()) {
//            complain("Subscriptions not supported on your device yet. Sorry!");
//            return;
//        }


        if (mInfiniteGasSku.equalsIgnoreCase(Appconstants.SKU_ECONOMY)) {

            Toast.makeText(SubscriptionActivity.this, "Already in use", Toast.LENGTH_SHORT).show();

        } else {

            mSelectedSubscriptionPeriod = Appconstants.SKU_ECONOMY;



            /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
            String payload = sessionTradesman.getId();


            List<String> oldSkus = null;
            if (mInfiniteGasSku.equalsIgnoreCase(Appconstants.SKU_FUll)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            } /*else if (mInfiniteGasSku.equalsIgnoreCase(Appconstants.SKU_INFINITE_GAS_WEEKLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);

            }*/


            Log.d(TAG, "Launching purchase flow for gas subscription.");
//            try {
//                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
//                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
//            } catch (IabHelper.IabAsyncInProgressException e) {
//                complain("Error launching purchase flow. Another async operation in progress.");
//            }

        }

    }

    public void runFullPlan() {

//        if (!mHelper.subscriptionsSupported()) {
//            complain("Subscriptions not supported on your device yet. Sorry!");
//            return;
//        }

        if (mInfiniteGasSku.equalsIgnoreCase(Appconstants.SKU_FUll)) {
            Toast.makeText(SubscriptionActivity.this, "Already in use", Toast.LENGTH_SHORT).show();
        } else {

            mSelectedSubscriptionPeriod = Appconstants.SKU_FUll;


            /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
            String payload = sessionTradesman.getId();


            List<String> oldSkus = null;
            if (mInfiniteGasSku.equalsIgnoreCase(Appconstants.SKU_ECONOMY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            }/* else if (mInfiniteGasSku.equalsIgnoreCase(Appconstants.SKU_INFINITE_GAS_WEEKLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);

            }*/


            Log.d(TAG, "Launching purchase flow for gas subscription.");
//            try {
//                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
//                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
//            } catch (IabHelper.IabAsyncInProgressException e) {
//                complain("Error launching purchase flow. Another async operation in progress.");
//            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        // if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
//        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
//            // not handled, so handle it ourselves (here's where you'd
//            // perform any handling of activity results not related to in-app
//            // billing...
//            super.onActivityResult(requestCode, resultCode, data);
//        } else {
//            Log.d(TAG, "onActivityResult handled by IABUtil.");
//        }

    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        Log.e("payload", "???" + payload);

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

//    // Callback for when a purchase is finished
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
//            if (purchase.getSku().equals(Appconstants.SKU_ECONOMY)
//                    || purchase.getSku().equals(Appconstants.SKU_FUll)) {
//                // bought the infinite gas subscription
//                Log.d(TAG, "Infinite gas subscription purchased.");
//
//                mSubscribedToInfiniteGas = true;
//                mInfiniteGasSku = purchase.getSku();
//                saveData(purchase.getSku());
//
//
//            }
//
//        }
//    };
//

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
        Log.e(TAG, "**** TrivialDrive Error: " + message);
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

    }
}
