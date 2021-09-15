package com.cliffex.Fixezi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by technorizen8 on 13/4/16.
 */
public class InvoicePage extends AppCompatActivity {
    Toolbar invoices_pager_toolbar;
    TextView toolbar_title;
    GridView InvoiceGridView;
    ArrayList<String> ImageFilePath = new ArrayList<String>();// list of file paths
    File[] listFile;
    ListViewAdapter listViewAdapter;
    RelativeLayout NavigationUpIM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_pages);
        getFromSdcard();

        invoices_pager_toolbar=(Toolbar)findViewById(R.id.invoices_pager_toolbar);
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        InvoiceGridView=(GridView)findViewById(R.id.InvoiceGridView);
        NavigationUpIM=(RelativeLayout)findViewById(R.id.NavigationUpIM);

        toolbar_title.setText("Invoices");
        setSupportActionBar(invoices_pager_toolbar);
        NavigationUpIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

            if(ImageFilePath.size()>0)
            {
                Log.e("SIZE","????"+ImageFilePath.size());
                 listViewAdapter=new ListViewAdapter(InvoicePage.this,ImageFilePath);
                InvoiceGridView.setAdapter(listViewAdapter);
            }else{

                Log.e("Else","????"+ImageFilePath.size());
            }
    }


    public void getFromSdcard()
    {
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "/FIXEZI/Invoices");

        if (file.isDirectory()) {
            listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++) {

                ImageFilePath.add(listFile[i].getAbsolutePath());
            }
        }
    }

    public void deleetefile(String path)
    {
        File file = new File(path);
        file.delete();
        if(file.exists()){
            try {
                file.getCanonicalFile().delete();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.exists()){
                getApplicationContext().deleteFile(file.getName());
            }
        }
    }


    public class ListViewAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        private ArrayList<String> ImagePath;


        public ListViewAdapter(Context context,ArrayList<String> ImagePath) {


            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.ImagePath = ImagePath;


        }

        public class ViewHolder {

            ImageView InvoiceImage;
        }

        @Override
        public int getCount() {
            return ImagePath.size();
        }

        @Override
        public String getItem(int position) {
            return ImagePath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.invoice_rowitem, null);
                holder.InvoiceImage = (ImageView) view.findViewById(R.id.InvoiceImage);

                view.setTag(holder);


            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.InvoiceImage.setImageURI(Uri.fromFile(new File(ImagePath.get(position))));



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(InvoicePage.this, FullScreenActivity.class);
                    intent.putStringArrayListExtra("ImageArrayList", ImagePath);
                    intent.putExtra("position", position);
                    startActivityForResult(intent,12);

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InvoicePage.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    builder.setMessage("Are you sure you want to delete this image?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleetefile(ImagePath.get(position));
                                    ImagePath.remove(position);
                                    notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();

                    return true;
                }
            });



            return view;
        }

    }

}
