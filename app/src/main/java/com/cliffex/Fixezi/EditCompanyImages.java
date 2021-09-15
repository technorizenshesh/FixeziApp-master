package com.cliffex.Fixezi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
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
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.cliffex.Fixezi.Model.TradesManBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.google.gson.Gson;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditCompanyImages extends AppCompatActivity {

    Toolbar toolbar;
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

                    gotoAddProfileImage(camera2, gallery2);
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

                gotoAddProfileImage(camera, gallery);

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

                        new JsonUpdate().execute(ETCompanyDetail.getText().toString());
                    }

                } else {

                    Toast.makeText(EditCompanyImages.this, "Please check the box to edit", Toast.LENGTH_SHORT).show();

                }

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


    private void gotoAddProfileImage(final int camera, final int gallery) {

        final Dialog dialog = new Dialog(EditCompanyImages.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choosemedia);
        dialog.setTitle("Upload Image");

        ImageView CameraImage = (ImageView) dialog.findViewById(R.id.CameraImage);
        ImageView GalleryImage = (ImageView) dialog.findViewById(R.id.GalleryImage);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case gallery:

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String FinalPath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e("PATH", "" + FinalPath);

                    Intent intent = new Intent(EditCompanyImages.this, CropActivity.class);
                    intent.putExtra("ImagePath", FinalPath);
                    startActivityForResult(intent, 1000);

                    break;

                case camera:

                    Intent intent2 = new Intent(EditCompanyImages.this, CropActivity.class);
                    intent2.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent2, 1000);

                    break;


                case gallery2:

                    Uri selectedImage2 = data.getData();
                    String[] filePathColumn2 = {MediaStore.Images.Media.DATA};
                    Cursor cursor2 = getContentResolver().query(selectedImage2, filePathColumn2, null, null, null);
                    cursor2.moveToFirst();

                    int columnIndex2 = cursor2.getColumnIndex(filePathColumn2[0]);
                    String FinalPath2 = cursor2.getString(columnIndex2);
                    cursor2.close();
                    Log.e("PATH", "" + FinalPath2);

                    Intent intent3 = new Intent(EditCompanyImages.this, CropActivity.class);
                    intent3.putExtra("ImagePath", FinalPath2);
                    startActivityForResult(intent3, 1001);

                    break;

                case camera2:

                    Intent intent4 = new Intent(EditCompanyImages.this, CropActivity.class);
                    intent4.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent4, 1001);

                    break;


                case 1000:

                    String newPath = data.getStringExtra("CroppedImage");
                    Appconstants.companyProfileRatio = data.getStringExtra("Ratio");
                    //Uri uri = Uri.fromFile(new File(newPath));
                    Uri uri = FileProvider.getUriForFile(EditCompanyImages.this, getApplicationContext().getPackageName() + ".provider", new File(newPath));

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

                    Toast.makeText(EditCompanyImages.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    //TODO


                    if (Appconstants.companyProfileRatio.equalsIgnoreCase("43")) {
                        CompanyProfileIM.setLayoutParams(layoutParams43);
                    } else if (Appconstants.companyProfileRatio.equalsIgnoreCase("169")) {
                        CompanyProfileIM.setLayoutParams(layoutParams169);
                    } else if (Appconstants.companyProfileRatio.equalsIgnoreCase("44")) {
                        CompanyProfileIM.setLayoutParams(layoutParams44);
                    }
                    //CompanyProfileIM.setImageURI(Uri.fromFile(new File(newPath)));
                    CompanyProfileIM.setImageURI(FileProvider.getUriForFile(EditCompanyImages.this, getApplicationContext().getPackageName() + ".provider", new File(newPath)));

                    /*BTUploadPhoto.setBackgroundResource(R.drawable.border_black_solid_red);
                    BTUploadPhoto.setTextColor(Color.parseColor("#ffffff"));
                    BTUploadPhoto.setText("Image Uploaded");*/

                    break;

                case 1001:

                    CompanyUpload = data.getStringExtra("CroppedImage");
                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    //Uri uri2 = Uri.fromFile(new File(CompanyUpload));
                    Uri uri2 = FileProvider.getUriForFile(EditCompanyImages.this, getApplicationContext().getPackageName() + ".provider", new File(CompanyUpload));
                    Log.e("22NEWPATH", "???" + CompanyUpload);

                    Bitmap bm2 = null;
                    try {
                        bm2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                    bm2.compress(Bitmap.CompressFormat.JPEG, 90, bao2);
                    byte[] ba2 = bao2.toByteArray();
                    Appconstants.IMAGE_BASE642 = Base64Decode.encodeBytes(ba2);
                    Log.e("bitmap gallery", Appconstants.IMAGE_BASE642);

                    Toast.makeText(EditCompanyImages.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    RLCompanyUpload.setVisibility(View.VISIBLE);
                    BTUploadCompanyDetail.setVisibility(View.GONE);

                    if (Appconstants.companyDesRatio.equalsIgnoreCase("43")) {

                        ComapanyUploadIm.setLayoutParams(layoutParams43);
                    } else if (Appconstants.companyDesRatio.equalsIgnoreCase("169")) {

                        ComapanyUploadIm.setLayoutParams(layoutParams169);
                    } else if (Appconstants.companyDesRatio.equalsIgnoreCase("44")) {

                        ComapanyUploadIm.setLayoutParams(layoutParams44);
                    }

                    ComapanyUploadIm.setImageURI(Uri.fromFile(new File(CompanyUpload)));

                    BTUploadCompanyDetail.setBackgroundResource(R.drawable.border_black_solid_red);
                    BTUploadCompanyDetail.setTextColor(Color.parseColor("#ffffff"));
                    BTUploadCompanyDetail.setText("Image Uploaded");
                    PhotoOrString = "Photo";
                    ETCompanyDetail.setFocusable(false);

                    break;

            }
        }
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