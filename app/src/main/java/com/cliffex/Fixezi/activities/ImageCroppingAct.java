package com.cliffex.Fixezi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.cliffex.Fixezi.R;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class ImageCroppingAct extends AppCompatActivity {

    Context mContext = ImageCroppingAct.this;
    CropImageView mCropView;
    private LoadCallback mLoadCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropping);
        itit();
    }

    private void itit() {



    }

}