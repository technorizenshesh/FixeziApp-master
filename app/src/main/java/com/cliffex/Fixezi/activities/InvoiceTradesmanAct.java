package com.cliffex.Fixezi.activities;

import static com.cliffex.Fixezi.R.drawable.border_tourtoise_solide;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.JobDetailsAddQuote;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.adapter.AdapterImage;
import com.cliffex.Fixezi.util.Compress;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cliffex.Fixezi.R;
import com.cliffex.Fixezi.util.ProjectUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

public class InvoiceTradesmanAct extends AppCompatActivity {

    private static final int PERMISSION_ID = 101;
    Context mContext = InvoiceTradesmanAct.this;
    ImageView image_nav;
    TextView tvTittleInvoice, tvJobId, btJobMarked;
    TextView toolbar_title;
    ImageView addPhoto;
    EditText tvNotes;
    RecyclerView rvPhotos;
    int CAMERA = 0, GALLERY = 1;
    String problemId = "";
    ArrayList<File> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_tradesman);
        problemId = getIntent().getStringExtra("problemId");
        itit();
    }

    private void itit() {

        tvTittleInvoice = findViewById(R.id.tvTittleInvoice);
        toolbar_title = findViewById(R.id.toolbar_title);
        image_nav = findViewById(R.id.image_nav);
        addPhoto = findViewById(R.id.addPhoto);
        rvPhotos = findViewById(R.id.rvPhotos);
        tvJobId = findViewById(R.id.tvJobId);
        tvNotes = findViewById(R.id.tvNotes);
        btJobMarked = findViewById(R.id.btJobMarked);

        tvJobId.setText("Job Id " + problemId);

        toolbar_title.setText("Invoice/Receipts");

        String text = "<font color='#49A2F0'>Take a photo of </font> your invoice/receipt";
        tvTittleInvoice.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        image_nav.setOnClickListener(v -> {
            finish();
        });

        addPhoto.setOnClickListener(v -> {
            if (checkPermissions()) {
                gotoAddProfileImage();
            } else {
                requestPermissions();
            }
        });

        btJobMarked.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tvNotes.getText().toString().trim())) {
                Toast.makeText(mContext, "Please add a Note", Toast.LENGTH_SHORT).show();
            } else if (fileList == null && fileList.size() == 0) {
                Toast.makeText(mContext, "Please Take a photo of your invoice", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(mContext, JobDetailsAddQuote.class);
                intent.putExtra("problemId", problemId);

                startActivity(new Intent(mContext, JobDetailsAddQuote.class)
                        .putExtra("problemId", problemId)
                        .putExtra("filearray", fileList)
                        .putExtra("notes", tvNotes.getText().toString().trim())

                );
            }

        });

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_ID
        );
    }

    private void gotoAddProfileImage() {

        final Dialog dialog = new Dialog(InvoiceTradesmanAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choosemedia);
        dialog.setTitle("Upload Image");
        ImageView CameraImage = dialog.findViewById(R.id.CameraImage);
        ImageView GalleryImage = dialog.findViewById(R.id.GalleryImage);

        CameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
                dialog.dismiss();
            }
        });

        GalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                String path = getRealPathFromURI(getImageUri(mContext, thumbnail));

                Compress.get(mContext).setQuality(90)
                        .execute(new Compress.onSuccessListener() {
                            @Override
                            public void response(boolean status, String message, File file) {
                                fileList.add(file);
                                AdapterImage adapterImage = new AdapterImage(mContext, fileList, InvoiceTradesmanAct.this::getUpdatedList);
                                rvPhotos.setAdapter(adapterImage);
                            }
                        }).CompressedImage(path);
            } else if (requestCode == GALLERY) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        String path = getRealPathFromURI(contentURI);
                        Compress.get(mContext).setQuality(90).execute(new Compress.onSuccessListener() {
                            @Override
                            public void response(boolean status, String message, File file) {
                                fileList.add(file);
                                AdapterImage adapterImage = new AdapterImage(mContext, fileList, InvoiceTradesmanAct.this::getUpdatedList);
                                rvPhotos.setAdapter(adapterImage);
                            }
                        }).CompressedImage(path);
                    } catch (Exception e) {
                        Log.e("hjagksads", "image = " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getUpdatedList(ArrayList<File> fileListFinal) {
        fileList = fileListFinal;
        Log.e("fileList", "fileList = " + fileList);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}