package com.cliffex.Fixezi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.MultipartUtility;
import com.cliffex.Fixezi.Other.AppConfig;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.cliffex.Fixezi.util.TimePickerFragmentNotes;
import com.google.android.gms.maps.Projection;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNotes extends AppCompatActivity {

    private static final int PERMISSION_ID = 1001;
    Toolbar toolbar;
    SessionTradesman sessionTradesman;
    TextView toolbar_textview;
    RelativeLayout NavigationUpIM;
    EditText AddNotesET;
    Button UploadNotesBT;
    TextView AddNotesTV2;
    Uri fileUri;
    String PhotoOrString = "";
    ImageView ClearImageIM;

    private static final int camera2 = 3;
    private static final int gallery2 = 4;

    String IMAGE_BASE642 = "";
    String CroppedImagePath = "";
    String CroppedImagePath1 = "";
    String Ratio = "";
    String ProblemId = "";
    ProgressBar AddNotesPB;
    TextView tv_time;
    public static String str_addnotes;
    MultipartBody.Part user_imagemultipart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        sessionTradesman = new SessionTradesman(this);
        toolbar = (Toolbar) findViewById(R.id.tradesamn_toolbar);
        toolbar_textview = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        toolbar_textview.setText("Add Notes");
        setSupportActionBar(toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        UploadNotesBT = (Button) findViewById(R.id.UploadNotesBT);
        AddNotesET = (EditText) findViewById(R.id.AddNotesET);
        AddNotesTV2 = (TextView) findViewById(R.id.AddNotesTV2);
        tv_time = (TextView) findViewById(R.id.tv_time);
        ClearImageIM = (ImageView) findViewById(R.id.ClearImageIM);
        AddNotesPB = (ProgressBar) findViewById(R.id.AddNotesPB);

        Bundle bundle = getIntent().getExtras();
        ProblemId = bundle.getString("ProblemId");
        Log.e("ProblemId", ">>>" + ProblemId);

        AddNotesTV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strAddNotesET = AddNotesET.getText().toString();
                String offerTime = tv_time.getText().toString();

                if (strAddNotesET.equalsIgnoreCase("")) {
                    AddNotesET.setError("Please Add a Note");
                } else {
                    File file;
                    System.out.println("------------------- " + user_imagemultipart);
                    if (CroppedImagePath != null) {
                        file = new File(CroppedImagePath);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        user_imagemultipart = MultipartBody.Part.createFormData("notes_image", file.getName(), requestFile);
                        addNotesApi(ProblemId, strAddNotesET, "Photo", offerTime);
                        // addNotes(ProblemId, strAddNotesET, "Photo", offerTime, user_imagemultipart);
                    } else {
                        Toast.makeText(AddNotes.this, "Please select image", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new TimePickerFragmentNotes();
                // Show the time picker dialog fragment
                dFragment.show(getFragmentManager(), "Time Picker");

            }
        });

        UploadNotesBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PhotoOrString.equalsIgnoreCase("String")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddNotes.this);
                    builder1.setTitle("fixezi Team");
                    builder1.setMessage("You can either upload image of your notes or enter content for notes.");
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
                        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddNotes.this);
                    } else {
                        requestPermissions();
                    }
                    // gotoAddProfileImage(camera2, gallery2);
                }

            }
        });

        ClearImageIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImageIM.setVisibility(View.GONE);
                UploadNotesBT.setBackgroundResource(R.drawable.border_black_solid_white);
                UploadNotesBT.setText("Click here to upload a Note");
                UploadNotesBT.setTextColor(Color.parseColor("#000000"));
                PhotoOrString = "";
                IMAGE_BASE642 = "";
                CroppedImagePath = "";
                Ratio = "";
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

    private class JsonAddNotes extends AsyncTask<String, String, String> {

        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AddNotesPB.setVisibility(View.VISIBLE);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            String charset = "UTF-8";
            //http://fixezi.com.au/FIXEZI/webserv.php?add_trademan_notes&problem_id=1¬es_description=testing¬es_image=1.png¬es_image_ratio=dfhdf¬es_type=aaa
            String requestURL = HttpPAth.Urlpath + "add_trademan_notes";

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                multipart.addFormField("problem_id", strings[0]);

                if (PhotoOrString.equalsIgnoreCase("String")) {

                    multipart.addFormField("notes_description", strings[1]);
                    multipart.addFormField("notes_type", "String");
                    multipart.addFormField("timeonjob", tv_time.getText().toString());


                } else if (PhotoOrString.equalsIgnoreCase("Photo")) {

                    File ImageFile = new File(CroppedImagePath);
                    multipart.addFilePart("notes_image", ImageFile);
                    multipart.addFormField("notes_image_ratio", Ratio);
                    multipart.addFormField("notes_description", "");
                    multipart.addFormField("notes_type", "Photo");
                    multipart.addFormField("timeonjob", tv_time.getText().toString());

                }


                List<String> response = multipart.finish();

                String Jsondata = "";

                for (String line : response) {
                    System.out.println(line);
                    Jsondata = line;
                }

                Log.e("JsonAddNotes", Jsondata);
                JSONArray jsonArray = new JSONArray(Jsondata);
                JSONObject parentObject = jsonArray.getJSONObject(0);
                result = parentObject.getString("result");

                return result;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else if (result.equalsIgnoreCase("successfully")) {
                Toast.makeText(AddNotes.this, "Your note is added", Toast.LENGTH_SHORT).show();
                finish();
            }

            AddNotesPB.setVisibility(View.GONE);

        }
    }

    private void gotoAddProfileImage(final int camera, final int gallery) {

        final Dialog dialog = new Dialog(AddNotes.this);
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                // Glide.with(mContext).load(resultUri).into(binding.profileImage);
                String path = RealPathUtil.getRealPath(AddNotes.this, resultUri);
                File file = new File(path);
                // setImageFromCameraGallery(file);
                Log.e("asfasdasdad", "resultUri = " + resultUri);
                CroppedImagePath1 = path;
                CroppedImagePath = path;
                UploadNotesBT.setBackgroundResource(R.drawable.border_black_solid_red);
                UploadNotesBT.setTextColor(Color.parseColor("#ffffff"));
                UploadNotesBT.setText("Image Uploaded");
                ClearImageIM.setVisibility(View.VISIBLE);
                PhotoOrString = "Photo";
                // binding.profileImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case gallery2:

                    Uri selectedImage2 = data.getData();
                    String[] filePathColumn2 = {MediaStore.Images.Media.DATA};
                    Cursor cursor2 = getContentResolver().query(selectedImage2, filePathColumn2, null, null, null);
                    cursor2.moveToFirst();

                    int columnIndex2 = cursor2.getColumnIndex(filePathColumn2[0]);
                    String FinalPath2 = cursor2.getString(columnIndex2);
                    cursor2.close();
                    Log.e("PATH", "" + FinalPath2);
                    Intent intent3 = new Intent(AddNotes.this, CropActivity.class);
                    intent3.putExtra("ImagePath", FinalPath2);
                    startActivityForResult(intent3, 1001);

                    break;

                case camera2:

                    Intent intent4 = new Intent(AddNotes.this, CropActivity.class);
                    intent4.putExtra("ImagePath", fileUri.getPath());
                    startActivityForResult(intent4, 1001);

                    break;

                case 1001:

                    CroppedImagePath1 = data.getStringExtra("CroppedImage");
                    Ratio = data.getStringExtra("Ratio");

                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    Uri uri2 = Uri.fromFile(new File(CroppedImagePath1));
                    Log.e("22NEWPATH", "???" + CroppedImagePath1);


                    Bitmap bm2 = null;
                    try {
                        bm2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                    bm2.compress(Bitmap.CompressFormat.JPEG, 90, bao2);
                    byte[] ba2 = bao2.toByteArray();
                    IMAGE_BASE642 = Base64Decode.encodeBytes(ba2);
                    Log.e("bitmap gallery", Appconstants.IMAGE_BASE642);

                    Toast.makeText(AddNotes.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    UploadNotesBT.setBackgroundResource(R.drawable.border_black_solid_red);
                    UploadNotesBT.setTextColor(Color.parseColor("#ffffff"));
                    UploadNotesBT.setText("Image Uploaded");
                    ClearImageIM.setVisibility(View.VISIBLE);
                    PhotoOrString = "Photo";
                    break;
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
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

    private void addNotesApi(String problem_id, String notes_description, String notes_type, String timeonjob) {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(AddNotes.this, okHttpClient);

        HashMap<String, String> param = new HashMap<>();

        param.put("problem_id", problem_id);
        param.put("notes_description", notes_description);
        param.put("notes_type", notes_type);
        param.put("timeonjob", timeonjob);

        HashMap<String, File> fileHashMap = new HashMap<>();

        if (CroppedImagePath != null) {
            fileHashMap.put("notes_image", new File(CroppedImagePath));
        } else {
            fileHashMap.put("notes_image", null);
        }

        Log.e("AddNotesApiAddNotesApi","param = " + param);
        Log.e("AddNotesApiAddNotesApi","fileHashMap = " + fileHashMap);

        ProjectUtil.showProgressDialog(AddNotes.this,false,getString(R.string.please_wait));
        AndroidNetworking.upload(HttpPAth.Urlpath + "add_trademan_notes")
                .addMultipartParameter(param)
                .addMultipartFile(fileHashMap)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ProjectUtil.pauseProgressDialog();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject parentObject = jsonArray.getJSONObject(0);
                            String result = parentObject.getString("result");
                            System.out.println("addNotes--->" + result);
                            if (result.equals("successfully")) {
                                Toast.makeText(AddNotes.this, "Your note is added", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddNotes.this, "" + result, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtil.pauseProgressDialog();
                    }
                });

    }

    private void addNotes(String problem_id, String notes_description, String notes_type, String timeonjob, MultipartBody.Part body) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(AddNotes.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Call<ResponseBody> call = AppConfig.loadInterface().addNotes(problem_id, notes_description, notes_type, timeonjob, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject parentObject = jsonArray.getJSONObject(0);
                        String result = parentObject.getString("result");
                        System.out.println("addNotes--->" + result);
                        if (result.equals("successfully")) {
                            Toast.makeText(AddNotes.this, "Your note is added", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddNotes.this, "" + result, Toast.LENGTH_SHORT).show();
                        }

                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(AddNotes.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
