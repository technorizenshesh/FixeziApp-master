package com.cliffex.Fixezi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.Model.SeveralAddressBean;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.SimpleDividerItemDecoration;
import com.google.gson.Gson;

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
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class SelectAddressActivity extends AppCompatActivity {

    Toolbar ToolbarSA;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    EditText SearchAddressET;
    RecyclerView AddressNameRV;
    SessionUser sessionUser;

    ArrayList<SeveralAddressBean> severalAddressBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        sessionUser = new SessionUser(this);
        ToolbarSA = (Toolbar) findViewById(R.id.ToolbarSA);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        setSupportActionBar(ToolbarSA);
        toolbar_title.setText("Select Address");

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SearchAddressET = (EditText) findViewById(R.id.SearchAddressET);
        AddressNameRV = (RecyclerView) findViewById(R.id.AddressNameRV);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        AddressNameRV.setLayoutManager(mLayoutManager);

        AddressNameRV.addItemDecoration(new SimpleDividerItemDecoration(this));

        SearchAddressET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (severalAddressBeanList == null) {

                } else {

                    final String query = s.toString().toLowerCase().trim();

                    ArrayList<SeveralAddressBean> filteredList = new ArrayList<>();
                    for (int i = 0; i < severalAddressBeanList.size(); i++) {

                        final String text = severalAddressBeanList.get(i).getFile_name().toLowerCase();

                        if (text.contains(query)) {
                            filteredList.add(severalAddressBeanList.get(i));
                        }

                    }

                    AddressAdapter adapter = new AddressAdapter(SelectAddressActivity.this, filteredList);
                    AddressNameRV.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (InternetDetect.isConnected(this)) {

            new JsonLoadAddress().execute();
        } else {

            Toast.makeText(SelectAddressActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class JsonLoadAddress extends AsyncTask<String, String, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                    URL url = new URL(HttpPAth.Urlpath + "get_relative_accuracy_by_user");
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
                Log.e("JsonLoadAddress", response);

                severalAddressBeanList = new ArrayList<SeveralAddressBean>();
                Gson gson = new Gson();

                JSONArray parentarray = new JSONArray(response);

                for (int i = 0; i < parentarray.length(); i++) {

                    JSONObject parentobject = parentarray.getJSONObject(i);
                    SeveralAddressBean severalAddressBean = gson.fromJson(parentobject.toString(), SeveralAddressBean.class);

                    result = parentobject.getString("result");
                    severalAddressBeanList.add(severalAddressBean);

                }
                return result;

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else if (result.equalsIgnoreCase("successfully")) {

                AddressAdapter adapter = new AddressAdapter(SelectAddressActivity.this, severalAddressBeanList);
                AddressNameRV.setAdapter(adapter);

            } else {

            }
        }
    }


    private class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

        private Context mContext;
        private List<SeveralAddressBean> severalAddressBeanList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView AddressNameTV;

            public MyViewHolder(View view) {
                super(view);

                AddressNameTV = (TextView) view.findViewById(R.id.AddressNameTV);
            }
        }


        public AddressAdapter(Context mContext, List<SeveralAddressBean> severalAddressBeanList) {
            this.mContext = mContext;
            this.severalAddressBeanList = severalAddressBeanList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.address_rowitem, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.AddressNameTV.setText(severalAddressBeanList.get(position).getFile_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("PersonOnSite", severalAddressBeanList.get(position).getPerson_site());
                    returnIntent.putExtra("HouseNumber", severalAddressBeanList.get(position).getHousenoo());
                    returnIntent.putExtra("StreetName", severalAddressBeanList.get(position).getStreet());
                    returnIntent.putExtra("PostCode", severalAddressBeanList.get(position).getPost_code());
                    returnIntent.putExtra("CityState", severalAddressBeanList.get(position).getCity());
                    returnIntent.putExtra("HomeNumber", severalAddressBeanList.get(position).getHome_no());
                    returnIntent.putExtra("MobileNumber", severalAddressBeanList.get(position).getMobile_phone());
                    returnIntent.putExtra("Id", severalAddressBeanList.get(position).getId());
                    returnIntent.putExtra("r_name", severalAddressBeanList.get(position).getR_name());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
            });
        }

        @Override
        public int getItemCount() {

            return severalAddressBeanList == null ? 0 : severalAddressBeanList.size();

        }
    }

}
