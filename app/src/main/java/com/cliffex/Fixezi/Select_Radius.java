package com.cliffex.Fixezi;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.security.AccessControlContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class Select_Radius extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private static final int MAX = 20;
    private static final int MIN = 10;
    private GoogleMap mMap;
    private Circle circle;
    private TextView distance;
    private Toolbar ToolbarSU;
    private ImageView img_back;
    private Select_Radius mContext;
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
    private int progress3;
    private int max;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__radius);
        getCurrentLocation();

        img_back = findViewById(R.id.img_back);
        done_location_text = findViewById(R.id.done_location_text);

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
                Select_radius = PreferenceConnector.readString(Select_Radius.this, PreferenceConnector.Select_radius, "");
                Select_address_Raedus = PreferenceConnector.readString(Select_Radius.this, PreferenceConnector.Select_address_Raedus, "");
                Intent intent = new Intent();
                intent.putExtra("Select_radius", "" + progress3);
                intent.putExtra("Select_address_Raedus", Select_address_Raedus);
                setResult(2, intent);
                finish();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCurrentLocation() {
        mContext = this;
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
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

                    text_location = findViewById(R.id.text_location);
                    text_location.setText("" + address);

                    PreferenceConnector.writeString(Select_Radius.this, PreferenceConnector.Select_address_Raedus, address);

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
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
            try {} catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LatLng propertyLatlng = new LatLng(latitude, longitude);
            MapsInitializer.initialize(Objects.<Context>requireNonNull(this));
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
        }

        progress = findViewById(R.id.progress);
        distance = findViewById(R.id.distance);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude)))).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(address))
                .setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.c));

        Log.e("sdfsdfdsfdsf","(15 * 21) / 100 = " + (15 * 21) / 100);

//        progress.setMax(100);
//        distance.setText("" + 0 + "km ");
//        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                float finalZoom = (progress * 21) / 100;
//                distance.setText("" + progress + "km ");
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(finalZoom));
//                mMap.moveCamera(CameraUpdateFactory.zoomTo(finalZoom));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//
//        });

         progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress1 = 30 - progress;
                circle.setRadius(getRedious(progress));

                Log.e("CircleRedius", progress + "==>" + circle.getRadius());
                Log.e("progress1", progress1 + "==>" + circle.getRadius());

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) progress1));

                if(progress1 == 30) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 17));
                }
                if(progress1 == 29) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 16));
                }
                if(progress1 == 28) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 15));
                }
                if(progress1 == 27) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 14));
                }
                if(progress1 == 26) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 14));
                }
                if(progress1 == 25) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 13.50));
                }
                if(progress1 == 24) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 13.10));
                }
                if(progress1 == 23) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 12.85));
                }
                if(progress1 == 22) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 12.10));
                }
                if(progress1 == 21) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 11.85));
                }
                if(progress1 == 20) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 10.85));
                }

                if(progress1 == 19) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 9.85));
                }

                if(progress1 == 18) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 9.45));
                }

                if(progress1 == 17) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 9.08));
                }

                if(progress1 == 16) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 9.01));
                }

                if(progress1 == 15) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 9.35));
                }

                if(progress1 == 14) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 8.35));
                }

                if(progress1 == 13) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 7.35));
                }

                if(progress1 == 12) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 7.15));
                }

                if(progress1 == 11) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 7.01));
                }

                if(progress1 == 10) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), (float) 7));
                }
                distance.setText("" + progress * 5 + "km ");
                progress3 = progress * 5;
                PreferenceConnector.writeString(Select_Radius.this, PreferenceConnector.Select_radius, "" + progress * 5);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {}

        });

    }

    private LatLngBounds getLatLngBoundsFromCircle(Circle circle) {

        if (circle != null) {
            return new LatLngBounds.Builder()
                    .include(SphericalUtil.computeOffset(circle.getCenter(), circle.getRadius() * Math.sqrt(2), 45))
                    .include(SphericalUtil.computeOffset(circle.getCenter(), circle.getRadius() * Math.sqrt(2), 225))
                    .build();
        }
        return null;
    }

    private double getRedious(int progress) {

        if (progress == 0) {
            return 100;
        }
        if (progress == 1) {
            return 300;
        }
        if (progress == 2) {
            return 500;
        }
        if (progress == 3) {
            return 700;
        }
        if (progress == 4) {
            return 900;
        }
        if (progress == 5) {
            return 1200;
        }
        if (progress == 6) {
            return 1500;
        }
        if (progress == 7) {
            return 1800;
        }
        if (progress == 8) {
            return 3700;
        }
        if (progress == 9) {
            return  4000;
        }
        if (progress == 10) {
            return 5000;
        }
        if (progress == 11) {
            return 12000;
        }
        if (progress == 12) {
            return 24000;
        }
        if (progress == 13) {
            return 26000;
        }
        if (progress == 14) {
            return 32000;
        }
        if (progress == 15) {
            return 35000;
        }
        if (progress == 16) {
            return 49000;
        }
        if (progress == 17) {
            return 110000;
        }
        if (progress == 18) {
            return 130000;
        }
        //yha se
        if (progress == 19) {
            return 150000;
        }
        if (progress == 20) {
            return 170000;
        }
        return 800;
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}
