package com.cliffex.Fixezi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.cliffex.Fixezi.Constant.PreferenceConnector;
import com.cliffex.Fixezi.MyUtils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.security.AccessControlContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.security.AccessController.getContext;

//import com.google.android.maps.GeoPoint;

public class DemoUpdate extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Circle circle;
    private TextView distance;
    private Toolbar ToolbarSU;
    private ImageView img_back;
    private DemoUpdate mContext;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private String address;
    private TextView text_location;
    private TextView done_location_text;
    private String Select_adress;
    private String Select_address_Raedus;
    private CircleOptions circleOptions;
    private SeekBar progress;
    private double radius123;
    private Camera camera;
    private int progress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__radius);
        getCurrentLocation();

        img_back = (ImageView) findViewById(R.id.img_back);
        done_location_text = (TextView) findViewById(R.id.done_location_text);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        done_location_text.setOnClickListener(new View.OnClickListener() {

            private String Select_radius;

            @Override
            public void onClick(View view) {

                Select_radius = PreferenceConnector.readString(DemoUpdate.this, PreferenceConnector.Select_radius, "");
                Select_address_Raedus = PreferenceConnector.readString(DemoUpdate.this, PreferenceConnector.Select_address_Raedus, "");
                Intent intent = new Intent();
                intent.putExtra("Select_radius", Select_radius);
                intent.putExtra("Select_address_Raedus", Select_address_Raedus);
                setResult(2, intent);
                finish();
                Toast.makeText(mContext, Select_radius, Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCurrentLocation() {

        mContext = this;

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]

                    {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);


        } else {

            gps = new GPSTracker(this);

            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                Log.e("latitude", String.valueOf(latitude));
                Log.e("longitude", String.valueOf(longitude));

                Geocoder geocoder;
                List<Address> addresses = null;

                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    text_location = (TextView) findViewById(R.id.text_location);
                    text_location.setText("" + address);

                    PreferenceConnector.writeString(DemoUpdate.this, PreferenceConnector.Select_address_Raedus, address);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            } else {
                gps.showSettingsAlert();
            }
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MapsInitializer.initialize(Objects.<Context>requireNonNull(this));
        }

        LatLng propertyLatlng = new LatLng(latitude, longitude);

        try {
            circleOptions = new CircleOptions()
                    .center(propertyLatlng)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(5)
                    .radius(800)
                    .fillColor(Color.TRANSPARENT);

            circle = mMap.addCircle(circleOptions);

        } catch (Exception e) {

        }

        progress = (SeekBar) findViewById(R.id.progress);
        distance = (TextView) findViewById(R.id.distance);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude)))).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(address))
                .setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.c));

        final Matrix matrix = null;

        final Matrix finalMatrix = matrix;

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

             /*   int mProgress=21-progress;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), (float)mProgress));
                circle.setRadius(progress * 23);*/
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude))))
                        .zoom(progress)
                        .bearing(1)
                        .build();

                //int mProgress = 21 - progress;
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), (float)mProgress));

                circle.setRadius(progress * 23);
                progress1 = progress;
                Log.e("progress1", String.valueOf(progress1));

                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {

                        camera = Camera.open();
                        Camera.Parameters param = camera.getParameters();
                        try {
                            circle.setRadius(progress1 * 8);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        int zoomLevel = 14;

                        if(progress1 >= 10) {

                            try {
                               /* Log.e("zoommap", String.valueOf(progress1));

                                distance.setText("" + progress1 + "km ");*/

                                zoomLevel--;
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude))))
                                        .zoom(zoomLevel)
                                        .bearing(1)
                                        .build();

                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title(address))
                                        .setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.c));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                circle.setRadius(progress1 * 23);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            Log.e("zoommap1", String.valueOf(progress1));

                            zoomLevel++;

                        }

                        try{

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude))))
                                    .zoom(zoomLevel)
                                    .bearing(100)
                                    .build();
                            mMap.setMaxZoomPreference(progress1 * 15);
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(address))
                                    .setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.c));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        PreferenceConnector.writeString(DemoUpdate.this, PreferenceConnector.Select_radius, String.valueOf(progress1));

                        return null;
                    }
                }.execute();
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });

    }

    private BitmapDescriptor bitmapDescriptorFromVector(AccessControlContext context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_locationmarker);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(getApplicationContext(), vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public float getZoomLevel(Circle circle) {

        int zoomLevel = 14;

        if (circle != null) {


            if (circle.getRadius() >= 80) {

                /*circle.getRadius();*/

              /*  double radius = circle.getRadius() + circle.getRadius() / 2;
                double scale = radius / 500;
                zoomLevel = (int) (21+ Math.log(scale) / Math.log(2));*/

                zoomLevel--;
            }


   /*         double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (21+ Math.log(scale) / Math.log(2));

*//*            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10),1000,null);*//*

            Log.e("zoom_level", "" + zoomLevel);*/
        }

        return zoomLevel;

        /*return zoomLevel + .4f;*/

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }
}
