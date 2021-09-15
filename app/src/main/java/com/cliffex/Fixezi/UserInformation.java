package com.cliffex.Fixezi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.core.content.FileProvider;

import com.cliffex.Fixezi.Model.UserDetail;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

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
import java.util.List;
import java.util.Locale;

/**
 * Created by technorizen8 on 2/3/16.
 */
public class UserInformation extends AppCompatActivity {
    private static final int camera = 1;
    private static final int gallery = 2;
    private static final int camera2 = 3;
    private static final int gallery2 = 4;
    Toolbar userinfo_toolbar;
    TextView toolbar_title, contusertxt;
    EditText MobileNumberedit, WorkPhoneNumberedit, HomePhoneNumberedit, updateemailedit,
            usernameemailedit, k7edit, upadatecityedit, updatestateedittt;
    EditText UpdateHomeAddressET;
    AutoCompleteTextView updatepostaledit;
    CheckBox editcheck0, signup2checkbox;
    SessionUser sessionUser;
    String logid = "";
    RelativeLayout NavigationUpIM;
    ProgressDialog dialog;
    EditText FnameUpdateET, LnameUpdateET;
    ImageView img_user_image;
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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
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

        Toast.makeText(this, "login.>>"+logid, Toast.LENGTH_SHORT).show();

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

                    if(InternetDetect.isConnected(UserInformation.this)) {

                        if (Validation()) {
                            new SaveJsonTask().execute();
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
                gotoAddProfileImage(camera2, gallery2);
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

    private void iniComp() {
        userinfo_toolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        contusertxt = (TextView) findViewById(R.id.contusertxt);
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
        img_user_image = (ImageView) findViewById(R.id.img_user_image);

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


        FnameUpdateET.addTextChangedListener(new TextWatcher() {
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

            if(InternetDetect.isConnected(this)) {

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

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, camera2);
                dialog.dismiss();

            }
        });

        GalleryImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), gallery2);
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

                    Intent intent = new Intent(UserInformation.this, CropActivity.class);
                    intent.putExtra("ImagePath", FinalPath);
                    startActivityForResult(intent, 1000);

                    break;

                case camera:

                    Intent intent2 = new Intent(UserInformation.this, CropActivity.class);
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

                    Intent intent3 = new Intent(UserInformation.this, CropActivity.class);
                    intent3.putExtra("ImagePath", FinalPath2);
                    startActivityForResult(intent3, 1001);

                    break;

                case camera2:

                    Intent intent4 = new Intent(UserInformation.this, CropActivity.class);
                    intent4.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent4, 1001);

                    break;


                case 1000:

                    String newPath = data.getStringExtra("CroppedImage");
                    Appconstants.companyProfileRatio = data.getStringExtra("Ratio");
                    Uri uri = FileProvider.getUriForFile(UserInformation.this, getApplicationContext().getPackageName() + ".provider", new File(newPath));

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
                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    Uri uri2 = FileProvider.getUriForFile(UserInformation.this, getApplicationContext().getPackageName() + ".provider", new File(CompanyUpload));
                    Log.e("22NEWPATHH", "???" + CompanyUpload);

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
                    Toast.makeText(UserInformation.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    img_user_image.setImageBitmap(bm2);

                    break;

            }
        }
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

                Log.e("userDetailss", "" + FinalObject);

            } catch (Exception e) {
                System.out.println("Errror in gettting normal userbyID" + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

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
