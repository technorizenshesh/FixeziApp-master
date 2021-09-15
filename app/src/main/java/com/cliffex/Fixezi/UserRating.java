package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cliffex.Fixezi.Other.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRating extends AppCompatActivity {

    TextView toolbar_title,qotesaccepted,tv_canceljob;
    RelativeLayout NavigationUpIM;
    RatingBar ratin1,ratin2,ratin3;
    SessionUser sessionUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating);

        progressDialog = new ProgressDialog(this);
        sessionUser = new SessionUser(this);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        qotesaccepted = (TextView) findViewById(R.id.qotesaccepted);
        tv_canceljob = (TextView) findViewById(R.id.tv_canceljob);
        ratin1 = findViewById(R.id.ratin1);
        ratin2 = findViewById(R.id.ratin2);
        ratin3 = findViewById(R.id.ratin3);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        toolbar_title.setText("User rating");

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "DroidSerif.ttf");
        toolbar_title.setTypeface(custom_font);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUserRating();

    }

    private void getUserRating() {

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        HashMap<String,String> param = new HashMap<>();
        param.put("user_id",sessionUser.getId());

        Call<ResponseBody> call = AppConfig.loadInterface().getUserRatingApiCall(param);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        Log.e("sdfsfsdfsd","responseDataRetrofi = " + responseData);

                        // String message=object.getString("message");

                        tv_canceljob.setText(obj.getString("job_cancellations"));
                        qotesaccepted.setText(obj.getString("quotes_accepted_declined"));

                        JSONObject jsonObject = obj.getJSONObject("rating");
                        String easily_contacted = jsonObject.getString("easily_contacted");
                        String ease_of_job = jsonObject.getString("ease_of_job");
                        String payment_on_completion = jsonObject.getString("payment_on_completion");
                        Log.e("easily_contatcted", "" + easily_contacted);
                        ratin3.setRating(Float.parseFloat(payment_on_completion));
                        ratin2.setRating(Float.parseFloat(ease_of_job));
                        ratin1.setRating(Float.parseFloat(easily_contacted));

                    } else ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(UserRating.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
