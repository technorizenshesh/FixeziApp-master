package com.cliffex.Fixezi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cliffex.Fixezi.Other.AppConfig;
import com.cliffex.Fixezi.Response.TradesmanProfile_Response;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cliffex.Fixezi.Other.MySharedPref.getData;

public class RatingActivity extends AppCompatActivity {

    ImageView img_back;
    RatingBar ratinE, ratinworkman, ratinprice;
    float f_efficency, f_workmanship, f_price;
    String TradesmanID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        img_back = (ImageView) findViewById(R.id.img_back);
        ratinE = (RatingBar) findViewById(R.id.ratinE);
        ratinworkman = (RatingBar) findViewById(R.id.ratinworkman);
        ratinprice = (RatingBar) findViewById(R.id.ratinprice);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TradesmanID = getData(getApplicationContext(), "tradmen_id", null);

        System.out.println("TTradesmanID__________" + TradesmanID);
        tradmenprofile(TradesmanID);

    }

    private void tradmenprofile(String tradesman_id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(RatingActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().tradmentrating(tradesman_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        String responedata = response.body().string();
                        JSONArray arr = new JSONArray(responedata);
                        if (arr.length() > 0) {
                            JSONObject object = arr.getJSONObject(0);
                            if (object.getString("result").equals("successfully")) {
                                Gson gson = new Gson();
                                TradesmanProfile_Response successResponse = gson.fromJson(String.valueOf(object), TradesmanProfile_Response.class);
                                f_efficency = Float.parseFloat(successResponse.getRating().getAffordability());
                                f_workmanship = Float.parseFloat(successResponse.getRating().getWorkmanship());
                                f_price = Float.parseFloat(successResponse.getRating().getPunctual());
                                ratinE.setRating(f_efficency);
                                ratinworkman.setRating(f_workmanship);
                                ratinprice.setRating(f_price);
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    //Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(RatingActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
