package com.cliffex.Fixezi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.phoneNumber;

public class ScheduleJobs extends AppCompatActivity {


    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    RecyclerView recyclerView;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_jobs);

        sessionTradesman = new SessionTradesman(this);
        Log.e("Trademan ID", sessionTradesman.getId());
        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Schedule Jobs");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.RVScheduleJobs);

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


        if (InternetDetect.isConnected(this)){

            new JsonTaskGetAllJobRequest().execute();
        }else {

            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    public class JsonTaskGetAllJobRequest extends AsyncTask<String, String, List<IncomingRequestBean>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "Tradesman_accepted_byid&");
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
                Log.e("Json", response);

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

                    incomingRequestBean.setProblem(problem);
                    incomingRequestListBeanList.add(incomingRequestBean);

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
            if (result==null){

            }else {

                AlbumsAdapter adapter = new AlbumsAdapter(ScheduleJobs.this, result);
                recyclerView.setAdapter(adapter);
            }
        }
    }


    public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

        private Context mContext;
        List<IncomingRequestBean> incomingRequestListBean;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ProblemJobRequest, DateJobRequest, TimeJobRequest, FlexibleDateJobRequest, FlexibleTimeJobRequest, StatusTV;


            public MyViewHolder(View view) {
                super(view);
                ProblemJobRequest = (TextView) view.findViewById(R.id.ProblemJobRequest);
                DateJobRequest = (TextView) view.findViewById(R.id.DateJobRequest);
                TimeJobRequest = (TextView) view.findViewById(R.id.TimeJobRequest);
                FlexibleDateJobRequest = (TextView) view.findViewById(R.id.FlexibleDateJobRequest);
                FlexibleTimeJobRequest = (TextView) view.findViewById(R.id.FlexibleTimeJobRequest);
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


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ScheduleJobs.this, ScheduleJobsDetailTradesman.class);
                    intent.putExtra("ProblemId", incomingRequestListBean.get(position).getProblem().getId());
                    startActivity(intent);


                }
            });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    final int[] MyWhich = {0};


                    AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleJobs.this);

                    final CharSequence[] array = {"Sms", "Email"};
                    builder.setTitle("Forwad Job Via");
                    builder.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            MyWhich[0] = which;
                        }
                    });

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            Log.e("MyWhich", ">>>" + array[MyWhich[0]]);

                            if (MyWhich[0] == 0) {

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                                intent.putExtra("sms_body", "http://www.technorizen.co.in/WORKSPACE6/FIXEZI/job.php?problem_id="+ incomingRequestListBean.get(position).getProblem().getId());
                                startActivity(intent);

                            } else if (MyWhich[0] == 1) {

                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "fixezi Job Work");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "http://www.technorizen.co.in/WORKSPACE6/FIXEZI/job.php?problem_id="+ incomingRequestListBean.get(position).getProblem().getId());
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }

                            dialog.dismiss();

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert11 = builder.create();
                    alert11.show();

                    return false;
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

}
