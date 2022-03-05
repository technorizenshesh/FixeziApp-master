package com.cliffex.Fixezi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class ReceivedJobs extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    RecyclerView recyclerView;
    SessionWorker sessionWorker;
    TextView NoJobsFoundTV3;
    int countReschedule,countpending,countincompleted,countAccepted,countcancel,countcompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_jobs);

        sessionWorker = new SessionWorker(this);
        toolbar = (Toolbar) findViewById(R.id.ToolbarRJ);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        toolbar_textview.setText("Received Jobs");

        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.RVReceivedJobs);
        NoJobsFoundTV3 = (TextView) findViewById(R.id.NoJobsFoundTV3);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

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
                    for (int j=0;j<ProblemArray.length();j++){
                        JSONObject obj=ProblemArray.getJSONObject(j);
                        if (obj.getString("order_status").equals("RESCHEDULE")){
                            countReschedule++;
                        }else if (obj.getString("order_status").equals("PENDING")){
                            countpending++;
                        }else if (obj.getString("status").equals("INCOMPLETED")){
                            countincompleted++;
                        }else if (obj.getString("status").equals("ACCEPTED")){
                            countAccepted++;
                        }else if (obj.getString("status").equals("COMPLETED")){
                            countcompleted++;
                        }else if (obj.getString("status").equals("CANCELLED")){
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

                NoJobsFoundTV3.setVisibility(View.VISIBLE);
            } else if (result.isEmpty()) {

                NoJobsFoundTV3.setVisibility(View.VISIBLE);
            } else {

                JobAdapter adapter = new JobAdapter(ReceivedJobs.this, result);
                recyclerView.setAdapter(adapter);
            }
        }
    }


    private class JobAdapter extends RecyclerView.Adapter<JobAdapter.MyViewHolder> {

        private Context mContext;
        List<IncomingRequestBean> incomingRequestListBean;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView ProblemJobRequest, DateJobRequest, TimeJobRequest, FlexibleDateJobRequest, FlexibleTimeJobRequest, StatusTV, NumberTv;

            public MyViewHolder(View view) {
                super(view);
                ProblemJobRequest = (TextView) view.findViewById(R.id.ProblemJobRequest);
                DateJobRequest = (TextView) view.findViewById(R.id.DateJobRequest);
                TimeJobRequest = (TextView) view.findViewById(R.id.TimeJobRequest);
                FlexibleDateJobRequest = (TextView) view.findViewById(R.id.FlexibleDateJobRequest);
                FlexibleTimeJobRequest = (TextView) view.findViewById(R.id.FlexibleTimeJobRequest);
                StatusTV = (TextView) view.findViewById(R.id.StatusTV);
                NumberTv = (TextView) view.findViewById(R.id.NumberTv);

            }
        }

        public JobAdapter(Context mContext, List<IncomingRequestBean> incomingRequestListBean1) {
            this.mContext = mContext;
            incomingRequestListBean = incomingRequestListBean1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_request_cardview, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("REJECTED")) {


                holder.StatusTV.setText("REJECTED");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));

            } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("PROCESS")) {

                holder.StatusTV.setText("PROCESS");
                holder.StatusTV.setTextColor(Color.parseColor("#DAA520"));

            } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("RESCHEDULE")) {

                holder.StatusTV.setText("RESCHEDULE");
                holder.StatusTV.setTextColor(Color.parseColor("#DAA520"));

            } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("ACCEPTED")) {

                holder.StatusTV.setText("ACCEPTED");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));

            } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("COMPLETED") || incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("RATED")) {

                holder.StatusTV.setText("COMPLETED");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));

            } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("CANCELLED")) {

                holder.StatusTV.setText("CANCELLED");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));
            } else if (incomingRequestListBean.get(position).getProblem().getStatus().equalsIgnoreCase("PENDING")) {

                holder.StatusTV.setText("PENDING");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));
            }


            holder.ProblemJobRequest.setText(incomingRequestListBean.get(position).getProblem().getProblem());
            holder.DateJobRequest.setText(incomingRequestListBean.get(position).getProblem().getDate());
            holder.TimeJobRequest.setText(incomingRequestListBean.get(position).getProblem().getTime());
            holder.FlexibleDateJobRequest.setText(incomingRequestListBean.get(position).getProblem().getIsDateFlexible());
            holder.FlexibleTimeJobRequest.setText(incomingRequestListBean.get(position).getProblem().getIsTimeFlexible());
            holder.NumberTv.setText("Job " + incomingRequestListBean.get(position).getProblem().getId());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ReceivedJobs.this, ReceivedJobsDetail.class);
                    intent.putExtra("ProblemId", incomingRequestListBean.get(position).getProblem().getId());
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {

            return incomingRequestListBean == null ? 0 : incomingRequestListBean.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (InternetDetect.isConnected(this)) {

            new JsonTaskGetRecievedJob().execute();
        } else {

            Toast.makeText(ReceivedJobs.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();

        }
    }
}