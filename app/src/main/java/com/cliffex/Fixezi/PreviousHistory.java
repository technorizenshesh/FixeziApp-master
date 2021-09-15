package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.PreviousHistoryBean;
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

/**
 * Created by technorizen8 on 17/3/16.
 */
public class PreviousHistory extends AppCompatActivity {

    Toolbar previoushistory_toolbar;
    TextView toolbar_title, PreviousTradesmanRatedTV;
    ListView previousratelistview;
    RelativeLayout NavigationUpIM;
    SessionUser sessionUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.Please Wait ......");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.previoushistory_activity);
        previoushistory_toolbar = (Toolbar) findViewById(R.id.previoushistory_toolbar);
        toolbar_title = (TextView) previoushistory_toolbar.findViewById(R.id.toolbar_title);
        previousratelistview = (ListView) findViewById(R.id.previousratelistview);
        PreviousTradesmanRatedTV = (TextView) findViewById(R.id.PreviousTradesmanRatedTV);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_title.setText("Previous history");
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(previoushistory_toolbar);

        sessionUser = new SessionUser(this);

        if (InternetDetect.isConnected(this)) {

            new JsonTask().execute();
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private class JsonTask extends AsyncTask<String, String, List<PreviousHistoryBean>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            Log.e("In Pre", "yes");
        }

        @Override
        protected List<PreviousHistoryBean> doInBackground(String... paramss) {
            Log.e("In back", "yes");

            try {
                URL url = new URL(HttpPAth.Urlpath + "get_users_rating&");
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
                Log.e("JsonPendingRequest", response);

                List<PreviousHistoryBean> pendingRequestListBeanList = new ArrayList<PreviousHistoryBean>();


                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    PreviousHistoryBean historyBean = new PreviousHistoryBean();

                    historyBean.setTradesmanName(object.getString("business_name"));
                    historyBean.setPunctual(object.getString("punctual"));
                    historyBean.setWorkmanship(object.getString("workmanship"));
                    historyBean.setAffordability(object.getString("affordability"));
                    pendingRequestListBeanList.add(historyBean);
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
        protected void onPostExecute(List<PreviousHistoryBean> result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result == null) {

            } else {

                ListViewAdapter adapter = new ListViewAdapter(PreviousHistory.this, result);
                previousratelistview.setAdapter(adapter);
            }
        }
    }

    private class ListViewAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        private List<PreviousHistoryBean> data;


        public ListViewAdapter(Context context, List<PreviousHistoryBean> data) {

            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.data = data;
        }

        public class ViewHolder {
            TextView TradesmanName;
            RatingBar ratingBar1, ratingBar2, ratingBar3;
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
                view = inflater.inflate(R.layout.custom_previousrate_list, null);
                holder.TradesmanName = (TextView) view.findViewById(R.id.TradesmanName);
                holder.ratingBar1 = (RatingBar) view.findViewById(R.id.ratingBar1);
                holder.ratingBar2 = (RatingBar) view.findViewById(R.id.ratingBar2);
                holder.ratingBar3 = (RatingBar) view.findViewById(R.id.ratingBar3);
                view.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.TradesmanName.setText(data.get(position).getTradesmanName());
            if (!(data.get(position).getPunctual().equalsIgnoreCase(""))) {

                holder.ratingBar1.setRating(Float.parseFloat(data.get(position).getPunctual()));
                holder.ratingBar2.setRating(Float.parseFloat(data.get(position).getWorkmanship()));
                holder.ratingBar3.setRating(Float.parseFloat(data.get(position).getAffordability()));
            }

            return view;
        }
    }
}

