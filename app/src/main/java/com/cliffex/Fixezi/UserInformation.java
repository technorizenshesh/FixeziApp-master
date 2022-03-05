package com.cliffex.Fixezi;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Model.UserDetail;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class UserInformation extends AppCompatActivity {

    private static final int camera = 1;
    private static final int gallery = 2;
    private static final int camera2 = 3;
    private static final int gallery2 = 4;
    private static final int PERMISSION_ID = 1234;
    Context mContext = UserInformation.this;
    Toolbar userinfo_toolbar;
    String latUser, lonUser;
    TextView toolbar_title, contusertxt;
    EditText MobileNumberedit, WorkPhoneNumberedit, HomePhoneNumberedit, updateemailedit,
            usernameemailedit, k7edit, upadatecityedit, updatestateedittt;
    EditText UpdateHomeAddressET, Other_Numberedit;
    AutoCompleteTextView updatepostaledit;
    CheckBox editcheck0, signup2checkbox;
    SessionUser sessionUser;
    String logid = "";
    RelativeLayout NavigationUpIM;
    ProgressDialog dialog;
    EditText FnameUpdateET, LnameUpdateET;
    CircleImageView img_user_image;
    Uri fileUri;
    String PhotoOrString = "";
    String CompanyUpload = "";
    String fileUriPath;

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(), "/fixezi");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Hello Camera", "Oops! Failed create Hello Camera directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Updating Information..");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinformation);
        sessionUser = new SessionUser(this);
        logid = sessionUser.getId();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        iniComp();

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (InternetDetect.isConnected(getApplicationContext())) {
            new JsonGetInfo().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check internet connection", Toast.LENGTH_SHORT);
        }

        toolbar_title.setText("User information");

        setSupportActionBar(userinfo_toolbar);

        contusertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signup2checkbox.isChecked()) {
                    if (InternetDetect.isConnected(UserInformation.this)) {
                        if (Validation()) {
                            updateProfile();
                            // new SaveJsonTask().execute();
                        }
                    } else {
                        Toast.makeText(UserInformation.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserInformation.this, "Please check the box to edit", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean Validation() {
                if (FnameUpdateET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(UserInformation.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (LnameUpdateET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(UserInformation.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (UpdateHomeAddressET.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(UserInformation.this, "Enter Home Address", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (MobileNumberedit.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(UserInformation.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

        });

        img_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    gotoAddProfileImage(camera2, gallery2);
                } else {
                    requestPermissions();
                }
            }
        });

        editcheck0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updatepostaledit.setEnabled(true);
                    MobileNumberedit.setEnabled(true);
                    updateemailedit.setEnabled(true);
                    usernameemailedit.setEnabled(true);
                    k7edit.setEnabled(true);
                    upadatecityedit.setEnabled(true);
                    updatestateedittt.setEnabled(true);
                    WorkPhoneNumberedit.setEnabled(true);
                    LnameUpdateET.setEnabled(true);
                    FnameUpdateET.setEnabled(true);
                    UpdateHomeAddressET.setEnabled(true);
                    img_user_image.setEnabled(true);
                } else {
                    updatepostaledit.setEnabled(false);
                    MobileNumberedit.setEnabled(false);
                    updateemailedit.setEnabled(false);
                    usernameemailedit.setEnabled(false);
                    k7edit.setEnabled(false);
                    upadatecityedit.setEnabled(false);
                    updatestateedittt.setEnabled(false);
                    WorkPhoneNumberedit.setEnabled(false);
                    LnameUpdateET.setEnabled(false);
                    FnameUpdateET.setEnabled(false);
                    UpdateHomeAddressET.setEnabled(false);
                    img_user_image.setEnabled(false);
                }
            }
        });

    }

    private void getLatLonFromAddress(String address) {
        ProjectUtil.showProgressDialog(UserInformation.this, false, "Please wait...");
        String postReceiverUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                address.replace(",", "+") + "&key=" + getResources().getString(R.string.places_api_key);
        Log.e("asdasdasds", "Places Url = " + postReceiverUrl);
        AndroidNetworking.get(postReceiverUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                ProjectUtil.pauseProgressDialog();
                Log.e("getLatLonFromAddress", response);
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray resultArray = mainObj.getJSONArray("results");
                    JSONObject resultFirstObj = resultArray.getJSONObject(0);
                    JSONObject geometryObj = resultFirstObj.getJSONObject("geometry");
                    JSONObject locationObj = geometryObj.getJSONObject("location");

                    double lat = locationObj.getDouble("lat");
                    double lon = locationObj.getDouble("lng");

                    latUser = String.valueOf(lat);
                    lonUser = String.valueOf(lon);

                    //                  Appconstants.servicelocation = diiferent_add.getText().toString().trim();
//                  Appconstants.ServiceLocation = diiferent_add.getText().toString().trim();
//                  Appconstants.SITE_ADDRESS = diiferent_add.getText().toString().trim();
//                  Appconstants.lat = lat;
//                  Appconstants.lon = lon;

                    Log.e("sfsdfsdfsdf", "SITE_ADDRESS = " + Appconstants.SITE_ADDRESS);
                    Log.e("sfsdfsdfsdf", "lat = " + lat);
                    Log.e("sfsdfsdfsdf", "lon = " + lon);
                    Log.e("sfsdfsdfsdf", "lat Add= " + (lat * 1E6));
                    Log.e("sfsdfsdfsdf", "lon Add= " + lon * 1E6);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                ProjectUtil.pauseProgressDialog();
                Log.e("responseresponse", "anError = " + anError.getErrorBody());
                Log.e("responseresponse", "anError = " + anError.getErrorDetail());
            }

        });

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_ID
        );
    }

    private void iniComp() {
        userinfo_toolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        contusertxt = (TextView) findViewById(R.id.contusertxt);
        Other_Numberedit = findViewById(R.id.Other_Numberedit);
        updatepostaledit = (AutoCompleteTextView) findViewById(R.id.updatepostaledit);
        MobileNumberedit = (EditText) findViewById(R.id.MobileNumberedit);
        updateemailedit = (EditText) findViewById(R.id.updateemailedit);
        UpdateHomeAddressET = (EditText) findViewById(R.id.UpdateHomeAddressET);
        usernameemailedit = (EditText) findViewById(R.id.usernameemailedit);
        k7edit = (EditText) findViewById(R.id.k7edit);
        upadatecityedit = (EditText) findViewById(R.id.upadatecityedit);
        updatestateedittt = (EditText) findViewById(R.id.updatestateedittt);
        HomePhoneNumberedit = (EditText) findViewById(R.id.HomePhoneNumberedit);
        WorkPhoneNumberedit = (EditText) findViewById(R.id.WorkPhoneNumberedit);
        editcheck0 = (CheckBox) findViewById(R.id.editcheck0);
        signup2checkbox = (CheckBox) findViewById(R.id.signup2checkbox);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);

        FnameUpdateET = (EditText) findViewById(R.id.FnameUpdateET);
        LnameUpdateET = (EditText) findViewById(R.id.LnameUpdateET);
        img_user_image = findViewById(R.id.img_user_image);

        updatepostaledit.setEnabled(false);
        MobileNumberedit.setEnabled(false);
        updateemailedit.setEnabled(false);
        usernameemailedit.setEnabled(false);
        k7edit.setEnabled(false);
        upadatecityedit.setEnabled(false);
        updatestateedittt.setEnabled(false);
        WorkPhoneNumberedit.setEnabled(false);
        LnameUpdateET.setEnabled(false);
        FnameUpdateET.setEnabled(false);
        UpdateHomeAddressET.setEnabled(false);
        img_user_image.setEnabled(false);

        UpdateHomeAddressET.setOnClickListener(v -> {
            Intent in = new Intent(UserInformation.this, GooglePlacesAutocompleteActivity.class);
            startActivityForResult(in, 101);
        });

        FnameUpdateET.addTextChangedListener(new TextWatcher() {

            int mStart = 0;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mStart = arg1 + arg3;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable et) {

                String input = et.toString();
                String capitalizedText;

                if (input.length() < 1)
                    capitalizedText = input;
                else if (input.length() > 1 && input.contains(" ")) {
                    String fstr = input.substring(0, input.lastIndexOf(" ") + 1);
                    if (fstr.length() == input.length()) {
                        capitalizedText = fstr;
                    } else {
                        String sstr = input.substring(input.lastIndexOf(" ") + 1);
                        sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1);
                        capitalizedText = fstr + sstr;
                    }
                } else
                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);

                if (!capitalizedText.equals(FnameUpdateET.getText().toString())) {
                    FnameUpdateET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            FnameUpdateET.setSelection(mStart);
                            FnameUpdateET.removeTextChangedListener(this);
                        }
                    });
                    FnameUpdateET.setText(capitalizedText);
                }

            }
        });

        LnameUpdateET.addTextChangedListener(new TextWatcher() {
            int mStart = 0;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                mStart = arg1 + arg3;

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable et) {

                String input = et.toString();
                String capitalizedText;
                if (input.length() < 1)
                    capitalizedText = input;
                else if (input.length() > 1 && input.contains(" ")) {
                    String fstr = input.substring(0, input.lastIndexOf(" ") + 1);
                    if (fstr.length() == input.length()) {
                        capitalizedText = fstr;
                    } else {
                        String sstr = input.substring(input.lastIndexOf(" ") + 1);
                        sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1);
                        capitalizedText = fstr + sstr;
                    }
                } else
                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);

                if (!capitalizedText.equals(LnameUpdateET.getText().toString())) {
                    LnameUpdateET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            LnameUpdateET.setSelection(mStart);
                            LnameUpdateET.removeTextChangedListener(this);
                        }
                    });

                    LnameUpdateET.setText(capitalizedText);
                }

            }
        });

        if (Appconstants.postCode == null) {
            if (InternetDetect.isConnected(this)) {
                new JsontaskPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else if (Appconstants.postCode.isEmpty()) {
            if (InternetDetect.isConnected(this)) {
                new JsontaskPostCode().execute();
            } else {
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            updatepostaledit.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_checkout, R.id.text1, Appconstants.postCode));
        }

    }

    private void updateProfile() {

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait));
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        HashMap<String, String> paramHashMap = new HashMap<>();

        paramHashMap.put("user_id", logid);
        paramHashMap.put("name", FnameUpdateET.getText().toString().trim() + " " + LnameUpdateET.getText().toString().trim());
        paramHashMap.put("first_name", FnameUpdateET.getText().toString().trim());
        paramHashMap.put("last_name", LnameUpdateET.getText().toString().trim());
        paramHashMap.put("home_address", UpdateHomeAddressET.getText().toString().trim());
        paramHashMap.put("home_phone", "");
        if (latUser == null) {
            paramHashMap.put("user_lat", "");
            paramHashMap.put("user_lon", "");
        } else {
            paramHashMap.put("user_lat", latUser);
            paramHashMap.put("user_lon", lonUser);
        }
        paramHashMap.put("work_phone", Other_Numberedit.getText().toString().trim());
        paramHashMap.put("mobile_phone", MobileNumberedit.getText().toString().trim());
        paramHashMap.put("email", updateemailedit.getText().toString().trim());
        paramHashMap.put("username", updateemailedit.getText().toString().trim());

        HashMap<String, File> fileHashParam = new HashMap<>();
        if (CompanyUpload == null) fileHashParam.put("user_image", null);
        else fileHashParam.put("user_image", new File(CompanyUpload));

        AndroidNetworking.initialize(mContext, okHttpClient);
        AndroidNetworking.upload("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?" + "update_normal_user_profile")
                .addMultipartParameter(paramHashMap)
                .addMultipartFile(fileHashParam)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        finish();
                        Toast.makeText(mContext, "Update Profile Successfully", Toast.LENGTH_SHORT).show();
                        Log.e("adasdasdasdasd", "response = " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

    private void gotoAddProfileImage(final int camera2, final int gallery2) {

        final Dialog dialog = new Dialog(UserInformation.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choosemedia);
        dialog.setTitle("Upload Image");

        ImageView CameraImage = (ImageView) dialog.findViewById(R.id.CameraImage);
        ImageView GalleryImage = (ImageView) dialog.findViewById(R.id.GalleryImage);

        CameraImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, camera2);
                dialog.dismiss();
            }
        });

        GalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, gallery2);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                CompanyUpload = RealPathUtil.getRealPath(mContext, resultUri);
                Log.e("asdasfasdsasd", "CompanyUpload = " + CompanyUpload);
                Log.e("asdasfasdsasd", "resultUri = " + resultUri);
                img_user_image.setImageURI(resultUri);
                Toast.makeText(UserInformation.this, "Uploaded", Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (resultCode == 101) {
            String add = data.getStringExtra("add");
            UpdateHomeAddressET.setText(add);
            getLatLonFromAddress(add);
            Log.e("asdadadasd", "address = " + add);
        }

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case gallery:

                    if (data != null) {

                        Uri contentURI = data.getData();
                        try {
                            String pat3 = RealPathUtil.getRealPath(mContext, contentURI);
                            CropImage.activity(data.getData()).start(this);
//                            Compress.get(mContext).setQuality(80).execute(new Compress.onSuccessListener() {
//                                @Override
//                                public void response(boolean status, String message, File file) {
//                                    Intent intent121 = new Intent(mContext, CropActivity.class);
//                                    intent121.putExtra("ImagePath", file.getAbsolutePath());
//                                    startActivityForResult(intent121, 1000);
//                                }
//                            }).CompressedImage(pat3);

                        } catch (Exception e) {
                            Log.e("hjagksads", "image = " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    break;

                case camera:
                    Bundle extras2 = data.getExtras();
                    Bitmap bitmapNew2 = (Bitmap) extras2.get("data");
                    Bitmap imageBitmap2 = BITMAP_RE_SIZER(bitmapNew2, bitmapNew2.getWidth(), bitmapNew2.getHeight());

                    Uri tempUri2 = getImageUri(mContext, imageBitmap2);

                    String path2 = RealPathUtil.getRealPath(mContext, tempUri2);

                    CropImage.activity(tempUri2).start(this);

                    break;

                case gallery2:
                    if (data != null) {
                        Uri contentURI = data.getData();
                        CropImage.activity(data.getData()).start(this);
                        //                        try {
//                            String pat3 = RealPathUtil.getRealPath(mContext, contentURI);
//                            Compress.get(mContext).setQuality(80).execute(new Compress.onSuccessListener() {
//                                @Override
//                                public void response(boolean status, String message, File file) {
//                                    Intent intent3 = new Intent(UserInformation.this, CropActivity.class);
//                                    intent3.putExtra("ImagePath", file.getAbsolutePath());
//                                    startActivityForResult(intent3, 1001);
//                                }
//                            }).CompressedImage(pat3);
//
//                        } catch (Exception e) {
//                            Log.e("hjagksads", "image = " + e.getMessage());
//                            e.printStackTrace();
//                        }
                    }
                    break;

                case camera2:

                    Bundle extras3 = data.getExtras();
                    Bitmap bitmapNew3 = (Bitmap) extras3.get("data");
                    Bitmap imageBitmap3 = BITMAP_RE_SIZER(bitmapNew3, bitmapNew3.getWidth(), bitmapNew3.getHeight());

                    Uri tempUri3 = getImageUri(mContext, imageBitmap3);

                    String path3 = RealPathUtil.getRealPath(mContext, tempUri3);
                    CropImage.activity(tempUri3).start(this);

//                    Compress.get(mContext).setQuality(80).execute(new Compress.onSuccessListener() {
//                        @Override
//                        public void response(boolean status, String message, File file) {
//                            Intent intent2 = new Intent(UserInformation.this, CropActivity.class);
//                            intent2.putExtra("ImagePath", fileUriPath);
//                            startActivityForResult(intent2, 1001);
//                        }
//                    }).CompressedImage(path3);

                    break;

                case 1000:

                    // ImageUpload_drivnig = data.getStringExtra("CroppedImage");

                    String newPath = data.getStringExtra("CroppedImage");
                    Appconstants.companyProfileRatio = data.getStringExtra("Ratio");
                    Uri uri = FileProvider.getUriForFile(UserInformation.this, getApplicationContext().getPackageName() + ".provider", new File(newPath));

                    img_user_image.setImageURI(Uri.parse(newPath));

                    Log.e("22NEWPATH", "???" + newPath);

                    Bitmap bm = null;

                    try {
                        bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte[] ba = bao.toByteArray();
                    Appconstants.IMAGE_BASE64 = Base64Decode.encodeBytes(ba);

                    Log.e("bitmap gallery", Appconstants.IMAGE_BASE64);

                    Toast.makeText(UserInformation.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    break;

                case 1001:
                    CompanyUpload = data.getStringExtra("CroppedImage");
                    Toast.makeText(UserInformation.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    img_user_image.setImageURI(Uri.parse(CompanyUpload));
                    break;

            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    public Uri getOutputMediaFileUri(int type) {
        fileUriPath = getOutputMediaFile(type).getPath();
        Uri uri = FileProvider.getUriForFile(UserInformation.this, getApplicationContext().getPackageName() + ".provider", new File(fileUriPath));
        return uri;
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

    private class JsonGetInfo extends AsyncTask<String, Void, String> {

        String name = "", post_code = "", city = "", state = "", home_phone = "", work_phone = "", mobile_phone = "", email = "", username = "", street = "", housenoo = "", result = "";
        UserDetail userDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "get_normal_user_data_byid&");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_id", logid));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                Log.e("Json", obj);

                JSONArray ParentArray = new JSONArray(obj);
                JSONObject FinalObject = ParentArray.getJSONObject(0);

                Log.e("Json2", String.valueOf(FinalObject));

                userDetail = new UserDetail();

                result = FinalObject.getString("result");

                userDetail.setName(FinalObject.getString("name"));
                userDetail.setFirst_name(FinalObject.getString("first_name"));
                userDetail.setLast_name(FinalObject.getString("last_name"));
                userDetail.setHomeAddress(FinalObject.getString("home_address"));
                userDetail.setPostcode(FinalObject.getString("post_code"));
                userDetail.setCity(FinalObject.getString("city"));
                userDetail.setState(FinalObject.getString("state"));
                userDetail.setHomephone(FinalObject.getString("home_phone"));
                userDetail.setWorkphone(FinalObject.getString("work_phone"));
                userDetail.setMobilephone(FinalObject.getString("mobile_phone"));
                userDetail.setEmail(FinalObject.getString("email"));
                userDetail.setUsername(FinalObject.getString("username"));
                userDetail.setUserImage(FinalObject.getString("user_image"));

                Log.e("userDetailss", "" + FinalObject);

            } catch (Exception e) {
                System.out.println("Errror in gettting normal userbyID" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (userDetail.getUserImage() != null && !userDetail.getUserImage().equals("")) {
                Log.e("asdasdasdas", "userDetail.getUserImage() = " + userDetail.getUserImage());
                Glide.with(mContext)
                        .load(userDetail.getUserImage())
                        .placeholder(R.drawable.user_icon_abc)
                        .error(R.drawable.user_icon_abc)
                        .into(img_user_image);
            }

            if (userDetail.getFirst_name().trim().equalsIgnoreCase("")) {
                FnameUpdateET.setText(userDetail.getFirst_name());
            } else {
                FnameUpdateET.setText(userDetail.getFirst_name().substring(0, 1).toUpperCase() + userDetail.getFirst_name().substring(1));
            }

            if (userDetail.getLast_name().trim().equalsIgnoreCase("")) {
                LnameUpdateET.setText(userDetail.getLast_name());
            } else {
                LnameUpdateET.setText(userDetail.getLast_name().substring(0, 1).toUpperCase() + userDetail.getLast_name().substring(1));
            }

            updatepostaledit.setText(userDetail.getPostcode());

            if (userDetail.getCity().trim().equalsIgnoreCase("")) {
                upadatecityedit.setText(userDetail.getCity());
            } else {
                upadatecityedit.setText(userDetail.getCity().substring(0, 1).toUpperCase() + userDetail.getCity().substring(1));
            }

            updatestateedittt.setText(userDetail.getState());
            WorkPhoneNumberedit.setText(userDetail.getWorkphone());
            MobileNumberedit.setText(userDetail.getMobilephone());
            updateemailedit.setText(userDetail.getEmail());
            usernameemailedit.setText(userDetail.getUsername());
            UpdateHomeAddressET.setText(userDetail.getHomeAddress());

        }
    }

    private class SaveJsonTask extends AsyncTask<String, Void, String> {

        String post_code = updatepostaledit.getText().toString(),
                fname = FnameUpdateET.getText().toString(), lname = LnameUpdateET.getText().toString(),
                city = upadatecityedit.getText().toString(), state = updatestateedittt.getText().toString(), home_phone = HomePhoneNumberedit.getText().toString(),
                work_phone = WorkPhoneNumberedit.getText().toString(), mobile_phone = MobileNumberedit.getText().toString(),
                email = updateemailedit.getText().toString(), username = usernameemailedit.getText().toString(), home_address = UpdateHomeAddressET.getText().toString();

        UserDetail userDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "update_normal_user_profile");

            try {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("user_id", logid));
                nameValuePairs.add(new BasicNameValuePair("name", fname + " " + lname));
                nameValuePairs.add(new BasicNameValuePair("first_name", fname));
                nameValuePairs.add(new BasicNameValuePair("last_name", lname));
                nameValuePairs.add(new BasicNameValuePair("home_address", home_address));
                nameValuePairs.add(new BasicNameValuePair("home_phone", home_phone));
                nameValuePairs.add(new BasicNameValuePair("work_phone", work_phone));
                nameValuePairs.add(new BasicNameValuePair("mobile_phone", mobile_phone));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("username", username));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String obj = EntityUtils.toString(response.getEntity());

                Log.e("Jsonsave", obj);

                JSONArray mainArray = new JSONArray(obj);
                JSONObject jsonObject = mainArray.getJSONObject(0);

                userDetail = new UserDetail();

                userDetail.setName(jsonObject.getString("name"));
                userDetail.setFirst_name(jsonObject.getString("first_name"));
                userDetail.setLast_name(jsonObject.getString("last_name"));
                userDetail.setHomeAddress(jsonObject.getString("home_address"));
                userDetail.setPostcode(jsonObject.getString("post_code"));
                userDetail.setCity(jsonObject.getString("city"));
                userDetail.setState(jsonObject.getString("state"));
                userDetail.setHomephone(jsonObject.getString("home_phone"));
                userDetail.setWorkphone(jsonObject.getString("work_phone"));
                userDetail.setMobilephone(jsonObject.getString("mobile_phone"));
                userDetail.setEmail(jsonObject.getString("email"));
                userDetail.setUsername(jsonObject.getString("username"));
                userDetail.setHomeAddress(jsonObject.getString("home_address"));

                String result = jsonObject.getString("result");
                return result;

            } catch (Exception e) {
                System.out.println("eeeerrrrorrrrr in registration ++++++" + e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null) {

            } else if (result.equalsIgnoreCase("successfully")) {

                Toast.makeText(UserInformation.this, "Information Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private class JsontaskPostCode extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(HttpPAth.Urlpath + "all_postcode");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                InputStream inputStream = httpEntity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                Appconstants.postCode = new ArrayList<>();
                String Jsondata = stringBuffer.toString();
                Log.e("Jsondata Load: ", Jsondata);
                JSONArray parentarray = new JSONArray(Jsondata);

                for (int i = 0; i < parentarray.length(); i++) {
                    JSONObject parentobject = parentarray.getJSONObject(i);

                    String post_code = parentobject.getString("post_code");
                    Appconstants.postCode.add(post_code);
                    result = parentobject.getString("result");
                }

                return result;
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
            } else if (result.equalsIgnoreCase("successfully")) {
                updatepostaledit.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_checkout, R.id.text1, Appconstants.postCode));
            } else {
            }

        }
    }


}
