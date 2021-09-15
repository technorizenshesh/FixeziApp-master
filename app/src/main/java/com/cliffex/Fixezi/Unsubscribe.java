package com.cliffex.Fixezi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Unsubscribe extends AppCompatActivity {

    Toolbar ToolbarUnsubscribe;
    TextView toolbar_title, AppreciateTV, ClickUnsubscribeTV, ThankYouTV;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe);

        ToolbarUnsubscribe = (Toolbar) findViewById(R.id.ToolbarUnsubscribe);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        NavigationUpIM = (RelativeLayout) findViewById(R.id.NavigationUpIM);
        AppreciateTV = (TextView) findViewById(R.id.AppreciateTV);
        ThankYouTV = (TextView) findViewById(R.id.ThankYouTV);
        ClickUnsubscribeTV = (TextView) findViewById(R.id.ClickUnsubscribeTV);

        toolbar_title.setText("Unsubscribe");
        setSupportActionBar(ToolbarUnsubscribe);

        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ClickUnsubscribeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Unsubscribe.this);
                builder1.setMessage("Are you sure");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

    }
}
