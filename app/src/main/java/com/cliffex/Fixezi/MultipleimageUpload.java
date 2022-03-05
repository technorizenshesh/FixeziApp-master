package com.cliffex.Fixezi;

import android.app.Activity;
import android.app.Dialog;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MultipleimageUpload extends AppCompatActivity {
    private static final int camera = 1, gallery = 2;
    HorizontalRecyclerView customImageAdapter;
    ArrayList<GetSet> getSets;
    RecyclerView listView;
    int position;
    String imageTempName;
    String[] imageFor;
    private String fileUriPath, ImageUpload;
    private Uri fileUri, selectedImage;

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(), "/fixezi");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipleimage_upload);
        listView = findViewById(R.id.captureList);
        getSets = new ArrayList<GetSet>();
        imageFor = getResources().getStringArray(R.array.imageFor);

        for (int i = 0; i < 3; i++) {
            GetSet inflate = new GetSet();
            inflate.setUid(String.valueOf(i));
            inflate.setLabel("Image");
            inflate.setHaveImage(false);
            inflate.setSubtext(imageFor[i]);
            inflate.setStatus(true);

            getSets.add(inflate);
        }

        //customImageAdapter = new HorizontalRecyclerView(getSets, MultipleimageUpload.this);
        listView.setAdapter(customImageAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    }

    public void captureImage(int pos, String imageName) {
        position = pos;
        imageTempName = imageName;
        gotoAddProfileImage(camera, gallery);
    }

    private void gotoAddProfileImage(final int camera, final int gallery) {

        final Dialog dialog = new Dialog(MultipleimageUpload.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choosemedia);
        dialog.setTitle("Upload Image");

        ImageView CameraImage = dialog.findViewById(R.id.CameraImage);
        ImageView GalleryImage = dialog.findViewById(R.id.GalleryImage);

        CameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, camera);
                dialog.dismiss();
            }
        });

        GalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), gallery);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public Uri getOutputMediaFileUri(int type) {
        fileUriPath = getOutputMediaFile(type).getPath();
        Uri uri = FileProvider.getUriForFile(MultipleimageUpload.this, getApplicationContext().getPackageName() + ".provider", new File(fileUriPath));
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_CANCELED) {

            switch (requestCode) {

                case gallery:

                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String FinalPath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e("PATH", "" + FinalPath);
                    Intent intent = new Intent(MultipleimageUpload.this, CropActivity.class);
                    intent.putExtra("ImagePath", FinalPath);
                    startActivityForResult(intent, 1000);
                    Toast.makeText(this, "Select image 1", Toast.LENGTH_SHORT).show();

                    break;

                case camera:
                    Intent intent2 = new Intent(MultipleimageUpload.this, CropActivity.class);
                    intent2.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent2, 1000);
                    break;

                case 1000:
                    ImageUpload = data.getStringExtra("CroppedImage");
                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    Uri uri21 = FileProvider.getUriForFile(MultipleimageUpload.this,
                            getApplicationContext().getPackageName() + ".provider", new File(ImageUpload));
                    Log.e("22NEWPATHH", "???" + ImageUpload);

                    File imgFile5 = new File(ImageUpload);
                    Log.e("imagefile2", "" + imgFile5);
                    Bitmap bm21 = null;

                    try {
                        bm21 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri21);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao21 = new ByteArrayOutputStream();
                    bm21.compress(Bitmap.CompressFormat.JPEG, 90, bao21);
                    byte[] ba21 = bao21.toByteArray();
                    Appconstants.IMAGE_BASE642 = Base64Decode.encodeBytes(ba21);
                    Log.e("bitmap gallery", Appconstants.IMAGE_BASE642);
                  //  customImageAdapter.setImageInItem(position, bm21, ImageUpload);
                    break;
            }
        }
    }
}