package com.cliffex.Fixezi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.IncomingRequestBean;
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
import java.util.LinkedHashMap;
import java.util.Map;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class RescheduleJobsDetail extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview, ProblemDetail, DateDetail, TimeDetail, FlexibleDateDetail,
            FlexibleTimeDetail, PersonOnSiteDetail, JobAddressDetail, TimeFlexibilityTVDetailRSJ;
    TextView HomeNmberDetail, MobileNmberDetail, JobRequestDetail, FullNameDetail, FullAddressDetailUser, HomeNmberDetailUser, WorkNmberDetailUser, MobileNmberDetailUser, EmailDetail;
    RelativeLayout RLDetail;
    SessionTradesman sessionTradesman;
    Button AcceptJobBt, RejectJobBt;
    String ProblemId;
    IncomingRequestBean incomingRequestBean;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_job_detail);

        RLDetail = (RelativeLayout) findViewById(R.id.RLDetailRSJ);
        ProblemDetail = (TextView) findViewById(R.id.ProblemDetailRSJ);
        DateDetail = (TextView) findViewById(R.id.DateDetailRSJ);
        TimeDetail = (TextView) findViewById(R.id.TimeDetailRSJ);
        FlexibleDateDetail = (TextView) findViewById(R.id.FlexibleDateDetailRSJ);
        FlexibleTimeDetail = (TextView) findViewById(R.id.FlexibleTimeDetailRSJ);
        PersonOnSiteDetail = (TextView) findViewById(R.id.PersonOnSiteDetailRSJ);
        JobAddressDetail = (TextView) findViewById(R.id.JobAddressDetailRSJ);
        HomeNmberDetail = (TextView) findViewById(R.id.HomeNmberDetailRSJ);
        MobileNmberDetail = (TextView) findViewById(R.id.MobileNmberDetailRSJ);
        JobRequestDetail = (TextView) findViewById(R.id.JobRequestDetailRSJ);
        FullNameDetail = (TextView) findViewById(R.id.FullNameDetailRSJ);
        FullAddressDetailUser = (TextView) findViewById(R.id.FullAddressDetailUserRSJ);
        HomeNmberDetailUser = (TextView) findViewById(R.id.HomeNmberDetailUserRSJ);
        WorkNmberDetailUser = (TextView) findViewById(R.id.WorkNmberDetailUserRSJ);
        MobileNmberDetailUser = (TextView) findViewById(R.id.MobileNmberDetailUserRSJ);
        EmailDetail = (TextView) findViewById(R.id.EmailDetailRSJ);
        TimeFlexibilityTVDetailRSJ = (TextView) findViewById(R.id.TimeFlexibilityTVDetailRSJ);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        RejectJobBt = (Button) findViewById(R.id.RejectJobBtRSJ);
        AcceptJobBt = (Button) findViewById(R.id.AcceptJobBtRSJ);

        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        toolbar_textview.setText("Job Detail");
        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        ProblemId = extra.getString("ProblemId");


        AcceptJobBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetDetect.isConnected(RescheduleJobsDetail.this)) {

                    new JsonTaskAcceptOrReject().execute(ProblemId, "ACCEPTED", incomingRequestBean.getProblem().getUser_id());
                } else {
                    Toast.makeText(RescheduleJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RejectJobBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetDetect.isConnected(RescheduleJobsDetail.this)) {

                    new JsonTaskAcceptOrReject().execute(ProblemId, "REJECTED", incomingRequestBean.getProblem().getUser_id());
                } else {
                    Toast.makeText(RescheduleJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (InternetDetect.isConnected(this)) {

            new JsonTaskGetJobDetail().execute(ProblemId);
        } else {

            Toast.makeText(RescheduleJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    public class JsonTaskGetJobDetail extends AsyncTask<String, String, IncomingRequestBean> {


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
                System.out.println(response);
                Log.e("Job Detail", response);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject object = jsonArray.getJSONObject(0);


                incomingRequestBean = new IncomingRequestBean();
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
                problem.setJob_Request(ProblemObject.getString("Job_Request"));
                problem.setUser_id(ProblemObject.getString("user_id"));
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

            Log.e("In Post", "YEs");
            if (result == null) {
                Log.e("result", "null");

            } else {

                Log.e("result", "not null");

                RLDetail.setVisibility(View.VISIBLE);
                ProblemDetail.setText(result.getProblem().getProblem());
                DateDetail.setText(result.getProblem().getDate());
                TimeDetail.setText(result.getProblem().getTime());
                FlexibleDateDetail.setText(result.getProblem().getIsDateFlexible());
                FlexibleTimeDetail.setText(result.getProblem().getIsTimeFlexible());
                PersonOnSiteDetail.setText(result.getProblem().getPersonOnSite());
               // JobAddressDetail.setText(result.getProblem().getHousenoo()+", "+result.getProblem().getStreet()+", "+result.getProblem().getJob_Address()+", "+result.getProblem().getCity());
                JobAddressDetail.setText(result.getProblem().getHousenoo()+", "+result.getProblem().getStreet()+", "+result.getProblem().getJob_Address()+", "+result.getProblem().getCity());
                HomeNmberDetail.setText(result.getProblem().getHome_Number());
                MobileNmberDetail.setText(result.getProblem().getMobile_Number());
                JobRequestDetail.setText(result.getProblem().getJob_Request());
                TimeFlexibilityTVDetailRSJ.setText(result.getProblem().getIsTimeFlexible_value());
                FullNameDetail.setText(result.getName());

                String FullAddress = result.getProblem().getHousenoo()+", "+result.getHome_address() + ", " + result.getPost_code() + ", " + result.getCity();
                FullAddressDetailUser.setText(FullAddress);

                HomeNmberDetailUser.setText(result.getHome_phone());
                WorkNmberDetailUser.setText(result.getWork_phone());
                MobileNmberDetailUser.setText(result.getMobile_phone());
                EmailDetail.setText(result.getEmail());

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
                System.out.println(response);
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
                finish();
            } else if (result.equalsIgnoreCase("REJECTED")) {
                finish();

            }
        }
    }
}
