package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.Model.WorkerBean;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class ScheduleJobsDetailTradesman extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview, ProblemDetail, DateDetail, TimeDetail, FlexibleDateDetail, FlexibleTimeDetail,
            PersonOnSiteDetail, JobAddressDetail, TimeFlexibilityTVDetailSchedule;
    TextView HomeNmberDetail, MobileNmberDetail, JobRequestDetail, FullNameDetail, FullAddressDetailUser, HomeNmberDetailUser, WorkNmberDetailUser, MobileNmberDetailUser, EmailDetail;
    RelativeLayout RLDetail;
    SessionTradesman sessionTradesman;
    RelativeLayout NavigationUpIM;
    List<WorkerBean> workerBeanList;
    String ProblemId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_jobs_detail_tradesman);

        RLDetail = (RelativeLayout) findViewById(R.id.RLDetailSchedule);
        ProblemDetail = (TextView) findViewById(R.id.ProblemDetailSchedule);
        DateDetail = (TextView) findViewById(R.id.DateDetailSchedule);
        TimeDetail = (TextView) findViewById(R.id.TimeDetailSchedule);
        FlexibleDateDetail = (TextView) findViewById(R.id.FlexibleDateDetailSchedule);
        FlexibleTimeDetail = (TextView) findViewById(R.id.FlexibleTimeDetailSchedule);
        PersonOnSiteDetail = (TextView) findViewById(R.id.PersonOnSiteDetailSchedule);
        JobAddressDetail = (TextView) findViewById(R.id.JobAddressDetailSchedule);
        HomeNmberDetail = (TextView) findViewById(R.id.HomeNmberDetailSchedule);
        MobileNmberDetail = (TextView) findViewById(R.id.MobileNmberDetailSchedule);
        JobRequestDetail = (TextView) findViewById(R.id.JobRequestDetailSchedule);
        FullNameDetail = (TextView) findViewById(R.id.FullNameDetailSchedule);
        FullAddressDetailUser = (TextView) findViewById(R.id.FullAddressDetailUserSchedule);
        HomeNmberDetailUser = (TextView) findViewById(R.id.HomeNmberDetailUserSchedule);
        WorkNmberDetailUser = (TextView) findViewById(R.id.WorkNmberDetailUserSchedule);
        MobileNmberDetailUser = (TextView) findViewById(R.id.MobileNmberDetailUserSchedule);
        EmailDetail = (TextView) findViewById(R.id.EmailDetailSchedule);
        TimeFlexibilityTVDetailSchedule = (TextView) findViewById(R.id.TimeFlexibilityTVDetailSchedule);
        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Job Detail");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        String ProblemId = extra.getString("ProblemId");

        workerBeanList = new ArrayList<>();
        if (InternetDetect.isConnected(this)) {

            new JsonTaskGetJobDetail().execute(ProblemId);
            new JsonGetWorker().execute(sessionTradesman.getId());

        } else {
            Toast.makeText(ScheduleJobsDetailTradesman.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

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
                System.out.println(response);
                Log.e("JsonAllAddress", response);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject object = jsonArray.getJSONObject(0);


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
                problem.setHousenoo(ProblemObject.getString("housenoo"));
                problem.setStreet(ProblemObject.getString("street"));
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
                JobAddressDetail.setText(result.getProblem().getJob_Address());
                HomeNmberDetail.setText(result.getProblem().getHome_Number());
                MobileNmberDetail.setText(result.getProblem().getMobile_Number());
                JobRequestDetail.setText(result.getProblem().getJob_Request());
                TimeFlexibilityTVDetailSchedule.setText(result.getProblem().getIsTimeFlexible_value());
                FullNameDetail.setText(result.getName());

                if (result.getHome_address().equalsIgnoreCase("")) {

                    String FullAddress = result.getHousenoo() + ", " + result.getStreet() + ", " + result.getPost_code() + ", " + result.getCity();
                    FullAddressDetailUser.setText(FullAddress);

                } else {

                    String FullAddress = result.getHome_address() + " " + result.getPost_code() + " " + result.getCity();
                    FullAddressDetailUser.setText(FullAddress);
                }

                HomeNmberDetailUser.setText(result.getHome_phone());
                WorkNmberDetailUser.setText(result.getWork_phone());
                MobileNmberDetailUser.setText(result.getMobile_phone());
                EmailDetail.setText(result.getEmail());

                ProblemId = result.getProblem().getId();

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_job_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ForwardJob:


                if (!(workerBeanList.isEmpty())) {


                    String[] workerNameArray = new String[workerBeanList.size()];

                    for (int i = 0; i < workerBeanList.size(); i++) {

                        workerNameArray[i] = workerBeanList.get(i).getName();

                    }

                    new AlertDialog.Builder(ScheduleJobsDetailTradesman.this)
                            .setTitle("Forward this job to")
                            .setSingleChoiceItems(workerNameArray, 0, null)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                    if (InternetDetect.isConnected(ScheduleJobsDetailTradesman.this)) {

                                        new JsonForwardJob().execute(workerBeanList.get(selectedPosition).getId(), ProblemId);

                                    } else {
                                        Toast.makeText(ScheduleJobsDetailTradesman.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                    }

                                }


                                class JsonForwardJob extends AsyncTask<String, String, String> {

                                    String JsonResult = "";

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();

                                    }

                                    @Override
                                    protected String doInBackground(String... strings) {

                                        try {
                                            URL url = new URL(HttpPAth.Urlpath + "forward_problem&");
                                            Map<String, Object> params = new LinkedHashMap<>();
                                            params.put("worker_id", strings[0]);
                                            params.put("problem_id", strings[1]);


                                            StringBuilder postData = new StringBuilder();
                                            for (Map.Entry<String, Object> param : params.entrySet()) {
                                                if (postData.length() != 0)
                                                    postData.append('&');
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
                                            Log.e("JsonForwardJob", response);
                                            //[{"result":"successfully"}]

                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject FinalObject = jsonArray.getJSONObject(0);
                                            JsonResult = FinalObject.getString("result");

                                            return JsonResult;

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
                                    protected void onPostExecute(String result) {
                                        super.onPostExecute(result);

                                        if (result == null) {

                                        } else if (result.equalsIgnoreCase("successfully")) {

                                            Toast.makeText(ScheduleJobsDetailTradesman.this, "Forward", Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                }


                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();

                                }
                            })

                            .show();
                } else {

                    Toast.makeText(ScheduleJobsDetailTradesman.this, "You have no workers", Toast.LENGTH_SHORT).show();
                }


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public class JsonGetWorker extends AsyncTask<String, String, List<WorkerBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<WorkerBean> doInBackground(String... strings) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "worker_user_list&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", strings[0]);


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
                Log.e("JsonGetWorkerList", response);

                workerBeanList = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    WorkerBean workerBean = new WorkerBean();

                    workerBean.setId(object.getString("id"));
                    workerBean.setName(object.getString("name"));
                    workerBean.setUsername(object.getString("username"));
                    workerBean.setPassword(object.getString("password"));

                    workerBeanList.add(workerBean);
                }

                return workerBeanList;


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
        protected void onPostExecute(List<WorkerBean> result) {
            super.onPostExecute(result);

            if (result == null) {


            } else if (result.isEmpty()) {


            } else {


            }

        }

    }


}