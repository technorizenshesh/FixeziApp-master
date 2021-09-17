package com.cliffex.Fixezi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import java.util.LinkedHashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllJobsDetail extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview, ProblemDetail, DateDetail, TimeDetail, FlexibleDateDetail,
            FlexibleTimeDetail, PersonOnSiteDetail, JobAddressDetail, TimeFlexibilityTVAJD;
    TextView HomeNmberDetail, MobileNmberDetail, JobRequestDetail, FullNameDetail, FullAddressDetailUser, HomeNmberDetailUser, WorkNmberDetailUser, MobileNmberDetailUser, EmailDetail;
    LinearLayout RLDetail;
    RelativeLayout NavigationUpIM;
    SessionTradesman sessionTradesman;
    TextView StatusDetailAJDTV, SubStatusDetailAJDTV;
    TextView AddNotesTV, ViewNotesTV;
    LinearLayout NoteOptionsLL;
    String ProblemId = "";
    private TextView start_jobs;
    private TextView IsJobDoneTV;
    private TextView stop_jobs;
    private TextView resume_jobs;
    private RadioGroup radiogroup;
    private RadioButton radiobutton;
    private TextView radio_btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_jobs_detail);

        start_jobs = (TextView) findViewById(R.id.start_jobs);
        stop_jobs = (TextView) findViewById(R.id.stop_jobs);
        resume_jobs = (TextView) findViewById(R.id.resume_jobs);

        IsJobDoneTV = (TextView) findViewById(R.id.IsJobDoneTV);
        RLDetail = (LinearLayout) findViewById(R.id.RLDetailScheduleAJD);
        ProblemDetail = (TextView) findViewById(R.id.ProblemDetailScheduleAJD);
        DateDetail = (TextView) findViewById(R.id.DateDetailScheduleAJD);
        TimeDetail = (TextView) findViewById(R.id.TimeDetailScheduleAJD);
        FlexibleDateDetail = (TextView) findViewById(R.id.FlexibleDateDetailScheduleAJD);
        FlexibleTimeDetail = (TextView) findViewById(R.id.FlexibleTimeDetailScheduleAJD);
        PersonOnSiteDetail = (TextView) findViewById(R.id.PersonOnSiteDetailScheduleAJD);
        JobAddressDetail = (TextView) findViewById(R.id.JobAddressDetailScheduleAJD);
        HomeNmberDetail = (TextView) findViewById(R.id.HomeNmberDetailScheduleAJD);
        MobileNmberDetail = (TextView) findViewById(R.id.MobileNmberDetailScheduleAJD);
        JobRequestDetail = (TextView) findViewById(R.id.JobRequestDetailScheduleAJD);
        FullNameDetail = (TextView) findViewById(R.id.FullNameDetailScheduleAJD);
        FullAddressDetailUser = (TextView) findViewById(R.id.FullAddressDetailUserScheduleAJD);
        HomeNmberDetailUser = (TextView) findViewById(R.id.HomeNmberDetailUserScheduleAJD);
        WorkNmberDetailUser = (TextView) findViewById(R.id.WorkNmberDetailUserScheduleAJD);
        MobileNmberDetailUser = (TextView) findViewById(R.id.MobileNmberDetailUserScheduleAJD);
        TimeFlexibilityTVAJD = (TextView) findViewById(R.id.TimeFlexibilityTVAJD);
        EmailDetail = (TextView) findViewById(R.id.EmailDetailScheduleAJD);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        StatusDetailAJDTV = (TextView) findViewById(R.id.StatusDetailAJDTV);
        SubStatusDetailAJDTV = (TextView) findViewById(R.id.SubStatusDetailAJDTV);

        start_jobs = (TextView) findViewById(R.id.start_jobs);
        stop_jobs = (TextView) findViewById(R.id.stop_jobs);
        resume_jobs = (TextView) findViewById(R.id.resume_jobs);

        IsJobDoneTV = (TextView) findViewById(R.id.IsJobDoneTV);

        start_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manage_waiting_timeApi("START");
                start_jobs.setVisibility(View.GONE);
                stop_jobs.setVisibility(View.VISIBLE);
                resume_jobs.setVisibility(View.GONE);
            }
        });

        resume_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manage_waiting_timeApi("START");
            }
        });

        stop_jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                manage_waiting_timeApi("STOP");

                start_jobs.setVisibility(View.GONE);
                stop_jobs.setVisibility(View.GONE);
                resume_jobs.setVisibility(View.VISIBLE);
            }
        });


        IsJobDoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog callFeeDialog = new Dialog(AllJobsDetail.this);
                callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                callFeeDialog.setContentView(R.layout.alert_dialog_job_details);

                TextView okBt = (TextView) callFeeDialog.findViewById(R.id.OkayBTCR);
                TextView MyText = (TextView) callFeeDialog.findViewById(R.id.cancle_text);
                radiogroup = (RadioGroup) callFeeDialog.findViewById(R.id.radiogroup);

                okBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        int selectedId = radiogroup.getCheckedRadioButtonId();
                        radiobutton = (RadioButton) callFeeDialog.findViewById(selectedId);

                        if (radiobutton.getText().toString().equals("Complete")) {
                            AlertDialogOption();
                        }

                        if (radiobutton.getText().toString().equals("Incomplete")) {

                            AlertDialogOption1();

                            SweetAlertDialog alertDialog = new SweetAlertDialog(AllJobsDetail.this, SweetAlertDialog.NORMAL_TYPE);
                            alertDialog.setTitleText("fixezi");
                            alertDialog.setContentText("Jobs marked as incomplete will be sent to 'jobs pending' folder ");
                            alertDialog.show();

                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(AllJobsDetail.this, R.color.lightblue));

                        }

                        /* Toast.makeText(AllJobsDetail.this, radiobutton.getText(), Toast.LENGTH_SHORT).show();*/

                        callFeeDialog.dismiss();

                    }

                    private void AlertDialogOption() {

                        final Dialog callFeeDialog = new Dialog(AllJobsDetail.this);
                        callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        callFeeDialog.setContentView(R.layout.alert_dialog_optionscedule);

                        TextView cancle_text = (TextView) callFeeDialog.findViewById(R.id.cancle_text);
                        TextView OkayBTCR = (TextView) callFeeDialog.findViewById(R.id.OkayBTCR);
                        radiogroup = (RadioGroup) callFeeDialog.findViewById(R.id.radiogroup);

                        OkayBTCR.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callFeeDialog.dismiss();
                            }
                        });

                        cancle_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callFeeDialog.dismiss();
                            }
                        });

                        callFeeDialog.show();

                    }
                });

                MyText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callFeeDialog.dismiss();
                    }
                });

                callFeeDialog.show();

            }
        });


        NoteOptionsLL = (LinearLayout) findViewById(R.id.NoteOptionsLL);
        ViewNotesTV = (TextView) findViewById(R.id.ViewNotesTV);
        AddNotesTV = (TextView) findViewById(R.id.AddNotesTV);

        JobAddressDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllJobsDetail.this, MapActivity.class);
                startActivity(intent);
            }
        });

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

        if (InternetDetect.isConnected(this)) {

            new JsonTaskGetJobDetail().execute(ProblemId);

        } else {
            Toast.makeText(AllJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void manage_waiting_timeApi(String start) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AllJobsDetail.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        AndroidNetworking.get("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?" + "manage_waiting_time&problem_id=" + ProblemId + "&waiting_status=" + start + "&timezone=" + "Asia")
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String result = response.getString("result");

                            if (result.equalsIgnoreCase("successfull")) {

                                progressDialog.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                    }
                });
    }

    private void AlertDialogOption1() {

        final Dialog callFeeDialog = new Dialog(AllJobsDetail.this);
        callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        callFeeDialog.setContentView(R.layout.alert_dailogeincompelete);

        TextView cancle_text = (TextView) callFeeDialog.findViewById(R.id.cancle_text);
        TextView OkayBTCR = (TextView) callFeeDialog.findViewById(R.id.OkayBTCR);
        radiogroup = (RadioGroup) callFeeDialog.findViewById(R.id.radiogroup);
        radio_btn_complete = (RadioButton) callFeeDialog.findViewById(R.id.radio_btn_complete);

        radio_btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AllJobsDetail.this, JobDetailsAddQuote.class);
                startActivity(in);
            }
        });

        OkayBTCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFeeDialog.dismiss();
            }
        });

        cancle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFeeDialog.dismiss();
            }
        });

        callFeeDialog.show();

    }
/*
    private void AlertDailogePending() {

        final Dialog callFeeDialog = new Dialog(AllJobsDetail.this);
        callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        callFeeDialog.setContentView(R.layout.pendingalert);
        callFeeDialog.show();


    }*/

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
                Log.e("JsonJobDetail", response);

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
                problem.setCity(ProblemObject.getString("city"));
                problem.setHome_Number(ProblemObject.getString("Home_Number"));
                problem.setMobile_Number(ProblemObject.getString("Mobile_Number"));
                problem.setJob_Request(ProblemObject.getString("Job_Request"));
                problem.setUser_id(ProblemObject.getString("user_id"));
                problem.setProblem(ProblemObject.getString("problem"));
                problem.setIsTimeFlexible_value(ProblemObject.getString("IsTimeFlexible_value"));
                problem.setSub_status(ProblemObject.getString("sub_status"));
                problem.setStatus(ProblemObject.getString("order_status"));
                problem.setHousenoo(ProblemObject.getString("housenoo"));
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
        protected void onPostExecute(final IncomingRequestBean result) {
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

                //  JobAddressDetail.setText(result.getProblem().getHousenoo() + ", " + result.getProblem().getStreet() + ", " + result.getPost_code() + ", " + result.getCity());
                HomeNmberDetail.setText(result.getProblem().getHome_address());
                MobileNmberDetail.setText(result.getProblem().getMobile_Number());
                JobRequestDetail.setText(result.getProblem().getJob_Request());
                FullNameDetail.setText(result.getName());
                TimeFlexibilityTVAJD.setText(result.getProblem().getIsTimeFlexible_value());
                FullAddressDetailUser.setText(result.getHome_address());

                if (result.getHome_address().equalsIgnoreCase("")) {
                    String FullAddress = result.getProblem().getHousenoo() + ", " + result.getProblem().getStreet() + ", " + result.getPost_code() + ", " + result.getCity();
                    // FullAddressDetailUser.setText(FullAddress);
                } else {
                    String FullAddress = result.getProblem().getHousenoo() + ", " + result.getProblem().getStreet() + ", " + result.getPost_code() + ", " + result.getCity();
                    // FullAddressDetailUser.setText(FullAddress);
                }

                HomeNmberDetailUser.setText(result.getHome_phone());
                WorkNmberDetailUser.setText(result.getWork_phone());
                MobileNmberDetailUser.setText(result.getMobile_phone());
                EmailDetail.setText(result.getEmail());

                if (result.getProblem().getStatus().equalsIgnoreCase("PROCESS")) {

                    StatusDetailAJDTV.setText("Incoming");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#DAA520"));
                    HomeNmberDetailUser.setText("Hidden");
                    WorkNmberDetailUser.setText("Hidden");
                    MobileNmberDetailUser.setText("Hidden");
                    EmailDetail.setText("Hidden");
                    MobileNmberDetail.setText("Hidden");
                    JobAddressDetail.setText("Hidden");
                    HomeNmberDetail.setText("Hidden");
                    FullNameDetail.setText(result.getFirst_name() + " " + "Hidden");
                    FullAddressDetailUser.setText("Address Hidden");

                } else if (result.getProblem().getStatus().equalsIgnoreCase("RESCHEDULE")) {
                    StatusDetailAJDTV.setText("Reschedule");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#DAA520"));

                } else if (result.getProblem().getStatus().equalsIgnoreCase("ACCEPTED")) {
                    StatusDetailAJDTV.setText("Accepted");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#32CD32"));

                } else if (result.getProblem().getStatus().equalsIgnoreCase("REJECTED")) {

                    StatusDetailAJDTV.setText("Cancelled");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#FF0000"));
                } else if (result.getProblem().getStatus().equalsIgnoreCase("CANCELLED")) {

                    StatusDetailAJDTV.setText("Cancelled");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#FF0000"));

                } else if (result.getProblem().getStatus().equalsIgnoreCase("COMPLETED") || result.getProblem().getStatus().equalsIgnoreCase("RATED")) {

                    StatusDetailAJDTV.setText("Completed");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#32CD32"));

                    SubStatusDetailAJDTV.setText(result.getProblem().getSub_status());
                    SubStatusDetailAJDTV.setVisibility(View.VISIBLE);

                } else if (result.getProblem().getStatus().equalsIgnoreCase("PENDING")) {

                    StatusDetailAJDTV.setText("Pending");
                    StatusDetailAJDTV.setTextColor(Color.parseColor("#FF0000"));

                    SubStatusDetailAJDTV.setText(result.getProblem().getSub_status());
                    SubStatusDetailAJDTV.setVisibility(View.VISIBLE);
                }

                ProblemId = result.getProblem().getId();

                AddNotesTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(AllJobsDetail.this, AddNotes.class);
                        intent.putExtra("ProblemId", ProblemId);
                        startActivity(intent);
                    }
                });

                ViewNotesTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(AllJobsDetail.this, ViewNotes.class);
                        intent.putExtra("ProblemId", ProblemId);
                        startActivity(intent);
                    }
                });

            }
        }
    }


}
