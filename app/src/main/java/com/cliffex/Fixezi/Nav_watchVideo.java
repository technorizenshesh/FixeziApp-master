package com.cliffex.Fixezi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import hb.xvideoplayer.MxVideoPlayer;

public class Nav_watchVideo extends AppCompatActivity {

    private ImageView img_back;
    private VideoView  videoPlayerWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_watch_video);
        img_back= (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        videoPlayerWidget = (VideoView ) findViewById(R.id.mpw_video_player);
        //videoPlayerWidget.startPlay("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
      //  videoPlayerWidget.autoStartPlay("http://fixezi.com.au/FIXEZI/Final-Video-r4-fixezi%20(1)%20(1).mp4", MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");

        videoPlayerWidget.setVideoPath("http://fixezi.com.au/FIXEZI/Final-Video-r4-fixezi%20(1)%20(1).mp4");
        videoPlayerWidget.start();
    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();
       // videoPlayerWidget.autoStartPlay("http://fixezi.com.au/FIXEZI/Final-Video-r4-fixezi%20(1)%20(1).mp4", MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        videoPlayerWidget.setVideoPath("http://fixezi.com.au/FIXEZI/Final-Video-r4-fixezi%20(1)%20(1).mp4");
    }

    @Override
    public void onPause() {

        try {

            Log.e("DEBUG", "OnPause of loginFragment");
            super.onPause();
            MxVideoPlayer.releaseAllVideos();

        }catch (Exception e){

            e.printStackTrace();

        }

    }
}
