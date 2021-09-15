package com.cliffex.Fixezi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoveAccount extends AppCompatActivity {

    private TextView ThankYouTV;
    private TextView toolbar_title;
    private SessionUser sessionUser;
    private String id_user;
    ImageView image_nav;
    private JSONObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_account);

        sessionUser = new SessionUser(this);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        ThankYouTV = (TextView) findViewById(R.id.ThankYouTV);
        image_nav =  findViewById(R.id.image_nav);

        SetuUi();
    }

    private void SetuUi() {

        image_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title.setText("Remove Account ");

        ThankYouTV = (TextView) findViewById(R.id.ThankYouTV);
        ThankYouTV.setText(Html.fromHtml("<font color='#00A2E8'>Please click here</font> " + "<br></br>" + " to remove your account "));

        ThankYouTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(RemoveAccount.this);
                builder1.setMessage("Are you sure");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RemoveAccountApi();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

    }

    private void RemoveAccountApi() {

        id_user = sessionUser.getId();
        AndroidNetworking.get("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?" + "delete_account&table=" + "tradesman_user&table_id" + id_user)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String result = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}
