package com.cliffex.Fixezi.MyFragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.R;
import com.cliffex.Fixezi.ScheduleJobsDetailTradesman;
import com.cliffex.Fixezi.ScheduleJobsDetailWorker;
import com.cliffex.Fixezi.SessionTradesman;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleNewFrag extends Fragment {

    String Date = "";
    RecyclerView ScheduleNewRV;
    SessionTradesman sessionTradesman;
    TextView NoJobsFoundTV;
    ProgressBar ScheduleFragPB;


    public ScheduleNewFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {

            Date = this.getArguments().getString("Date", "");
            Log.e("Date", Date);
        }

        sessionTradesman = new SessionTradesman(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_schedule_new, container, false);


        ScheduleNewRV = view.findViewById(R.id.ScheduleNewRV);
        NoJobsFoundTV = view.findViewById(R.id.NoJobsFoundTV);
        ScheduleFragPB = view.findViewById(R.id.ScheduleFragPB);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ScheduleNewRV.setLayoutManager(mLayoutManager);

        ScheduleNewRV.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        ScheduleNewRV.setItemAnimator(new DefaultItemAnimator());

        if (InternetDetect.isConnected(getActivity())) {

            new JsonGetScheduleNew().execute();
        } else {

            Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private class JsonGetScheduleNew extends AsyncTask<String, String, List<IncomingRequestBean>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ScheduleFragPB.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... paramss) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "Tradesman_accepted_date_again&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("Tradesman_id", sessionTradesman.getId());
                params.put("date", Date);


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

            ScheduleFragPB.setVisibility(View.GONE);
            if (result == null) {
                NoJobsFoundTV.setVisibility(View.VISIBLE);
            } else if (result.isEmpty()) {
                NoJobsFoundTV.setVisibility(View.VISIBLE);
            } else {
                NoJobsFoundTV.setVisibility(View.GONE);
                AlbumsAdapter adapter = new AlbumsAdapter(getActivity(), result);
                ScheduleNewRV.setAdapter(adapter);
            }
        }

    }


    public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

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
            holder.NumberTv.setText("Job-" + incomingRequestListBean.get(position).getProblem().getId());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Appconstants.AdminType.equalsIgnoreCase("Tradesman")) {

                        Intent intent = new Intent(mContext, ScheduleJobsDetailTradesman.class);
                        intent.putExtra("ProblemId", incomingRequestListBean.get(position).getProblem().getId());
                        startActivity(intent);
                    } else if (Appconstants.AdminType.equalsIgnoreCase("Worker")) {

                        Intent intent = new Intent(mContext, ScheduleJobsDetailWorker.class);
                        intent.putExtra("ProblemId", incomingRequestListBean.get(position).getProblem().getId());
                        startActivity(intent);
                    }

                }
            });


            /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    final int[] MyWhich = {0};


                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

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
                                intent.putExtra("sms_body", "http://www.technorizen.co.in/WORKSPACE6/FIXEZI/job.php?problem_id=" + incomingRequestListBean.get(position).getProblem().getId());
                                startActivity(intent);

                            } else if (MyWhich[0] == 1) {

                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "fixeziupdate Job Work");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "http://www.technorizen.co.in/WORKSPACE6/FIXEZI/job.php?problem_id=" + incomingRequestListBean.get(position).getProblem().getId());
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
            });*/

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
