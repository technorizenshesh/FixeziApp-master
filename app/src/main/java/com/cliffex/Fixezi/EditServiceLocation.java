package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.Model.ServiceLocationBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.util.ProjectUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class EditServiceLocation extends AppCompatActivity {

    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    ListView ServiceLocationLV;
    String latitude = "", longitude = "";
    AutoCompleteTextView ServiceLocation;
    List<ServiceLocationBean> serviceLocationBeanArrayList;
    Button UpdateLocationBT;
    ProgressDialog progressDialog;
    RelativeLayout NavigationUpIM;
    CheckBox EditServiceLocationCB;
    private LinearLayout location_search;
    private String Address_Save;
    private EditText location_get;
    private TextView seek_bar_text_distance;
    private SeekBar seekBar;
    private String Select_radius;
    Context mContext = EditServiceLocation.this;
    private String Select_address_Raedus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading.Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_location);

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        EditServiceLocationCB = (CheckBox) findViewById(R.id.EditServiceLocationCB);

        seek_bar_text_distance = (TextView) findViewById(R.id.seek_bar_text_distance);
        seekBar = (SeekBar) findViewById(R.id.seekBar_luminosite);

        location_get = (EditText) findViewById(R.id.location_get);
        location_search = (LinearLayout) findViewById(R.id.location_search);
        toolbar_textview.setText("Edit Service Location");
        setSupportActionBar(toolbar);

        seekBar.setEnabled(false);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        location_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(EditServiceLocation.this, Select_Radius.class);
                startActivityForResult(in, 2);

                // AddressAlertDailoge();
            }
        });

        Address_Save = PreferenceConnector.readString(EditServiceLocation.this, PreferenceConnector.Address_Save, "");

        if (!Address_Save.equals("")) {
            location_get.setText(Address_Save);
        }

        ServiceLocationLV = (ListView) findViewById(R.id.ServiceLocationLV);
        ServiceLocation = (AutoCompleteTextView) findViewById(R.id.ServiceLocation);
        UpdateLocationBT = (Button) findViewById(R.id.UpdateLocationBT);

        ServiceLocationLV.setEnabled(false);
        ServiceLocation.setEnabled(false);
        UpdateLocationBT.setEnabled(false);
        location_search.setEnabled(false);

        EditServiceLocationCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    ServiceLocationLV.setEnabled(true);
                    ServiceLocation.setEnabled(true);
                    UpdateLocationBT.setEnabled(true);
                    location_search.setEnabled(true);
                } else {
                    ServiceLocationLV.setEnabled(false);
                    ServiceLocation.setEnabled(false);
                    UpdateLocationBT.setEnabled(false);
                    location_search.setEnabled(false);
                }

            }
        });

        UpdateLocationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetDetect.isConnected(EditServiceLocation.this)) {
                    updateServiceAddress();
                    // new JsonTaskUpdate().execute(sessionTradesman.getId(), Select_address_Raedus, Select_radius, latitude, longitude);
                } else {
                    Toast.makeText(EditServiceLocation.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }

//                if (serviceLocationBeanArrayList.isEmpty()) {
//
//                    Log.e("ISEMPT", "YES");
//                    Toast.makeText(EditServiceLocation.this, "Please Select a Service Location", Toast.LENGTH_SHORT).show();
//
//                } else {
//
//                    StringBuilder s = new StringBuilder();
//
//                    for (int i = 0; i < serviceLocationBeanArrayList.size(); i++) {
//
//
//                        if (i == 0) {
//                            s.append(serviceLocationBeanArrayList.get(i).getName());
//                            continue;
//                        }
//                        s.append("," + serviceLocationBeanArrayList.get(i).getName());
//
//                    }
//
//                    String AllServiceLocation = s.toString();
//
//                    Log.e("VALUE", AllServiceLocation);
//
//
//                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* seek_bar_text_distance.setText("Charges $ " + progress + "");*/

                // Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //   Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });

        ServiceLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (CheckDuplicate()) {

                    Toast.makeText(EditServiceLocation.this, "Already Selected", Toast.LENGTH_SHORT).show();

                } else {

                    ServiceLocationBean serviceLocationBean = new ServiceLocationBean();
                    serviceLocationBean.setId("MYID");
                    serviceLocationBean.setName(ServiceLocation.getText().toString());
                    serviceLocationBeanArrayList.add(serviceLocationBean);

                    ListViewAdapter adapter = new ListViewAdapter(EditServiceLocation.this, serviceLocationBeanArrayList);
                    ServiceLocationLV.setAdapter(adapter);
                }

                ServiceLocation.setText("");

            }

            private boolean CheckDuplicate() {

                for (ServiceLocationBean serviceLocationBean : serviceLocationBeanArrayList) {

                    if (serviceLocationBean.getName().equalsIgnoreCase(ServiceLocation.getText().toString())) {

                        return true;
                    }

                }
                return false;
            }

        });

        if (InternetDetect.isConnected(this)) {
            getEditAddress();
            // new JsonTaskGetServiceLocation().execute();
        } else {
            Toast.makeText(EditServiceLocation.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        if (Appconstants.postCode == null) {
            if (InternetDetect.isConnected(this)) {
                new JsonPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else if (Appconstants.postCode.isEmpty()) {
            if (InternetDetect.isConnected(this)) {
                new JsonPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            ServiceLocation.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    R.layout.custom_checkout, R.id.text1, Appconstants.postCode));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            try {
                seekBar.setProgress(0); // call these two methods before setting progress.
                seekBar.setMax(100);

                Select_radius = data.getStringExtra("Select_radius");
                Select_address_Raedus = data.getStringExtra("Select_address_Raedus");
                latitude = data.getStringExtra("lat");
                longitude = data.getStringExtra("lon");

                Log.e("dasdasdasdasd", "Select_radius = " + Select_radius);
                Log.e("dasdasdasdasd", "Select_address_Raedus = " + Select_address_Raedus);
                Log.e("dasdasdasdasd", "latitude = " + latitude);
                Log.e("dasdasdasdasd", "longitude = " + longitude);

                location_get.setText(Select_address_Raedus);
                seek_bar_text_distance.setText(Select_radius + "km");
                seekBar.setEnabled(false);
                seekBar.post(new Runnable() { @Override
                    public void run() {
                        seekBar.setProgress(parseInt(Select_radius));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void getEditAddress() {
        ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
        AndroidNetworking.get(HttpPAth.Urlpath + "get_tradesman_service_locatin_byid&tradesman_id=" + sessionTradesman.getId())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        Log.e("fasdfasdasd", "response = " + response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResult = jsonArray.getJSONObject(0);

                            location_get.setText(jsonResult.getString("parth_address"));

                            latitude = jsonResult.getString("lat");
                            longitude = jsonResult.getString("lon");

                            seek_bar_text_distance.setText(jsonResult.getString("radius") + "km");
                            seekBar.setEnabled(false);
                            seekBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        seekBar.setProgress(parseInt(jsonResult.getString("radius")));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });
    }

    private class JsonTaskGetServiceLocation extends AsyncTask<String, String, List<ServiceLocationBean>> {

        String result;

        @Override
        protected List<ServiceLocationBean> doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "get_tradesman_service_locatin_byid&tradesman_id=" + sessionTradesman.getId());
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
                    serviceLocationBean.setName(object.getString("service_locatin"));
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
                ListViewAdapter adapter = new ListViewAdapter(EditServiceLocation.this, result);
                ServiceLocationLV.setAdapter(adapter);
            }

        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        location_get = (EditText) findViewById(R.id.location_get);

        Address_Save = PreferenceConnector.readString(EditServiceLocation.this, PreferenceConnector.Address_Save, "");

        if (!Address_Save.equals("")) {
            location_get.setText(Address_Save);
        }

    }

    private class ListViewAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        private List<ServiceLocationBean> serviceLocationBeanList;


        public ListViewAdapter(Context context, List<ServiceLocationBean> serviceLocationBeanList) {

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
                view.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Set the results into TextViews
            holder.ServiceLocationName.setText(serviceLocationBeanList.get(position).getName());

            holder.DeleteLocationIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EditServiceLocationCB.isChecked()) {
                        String id = serviceLocationBeanList.get(position).getId();

                        if (id.equalsIgnoreCase("MYID")) {
                            serviceLocationBeanList.remove(position);
                            notifyDataSetChanged();
                        } else {
                            /* new JsonTaskDelete().execute(serviceLocationBeanList.get(position).getId());*/
                            serviceLocationBeanList.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                }

            });
            return view;
        }

    }

    private void updateServiceAddress() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tradesman_id", sessionTradesman.getId());
        hashMap.put("lat", latitude);
        hashMap.put("lon", longitude);
        hashMap.put("parth_address", Select_address_Raedus);
        hashMap.put("radius", Select_radius);

        Log.e("afasfdsfdsgfds", "hashMap = " + hashMap);

        ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
        AndroidNetworking.post(HttpPAth.Urlpath + "update_service_address")
                .addBodyParameter(hashMap)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });
    }

    private class JsonTaskUpdate extends AsyncTask<String, String, String> {

        String result;
        String Url;

        @Override
        protected String doInBackground(String... params) {

            Url = HttpPAth.Urlpath + "Upadte_tradesman_service_locatin&tradesman_id=" +

                    params[0] + "&parth_address=" + params[1] + "&radius=" + params[2] +
                    "&lat=" + params[3] + "&lon=" + params[4];

            Log.e("adasdsdasd", "URL before save = " + Url);

            Url = Url.replaceAll(" ", "%20");

            Log.e("adasdsdasd", "URL After save = " + Url);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(Url);
            try {
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                JSONArray jsonArray = new JSONArray(obj);
                JSONObject object = jsonArray.getJSONObject(0);

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

                    Toast.makeText(EditServiceLocation.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                }
            }

        }
    }

    private class JsonPostCode extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(HttpPAth.Urlpath + "all_postcode");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuffer stringBuffer = new StringBuffer();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                Appconstants.postCode = new ArrayList<>();
                String Jsondata = stringBuffer.toString();
                Log.e("Jsondata Load: ", Jsondata);
                JSONArray parentarray = new JSONArray(Jsondata);

                for (int i = 0; i < parentarray.length(); i++) {
                    JSONObject parentobject = parentarray.getJSONObject(i);

                    String post_code = parentobject.getString("post_code");
                    Appconstants.postCode.add(post_code);
                    result = parentobject.getString("result");

                }
                return result;
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result == null) {

            } else if (result.equalsIgnoreCase("successfully")) {

                ServiceLocation.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.custom_checkout, R.id.text1, Appconstants.postCode));

            } else {

            }

        }

    }

}
