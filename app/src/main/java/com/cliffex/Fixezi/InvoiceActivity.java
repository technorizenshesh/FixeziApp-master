package com.cliffex.Fixezi;

import static android.os.Environment.getExternalStorageDirectory;

import android.content.Intent;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uri;

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

                if (checkPermisssionForReadStorage()) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(1);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

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
        try {
            fileUriPath = getOutputMediaFile(type).getPath();
            uri = FileProvider.getUriForFile(InvoiceActivity.this,
                    getApplicationContext().getPackageName() + ".provider",
                    new File(fileUriPath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uri;
    }


    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/fixezi/Invoices");
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {
                Log.d("Hello Camera", "Oops! Failed create Hello Camera directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
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


    public boolean checkPermisssionForReadStorage() {

        if (ContextCompat.checkSelfPermission(InvoiceActivity.this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(InvoiceActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(InvoiceActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(InvoiceActivity.this,
                    android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(InvoiceActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(InvoiceActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(InvoiceActivity.this,
                        new String[]{android.Manifest.permission.CAMERA,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {
                ActivityCompat.requestPermissions(InvoiceActivity.this,
                        new String[]{
                                android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }

            return false;

        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (camera1 && read_external_storage && write_external_storage) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(1);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        // gotoAddProfileImage3(camera, gallery);
                    } else {
                        Toast.makeText(InvoiceActivity.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InvoiceActivity.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}
