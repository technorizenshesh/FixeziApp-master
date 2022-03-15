package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.Model.WorkerBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllJobs extends AppCompatActivity {

    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    RecyclerView recyclerView;
    RelativeLayout NavigationUpIM;
    List<WorkerBean> workerBeanList;
    TextView NoJobsFoundTV2;
    private String order_status;
    private TextView rut;
    private Spanned textstyle;
    private JSONObject forword;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_all_jobs);

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        rut = (TextView) findViewById(R.id.rut);

        textstyle = Html.fromHtml("<span style=color:#49A2F0>" +
                "'Any Accepted job 'Can be Exported / Cancelled / Forworded " +
                "and you can remove any 'Cancelled / Rejected / Exported Job " +
                "</span>" + "<span style=color:white>" +
                "Just press and hold a job to bring up the menu " + "</span>");

        rut.setText(textstyle);


        // rut.setText(" ");
        toolbar_textview.setText("All Jobs");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.RVAllJobs);
        NoJobsFoundTV2 = (TextView) findViewById(R.id.NoJobsFoundTV2);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (InternetDetect.isConnected(this)) {
            new JsonGetAllJob().execute();
            new JsonGetWorker().execute(sessionTradesman.getId());
        } else {
            Toast.makeText(AllJobs.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class JsonGetAllJob extends AsyncTask<String, String, List<IncomingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {

            Log.e("Tradesman_id", sessionTradesman.getId());

            try {

                URL url = new URL(HttpPAth.Urlpath + "get_all_product_detaits&");

                Map<String, Object> params = new LinkedHashMap<>();

                params.put("Tradesman_id", sessionTradesman.getId());

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

                Log.e("JsonAllJob", response);

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
                    incomingRequestBean.setTradesmanName(object.getString("Tradesman_name"));

              /*       forword = object.getJSONObject("forword");

                     name =  forword.getString("name");*/


                    JSONArray ProblemArray = object.getJSONArray("problem");
                    JSONObject ProblemObject = ProblemArray.getJSONObject(0);

                    IncomingRequestBean.Problem problem = new IncomingRequestBean.Problem();

                    order_status = ProblemObject.getString("order_status");

                    Log.e("OrderStatus", order_status);

                    if (order_status.equalsIgnoreCase("ACCEPTED") || order_status.equalsIgnoreCase("REJECTED")||order_status.equalsIgnoreCase("PROCESS")) {

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
                        problem.setOrder_status(ProblemObject.getString("order_status"));
                        problem.setSchedule(ProblemObject.getString("schedule"));
                        problem.setStatus(ProblemObject.getString("status"));
                        incomingRequestBean.setProblem(problem);
                        incomingRequestListBeanList.add(incomingRequestBean);
                    }


                    Log.e("ListSize", "??" + incomingRequestListBeanList.size());
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

            Log.e("fasdasdadasd","result = " + new Gson().toJson(result) );

            if (result == null) {
                NoJobsFoundTV2.setVisibility(View.VISIBLE);
            } else if (result.isEmpty()) {
                NoJobsFoundTV2.setVisibility(View.VISIBLE);
            } else {
                AlbumsAdapter adapter = new AlbumsAdapter(AllJobs.this, result);
                recyclerView.setAdapter(adapter);
            }

        }

    }

    private class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

        private Context mContext;
        List<IncomingRequestBean> incomingRequestListBean;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView ProblemJobRequest, DateJobRequest, TimeJobRequest, FlexibleDateJobRequest,
                    FlexibleTimeJobRequest, StatusTV, NumberTv, jobforwordto;

            TextView residentialbtn, commercialbtn;

            public MyViewHolder(View view) {
                super(view);

                ProblemJobRequest = (TextView) view.findViewById(R.id.ProblemJobRequest);
                DateJobRequest = (TextView) view.findViewById(R.id.DateJobRequest);
                TimeJobRequest = (TextView) view.findViewById(R.id.TimeJobRequest);
                FlexibleDateJobRequest = (TextView) view.findViewById(R.id.FlexibleDateJobRequest);
                FlexibleTimeJobRequest = (TextView) view.findViewById(R.id.FlexibleTimeJobRequest);
                StatusTV = (TextView) view.findViewById(R.id.StatusTV);
                NumberTv = (TextView) view.findViewById(R.id.NumberTv);
                residentialbtn = (TextView) view.findViewById(R.id.residentialbtn);
                commercialbtn = (TextView) view.findViewById(R.id.commercialbtn);
                jobforwordto = (TextView) view.findViewById(R.id.jobforwordto);

            }
        }

        public AlbumsAdapter(Context mContext, List<IncomingRequestBean> incomingRequestListBean1) {
            this.mContext = mContext;
            incomingRequestListBean = incomingRequestListBean1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_all_jobs, parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                    .equalsIgnoreCase("REJECTED")) {

                holder.StatusTV.setText("REJECTED BY TRADESMAN");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));
                holder.residentialbtn.setText(" ADMIN ");
                holder.jobforwordto.setText("Job Was Sent To");
                String PersonOnSite = incomingRequestListBean.get(position).getTradesmanName();
                holder.commercialbtn.setText(PersonOnSite);

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                    .equalsIgnoreCase("PROCESS")) {

                holder.StatusTV.setText("IN PROCESS");
                holder.StatusTV.setTextColor(Color.parseColor("#c79202"));
                holder.residentialbtn.setText(" ADMIN ");
                holder.jobforwordto.setText("Job Was Sent To");
                String PersonOnSite = incomingRequestListBean.get(position).getTradesmanName();
                holder.commercialbtn.setText(PersonOnSite);

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                    .equalsIgnoreCase("RESCHEDULE")) {

                holder.residentialbtn.setText(" ADMIN ");
                holder.StatusTV.setText("RESCHEDULE");
                holder.StatusTV.setTextColor(Color.parseColor("#DAA520"));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                      .equalsIgnoreCase("ACCEPTED")) {

                holder.StatusTV.setText("ACCEPTED");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));

                holder.jobforwordto.setText("Job Was Sent To");

                String PersonOnSite = incomingRequestListBean.get(position).getTradesmanName();
                holder.commercialbtn.setText(PersonOnSite);
                holder.commercialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.colorBlue));
                holder.residentialbtn.setText(" ADMIN ");

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("COMPLETED") ||
                    incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("RATED")) {

                holder.StatusTV.setText("COMPLETED");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));
                holder.residentialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                holder.residentialbtn.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red));
                holder.residentialbtn.setText("USER");

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                    .equalsIgnoreCase("CANCELLED")) {

                holder.StatusTV.setText("CANCELLED BY USER");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                holder.residentialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                holder.residentialbtn.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red));
                holder.residentialbtn.setText("USER");

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                    .equalsIgnoreCase("PENDING")) {

                holder.StatusTV.setText("PENDING");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                holder.jobforwordto.setText("Job Was Sent  to ");

                String PersonOnSite = incomingRequestListBean.get(position).getProblem().getPersonOnSite();
                holder.commercialbtn.setText(PersonOnSite);
                holder.commercialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.colorBlue));
                holder.residentialbtn.setBackgroundColor(ContextCompat.getColor(mContext,R.color.red));
                holder.residentialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                holder.residentialbtn.setText("USER");
            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status()
                    .equalsIgnoreCase("JOB EXPORTED")) {

                holder.StatusTV.setText("EXPORTED BY TRADESMAN");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                holder.jobforwordto.setText("Job Was Sent  to ");

                String PersonOnSite = incomingRequestListBean.get(position).getProblem().getPersonOnSite();
                holder.commercialbtn.setText(PersonOnSite);
                holder.commercialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.colorBlue));
                holder.residentialbtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                holder.residentialbtn.setText(" ADMIN ");

            }

            holder.NumberTv.setText("Job " + incomingRequestListBean.get(position).getProblem().getId());
            holder.ProblemJobRequest.setText(incomingRequestListBean.get(position).getProblem().getProblem());
            holder.DateJobRequest.setText(incomingRequestListBean.get(position).getProblem().getDate());
            holder.TimeJobRequest.setText(incomingRequestListBean.get(position).getProblem().getTime());
            holder.FlexibleDateJobRequest.setText(incomingRequestListBean.get(position).getProblem().getIsDateFlexible());
            holder.FlexibleTimeJobRequest.setText(incomingRequestListBean.get(position).getProblem().getIsTimeFlexible());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("PROCESS")) {
                     } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("REJECTED")) {
                     } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("CANCELLED")) {
                     } else {
                         Intent intent = new Intent(AllJobs.this, AllJobsDetail.class);
                         intent.putExtra("ProblemId",incomingRequestListBean.get(position).getProblem().getId());
                         startActivity(intent);
                     }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    String Status = incomingRequestListBean.get(position).getProblem().getStatus();

                    if (Status.equalsIgnoreCase("REJECTED") ||
                            Status.equalsIgnoreCase("COMPLETED") ||
                            Status.equalsIgnoreCase("CANCELLED") ||
                            Status.equalsIgnoreCase("RATED") ||
                            Status.equalsIgnoreCase("NOTFIXED")) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AllJobs.this);
                        builder1.setMessage("Do you want to remove this jo ?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (InternetDetect.isConnected(mContext)) {
                                            new JsonDelete().execute(incomingRequestListBean.get(position).getProblem().getId());
                                        } else {
                                            Toast.makeText(mContext, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.cancel();
                                    }

                                    class JsonDelete extends AsyncTask<String, String, String> {

                                        String JsonResult = "";

                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();

                                        }

                                        @Override
                                        protected String doInBackground(String... paramss) {

                                            try {

                                                URL url = new URL(HttpPAth.Urlpath + "delete_booking_problem&");
                                                Map<String, Object> params = new LinkedHashMap<>();
                                                params.put("problem_id", paramss[0]);


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
                                                Log.e("JsonDelete", response);

                                                JSONObject FinalObject = new JSONObject(response);
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

                                                Toast.makeText(AllJobs.this, "Deleted", Toast.LENGTH_SHORT).show();

                                                incomingRequestListBean.remove(position);
                                                notifyDataSetChanged();

                                            }

                                        }

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


                    } else if (Status.equalsIgnoreCase("ACCEPTED")) {


                        String[] optionsArray = {"Cancel Job", "Forward this job to"};
                        new AlertDialog.Builder(AllJobs.this)
                                .setTitle("Select Option")
                                .setSingleChoiceItems(optionsArray, 0, null)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                        if (selectedPosition == 0) {


                                            Calendar c = Calendar.getInstance();

                                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                            String currentDateStr = df.format(c.getTime());
                                            Date currentDate = null, jobDate = null;
                                            try {

                                                currentDate = df.parse(currentDateStr);
                                                jobDate = df.parse(incomingRequestListBean.get(position).getProblem().getDate());

                                                Log.e("Current Date", currentDateStr);
                                                Log.e("Job Date", incomingRequestListBean.get(position).getProblem().getDate());
                                                Log.e("Compare Date", ">>>" + currentDate.compareTo(jobDate));


                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                            if (currentDate.compareTo(jobDate) < 0) {

                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(AllJobs.this);
                                                builder1.setMessage("Are you sure you want to cancel?");
                                                builder1.setCancelable(true);

                                                builder1.setPositiveButton(
                                                        "Yes",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                if (InternetDetect.isConnected(AllJobs.this)) {
                                                                    new JsonCancelJob().execute(incomingRequestListBean.get(position).getProblem().getId(), incomingRequestListBean.get(position).getProblem().getUser_id(), sessionTradesman.getId());
                                                                } else {

                                                                    Toast.makeText(AllJobs.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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

                                                Log.e("Else Compare Date", ">>>>" + currentDate.compareTo(jobDate));
                                            }


                                        } else {


                                            if (!(workerBeanList.isEmpty())) {


                                                String[] workerNameArray = new String[workerBeanList.size()];

                                                for (int i = 0; i < workerBeanList.size(); i++) {

                                                    workerNameArray[i] = workerBeanList.get(i).getName();

                                                }

                                                new AlertDialog.Builder(AllJobs.this)
                                                        .setTitle("Forward this job to")
                                                        .setSingleChoiceItems(workerNameArray, 0, null)
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                dialog.dismiss();
                                                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                                                if (InternetDetect.isConnected(AllJobs.this)) {

                                                                    new JsonForwardJob().execute(workerBeanList.get(selectedPosition).getId(), incomingRequestListBean.get(position).getProblem().getId());
                                                                } else {
                                                                    Toast.makeText(AllJobs.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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

                                                                        Toast.makeText(AllJobs.this, "Forward", Toast.LENGTH_SHORT).show();

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

                                                Toast.makeText(AllJobs.this, "You have no workers", Toast.LENGTH_SHORT).show();
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

                    }

                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {

            return incomingRequestListBean == null ? 0 : incomingRequestListBean.size();

        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = 10; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private class JsonGetWorker extends AsyncTask<String, String, List<WorkerBean>> {

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

    private class JsonCancelJob extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... paramss) {

            http:
            try {
                URL url = new URL(HttpPAth.Urlpath + "problem_canceled_by_trademan_user&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("problem_id", paramss[0]);
                params.put("user_id", paramss[1]);
                params.put("Tradesman_id", paramss[2]);

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


            if (result == null) {

            } else if (result.equalsIgnoreCase("CENCEL BY USER")) {

                Toast.makeText(AllJobs.this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
