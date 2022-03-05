package com.cliffex.Fixezi;

import android.app.Activity;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CropActivity extends AppCompatActivity {

    CropImageView newCropImageView;
    LinearLayout CropCancelLL, CropDoneLL, RotateDoneLL2, RatioToggleLL2;

    Toolbar toolbar;
    Uri fileUri;
    int degree = 0;
    int ratio = 0;
    String ratioStr = "43";
    int returnCode = 0;
    String isEdit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        newCropImageView = (CropImageView) findViewById(R.id.newCropImageView);

        CropDoneLL = (LinearLayout) findViewById(R.id.CropDoneLL2);
        CropCancelLL = (LinearLayout) findViewById(R.id.CropCancelLL2);
        RotateDoneLL2 = (LinearLayout) findViewById(R.id.RotateDoneLL2);
        RatioToggleLL2 = (LinearLayout) findViewById(R.id.RatioToggleLL2);

        newCropImageView.setAspectRatio(4, 3);
        newCropImageView.setAutoZoomEnabled(true);

        Bundle extra = getIntent().getExtras();
        String ImagePath = getIntent().getStringExtra("ImagePath");
        isEdit = getIntent().getStringExtra("isEdit");
        returnCode = getIntent().getIntExtra("code", 0);

        Log.e("image_path_crop", "" + ImagePath);

        newCropImageView.setImageUriAsync(Uri.fromFile(new File(ImagePath)));
        CropCancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RotateDoneLL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree = degree + 90;
                newCropImageView.rotateImage(degree);
            }
        });

        RatioToggleLL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratio = ratio + 1;

                if (ratio == 0) {
                    newCropImageView.setAspectRatio(4, 3);
                    ratioStr = "43";
                } else if (ratio == 1) {
                    newCropImageView.setAspectRatio(16, 9);
                    ratioStr = "169";
                } else if (ratio == 2) {
                    newCropImageView.setAspectRatio(4, 4);
                    ratioStr = "44";
                    ratio = -1;
                }
            }
        });

        CropDoneLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileUri = getOutputMediaFileUri(1);

                Bitmap cropped = newCropImageView.getCroppedImage();

                File f = new File(fileUri.getPath());
                Bitmap bitmap = cropped;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("CroppedImage", fileUri.getPath());
                returnIntent.putExtra("Ratio", ratioStr);
                if (isEdit == null || isEdit.equals("")) {
                    setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    setResult(returnCode, returnIntent);
                }

                Log.e("CropedImage", "CropedImage = " + fileUri.getPath());

                finish();

            }
        });
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/fixezi");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Hello Camera", "Oops! Failed create Hello Camera directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }


}
