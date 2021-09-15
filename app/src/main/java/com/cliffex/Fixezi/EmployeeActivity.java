package com.cliffex.Fixezi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmployeeActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    ImageView ProfileDashIM, SignInAsUserInfoIM;
    TextView ReceivedJobsTV;
    CheckBox CheckboxSigninAsWorker;
    TextView ContinueAsWorkerTV;
    TextView DailyJobsWorkerTV, ScheduleJobsWorkerTV;
    TextView MessageTV2;
    Context mContext = EmployeeActivity.this;
    ImageView MinimizeIM, MaximizeIM;
    int countReschedule, countpending, countincompleted, countAccepted, countcancel, countcompleted;
    SessionWorker sessionWorker;
    TextView recived_count, tv_pending, tv_completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        sessionWorker = new SessionWorker(this);

        toolbar = (Toolbar) findViewById(R.id.ToolbarWM);
        toolbar_title = (TextView) findViewById(R.id.ToolbarTitleDash);
        recived_count = (TextView) findViewById(R.id.recived_count);
        tv_completed = (TextView) findViewById(R.id.tv_completed);
        tv_pending = (TextView) findViewById(R.id.tv_pending);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpDashIM);
        ProfileDashIM = (ImageView) findViewById(R.id.ProfileDashIM);
        SignInAsUserInfoIM = (ImageView) findViewById(R.id.SignInAsUserInfoIM);
        CheckboxSigninAsWorker = (CheckBox) findViewById(R.id.CheckboxSigninAsWorker);
        ContinueAsWorkerTV = (TextView) findViewById(R.id.ContinueAsWorkerTV);
        MinimizeIM = (ImageView) findViewById(R.id.MinimizeIM2);
        MaximizeIM = (ImageView) findViewById(R.id.MaximizeIM2);
        MessageTV2 = (TextView) findViewById(R.id.MessageTV22);

        setSupportActionBar(toolbar);
        toolbar_title.setText("Employee");
        ProfileDashIM.setVisibility(View.GONE);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                startActivity(new Intent(mContext,MainActivity.class));
            }
        });

        ReceivedJobsTV = (TextView) findViewById(R.id.ReceivedJobsTV);
        DailyJobsWorkerTV = (TextView) findViewById(R.id.DailyJobsWorkerTV);
        ScheduleJobsWorkerTV = (TextView) findViewById(R.id.ScheduleJobsWorkerTV);

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

        ReceivedJobsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeActivity.this, ReceivedJobs.class);
                startActivity(intent);
            }
        });

        ScheduleJobsWorkerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmployeeActivity.this, ScheduleNewWorker.class);
                startActivity(intent);
            }
        });

        DailyJobsWorkerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());

                ArrayList<String> stringArrayList = new ArrayList<String>();
                stringArrayList.add(formattedDate);

                Intent intent = new Intent(EmployeeActivity.this, ScheduleNewDetail.class);
                intent.putStringArrayListExtra("DateList", stringArrayList);
                intent.putExtra("Position", 0);
                startActivity(intent);
            }
        });

        SignInAsUserInfoIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog callFeeDialog = new Dialog(EmployeeActivity.this);
                callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                callFeeDialog.setContentView(R.layout.alert_dialog_comm_resi);

                final Button okBt = (Button) callFeeDialog.findViewById(R.id.OkayBTCR);
                TextView MyText = (TextView) callFeeDialog.findViewById(R.id.MyText);
                MyText.setText("Free Sign up as a user for when you need to find a Tradesman.");

                okBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        callFeeDialog.dismiss();
                    }
                });

                callFeeDialog.show();

            }
        });

        ContinueAsWorkerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckboxSigninAsWorker.isChecked()) {

                    Intent intent = new Intent(EmployeeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(EmployeeActivity.this, "Please check the box", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    private class JsonTaskGetRecievedJob extends AsyncTask<String, String, List<IncomingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "get_all_problem_details_of_worker&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("worker_id", sessionWorker.getId());


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
                Log.e("JsonReceiveJob", response);

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
                    for (int j = 0; j < ProblemArray.length(); j++) {
                        JSONObject obj = ProblemArray.getJSONObject(j);
                        if (obj.getString("order_status").equals("RESCHEDULE")) {
                            countReschedule++;
                        } else if (obj.getString("order_status").equals("PENDING")) {
                            countpending++;
                        } else if (obj.getString("status").equals("INCOMPLETED")) {
                            countincompleted++;
                        } else if (obj.getString("status").equals("ACCEPTED")) {
                            countAccepted++;
                        } else if (obj.getString("status").equals("COMPLETED")) {
                            countcompleted++;
                        } else if (obj.getString("status").equals("CANCELLED")) {
                            countcancel++;
                        }
                    }

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

                // NoJobsFoundTV3.setVisibility(View.VISIBLE);
            } else if (result.isEmpty()) {

                //NoJobsFoundTV3.setVisibility(View.VISIBLE);
            } else {

                Log.e(">>>>", "countAccepted >>" + countAccepted);
                Log.e(">>>>", "countcancel >>" + countcancel);
                Log.e(">>>>", "countcompleted >>" + countcompleted);
                Log.e(">>>>", "countincompleted >>" + countincompleted);
                Log.e(">>>>", "countpending >>" + countpending);
                Log.e(">>>>", "countReschedule >>" + countReschedule);

                String countPending = String.valueOf(countpending);
                String countCompleted = String.valueOf(countcompleted);
                String countResdule = String.valueOf(countReschedule);


                if (countResdule.equalsIgnoreCase("0")) {

                } else {
                    recived_count.setText(countReschedule + "");
                    recived_count.setVisibility(View.VISIBLE);
                }

                if (countPending.equalsIgnoreCase("0")) {

                } else {
                    tv_pending.setVisibility(View.VISIBLE);
                    tv_pending.setText(countPending);
                }
                if (countCompleted.equalsIgnoreCase("0")) {

                } else {
                    tv_completed.setVisibility(View.VISIBLE);
                    tv_completed.setText(countCompleted);
                }





                /*ReceivedJobs.JobAdapter adapter = new ReceivedJobs.JobAdapter(ReceivedJobs.this, result);
                recyclerView.setAdapter(adapter);*/
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetDetect.isConnected(this)) {
            new JsonTaskGetRecievedJob().execute();
        } else {
            Toast.makeText(EmployeeActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(mContext,MainActivity.class));
    }


}
