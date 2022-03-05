package com.cliffex.Fixezi;

import android.app.Activity;
import android.app.Dialog;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.PendingRequestBean;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class JobRequestUserDetail extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview, ProblemDetail, DateDetail, TimeDetail, FlexibleDateDetail,
            FlexibleTimeDetail, PersonOnSiteDetail, JobAddressDetail, TimeFlexibilityTVDetailUser;
    TextView HomeNmberDetail, MobileNmberDetail, JobRequestDetail, FullNameDetail, FullAddressDetailUser, HomeNmberDetailUser,
            WorkNmberDetailUser, MobileNmberDetailUser, EmailDetail, OfficeNumber, BusinessNameTradesman2, MobileNumber, RescheduleBT, SubStatusDetailUserTV, StatusDetailUserTV;
    LinearLayout RLDetail;
    SessionUser sessionUser;
    Button CancelBT,btCancelJob,btSetReminder;
    Context mContext = JobRequestUserDetail.this;
    PendingRequestBean pendingRequestBean;
    String ProblemId = "";
    LinearLayout LLJR;
    RelativeLayout NavigationUpIM;
    String problemId = "";
    String trademanId = "";
    String userID = "";
    ProgressDialog progressDialog;
    TextView IsJobDoneTV, ViewNotesTV2, AddNotesTV;
    private TextView text_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request_user_detail);
        progressDialog = new ProgressDialog(this);

        btCancelJob = findViewById(R.id.btCancelJob);
        btSetReminder = findViewById(R.id.btSetReminder);
        RLDetail = findViewById(R.id.RLDetailUser);
        ProblemDetail = (TextView) findViewById(R.id.ProblemDetailUser);
        DateDetail = (TextView) findViewById(R.id.DateDetailUser);
        TimeDetail = (TextView) findViewById(R.id.TimeDetailUser);
        FlexibleDateDetail = (TextView) findViewById(R.id.FlexibleDateDetailUser);
        FlexibleTimeDetail = (TextView) findViewById(R.id.FlexibleTimeDetailUser);
        PersonOnSiteDetail = (TextView) findViewById(R.id.PersonOnSiteDetailUser);
        JobAddressDetail = (TextView) findViewById(R.id.JobAddressDetailUser);
        HomeNmberDetail = (TextView) findViewById(R.id.HomeNmberDetailUser1);
        MobileNmberDetail = (TextView) findViewById(R.id.MobileNmberDetailUser1);
        JobRequestDetail = (TextView) findViewById(R.id.JobRequestDetailUser);
        FullNameDetail = (TextView) findViewById(R.id.FullNameDetailUser);
        FullAddressDetailUser = (TextView) findViewById(R.id.FullAddressDetailUserUser);
        HomeNmberDetailUser = (TextView) findViewById(R.id.HomeNmberDetailUser2);
        WorkNmberDetailUser = (TextView) findViewById(R.id.WorkNmberDetailUser2);
        MobileNmberDetailUser = (TextView) findViewById(R.id.MobileNmberDetailUser2);
        EmailDetail = (TextView) findViewById(R.id.EmailDetailUser);
        BusinessNameTradesman2 = (TextView) findViewById(R.id.BusinessNameTradesman2);
        OfficeNumber = (TextView) findViewById(R.id.OfficeNumber);
        TimeFlexibilityTVDetailUser = (TextView) findViewById(R.id.TimeFlexibilityTVDetailUser);
        MobileNumber = (TextView) findViewById(R.id.MobileNumber);
        RescheduleBT = (TextView) findViewById(R.id.RescheduleBT);
        StatusDetailUserTV = (TextView) findViewById(R.id.StatusDetailUserTV);
        SubStatusDetailUserTV = (TextView) findViewById(R.id.SubStatusDetailUserTV);
        IsJobDoneTV = (TextView) findViewById(R.id.IsJobDoneTV);
        AddNotesTV = (TextView) findViewById(R.id.AddNotesTV);
        ViewNotesTV2 = (TextView) findViewById(R.id.ViewNotesTV2);
        LLJR = (LinearLayout) findViewById(R.id.LLJR);
        CancelBT = (Button) findViewById(R.id.CancelBT);
        sessionUser = new SessionUser(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        text_number = (TextView) findViewById(R.id.text_number);

        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        ProblemId = extra.getString("ProblemId");
        toolbar_textview.setText("Job Detail ");

        text_number.setText("" + ProblemId);
        text_number.setTextColor(Color.parseColor("#cd3232"));

        if (InternetDetect.isConnected(this)) {
            new JsonTaskGetJobDetail().execute(ProblemId);
        } else {
             Toast.makeText(JobRequestUserDetail.this,"Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        JobAddressDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobRequestUserDetail.this, MapActivity.class);
                startActivity(intent);
            }
        });

        AddNotesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobRequestUserDetail.this, AddNotes.class);
                intent.putExtra("ProblemId", ProblemId);
                startActivity(intent);
            }
        });

        btSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminderDialog();
            }
        });

        btCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelJobDialog();
            }
        });

    }

    private void openGoogleCalender() {
        Uri calendarUri = CalendarContract.CONTENT_URI
                .buildUpon()
                .appendPath("time")
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, calendarUri));
    }

    private void setReminderDialog() {
        final Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.set_reminder_dialog);

        Button btYes = dialog.findViewById(R.id.btYes);
        Button btNo = dialog.findViewById(R.id.btNo);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGoogleCalender();
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cancelJobDialog() {
        final Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.cancel_job_dialog);

        Button btYes = dialog.findViewById(R.id.btYes);
        Button btNo = dialog.findViewById(R.id.btNo);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callJobCancelApi(dialog);
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void callJobCancelApi(Dialog dialog) {
        dialog.dismiss();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        new JsonCancelJob().execute(problemId,userID,trademanId);
    }

    public class JsonTaskGetJobDetail extends AsyncTask<String, String, PendingRequestBean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected PendingRequestBean doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_problem_id_again_userdata&");
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

                while((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                System.out.println(response);
                Log.e("JsonJobRequestDetail", response);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject object = jsonArray.getJSONObject(0);

                pendingRequestBean = new PendingRequestBean();
                pendingRequestBean.setId(object.getString("id"));
                pendingRequestBean.setBusiness_name(object.getString("business_name"));
                pendingRequestBean.setTrading_name(object.getString("trading_name"));
                pendingRequestBean.setOffice_no(object.getString("office_no"));
                pendingRequestBean.setMobile_no(object.getString("mobile_no"));
                pendingRequestBean.setEmail(object.getString("email"));
                pendingRequestBean.setWebsite_url(object.getString("website_url"));
                pendingRequestBean.setAfter_hours(object.getString("after_hours"));
                pendingRequestBean.setSelect_trade(object.getString("select_trade"));

                JSONArray ProblemArray = object.getJSONArray("problem");
                JSONObject ProblemObject = ProblemArray.getJSONObject(0);

                PendingRequestBean.Problem problem = new PendingRequestBean.Problem();

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
                problem.setTradesman_id(ProblemObject.getString("Tradesman_id"));
                problem.setProblem(ProblemObject.getString("problem"));
                problem.setOrder_status(ProblemObject.getString("order_status"));
                problem.setIsTimeFlexible_value(ProblemObject.getString("IsTimeFlexible_value"));
                problem.setSub_status(ProblemObject.getString("sub_status"));
                problem.setReason(ProblemObject.getString("reason"));
                problem.setHousenoo(ProblemObject.getString("housenoo"));
                problem.setStreet(ProblemObject.getString("street"));
                problem.setCity(ProblemObject.getString("city"));


                pendingRequestBean.setProblem(problem);


                return pendingRequestBean;


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
        protected void onPostExecute(PendingRequestBean result) {
            super.onPostExecute(result);


            if (result == null) {


            } else {

                RLDetail.setVisibility(View.VISIBLE);
                ProblemDetail.setText(result.getProblem().getProblem());
                DateDetail.setText(result.getProblem().getDate());
                TimeDetail.setText(result.getProblem().getTime());
                FlexibleDateDetail.setText(result.getProblem().getIsDateFlexible());
                FlexibleTimeDetail.setText(result.getProblem().getIsTimeFlexible());
                PersonOnSiteDetail.setText(result.getProblem().getPersonOnSite());
                JobAddressDetail.setText(result.getProblem().getHousenoo() + "," + result.getProblem().getStreet() + "," + result.getProblem().getJob_Address() + "," + result.getProblem().getCity());

                HomeNmberDetail.setText(result.getProblem().getHome_Number());
                MobileNmberDetail.setText(result.getProblem().getMobile_Number());
                JobRequestDetail.setText(result.getProblem().getJob_Request());
                TimeFlexibilityTVDetailUser.setText(result.getProblem().getIsTimeFlexible_value());
                EmailDetail.setText(result.getEmail());

                problemId = result.getProblem().getId();
                trademanId = result.getProblem().getTradesman_id();
                userID = result.getProblem().getUser_id();


                FullNameDetail.setText(sessionUser.getUserDetails().get(SessionUser.KEY_NAME));
                String FullAddress = result.getProblem().getHousenoo() + "," + sessionUser.getUserDetails().get(SessionUser.KEY_HOME_ADDRESS) + ", " + sessionUser.getUserDetails().get(SessionUser.KEY_POSTCODE) + ", " + sessionUser.getUserDetails().get(SessionUser.KEY_CITY);
                // String FullAddress = sessionUser.getUserDetails().get(SessionUser.KEY_HOME_ADDRESS) + ", " + sessionUser.getUserDetails().get(SessionUser.KEY_POSTCODE) + ", " + sessionUser.getUserDetails().get(SessionUser.KEY_CITY);

                FullAddressDetailUser.setText(FullAddress);
                HomeNmberDetailUser.setText(sessionUser.getUserDetails().get(SessionUser.KEY_HOMEPHONE));
                WorkNmberDetailUser.setText(sessionUser.getUserDetails().get(SessionUser.KEY_WORKPHONE));
                MobileNmberDetailUser.setText(sessionUser.getUserDetails().get(SessionUser.KEY_MOBILEPHONE));
                EmailDetail.setText(sessionUser.getUserDetails().get(SessionUser.KEY_EMAIL));


                BusinessNameTradesman2.setText(result.getBusiness_name());
                OfficeNumber.setText(result.getOffice_no());
                MobileNumber.setText(result.getMobile_no());

                if (result.getProblem().getOrder_status().equalsIgnoreCase("REJECTED") || result.getProblem().getOrder_status().equalsIgnoreCase("COMPLETED") || result.getProblem().getOrder_status().equalsIgnoreCase("RATED")) {
                    LLJR.setVisibility(View.GONE);
                }

                IsJobDoneTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String[] strings = {"Completed", "Incomplete"};
                        new android.app.AlertDialog.Builder(JobRequestUserDetail.this)
                                .setTitle("Select One")
                                .setSingleChoiceItems(strings, 0, null)

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int whichButton) {

                                        int selectedPosition = ((android.app.AlertDialog) dialog).getListView().getCheckedItemPosition();

                                        if (selectedPosition == 0) {

                                            final String[] strings = {"Paid with cash", "Paid Eft", "Paid with cheque", "Paid with online banking", "Arranged payment plan", "Admin to email invoice"};

                                            new android.app.AlertDialog.Builder(JobRequestUserDetail.this)
                                                    .setTitle("Select One")
                                                    .setSingleChoiceItems(strings, 0, null)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, int whichButton) {

                                                            int selectedPosition = ((android.app.AlertDialog) dialog).getListView().getCheckedItemPosition();

                                                            if (InternetDetect.isConnected(JobRequestUserDetail.this)) {

                                                                new JsonJobDone().execute(problemId, userID, "0", strings[selectedPosition]);
                                                            } else {
                                                                Toast.makeText(JobRequestUserDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                                            }

                                                            dialog.dismiss();

                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            dialog.dismiss();

                                                        }
                                                    })

                                                    .show();


                                        } else if (selectedPosition == 1) {

                                            final String[] strings = {"Ordered parts", "Job has been Quoted", "Need to return", "Need to quote"};

                                            new android.app.AlertDialog.Builder(JobRequestUserDetail.this)
                                                    .setTitle("Select One")
                                                    .setSingleChoiceItems(strings, 0, null)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, int whichButton) {

                                                            int selectedPosition = ((android.app.AlertDialog) dialog).getListView().getCheckedItemPosition();
                                                            System.out.println("what is posion>>>" + selectedPosition);

                                                            if (InternetDetect.isConnected(JobRequestUserDetail.this)) {

                                                                new JsonJobDone().execute(problemId, userID, "0", strings[selectedPosition]);

                                                            } else {

                                                                Toast.makeText(JobRequestUserDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                                            }

                                                            dialog.dismiss();

                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            dialog.dismiss();

                                                        }
                                                    })

                                                    .show();


                                        } else {

                                            if (InternetDetect.isConnected(JobRequestUserDetail.this)) {

                                                // new JsonJobDone().execute(problemId, userID, "1", strings[selectedPosition]);
                                            } else {
                                                Toast.makeText(JobRequestUserDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        dialog.dismiss();

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        dialog.dismiss();
                                    }
                                })

                                .show();

                    }
                });

                ViewNotesTV2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(JobRequestUserDetail.this, ViewNotes.class);
                        intent.putExtra("ProblemId", ProblemId);
                        startActivity(intent);

                    }
                });

                RescheduleBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JobRequestUserDetail.this, RescheduleTimeActivity.class);
                        startActivityForResult(intent, 10);
                    }
                });


                CancelBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String currentDateStr = df.format(c.getTime());
                        Date currentDate = null, jobDate = null;
                        try {

                            currentDate = df.parse(currentDateStr);
                            jobDate = df.parse(pendingRequestBean.getProblem().getDate());

                            Log.e("Current Date", currentDateStr);
                            Log.e("Job Date", pendingRequestBean.getProblem().getDate());
                            Log.e("Compare Date", ">>>" + currentDate.compareTo(jobDate));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (currentDate.compareTo(jobDate) < 0) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(JobRequestUserDetail.this);
                            builder1.setMessage("Are you sure you want to cancel?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            if (InternetDetect.isConnected(JobRequestUserDetail.this)) {
                                                new JsonCancelJob().execute(ProblemId, sessionUser.getId(), pendingRequestBean.getId());
                                            } else {

                                                Toast.makeText(JobRequestUserDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                            }

                                            dialog.cancel();
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        } else {

                            Toast.makeText(JobRequestUserDetail.this, "You can't cancel this job", Toast.LENGTH_SHORT).show();
                            Log.e("Else Compare Date", ">>>>" + currentDate.compareTo(jobDate));
                        }
                    }
                });

                if (pendingRequestBean.getProblem().getReason().equalsIgnoreCase("Need to quote")) {
                    StatusDetailUserTV.setText(pendingRequestBean.getProblem().getReason());
                    StatusDetailUserTV.setTextColor(Color.parseColor("#FF0000"));


                } else if (pendingRequestBean.getProblem().getReason().equalsIgnoreCase("Ordered parts")) {
                    StatusDetailUserTV.setText(pendingRequestBean.getProblem().getReason());
                    StatusDetailUserTV.setTextColor(Color.parseColor("#ff33b5e5"));

                } else if (pendingRequestBean.getProblem().getReason().equalsIgnoreCase("Job has been Quoted")) {
                    StatusDetailUserTV.setText(pendingRequestBean.getProblem().getReason());
                    StatusDetailUserTV.setTextColor(Color.parseColor("#32CD32"));

                } else if (pendingRequestBean.getProblem().getReason().equalsIgnoreCase("Need to return")) {

                    StatusDetailUserTV.setText(pendingRequestBean.getProblem().getReason());
                    StatusDetailUserTV.setTextColor(Color.parseColor("#000000"));
                }


            }
        }
    }

    private class JsonCancelJob extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "problem_canceled_by_user&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", paramss[0]);
                params.put("user_id", paramss[1]);
                params.put("trademan_id", paramss[2]);

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
                Log.e("JsonCancel", response);

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

            progressDialog.dismiss();
            Toast.makeText(JobRequestUserDetail.this, "Cancelled", Toast.LENGTH_LONG).show();
            finish();

            if (result == null) {
            } else if (result.equalsIgnoreCase("CENCEL BY USER")) {}

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {

                RescheduleBT.setEnabled(false);

                if (InternetDetect.isConnected(JobRequestUserDetail.this)) {

                    new JsonReschedule().execute(problemId, trademanId);

                } else {
                    Toast.makeText(JobRequestUserDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private class JsonReschedule extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.e("On Pre", "Yes");
        }

        @Override
        protected String doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "reschedule_status_update&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("table_id", paramss[0]);
                params.put("Tradesman_id", paramss[1]);

                params.put("date", Appconstants.DATE_SELECTED);
                params.put("time", Appconstants.TIME_SELECTED);
                params.put("IsDateFlexible", Appconstants.dateFlexi);
                params.put("IsTimeFlexible", Appconstants.timeFlexi);

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
                Log.e("JsonCancel", response);

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

                RescheduleBT.setEnabled(true);
            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(JobRequestUserDetail.this, "Reschedule Request Sent", Toast.LENGTH_LONG).show();
                finish();
            } else {

                RescheduleBT.setEnabled(true);
            }

        }

    }

    private class JsonJobDone extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "problem_completed_by_user&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", strings[0]);
                params.put("user_id", strings[1]);
                params.put("status", strings[2]);
                if (!(strings[2].equalsIgnoreCase("2"))) {

                    params.put("sub_status", strings[3]);
                }


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
                Log.e("JsonJobDone", response);

                JSONArray array = new JSONArray(response);
                String result = array.getJSONObject(0).getString("result");

                //[{"id":"19","date":"25-04-2017","time":"6:43 AM","IsDateFlexible":"yes","IsTimeFlexible":"yes","PersonOnSite":"gaurav rathore","Job_Address":"656 sneh nagr, sapna sangeeta, 6603 Lake Hinds, indore","Home_Number":"6666","Mobile_Number":"9999999","Job_Request":"","user_id":"1","Tradesman_id":"1","problem":"Emergency,Commercial Repairs","order_status":"ACCEPTED","schedule":"NO","noti_status":"0","result":"successfully"}]

                return result;

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

                finish();
            }
        }
    }
}

