package com.cliffex.Fixezi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.Model.CalendarBean;
import com.cliffex.Fixezi.Model.IncomingRequestBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class ScheduleNew extends AppCompatActivity {

    CaldroidFragment caldroidFragment;
    SimpleDateFormat formatter;
    SessionTradesman sessionTradesman;
    List<CalendarBean> calendarBeanList;
    ProgressBar SchedulePB;
    TextView NoJobsFoundTV5;
    Toolbar toolbar;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    RecyclerView RecyclerViewSchedule;

    int doubleClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_new);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Schedule Jobs");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SchedulePB = (ProgressBar) findViewById(R.id.SchedulePB);
        NoJobsFoundTV5 = (TextView) findViewById(R.id.NoJobsFoundTV5);
        RecyclerViewSchedule = (RecyclerView) findViewById(R.id.RecyclerViewSchedule);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerViewSchedule.setLayoutManager(mLayoutManager);

        RecyclerViewSchedule.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        RecyclerViewSchedule.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewSchedule.setNestedScrollingEnabled(false);

        sessionTradesman = new SessionTradesman(this);
        // formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        caldroidFragment = new CaldroidFragment();

        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(java.util.Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(java.util.Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }


        CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {


                if (InternetDetect.isConnected(ScheduleNew.this)) {

                    new JsonGetScheduleNew().execute(formatter.format(date).toString());
                } else {
                    Toast.makeText(ScheduleNew.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }


                /*doubleClick++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        doubleClick= 0;
                    }
                };

                if (doubleClick== 1) {

                    if (InternetDetect.isConnected(ScheduleNew.this)){

                        new JsonGetScheduleNew().execute(formatter.format(date).toString());
                    }else{
                        Toast.makeText(ScheduleNew.this,"Please Connect to Internet",Toast.LENGTH_SHORT).show();
                    }
                    handler.postDelayed(r, 500);

                } else if (doubleClick== 2) {


                    ArrayList<String> stringArrayList = new ArrayList<>();

                    for (int i = 0; i < calendarBeanList.size(); i++) {

                        stringArrayList.add(calendarBeanList.get(i).getDate());
                        Log.e("DATEINPUT", calendarBeanList.get(i).getDate());

                    }


                    for (int i = 0; i < stringArrayList.size(); i++) {


                        if (formatter.format(date).equalsIgnoreCase(stringArrayList.get(i))) {

                            Intent intent = new Intent(ScheduleNew.this, ScheduleNewDetail.class);
                            intent.putStringArrayListExtra("DateList", stringArrayList);
                            intent.putExtra("Position", i);
                            startActivity(intent);
                            break;

                        }

                    }

                    doubleClick= 0;

                }*/


            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
            }

            @Override
            public void onLongClickDate(Date date, View view) {


                ArrayList<String> stringArrayList = new ArrayList<>();

                for (int i = 0; i < calendarBeanList.size(); i++) {

                    stringArrayList.add(calendarBeanList.get(i).getDate());
                    Log.e("DATEINPUT", calendarBeanList.get(i).getDate());

                }


                for (int i = 0; i < stringArrayList.size(); i++) {


                    if (formatter.format(date).equalsIgnoreCase(stringArrayList.get(i))) {

                        Intent intent = new Intent(ScheduleNew.this, ScheduleNewDetail.class);
                        intent.putStringArrayListExtra("DateList", stringArrayList);
                        intent.putExtra("Position", i);
                        startActivity(intent);
                        break;

                    }

                }

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        caldroidFragment.setCaldroidListener(listener);

        if (InternetDetect.isConnected(this)) {

            new JsonGetEventList().execute();
        } else {


            Toast.makeText(ScheduleNew.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private class JsonGetEventList extends AsyncTask<String, String, List<CalendarBean>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SchedulePB.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CalendarBean> doInBackground(String... strings) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "get_problem_date_byTradesman&");
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


                Log.e("JsonGetCalendar", ">>>>" + response);

                //{"result":[{"month":"May 2017","month_data":[{"id":"3","name":"Sanskrati Nratya","date":"2017-05-08","time":"12:00 PM","description":" Hello everyone plz come to sanskrati nratya on 2017-05-08     ","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG50305.png","type":"event","date_type":"multiple","start_date":"2017-05-08","end_date":"2017-05-10","year":"2017","month":"05","day":"08"},{"id":"4","name":"Deewali Festival","date":"2017-05-12","time":"12:00 PM","description":"    Hello everyone enjoy wonderful festival deewali on 2017-05-12 to 2017-05-15        ","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG61378.png","type":"holiday","date_type":"multiple","start_date":"2017-05-12","end_date":"2017-05-15","year":"2017","month":"05","day":"12"},{"id":"15","name":"Vaderdag","date":"2017-05-17","time":"","description":"  vaderdag2","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG31906.png","type":"event","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"05","day":"17"},{"id":"14","name":"Pinksteren","date":"2017-05-24","time":"","description":"  test maarten","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG58468.png","type":"event","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"05","day":"24"},{"id":"13","name":"First school day Maarten","date":"2017-05-27","time":"","description":"  ","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG94048.png","type":"holiday","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"05","day":"27"},{"id":"16","name":"vaderdag2","date":"2017-05-30","time":"","description":"  vaderdag2","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG96522.png","type":"holiday","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"05","day":"30"}]},{"month":"Jun 2017","month_data":[{"id":"12","name":"New event Maarten","date":"2017-06-01","time":"","description":"  Back to school","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG85622.png","type":"event","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"06","day":"01"}]},{"month":"Jul 2017","month_data":[{"id":"7","name":"Malva Utsav","date":"2017-07-06","time":"12:00 PM","description":"    Hello everyone plz come to malva utsa on 2017-05-12 to 2017-05-15     ","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG67688.png","type":"event","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"07","day":"06"},{"id":"6","name":"Holi Festival","date":"2017-07-10","time":"12:00 PM","description":"   Enjoy holi festival   ","image":"http:\/\/resultmakershosting.nl\/SINT\/img\/USER_IMG7482.png","type":"holiday","date_type":"single","start_date":"0000-00-00","end_date":"0000-00-00","year":"2017","month":"07","day":"10"}]}],"message":"successfull","status":1}

                calendarBeanList = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CalendarBean calendarBean = new CalendarBean();
                    calendarBean.setDate(jsonObject.getString("date"));
                    calendarBean.setDate_d(jsonObject.getString("date_d"));
                    calendarBean.setDate_m(jsonObject.getString("date_m"));
                    calendarBean.setDate_y(jsonObject.getString("date_y"));

                    calendarBeanList.add(calendarBean);

                }


                return calendarBeanList;

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
        protected void onPostExecute(List<CalendarBean> result) {
            super.onPostExecute(result);
            if (result == null) {

                NoJobsFoundTV5.setVisibility(View.VISIBLE);

                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar1, caldroidFragment);
                t.commit();

            } else if (result.isEmpty()) {

                NoJobsFoundTV5.setVisibility(View.VISIBLE);

                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar1, caldroidFragment);
                t.commit();

            } else {

                for (int i = 0; i < calendarBeanList.size(); i++) {

                    setCustomResourceForDates2(Integer.parseInt(calendarBeanList.get(i).getDate_y()), Integer.parseInt(calendarBeanList.get(i).getDate_m()) - 1, Integer.parseInt(calendarBeanList.get(i).getDate_d()), "event");
                }


                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar1, caldroidFragment);
                t.commit();


                /*CalendarAdapter adapter = new CalendarAdapter(Calendar.this, calendarBeanList);
                RecyclerViewCalendar.setAdapter(adapter);
                */

                NoJobsFoundTV5.setVisibility(View.GONE);
            }

            SchedulePB.setVisibility(View.GONE);
        }
    }


    private void setCustomResourceForDates2(int year, int month, int day, String type) {

        java.util.Calendar cal = java.util.Calendar.getInstance();

        //My Date
        cal = java.util.Calendar.getInstance();
        cal.set(year, month, day);
        Date myDate = cal.getTime();

        if (caldroidFragment != null) {

            ColorDrawable blue;
            if (type.equalsIgnoreCase("event")) {

                blue = new ColorDrawable(getResources().getColor(R.color.blue));
            } else if (type.equalsIgnoreCase("holiday")) {

                blue = new ColorDrawable(getResources().getColor(R.color.red));

            } else {
                blue = new ColorDrawable(getResources().getColor(R.color.blue));
            }

            caldroidFragment.setBackgroundDrawableForDate(blue, myDate);
            caldroidFragment.setTextColorForDate(R.color.white, myDate);

        }
    }


    private class JsonGetScheduleNew extends AsyncTask<String, String, List<IncomingRequestBean>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SchedulePB.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<IncomingRequestBean> doInBackground(String... strings) {


            try {
                URL url = new URL(HttpPAth.Urlpath + "Tradesman_accepted_date_again&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("Tradesman_id", sessionTradesman.getId());
                params.put("date", strings[0]);


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
                    problem.setHousenoo(ProblemObject.getString("housenoo"));
                    problem.setStreet(ProblemObject.getString("street"));
                    problem.setHome_Number(ProblemObject.getString("Home_Number"));
                    problem.setMobile_Number(ProblemObject.getString("Mobile_Number"));
                    problem.setJob_Request(ProblemObject.getString("Job_Request"));
                    problem.setUser_id(ProblemObject.getString("user_id"));
                    problem.setProblem(ProblemObject.getString("problem"));

                    incomingRequestBean.setProblem(problem);
                    incomingRequestListBeanList.add(incomingRequestBean);

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

            SchedulePB.setVisibility(View.GONE);
            if (result == null) {
                NoJobsFoundTV5.setVisibility(View.VISIBLE);
                AlbumsAdapter adapter = new AlbumsAdapter(ScheduleNew.this, null);
                RecyclerViewSchedule.setAdapter(adapter);
            } else if (result.isEmpty()) {
                NoJobsFoundTV5.setVisibility(View.VISIBLE);
                AlbumsAdapter adapter = new AlbumsAdapter(ScheduleNew.this, null);
                RecyclerViewSchedule.setAdapter(adapter);
            } else {
                NoJobsFoundTV5.setVisibility(View.GONE);

                AlbumsAdapter adapter = new AlbumsAdapter(ScheduleNew.this, result);
                RecyclerViewSchedule.setAdapter(adapter);
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
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "fixezi Job Work");
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
            });
*/
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
