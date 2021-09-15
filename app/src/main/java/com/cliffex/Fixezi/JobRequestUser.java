package com.cliffex.Fixezi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.Model.PendingRequestBean;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JobRequestUser extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview,rut;
    SessionUser sessionUser;
    RecyclerView recyclerView;
    Context mContext = JobRequestUser.this;
    RelativeLayout NavigationUpIM;
    AlbumsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_request_user);
        sessionUser = new SessionUser(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        rut = (TextView) findViewById(R.id.rut);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Job Request");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rut.setText(Html.fromHtml("<font color='#49A2F0'>You can delete cancelled jobs, </font> just select the job you wish to delete"), TextView.BufferType.SPANNABLE);

        recyclerView = (RecyclerView) findViewById(R.id.RVJobRequest);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AlbumsAdapter(mContext,null);

    }

    private class JsonTaskGetAllJobRequest extends AsyncTask<String, String, List<PendingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("In Pre", "yes");
        }

        @Override
        protected List<PendingRequestBean> doInBackground(String... paramss) {
            Log.e("In back", "yes");

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_bookingList_by_userid&");
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

            if (result==null) {

            } else {

                for(int i=0;i<result.size();i++) {
                   if(result.get(i).getProblem().getOrder_status().equalsIgnoreCase("COMPLETED") ||
                      result.get(i).getProblem().getOrder_status().equalsIgnoreCase("RATED")) {
                      result.remove(i);
                      i--;
                   }
                }

                adapter = new AlbumsAdapter(JobRequestUser.this, result);
                recyclerView.setAdapter(adapter);

            }

        }


    }

    private class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

        private Context mContext;
        List<PendingRequestBean> pendingRequestListBean;

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

        public AlbumsAdapter(Context mContext, List<PendingRequestBean> pendingRequestListBean) {
            this.mContext = mContext;
            this.pendingRequestListBean = pendingRequestListBean;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_request_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,@SuppressLint("RecyclerView") final int position) {

            holder.ProblemJobRequest.setText(pendingRequestListBean.get(position).getProblem().getProblem());
            holder.DateJobRequest.setText(pendingRequestListBean.get(position).getProblem().getDate());
            holder.TimeJobRequest.setText(pendingRequestListBean.get(position).getProblem().getTime());
            holder.FlexibleDateJobRequest.setText(pendingRequestListBean.get(position).getProblem().getIsDateFlexible());
            holder.FlexibleTimeJobRequest.setText(pendingRequestListBean.get(position).getProblem().getIsTimeFlexible());
            holder.NumberTv.setText("Job " + pendingRequestListBean.get(position).getProblem().getId());

            if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("PROCESS")) {
                holder.StatusTV.setText("Waiting on tradesman's replay");
                holder.StatusTV.setTextColor(Color.parseColor("#DAA520"));
            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("RESCHEDULE")) {
                holder.StatusTV.setText("Reschedule");
                holder.StatusTV.setTextColor(Color.parseColor("#DAA520"));

            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("ACCEPTED")) {
                holder.StatusTV.setText("Accepted");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));

            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("REJECTED")) {

                holder.StatusTV.setText("REJECTED BY TRADESMAN");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));
            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("PENDING")) {

                holder.StatusTV.setText("Pending");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));
            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("COMPLETED") || pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("RATED")) {

                holder.StatusTV.setText("Completed");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));
            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("CANCELLED")) {

                holder.StatusTV.setText("CANCELLED BY USER");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));

            } else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("JOB EXPORTED")) {
                holder.StatusTV.setText("EXPORTED BY TRADESMAN");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));
            }

            /*else if (pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("NOTFIXED")) {

                holder.StatusTV.setText("Can't be fixed");
                holder.StatusTV.setTextColor(Color.parseColor("#FF0000"));

            }*/

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("CANCELLED") ||
                            pendingRequestListBean.get(position).getProblem().getOrder_status().equalsIgnoreCase("REJECTED")){
                        cancelledJobDialog(pendingRequestListBean,pendingRequestListBean.get(position).getProblem().getOrder_status(),position);
                    } else {
                        Intent intent = new Intent(JobRequestUser.this, JobRequestUserDetail.class);
                        intent.putExtra("ProblemId", pendingRequestListBean.get(position).getProblem().getId());
                        startActivity(intent);
                    }
                }
            });

//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    String Status = pendingRequestListBean.get(position).getProblem().getOrder_status();
//
//                    if (Status.equalsIgnoreCase("REJECTED") ||
//                            Status.equalsIgnoreCase("COMPLETED") ||
//                            Status.equalsIgnoreCase("CANCELLED") ||
//                            Status.equalsIgnoreCase("RATED") ||
//                            Status.equalsIgnoreCase("NOTFIXED")) {
//
//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(JobRequestUser.this);
//                        builder1.setMessage("Do you want to remove this job ?");
//                        builder1.setCancelable(true);
//
//                        builder1.setPositiveButton(
//                                "Yes",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        new JsonDelete().execute(pendingRequestListBean.get(position).getProblem().getId());
//                                        dialog.cancel();
//                                    }
//
//                                    class JsonDelete extends AsyncTask<String, String, String> {
//
//                                        String JsonResult = "";
//
//                                        @Override
//                                        protected void onPreExecute() {
//                                            super.onPreExecute();
//                                        }
//
//                                        @Override
//                                        protected String doInBackground(String... paramss) {
//                                            try {
//                                                URL url = new URL(HttpPAth.Urlpath + "delete_booking_problem&");
//                                                Map<String, Object> params = new LinkedHashMap<>();
//                                                params.put("problem_id", paramss[0]);
//
//                                                StringBuilder postData = new StringBuilder();
//                                                for (Map.Entry<String, Object> param : params.entrySet()) {
//                                                    if (postData.length() != 0)
//                                                        postData.append('&');
//                                                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//                                                    postData.append('=');
//                                                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//                                                }
//                                                String urlParameters = postData.toString();
//                                                URLConnection conn = url.openConnection();
//
//                                                conn.setDoOutput(true);
//
//                                                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//
//                                                writer.write(urlParameters);
//                                                writer.flush();
//
//                                                String response = "";
//                                                String line;
//                                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//                                                while ((line = reader.readLine()) != null) {
//                                                    response += line;
//                                                }
//                                                writer.close();
//                                                reader.close();
//                                                Log.e("JsonDelete", response);
//
//                                                JSONObject FinalObject = new JSONObject(response);
//                                                JsonResult = FinalObject.getString("result");
//
//                                                return JsonResult;
//
//                                            } catch (UnsupportedEncodingException e1) {
//                                                e1.printStackTrace();
//                                            } catch (IOException e1) {
//                                                e1.printStackTrace();
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            return null;
//                                        }
//
//                                        @Override
//                                        protected void onPostExecute(String result) {
//                                            super.onPostExecute(result);
//
//                                            if (result == null) {} else if (result.equalsIgnoreCase("successfully")) {
//                                                Toast.makeText(JobRequestUser.this, "Deleted", Toast.LENGTH_SHORT).show();
//                                                pendingRequestListBean.remove(position);
//                                                notifyDataSetChanged();
//                                            }
//
//                                        }
//
//                                    }
//
//                                });
//
//                        builder1.setNegativeButton(
//                                "No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                        AlertDialog alert11 = builder1.create();
//                        alert11.show();
//
//
//                    }
//                    else if (Status.equalsIgnoreCase("ACCEPTED")) {
//
//                        Calendar c = Calendar.getInstance();
//
//                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                        String currentDateStr = df.format(c.getTime());
//                        Date currentDate = null, jobDate = null;
//                        try {
//
//                            currentDate = df.parse(currentDateStr);
//                            jobDate = df.parse(pendingRequestListBean.get(position).getProblem().getDate());
//
//                            Log.e("Current Date", currentDateStr);
//                            Log.e("Job Date", pendingRequestListBean.get(position).getProblem().getDate());
//                            Log.e("Compare Date", ">>>" + currentDate.compareTo(jobDate));
//
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (currentDate.compareTo(jobDate) < 0) {
//
//                            android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(JobRequestUser.this);
//                            builder1.setMessage("Are you sure you want to cancel?");
//                            builder1.setCancelable(true);
//
//                            builder1.setPositiveButton(
//                                    "Yes",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//
//                                            if (InternetDetect.isConnected(JobRequestUser.this)) {
//                                                new JsonCancelJob().execute(pendingRequestListBean.get(position).getProblem().getId(), sessionUser.getId(), pendingRequestListBean.get(position).getId());
//                                            } else {
//
//                                                Toast.makeText(JobRequestUser.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
//                                            }
//
//                                            dialog.cancel();
//                                        }
//                                    });
//
//                            builder1.setNegativeButton(
//                                    "No",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//
//                            android.support.v7.app.AlertDialog alert11 = builder1.create();
//                            alert11.show();
//
//                        } else {
//                            Log.e("Else Compare Date", ">>>>" + currentDate.compareTo(jobDate));
//                        }
//
//                    }
//                    return true;
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return pendingRequestListBean == null ? 0 : pendingRequestListBean.size();
        }

    }

    private void cancelledJobDialog(final List<PendingRequestBean> pendingRequestListBean,
                                    final String Status,final int position) {
        final Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.cancel_job_remove_dialog);

        Button btNo = dialog.findViewById(R.id.btNo);
        Button btYes = dialog.findViewById(R.id.btYes);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status.equalsIgnoreCase("REJECTED") ||
                        Status.equalsIgnoreCase("COMPLETED") ||
                        Status.equalsIgnoreCase("CANCELLED") ||
                        Status.equalsIgnoreCase("RATED") ||
                        Status.equalsIgnoreCase("NOTFIXED")) {

                    dialog.dismiss();

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

                            if (result == null) {} else if (result.equalsIgnoreCase("successfully")) {
                                Toast.makeText(JobRequestUser.this, "Deleted", Toast.LENGTH_SHORT).show();
                                pendingRequestListBean.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }

                    new JsonDelete().execute(pendingRequestListBean.get(position).getProblem().getId());

                } else if (Status.equalsIgnoreCase("ACCEPTED")) {

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String currentDateStr = df.format(c.getTime());
                    Date currentDate = null, jobDate = null;
                    try {

                        currentDate = df.parse(currentDateStr);
                        jobDate = df.parse(pendingRequestListBean.get(position).getProblem().getDate());

                        Log.e("Current Date", currentDateStr);
                        Log.e("Job Date", pendingRequestListBean.get(position).getProblem().getDate());
                        Log.e("Compare Date", ">>>" + currentDate.compareTo(jobDate));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (currentDate.compareTo(jobDate) < 0) {

                        if (InternetDetect.isConnected(JobRequestUser.this)) {
                            new JsonCancelJob().execute(pendingRequestListBean.get(position).getProblem().getId(), sessionUser.getId(), pendingRequestListBean.get(position).getId());
                        } else {
                            Toast.makeText(JobRequestUser.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                        }

                        dialog.cancel();
//                        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(JobRequestUser.this);
//                        builder1.setMessage("Are you sure you want to cancel?");
//                        builder1.setCancelable(true);
//
//                        builder1.setPositiveButton(
//                                "Yes",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//
//                                    }
//                                });
//
//                        builder1.setNegativeButton(
//                                "No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                        android.support.v7.app.AlertDialog alert11 = builder1.create();
//                        alert11.show();

                    } else {
                        Log.e("Else Compare Date", ">>>>" + currentDate.compareTo(jobDate));
                    }

                }
            }
        });

        dialog.show();

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

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetDetect.isConnected(this)) {
            new JsonTaskGetAllJobRequest().execute();
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
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


            if (result == null) {

            } else if (result.equalsIgnoreCase("CENCEL BY USER")) {

                Toast.makeText(JobRequestUser.this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            }

        }

    }


}
