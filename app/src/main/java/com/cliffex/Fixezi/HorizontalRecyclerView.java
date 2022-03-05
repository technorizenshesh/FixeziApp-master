package com.cliffex.Fixezi;

import android.content.Context;
import com.cliffex.Fixezi.MyUtils.InternetDetect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HorizontalRecyclerView extends RecyclerView.Adapter<HorizontalRecyclerView.HorizontalViewHolder> {

    List<GetSet> _data;
    Context _c;
    public ArrayList<File> arraylist_file;
    private String data_image;
    private ArrayList<String> your_array_list= new ArrayList<String>();


    public HorizontalRecyclerView(List<GetSet> getData, Context context, ArrayList arrayList) {
        _data = getData;
        _c = context;
        arraylist_file = arrayList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.parcel_images, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holdar, int position) {
        final GetSet dataSet = (GetSet) _data.get(position);
        dataSet.setListItemPosition(position);

        if(!dataSet.isHaveImage()) {
            Bitmap icon = BitmapFactory.decodeResource(_c.getResources(), R.drawable.full);
            holdar.imageView.setImageBitmap(icon);
        } else {
            holdar.imageView.setImageBitmap(dataSet.getImage());
        }

        holdar.parcelName.setText(dataSet.getLabel());
        holdar.label.setText(dataSet.getSubtext());

        if(dataSet.isStatus()) {
            holdar.clickImage.setVisibility(View.VISIBLE);
            holdar.removeImage.setVisibility(View.GONE);
        } else {
            holdar.removeImage.setVisibility(View.VISIBLE);
            holdar.clickImage.setVisibility(View.GONE);
        }

        holdar.clickImage.setFocusable(false);
        holdar.removeImage.setFocusable(false);
        holdar.clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TradesmanSignup2) _c).captureImage(dataSet.getListItemPosition(), dataSet.getLabel() + "" + dataSet.getSubtext());
            }
        });

        holdar.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.setStatus(true);
                dataSet.setHaveImage(false);
                notifyDataSetChanged();
            }
        });
    }

    public void setImageInItem(int position, Bitmap imageSrc, String imagePath, ArrayList<File> your_array_list) {
        GetSet dataSet = (GetSet) _data.get(position);
        Log.e("bitmapimage",""+ imagePath);
        dataSet.setImage(imageSrc);
        dataSet.setImagePath(imagePath);
        dataSet.setArrayList(your_array_list);
        dataSet.setStatus(false);
        dataSet.setHaveImage(true);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private final ImageView clickImage,removeImage,imageView;
        private final TextView parcelName,label;
        private final TextView text;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            clickImage = (ImageView) itemView.findViewById(R.id.capture);
            removeImage = (ImageView) itemView.findViewById(R.id.cancel);
            parcelName = (TextView) itemView.findViewById(R.id.parcelName);
            label = (TextView) itemView.findViewById(R.id.imageFor);
            imageView = (ImageView) itemView.findViewById(R.id.imgPrv);

            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}