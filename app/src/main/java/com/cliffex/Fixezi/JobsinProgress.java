package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.PendingRequestBean;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

/**
 * Created by technorizen8 on 17/3/16.
 */
public class JobsinProgress extends AppCompatActivity {

    Toolbar jobinprogress_toolbar;
    TextView toolbar_title;
    ListView ListViewJobs;
    SessionUser sessionUser;
    ProgressDialog progressDialog;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.Please Wait ......");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs_in_progress);

        jobinprogress_toolbar = (Toolbar) findViewById(R.id.jobinprogress_toolbar);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);


        toolbar_title = (TextView) jobinprogress_toolbar.findViewById(R.id.toolbar_title);
        ListViewJobs = (ListView) findViewById(R.id.ListViewJobs);
        sessionUser = new SessionUser(this);

        toolbar_title.setText("Jobs in Progress");
        setSupportActionBar(jobinprogress_toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (InternetDetect.isConnected(this)) {

            new JsonTaskGetAllJobRequest().execute();
        } else {

            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public class JsonTaskGetAllJobRequest extends AsyncTask<String, String, List<PendingRequestBean>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            Log.e("In Pre", "yes");
        }

        @Override
        protected List<PendingRequestBean> doInBackground(String... paramss) {
            Log.e("In back", "yes");

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_bookingList_accept_jobs&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", sessionUser.getId());


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
                Log.e("JsonPendingRequest", response);

                List<PendingRequestBean> pendingRequestListBeanList = new ArrayList<PendingRequestBean>();


                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    PendingRequestBean pendingRequestBean = new PendingRequestBean();
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
                    problem.setSchedule(ProblemObject.getString("schedule"));


                    pendingRequestBean.setProblem(problem);
                    pendingRequestListBeanList.add(pendingRequestBean);

                    Log.e("ListSize", "??" + pendingRequestListBeanList.size());
                }


                return pendingRequestListBeanList;

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
        protected void onPostExecute(List<PendingRequestBean> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result == null) {

            } else {
                ListViewAdapter adapter = new ListViewAdapter(JobsinProgress.this, result);
                ListViewJobs.setAdapter(adapter);
            }
        }

    }


    private class ListViewAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        private List<PendingRequestBean> data;


        public ListViewAdapter(Context context, List<PendingRequestBean> data) {


            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.data = data;
        }

        public class ViewHolder {
            TextView BusinessNameJIP, JobTimeJIP, ClickHereTV, JobDateJIP;
        }

        @Override
        public int getCount() {

            return data.size();

        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.jobs_in_progress_rowitem, null);

                holder.BusinessNameJIP = (TextView) view.findViewById(R.id.BusinessNameJIP);
                holder.JobTimeJIP = (TextView) view.findViewById(R.id.JobTimeJIP);
                holder.JobDateJIP = (TextView) view.findViewById(R.id.JobDateJIP);
                holder.ClickHereTV = (TextView) view.findViewById(R.id.ClickHereTV);

                view.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.BusinessNameJIP.setText(data.get(position).getBusiness_name());
            holder.JobTimeJIP.setText(data.get(position).getProblem().getTime());
            holder.JobDateJIP.setText(data.get(position).getProblem().getDate());

            holder.ClickHereTV.setOnClickListener(new View.OnClickListener() {

                Date JobDate;

                @Override
                public void onClick(View view) {

                    /*Date today=new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
                    String Todayformat = formatter.format(today);


                    Log.e("Today",">>>"+Todayformat);
                    Log.e("Jobdate",">>>"+data.get(position).getProblem().getDate());


                    String JobDateSTR=data.get(position).getProblem().getDate() ;
                    DateFormat format = new SimpleDateFormat("dd-MM-yyy", Locale.ENGLISH);


                    try {
                        JobDate = format.parse(JobDateSTR);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (today.after(JobDate)){

                        Log.e("IF","yes");

                        Intent intent = new Intent(JobsinProgress.this, RatingSystem.class);
                        intent.putExtra("TradesmanId", data.get(position).getProblem().getTradesman_id());
                        intent.putExtra("ProblemId", data.get(position).getProblem().getId());
                        startActivity(intent);

                    }else if (today.before(JobDate)){

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(JobsinProgress.this);
                        builder1.setMessage("You cannot rate this tradesman untill your scheduled job is done.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    }else if (today.equals(JobDate)){


                        Intent intent = new Intent(JobsinProgress.this, RatingSystem.class);
                        intent.putExtra("TradesmanId", data.get(position).getProblem().getTradesman_id());
                        intent.putExtra("ProblemId", data.get(position).getProblem().getId());
                        startActivity(intent);

                    }*/

                    Intent intent = new Intent(JobsinProgress.this, RatingSystem.class);
                    intent.putExtra("TradesmanId", data.get(position).getProblem().getTradesman_id());
                    intent.putExtra("ProblemId", data.get(position).getProblem().getId());
                    startActivity(intent);

                }
            });


            return view;
        }

    }

}
