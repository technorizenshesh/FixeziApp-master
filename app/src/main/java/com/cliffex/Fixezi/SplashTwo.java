package com.cliffex.Fixezi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import org.ankit.perfectdialog.EasyDialog;
import org.ankit.perfectdialog.EasyDialogListener;
import org.ankit.perfectdialog.Icon;
import org.json.JSONObject;
import java.util.List;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class SplashTwo extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback {

    private static final int REQUEST_CODE_PERMISSION = 2;
    TextView ContinueSTTV;
    JSONObject data = null;
    String request_id;
    int REQUEST_CHECK_SETTINGS = 100;
    String[] mPermission = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WAKE_LOCK
            , Manifest.permission.VIBRATE};
    LocationManager locationManager;
    boolean GpsStatus;
    private AlertDialog gpsAlertDialog;
    private boolean isGpsDialogShowing = false;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private TextView tvs;
    private PendingResult<LocationSettingsResult> result;
    private Intent intent1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Criteria criteria;
    private List<String> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_two);

        GPSStatus();

        getGPSLocationDailoge();

        // getLocationDialog();

        ContinueSTTV = findViewById(R.id.ContinueSTTV);

        if (getIntent().getExtras() != null) {
            new EasyDialog.Builder(this)
                    .setTitle("Alert!")
                    .setSubtitle("You have 1 new Job Request")
                    .isCancellable(true)
                    .setCancelBtnColor("#000000")
                    .setConfirmBtnColor("#fd3621")
                    .setIcon(Icon.INFO)
                    .setConfirmBtn("yes", new EasyDialogListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setCancelBtn("no", new EasyDialogListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).build();
        }

        ContinueSTTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashTwo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        try {
//
//            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
//                    != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, mPermission[1])
//                            != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, mPermission[2])
//                            != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, mPermission[3])
//                            != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, mPermission[4])
//                            != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, mPermission[5])
//                            != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, mPermission[6])
//                            != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        mPermission, REQUEST_CODE_PERMISSION);
//
//            } else {
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void GPSStatus() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void getGPSLocationDailoge() {

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                dialogBuilder.setView(inflater.inflate(R.layout.costumdailoge, null));
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvs = alertDialog.findViewById(R.id.allow_once);
                TextView tvs1 = alertDialog.findViewById(R.id.allow_once);
                TextView tvs2 = alertDialog.findViewById(R.id.allow_once);

                tvs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(SplashTwo.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        alertDialog.dismiss();
                    }
                });
                tvs1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(SplashTwo.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                        alertDialog.dismiss();
                    }
                });

                tvs2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(SplashTwo.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                         alertDialog.dismiss();
                    }
                });

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }


        } else {
            Toast.makeText(this, "Now Permission Granted  ", Toast.LENGTH_SHORT).show();
        }

      /*  if (GpsStatus == true) {
            Toast.makeText(this, "enable gps ", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            dialogBuilder.setView(inflater.inflate(R.layout.costumdailoge, null));
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            TextView tvs = alertDialog.findViewById(R.id.allow_once);
            tvs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void removeGpsDialog() {
        if (gpsAlertDialog != null && gpsAlertDialog.isShowing()) {
            gpsAlertDialog.dismiss();
            isGpsDialogShowing = false;
            gpsAlertDialog = null;
        }
    }

    private void ShowGpsDialog() {
        isGpsDialogShowing = true;
        AlertDialog.Builder gpsBuilder = new AlertDialog.Builder(
                SplashTwo.this);
        gpsBuilder.setCancelable(false);
        gpsBuilder.setTitle("getString(R.string.dialog_no_gps)")
                .setMessage("getString(R.string.dialog_no_gps_messgae)")
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                removeGpsDialog();
                            }
                        })

                .setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeGpsDialog();
                                finish();
                            }
                        });

        gpsAlertDialog = gpsBuilder.create();
        gpsAlertDialog.show();

    }

   /* private void getGPSLocationDailoge() {


    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeGpsDialog();
    }

    private void getLocationDialog() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {

            if (grantResults.length == 7 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(SplashTwo.this, "Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();

                        //Request location updates:
                       // locationManager.requestLocationUpdates(, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        tvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.build().describeContents();
                builder.setAlwaysShow(true);
                result = LocationServices.SettingsApi.checkLocationSettings(
                                mGoogleApiClient,
                                builder.build());
            }
        });
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onResult(@NonNull Result result) {

        final Status status = result.getStatus();

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(SplashTwo.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }

}
