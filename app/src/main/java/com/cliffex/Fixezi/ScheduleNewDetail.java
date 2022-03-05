package com.cliffex.Fixezi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cliffex.Fixezi.MyFragments.ScheduleNewFrag;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class ScheduleNewDetail extends AppCompatActivity {

    ViewPager ViewPagerTwo;
    TabLayout tabLayout;
    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_new_detail);
        sessionTradesman = new SessionTradesman(this);

        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Daily Jobs");
        setSupportActionBar(toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ViewPagerTwo = (ViewPager) findViewById(R.id.ViewPagerTwo);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        Bundle bundle = getIntent().getExtras();
        List<String> stringList = bundle.getStringArrayList("DateList");
        int position = bundle.getInt("Position");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < stringList.size(); i++) {
            Bundle objectId = new Bundle();
            objectId.putString("Date", stringList.get(i));
            ScheduleNewFrag scheduleNewFrag = new ScheduleNewFrag();
            scheduleNewFrag.setArguments(objectId);


            String MyDate = "";
            DateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat df = new SimpleDateFormat("dd MMM, yyyy");
            String OrderDateStr = stringList.get(i);
            try {
                Date OrderDate = sf.parse(OrderDateStr);
                MyDate = df.format(OrderDate);
            } catch (ParseException e) {
                Log.e("PArse Exception", e.getMessage());
                e.printStackTrace();
            }


            adapter.addFragment(scheduleNewFrag, MyDate);
        }
        ViewPagerTwo.setAdapter(adapter);
        ViewPagerTwo.setCurrentItem(position);
        tabLayout.setupWithViewPager(ViewPagerTwo);

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


}
