package com.cliffex.Fixezi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

public class EnterDifferentAddress extends AppCompatActivity {

    Toolbar ToolbarEDA;
    TextView toolbar_title;
    RelativeLayout NavigationUpIM;
    TextView LoadAddressBT, DeleteAddressBT, SubmitAddressBT, SaveAddressBT;
    EditText MobileNumberET, HomeNumberET, CityStateET, StreetNameET, HouseNumberET, PersonOnSiteET;
    AutoCompleteTextView PostCodeET;
    SessionUser sessionUser;
    private EditText relationship_person;
    private EditText diiferent_add;
    private EditText other_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_different_address);

        sessionUser = new SessionUser(this);

        ToolbarEDA = (Toolbar) findViewById(R.id.ToolbarEDA);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        setSupportActionBar(ToolbarEDA);
        toolbar_title.setText("Enter Different Address");

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LoadAddressBT = (TextView) findViewById(R.id.LoadAddressBT);
        relationship_person = (EditText) findViewById(R.id.relationship_person);
        SaveAddressBT = (TextView) findViewById(R.id.SaveAddressBT);
        SubmitAddressBT = (TextView) findViewById(R.id.SubmitAddressBT);
        DeleteAddressBT = (TextView) findViewById(R.id.DeleteAddressBT);
        PersonOnSiteET = (EditText) findViewById(R.id.PersonOnSiteET);
        HouseNumberET = (EditText) findViewById(R.id.HouseNumberET);
        StreetNameET = (EditText) findViewById(R.id.StreetNameET);
        PostCodeET = (AutoCompleteTextView) findViewById(R.id.PostCodeET);
        CityStateET = (EditText) findViewById(R.id.CityStateET);
        HomeNumberET = (EditText) findViewById(R.id.HomeNumberET);
        MobileNumberET = (EditText) findViewById(R.id.MobileNumberET);
        diiferent_add = (EditText) findViewById(R.id.diiferent_add);
        other_phone = (EditText) findViewById(R.id.other_phone);

        relationship_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(EnterDifferentAddress.this,relationship_person);
                // Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                // registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        relationship_person.setText(item.getTitle());
                        Appconstants.RELATION_STATUS = item.getTitle().toString().trim();
                        if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.landlord))) {
                            Appconstants.RELATION_ID = "1";
                        } else if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.real_estate_agent))){
                            Appconstants.RELATION_ID = "2";
                        } else if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.family_member))){
                            Appconstants.RELATION_ID = "3";
                        } else if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.friend))){
                            Appconstants.RELATION_ID = "4";
                        } else if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.tradesman))){
                            Appconstants.RELATION_ID = "5";
                        } else if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.contractor))){
                            Appconstants.RELATION_ID = "6";
                        } else if(item.getTitle().toString().trim().equalsIgnoreCase(getString(R.string.i_am_the_owner))){
                            Appconstants.RELATION_ID = "7";
                        }
                        return true;
                    }
                });
                popup.show(); // showing popup menu
            }
        });

        DeleteAddressBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterDifferentAddress.this, SelectAddressActivity.class);
                startActivityForResult(intent, 20);
            }
        });

        SubmitAddressBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()) {
                    Appconstants.WHICH_TYPE_ADDRESS = "Postal Address";
                    Appconstants.SITE_ADDRESS = HouseNumberET.getText().toString() + ", " + StreetNameET.getText().toString() + ", " + PostCodeET.getText().toString() + ", " + CityStateET.getText().toString();
                    Appconstants.PERSON_ON_SITE = PersonOnSiteET.getText().toString();
                    Appconstants.SITE_HOME_NUMBER = HomeNumberET.getText().toString();
                    Appconstants.SITE_MOBILE_NUMBER = MobileNumberET.getText().toString();
                    // Appconstants.ServiceLocation = PostCodeET.getText().toString();
                    acceptCancelDialog();
                }
            }

            private boolean Validation() {
                if (PersonOnSiteET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (relationship_person.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Relationship Person", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (diiferent_add.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Different Address", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (other_phone.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Other Phone", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (MobileNumberET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Moblile Number", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

        diiferent_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EnterDifferentAddress.this, GooglePlacesAutocompleteActivity.class);
                startActivityForResult(in, 101);
            }
        });

        SaveAddressBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(EnterDifferentAddress.this);
                    alert.setTitle("Save As");
                    alert.setMessage("File Name :");

                    final EditText input = new EditText(EnterDifferentAddress.this);
                    alert.setView(input);

                    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    String value = input.getText().toString();

                                    if (input.getText().toString().equalsIgnoreCase("")) {
                                        Toast.makeText(EnterDifferentAddress.this, "Not Saved. Please enter a file name", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (InternetDetect.isConnected(EnterDifferentAddress.this)) {
                                            new JsonSaveAddress().execute(PersonOnSiteET.getText().toString(), other_phone.getText().toString(),
                                                    diiferent_add.getText().toString(), PostCodeET.getText().toString(), CityStateET.getText().toString(),
                                                    other_phone.getText().toString(), MobileNumberET.getText().toString(), value,relationship_person.getText().toString());
                                        } else {
                                            Toast.makeText(EnterDifferentAddress.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                }
                            }

                    );

                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    return;
                                }
                            }
                    );
                    alert.show();
                }
            }

            private boolean Validation() {

                if (PersonOnSiteET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (MobileNumberET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter Moblile Number", Toast.LENGTH_SHORT).show();
                    return false;
                }

                /* else if (HouseNumberET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(EnterDifferentAddress.this, "Enter House Number", Toast.LENGTH_SHORT).show();
                    return false;
                }*/ /*else if (StreetNameET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(EnterDifferentAddress.this, "Enter Street Name", Toast.LENGTH_SHORT).show();
                    return false;
                } *//*else if (PostCodeET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(EnterDifferentAddress.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                    return false;
                } *//*else if (CityStateET.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(EnterDifferentAddress.this, "Enter City", Toast.LENGTH_SHORT).show();
                    return false;
                } */
                return true;
            }

        });

        LoadAddressBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterDifferentAddress.this, SelectAddressActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        if (InternetDetect.isConnected(this)) {
            new JsontaskPostCode().execute();
        } else {
            Toast.makeText(EnterDifferentAddress.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void acceptCancelDialog() {

        Dialog dialog = new Dialog(EnterDifferentAddress.this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.accept_cancel_address_dialog);

        Button btNo = dialog.findViewById(R.id.btNo);
        Button btYes = dialog.findViewById(R.id.btYes);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btYes.setOnClickListener(v -> {
            continueDialog();
            dialog.dismiss();
        });

        dialog.show();

    }

    private void continueDialog() {
        Dialog dialog = new Dialog(EnterDifferentAddress.this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.continue_address_dialog);

        Button btContinue = dialog.findViewById(R.id.btContinue);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        btContinue.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
            dialog.dismiss();
        });

        dialog.show();

    }

    private class JsontaskPostCode extends AsyncTask<String,String,String> {

        String result = "";
        ArrayList<String> postCodeList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                postCodeList = new ArrayList<>();
                String Jsondata = stringBuffer.toString();
                Log.e("Jsondata Load: ", Jsondata);
                JSONArray parentarray = new JSONArray(Jsondata);

                for (int i = 0; i < parentarray.length(); i++) {
                    JSONObject parentobject = parentarray.getJSONObject(i);

                    String post_code = parentobject.getString("post_code");
                    postCodeList.add(post_code);
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

            if (result == null) {

                Toast.makeText(EnterDifferentAddress.this, "Server Error", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {

                PostCodeET.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.custom_checkout, R.id.text1, postCodeList));
            } else {
                Log.e("Post Code else", "yes");
            }

        }

    }


    private class JsonSaveAddress extends AsyncTask<String, String, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

//            person_site
//            housenoo
//            street
//            post_code
//            city
//            home_no
//            mobile_phone
//            file_name
//            user_id
//            dif_lat
//            dif_lon
//            r_name
//            r_id

            try {
                URL url = new URL(HttpPAth.Urlpath + "update_relative_information");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("person_site", strings[0]);
                params.put("housenoo", strings[1]);
                params.put("street", strings[2]);
                params.put("post_code", strings[3]);
                params.put("city", strings[4]);
                params.put("home_no", strings[5]);
                params.put("mobile_phone", strings[6]);
                params.put("file_name", strings[7]);
                params.put("user_id", sessionUser.getId());
                params.put("dif_lat", "");
                params.put("dif_lon", "");
                params.put("r_name", strings[8]);
                params.put("r_id", "");

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
                Log.e("JsonSaveAddress", response);

                JSONArray parentarray = new JSONArray(response);
                JSONObject parentobject = parentarray.getJSONObject(0);
                result = parentobject.getString("result");

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
                Toast.makeText(EnterDifferentAddress.this, "Server Error", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(EnterDifferentAddress.this, "Address Saved", Toast.LENGTH_SHORT).show();

            } else {

            }
        }
    }


    private class JsonDeleteAddress extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(HttpPAth.Urlpath + "delete_relative_accuracy_by_user");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("id", strings[0]);

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
                Log.e("JsonDeleted", response);

                JSONObject parentobject = new JSONObject(response);
                String result = parentobject.getString("result");

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
                Toast.makeText(EnterDifferentAddress.this, "Server Error", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {
                Toast.makeText(EnterDifferentAddress.this, "Address Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address != null) {
                try {
                    Address location = address.get(0);
                    Log.e("dsfasdadas","getLatitude = " + location.getLatitude());
                    Log.e("dsfasdadas","getLongitude = " + location.getLongitude());
                    Appconstants.servicelocation = diiferent_add.getText().toString().trim();
                    Appconstants.ServiceLocation = diiferent_add.getText().toString().trim();
                    Appconstants.lat = location.getLatitude();
                    Appconstants.lon = location.getLongitude();
//                    Intent intent = new Intent();
//                    intent.putExtra("lat",location.getLatitude());
//                    intent.putExtra("lon",location.getLongitude());
//                    intent.putExtra("add",diiferent_add.getText().toString().trim());
//                    setResult(10,intent);
//                    finish();
                } catch (Exception er) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLatLonFromAddress(String address) {
        ProjectUtil.showProgressDialog(EnterDifferentAddress.this,false,"Please wait...");
        String postReceiverUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                address.replace(",","+") + "&key=" +
                getResources().getString(R.string.places_api_key);
        Log.e("asdasdasds","Plcaes Url = " + postReceiverUrl);
        AndroidNetworking.get(postReceiverUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                ProjectUtil.pauseProgressDialog();
                Log.e("responseresponse",response);
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray resultArray = mainObj.getJSONArray("results");
                    JSONObject resultFirstObj = resultArray.getJSONObject(0);
                    JSONObject geometryObj = resultFirstObj.getJSONObject("geometry");
                    JSONObject locationObj = geometryObj.getJSONObject("location");

                    double lat = locationObj.getDouble("lat");
                    double lon = locationObj.getDouble("lng");

                    Appconstants.servicelocation = diiferent_add.getText().toString().trim();
                    Appconstants.ServiceLocation = diiferent_add.getText().toString().trim();
                    Appconstants.SITE_ADDRESS = diiferent_add.getText().toString().trim();
                    Appconstants.lat = lat;
                    Appconstants.lon = lon;

                    Log.e("sfsdfsdfsdf","Address = " + diiferent_add.getText().toString().trim());
                    Log.e("sfsdfsdfsdf","SITE_ADDRESS = " + Appconstants.SITE_ADDRESS);
                    Log.e("sfsdfsdfsdf","lat = " + lat);
                    Log.e("sfsdfsdfsdf","lon = " + lon);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                ProjectUtil.pauseProgressDialog();
                Log.e("responseresponse","anError = "+anError.getErrorBody());
                Log.e("responseresponse","anError = "+anError.getErrorDetail());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 101) {
            String add = data.getStringExtra("add");
            diiferent_add.setText(add);
            getLatLonFromAddress(add);
            Log.e("asdadadasd", "address = " + add);
        }

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                PersonOnSiteET.setText(data.getStringExtra("PersonOnSite"));
                other_phone.setText(data.getStringExtra("HouseNumber"));
                diiferent_add.setText(data.getStringExtra("StreetName"));
                getLatLonFromAddress(diiferent_add.getText().toString().trim());
                PostCodeET.setText(data.getStringExtra("PostCode"));
                CityStateET.setText(data.getStringExtra("CityState"));
                relationship_person.setText(data.getStringExtra("r_name"));
                // HomeNumberET.setText(data.getStringExtra("HomeNumber"));
                MobileNumberET.setText(data.getStringExtra("MobileNumber"));

                UserActivity.address = data.getStringExtra("StreetName");

                Appconstants.RELATION_STATUS = data.getStringExtra("r_name");
                if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.landlord))) {
                    Appconstants.RELATION_ID = "1";
                } else if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.real_estate_agent))){
                    Appconstants.RELATION_ID = "2";
                } else if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.family_member))){
                    Appconstants.RELATION_ID = "3";
                } else if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.friend))){
                    Appconstants.RELATION_ID = "4";
                } else if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.tradesman))){
                    Appconstants.RELATION_ID = "5";
                } else if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.contractor))){
                    Appconstants.RELATION_ID = "6";
                } else if(data.getStringExtra("r_name").trim().equalsIgnoreCase(getString(R.string.i_am_the_owner))){
                    Appconstants.RELATION_ID = "7";
                }
            }
        }
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                String Id = data.getStringExtra("Id");
                if (InternetDetect.isConnected(EnterDifferentAddress.this)) {
                    new JsonDeleteAddress().execute(Id);
                } else {
                    Toast.makeText(EnterDifferentAddress.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }
}
