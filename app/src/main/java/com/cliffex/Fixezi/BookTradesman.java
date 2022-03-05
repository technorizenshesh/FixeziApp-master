package com.cliffex.Fixezi;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookTradesman extends AppCompatActivity {

    Toolbar ToolbarBT;
    TextView toolbar_title, OfficeNumberBT, MobileNumberBT, BusinessNameBT, BookNowTVBT, CancelTVBT,
            WebUrlBT, ServicingLocationBT, CompanyDetailBT, TradesmanEmailBT;
    TradesManBean tradesManBean;
    ImageView TradesmanImageBT;
    RelativeLayout NavigationUpIM;
    Dialog postalDialog;
    SessionUser sessionUser;
    ScrollView ParentSV;
    RelativeLayout RLWebUrl, RLServicingLocation;
    ImageView CompanyDetailUploadIM, img_company_detailsUpload;
    TextView OutOfTV, tv_value_chage;
    FrameLayout.LayoutParams layoutParams;
    RatingBar AffordabilityRB, WorkmanshipRB, PucntualRB;
    CardView CompanyDetailCC;
    SeekBar seekBar_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tradesman);

        Log.e("DATE_SELECTed123", "BookTradesman = " + Appconstants.DATE_SELECTED);

        ToolbarBT = (Toolbar) findViewById(R.id.ToolbarBT);
        toolbar_title = (TextView) ToolbarBT.findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) ToolbarBT.findViewById(R.id.NavigationUpIM);
        TradesmanImageBT = (ImageView) findViewById(R.id.TradesmanImageBT);
        img_company_detailsUpload = (ImageView) findViewById(R.id.img_company_detailsUpload);
        BusinessNameBT = (TextView) findViewById(R.id.BusinessNameBT);
        MobileNumberBT = (TextView) findViewById(R.id.MobileNumberBT);
        OfficeNumberBT = (TextView) findViewById(R.id.OfficeNumberBT);
        TradesmanEmailBT = (TextView) findViewById(R.id.TradesmanEmailBT);
        WebUrlBT = (TextView) findViewById(R.id.WebUrlBT);
        RLWebUrl = (RelativeLayout) findViewById(R.id.RLWebUrl);
        RLServicingLocation = (RelativeLayout) findViewById(R.id.RLServicingLocation);
        ServicingLocationBT = (TextView) findViewById(R.id.ServicingLocationBT);
        tv_value_chage = (TextView) findViewById(R.id.tv_value_chage);
        CompanyDetailBT = (TextView) findViewById(R.id.CompanyDetailBT);
        CompanyDetailCC = (CardView) findViewById(R.id.CompanyDetailCC);
        seekBar_book = (SeekBar) findViewById(R.id.seekBar_book);

        CompanyDetailUploadIM = (ImageView) findViewById(R.id.CompanyDetailUploadIM);
        ParentSV = (ScrollView) findViewById(R.id.ParentSV);
        BookNowTVBT = (TextView) findViewById(R.id.BookNowTVBT);
        CancelTVBT = (TextView) findViewById(R.id.CancelTVBT);
        OutOfTV = (TextView) findViewById(R.id.OutOfTV);

        PucntualRB = (RatingBar) findViewById(R.id.PucntualRB);
        WorkmanshipRB = (RatingBar) findViewById(R.id.WorkmanshipRB);
        AffordabilityRB = (RatingBar) findViewById(R.id.AffordabilityRB);

        sessionUser = new SessionUser(this);
        toolbar_title.setText("Book Tradesman");
        setSupportActionBar(ToolbarBT);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        String Data = extra.getString("Data");

        Log.e("Data", Data);

        try {

            JSONObject object = new JSONObject(Data);

            tradesManBean = new TradesManBean();

            tradesManBean.setId(object.getString("id"));

            tradesManBean.setBusiness_name(object.getString("business_name"));

            tradesManBean.setBusiness_address(object.getString("businessAddress"));

            tradesManBean.setTrading_name(object.getString("trading_name"));

            tradesManBean.setOffice_no(object.getString("office_no"));

            tradesManBean.setCompany_detail(object.getString("company_detail"));

            tradesManBean.setCompany_detail_upload(object.getString("company_detail_upload"));

            tradesManBean.setBusinessOwnerrnName(object.getString("businessOwnerrnName"));

            tradesManBean.setMobile_no(object.getString("mobile_no"));

            tradesManBean.setEmail(object.getString("email"));

            tradesManBean.setWebsite_url(object.getString("website_url"));

            tradesManBean.setLatitude(object.getString("latitude"));

            tradesManBean.setLongitude(object.getString("longitude"));

            tradesManBean.setHour_min(object.getString("hour_min"));

            tradesManBean.setAfter_hours(object.getString("after_hours"));

            tradesManBean.setSelect_trade(object.getString("select_trade"));

            tradesManBean.setLicense_pic(object.getString("license_pic"));

            tradesManBean.setUser_id(object.getString("user_id"));

            tradesManBean.setWork(object.getString("work"));

            tradesManBean.setProfile_pic(object.getString("profile_pic"));

            tradesManBean.setCompany_profile_ratio(object.getString("company_profile_ratio"));

            tradesManBean.setCompany_upload_ratio(object.getString("company_upload_ratio"));

            if (object.getJSONArray("punctual").length() == 0) {
            } else {
                float punctualRate = computeRatingNormal(object.getJSONArray("punctual"));
                PucntualRB.setRating(punctualRate);
            }

            float workmanRate = computeRatingNormal(object.getJSONArray("workmanship"));
            float affordableRate = computeRatingNormal(object.getJSONArray("affordability"));

            WorkmanshipRB.setRating(workmanRate);
            AffordabilityRB.setRating(affordableRate);

            JSONArray ServiceLocationArray = object.getJSONArray("service_locatin");
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < ServiceLocationArray.length(); i++) {
                if (i == 0) {
                    s.append(ServiceLocationArray.get(i));
                    continue;
                }
                s.append("," + ServiceLocationArray.get(i));
            }

            String AllLocation = s.toString();

            BusinessNameBT.setText(tradesManBean.getBusiness_name());
            OfficeNumberBT.setText(tradesManBean.getOffice_no());
            MobileNumberBT.setText(tradesManBean.getMobile_no());
            TradesmanEmailBT.setText(tradesManBean.getEmail());

            if (tradesManBean.getCompany_detail().equalsIgnoreCase("")) {
                CompanyDetailCC.setVisibility(View.GONE);
            } else {
                CompanyDetailCC.setVisibility(View.VISIBLE);
                CompanyDetailBT.setText(tradesManBean.getCompany_detail());
            }

            ServicingLocationBT.setText(AllLocation);

           /* RBPunctual.setRating(tradesManBean.getPunctual());
            RBWorkmanship.setRating(tradesManBean.getWorkmanship());
            RBAffordablity.setRating(tradesManBean.getAffordability());*/

            Glide.with(BookTradesman.this)
                    .load(tradesManBean.getProfile_pic())
                    .thumbnail(0.5f).into(TradesmanImageBT);

            Glide.with(BookTradesman.this)
                    .load(tradesManBean.getCompany_detail_upload())
                    .thumbnail(0.5f).into(img_company_detailsUpload);

            if (tradesManBean.getFixed_Price() == null ||
                    tradesManBean.getFixed_Price().equals("")) {
                tv_value_chage.setText("Charges $ " + 50);
                // seekBar_book.setProgress(Integer.parseInt(tradesManBean.getHour_min()));
            } else {
                tv_value_chage.setText("Charges $ " + tradesManBean.getFixed_Price());
            }

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int width = displaymetrics.widthPixels;
            int height = displaymetrics.heightPixels;

            int MyHeight = width;

            if (tradesManBean.getCompany_profile_ratio().equalsIgnoreCase("43")) {
                MyHeight = ((width * 3) / 4);
            } else if (tradesManBean.getCompany_profile_ratio().equalsIgnoreCase("169")) {
                MyHeight = ((width * 9) / 16);
            } else if (tradesManBean.getCompany_profile_ratio().equalsIgnoreCase("44")) {
                MyHeight = width;
            }

            layoutParams = new FrameLayout.LayoutParams(width, MyHeight);
            layoutParams.gravity = Gravity.CENTER;
            TradesmanImageBT.setLayoutParams(layoutParams);

            if (!(tradesManBean.getCompany_detail_upload().contains("EMPTY"))) {
                CompanyDetailUploadIM.setVisibility(View.VISIBLE);

                Glide.with(BookTradesman.this).load(tradesManBean.getCompany_detail_upload())
                        .thumbnail(0.5f)
                        .into(CompanyDetailUploadIM);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Book Trademan", e.getMessage());
        }

        CompanyDetailUploadIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookTradesman.this, MyFullScreenActivity.class);
                intent.putExtra("ImagePath", tradesManBean.getCompany_detail_upload());
                startActivity(intent);
            }
        });

        BookNowTVBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postalDialog = new Dialog(BookTradesman.this);
                postalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                postalDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                postalDialog.setContentView(R.layout.alert_dialog_book_tradesman);

                final Button cancelBt = (Button) postalDialog.findViewById(R.id.cancelBt);
                final Button acceptBt = (Button) postalDialog.findViewById(R.id.acceptBt);

                cancelBt.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    postalDialog.dismiss();
                                                }
                                            }
                );

                acceptBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Appconstants.TradesmanBusinessName = tradesManBean.getBusiness_name();
                        Appconstants.TradesmanOfficeNumber = tradesManBean.getOffice_no();
                        Appconstants.TradesmanMobileNumber = tradesManBean.getMobile_no();
                        new JsonTaskBookTradesman().execute(tradesManBean.getId());
                        postalDialog.dismiss();
                    }
                });
                postalDialog.show();
            }
        });

        CancelTVBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        seekBar_book.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

    }

    private class JsonTaskBookTradesman extends AsyncTask<String, Void, String> {

        String ProblemId, TrademanId;

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "booking_tradesman&");
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("date", Appconstants.DATE_SELECTED));
                nameValuePairs.add(new BasicNameValuePair("time", Appconstants.TIME_SELECTED));
                nameValuePairs.add(new BasicNameValuePair("IsDateFlexible", Appconstants.dateFlexi));
                nameValuePairs.add(new BasicNameValuePair("IsTimeFlexible", Appconstants.timeFlexi));
                nameValuePairs.add(new BasicNameValuePair("PersonOnSite", Appconstants.PERSON_ON_SITE));
                nameValuePairs.add(new BasicNameValuePair("Job_Address", Appconstants.SITE_ADDRESS));
                nameValuePairs.add(new BasicNameValuePair("street", Appconstants.SITE_ADDRESS));
                nameValuePairs.add(new BasicNameValuePair("country", ""));
                nameValuePairs.add(new BasicNameValuePair("state", ""));
                nameValuePairs.add(new BasicNameValuePair("city", ""));
                nameValuePairs.add(new BasicNameValuePair("r_status", Appconstants.RELATION_STATUS));
                nameValuePairs.add(new BasicNameValuePair("r_id", Appconstants.RELATION_ID));
                nameValuePairs.add(new BasicNameValuePair("Home_Number", Appconstants.SITE_HOME_NUMBER));
                nameValuePairs.add(new BasicNameValuePair("Mobile_Number", Appconstants.SITE_MOBILE_NUMBER));
                nameValuePairs.add(new BasicNameValuePair("Job_Request", Appconstants.JOB_REQUEST));
                nameValuePairs.add(new BasicNameValuePair("user_id", sessionUser.getId()));
                nameValuePairs.add(new BasicNameValuePair("Tradesman_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("problem", Appconstants.PROBLEM_SELECTED));
                nameValuePairs.add(new BasicNameValuePair("IsTimeFlexible_value", Appconstants.timeFlexibleValue));

                Log.e("nameValuePairs", "nameValuePairs = " + nameValuePairs.toString());

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);

                String object = EntityUtils.toString(response.getEntity());
                Log.e("JsonData>>>>>Book", ">>" + object);

                JSONArray jsonArray = new JSONArray(object);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String result = jsonObject.getString("result");

                ProblemId = jsonObject.getString("id");
                TrademanId = jsonObject.getString("Tradesman_id");

                return result;

            } catch (Exception e) {
                System.out.println("exception in confirmation++" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(BookTradesman.this, "Server Problem", Toast.LENGTH_SHORT).show();
            } else if (result.equalsIgnoreCase("successfully")) {
                Intent intent = new Intent(BookTradesman.this, JobSent.class);
                intent.putExtra("ProblemId", ProblemId);
                intent.putExtra("TrademanId", TrademanId);
                startActivity(intent);
            }
        }
    }

    private float computeRatingNormal(JSONArray jsonArray) {

        float puctualRate = 0;
        int userCount = 0;

        List<String> punctualList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                punctualList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ComputeRateJson", e.getMessage());
                return 0;
            }
        }

        if (!(punctualList.isEmpty())) {

            for (int i = 0; i < punctualList.size(); i++) {

                if (!(punctualList.get(i).equalsIgnoreCase(""))) {

                    puctualRate = puctualRate + Float.parseFloat(punctualList.get(i));
                    userCount = userCount + 1;
                }

            }

            puctualRate = puctualRate / userCount;

            OutOfTV.setText("Out of " + userCount + " Users");

        }
        return puctualRate;
    }
}

