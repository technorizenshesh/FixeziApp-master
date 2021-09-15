package com.cliffex.Fixezi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;

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

public class JobSent extends AppCompatActivity {

    Toolbar job_req_toolbar;
    TextView toolbar_title, canceljob11;
    RelativeLayout cancelTwo;
    TextView tvBillPayMobNo, tvTradMobNo;//tv_trademan_mobile_no
    TextView tvProName, tvProDate, tvProTime, tvProFleTime, tvProFleDate, tvProPerSite, tvProJobAdd, tvProHomeNo, tvProMobiNo, tvProJobReq;
    TextView tvBiPayName, tvBilPayAdd, tvBilPayHomNo, tvBilPayWorkNo, tvBilPayMobiNo, tvBilPayEmail;
    TextView tvTraName, tvTraMobiNo;
    Typeface custom_font;
    TextView TimeFlexibilityTV;
    SessionUser sessionUser;
    Dialog GeneralDialog;
    ImageView CancelIM;
    String userid, username, useremail, userMobi, userHome, userWork, userSite, userAdd, userhomeAdd, userstreet, usercity, userstate, userpostcode;

    String TrademanId, ProblemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_request_activity);

        sessionUser = new SessionUser(this);

        initiComp();
        GetSharedPre();

        toolbar_title.setText("Job Request Sent");
        setSupportActionBar(job_req_toolbar);

        Bundle extra = getIntent().getExtras();
        ProblemId = extra.getString("ProblemId");
        TrademanId = extra.getString("TrademanId");


        CancelIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Appconstants.timeFlexi = "";
                Appconstants.dateFlexi = "";
                Appconstants.TIME_SELECTED = "";
                Appconstants.DATE_SELECTED = "";
                Appconstants.JOB_REQUEST = "";
                Appconstants.SITE_MOBILE_NUMBER = "";
                Appconstants.SITE_HOME_NUMBER = "";
                Appconstants.SITE_ADDRESS = "";
                Appconstants.PERSON_ON_SITE = "";
                Appconstants.timeFlexibleValue="";
                Appconstants.PROBLEM_SELECTED="";
                Appconstants.PREVIOUSTRADE_SELECTED="";
                Appconstants.afterhours="";
                Appconstants.Category="";
                Appconstants.ServiceLocation="";

                Intent intent = new Intent(JobSent.this, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        custom_font = Typeface.createFromAsset(getAssets(), "DroidSerif.ttf");
        toolbar_title.setTypeface(custom_font);
        canceljob11.setTypeface(custom_font);
        tvBiPayName.setTypeface(custom_font);
        tvBilPayAdd.setTypeface(custom_font);
        tvBilPayHomNo.setTypeface(custom_font);
        tvBilPayWorkNo.setTypeface(custom_font);
        tvBilPayMobiNo.setTypeface(custom_font);
        tvBilPayEmail.setTypeface(custom_font);
        SetBilPayData();

        cancelTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralDialog = new Dialog(JobSent.this);
                GeneralDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                GeneralDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                GeneralDialog.setContentView(R.layout.alert_dialog_reschedule);


                final Button cancelBt = (Button) GeneralDialog.findViewById(R.id.cancelBtReschedule);
                final Button acceptBt = (Button) GeneralDialog.findViewById(R.id.acceptBtReschedule);

                cancelBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        GeneralDialog.dismiss();
                    }
                });


                acceptBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        new JsonTaskRescheduleOrCancel().execute(ProblemId, TrademanId);

                    }
                });

                GeneralDialog.show();


            }
        });


    }


    public void GetSharedPre() {

        HashMap<String, String> UserInfo = sessionUser.getUserDetails();
        userid = UserInfo.get(SessionUser.KEY_ID);
        username = UserInfo.get(SessionUser.KEY_NAME);
        useremail = UserInfo.get(SessionUser.KEY_EMAIL);
        userHome = UserInfo.get(SessionUser.KEY_HOMEPHONE);
        userMobi = UserInfo.get(SessionUser.KEY_MOBILEPHONE);
        userWork = UserInfo.get(SessionUser.KEY_WORKPHONE);
        userSite = UserInfo.get(SessionUser.KEY_NAME);

        userhomeAdd = UserInfo.get(SessionUser.KEY_HOME_ADDRESS);

        usercity = UserInfo.get(SessionUser.KEY_CITY);
        userstate = UserInfo.get(SessionUser.KEY_STATE);
        userpostcode = UserInfo.get(SessionUser.KEY_POSTCODE);

        userAdd = userhomeAdd +", " + userpostcode + ", " + usercity;

    }

    public void SetBilPayData() {
        if (username.equals("") || username.equals("null")) {
            tvBiPayName.setText(username);
        } else {
            tvBiPayName.setText(username);
        }
        if (userAdd.equals("") || userAdd.equals("null")) {
            tvBilPayAdd.setText("no data");
        } else {

            tvBilPayAdd.setText(userAdd);
        }

        if (userHome.equals("") || userHome.equals("null")) {
            tvBilPayHomNo.setText("no data");
        } else {
            tvBilPayHomNo.setText(userHome);
        }
        if (userWork.equals("") || userWork.equals("null")) {
            tvBilPayWorkNo.setText("no data");
        } else {

            tvBilPayWorkNo.setText(userWork);
        }
        if (userMobi.equals("") || userMobi.equals("null")) {
            tvBilPayMobiNo.setText("no data");
        } else {
            tvBilPayMobiNo.setText(userMobi);
        }
        if (useremail.equals("") || useremail.equals("null")) {
            tvBilPayEmail.setText("no data");
        } else {
            tvBilPayEmail.setText(useremail);
        }
        if (Appconstants.PROBLEM_SELECTED.equals("") || Appconstants.PROBLEM_SELECTED.equals("null")) {
            tvProName.setText("no data");
        } else {
            tvProName.setText(Appconstants.PROBLEM_SELECTED);
        }
        if (Appconstants.DATE_SELECTED.equals("") || Appconstants.DATE_SELECTED.equals("null")) {
            tvProDate.setText("no data");
        } else {
            tvProDate.setText(Appconstants.DATE_SELECTED);
        }

        if (Appconstants.TIME_SELECTED.equals("") || Appconstants.TIME_SELECTED.equals("null")) {
            tvProTime.setText("no data");
        } else {
            tvProTime.setText(Appconstants.TIME_SELECTED);
        }


        if (Appconstants.JOB_REQUEST.equals("") || Appconstants.JOB_REQUEST.equals("null")) {
            tvProJobReq.setText("no data");
        } else {
            tvProJobReq.setText(Appconstants.JOB_REQUEST);
        }


        if (Appconstants.PERSON_ON_SITE.equals("") || Appconstants.PERSON_ON_SITE.equals("null")) {
            tvProPerSite.setText("no data");
        } else {
            tvProPerSite.setText(Appconstants.PERSON_ON_SITE);
        }


        if (Appconstants.SITE_ADDRESS.equals("") || Appconstants.SITE_ADDRESS.equals("null")) {
            tvProJobAdd.setText("no data");
        } else {
            tvProJobAdd.setText(Appconstants.SITE_ADDRESS);
        }


        if (Appconstants.SITE_HOME_NUMBER.equals("") || Appconstants.SITE_HOME_NUMBER.equals("null")) {
            tvProHomeNo.setText("no data");
        } else {
            tvProHomeNo.setText(Appconstants.SITE_HOME_NUMBER);
        }


        if (Appconstants.SITE_MOBILE_NUMBER.equals("") || Appconstants.SITE_MOBILE_NUMBER.equals("null")) {
            tvProMobiNo.setText("no data");
        } else {
            tvProMobiNo.setText(Appconstants.SITE_MOBILE_NUMBER);
        }


        Log.e("TradesmanBusinessName", "??" + Appconstants.TradesmanBusinessName);

        Log.e("TradesmanBusinessName", "??" + Appconstants.TradesmanBusinessName);
        Log.e("TradesmanBusinessName", "??" + Appconstants.TradesmanBusinessName);

        if (Appconstants.TradesmanBusinessName.equals("")) {
            tvTraName.setText("no data");
        } else {
            tvTraName.setText(Appconstants.TradesmanBusinessName);
        }
        if (Appconstants.TradesmanOfficeNumber.equals("")) {
            tvTraMobiNo.setText("no data");
        } else {
            tvTraMobiNo.setText(Appconstants.TradesmanOfficeNumber);
        }

        if (Appconstants.TradesmanMobileNumber.equals("")) {
            tvTradMobNo.setText("no data");
        } else {
            tvTradMobNo.setText(Appconstants.TradesmanMobileNumber);
        }

        tvProFleTime.setText(Appconstants.timeFlexi);
        tvProFleDate.setText(Appconstants.dateFlexi);
        TimeFlexibilityTV.setText(Appconstants.timeFlexibleValue);

    }

    private void initiComp() {

        job_req_toolbar = (Toolbar) findViewById(R.id.job_req_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        canceljob11 = (TextView) findViewById(R.id.canceljob11);
        tvBillPayMobNo = (TextView) findViewById(R.id.tv_bill_player_mobile_no);
        tvTradMobNo = (TextView) findViewById(R.id.tv_trademan_mobile_no);

        cancelTwo = (RelativeLayout) findViewById(R.id.cancelTwo);
        tvProName = (TextView) findViewById(R.id.tv_problem_name);
        tvProDate = (TextView) findViewById(R.id.datetxt);
        tvProTime = (TextView) findViewById(R.id.timetxt);
        tvProFleTime = (TextView) findViewById(R.id.tv_flexible_time);
        tvProFleDate = (TextView) findViewById(R.id.tv_flexible_date);
        tvProPerSite = (TextView) findViewById(R.id.tv_person_site);
        tvProJobAdd = (TextView) findViewById(R.id.tv_job_address);
        tvProHomeNo = (TextView) findViewById(R.id.tv_home_contact_no);
        tvProMobiNo = (TextView) findViewById(R.id.tv_mobile_no);
        tvProJobReq = (TextView) findViewById(R.id.tv_job_requirment);
        tvBiPayName = (TextView) findViewById(R.id.tv_bill_payer_name);
        tvBilPayAdd = (TextView) findViewById(R.id.tv_bill_payer_address);
        tvBilPayHomNo = (TextView) findViewById(R.id.tv_bill_payer_home_no);
        tvBilPayWorkNo = (TextView) findViewById(R.id.tv_bill_payer_work_no);
        tvBilPayMobiNo = (TextView) findViewById(R.id.tv_bill_player_mobile_no);
        tvBilPayEmail = (TextView) findViewById(R.id.tv_bill_payer_email);
        tvTraName = (TextView) findViewById(R.id.tv_trademan_name);
        TimeFlexibilityTV = (TextView) findViewById(R.id.TimeFlexibilityTV);

        tvTraMobiNo = (TextView) findViewById(R.id.tv_trademan_off_mobile_no);
        CancelIM = (ImageView) findViewById(R.id.CancelIM);


    }


    public class JsonTaskRescheduleOrCancel extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... paramss) {

            /*http://www.technorizen.co.in/WORKSPACE6/FIXEZI/webserv.php?reschedule_data_time_byuser&order_status=RESCHEDULE&date=15-10-2015&time=10:20&problem_id=1&Tradesman_id=1*/
            try {
                URL url = new URL(HttpPAth.Urlpath + "reschedule_data_time_byuser&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", paramss[0]);
                params.put("Tradesman_id", paramss[1]);
                params.put("order_status", "");

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
                Log.e("JsonCAncel", response);

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

                Toast.makeText(JobSent.this, "Server Problem", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("CENCEL BY USER")) {

                Appconstants.timeFlexi = "";
                Appconstants.dateFlexi = "";
                Appconstants.TIME_SELECTED = "";
                Appconstants.DATE_SELECTED = "";
                Appconstants.JOB_REQUEST = "";
                Appconstants.SITE_MOBILE_NUMBER = "";
                Appconstants.SITE_HOME_NUMBER = "";
                Appconstants.SITE_ADDRESS = "";
                Appconstants.PERSON_ON_SITE = "";
                Appconstants.timeFlexibleValue="";
                Appconstants.PROBLEM_SELECTED="";
                Appconstants.PREVIOUSTRADE_SELECTED="";
                Appconstants.afterhours="";
                Appconstants.Category="";
                Appconstants.ServiceLocation="";

                Toast.makeText(JobSent.this, "Cancelled", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(JobSent.this, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Appconstants.timeFlexi = "";
        Appconstants.dateFlexi = "";
        Appconstants.TIME_SELECTED = "";
        Appconstants.DATE_SELECTED = "";
        Appconstants.JOB_REQUEST = "";
        Appconstants.SITE_MOBILE_NUMBER = "";
        Appconstants.SITE_HOME_NUMBER = "";
        Appconstants.SITE_ADDRESS = "";
        Appconstants.PERSON_ON_SITE = "";
        Appconstants.timeFlexibleValue="";
        Appconstants.PROBLEM_SELECTED="";
        Appconstants.PREVIOUSTRADE_SELECTED="";
        Appconstants.afterhours="";
        Appconstants.Category="";
        Appconstants.ServiceLocation="";

        Intent intent = new Intent(JobSent.this, UserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}