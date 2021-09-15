package com.cliffex.Fixezi;

import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IncomingJobRequest extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview, NoIncomingTV;
    SessionTradesman sessionTradesman;
    RecyclerView recyclerView;
    RelativeLayout NavigationUpIM;
    ProgressDialog progressDialog;
    TextView tv_msg;
    Context mContext = IncomingJobRequest.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading.Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_job_request);
        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);

        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        toolbar_textview.setText("Incoming Job Requests");
        setSupportActionBar(toolbar);

        tv_msg = (TextView) findViewById(R.id.tv_msg);
        recyclerView = (RecyclerView) findViewById(R.id.RVIncomingJobRequest);
        NoIncomingTV = (TextView) findViewById(R.id.NoIncomingTV);

        String text = "<font color='#49A2F0'>When you accept a job </font> it is moved to 'All Jobs'";
        tv_msg.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private class JsonTaskGetAllJobRequest extends AsyncTask<String, String, List<IncomingRequestBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_bookingList_byid&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("Tradesman_id", sessionTradesman.getId());

                Log.e("sgxdsgxdsgxdsgds","Tradesman_id = " + sessionTradesman.getId());

                StringBuilder postData = new StringBuilder();

                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
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
                Log.e("JsonIncomingJob>>>>>>>", response);

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
                    incomingRequestBean.setStatus(object.getString("status"));
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
                    problem.setOrder_status(ProblemObject.getString("order_status"));

                    incomingRequestBean.setProblem(problem);
                    incomingRequestListBeanList.add(incomingRequestBean);
                    incomingRequestListBeanList.get(i).getStatus();
                    // if (incomingRequestListBeanList.get(i).getProblem().getStatus().equals("Pending"))

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
            progressDialog.dismiss();
            if (result == null) {
                NoIncomingTV.setVisibility(View.VISIBLE);
            } else if (result.isEmpty()) {
                NoIncomingTV.setVisibility(View.VISIBLE);
            } else {
                Collections.reverse(result);
                AlbumsAdapter adapter = new AlbumsAdapter(IncomingJobRequest.this, result);
                recyclerView.setAdapter(adapter);
            }
        }

    }

    private class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

        private Context mContext;
        List<IncomingRequestBean> incomingRequestListBean;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView ProblemJobRequest, DateJobRequest, TimeJobRequest, FlexibleDateJobRequest, FlexibleTimeJobRequest, NumberTv,StatusTV;

            public MyViewHolder(View view) {
                super(view);
                ProblemJobRequest = (TextView) view.findViewById(R.id.ProblemJobRequest);
                DateJobRequest = (TextView) view.findViewById(R.id.DateJobRequest);
                TimeJobRequest = (TextView) view.findViewById(R.id.TimeJobRequest);
                FlexibleDateJobRequest = (TextView) view.findViewById(R.id.FlexibleDateJobRequest);
                FlexibleTimeJobRequest = (TextView) view.findViewById(R.id.FlexibleTimeJobRequest);
                NumberTv = (TextView) view.findViewById(R.id.NumberTv);
                StatusTV = (TextView) view.findViewById(R.id.StatusTV);
            }
        }

        public AlbumsAdapter(Context mContext, List<IncomingRequestBean> incomingRequestListBean1) {
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

            holder.ProblemJobRequest.setText(incomingRequestListBean.get(position).getProblem().getProblem());
            holder.DateJobRequest.setText(incomingRequestListBean.get(position).getProblem().getDate());
            holder.TimeJobRequest.setText(incomingRequestListBean.get(position).getProblem().getTime());
            holder.FlexibleDateJobRequest.setText(incomingRequestListBean.get(position).getProblem().getIsDateFlexible());
            holder.FlexibleTimeJobRequest.setText(incomingRequestListBean.get(position).getProblem().getIsTimeFlexible());
            holder.NumberTv.setText("Job " + incomingRequestListBean.get(position).getProblem().getId());

            Log.e("dsfsdfsdfds","Status = "+ position +" = " + incomingRequestListBean.get(position).getProblem().getOrder_status());

            if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("REJECTED")) {

                holder.StatusTV.setText("REJECTED BY TRADESMAN");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("PROCESS")) {

                holder.StatusTV.setText("IN PROCESS");
                holder.StatusTV.setTextColor(Color.parseColor("#c79202"));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("RESCHEDULE")) {

                holder.StatusTV.setText("RESCHEDULE");
                holder.StatusTV.setTextColor(Color.parseColor("#DAA520"));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("ACCEPTED")) {

                holder.StatusTV.setText("ACCEPTED");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim().equalsIgnoreCase("COMPLETED") ||
                    incomingRequestListBean.get(position).getProblem().getStatus().trim().equalsIgnoreCase("RATED")) {

                holder.StatusTV.setText("COMPLETED");
                holder.StatusTV.setTextColor(Color.parseColor("#32CD32"));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("CANCELLED")) {

                holder.StatusTV.setText("CANCELLED BY USER");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("PENDING")) {

                holder.StatusTV.setText("PENDING");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));

            } else if (incomingRequestListBean.get(position).getProblem().getOrder_status().trim()
                    .equalsIgnoreCase("JOB EXPORTED")) {

                holder.StatusTV.setText("EXPORTED BY TRADESMAN");
                holder.StatusTV.setTextColor(ContextCompat.getColor(mContext,R.color.red));

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(IncomingJobRequest.this, IncomingJobRequestDetail.class);
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

        AlbumsAdapter adapter = new AlbumsAdapter(IncomingJobRequest.this, null);
        recyclerView.setAdapter(adapter);

        if (InternetDetect.isConnected(this)) {
            new JsonTaskGetAllJobRequest().execute();
        } else {
            Toast.makeText(IncomingJobRequest.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }
}
