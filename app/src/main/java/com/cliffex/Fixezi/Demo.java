package com.cliffex.Fixezi;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Demo extends AppCompatActivity {

    TextView TESTBT;
    View ViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        TESTBT = (TextView) findViewById(R.id.TESTBT);

    }
}
