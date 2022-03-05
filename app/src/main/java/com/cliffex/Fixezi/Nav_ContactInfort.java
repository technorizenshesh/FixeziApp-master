package com.cliffex.Fixezi;

import android.content.Intent;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Nav_ContactInfort extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    private EditText et_msg;
    private TextView tv_sendmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__contact_infort);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // ******for Key board hiding
        findID();
        img_back.setOnClickListener(this);
        tv_sendmsg.setOnClickListener(this);
    }

    private void findID() {
        img_back= (ImageView) findViewById(R.id.img_back);
        et_msg= (EditText) findViewById(R.id.et_msg);
        tv_sendmsg= (TextView) findViewById(R.id.tv_sendmsg);
    }

    @Override
    public void onClick(View v) {
        if (v==img_back) {
            finish();
        }else if (v==tv_sendmsg) {
            String message=et_msg.getText().toString();
            if (message == null || message.equals("")) {
                et_msg.setError("Please Enter Message");
            } else {
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"fixeziteam@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                emailIntent.setSelector(emailSelectorIntent);

                if(emailIntent.resolveActivity(getPackageManager())!=null)
                    startActivity(emailIntent);
            }

        }

    }



}
