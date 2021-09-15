package com.cliffex.Fixezi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.MyUtils.TouchImageView;

import java.util.ArrayList;

public class FullScreenActivity extends AppCompatActivity {
    ArrayList<String> imagePaths;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        viewPager = (ViewPager) findViewById(R.id.pager);


        Bundle extra=getIntent().getExtras();
        imagePaths= extra.getStringArrayList("ImageArrayList");
        int position=extra.getInt("position");
        FullScreenImageAdapter adapter=new FullScreenImageAdapter(this,imagePaths);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

    }


    public class FullScreenImageAdapter extends PagerAdapter {

        private Activity _activity;
        private ArrayList<String> _imagePaths;
        private LayoutInflater inflater;

        // constructor
        public FullScreenImageAdapter(Activity activity,
                                      ArrayList<String> imagePaths) {
            this._activity = activity;
            this._imagePaths = imagePaths;
        }

        @Override
        public int getCount() {
            return this._imagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            TouchImageView imgDisplay;
            Button btnClose;

            inflater = (LayoutInflater) _activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                    false);

            imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
            btnClose = (Button) viewLayout.findViewById(R.id.btnClose);


            Glide.with(FullScreenActivity.this)
                    .load(_imagePaths.get(position))
                    .into(imgDisplay);


            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _activity.finish();
                }
            });



            ((ViewPager) container).addView(viewLayout);

            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }



}
