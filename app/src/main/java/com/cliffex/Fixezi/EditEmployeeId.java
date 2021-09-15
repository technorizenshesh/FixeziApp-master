package com.cliffex.Fixezi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.Model.WorkerBean;
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

public class EditEmployeeId extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    RecyclerView RecyclerViewWorker;
    int indexvalue = 0;
    SessionTradesman sessionTradesman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee_id);

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Manage Employee");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerViewWorker = (RecyclerView) findViewById(R.id.RecyclerViewWorker);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerViewWorker.setLayoutManager(mLayoutManager);

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

                List<WorkerBean> workerBeanList = new ArrayList<>();

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

                Toast.makeText(EditEmployeeId.this, "No user Found", Toast.LENGTH_SHORT).show();

            } else if (result.isEmpty()) {

                Toast.makeText(EditEmployeeId.this, "No user Found", Toast.LENGTH_SHORT).show();

            } else {

                WorkerAdapter adapter = new WorkerAdapter(EditEmployeeId.this, result);
                RecyclerViewWorker.setAdapter(adapter);

            }

        }

    }


    private class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.MyViewHolder> {

        private Context mContext;
        List<WorkerBean> workerBeanList;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout OverflowIcon;
            TextView WorkerPasswordTV, WorkerUsernameTV, WorkerNameTV;


            public MyViewHolder(View view) {
                super(view);
                OverflowIcon = (RelativeLayout) view.findViewById(R.id.RLOverflowIcon);
                WorkerNameTV = (TextView) view.findViewById(R.id.WorkerNameTV);
                WorkerUsernameTV = (TextView) view.findViewById(R.id.WorkerUsernameTV);
                WorkerPasswordTV = (TextView) view.findViewById(R.id.WorkerPasswordTV);

            }
        }


        public WorkerAdapter(Context mContext, List<WorkerBean> workerBeanList) {
            this.mContext = mContext;
            this.workerBeanList = workerBeanList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.worker_rowitem, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.WorkerNameTV.setText(workerBeanList.get(position).getName());
            holder.WorkerUsernameTV.setText(workerBeanList.get(position).getUsername());
            holder.WorkerPasswordTV.setText(workerBeanList.get(position).getPassword());


            holder.OverflowIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.OverflowIcon);
                    indexvalue = position;
                }
            });

        }

        private void showPopupMenu(View view) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_worker, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
            popup.show();
        }


        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            public MyMenuItemClickListener() {
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {


                    case R.id.ChangePasswordMenu:

                        Intent intent = new Intent(EditEmployeeId.this, ChangePassword.class);
                        intent.putExtra("id", workerBeanList.get(indexvalue).getId());
                        startActivity(intent);
                        return true;

                    case R.id.DeleteUser:

                        if (InternetDetect.isConnected(EditEmployeeId.this)) {

                            new JsonDeleteWorker().execute(workerBeanList.get(indexvalue).getId());
                        } else {
                            Toast.makeText(EditEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    default:
                }
                return false;
            }


            private class JsonDeleteWorker extends AsyncTask<String, String, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... strings) {

                    try {
                        URL url = new URL(HttpPAth.Urlpath + "delete_worker_user&");
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put("worker_id", strings[0]);


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
                        Log.e("JsonDeleteWorker", response);

                        //{"result":"successfully"}

                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("result");

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

                        Toast.makeText(EditEmployeeId.this, "Server Problem", Toast.LENGTH_SHORT).show();

                    } else if (result.equalsIgnoreCase("successfully")) {

                        workerBeanList.remove(indexvalue);
                        notifyDataSetChanged();
                    }
                }
            }
        }

        @Override
        public int getItemCount() {

            return workerBeanList == null ? 0 : workerBeanList.size();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (InternetDetect.isConnected(this)) {

            new JsonGetWorker().execute(sessionTradesman.getId());
        } else {

            Toast.makeText(EditEmployeeId.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();

        }
    }
}
