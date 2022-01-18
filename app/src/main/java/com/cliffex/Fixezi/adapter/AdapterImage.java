package com.cliffex.Fixezi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cliffex.Fixezi.R;
import java.io.File;
import java.util.ArrayList;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ImageMyViewHolder> {

    Context mContext;
    ArrayList<File> fileList;
    ImageFileInterface imageFileInterface;

    public AdapterImage(Context mContext, ArrayList<File> fileList,ImageFileInterface imageFileInterface) {
        this.mContext = mContext;
        this.fileList = fileList;
        this.imageFileInterface = imageFileInterface;
    }

    @NonNull
    @Override
    public ImageMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapters_image, parent, false);
        return new ImageMyViewHolder(view);
    }

    public interface ImageFileInterface {
        void onSucuss(ArrayList<File> file);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageMyViewHolder holder, int position) {

        File file = fileList.get(position);

        holder.ivImage.setImageURI(Uri.parse(file.getPath()));

        holder.ivRemoved.setOnClickListener(v -> {
            fileList.remove(position);
            imageFileInterface.onSucuss(fileList);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return fileList == null ? 0 : fileList.size();
    }


    public class ImageMyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage, ivRemoved;

        public ImageMyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivRemoved = itemView.findViewById(R.id.ivRemoved);
        }

    }

}
