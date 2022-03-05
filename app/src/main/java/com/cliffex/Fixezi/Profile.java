package com.cliffex.Fixezi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
/**
 * Created by technorizen8 on 2/3/16.
 */

public class Profile extends AppCompatActivity {

    Toolbar profile_toolbar;
    TextView toolbar_title, singggouttt, baccckkk;
    RelativeLayout backbtnlayyy, signoutbtnlay;
    SharedPreferences sharedPreferences;
    RelativeLayout NavigationUpIM;
    TextView InvoicesTV, PreviousHistoryTV, JobsInProgressTV, JobRequestsTV, YourInfoTV,
            WhatdoyouthinksTV, TellaFriendsTV, Yourrating;
    TextView JobCountProfileTV;
    private RelativeLayout fb_link;
    SessionUser sessionUser;
    SessionWorker sessionWorker;
    SessionTradesman sessionTradesman;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        sessionUser = new SessionUser(Profile.this);
        sessionWorker = new SessionWorker(Profile.this);
        sessionTradesman = new SessionTradesman(Profile.this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        YourInfoTV = (TextView) findViewById(R.id.YourInfoTV);
        JobRequestsTV = (TextView) findViewById(R.id.JobRequestsTV);
        JobsInProgressTV = (TextView) findViewById(R.id.JobsInProgressTV);
        PreviousHistoryTV = (TextView) findViewById(R.id.PreviousHistoryTV);
        InvoicesTV = (TextView) findViewById(R.id.InvoicesTV);
        TellaFriendsTV = (TextView) findViewById(R.id.TellaFriendsTV);
        WhatdoyouthinksTV = (TextView) findViewById(R.id.WhatdoyouthinksTV);
        Yourrating = (TextView) findViewById(R.id.Yourrating);
        fb_link = (RelativeLayout) findViewById(R.id.fb_link);
        backbtnlayyy = (RelativeLayout) findViewById(R.id.backbtnlayyy);
        signoutbtnlay = (RelativeLayout) findViewById(R.id.signoutbtnlay);
        profile_toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        singggouttt = (TextView) findViewById(R.id.singggouttt);
        baccckkk = (TextView) findViewById(R.id.baccckkk);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        JobCountProfileTV = (TextView) findViewById(R.id.JobCountProfileTV);

        toolbar_title.setText("Profile page");
        setSupportActionBar(profile_toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fb_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Nav_FB.class);
                startActivity(intent);
            }
        });

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "DroidSerif.ttf");
        toolbar_title.setTypeface(custom_font);
        baccckkk.setTypeface(custom_font);
        singggouttt.setTypeface(custom_font);

        InvoicesTV.setTypeface(custom_font);
        PreviousHistoryTV.setTypeface(custom_font);
        JobsInProgressTV.setTypeface(custom_font);
        JobRequestsTV.setTypeface(custom_font);
        YourInfoTV.setTypeface(custom_font);
        WhatdoyouthinksTV.setTypeface(custom_font);
        TellaFriendsTV.setTypeface(custom_font);

        Bundle bundle = getIntent().getExtras();
        String jobCount = bundle.getString("jobCount");

        if (UserActivity.jobCount == 0) {
            JobCountProfileTV.setVisibility(View.GONE);
        } else {
            JobCountProfileTV.setVisibility(View.VISIBLE);
            JobCountProfileTV.setText(String.valueOf(UserActivity.jobCount));
        }

        JobRequestsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, JobRequestUser.class);
                startActivity(intent);
            }
        });

        YourInfoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Profile_Your_Information.class);
                startActivity(i);
            }
        });

        JobsInProgressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendingJobs.class);
                startActivity(i);
            }
        });

        PreviousHistoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PreviousHistory.class);
                startActivity(i);
            }
        });

        TellaFriendsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TellaFriendAbout.class);
                startActivity(i);
            }
        });

        WhatdoyouthinksTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WhatYouthink.class);
                startActivity(i);
            }
        });

        InvoicesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InvoiceActivity.class);
                startActivity(i);
            }
        });

        Yourrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserRating.class);
                startActivity(i);
            }
        });

        backbtnlayyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signoutbtnlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("status", "null");
                editor.putString("loginname", "null");
                editor.putString("userid", "null");
                editor.commit();
                sessionUser.logoutUser();
                sessionWorker.logoutUser();
                sessionTradesman.logoutUser();
                finishAffinity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserActivity.jobCount == 0) {
            JobCountProfileTV.setVisibility(View.GONE);
        } else {
            JobCountProfileTV.setVisibility(View.VISIBLE);
            JobCountProfileTV.setText(String.valueOf(UserActivity.jobCount));
        }
    }

}

