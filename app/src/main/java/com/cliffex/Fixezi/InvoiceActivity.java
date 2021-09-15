package com.cliffex.Fixezi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by technorizen8 on 17/3/16.
 */
public class InvoiceActivity extends AppCompatActivity {
    Toolbar invoices_toolbar;
    TextView toolbar_title, TakePhotoTV;
    ImageView invoicecam, bluefolderimage;
    RelativeLayout NavigationUpIM;
    Uri fileUri;
    String fileUriPath;
    private static final int CAMERA_REQUEST = 111;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices_activity);

        invoices_toolbar = (Toolbar) findViewById(R.id.invoices_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        TakePhotoTV = (TextView) findViewById(R.id.TakePhotoTV);
        bluefolderimage = (ImageView) findViewById(R.id.bluefolderimage);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        invoicecam = (ImageView) findViewById(R.id.invoicecam);

        toolbar_title.setText("Invoices");

        setSupportActionBar(invoices_toolbar);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        invoicecam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        bluefolderimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), InvoicePage.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(InvoiceActivity.this, "camera cancelled", Toast.LENGTH_SHORT).show();

        }
    }

    public Uri getOutputMediaFileUri(int type) {

        fileUriPath = getOutputMediaFile(type).getPath();
        Uri uri = FileProvider.getUriForFile(InvoiceActivity.this, getApplicationContext().getPackageName() + ".provider", new File(fileUriPath));
        return uri;
    }


    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(), "/fixezi/Invoices");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Hello Camera", "Oops! Failed create Hello Camera directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
