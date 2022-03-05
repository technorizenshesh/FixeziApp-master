package com.cliffex.Fixezi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.HttpPAth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import me.crosswall.lib.coverflow.core.PagerContainer;import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class SelectTrademan extends AppCompatActivity {

    List<TradesManBean> tradesManBeanList;
    Toolbar selecttradesman_toolbar;
    TextView toolbar_title,
            tradeenameee,
            selectedfromprevious,
            selectacompanytxttt,
            youhaveselecttradementxttt;
    ImageView right, left, CallOutFeeInfoST;
    RelativeLayout NavigationUpIM;
    SessionUser sessionUser;
    ArrayList<String> JsonObjectList;
    ViewPager pager;
    PagerContainer container;
    Button QuoteNo, QuoteYes;
    FrameLayout.LayoutParams layoutParams;
    TextView tv_selecttedArea;
    RelativeLayout rel_needqoutes;
    private String search_address;
    private MyPagerAdapter adapter;
    private RecyclerView PagerContainerST1;
    private LinearLayoutManager HorizontalLayout;
    private String status = "";
    private String select_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_tradesman_activity);

        Log.e("DATE_SELECTed123", "SelectTrademan = " + Appconstants.DATE_SELECTED);
        Log.e("DATE_SELECTed123", "Appconstants.SITE_ADDRESS = " + Appconstants.SITE_ADDRESS);

        PreferenceConnector.writeString(SelectTrademan.this,
                PreferenceConnector.Address_Save, "");
        PreferenceConnector.writeString(SelectTrademan.this,
                PreferenceConnector.Address_Save1, "");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                search_address = "";
                status = "null";
            } else {
                search_address = extras.getString("address");
                status = extras.getString("status");
                select_status = extras.getString("select_status");
            }
        } else {
            search_address = (String) savedInstanceState.getSerializable("address");
            status = (String) savedInstanceState.getSerializable("status");
        }

        selecttradesman_toolbar = (Toolbar) findViewById(R.id.selecttradesman_toolbar);
        toolbar_title = (TextView) selecttradesman_toolbar.findViewById(R.id.toolbar_title);
        tradeenameee = (TextView) findViewById(R.id.tradeenameee);
        selectedfromprevious = (TextView) findViewById(R.id.selectedfromprevious);
        selectacompanytxttt = (TextView) findViewById(R.id.selectacompanytxttt);
        tv_selecttedArea = (TextView) findViewById(R.id.tv_selecttedArea);
        youhaveselecttradementxttt = (TextView) findViewById(R.id.youhaveselecttradementxttt);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        pager = (ViewPager) findViewById(R.id.OverlapPagerST);
        CallOutFeeInfoST = (ImageView) findViewById(R.id.CallOutFeeInfoST);
        PagerContainerST1 = (RecyclerView) findViewById(R.id.PagerContainerST1);
        QuoteYes = (Button) findViewById(R.id.QuoteYes);
        QuoteNo = (Button) findViewById(R.id.QuoteNo);
        rel_needqoutes = (RelativeLayout) findViewById(R.id.rel_needqoutes);

        left = (ImageView) findViewById(R.id.left);
        sessionUser = new SessionUser(this);
        toolbar_title.setText("Select Tradesman");
        setSupportActionBar(selecttradesman_toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectedfromprevious.setText(Appconstants.Category);
        tradeenameee.setText(Appconstants.PREVIOUSTRADE_SELECTED);

        tradesManBeanList = new ArrayList<>();
        JsonObjectList = new ArrayList<>();

        Log.e("VAlue1", Appconstants.PREVIOUSTRADE_SELECTED);
        Log.e("VAlue2", search_address);
        Log.e("VAlue3", Appconstants.Category);
        Log.e("VAlue4", Appconstants.afterhours);

        if (InternetDetect.isConnected(this)) {
            
            new JsonTask().execute();
            
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        if (select_status.equals("quick_search")) {
        }

        QuoteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog QuoteDialog = new Dialog(SelectTrademan.this);
                QuoteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                QuoteDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                QuoteDialog.setContentView(R.layout.quote_dialog);

                TextView DoneQuote = (TextView) QuoteDialog.findViewById(R.id.DoneQuote);
                TextView CancelQuote = (TextView) QuoteDialog.findViewById(R.id.CancelQuote);
                final EditText AmountQuoteET = (EditText) QuoteDialog.findViewById(R.id.AmountQuoteET);
                final EditText ValidityQuoteET = (EditText) QuoteDialog.findViewById(R.id.ValidityQuoteET);

                CancelQuote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuoteDialog.dismiss();
                    }
                });

                DoneQuote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Validation()) {
                            Appconstants.IsQuoteSelected = "Yes";
                            QuoteYes.setBackgroundResource(R.drawable.border_black_solid_red);
                            QuoteYes.setTextColor(Color.parseColor("#ffffff"));
                            QuoteNo.setBackgroundResource(R.drawable.border_black_solid_white);
                            QuoteNo.setTextColor(Color.parseColor("#000000"));
                            QuoteDialog.dismiss();
                        }

                    }

                    private boolean Validation() {
                        if (AmountQuoteET.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(SelectTrademan.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                            return false;
                        } else if (ValidityQuoteET.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(SelectTrademan.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    }

                });

                QuoteDialog.show();
            }
        });

        QuoteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.IsQuoteSelected = "No";
                QuoteNo.setBackgroundResource(R.drawable.border_black_solid_red);
                QuoteNo.setTextColor(Color.parseColor("#ffffff"));
                QuoteYes.setBackgroundResource(R.drawable.border_black_solid_white);
                QuoteYes.setTextColor(Color.parseColor("#000000"));
            }
        });

        rel_needqoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SelectTrademan.this);
                dialog.setContentView(R.layout.dialog_need_quotes);
                Button btn_di_ok = (Button) dialog.findViewById(R.id.btn_di_ok);
                btn_di_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {

            int CurrentItem = 0;

            @Override
            public void onClick(View v) {

                CurrentItem = pager.getCurrentItem();
                if (CurrentItem == 0) {

                } else {

                    pager.setCurrentItem(CurrentItem - 1);
                }
            }
        });

        CallOutFeeInfoST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog YourAddDialog = new Dialog(SelectTrademan.this);
                YourAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                YourAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                YourAddDialog.setContentView(R.layout.alert_dialog_call_out_fee_st);

                final Button OkayBT = (Button) YourAddDialog.findViewById(R.id.okBtST);

                OkayBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YourAddDialog.dismiss();
                    }
                });

                YourAddDialog.show();

            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        int MyWidth = width / 2;
        layoutParams = new FrameLayout.LayoutParams(MyWidth, MyWidth);
        layoutParams.gravity = Gravity.CENTER;
        pager.setLayoutParams(layoutParams);

    }

    public class JsonTask extends AsyncTask<String, String, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//          Toast.makeText(SelectTrademan.this, Appconstants.afterhours + "," + Appconstants.Category + ","
//                  + Appconstants.ServiceLocation + "," + Appconstants.PREVIOUSTRADE_SELECTED, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(HttpPAth.Urlpath + "get_tradesman_user_by_trade");

                List<NameValuePair> list = new ArrayList<NameValuePair>();

//              HashMap<String,String> hashMap = new HashMap<>();
//              hashMap.put("select_trade",Appconstants.PREVIOUSTRADE_SELECTED);
//              hashMap.put("service_locatin",Appconstants.ServiceLocation);
//              hashMap.put("address_type",Appconstants.Category);
//              hashMap.put("after_hours",Appconstants.afterhours);
//              hashMap.put("lat", String.valueOf(Appconstants.lat));
//              hashMap.put("lon",String.valueOf(Appconstants.lon));

//              Log.e("paramslisttradman","hashmap = " + hashMap.toString());

                list.add(new BasicNameValuePair("select_trade", Appconstants.PREVIOUSTRADE_SELECTED));
                list.add(new BasicNameValuePair("service_locatin", "" + search_address));
                list.add(new BasicNameValuePair("address_type", Appconstants.Category));
                list.add(new BasicNameValuePair("after_hours", Appconstants.afterhours));
                list.add(new BasicNameValuePair("lat", String.valueOf(Appconstants.lat)));
                list.add(new BasicNameValuePair("lon", String.valueOf(Appconstants.lon)));
                list.add(new BasicNameValuePair("parth_address", "" + search_address));

                Log.e("paramslisttradman", "" + list);

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

                String Jsondata = stringBuffer.toString();
                Log.e("JsonSelectTrades : ", Jsondata);

                JSONArray parentArray = new JSONArray(Jsondata);

                for (int i = 0; i < parentArray.length(); i++) {

                    JSONObject finalobject = parentArray.getJSONObject(i);

                    JsonObjectList.add(String.valueOf(finalobject));

                    TradesManBean tradesManBean = new TradesManBean();
                    tradesManBean.setId(finalobject.getString("id"));
                    tradesManBean.setBusiness_name(finalobject.getString("business_name"));
                    tradesManBean.setBzy_status(finalobject.getString("bzy_status"));
                    tradesManBean.setTrading_name(finalobject.getString("trading_name"));
                    tradesManBean.setOffice_no(finalobject.getString("office_no"));
                    tradesManBean.setMobile_no(finalobject.getString("mobile_no"));
                    tradesManBean.setEmail(finalobject.getString("email"));
                    tradesManBean.setWebsite_url(finalobject.getString("website_url"));
                    tradesManBean.setLatitude(finalobject.getString("latitude"));
                    tradesManBean.setLongitude(finalobject.getString("longitude"));
                    tradesManBean.setAfter_hours(finalobject.getString("after_hours"));
                    tradesManBean.setSelect_trade(finalobject.getString("select_trade"));
                    tradesManBean.setLicense_pic(finalobject.getString("license_pic"));
                    tradesManBean.setUser_id(finalobject.getString("user_id"));
                    tradesManBean.setWork(finalobject.getString("work"));
                    tradesManBean.setProfile_pic(finalobject.getString("profile_pic"));
                    tradesManBean.setReCommendent(finalobject.getString("recommend"));
                    tradesManBean.setBusiness_address(finalobject.getString("businessAddress"));

                    tradesManBeanList.add(tradesManBean);

                    result = finalobject.getString("result");
                    Log.e("Tradment_SIZE", ">>>>>" + tradesManBeanList.size());

                }

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
            } else if (result.equalsIgnoreCase("successfully")) {
                tv_selecttedArea.setText(tradesManBeanList.size() + " Result for this Trade in your Area");
                HorizontalLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                PagerContainerST1.setLayoutManager(HorizontalLayout);
                adapter = new MyPagerAdapter((ArrayList<TradesManBean>) tradesManBeanList, status);
                PagerContainerST1.setAdapter(adapter);
            } else if (result.equalsIgnoreCase("no data found")) {
                Toast.makeText(SelectTrademan.this, " no data found ", Toast.LENGTH_SHORT).show();
            } else {

                // select a tradesman //

                AlertDialog.Builder builder1 = new AlertDialog.Builder(SelectTrademan.this);
                builder1.setMessage("We couldn't find any trades for your location, " +
                        "we are recruiting more and more trades every week, " +
                        "please try again later.\nThankyou");

                builder1.setCancelable(false);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        }

    }

    private class MyPagerAdapter extends RecyclerView.Adapter<MyPagerAdapter.MyViewHolder> {

        private final String status;
        private final ArrayList<TradesManBean> tradesManBeanList;
        private View itemView;

        public MyPagerAdapter(ArrayList<TradesManBean> tradesManBeanList, String status) {
            this.tradesManBeanList = tradesManBeanList;
            this.status = status;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cover, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.user_name.setText(tradesManBeanList.get(position).getBusiness_name());

            holder.recommneded_by_user.setText("Reccomended by \n" + tradesManBeanList.get(position).getReCommendent() + " user");

            Log.e("IMAGE_URL------------->", "" + tradesManBeanList.get(position).getProfile_pic());
            Log.e("IMAGE_URL------------->", "" + tradesManBeanList.get(position).getProfile_pic());
            Log.e("STATUS------------->", "" + tradesManBeanList.get(position).getBzy_status());

            Glide.with(SelectTrademan.this).load(tradesManBeanList.get(position).getProfile_pic())
                    .thumbnail(0.5f).into(holder.CoverImage);

            if (tradesManBeanList.get(position).getBzy_status().equalsIgnoreCase("1")) {
                holder.CancelRL.setVisibility(View.VISIBLE);
            } else {
                holder.CancelRL.setVisibility(View.GONE);
            }

            holder.linear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (tradesManBeanList.get(position).getBzy_status().equalsIgnoreCase("1")) {
                        Toast.makeText(SelectTrademan.this, "This Tradesman is currently on hold.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (status.equalsIgnoreCase("login")) {
                            Log.e("dsfasfasas","JsonObjectList = " + JsonObjectList.get(position));
                            Intent intent = new Intent(SelectTrademan.this, BookTradesman.class);
                            intent.putExtra("Data", JsonObjectList.get(position));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SelectTrademan.this, SignupActivity.class);
                            intent.putExtra("status", "signuptradmenn");
                            intent.putExtra("status1", status);
                            intent.putExtra("select_status", select_status);
                            startActivity(intent);
                        }

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return tradesManBeanList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout linear1;
            ImageView CoverImage;
            TextView user_name;
            TextView recommneded_by_user;
            RelativeLayout CancelRL;

            public MyViewHolder(View itemView) {

                super(itemView);

                CoverImage = itemView.findViewById(R.id.CoverImage);
                user_name = itemView.findViewById(R.id.user_name);
                recommneded_by_user = itemView.findViewById(R.id.recommneded_by_user);
                CancelRL = itemView.findViewById(R.id.CancelRL);
                linear1 = itemView.findViewById(R.id.linear1);

            }
        }

    }

}