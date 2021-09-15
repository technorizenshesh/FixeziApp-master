package com.cliffex.Fixezi;/*
package com.gauravr.technorizen.fixezi;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gauravr.technorizen.crowdsell.util.IabBroadcastReceiver;
import com.gauravr.technorizen.crowdsell.util.IabHelper;
import com.gauravr.technorizen.crowdsell.util.IabResult;
import com.gauravr.technorizen.crowdsell.util.Inventory;
import com.gauravr.technorizen.crowdsell.util.Purchase;

import java.util.ArrayList;
import java.util.List;


public class UpgradeActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    Toolbar ToolbarUpgrade;
    static final String TAG = "Crowdsell";

    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    // Will the subscription auto-renew?


    // Tracks the currently owned infinite gas SKU, and the options in the Manage dialog
    String mInfiniteGasSku = "";


    // Used to select between purchasing gas on a monthly or yearly basis
    String mSelectedSubscriptionPeriod = "";

    // SKU for our subscription (infinite gas)
    static final String SKU_INFINITE_GAS_MONTHLY = "monthly_plan";
    static final String SKU_INFINITE_GAS_YEARLY = "yearly_plan";
    static final String SKU_INFINITE_GAS_WEEKLY = "weekly";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;


    // Current amount of gas in tank, in units
    int mTank;

    // The helper object
    IabHelper mHelper;
    static final int TANK_MAX = 4;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;


    Button MonthlyBT, YearlyBT;
    LinearLayout Weekly;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String WhichPlan = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        ToolbarUpgrade = (Toolbar) findViewById(R.id.ToolbarUpgrade);
        setSupportActionBar(ToolbarUpgrade);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Upgrade Plan");

        sharedPreferences = getApplicationContext().getSharedPreferences("CrowdsellPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        MonthlyBT = (Button) findViewById(R.id.MonthlyBT);
        YearlyBT = (Button) findViewById(R.id.YearlyBT);
        Weekly = (LinearLayout) findViewById(R.id.Weekly);


        MonthlyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MonthlyClicked();

            }
        });

        YearlyBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YearlyClicked();

            }
        });


        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                WeeklyClicked();

            }
        });


        loadData();
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgf3NGtQnN9wbD2sAfHOsFQhzB5xY/Rfky5Pz8PacQzilvE2HSRcMHeq0WuI+31eddWcRYjF9PZGMucXS2PrJC3kIh3+hc2L4qRNva2eVE9uP/18xsTApkdct+dsaXyAzbpYpuQ2N0eNQc+ON6neWdVMbkra/tm4XkTAvbMipbVA/gJOHzs8GirA7pvlqYlPptDV8gXLsRC2PMRdQEp3oCwlecMhe2Xg9ZiuZa3xVd7dMc3h6d8KHGkRTG4YeFKM0aLK0pwPNoI5iwVwJdSM+scnbDCKr4/8stqmG+V9IrSv4/62BuIIPIEfTFWgwpzCY6B9nuaDofMrBzlhgagxWzwIDAQAB";

        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);


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

                mBroadcastReceiver = new IabBroadcastReceiver(UpgradeActivity.this);
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
    }


    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            Log.e("GAURAV LOG", "QueryInventoryFinished");


            if (mHelper == null) return;

            //
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            */
/*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             *//*

            // First find out which subscription is auto renewing
            Purchase gasMonthly = inventory.getPurchase(SKU_INFINITE_GAS_MONTHLY);
            Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
            Purchase gasWeekly = inventory.getPurchase(SKU_INFINITE_GAS_WEEKLY);

            if (gasMonthly != null) {
                mInfiniteGasSku = SKU_INFINITE_GAS_MONTHLY;
            } else if (gasYearly != null) {
                mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
            } else if (gasWeekly != null) {
                mInfiniteGasSku = SKU_INFINITE_GAS_WEEKLY;
            } else {
                mInfiniteGasSku = "";
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
                    || (gasYearly != null && verifyDeveloperPayload(gasYearly)) || (gasWeekly != null && verifyDeveloperPayload(gasWeekly));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) {

                if (mInfiniteGasSku.equalsIgnoreCase("")) {

                    saveData("FreePlan");
                } else {

                    saveData(mInfiniteGasSku);
                }


            } else {

                saveData("FreePlan");

            }


            Log.e(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }


    public void WeeklyClicked() {

        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }


        if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_WEEKLY)) {

            Toast.makeText(UpgradeActivity.this, "Already in use", Toast.LENGTH_SHORT).show();

        } else {

            mSelectedSubscriptionPeriod = SKU_INFINITE_GAS_WEEKLY;
            String payload = "";


            List<String> oldSkus = null;
            if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_YEARLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            } else if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_MONTHLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);

            }


            Log.d(TAG, "Launching purchase flow for gas subscription.");
            try {
                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
            }

        }

    }


    public void MonthlyClicked() {

        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }


        if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_MONTHLY)) {

            Toast.makeText(UpgradeActivity.this, "Already in use", Toast.LENGTH_SHORT).show();

        } else {

            mSelectedSubscriptionPeriod = SKU_INFINITE_GAS_MONTHLY;





            */
/* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. *//*

            String payload = "";


            List<String> oldSkus = null;
            if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_YEARLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            } else if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_WEEKLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);

            }


            Log.d(TAG, "Launching purchase flow for gas subscription.");
            try {
                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
            }

        }

    }

    public void YearlyClicked() {

        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }


        if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_YEARLY)) {

            Toast.makeText(UpgradeActivity.this, "Already in use", Toast.LENGTH_SHORT).show();

        } else {

            mSelectedSubscriptionPeriod = SKU_INFINITE_GAS_YEARLY;


            */
/* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. *//*

            String payload = "";


            List<String> oldSkus = null;
            if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_MONTHLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);
            } else if (mInfiniteGasSku.equalsIgnoreCase(SKU_INFINITE_GAS_WEEKLY)) {

                oldSkus = new ArrayList<String>();
                oldSkus.add(mInfiniteGasSku);

            }


            Log.d(TAG, "Launching purchase flow for gas subscription.");
            try {
                mHelper.launchPurchaseFlow(this, mSelectedSubscriptionPeriod, IabHelper.ITEM_TYPE_SUBS,
                        oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain("Error launching purchase flow. Another async operation in progress.");
            }

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

    */
/**
     * Verifies the developer payload of a purchase.
     *//*

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        Log.e("payload", "???" + payload);

        */
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
         *//*


        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            Log.e("GAURAV LOG", "IabPurchaseFinished");

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);

                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");

                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_INFINITE_GAS_MONTHLY)
                    || purchase.getSku().equals(SKU_INFINITE_GAS_YEARLY) || purchase.getSku().equals(SKU_INFINITE_GAS_WEEKLY)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");

                mSubscribedToInfiniteGas = true;
                mInfiniteGasSku = purchase.getSku();
                saveData(purchase.getSku());


            }

        }
    };


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
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        Builder bld = new Builder(this);
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

        WhichPlan = sharedPreferences.getString("WhichPlan", "FreePlan");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
*/


     /*







         sdfsd
sdfsd*/