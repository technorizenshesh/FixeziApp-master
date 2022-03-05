package com.cliffex.Fixezi;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.util.IabBroadcastReceiver;
// import com.cliffex.Fixezi.util.IabHelper;
import com.cliffex.Fixezi.util.IabResult;
import com.cliffex.Fixezi.util.Inventory;
import com.cliffex.Fixezi.util.Purchase;
import com.google.gson.Gson;

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
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class PurchaseEmployeeId extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    Toolbar toolbar;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    TextView PurEmpMsgTV;
    LinearLayout Purchase3Ids, Purchase9Ids, Purchase6Ids;
    SessionTradesman sessionTradesman;


    // Debug tag, for logging
    static final String TAG = "fixezi";

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    /*static final String SKU_GAS50 = "buy_50";
    static final String SKU_GAS250 = "buy_250";
    static final String SKU_GAS600 = "buy_600";
    static final String SKU_GAS2000 = "buy_2000";*/

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    // IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;


    boolean IsAlreadyPurchased3 = false;
    Purchase myGlobalPurchase3;

    boolean IsAlreadyPurchased6 = false;
    Purchase myGlobalPurchase6;

    boolean IsAlreadyPurchased9 = false;
    Purchase myGlobalPurchase9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_employee_id);

        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.ToolbarPEID);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Purchase Employee Id's");
        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        PurEmpMsgTV = (TextView) findViewById(R.id.PurEmpMsgTV);

        Purchase3Ids = (LinearLayout) findViewById(R.id.Purchase3Ids);
        Purchase6Ids = (LinearLayout) findViewById(R.id.Purchase6Ids);
        Purchase9Ids = (LinearLayout) findViewById(R.id.Purchase9Ids);

        String text = "<font color='#1E90FF'>We give you 3 Employee Id's to start with,</font> If you need more Id's you can purchase them in the box below.";
        PurEmpMsgTV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);


        Purchase9Ids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IsAlreadyPurchased9) {

                    if (InternetDetect.isConnected(PurchaseEmployeeId.this)) {

                        new JsonAddEmployeeIds().execute("9");
                    } else {

                        Toast.makeText(PurchaseEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    String payload = sessionTradesman.getId();
//                    try {
//                        mHelper.launchPurchaseFlow(PurchaseEmployeeId.this, Appconstants.SKU_9_ID, RC_REQUEST, mPurchaseFinishedListener, payload);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        e.printStackTrace();
//                    }
                }

            }
        });

        Purchase6Ids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (IsAlreadyPurchased6) {

                    if (InternetDetect.isConnected(PurchaseEmployeeId.this)) {

                        new JsonAddEmployeeIds().execute("6");
                    } else {

                        Toast.makeText(PurchaseEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }


                } else {


                    String payload = sessionTradesman.getId();
//                    try {
//                        mHelper.launchPurchaseFlow(PurchaseEmployeeId.this, Appconstants.SKU_6_ID, RC_REQUEST, mPurchaseFinishedListener, payload);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        e.printStackTrace();
//                    }
                }

            }
        });

        Purchase3Ids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (IsAlreadyPurchased3) {

                    if (InternetDetect.isConnected(PurchaseEmployeeId.this)) {

                        new JsonAddEmployeeIds().execute("3");
                    } else {

                        Toast.makeText(PurchaseEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }


                } else {


                    String payload = sessionTradesman.getId();
//                    try {
//                        mHelper.launchPurchaseFlow(PurchaseEmployeeId.this, Appconstants.SKU_3_ID, RC_REQUEST, mPurchaseFinishedListener, payload);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        e.printStackTrace();
//                    }
                }


            }
        });


        // IAB CODE
        // load game data
        String base64EncodedPublicKey = Appconstants.base64EncodedPublicKey;

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
//        mHelper = new IabHelper(this, base64EncodedPublicKey);
//
//        // enable debug logging (for a production application, you should set this to false).
//        mHelper.enableDebugLogging(true);
//
//        // Start setup. This is asynchronous and the specified listener
//        // will be called once setup completes.
//        Log.d(TAG, "Starting setup.");
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
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
//                // Important: Dynamically register for broadcast messages about updated purchases.
//                // We register the receiver here instead of as a <receiver> in the Manifest
//                // because we always call getPurchases() at startup, so therefore we can ignore
//                // any broadcasts sent while the app isn't running.
//                // Note: registering this listener in an Activity is a bad idea, but is done here
//                // because this is a SAMPLE. Regardless, the receiver must be registered after
//                // IabHelper is setup, but before first call to getPurchases().
//                mBroadcastReceiver = new IabBroadcastReceiver(PurchaseEmployeeId.this);
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
//            }
//        });


    }


    private class JsonAddEmployeeIds extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";
        String SkuType = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected TradesManBean doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "update_employee_code&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", sessionTradesman.getId());
                params.put("employee_code", paramss[0]);
                SkuType = paramss[0];

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


            if (jsonresult.equalsIgnoreCase("successfully")) {

                if (SkuType.equalsIgnoreCase("3")) {

//                    try {
//                        mHelper.consumeAsync(myGlobalPurchase3, mConsumeFinishedListener);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        complain("Error consuming gas. Another async operation in progress.");
//
//                    }

                } else if (SkuType.equalsIgnoreCase("6")) {

//                    try {
//                        mHelper.consumeAsync(myGlobalPurchase6, mConsumeFinishedListener);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        complain("Error consuming gas. Another async operation in progress.");
//
//                    }

                } else if (SkuType.equalsIgnoreCase("9")) {

//                    try {
//                        mHelper.consumeAsync(myGlobalPurchase9, mConsumeFinishedListener);
//                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        complain("Error consuming gas. Another async operation in progress.");
//
//                    }
                }


            }
        }
    }

    /*private class JsonAddEmployeeIds extends AsyncTask<String, String, String> {

        String SkuType = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "update_employee_code&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", sessionTradesman.getId());
                params.put("employee_code", paramss[0]);
                SkuType = paramss[0];


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
            if (result == null) {

            } else if (result.equalsIgnoreCase("ACCEPTED")) {

                if (SkuType.equalsIgnoreCase("3")) {

                    try {
                        mHelper.consumeAsync(myGlobalPurchase3, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");

                    }

                } else if (SkuType.equalsIgnoreCase("6")) {

                    try {
                        mHelper.consumeAsync(myGlobalPurchase6, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");

                    }

                } else if (SkuType.equalsIgnoreCase("9")) {

                    try {
                        mHelper.consumeAsync(myGlobalPurchase9, mConsumeFinishedListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error consuming gas. Another async operation in progress.");

                    }
                }
            }
        }
    }
*/

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
//            Purchase purchase3 = inventory.getPurchase(Appconstants.SKU_3_ID);
//            if (purchase3 != null && verifyDeveloperPayload(purchase3)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//
//
//                IsAlreadyPurchased3 = true;
//                myGlobalPurchase3 = inventory.getPurchase(Appconstants.SKU_3_ID);
//
//                /*try {
//                    mHelper.consumeAsync(inventory.getPurchase(Appconstants.SKU_PAY_PER_JOB), mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                }*/
//
//            }
//
//            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
//            Purchase purchase6 = inventory.getPurchase(Appconstants.SKU_6_ID);
//            if (purchase6 != null && verifyDeveloperPayload(purchase6)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//
//
//                IsAlreadyPurchased6 = true;
//                myGlobalPurchase6 = inventory.getPurchase(Appconstants.SKU_6_ID);
//
//                /*try {
//                    mHelper.consumeAsync(inventory.getPurchase(Appconstants.SKU_PAY_PER_JOB), mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                }*/
//
//            }
//
//            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
//            Purchase purchase9 = inventory.getPurchase(Appconstants.SKU_9_ID);
//            if (purchase9 != null && verifyDeveloperPayload(purchase9)) {
//                Log.d(TAG, "We have gas. Consuming it.");
//
//
//                IsAlreadyPurchased9 = true;
//                myGlobalPurchase9 = inventory.getPurchase(Appconstants.SKU_9_ID);
//
//                /*try {
//                    mHelper.consumeAsync(inventory.getPurchase(Appconstants.SKU_PAY_PER_JOB), mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//                }*/
//
//            }
//
//
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
//            if (purchase.getSku().equals(Appconstants.SKU_3_ID)) {
//
//                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
//                IsAlreadyPurchased3 = true;
//                myGlobalPurchase3 = purchase;
//
//                if (InternetDetect.isConnected(PurchaseEmployeeId.this)) {
//
//                    new JsonAddEmployeeIds().execute("3");
//                } else {
//
//                    Toast.makeText(PurchaseEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
//                }
//
//                /*try {
//                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
//
//                    return;
//                }*/
//            } else if (purchase.getSku().equals(Appconstants.SKU_6_ID)) {
//
//                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
//                IsAlreadyPurchased6 = true;
//                myGlobalPurchase6 = purchase;
//
//                if (InternetDetect.isConnected(PurchaseEmployeeId.this)) {
//
//                    new JsonAddEmployeeIds().execute("6");
//                } else {
//
//                    Toast.makeText(PurchaseEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
//                }
//
//
//            } else if (purchase.getSku().equals(Appconstants.SKU_9_ID)) {
//
//                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
//                IsAlreadyPurchased9 = true;
//                myGlobalPurchase9 = purchase;
//
//                if (InternetDetect.isConnected(PurchaseEmployeeId.this)) {
//
//                    new JsonAddEmployeeIds().execute("9");
//                } else {
//
//                    Toast.makeText(PurchaseEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
//                }
//
//
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
//            if (purchase.getSku().equals(Appconstants.SKU_3_ID)) {
//
//                if (result.isSuccess()) {
//                    // successfully consumed, so we apply the effects of the item in our
//                    // game world's logic, which in our case means filling the gas tank a bit
//                    Log.d(TAG, "Consumption successful. Provisioning.");
//
//                    IsAlreadyPurchased3 = false;
//                    finish();
//
//                } else {
//                    complain("Error while consuming: " + result);
//                }
//            } else if (purchase.getSku().equals(Appconstants.SKU_6_ID)) {
//
//                if (result.isSuccess()) {
//                    // successfully consumed, so we apply the effects of the item in our
//                    // game world's logic, which in our case means filling the gas tank a bit
//                    Log.d(TAG, "Consumption successful. Provisioning.");
//
//                    IsAlreadyPurchased6 = false;
//                    finish();
//
//                } else {
//                    complain("Error while consuming: " + result);
//                }
//            } else if (purchase.getSku().equals(Appconstants.SKU_9_ID)) {
//
//                if (result.isSuccess()) {
//                    // successfully consumed, so we apply the effects of the item in our
//                    // game world's logic, which in our case means filling the gas tank a bit
//                    Log.d(TAG, "Consumption successful. Provisioning.");
//
//                    IsAlreadyPurchased9 = false;
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
//        Log.d(TAG, "Destroying helper.");
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

}
