package com.cliffex.Fixezi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.Model.CategoryBean;
import com.cliffex.Fixezi.MyUtils.Appconstants;
import com.cliffex.Fixezi.MyUtils.Base64Decode;
import com.cliffex.Fixezi.MyUtils.HttpPAth;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import com.cliffex.Fixezi.MyUtils.NonScrollListView;
import com.cliffex.Fixezi.ratrofit.ApiClient;
import com.cliffex.Fixezi.ratrofit.TrademanSignup;
import com.cliffex.Fixezi.util.Compress;
import com.cliffex.Fixezi.util.ProjectUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.cliffex.Fixezi.R.drawable.border_tourtoise_solide;
import static com.cliffex.Fixezi.R.drawable.btn_skyblue;

public class TradesmanSignup2 extends AppCompatActivity {

    private static final int camera = 1, gallery = 2, camera2 = 3,
            gallery2 = 4, camera3 = 5, gallery3 = 6,camera_driving = 10,gallery_driving=11;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;

    /* Toolbar trade_toolbar2 = new Toolbar(this); */
    EditText TradesmanUsername,TradesmanPassword,TradesmanConfirmPassword,ETCompanyDetail;
    Button afterhoursYESbtn,afterhoursNObtn,commercialbtn,residentialbtn,bothbtn,backbtn,confirmbtn,BTUploadPhoto;
    TextView toolbar_title, tvTermCon, selecttradeTV,SelectedLocationsTV,ExampleTV;
    RelativeLayout SelectTradeSignUpTradesman;
    ArrayList<CategoryBean> catarray = new ArrayList<>();
    String redcolor = "#fd3621",ImageUpload_details,profileCompLogo;
    String[] mainCategoryArray = null;
    String[] posID;
    Context mContext = TradesmanSignup2.this;
    GoogleCloudMessaging gcm;
    String RegId = "";
    RelativeLayout NavigationUpIM;
    Uri fileUri;
    String CompanyUpload = "", chargesvalue = "";
    ProgressDialog progressDialog;
    ArrayList<String> Selecteddata;
    ImageView TradesmanConditionsIm,ClearImageIM;
    Button BTUploadCompanyDetail;
    String PhotoOrString = "", fileUriPath;
    boolean IsTermsOpen = true;
    TextView SignUp2TV, tv_value;
    SeekBar seekBar;
    ArrayList<Uri> list = new ArrayList<>();
    TrademanSignup apiInterface;
    HashMap<String, ArrayList<File>> fileArrayHash = new HashMap<>();
    private CheckBox cheak_houry_charges, hourly_charges_client;
    private TextView select_tradestv;
    private LinearLayout slectlinear;
    private CheckBox cheakboxcontarct;
    private LinearLayout contract_linear;
    private TextView licence_select;
    private RelativeLayout imag_rl;
    private TextView texttrade;
    private ImageView contarct_licence_camera, gallary_image;
    private Button BTUploadCompanyDetail1;
    private File file;
    private String extension, timeStamp;
    private Uri selectedImage;
    private ArrayList<String> ProblemList = new ArrayList<>();
    private ArrayList<File> arraylist_file = new ArrayList<>();
    private RecyclerView listView;
    private String[] imageFor;
    private ArrayList<GetSet> getSets;
    private HorizontalRecyclerView customImageAdapter;
    private int position;
    private String imageTempName;
    private String ImageUpload;
    private String dyg;
    private ArrayList<File> your_array_list = new ArrayList<File>();
    private String image_final_list;
    private File image_final_list1;
    private List<File> fddf,fddf2;
    private Uri selectedImageDriving;
    private String ImageUpload_drivnig;
    private Uri fileUri1;
    private Uri selected_details;
    private RelativeLayout CompanyDetailre;
    private RelativeLayout driver_license;
    private String result1;
    private JSONObject result12;
    private String result;
    private LinearLayout driving_licnecelinear;
    private Toolbar colorPrimaryDark;
    private Toolbar ToolbarSU;
    StateProgressBar stateProgressBar;
    String[] step = {"Details","Verify","Upload","Complete"};

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(android.os.Environment.getExternalStorageDirectory(), "/fixezi");
        if (!mediaStorageDir.exists()){

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

    public static File saveImage(final Context context, final String imageData) {
        final byte[] imgBytesData = android.util.Base64.decode(imageData,
                android.util.Base64.DEFAULT);

        File file = null;
        try {
            file = File.createTempFile("image", null, context.getCacheDir());
            Log.e("fileeee", "" + file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        final FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                fileOutputStream);
        try {
            bufferedOutputStream.write(imgBytesData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading.Please Wait ......");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tademan_signup2);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                RegId = token;
                Log.e("tokentoken", "retrieve token successful : " + RegId);
            } else {
                Log.e("tokentoken", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
        }).addOnCanceledListener(() -> {
        }).addOnCompleteListener(task -> Log.e("tokentoken", "This is the token : " + task.getResult()));

        arraylist_file = new ArrayList<>();
        apiInterface = ApiClient.getClient(TradesmanSignup2.this).create(TrademanSignup.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

         /*  trade_toolbar2 = */

         // finally change the color
         ToolbarSU = (Toolbar) findViewById(R.id.trade_toolbar2);

         toolbar_title = ToolbarSU.findViewById(R.id.toolbar_title);
         NavigationUpIM =  ToolbarSU.findViewById(R.id.NavigationUpIM);

        stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(step);
        stateProgressBar.setStateDescriptionTypeface("fonts/droidderif.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/droidderif.ttf");
        stateProgressBar.setStateSize(25f);
        stateProgressBar.setStateDescriptionSize(15f);
        stateProgressBar.setDescriptionTopSpaceIncrementer(10f);

        driving_licnecelinear = (LinearLayout)findViewById(R.id.driving_licnecelinear);

        driving_licnecelinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        tvTermCon = findViewById(R.id.tv_term_conditions);
        driver_license  = findViewById(R.id.driver_license);
        SelectedLocationsTV = findViewById(R.id.SelectedLocationsTV);
        ExampleTV = findViewById(R.id.ExampleTV);
        TradesmanConditionsIm = findViewById(R.id.TradesmanConditionsIm);
        SelectTradeSignUpTradesman = findViewById(R.id.SelectTradeSignUpTradesman);
        selecttradeTV = findViewById(R.id.selecttradeTV);
        TradesmanUsername = findViewById(R.id.TradesmanUsername);
        TradesmanPassword = findViewById(R.id.TradesmanPassword);
        TradesmanConfirmPassword = findViewById(R.id.TradesmanConfirmPassword);
        SignUp2TV = findViewById(R.id.SignUp2TV);
        tv_value = findViewById(R.id.tv_value);
        seekBar = findViewById(R.id.seekBar_luminosite);
        texttrade = findViewById(R.id.texttrade);
        contarct_licence_camera = findViewById(R.id.contarct_licence_camera);
        gallary_image = findViewById(R.id.gallary_image);
        imag_rl = findViewById(R.id.imag_rl);
        CompanyDetailre   = (RelativeLayout)findViewById(R.id.CompanyDetailre);
        licence_select = findViewById(R.id.licence_select);
        contract_linear = findViewById(R.id.contract_linear);
        cheakboxcontarct = findViewById(R.id.cheakboxcontarct);

        cheak_houry_charges = findViewById(R.id.cheak_houry_charges);
        hourly_charges_client = findViewById(R.id.hourly_charges_client);

        slectlinear = findViewById(R.id.slectlinear);
        select_tradestv = findViewById(R.id.select_tradestv);

        toolbar_title.setText("Tradesman Signup");
        //setSupportActionBar(trade_toolbar2);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initComp();

        if (InternetDetect.isConnected(this)) {

            new JsonGCMTask().execute();

        } else {
            Toast.makeText(TradesmanSignup2.this, "Please Connect to Internet", Toast.LENGTH_SHORT);
        }

        TradesmanUsername.setText(Appconstants.tradeemailS);
        String text = "<font color='#1E90FF'>Please add your Company images &amp; services</font> Once you have activated your account you can preview you're images in Company profile-edit company image's";
        SignUp2TV.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        BTUploadCompanyDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PhotoOrString.equalsIgnoreCase("String")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(TradesmanSignup2.this);
                    builder1.setTitle("fixezi Team");
                    builder1.setMessage("You can either upload image of your company detail or enter text of company detail.");
                    builder1.setIcon(R.drawable.mainlogo);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    // alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(mContext,R.color.skyblue));
                    alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext,R.color.skyblue));
                } else {
                    gotoAddProfileImage3(camera2, gallery2);
                }
            }
        });

        ETCompanyDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PhotoOrString.equalsIgnoreCase("Photo")) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(TradesmanSignup2.this);
                    builder1.setTitle("fixezi Team");
                    builder1.setMessage("You can either upload image of your company detail or enter text of company detail.");
                    builder1.setIcon(R.drawable.mainlogo);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    // alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(mContext,R.color.skyblue));
                    alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext,R.color.skyblue));
                }
            }
        });

        ETCompanyDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ETCompanyDetail.getText().toString().equalsIgnoreCase("")) {
                    PhotoOrString = "";
                } else {
                    PhotoOrString = "String";
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ClearImageIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImageIM.setVisibility(View.GONE);
                BTUploadCompanyDetail.setBackgroundResource(R.drawable.border_black_solid_white);
                BTUploadCompanyDetail.setText("Click here to upload company details and service you provide");
                BTUploadCompanyDetail.setTextColor(Color.parseColor("#000000"));
                PhotoOrString = "";
                ETCompanyDetail.setFocusableInTouchMode(true);
                ETCompanyDetail.setFocusable(true);
            }
        });

        afterhoursYESbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.afterhours = "yes";
                afterhoursYESbtn.setBackgroundResource(R.drawable.border_black_solid_red);
                afterhoursYESbtn.setTextColor(Color.parseColor("#ffffff"));
                afterhoursNObtn.setBackgroundResource(R.drawable.border_black_solid_white);
                afterhoursNObtn.setTextColor(Color.parseColor("#000000"));
            }
        });

        afterhoursNObtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.afterhours = "no";
                afterhoursNObtn.setBackgroundResource(R.drawable.border_black_solid_red);
                afterhoursNObtn.setTextColor(Color.parseColor("#ffffff"));
                afterhoursYESbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                afterhoursYESbtn.setTextColor(Color.parseColor("#000000"));
            }
        });

        TradesmanConditionsIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog callFeeDialog = new Dialog(TradesmanSignup2.this);
                callFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                callFeeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                callFeeDialog.setContentView(R.layout.alert_dialog_tradesman_signup);
                final Button okBt = callFeeDialog.findViewById(R.id.okBtT);
                final TextView ClickLinkTrade = callFeeDialog.findViewById(R.id.ClickLinkTrade);

                ClickLinkTrade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.licencerecognition.gov.au"));
                        startActivity(browserIntent);

                    }
                });

                okBt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        callFeeDialog.dismiss();
                    }
                });

                callFeeDialog.show();

            }
        });

        tvTermCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent j = new Intent(getApplicationContext(), TermsConditionTradmanSignup.class);
                j.putExtra("From", "TradesmanTwo");
                startActivity(j);
                IsTermsOpen = false;

            }
        });

        commercialbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.doyouwork = "commercial";
                commercialbtn.setBackgroundResource(R.drawable.border_black_solid_red);
                commercialbtn.setTextColor(Color.parseColor("#ffffff"));
                residentialbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                residentialbtn.setTextColor(Color.parseColor("#000000"));
                bothbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                bothbtn.setTextColor(Color.parseColor("#000000"));
            }
        });

        SelectTradeSignUpTradesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ProblemList = new ArrayList<String>(Arrays.asList(mainCategoryArray));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final Dialog SelectProblemDailog = new Dialog(TradesmanSignup2.this);
                SelectProblemDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                SelectProblemDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                SelectProblemDailog.setContentView(R.layout.select_problem_alert_dailog);
                NonScrollListView SelectProblemList = SelectProblemDailog.findViewById(R.id.SelectProblemList);
                Button CancelProblemTV = SelectProblemDailog.findViewById(R.id.CancelProblemTV);
                Button AcceptProblemTV = SelectProblemDailog.findViewById(R.id.AcceptProblemTV);
                TextView SelecTradeTV = SelectProblemDailog.findViewById(R.id.SelecTradeTV);

                Selecteddata = new ArrayList<String>();
                SelecTradeTV.setText("Select Trade");
                try {
                    if (!ProblemList.equals("")) {
                        ListViewAdapter adapter2 = new ListViewAdapter(TradesmanSignup2.this, ProblemList);
                        SelectProblemList.setAdapter(adapter2);
                        SelectProblemList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        SelectProblemList.setItemsCanFocus(false);
                    }
                    else {
                        Toast.makeText(TradesmanSignup2.this, "please wait !!!", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){

                }

                CancelProblemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectProblemDailog.dismiss();
                    }
                });

                AcceptProblemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Selecteddata.isEmpty()) {

                        } else {

                            StringBuilder s = new StringBuilder();
                            for (int i = 0; i < Selecteddata.size(); i++) {
                                if (i == 0) {
                                    s.append(Selecteddata.get(i));
                                    continue;
                                }
                                s.append("," + Selecteddata.get(i));
                            }

                            String AllFollowerID = s.toString();
                            Log.e("AllFollowerID", "??" + AllFollowerID);
                            Log.e("SelecteddataSize", "??" + Selecteddata.size());
                            select_tradestv.setText("Select Your Trades" + " " + Selecteddata.size());
                            selecttradeTV.setText(AllFollowerID);
                            Appconstants.selecttrade = AllFollowerID;
                            selecttradeTV.setTextColor(Color.WHITE);
                            slectlinear.setBackgroundColor(Color.parseColor(redcolor));
                            texttrade.setText("Upload contractor licence image for" + " (" + Selecteddata.size() + ") " + "trades");

                            if (!(Selecteddata.size() == 0)) {
                                listView = findViewById(R.id.captureList);
                                getSets = new ArrayList<GetSet>();
                                imageFor = getResources().getStringArray(R.array.imageFor);

                                for(int i = 0; i < Selecteddata.size(); i++) {
                                    GetSet inflate = new GetSet();
                                    inflate.setUid(String.valueOf(i));
                                    inflate.setLabel("Image");
                                    inflate.setHaveImage(false);
                                    inflate.setSubtext(imageFor[i]);
                                    inflate.setStatus(true);
                                    getSets.add(inflate);
                                }

                                customImageAdapter = new HorizontalRecyclerView(getSets, TradesmanSignup2.this, arraylist_file);
                                listView.setAdapter(customImageAdapter);
                                listView.setLayoutManager(new LinearLayoutManager(TradesmanSignup2.this, LinearLayoutManager.HORIZONTAL, false));

                                try {
                                    fddf2 = getSets.get(position).getArrayList();
                                } catch (Exception e) {}

                                if (fddf2 == null) {
                                } else if (ImageUpload_details == null) {
                                } else if (ImageUpload_drivnig ==null) {
                                } else {
                                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                                }

                            }
                        }

                        SelectProblemDailog.dismiss();

                    }
                });

                SelectProblemDailog.show();

            }
        });


        residentialbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.doyouwork = "residential";
                residentialbtn.setBackgroundResource(R.drawable.border_black_solid_red);
                residentialbtn.setTextColor(Color.parseColor("#ffffff"));
                commercialbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                commercialbtn.setTextColor(Color.parseColor("#000000"));
                bothbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                bothbtn.setTextColor(Color.parseColor("#000000"));
            }
        });

        bothbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appconstants.doyouwork = "both";
                bothbtn.setBackgroundResource(border_tourtoise_solide);
                bothbtn.setTextColor(Color.parseColor("#ffffff"));
                residentialbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                residentialbtn.setTextColor(Color.parseColor("#000000"));
                commercialbtn.setBackgroundResource(R.drawable.border_black_solid_white);
                commercialbtn.setTextColor(Color.parseColor("#000000"));
            }
        });

        BTUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                android.Manifest.permission.INTERNET)
                                != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TradesmanSignup2.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(TradesmanSignup2.this,
                            android.Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(TradesmanSignup2.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                           ) {
                        gotoAddProfileImage(camera, gallery);
                    } else {
                        ActivityCompat.requestPermissions(TradesmanSignup2.this,
                                new String[]{
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,},
                                MY_PERMISSIONS_REQUEST_WRITE_FIELS);
                    }
                } else {
                    gotoAddProfileImage(camera, gallery);
                    Toast.makeText(getApplicationContext(), "Success!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmbtn.setEnabled(false);
                image_final_list1 = new File(String.valueOf(getSets.get(position).getArrayList()));
                fddf = getSets.get(position).getArrayList();

                Log.e("arraylist_fileq", "" + fddf);
                if (cheak_houry_charges.isChecked() || hourly_charges_client.isChecked()) {
                    boolean isError = false;
                    Appconstants.selecttrade = selecttradeTV.getText().toString();
                    Appconstants.TradesmanUsername = TradesmanUsername.getText().toString();
                    Appconstants.TradesmanPassword = TradesmanPassword.getText().toString();
                    Appconstants.TradesmanConfirmPassword = TradesmanConfirmPassword.getText().toString();
                    Appconstants.CompanyDetail = ETCompanyDetail.getText().toString();

                    if (Appconstants.afterhours.equals("")) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Do you work AfterHours ?");
                    } else if (Appconstants.selecttrade.equals("") || Appconstants.selecttrade.equals("SELECT YOUR TRADES")) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Select Trade");
                    } else if (Appconstants.doyouwork.equals("")) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Do you work ?");
                    } /*else if (Appconstants.IMAGE_BASE64.equals("")) {
                        isError = true;
                        errorToast("Please Upload Image");
                    } *//*else if (PhotoOrString.equals("")) {
                        isError = true;
                        errorToast("Please Enter Company Detail or Upload Company Detail Photo");
                    } */ else if (IsTermsOpen) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Read Terms and Condition");
                    } else if (Appconstants.TradesmanUsername.equals("")) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Enter Username");
                    } else if (Appconstants.TradesmanPassword.equals("")) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Enter Password");
                    } else if (Appconstants.TradesmanPassword.length() < 6) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Minimum password length is 6");
                    } else if (Appconstants.TradesmanConfirmPassword.equals("")) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Confirm Password");
                    } else if (!Appconstants.TradesmanPassword.equals(Appconstants.TradesmanConfirmPassword)) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Password does not match");
                    } else if (fddf==null) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Select Trades Image ");
                    } else if (ImageUpload_details == null) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Select  Image ");
                    } else if (profileCompLogo == null) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Select Company logo");
                    } else if (ImageUpload_drivnig == null) {
                        confirmbtn.setEnabled(true);
                        isError = true;
                        errorToast("Please Select Licence  ");
                    } else {
                        TradesmanSignUpUpdate(RegId, (ArrayList<File>) fddf);
                    }

                /*    if (!isError) {
                        if (InternetDetect.isConnected(getApplicationContext())) {

                            if (RegId.equalsIgnoreCase("")) {

                                new JsonGCMTask().execute();

                            } else {

                                Log.e("All Value Sign Up", Appconstants.buisinessnameeS + "\n" + Appconstants.tradingnameS + "\n"
                                        + Appconstants.tradeofiicenoS + "\n" + Appconstants.trademobileee
                                        + "\n" + Appconstants.tradeemailS + "\n" + Appconstants.companywebsiteS
                                        + "\n" + Appconstants.afterhours + "\n" + Appconstants.selecttrade
                                        + "\n" + Appconstants.servicelocation + "\n" + Appconstants.doyouwork
                                        + "\n" + "image profile >>  " + Appconstants.IMAGE_BASE64 + "  <<image profile " + "\n" + Appconstants.companyProfileRatio
                                        + "\n" + Appconstants.IMAGE_BASE642 + "\n" + Appconstants.companyDesRatio
                                        + "\n" + Appconstants.CompanyDetail + "\n" + Appconstants.TradesmanUsername
                                        + "\n" + Appconstants.TradesmanPassword);
                                TradesmanSignUpUpdate(RegId, (ArrayList<File>) fddf);

                            }

                        } else {

                            errorToast("Check Internet connection");
                        }
                    }*/

                } else {
                    Toast.makeText(TradesmanSignup2.this, "Please select the checkbox", Toast.LENGTH_SHORT).show();
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_value.setText("Charges $ " + progress + "");
                chargesvalue = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //   Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });

        cheak_houry_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_value.setText("Charges $ " + "0" + "");
                hourly_charges_client.setChecked(false);
                cheak_houry_charges.setChecked(true);
                seekBar.setEnabled(false);
            }
        });

        cheakboxcontarct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cheakboxcontarct.isChecked()) {
                    // imag_rl.setVisibility(View.GONE);
                }
            }
        });

        hourly_charges_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hourly_charges_client.setChecked(true);
                cheak_houry_charges.setChecked(false);
                seekBar.setEnabled(true);
            }
        });
    }

    private void gotoAddProfileImage3(final int camera2, final int gallery2) {

        final Dialog dialog = new Dialog(TradesmanSignup2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choosemedia);
        dialog.setTitle("Upload Image");
        ImageView CameraImage = dialog.findViewById(R.id.CameraImage);
        ImageView GalleryImage = dialog.findViewById(R.id.GalleryImage);

        CameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri1 = getOutputMediaFileUri(1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
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

    private void TradesmanSignUpUpdate(String regId, ArrayList<File> imgFile4567) {

        Log.e("imgFile4567", "" + imgFile4567);
        fileArrayHash.put("contractor_img[]", imgFile4567);

        ProjectUtil.showProgressDialog(mContext,false,"PLease wait...");

        AndroidNetworking.upload("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?" + "tradesman_sign_up")
                .addMultipartParameter("business_name", Appconstants.buisinessnameeS)
                .addMultipartParameter("trading_name", Appconstants.tradingnameS)
                .addMultipartParameter("businessOwnerrnName", Appconstants.buisness_owner_name)
                .addMultipartParameter("businessAddress", Appconstants.buisness_address)
                .addMultipartParameter("office_no", Appconstants.tradeofiicenoS)
                .addMultipartParameter("mobile_no", Appconstants.trademobileee)
                .addMultipartParameter("email", Appconstants.tradeemailS)
                .addMultipartParameter("website_url", Appconstants.companywebsiteS)
                .addMultipartParameter("after_hours", Appconstants.afterhours)
                .addMultipartParameter("select_trade", Appconstants.selecttrade)
                .addMultipartParameter("service_locatin", Appconstants.servicelocation)
                .addMultipartParameter("work_location", Appconstants.doyouwork)
                .addMultipartParameter("company_profile_ratio", Appconstants.companyProfileRatio)
                .addMultipartParameter("username", Appconstants.TradesmanUsername)
                .addMultipartParameter("password", Appconstants.TradesmanPassword)
                .addMultipartParameter("hour_min", chargesvalue)
                .addMultipartParameter("registration_id", regId)
                .addMultipartParameter("company_upload_ratio", Appconstants.companyDesRatio)
                .addMultipartFile("company_detail_upload", new File(ImageUpload_details))
                .addMultipartFile("profile_pic", new File(profileCompLogo))
                .addMultipartFileList(fileArrayHash)
                .addMultipartFile("driver_licence_img", new File(ImageUpload_drivnig))
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtil.pauseProgressDialog();
                        try {
                            JSONObject jresponse = new JSONObject(String.valueOf(response));
                            result = jresponse.getString("result");
                            Toast.makeText(TradesmanSignup2.this, ""+result, Toast.LENGTH_SHORT).show();
                            Log.e("result11", String.valueOf(result));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("response", String.valueOf(response));

                        if (result.equals("successfully")) {

                            Toast.makeText(TradesmanSignup2.this, "sasasas!!!", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(TradesmanSignup2.this);
                            builder1.setTitle("Registration Successful");
                            builder1.setIcon(R.drawable.mainlogo);
                            builder1.setMessage("A Confirmation Link has been sent to registered email id. Please click on the Activation Link to Activate your account.");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            PreferenceConnector.writeString(TradesmanSignup2.this, PreferenceConnector.OTPStatus, "");
                                            Intent intent = new Intent(getApplicationContext(), TradesmanLogin.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }
                        else {
                            Toast.makeText(TradesmanSignup2.this, "Email Already Exit ", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        confirmbtn.setEnabled(true);
                        ProjectUtil.pauseProgressDialog();
                        Toast.makeText(TradesmanSignup2.this, "error >>" + error, Toast.LENGTH_SHORT).show();
                    }
                });
              }

    private void errorToast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }

    private void initComp() {
        afterhoursYESbtn = findViewById(R.id.afterhoursYESbtn);
        afterhoursNObtn = findViewById(R.id.afterhoursNObtn);
        commercialbtn = findViewById(R.id.commercialbtn);
        residentialbtn = findViewById(R.id.residentialbtn);
        bothbtn = findViewById(R.id.bothbtn);
        backbtn = findViewById(R.id.backbtn);
        confirmbtn = findViewById(R.id.confirmbtn);
        BTUploadPhoto = findViewById(R.id.BTUploadPhoto);
        BTUploadCompanyDetail = findViewById(R.id.BTUploadCompanyDetail);
        ETCompanyDetail = findViewById(R.id.ETCompanyDetail);
        ClearImageIM = findViewById(R.id.ClearImageIM);
        BTUploadCompanyDetail1 = findViewById(R.id.BTUploadCompanyDetail1);

        driving_licnecelinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyDetailre.setBackgroundColor(border_tourtoise_solide);
                gotoAddProfileImage1(camera_driving, gallery_driving);
            }
        });
    }

    private void gotoAddProfileImage1(final int camera_driving, final int gallery_driving) {
        final Dialog dialog = new Dialog(TradesmanSignup2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choosemedia);
        dialog.setTitle("Upload Image");

        ImageView CameraImage = dialog.findViewById(R.id.CameraImage);
        ImageView GalleryImage = dialog.findViewById(R.id.GalleryImage);

        CameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri1 = getOutputMediaFileUri(1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
                startActivityForResult(cameraIntent, camera_driving);
                dialog.dismiss();
            }
        });

        GalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), gallery_driving);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        catarray.clear();
        if (InternetDetect.isConnected(getApplicationContext())) {
            AsyncCategory asyncCategory = new AsyncCategory();
            asyncCategory.execute();
        } else {}

        try {
            fddf2 = getSets.get(position).getArrayList();
        } catch (Exception e) {}

        if (fddf2 == null) {
            return;
        } else if (ImageUpload_details == null) {
            return;
        } else if (ImageUpload_drivnig ==null) {
            return;
        } else if (cheak_houry_charges.isChecked() || hourly_charges_client.isChecked()) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
            boolean isError = false;
            Appconstants.selecttrade = selecttradeTV.getText().toString();
            Appconstants.TradesmanUsername = TradesmanUsername.getText().toString();
            Appconstants.TradesmanPassword = TradesmanPassword.getText().toString();
            Appconstants.TradesmanConfirmPassword = TradesmanConfirmPassword.getText().toString();
            Appconstants.CompanyDetail = ETCompanyDetail.getText().toString();

            if (Appconstants.afterhours.equals("")) {
                return;
            } else if (Appconstants.selecttrade.equals("") || Appconstants.selecttrade.equals("SELECT YOUR TRADES")) {
                return;
            } else if (Appconstants.doyouwork.equals("")) {
                return;
            } else if (IsTermsOpen) {
                return;
            } else if (Appconstants.TradesmanUsername.equals("")) {
                return;
            } else if (Appconstants.TradesmanPassword.equals("")) {
                return;
            } else if (Appconstants.TradesmanPassword.length() < 6) {
                return;
            } else if (Appconstants.TradesmanConfirmPassword.equals("")) {
                return;
            } else if (!Appconstants.TradesmanPassword.equals(Appconstants.TradesmanConfirmPassword)) {
                return;
            } else {
                Toast.makeText(this, "Your profile is completed", Toast.LENGTH_LONG).show();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            }
        } else {
            Toast.makeText(this, "Upload Step Completed", Toast.LENGTH_LONG).show();
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        }

    }

    private void gotoAddProfileImage(final int camera, final int gallery) {

        final Dialog dialog = new Dialog(TradesmanSignup2.this);
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

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case camera: {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    String path = getRealPathFromURI(getImageUri(mContext, thumbnail));

                    Compress.get(mContext).setQuality(20)
                            .execute(new Compress.onSuccessListener() {
                                @Override
                                public void response(boolean status, String message, File file) {
                                    profileCompLogo = file.getAbsolutePath();
                                    Log.e("sadasadasd", "ImageUpload_details = " + ImageUpload_details);
                                    BTUploadPhoto.setBackgroundResource(border_tourtoise_solide);
                                    BTUploadPhoto.setText("Image Upload");
                                }
                            }).CompressedImage(path);
                    break;
                }

                case gallery: {
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String FinalPath = cursor.getString(columnIndex);
                    cursor.close();
                    profileCompLogo = FinalPath;
                    Log.e("sadasadasd", "ImageUpload_details = " + ImageUpload_details);
                    BTUploadPhoto.setBackgroundResource(border_tourtoise_solide);
                    BTUploadPhoto.setText("Image Upload");
                    Log.e("PATH", "" + FinalPath);
                    break;
                }

                case gallery3:
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String FinalPath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e("PATH", "" + FinalPath);
                    Intent intent = new Intent(TradesmanSignup2.this, CropActivity.class);
                    intent.putExtra("ImagePath", FinalPath);
                    startActivityForResult(intent, 1000);
                    Toast.makeText(this, "Select image 1", Toast.LENGTH_SHORT).show();
                    break;

                case camera3:
                    Intent intent2 = new Intent(TradesmanSignup2.this, CropActivity.class);
                    intent2.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent2, 1000);
                    break;

                case 1000:
                    ImageUpload = data.getStringExtra("CroppedImage");
                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    Uri uri21 = FileProvider.getUriForFile(TradesmanSignup2.this,
                            getApplicationContext().getPackageName() + ".provider", new File(ImageUpload));

                    File imgFile = new File(ImageUpload);
                    Log.e("imagefile2", "" + imgFile);
                    Bitmap bm210 = null;
                    try {
                        bm210 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri21);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao210 = new ByteArrayOutputStream();
                    bm210.compress(Bitmap.CompressFormat.JPEG, 90, bao210);
                    byte[] ba21 = bao210.toByteArray();
                    Appconstants.IMAGE_BASE642 = Base64Decode.encodeBytes(ba21);
                    Log.e("bitmap gallery", Appconstants.IMAGE_BASE642);

                    your_array_list.add(new File(ImageUpload));
                    Log.e("SELETED IMG====", your_array_list + "");
                    // customImageAdapter = new HorizontalRecyclerView(getSets, TradesmanSignup2.this, arraylist_file);
                    try {
                        customImageAdapter.setImageInItem(position, bm210, ImageUpload, your_array_list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("asdfasfasfasd", "fddf = " + fddf2);
                    Log.e("asdfasfasfasd", "ImageUpload_details = " + ImageUpload_details);
                    Log.e("asdfasfasfasd", "ImageUpload_drivnig = " + ImageUpload_drivnig);

                    try {
                        fddf2 = getSets.get(position).getArrayList();
                    } catch (Exception e) {
                    }

                    if (fddf2 == null) {
                        return;
                    } else if (ImageUpload_details == null) {
                        return;
                    } else if (ImageUpload_drivnig == null) {
                        return;
                    } else if (cheak_houry_charges.isChecked() || hourly_charges_client.isChecked()) {
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        boolean isError = false;
                        Appconstants.selecttrade = selecttradeTV.getText().toString();
                        Appconstants.TradesmanUsername = TradesmanUsername.getText().toString();
                        Appconstants.TradesmanPassword = TradesmanPassword.getText().toString();
                        Appconstants.TradesmanConfirmPassword = TradesmanConfirmPassword.getText().toString();
                        Appconstants.CompanyDetail = ETCompanyDetail.getText().toString();

                        if (Appconstants.afterhours.equals("")) {
                            return;
                        } else if (Appconstants.selecttrade.equals("") || Appconstants.selecttrade.equals("SELECT YOUR TRADES")) {
                            return;
                        } else if (Appconstants.doyouwork.equals("")) {
                            return;
                        } else if (IsTermsOpen) {
                            return;
                        } else if (Appconstants.TradesmanUsername.equals("")) {
                            return;
                        } else if (Appconstants.TradesmanPassword.equals("")) {
                            return;
                        } else if (Appconstants.TradesmanPassword.length() < 6) {
                            return;
                        } else if (Appconstants.TradesmanConfirmPassword.equals("")) {
                            return;
                        } else if (!Appconstants.TradesmanPassword.equals(Appconstants.TradesmanConfirmPassword)) {
                            return;
                        } else {
                            Toast.makeText(this, "Your profile is completed", Toast.LENGTH_LONG).show();
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                        }
                    } else {
                        Toast.makeText(this, "Upload Step Completed", Toast.LENGTH_LONG).show();
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    }

                    break;

                case gallery_driving:
                    selectedImageDriving = data.getData();
                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                    Cursor cursor1 = getContentResolver().query(selectedImageDriving, filePathColumn1, null, null, null);
                    cursor1.moveToFirst();
                    int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                    String FinalPath1 = cursor1.getString(columnIndex1);
                    cursor1.close();
                    Log.e("PATH", "" + FinalPath1);
                    Intent intent12 = new Intent(TradesmanSignup2.this, CropActivity.class);
                    intent12.putExtra("ImagePath", FinalPath1);
                    startActivityForResult(intent12, 2000);
                    Toast.makeText(this, "Select image 1", Toast.LENGTH_SHORT).show();
                    break;

                case camera_driving:
                    Intent intent33 = new Intent(TradesmanSignup2.this, CropActivity.class);
                    intent33.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent33, 2000);
                    break;

                case 2000:
                    ImageUpload_drivnig = data.getStringExtra("CroppedImage");
                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    Uri uri212 = FileProvider.getUriForFile(TradesmanSignup2.this,
                            getApplicationContext().getPackageName() + ".provider", new File(ImageUpload_drivnig));
                    Log.e("Driving_licence", "???" + ImageUpload_drivnig);

                    File imgFile2 = new File(ImageUpload_drivnig);
                    Log.e("imagefile2", "" + imgFile2);
                    Bitmap bm212 = null;
                    try {
                        bm212 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri212);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao212 = new ByteArrayOutputStream();
                    bm212.compress(Bitmap.CompressFormat.JPEG, 90, bao212);
                    byte[] ba212 = bao212.toByteArray();
                    Appconstants.IMAGE_BASE642 = Base64Decode.encodeBytes(ba212);

                    Log.e("SELETED Drivingd====", ImageUpload_drivnig + "");
                    BTUploadCompanyDetail1.setBackgroundResource(border_tourtoise_solide);
                    BTUploadCompanyDetail1.setText("Image Upload");
                    BTUploadCompanyDetail1.setTextColor(R.color.white);

                    Log.e("asdfasfasfasd", "fddf = " + fddf2);
                    Log.e("asdfasfasfasd", "ImageUpload_details = " + ImageUpload_details);
                    Log.e("asdfasfasfasd", "ImageUpload_drivnig = " + ImageUpload_drivnig);

                    try {
                        fddf2 = getSets.get(position).getArrayList();
                    } catch (Exception e) {
                    }

                    if (fddf2 == null) {
                        return;
                    } else if (ImageUpload_details == null) {
                        return;
                    } else if (ImageUpload_drivnig == null) {
                        return;
                    } else if (cheak_houry_charges.isChecked() || hourly_charges_client.isChecked()) {
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        boolean isError = false;
                        Appconstants.selecttrade = selecttradeTV.getText().toString();
                        Appconstants.TradesmanUsername = TradesmanUsername.getText().toString();
                        Appconstants.TradesmanPassword = TradesmanPassword.getText().toString();
                        Appconstants.TradesmanConfirmPassword = TradesmanConfirmPassword.getText().toString();
                        Appconstants.CompanyDetail = ETCompanyDetail.getText().toString();

                        if (Appconstants.afterhours.equals("")) {
                            return;
                        } else if (Appconstants.selecttrade.equals("") || Appconstants.selecttrade.equals("SELECT YOUR TRADES")) {
                            return;
                        } else if (Appconstants.doyouwork.equals("")) {
                            return;
                        } else if (IsTermsOpen) {
                            return;
                        } else if (Appconstants.TradesmanUsername.equals("")) {
                            return;
                        } else if (Appconstants.TradesmanPassword.equals("")) {
                            return;
                        } else if (Appconstants.TradesmanPassword.length() < 6) {
                            return;
                        } else if (Appconstants.TradesmanConfirmPassword.equals("")) {
                            return;
                        } else if (!Appconstants.TradesmanPassword.equals(Appconstants.TradesmanConfirmPassword)) {
                            return;
                        } else {
                            Toast.makeText(this, "Your profile is completed", Toast.LENGTH_LONG).show();
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                        }
                    } else {
                        Toast.makeText(this, "Upload Step Completed", Toast.LENGTH_LONG).show();
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    }

                    break;

                case gallery2:
                    selected_details = data.getData();
                    String[] filePathColumn12 = {MediaStore.Images.Media.DATA};
                    Cursor cursor12 = getContentResolver().query(selected_details, filePathColumn12, null, null, null);
                    cursor12.moveToFirst();
                    int columnIndex12 = cursor12.getColumnIndex(filePathColumn12[0]);
                    String FinalPath23 = cursor12.getString(columnIndex12);
                    cursor12.close();
                    Log.e("PATH", "" + FinalPath23);
                    Intent intent121 = new Intent(TradesmanSignup2.this, CropActivity.class);
                    intent121.putExtra("ImagePath", FinalPath23);
                    startActivityForResult(intent121, 3000);
                    Toast.makeText(this, "Select image 1", Toast.LENGTH_SHORT).show();

                    break;

                case camera2:
                    Intent intent331 = new Intent(TradesmanSignup2.this, CropActivity.class);
                    intent331.putExtra("ImagePath", fileUriPath);
                    startActivityForResult(intent331, 3000);
                    break;

                case 3000:
                    ImageUpload_details = data.getStringExtra("CroppedImage");

                    Appconstants.companyDesRatio = data.getStringExtra("Ratio");
                    Uri uri2120 = FileProvider.getUriForFile(TradesmanSignup2.this,
                            getApplicationContext().getPackageName() + ".provider", new File(ImageUpload_details));
                    Log.e("ImageUpload_details", "???" + ImageUpload_details);

                    File imgFile25 = new File(ImageUpload_details);
                    Log.e("ImageUpload_details", "" + imgFile25);
                    Bitmap bm2120 = null;
                    try {
                        bm2120 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2120);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bao2120 = new ByteArrayOutputStream();
                    bm2120.compress(Bitmap.CompressFormat.JPEG, 90, bao2120);
                    byte[] ba2120 = bao2120.toByteArray();
                    Appconstants.IMAGE_BASE642 = Base64Decode.encodeBytes(ba2120);
                    BTUploadCompanyDetail.setBackgroundResource(border_tourtoise_solide);
                    BTUploadCompanyDetail.setText("Image Upload");
                    BTUploadCompanyDetail.setTextColor(R.color.white);

                    ETCompanyDetail.setFocusableInTouchMode(false);
                    ETCompanyDetail.setFocusable(false);
                    PhotoOrString = "Photo";

                    Log.e("asdfasfasfasd", "fddf = " + fddf2);
                    Log.e("asdfasfasfasd", "ImageUpload_details = " + ImageUpload_details);
                    Log.e("asdfasfasfasd", "ImageUpload_drivnig = " + ImageUpload_drivnig);

                    try {
                        fddf2 = getSets.get(position).getArrayList();
                    } catch (Exception e) {
                    }

                    if (fddf2 == null) {
                        return;
                    } else if (ImageUpload_details == null) {
                        return;
                    } else if (ImageUpload_drivnig == null) {
                        return;
                    } else if (cheak_houry_charges.isChecked() || hourly_charges_client.isChecked()) {
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        boolean isError = false;
                        Appconstants.selecttrade = selecttradeTV.getText().toString();
                        Appconstants.TradesmanUsername = TradesmanUsername.getText().toString();
                        Appconstants.TradesmanPassword = TradesmanPassword.getText().toString();
                        Appconstants.TradesmanConfirmPassword = TradesmanConfirmPassword.getText().toString();
                        Appconstants.CompanyDetail = ETCompanyDetail.getText().toString();

                        if (Appconstants.afterhours.equals("")) {
                            return;
                        } else if (Appconstants.selecttrade.equals("") || Appconstants.selecttrade.equals("SELECT YOUR TRADES")) {
                            return;
                        } else if (Appconstants.doyouwork.equals("")) {
                            return;
                        } else if (IsTermsOpen) {
                            return;
                        } else if (Appconstants.TradesmanUsername.equals("")) {
                            return;
                        } else if (Appconstants.TradesmanPassword.equals("")) {
                            return;
                        } else if (Appconstants.TradesmanPassword.length() < 6) {
                            return;
                        } else if (Appconstants.TradesmanConfirmPassword.equals("")) {
                            return;
                        } else if (!Appconstants.TradesmanPassword.equals(Appconstants.TradesmanConfirmPassword)) {
                            return;
                        } else {
                            Toast.makeText(this, "Your profile is completed", Toast.LENGTH_LONG).show();
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                        }
                    } else {
                        Toast.makeText(this, "Upload Step Completed", Toast.LENGTH_LONG).show();
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    }
                    break;
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        fileUriPath = getOutputMediaFile(type).getPath();
        Uri uri = FileProvider.getUriForFile(TradesmanSignup2.this, getApplicationContext().getPackageName() + ".provider", new File(fileUriPath));
        return uri;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public void captureImage(int listItemPosition, String s) {
        position = listItemPosition;
        imageTempName = s;
        gotoAddProfileImage(camera3, gallery3);
    }

    private class AsyncCategory extends AsyncTask<String, Void, String> {

        String id = "", name = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            catarray.clear();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpPAth.Urlpath + "category");
            try {
                HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    CategoryBean categoryBean = new CategoryBean();

                    id = jsonArray.getJSONObject(i).getString("id");
                    name = jsonArray.getJSONObject(i).getString("name");
                    categoryBean.setId(id);
                    categoryBean.setName(name);
                    catarray.add(categoryBean);

                }

            } catch (Exception e) {
                System.out.println("json category error+++" + e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Collections.sort(catarray, new Comparator<CategoryBean>() {
                @Override
                public int compare(CategoryBean v1, CategoryBean v2) {
                    return v1.getName().compareTo(v2.getName());
                }
            });

            mainCategoryArray = null;
            posID = null;
            mainCategoryArray = new String[catarray.size()];
            posID = new String[catarray.size()];

            int index = 0;
            for (CategoryBean value : catarray) {
                mainCategoryArray[index] = value.getName();
                posID[index] = value.getId();

                index++;
                System.out.println("Main category" + value.getName());
            }
        }
    }

    private class JsonGCMTask extends AsyncTask<String, String, String> {

        String msg = "";

        @Override
        protected String doInBackground(String... strings) {
            try {

                if (gcm == null) {

                    gcm = GoogleCloudMessaging.getInstance(TradesmanSignup2.this);
                }

                RegId = gcm.register("741851403308");

                return RegId;

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

                new JsonGCMTask().execute();

            } else {

            }
        }

    }

    private class ListViewAdapter extends BaseAdapter {

        private final ArrayList<String> data;
        Context mContext;
        LayoutInflater inflater;

        public ListViewAdapter(Context context, ArrayList<String> data) {

            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.select_problem_row_list, null);
                holder.ProblemNameTV = view.findViewById(R.id.ProblemNameTV);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.ProblemNameTV.setText(data.get(position));
            holder.ProblemNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.ProblemNameTV.isChecked()) {
                        holder.ProblemNameTV.setChecked(false);
                        Selecteddata.remove(data.get(position));

                    } else {

                        holder.ProblemNameTV.setChecked(true);
                        Selecteddata.add(data.get(position));

                    }
                }
            });

            return view;
        }


        public class ViewHolder {
            CheckedTextView ProblemNameTV;
        }
    }
}