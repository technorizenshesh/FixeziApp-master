package com.cliffex.Fixezi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.Model.ServiceLocationBean;
import com.cliffex.Fixezi.Model.CategoryBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.NonScrollListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EditTrade extends AppCompatActivity {
    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview, selecttradeTV;
    ListView TradeList;
    List<ServiceLocationBean> serviceLocationBeanArrayList;
    Button UpdataTradeBT;
    ProgressDialog progressDialog;
    RelativeLayout NavigationUpIM, SelectTradeEditTrade;
    ArrayList<String> postCode;
    ArrayList<CategoryBean> catarray = new ArrayList<>();
    String redcolor = "#fd3621";
    String mainCategoryArray[];
    String posID[];
    ArrayList<String> Selecteddata;
    CheckBox EditTradeCB;
    TextView tv_editMessage;
    EditText edit_comment;
    Button btn_sendInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading.Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trade);

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.ToolbarEditT);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        tv_editMessage = (TextView) findViewById(R.id.tv_editMessage);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        edit_comment= (EditText) findViewById(R.id.edit_comment);
        btn_sendInformation= (Button) findViewById(R.id.btn_sendInformation);
        toolbar_textview.setText("Edit/Add Trade");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TradeList = (ListView) findViewById(R.id.TradeList);

        SelectTradeEditTrade = (RelativeLayout) findViewById(R.id.SelectTradeEditTrade);
        selecttradeTV = (TextView) findViewById(R.id.SelectTradeTVEdit);
        UpdataTradeBT = (Button) findViewById(R.id.UpdataTradeBT);
        EditTradeCB = (CheckBox) findViewById(R.id.EditTradeCB);

        String text = "<font color='#1E90FF'>To edit/add a trade,</font> you will need to message fixezi admin.";
        tv_editMessage.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        TradeList.setEnabled(false);
        UpdataTradeBT.setEnabled(false);
        SelectTradeEditTrade.setEnabled(false);

        EditTradeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TradeList.setEnabled(true);
                    UpdataTradeBT.setEnabled(true);
                    SelectTradeEditTrade.setEnabled(true);
                } else {
                    TradeList.setEnabled(false);
                    UpdataTradeBT.setEnabled(false);
                    SelectTradeEditTrade.setEnabled(false);
                }


            }
        });

        btn_sendInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=edit_comment.getText().toString();
                if (message.equalsIgnoreCase("")||message==null){
                    edit_comment.setError("Please Enter Message");
                }else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"fixeziteam@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "");
                    i.putExtra(Intent.EXTRA_TEXT   , message);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(EditTrade.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        SelectTradeEditTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ArrayList<String> ProblemList = new ArrayList<String>(Arrays.asList(mainCategoryArray));

                final Dialog SelectProblemDailog = new Dialog(EditTrade.this);
                SelectProblemDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                SelectProblemDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                SelectProblemDailog.setContentView(R.layout.select_problem_alert_dailog);

                NonScrollListView SelectProblemList = (NonScrollListView) SelectProblemDailog.findViewById(R.id.SelectProblemList);
                Button CancelProblemTV = (Button) SelectProblemDailog.findViewById(R.id.CancelProblemTV);
                Button AcceptProblemTV = (Button) SelectProblemDailog.findViewById(R.id.AcceptProblemTV);
                TextView SelecTradeTV = (TextView) SelectProblemDailog.findViewById(R.id.SelecTradeTV);

                Selecteddata = new ArrayList<String>();
                SelecTradeTV.setText("Select Trade");
                ListViewAdapter adapter2 = new ListViewAdapter(EditTrade.this, ProblemList);
                SelectProblemList.setAdapter(adapter2);
                SelectProblemList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                SelectProblemList.setItemsCanFocus(false);


                CancelProblemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectProblemDailog.dismiss();
                    }
                });


                AcceptProblemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (Selecteddata.isEmpty()) {

                        } else {


                            StringBuilder s = new StringBuilder();
                            for (int i = 0; i < Selecteddata.size(); i++) {

                                if (i == 0) {
                                    s.append(Selecteddata.get(i));
                                    continue;
                                }

                                s.append("," + Selecteddata.get(i));
                            }


                            String AllFollowerID = s.toString();
                            Log.e("AllFollowerID", "??" + AllFollowerID);
                            /*selecttradeTV.setText(AllFollowerID);*/

                            for (int i = 0; i < Selecteddata.size(); i++) {


                                if (CheckDuplicate(Selecteddata.get(i))) {

                                } else {

                                    ServiceLocationBean serviceLocationBean = new ServiceLocationBean();
                                    serviceLocationBean.setId("MYID");
                                    serviceLocationBean.setName(Selecteddata.get(i));
                                    serviceLocationBeanArrayList.add(serviceLocationBean);

                                }


                            }

                            ListViewAdapterOld adapter = new ListViewAdapterOld(EditTrade.this, serviceLocationBeanArrayList);
                            TradeList.setAdapter(adapter);

                            HttpPAth.setListViewHeightBasedOnChildren(TradeList);

                        }

                        SelectProblemDailog.dismiss();
                    }

                    private boolean CheckDuplicate(String name) {

                        for (ServiceLocationBean serviceLocationBean : serviceLocationBeanArrayList) {


                            if (serviceLocationBean.getName().equalsIgnoreCase(name)) {

                                return true;
                            }

                        }
                        return false;
                    }

                });

                SelectProblemDailog.show();


            }
        });

        UpdataTradeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (serviceLocationBeanArrayList.isEmpty()) {
                    Log.e("ISEMPT", "YES");

                    Toast.makeText(EditTrade.this, "Please Select a Trade", Toast.LENGTH_SHORT).show();

                } else {

                    StringBuilder s = new StringBuilder();
                    for (int i = 0; i < serviceLocationBeanArrayList.size(); i++) {


                        if (i == 0) {
                            s.append(serviceLocationBeanArrayList.get(i).getName());
                            continue;
                        }
                        s.append("," + serviceLocationBeanArrayList.get(i).getName());

                    }


                    String AllServiceLocation = s.toString();
                    Log.e("VALUE", AllServiceLocation);

                    if (InternetDetect.isConnected(EditTrade.this)){

                        new JsonTaskUpdate().execute(sessionTradesman.getId(), AllServiceLocation);
                    }else{

                        Toast.makeText(EditTrade.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (InternetDetect.isConnected(this)) {
            new JsonGetTrade().execute();
        } else {
            Toast.makeText(EditTrade.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class JsonGetTrade extends AsyncTask<String, String, List<ServiceLocationBean>> {

        String result;

        @Override
        protected List<ServiceLocationBean> doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "get_tradesman_trade&tradesman_id=" + sessionTradesman.getId());
            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                Log.e("JsonGet Service", "????" + obj);

                JSONArray jsonArray = new JSONArray(obj);
                serviceLocationBeanArrayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);

                    ServiceLocationBean serviceLocationBean = new ServiceLocationBean();
                    serviceLocationBean.setId(object.getString("id"));
                    serviceLocationBean.setName(object.getString("name"));
                    result = object.getString("result");

                    serviceLocationBeanArrayList.add(serviceLocationBean);

                }

                return serviceLocationBeanArrayList;

            } catch (Exception e) {
                System.out.println("errror in Forget task " + e);
            }

            return null;

        }

        @Override
        protected void onPostExecute(List<ServiceLocationBean> result) {
            super.onPostExecute(result);

            if (result == null) {
            } else {
                for(int i=0;i<result.size();i++)
                    Log.e("resultresultresult","result = " + result.get(i).getName());
                ListViewAdapterOld adapter = new ListViewAdapterOld(EditTrade.this, result);
                TradeList.setAdapter(adapter);
                HttpPAth.setListViewHeightBasedOnChildren(TradeList);
            }

        }

    }

    private class ListViewAdapterOld extends BaseAdapter {


        Context mContext;
        LayoutInflater inflater;
        private List<ServiceLocationBean> serviceLocationBeanList;


        public ListViewAdapterOld(Context context,
                                  List<ServiceLocationBean> serviceLocationBeanList) {


            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.serviceLocationBeanList = serviceLocationBeanList;


        }

        public class ViewHolder {
            TextView ServiceLocationName;
            ImageView DeleteLocationIm;
        }

        @Override
        public int getCount() {

            return serviceLocationBeanList == null ? 0 : serviceLocationBeanList.size();

        }

        @Override
        public Object getItem(int position) {
            return serviceLocationBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.service_location_rowitem, null);
                // Locate the TextViews in listview_item.xml
                holder.ServiceLocationName = (TextView) view.findViewById(R.id.ServiceLocationName);
                holder.DeleteLocationIm = (ImageView) view.findViewById(R.id.DeleteLocationIm);


                // Locate the ImageView in listview_item.xml
                view.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Set the results into TextViews
            holder.ServiceLocationName.setText(serviceLocationBeanList.get(position).getName());

            holder.DeleteLocationIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (EditTradeCB.isChecked()) {


                        String id = serviceLocationBeanList.get(position).getId();
                        Log.e("SElected id", id);

                        if (id.equalsIgnoreCase("MYID")) {
                            serviceLocationBeanList.remove(position);
                            notifyDataSetChanged();
                        } else {
                            serviceLocationBeanList.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                }

            });


            return view;
        }


    }

    private class JsonTaskUpdate extends AsyncTask<String, String, String> {

        String result;
        String Url;

        @Override
        protected String doInBackground(String... params) {

            Url = HttpPAth.Urlpath + "update_select_trade&tradesman_id=" + params[0] + "&select_trade=" + params[1];
            Url = Url.replaceAll(" ", "%20");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Url);
            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                JSONObject object = new JSONObject(obj);
                result = object.getString("result");

                return result;

            } catch (Exception e) {
                System.out.println("errror in Forget task " + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else {
                if (result.equalsIgnoreCase("successfully")) {

                    Toast.makeText(EditTrade.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                }
            }

        }
    }

    private class ListViewAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        private ArrayList<String> data;


        public ListViewAdapter(Context context,
                               ArrayList<String> data) {


            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.data = data;
        }

        public class ViewHolder {
            CheckedTextView ProblemNameTV;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
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
                view = inflater.inflate(R.layout.select_problem_row_list, null);
                holder.ProblemNameTV = (CheckedTextView) view.findViewById(R.id.ProblemNameTV);
                view.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.ProblemNameTV.setText(data.get(position));


            holder.ProblemNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (holder.ProblemNameTV.isChecked()) {

                        holder.ProblemNameTV.setChecked(false);
                        Selecteddata.remove(data.get(position));

                    } else {

                        holder.ProblemNameTV.setChecked(true);
                        Selecteddata.add(data.get(position));

                    }
                }
            });

            return view;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        catarray.clear();

        if (InternetDetect.isConnected(getApplicationContext())) {
            AsyncCategory asyncCategory = new AsyncCategory();
            asyncCategory.execute();
        } else {

        }
    }

    private class AsyncCategory extends AsyncTask<String, Void, String> {

        String id = "", name = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            catarray.clear();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "category");
            try {
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    CategoryBean categoryBean = new CategoryBean();

                    id = jsonArray.getJSONObject(i).getString("id");
                    name = jsonArray.getJSONObject(i).getString("name");

                    categoryBean.setId(id);
                    categoryBean.setName(name);

                    catarray.add(categoryBean);

                }

            } catch (Exception e) {
                System.out.println("json category error+++" + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Collections.sort(catarray, new Comparator<CategoryBean>() {
                @Override
                public int compare(CategoryBean v1, CategoryBean v2) {
                    return v1.getName().compareTo(v2.getName());
                }
            });


            mainCategoryArray = null;
            posID = null;
            mainCategoryArray = new String[catarray.size()];
            posID = new String[catarray.size()];

            int index = 0;
            for (CategoryBean value : catarray) {
                mainCategoryArray[index] = value.getName();
                posID[index] = value.getId();

                index++;
                System.out.println("Main category" + value.getName());
            }
        }
    }


}
