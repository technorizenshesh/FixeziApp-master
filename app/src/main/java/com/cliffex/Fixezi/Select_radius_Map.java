package com.cliffex.Fixezi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.cliffex.Fixezi.MyUtils.InternetDetect;

public class Select_radius_Map extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_radius__map);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


    }
}
