package com.cliffex.Fixezi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Splash extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    AlertDialog dialog;
    SessionUser sessionUser;
    SessionTradesman sessionTradesman;
    SessionWorker sessionWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionUser = new SessionUser(Splash.this);
        sessionTradesman = new SessionTradesman(Splash.this);
        sessionWorker = new SessionWorker(Splash.this);

//        if(ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this,
//                        android.Manifest.permission.INTERNET)
//                        != PackageManager.PERMISSION_GRANTED||
//                ContextCompat.checkSelfPermission(this,
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED||
//                ContextCompat.checkSelfPermission(this,
//                        android.Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED||
//                ContextCompat.checkSelfPermission(this,
//                        android.Manifest.permission.ACCESS_NETWORK_STATE)
//                        != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WAKE_LOCK)) {
//                    go_next();
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.INTERNET,
//                                Manifest.permission.ACCESS_NETWORK_STATE,
//                                Manifest.permission.WAKE_LOCK
//                        },
//                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
//            }
//        } else {
//            go_next();
//            Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT).show();
//        }

    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if(requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                go_next();
//            } else {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
//                builder.setMessage(R.string.permission_required)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // FIRE ZE MISSILES!
//                                openPermissionScreen();
//                            }
//                        }).setNegativeButton(R.string.mdtp_cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                                dialog.dismiss();
//                            }
//                        });
//                dialog = builder.show();
//            }
//            return;
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            go_next();
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED  &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions (
                this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_FIELS
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_next();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setMessage(R.string.permission_required)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                openPermissionScreen();
                            }
                        }).setNegativeButton(R.string.mdtp_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
                dialog = builder.show();
            }
        }
    }

    public void go_next() {

        if(sessionUser.IsLoggedIn()) {
            Intent intent = new Intent(Splash.this, UserActivity.class);
            startActivity(intent);
            finish();
        } else if(sessionTradesman.IsLoggedIn()) {
            Intent intent = new Intent(Splash.this, TradesmanActivity.class);
            startActivity(intent);
            finish();
        } else if(sessionWorker.IsLoggedIn()) {
            Intent intent = new Intent(Splash.this, EmployeeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Splash.this, SplashTwo.class);
            startActivity(intent);
            finish();
        }
    }

    public void openPermissionScreen() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", Splash.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }

}
