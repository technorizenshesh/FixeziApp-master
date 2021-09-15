package com.cliffex.Fixezi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cliffex.Fixezi.MyUtils.HttpPAth;

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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by technorizen8 on 13/4/16.
 */
public class RatingSystem extends AppCompatActivity {
    RelativeLayout backratesystem, doneratesystem;
    Toolbar rating_system_toolbar;
    TextView toolbar_title, backonratingsytem, tvDoneratingsytem, tv1, tv2, tv3;
    RatingBar ratinPuctual, ratinWorkmanship, ratinafordability;
    SessionUser sessionUser;
    String TradesmanID = "", ProblemId = "";
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rating_system_screen);

        sessionUser = new SessionUser(this);
        rating_system_toolbar = (Toolbar) findViewById(R.id.rating_system_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        backonratingsytem = (TextView) findViewById(R.id.backonratingsytem);
        tvDoneratingsytem = (TextView) findViewById(R.id.doneonratingsytem);
        ratinPuctual = (RatingBar) findViewById(R.id.ratin1);
        ratinWorkmanship = (RatingBar) findViewById(R.id.ratin2);
        ratinafordability = (RatingBar) findViewById(R.id.ratin3);
        tv1 = (TextView) findViewById(R.id.tv1_ratin11);
        tv2 = (TextView) findViewById(R.id.tv2_ratin22);
        tv3 = (TextView) findViewById(R.id.tv3_ratin33);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        NavigationUpIM.setVisibility(View.GONE);

        toolbar_title.setText("Rate your Tradesman");
        setSupportActionBar(rating_system_toolbar);
        backratesystem = (RelativeLayout) findViewById(R.id.backratesystem);
        doneratesystem = (RelativeLayout) findViewById(R.id.doneratesystem);
        backratesystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        TradesmanID = extra.getString("TradesmanId");
        ProblemId = extra.getString("ProblemId");

        doneratesystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String puctual = String.valueOf(ratinPuctual.getRating());
                String workmanship = String.valueOf(ratinWorkmanship.getRating());
                String forda = String.valueOf(ratinafordability.getRating());

                Log.e("ALL VALUE", puctual + "," + workmanship + "," + forda);

                if (!(puctual.equalsIgnoreCase("0.0")) && !(workmanship.equalsIgnoreCase("0.0")) && !(forda.equalsIgnoreCase("0.0"))) {

                  new JsonRate().execute(puctual, workmanship, forda);

                } else {

                    Toast.makeText(RatingSystem.this, "Please rate first", Toast.LENGTH_SHORT);

                }


            }
        });
        ratinPuctual.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Log.e("Rating1", String.valueOf(rating));

            }
        });
        ratinWorkmanship.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Log.e("Rating2", String.valueOf(rating));

            }
        });
        ratinafordability.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Log.e("Rating3", String.valueOf(rating));
            }
        });
    }




    private class JsonRate extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("In Pre", "yes");
        }

        @Override
        protected String doInBackground(String... paramss) {
            Log.e("In back", "yes");

            try {
                URL url = new URL(HttpPAth.Urlpath + "Rating_by_user&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("user_id", sessionUser.getId());
                params.put("tradesman_id", TradesmanID);
                params.put("problem_id", ProblemId);
                params.put("punctual", paramss[0]);
                params.put("workmanship", paramss[1]);
                params.put("affordability", paramss[2]);


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
                Log.e("JsonPendingRequest", response);

                JSONObject object = new JSONObject(response);
                String result = object.getString("mes");


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

            } else {

                if (result.equalsIgnoreCase("Success")) {

                    Toast.makeText(RatingSystem.this, "Done", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        }
    }
}
