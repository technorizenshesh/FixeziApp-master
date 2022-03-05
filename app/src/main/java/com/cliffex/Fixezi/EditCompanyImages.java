package com.cliffex.Fixezi;

import static android.os.Build.VERSION.SDK_INT;

import com.cliffex.Fixezi.MyUtils.InternetDetect;
import static com.cliffex.Fixezi.R.drawable.border_tourtoise_solide;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.util.Compress;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditCompanyImages extends AppCompatActivity {

    private static final int GALLERY = 101;
    private static final int CAMERA = 102;
    private static final int CROP = 103;
    private static final int logoImageCode = 123;
    private static final int normalImageCode = 456;
    private static final int PERMISSION_ID = 1234;
    private static int cameraOrGallery = 0;

    File logoImage, normalImage;
    Toolbar toolbar;
    Context mContext = EditCompanyImages.this;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    ImageView CompanyProfileIM, ComapanyUploadIm;
    RelativeLayout.LayoutParams layoutParams43;
    RelativeLayout.LayoutParams layoutParams169;
    RelativeLayout.LayoutParams layoutParams44;
    Button BTUploadCompanyDetail;
    EditText ETCompanyDetail;
    Uri fileUri;
    private static final int camera = 1;
    private static final int gallery = 2;
    private static final int camera2 = 3;
    private static final int gallery2 = 4;
    String PhotoOrString = "";
    String CompanyUpload = "";
    ImageView ClearImageIM, ClearProfileImageIMTwo;
    RelativeLayout RLCompanyUpload;
    TextView SaveChangeTV;
    CheckBox EditImageCB;
    ProgressDialog progressDialog;
    String fileUriPath;
    private String str_image_path;
    private int imageCapturedCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.Please Wait ......");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_images);

        Appconstants.companyDesRatio = "";
        Appconstants.companyProfileRatio = "";
        Appconstants.IMAGE_BASE64 = "";
        Appconstants.IMAGE_BASE642 = "";

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.ToolbarECIm);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar_textview.setText("Edit Company Images");
        setSupportActionBar(toolbar);

        ComapanyUploadIm = (ImageView) findViewById(R.id.ComapanyUploadIm);
        CompanyProfileIM = (ImageView) findViewById(R.id.CompanyProfileIM);
        BTUploadCompanyDetail = (Button) findViewById(R.id.BTUploadCompanyDetailTwo);
        ETCompanyDetail = (EditText) findViewById(R.id.ETCompanyDetailTwo);
        ClearImageIM = (ImageView) findViewById(R.id.ClearImageIMTwo);
        ClearProfileImageIMTwo = (ImageView) findViewById(R.id.ClearProfileImageIMTwo);
        RLCompanyUpload = (RelativeLayout) findViewById(R.id.RLCompanyUpload);
        SaveChangeTV = (TextView) findViewById(R.id.SaveChangeTV);
        EditImageCB = (CheckBox) findViewById(R.id.EditImageCB);

        if (InternetDetect.isConnected(this)) {
            new JsonGetInfo().execute();
        } else {
            Toast.makeText(EditCompanyImages.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        int MyHeight43 = ((width * 3) / 4);
        int MyHeight169 = ((width * 9) / 16);
        int MyHeight44 = width;


        layoutParams43 = new RelativeLayout.LayoutParams(width, MyHeight43);
        layoutParams169 = new RelativeLayout.LayoutParams(width, MyHeight169);
        layoutParams44 = new RelativeLayout.LayoutParams(width, MyHeight44);
        //layoutParams.gravity = Gravity.CENTER;


        BTUploadCompanyDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PhotoOrString.equalsIgnoreCase("String")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditCompanyImages.this);
                    builder1.setTitle("fixezi Team");
                    builder1.setMessage("You can either upload image of your company detail or enter text of company detail.");
                    builder1.setIcon(R.drawable.mainlogo);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    if (checkPermissions()) {
                        imageCapturedCode = 2;
                        showPictureDialog();
                        // gotoAddProfileImage(camera2, gallery2);
                    } else {
                        requestPermissions();
                    }

                }
            }
        });

        ETCompanyDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PhotoOrString.equalsIgnoreCase("Photo")) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditCompanyImages.this);
                    builder1.setTitle("fixezi Team");
                    builder1.setMessage("You can either upload image of your company detail or enter text of company detail.");
                    builder1.setIcon(R.drawable.mainlogo);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {

                }
            }
        });

        ETCompanyDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ETCompanyDetail.getText().toString().equalsIgnoreCase("")) {

                    PhotoOrString = "";

                } else {

                    PhotoOrString = "String";
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ClearImageIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RLCompanyUpload.setVisibility(View.GONE);
                BTUploadCompanyDetail.setVisibility(View.VISIBLE);
                BTUploadCompanyDetail.setBackgroundResource(R.drawable.border_black_solid_white);
                BTUploadCompanyDetail.setText("Click here to upload company details and service you provide");
                BTUploadCompanyDetail.setTextColor(Color.parseColor("#000000"));
                PhotoOrString = "";
                ETCompanyDetail.setFocusableInTouchMode(true);
                ETCompanyDetail.setFocusable(true);
            }
        });

        ClearProfileImageIMTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    imageCapturedCode = 1;
                    showPictureDialog();
                    // gotoAddProfileImage(camera, gallery);
                } else {
                    requestPermissions();
                }
            }
        });

        ClearProfileImageIMTwo.setEnabled(false);
        ClearImageIM.setEnabled(false);
        ETCompanyDetail.setEnabled(false);

        EditImageCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ClearProfileImageIMTwo.setEnabled(true);
                    ETCompanyDetail.setEnabled(true);
                    ClearImageIM.setEnabled(true);
                    SaveChangeTV.setEnabled(true);
                } else {
                    ClearProfileImageIMTwo.setEnabled(false);
                    ETCompanyDetail.setEnabled(false);
                    ClearImageIM.setEnabled(false);
                    SaveChangeTV.setEnabled(false);
                }
            }
        });

        SaveChangeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditImageCB.isChecked()) {
                    if (PhotoOrString.equalsIgnoreCase("")) {
                        Toast.makeText(EditCompanyImages.this, "Please Enter Company Detail or Upload Company Detail Photo", Toast.LENGTH_SHORT).show();
                    } else {
                        updateProfileApi();
                        // new JsonUpdate().execute(ETCompanyDetail.getText().toString());
                    }
                } else {
                    Toast.makeText(EditCompanyImages.this, "Please check the box to edit", Toast.LENGTH_SHORT).show();
                }
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

    private void updateProfileApi() {

        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", sessionTradesman.getId());
        paramHash.put("company_profile_ratio", "");
        paramHash.put("company_upload_ratio", "");
        paramHash.put("company_detail", ETCompanyDetail.getText().toString().trim());

        HashMap<String, File> paramFileHash = new HashMap<>();

        if (logoImage != null) {
            paramFileHash.put("profile_pic", logoImage);
        }

        if (normalImage != null) {
            paramFileHash.put("company_detail_upload", normalImage);
        }

        Log.e("responseresponse", "paramHash = " + paramHash);
        Log.e("responseresponse", "paramFileHash = " + paramFileHash);

        ProjectUtil.showProgressDialog(mContext, false, "Please wait...");
        AndroidNetworking.upload(HttpPAth.Urlpath + "profile_pic_company_detail_Upadte")
                .addMultipartParameter(paramHash)
                .addMultipartFile(paramFileHash)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        finish();
                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                        Log.e("responseresponse", "response = " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }

                });
    }

    private class JsonUpdate extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected TradesManBean doInBackground(String... strings) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "profile_pic_company_detail_Upadte&");
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_id", sessionTradesman.getId()));

                if (Appconstants.IMAGE_BASE64.equalsIgnoreCase("")) {
                } else {
                    Log.e("WEBCheck1", ">>>>>" + Appconstants.IMAGE_BASE64);
                    nameValuePairs.add(new BasicNameValuePair("profile_pic", Appconstants.IMAGE_BASE64));
                    nameValuePairs.add(new BasicNameValuePair("company_profile_ratio", Appconstants.companyProfileRatio));
                }

                if (Appconstants.IMAGE_BASE642.equalsIgnoreCase("")) {
                    nameValuePairs.add(new BasicNameValuePair("company_detail", strings[0]));
                } else {
                    Log.e("WEBCheck2", ">>>>>" + Appconstants.IMAGE_BASE642);
                    nameValuePairs.add(new BasicNameValuePair("company_detail_upload", Appconstants.IMAGE_BASE642));
                    nameValuePairs.add(new BasicNameValuePair("company_upload_ratio", Appconstants.companyDesRatio));
                    nameValuePairs.add(new BasicNameValuePair("company_detail", strings[0]));
                }

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                Log.e("JsonUpdate", object);

                Gson gson = new Gson();

                JSONArray ParentArray = new JSONArray(object);

                JSONObject Object = ParentArray.getJSONObject(0);

                TradesManBean tradesManBean = gson.fromJson(Object.toString(), TradesManBean.class);

                jsonresult = Object.getString("result");

                return tradesManBean;

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TradesManBean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (jsonresult.equalsIgnoreCase("successfully")) {
                Toast.makeText(EditCompanyImages.this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private void setImageFromCameraGallery(String path) {
        if (imageCapturedCode == 1) {
            CompanyProfileIM.setImageURI(Uri.parse(path));
            logoImage = new File(path);
            Log.e("asdasdas", "logoImage Image = " + logoImage.getAbsolutePath());
        } else if (imageCapturedCode == 2) {

            normalImage = new File(path);
            Log.e("asdasdas", "Normal Image = " + normalImage.getAbsolutePath());
            RLCompanyUpload.setVisibility(View.VISIBLE);
            BTUploadCompanyDetail.setVisibility(View.GONE);

            ComapanyUploadIm.setImageURI(Uri.parse(path));
            // CompanyProfileIM.setImageURI(uri);
            BTUploadCompanyDetail.setBackgroundResource(R.drawable.border_black_solid_red);
            BTUploadCompanyDetail.setTextColor(Color.parseColor("#ffffff"));
            BTUploadCompanyDetail.setText("Image Uploaded");
            PhotoOrString = "Photo";
            ETCompanyDetail.setFocusable(false);

//            Compress.get(mContext).setQuality(90).execute(new Compress.onSuccessListener() {
//                @Override
//                public void response(boolean status, String message, File file) {
//
//                }
//            }).CompressedImage(file.getPath());

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("zsfasdasda", "requestCode = " + requestCode);
        Log.e("zsfasdasda", "resultCode = " + resultCode);
        Log.e("zsfasdasda", "resultCode = " + resultCode);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String path = RealPathUtil.getRealPath(mContext, resultUri);
                Log.e("hjagksads", "path = " + path);
                setImageFromCameraGallery(path);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        Log.e("hjagksads", "GALLERY URI = " + data.getData());
                        // String path = RealPathUtil.getRealPath(mContext, resultUri);
                        CropImage.activity(data.getData()).start(this);
                    } catch (Exception e) {
                        Log.e("hjagksads", "image = " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                //                String path = ProjectUtil.getRealPathFromURI(mContext, data.getData());
//
//                Intent intent = new Intent(mContext, CropActivity.class);
//                intent.putExtra("ImagePath", path);
//                intent.putExtra("isEdit", "isEdit");
//
//                if (imageCapturedCode == 1) {
//                    intent.putExtra("code", logoImageCode);
//                    startActivityForResult(intent, logoImageCode);
//                } else {
//                    intent.putExtra("code", normalImageCode);
//                    startActivityForResult(intent, normalImageCode);
//                }

            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bitmapNew = (Bitmap) extras.get("data");
                Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());

                Uri tempUri = getImageUri(mContext, imageBitmap);

                CropImage.activity(tempUri).start(this);

//              Intent intent = new Intent(mContext, CropActivity.class);
//              intent.putExtra("ImagePath", str_image_path);
//              intent.putExtra("isEdit", "isEdit");
//              if (imageCapturedCode == 1) {
//                  intent.putExtra("code", logoImageCode);
//                  startActivityForResult(intent, logoImageCode);
//              } else {
//                  intent.putExtra("code", normalImageCode);
//                  startActivityForResult(intent, normalImageCode);
//              }

            }
        } else if (resultCode == logoImageCode) {
            String path = data.getStringExtra("CroppedImage");
            Log.e("zsfasdasda", "Path = " + path);
            // setImageFromCameraGallery(new File(path));
        } else if (resultCode == normalImageCode) {
            String path = data.getStringExtra("CroppedImage");
            Log.e("zsfasdasda", "Path = " + path);
            // setImageFromCameraGallery(new File(path));
        }

    }

    public Bitmap BITMAP_RE_SIZER(Bitmap bitmap, int newWidth, int newHeight) {

        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    public static void requestPermissions(Context mContext) {
        ActivityCompat.requestPermissions(
                ((Activity) mContext), new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 101);
    }

    public void showPictureDialog() {
        androidx.appcompat.app.AlertDialog.Builder pictureDialog = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // cameraOrGallery = CAMERA;
                                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                startActivityForResult(galleryIntent, GALLERY);
                                dialog.dismiss();
                                // ProjectUtil.openGallery(mContext, GALLERY);
                                break;
                            case 1:
                                // cameraOrGallery = GALLERY;
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA);
                                dialog.dismiss();
                                // str_image_path = ProjectUtil.openCamera(mContext, CAMERA);
                                Toast.makeText(mContext, "str_image_path = " + str_image_path, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getOutputMediaFileUri(int type) {
        fileUriPath = getOutputMediaFile(type).getPath();
        Uri uri = FileProvider.getUriForFile(EditCompanyImages.this, getApplicationContext().getPackageName() + ".provider", new File(fileUriPath));
        return uri;
    }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private class JsonGetInfo extends AsyncTask<String, String, TradesManBean> {

        String jsonresult = "he";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TradesManBean doInBackground(String... paramss) {
            try {
                URL url = new URL(HttpPAth.Urlpath + "get_tradesman_profile&");
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("tradesman_id", sessionTradesman.getId());

                StringBuilder postData = new StringBuilder();

                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                writer.close();
                reader.close();
                Log.e("JsonGetInfo", response);

                Gson gson = new Gson();
                JSONArray ParentArray = new JSONArray(response);

                JSONObject Object = ParentArray.getJSONObject(0);

                TradesManBean tradesManBean = gson.fromJson(Object.toString(), TradesManBean.class);

                jsonresult = Object.getString("result");

                return tradesManBean;

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TradesManBean result) {
            super.onPostExecute(result);

            if (jsonresult.equalsIgnoreCase("successfully")) {

                if (result.getCompany_profile_ratio().equalsIgnoreCase("43")) {
                    CompanyProfileIM.setLayoutParams(layoutParams43);
                } else if (result.getCompany_profile_ratio().equalsIgnoreCase("169")) {
                    CompanyProfileIM.setLayoutParams(layoutParams169);
                } else if (result.getCompany_profile_ratio().equalsIgnoreCase("44")) {
                    CompanyProfileIM.setLayoutParams(layoutParams44);
                }

                Glide.with(EditCompanyImages.this).load(result.getProfile_pic())
                        .thumbnail(0.5f)
                        .into(CompanyProfileIM);
//
//                        Glide.with(EditCompanyImages.this).load(result.getCompany_detail_upload())
//                        .thumbnail(0.5f)
//                        .into(CompanyProfileIM);

                if (result.getCompany_detail().trim().equalsIgnoreCase("")) {

                    PhotoOrString = "Photo";
                    RLCompanyUpload.setVisibility(View.VISIBLE);
                    BTUploadCompanyDetail.setVisibility(View.GONE);

                    Glide.with(EditCompanyImages.this).load(result.getCompany_detail_upload())
                            .thumbnail(0.5f)
                            .into(ComapanyUploadIm);
                } else {

                    PhotoOrString = "String";
                    RLCompanyUpload.setVisibility(View.GONE);
                    BTUploadCompanyDetail.setVisibility(View.VISIBLE);

                    ETCompanyDetail.setText(result.getCompany_detail().trim());

                }
            }
        }
    }

}