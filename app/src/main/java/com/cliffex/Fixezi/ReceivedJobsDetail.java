package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ReceivedJobsDetail extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview, ProblemDetail, DateDetail, TimeDetail, FlexibleDateDetail,
            FlexibleTimeDetail, PersonOnSiteDetail, JobAddressDetail, TimeFlexibilityTVRJD;
    TextView HomeNmberDetail, MobileNmberDetail, JobRequestDetail, FullNameDetail, FullAddressDetailUser, HomeNmberDetailUser, WorkNmberDetailUser, MobileNmberDetailUser, EmailDetail;
    RelativeLayout RLDetail;
    RelativeLayout NavigationUpIM;
    TextView IsJobDoneTV, ViewNotesTV2,AddNotesTV;

    String ProblemId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_jobs_detail);

        RLDetail = (RelativeLayout) findViewById(R.id.RLDetailScheduleRJD);
        ProblemDetail = (TextView) findViewById(R.id.ProblemDetailScheduleRJD);
        DateDetail = (TextView) findViewById(R.id.DateDetailScheduleRJD);
        TimeDetail = (TextView) findViewById(R.id.TimeDetailScheduleRJD);
        FlexibleDateDetail = (TextView) findViewById(R.id.FlexibleDateDetailScheduleRJD);
        FlexibleTimeDetail = (TextView) findViewById(R.id.FlexibleTimeDetailScheduleRJD);
        AddNotesTV = (TextView) findViewById(R.id.AddNotesTV);
        PersonOnSiteDetail = (TextView) findViewById(R.id.PersonOnSiteDetailScheduleRJD);
        JobAddressDetail = (TextView) findViewById(R.id.JobAddressDetailScheduleRJD);
        HomeNmberDetail = (TextView) findViewById(R.id.HomeNmberDetailScheduleRJD);
        MobileNmberDetail = (TextView) findViewById(R.id.MobileNmberDetailScheduleRJD);
        JobRequestDetail = (TextView) findViewById(R.id.JobRequestDetailScheduleRJD);
        FullNameDetail = (TextView) findViewById(R.id.FullNameDetailScheduleRJD);
        FullAddressDetailUser = (TextView) findViewById(R.id.FullAddressDetailUserScheduleRJD);
        HomeNmberDetailUser = (TextView) findViewById(R.id.HomeNmberDetailUserScheduleRJD);
        WorkNmberDetailUser = (TextView) findViewById(R.id.WorkNmberDetailUserScheduleRJD);
        MobileNmberDetailUser = (TextView) findViewById(R.id.MobileNmberDetailUserScheduleRJD);
        EmailDetail = (TextView) findViewById(R.id.EmailDetailScheduleRJD);
        TimeFlexibilityTVRJD = (TextView) findViewById(R.id.TimeFlexibilityTVRJD);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        IsJobDoneTV = (TextView) findViewById(R.id.IsJobDoneTV);
        ViewNotesTV2 = (TextView) findViewById(R.id.ViewNotesTV2);

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

            Toast.makeText(ReceivedJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        AddNotesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReceivedJobsDetail.this,AddNotes.class);
                startActivity(intent);

            }
        });
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
                problem.setHome_Number(ProblemObject.getString("Home_Number"));
                problem.setMobile_Number(ProblemObject.getString("Mobile_Number"));
                problem.setJob_Request(ProblemObject.getString("Job_Request"));
                problem.setUser_id(ProblemObject.getString("user_id"));
                problem.setProblem(ProblemObject.getString("problem"));
                problem.setStatus(ProblemObject.getString("order_status"));
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
                HomeNmberDetail.setText(result.getProblem().getHome_Number());
                MobileNmberDetail.setText(result.getProblem().getMobile_Number());
                JobRequestDetail.setText(result.getProblem().getJob_Request());
                TimeFlexibilityTVRJD.setText(result.getProblem().getIsTimeFlexible_value());
                FullNameDetail.setText(result.getName());

                String FullAddress = result.getHome_address() + ", " + result.getPost_code() + ", " + result.getCity();
                FullAddressDetailUser.setText(FullAddress);

                HomeNmberDetailUser.setText(result.getHome_phone());
                WorkNmberDetailUser.setText(result.getWork_phone());
                MobileNmberDetailUser.setText(result.getMobile_phone());
                EmailDetail.setText(result.getEmail());

                if (result.getProblem().getStatus().equalsIgnoreCase("ACCEPTED")) {
                    IsJobDoneTV.setEnabled(true);
                    IsJobDoneTV.setVisibility(View.VISIBLE);
                }else if (result.getProblem().getStatus().equalsIgnoreCase("RESCHEDULE")) {
                    IsJobDoneTV.setEnabled(true);
                    IsJobDoneTV.setVisibility(View.VISIBLE);
                } else {
                    IsJobDoneTV.setVisibility(View.GONE);
                    IsJobDoneTV.setEnabled(false);
                }

                ProblemId = result.getProblem().getId();

                ViewNotesTV2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ReceivedJobsDetail.this, ViewNotes.class);
                        intent.putExtra("ProblemId", ProblemId);
                        startActivity(intent);


                    }
                });

                IsJobDoneTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final String[] strings = {"Completed", "Incomplete"};

                        new AlertDialog.Builder(ReceivedJobsDetail.this)
                                .setTitle("Select One")
                                .setSingleChoiceItems(strings, 0, null)

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int whichButton) {

                                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                        if (selectedPosition == 0) {

                                            final String[] strings = {"Paid with cash", "Paid Eft", "Paid with cheque", "Paid with online banking", "Arranged payment plan", "Admin to email invoice"};

                                            new AlertDialog.Builder(ReceivedJobsDetail.this)
                                                    .setTitle("Select One")
                                                    .setSingleChoiceItems(strings, 0, null)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, int whichButton) {

                                                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                                            if (InternetDetect.isConnected(ReceivedJobsDetail.this)) {

                                                                new JsonJobDone().execute(result.getProblem().getId(), result.getProblem().getUser_id(), "0", strings[selectedPosition]);
                                                            } else {
                                                                Toast.makeText(ReceivedJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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

                                            new AlertDialog.Builder(ReceivedJobsDetail.this)
                                                    .setTitle("Select One")
                                                    .setSingleChoiceItems(strings, 0, null)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, int whichButton) {

                                                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                                            System.out.println("what is posion>>>"+selectedPosition);

                                                            if (InternetDetect.isConnected(ReceivedJobsDetail.this)) {

                                                                new JsonJobDone().execute(result.getProblem().getId(), result.getProblem().getUser_id(), "0", strings[selectedPosition]);

                                                            } else {

                                                                Toast.makeText(ReceivedJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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

                                           /* if (InternetDetect.isConnected(ReceivedJobsDetail.this)) {

                                                new JsonJobDone().execute(result.getProblem().getId(), result.getProblem().getUser_id(), "2");
                                            } else {
                                                Toast.makeText(ReceivedJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                            }*/




                                        } else {

                                            if (InternetDetect.isConnected(ReceivedJobsDetail.this)) {

                                                new JsonJobDone().execute(result.getProblem().getId(), result.getProblem().getUser_id(), "1", strings[selectedPosition]);
                                            } else {
                                                Toast.makeText(ReceivedJobsDetail.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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
